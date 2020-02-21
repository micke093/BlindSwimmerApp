package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

/**
 * An ugly hack to administrate the selected Bluetooth device between activities.
 */
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