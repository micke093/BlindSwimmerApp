package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceImp implements IDevice {

    private BluetoothDevice device;

    @Override
    public String getName() {
        if (device != null){
            return device.getName();
        }
        return null;
    }

    @Override
    public String getAddress() {
        if (device != null){
            return device.getAddress();
        }
        return null;
    }

    @Override
    public void set(Object deviceToBeAdded) {
        if(deviceToBeAdded instanceof BluetoothDevice){
            device = (BluetoothDevice) deviceToBeAdded;
        }
    }
}
