package com.example.BlindSwimmerApp.CommunicationAdapters;

import android.bluetooth.BluetoothAdapter;

public class BluetoothAdapterBlindSwimmers implements ICommunicationAdapter {

    private BluetoothAdapter ba;

    public BluetoothAdapterBlindSwimmers(){
        ba = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean startDiscovery() {
        return ba.startDiscovery();
    }

    @Override
    public boolean isDiscovering() {
        return ba.isDiscovering();
    }

    @Override
    public boolean cancelDiscovery() {
        return ba.cancelDiscovery();
    }

    @Override
    public boolean isEnabled() {
        return ba.isEnabled();
    }

    @Override
    public String actionRequestEnable() {
        return BluetoothAdapter.ACTION_REQUEST_ENABLE;
    }
}
