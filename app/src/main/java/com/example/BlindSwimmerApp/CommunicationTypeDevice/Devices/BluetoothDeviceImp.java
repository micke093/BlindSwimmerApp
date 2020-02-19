package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;

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
        device = (BluetoothDevice) deviceToBeAdded;
    }

    @Override
    public String toString(){
        return device.toString();
    }

    @Override
    public Object getConnection(Context context, Object callbacks) {
        return device.connectGatt(context, false, (BluetoothGattCallback) callbacks);
    }
}
