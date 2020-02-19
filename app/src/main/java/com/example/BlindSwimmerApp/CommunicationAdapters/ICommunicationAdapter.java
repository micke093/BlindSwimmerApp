package com.example.BlindSwimmerApp.CommunicationAdapters;

public interface ICommunicationAdapter {

    boolean startDiscovery();

    boolean isDiscovering();

    boolean cancelDiscovery();

    boolean isEnabled();

    String actionRequestEnable();
}
