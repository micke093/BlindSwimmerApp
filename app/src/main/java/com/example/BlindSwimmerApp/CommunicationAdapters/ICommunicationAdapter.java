package com.example.BlindSwimmerApp.CommunicationAdapters;

public interface ICommunicationAdapter {
    /**
     *Has methods for BT management
     **/

    boolean startDiscovery();

    boolean isDiscovering();

    boolean cancelDiscovery();

    boolean isEnabled();

    String actionRequestEnable();
}
