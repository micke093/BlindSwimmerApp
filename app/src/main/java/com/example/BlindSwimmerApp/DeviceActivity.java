package com.example.BlindSwimmerApp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BlindSwimmerApp.DeviceCommunication.ArduinoBLECommunication;
import com.example.BlindSwimmerApp.DeviceCommunication.IDeviceCommunication;

/**
 * This is where we manage the BLE device and the corresponding services, characteristics et c.
 * <p>
 * NB: In this simple example there is no other way to turn off notifications than to
 * leave the activity (the BluetoothGatt is disconnected and closed in activity.onStop).
 */
public class DeviceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MYTAG";

    private TextView mDeviceName;
    private TextView mDeviceOtherInfo;
    private TextView mDeviceRSSI;
    private EditText mTextInputSensorOne;
    private EditText mTextInputSensorTwo;

    private Button backButton;
    private Button trainButton;
    private Button submitSensorButton;

    private BluetoothDevice mConnectedDevice = null;
    private BluetoothGatt mBluetoothGatt = null;

    private Handler mHandler;

    private IDeviceCommunication deviceCommunication = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        mConnectedDevice = ConnectedDevice.getInstance();
        if (mConnectedDevice != null) {
            if(mDeviceName != null)
            {
                mDeviceName.setText(mConnectedDevice.getName());
                if(mConnectedDevice.getName() == null)
                {
                    mDeviceName.setText("No set name (null)");
                }
                //get the RSSI value
                mDeviceRSSI.setText("" + getIntent().getExtras().getInt("RSSIValue", 0));
                String deviceName = (String) getIntent().getExtras().get("deviceName");
                System.out.println("Device name är: " + deviceName);
                mDeviceOtherInfo.setText("None");
                connect();
            }
            else
            {
                showToast("mDeviceView == null");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
        }
        ConnectedDevice.removeInstance();
        mConnectedDevice = null;
        finish();
    }

    private void connect() {
        if (mConnectedDevice != null) {
            // register call backs for bluetooth gatt
            mBluetoothGatt = mConnectedDevice.connectGatt(this, false, mBtGattCallback);
            Log.d(TAG, "connect: connctGatt called");
        }
    }

    /**
     * Callbacks for bluetooth gatt changes/updates
     * The documentation is not clear, but (some of?) the callback methods seems to
     * be executed on a worker thread - hence use a Handler when updating the ui.
     */
    private BluetoothGattCallback mBtGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                mBluetoothGatt = gatt;
                gatt.discoverServices();
                mHandler.post(new Runnable() {
                    public void run() {
                        //mDataView.setText("Connected");
                        showToast("Connected to device");
                    }
                });
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // close connection and display info in ui
                mBluetoothGatt = null;
                mHandler.post(new Runnable() {
                    public void run() {
                        //mDataView.setText("Disconnected");
                        showToast("Device disconnected");
                    }
                });
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            deviceCommunication.DeviceDiscovered(gatt);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, characteristic.getUuid().toString());
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicRead: " + deviceCommunication.ReadFromDevice(characteristic));
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        System.out.println("DeviceActivity");

        trainButton = findViewById(R.id.train_button);
        mDeviceName = findViewById(R.id.textDeviceName);
        mDeviceOtherInfo = findViewById(R.id.textOther);
        mDeviceRSSI = findViewById(R.id.textDeviceRSSI);
        mTextInputSensorOne = findViewById(R.id.editTextSensorOne);
        mTextInputSensorTwo = findViewById(R.id.editTextSensorTwo);

        submitSensorButton = findViewById(R.id.submitSensorButton);
        backButton = findViewById(R.id.buttonBack);
        backButton.setText("Back");

        submitSensorButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        trainButton.setOnClickListener(this);

        mHandler = new Handler();

        deviceCommunication = new ArduinoBLECommunication();
    }

    protected void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onClick(View v) {

        if(v==trainButton){
            startActivity(new Intent(DeviceActivity.this, TrainActivity.class));
        }

        else if(v==backButton){
            startActivity(new Intent(DeviceActivity.this, MainActivity.class));
        }

        else if(v==submitSensorButton)
        {
            String sensorInputOne = mTextInputSensorOne.getText().toString();
            String sensorInputTwo = mTextInputSensorTwo.getText().toString();

            if(!sensorInputOne.isEmpty() && !sensorInputTwo.isEmpty())
            {
                //TODO
                //send sensor name to arduino
            }
            else
            {
                showToast("Please enter sensor name");
            }
        }
    }
}
