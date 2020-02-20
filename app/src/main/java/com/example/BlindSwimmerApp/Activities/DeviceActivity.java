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
    //TODO rename to more appropriate name, since we do not actually train in the activity.
    private Button trainButton;
    private Button submitSensorButton;
    private Button readFromConnectedDeviceButton;

    private IDeviceCommunication deviceCommunication = null;
    private IDevice connectedDevice = null;

    @Override
    public void onClick(View v) {

        if(v == trainButton) {
            startActivity(new Intent(DeviceActivity.this, TrainActivity.class));
            deviceCommunication.WriteToDevice(deviceCommunication.getChangeModeToTrainMode());
            //TODO send mode change to device
        }

        else if(v == backButton) startActivity(new Intent(DeviceActivity.this, MainActivity.class));

        else if(v == readFromConnectedDeviceButton) deviceCommunication.startAsynchronousReadFromSelectedDevice();

        else if(v == submitSensorButton)
        {
            String sensorInputOne = textInputBluetoothBeaconOne.getText().toString();
            String sensorInputTwo = textInputBluetoothBeaconTwo.getText().toString();

            /*
            * Set the names of the two bluetooth beacons the device should listen for
            * */
            if(!sensorInputOne.isEmpty() && !sensorInputTwo.isEmpty())
            {
                Log.d(TAG, "onClick: Sensor one: " + sensorInputOne + " Sensor two: " + sensorInputTwo);
                deviceCommunication.WriteToDevice(deviceCommunication.getBluetoothBeaconOneSetName() + sensorInputOne);
                //Wait to send second sensor name since device can't read if sent to fast.
                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                deviceCommunication.WriteToDevice(deviceCommunication.getBluetoothBeaconTwoSetName() + sensorInputTwo);
                //TODO maybe implement confirmation on sensor name update.
            }
            else
            { showToast("Please enter sensor name"); }
        }
    }

    //========================= Private classes =========================================

    private void connect() {
        if (connectedDevice != null) {
            Log.d(TAG, "Connect: Connected device is: " + connectedDevice.getName());
            deviceCommunication.connectToDevice(connectedDevice, this);
        }
    }

    //============== SETUP FUNCTION FOR ANDROID APPLICATION =============================
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

        deviceCommunication = new ArduinoBLECommunication();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        connectedDevice = ConnectedDevice.getInstance();
        Log.d(TAG, "onStart: Connected device is: " + connectedDevice.getName());
        if (connectedDevice != null) {
            if(deviceName != null)
            {
                deviceName.setText(connectedDevice.getName());
                if(connectedDevice.getName() == null)
                {
                    deviceName.setText("No set name (null)");
                }
                Log.d(TAG, "Device name är: " + deviceName);
                connect();
            }
            else
            { showToast("mDeviceView == null"); }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        deviceCommunication.disconnectFromDevice();
        connectedDevice = null;
        finish();
    }

    protected void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
