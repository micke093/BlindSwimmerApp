package com.example.BlindSwimmerApp.CommunicationTypeDevice;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

public class BluetoothImp implements ICommunicationTypeDevice {

    private final static String TAG = "BluetoothDevice";

    @Override
    public boolean foundCorrectAction(Intent intent) {
        String action = intent.getAction();
        return BluetoothDevice.ACTION_FOUND.equals(action);
    }

    @Override
    public int getRSSIValueFromIntent(Intent intent) {
        int RSSI = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
        Log.d(TAG, "onReceive: " + RSSI);
        return RSSI;
    }

    @Override
    public String actionFound() {
        return BluetoothDevice.ACTION_FOUND;
    }

    @Override
    public String extraDevice() {
        return BluetoothDevice.EXTRA_DEVICE;
    }
}
