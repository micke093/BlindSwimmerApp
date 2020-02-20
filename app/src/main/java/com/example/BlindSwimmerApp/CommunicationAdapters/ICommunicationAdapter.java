package com.example.BlindSwimmerApp.CommunicationAdapters;

public interface ICommunicationAdapter {

    boolean startDiscoveryOfWirelessDevices();

    boolean isDiscoveringWirelessDevices();

    boolean cancelDiscoveryOfWirelessDevices();

    boolean isReadyToBeUsed();

    String actionRequestEnable();
}
