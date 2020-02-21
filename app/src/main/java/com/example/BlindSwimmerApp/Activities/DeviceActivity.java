package com.example.BlindSwimmerApp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.IDevice;
import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.ConnectedDevice;
import com.example.BlindSwimmerApp.R;
import com.example.BlindSwimmerApp.WirelessCommunicationWithDevices.ArduinoBLECommunication;
import com.example.BlindSwimmerApp.WirelessCommunicationWithDevices.IDeviceCommunication;

import java.util.concurrent.TimeUnit;

/**
 * This is where we manage the BLE device and the corresponding services, characteristics et c.
 * <p>
 * NB: In this simple example there is no other way to turn off notifications than to
 * leave the activity (the BluetoothGatt is disconnected and closed in activity.onStop).
 */
public class DeviceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "DeviceActivity";

    private TextView deviceName;
    private EditText textInputBluetoothBeaconOne;
    private EditText textInputBluetoothBeaconTwo;

    private Button backButton;
    private Button trainButton;
    private Button submitSensorButton;
    private Button readFromConnectedDeviceButton;

    private IDeviceCommunication deviceCommunication = null;
    private IDevice connectedDevice = null;

    @Override
    public void onClick(View v) {

        if(v == trainButton) {
            deviceCommunication.writeToDevice(deviceCommunication.getChangeModeToTrainMode());
            startActivity(new Intent(DeviceActivity.this, TrainActivity.class));
        }

        else if(v == backButton) {
            deviceCommunication.disconnectFromDevice();
            connectedDevice = null;
            startActivity(new Intent(DeviceActivity.this, MainActivity.class));
        }
        else if(v == readFromConnectedDeviceButton) deviceCommunication.startAsynchronousReadFromSelectedDevice();
        else if(v == submitSensorButton) {
            String sensorInputOne = textInputBluetoothBeaconOne.getText().toString();
            String sensorInputTwo = textInputBluetoothBeaconTwo.getText().toString();

            /*
            * Set the names of the two bluetooth beacons the device should listen for
            * */
            if(!sensorInputOne.isEmpty() && !sensorInputTwo.isEmpty()) {

                deviceCommunication.writeToDevice(deviceCommunication.getBluetoothBeaconOneSetName() + sensorInputOne);
                //Wait to send second sensor name since device can't read if sent to fast.

                try { TimeUnit.MILLISECONDS.sleep(150); }
                catch (InterruptedException e) { e.printStackTrace(); }

                deviceCommunication.writeToDevice(deviceCommunication.getBluetoothBeaconTwoSetName() + sensorInputTwo);
                //TODO maybe implement confirmation on sensor name update.
            }
            else { showToast("Please enter sensor name"); }
        }
    }

    //============================ PRIVATE FUNCTIONS =========================================
    private void connect() {
        if (connectedDevice != null) { deviceCommunication.connectToDevice(connectedDevice, this); }
    }

    //==================== SETUP FUNCTIONS FOR ANDROID APPLICATION ====================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        trainButton = findViewById(R.id.train_button);
        trainButton.setOnClickListener(this);
        backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(this);
        submitSensorButton = findViewById(R.id.submitSensorButton);
        submitSensorButton.setOnClickListener(this);
        readFromConnectedDeviceButton = findViewById(R.id.read_data_button);
        readFromConnectedDeviceButton.setOnClickListener(this);

        deviceName = findViewById(R.id.textDeviceName);
        textInputBluetoothBeaconOne = findViewById(R.id.editTextSensorOne);
        textInputBluetoothBeaconTwo = findViewById(R.id.editTextSensorTwo);

        connectedDevice = ConnectedDevice.getInstance();
        deviceCommunication = ArduinoBLECommunication.getInstance();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Connected device is: " + connectedDevice.getName());
        if (connectedDevice != null) {
            if(deviceName != null)
            {
                deviceName.setText(connectedDevice.getName());
                if(connectedDevice.getName() == null) deviceName.setText("No set name (null)");
                Log.d(TAG, "Device name is: " + deviceName);
                connect();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deviceCommunication.disconnectFromDevice();
        connectedDevice = null;
    }

    protected void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
