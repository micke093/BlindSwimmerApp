package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceImp implements IDevice {

    private static BluetoothDevice device;
    private static final Object lock = new Object();

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
    public Object get() {
        synchronized(lock) {
            return device;
        }
    }

    @Override
    public void set(Object deviceToBeAdded) {
        synchronized(lock) {
            if (deviceToBeAdded instanceof BluetoothDevice) {
                device = (BluetoothDevice) deviceToBeAdded;
            }
        }
    }

    @Override
    public void remove() {
        synchronized(lock) {
            device = null;
        }
    }

    @Override
    public String toString(){
        return device.toString();
    }
}
