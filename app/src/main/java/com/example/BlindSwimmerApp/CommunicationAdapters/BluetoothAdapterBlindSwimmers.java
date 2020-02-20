package com.example.BlindSwimmerApp.CommunicationAdapters;

import android.bluetooth.BluetoothAdapter;

public class BluetoothAdapterBlindSwimmers implements ICommunicationAdapter {

    private BluetoothAdapter ba;

    public BluetoothAdapterBlindSwimmers(){
        ba = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean startDiscoveryOfWirelessDevices() {
        return ba.startDiscovery();
    }

    @Override
    public boolean isDiscoveringWirelessDevices() {
        return ba.isDiscovering();
    }

    @Override
    public boolean cancelDiscoveryOfWirelessDevices() {
        return ba.cancelDiscovery();
    }

    @Override
    public boolean isReadyToBeUsed() {
        return ba.isEnabled();
    }

    @Override
    public String actionRequestEnable() {
        return BluetoothAdapter.ACTION_REQUEST_ENABLE;
    }
}
