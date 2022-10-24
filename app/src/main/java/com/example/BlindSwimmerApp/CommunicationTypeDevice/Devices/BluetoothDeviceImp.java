package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class BluetoothDeviceImp implements IDevice {

    private BluetoothDevice device;

    @Override
    public String getName() {
        if (device != null){ return device.getName(); }
        return null;
    }

    @Override
    public String getAddress() {
        if (device != null){ return device.getAddress(); }
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BluetoothDeviceImp that = (BluetoothDeviceImp) o;
        return Objects.equals(device, that.device);
    }
}
