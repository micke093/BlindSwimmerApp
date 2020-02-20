package com.example.BlindSwimmerApp.CommunicationTypeDevice;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

public class BluetoothImp implements ICommunicationTypeDevice {

    /**
     *
     **/

    @Override
    public String actionFound() {
        return BluetoothDevice.ACTION_FOUND;
    }

    @Override
    public String extraDevice() {
        return BluetoothDevice.EXTRA_DEVICE;
    }
}
