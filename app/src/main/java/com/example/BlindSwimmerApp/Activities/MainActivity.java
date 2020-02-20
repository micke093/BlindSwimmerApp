package com.example.BlindSwimmerApp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.CurrentConnectedDevice;
import com.example.BlindSwimmerApp.DeviceArrayAdapter;
import com.example.BlindSwimmerApp.CommunicationAdapters.BluetoothAdapterBlindSwimmers;
import com.example.BlindSwimmerApp.CommunicationAdapters.ICommunicationAdapter;
import com.example.BlindSwimmerApp.CommunicationTypeDevice.BluetoothImp;
import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.BluetoothDeviceImp;
import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.IDevice;
import com.example.BlindSwimmerApp.CommunicationTypeDevice.ICommunicationTypeDevice;
import com.example.BlindSwimmerApp.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MAIN";
    public static final int REQUEST_ENABLE_BT = 1000;
    public static final int REQUEST_ACCESS_LOCATION = 1001;
    private static final long SCAN_PERIOD = 5000;

    private ICommunicationAdapter communicationAdapter;
    private ICommunicationTypeDevice communicationDevice;

    private IDevice device;
    private ArrayList<IDevice> devices;
    private DeviceArrayAdapter arrayAdapter;

    private boolean scanning;
    private TextView mScanInfoView;
    private Handler mHandler;
    private int RSSI;

    /**
     * Requests permissions to access wireless communication on the users device
     **/
    private void initWirelessCommunication() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showToast("BLE is not supported");
            finish();
        } else {
            showToast("BLE is supported");
            // Access Location is a "dangerous" permission
            int hasAccessLocation = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasAccessLocation != PackageManager.PERMISSION_GRANTED) {
                // ask the user for permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_ACCESS_LOCATION);
                // the callback method onRequestPermissionsResult gets the result of this request
            }
        }
        // enable wireless communication alternative
        if (communicationAdapter == null || !communicationAdapter.isEnabled()) {
            assert communicationAdapter != null;
            Intent enableWirelessCommunicationIntent = new Intent(communicationAdapter.actionRequestEnable());
            startActivityForResult(enableWirelessCommunicationIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * Callback for permission request calls
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_LOCATION) {// if request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: ????
            } else {
                // stop this activity
                this.finish();
            }
        }
    }

    /**
     * Callback for request to turn on BT
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if user chooses not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Selects device on specific index in list of devices and sends the user to DeviceActivity
     **/
    private void onDeviceSelected(int position) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < devices.size(); i++) {
            sb.append(" device name: ");
            sb.append(devices.get(i).getName());
        }
        Log.d(TAG, "onDeviceSelected: List of devices" + sb.toString() + " Position selected: " + position);

        CurrentConnectedDevice.setInstance(devices.get(position));
        Log.d(TAG, "Selected device: " + CurrentConnectedDevice.getInstance().getName());

        Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
        intent.putExtra("deviceName", device.getName()); //send the device name value forward to DeviceActivity to catch
        intent.putExtra("RSSIValue", RSSI); //send the RSSI value forward to DeviceActivity to catch
        startActivity(intent);
    }

    /**
     * Scans the environment for BLE-devices
     **/
    private void scanForDevices(final boolean enable) {
        if (communicationAdapter.isDiscovering()) {
            communicationAdapter.cancelDiscovery();
        }
        if (enable) {
            if (!scanning) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (scanning) {
                            scanning = false;
                            communicationAdapter.startDiscovery();
                            showToast("BLE scan stopped");
                        }
                    }
                }, SCAN_PERIOD);
                scanning = true;
            }
        } else {
            if (scanning) {
                scanning = false;
                communicationAdapter.cancelDiscovery();
                showToast("BLE scan stopped");
            }
        }
    }

    /**
     * Below: Manage activity, and hence bluetooth, life cycle,
     * via onCreate, onStart and onStop.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();
        mScanInfoView = findViewById(R.id.scanInfo);

        communicationAdapter = new BluetoothAdapterBlindSwimmers();
        communicationDevice = new BluetoothImp();
        device = new BluetoothDeviceImp();
        devices = new ArrayList<>();

        Button startScanButton = findViewById(R.id.startScanButton);
        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devices.clear();
                scanForDevices(true);
            }
        });
        ListView scanListView = findViewById(R.id.scanListView);

        arrayAdapter = new DeviceArrayAdapter(this, devices);
        scanListView.setAdapter(arrayAdapter);
        scanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                onDeviceSelected(position);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initWirelessCommunication();
        devices.clear();
        scanForDevices(true);
        setupRegisterReceiver();
        communicationAdapter.startDiscovery();
    }

    // TODO ...
    @Override
    protected void onStop() {
        super.onStop();
        // stop scanning
        scanForDevices(false);
        devices.clear();
        arrayAdapter.notifyDataSetChanged();
        communicationAdapter.cancelDiscovery();
    }

    // short messages
    protected void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }


    /**
     *Filters BT-devices for Arduinos and puts them in a list
     **/
    private void setupRegisterReceiver() {
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                IDevice temp = new BluetoothDeviceImp();
                temp.set(intent.getParcelableExtra(communicationDevice.extraDevice()));
                if (temp.getName() != null) {
                    String deviceName = temp.getName();
                    if (!devices.contains(temp) && deviceName.startsWith("Arduino Swimmer")) {
                        devices.add(temp);
                        arrayAdapter.notifyDataSetChanged();
                        String msg = getString(R.string.found_devices_msg, devices.size());
                        mScanInfoView.setText(msg);
                        Log.d(TAG, "Swimmer found as: " + temp.getName() + ", Address: " + temp.getAddress());
                    }
                }
            }
        }, new IntentFilter(communicationDevice.actionFound()));
    }

    @Override
    public void onClick(View v) {
    }
}