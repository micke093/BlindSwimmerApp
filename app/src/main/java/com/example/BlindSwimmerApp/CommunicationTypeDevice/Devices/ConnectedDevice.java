package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

/**
 * An ugly hack to administrate the selected Bluetooth device
 * between activities.
 */
public class ConnectedDevice {

    private static IDevice theDevice = null;
    private static final Object lock = new Object();

    private ConnectedDevice() {
    }

    public static IDevice getInstance() {
        synchronized(lock) {
            return theDevice;
        }
    }

    public static void setInstance(IDevice newDevice) {
        synchronized(lock) {
            theDevice = newDevice;
        }
    }

    public static void removeInstance() {
        synchronized(lock) {
            theDevice = null;
        }
    }
}