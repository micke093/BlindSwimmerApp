package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

public class ConnectedDevice {

    private final static String TAG = "ConnectedDevice";
    private static IDevice device = null;
    private static final Object lock = new Object();

    private ConnectedDevice() { }

    public static IDevice getInstance() {
        synchronized(lock) { return device; }
    }

    public static void setInstance(IDevice newDevice) { synchronized(lock) { device = newDevice; } }

    public static void removeInstance() {
        synchronized(lock) { device = null; }
    }
}