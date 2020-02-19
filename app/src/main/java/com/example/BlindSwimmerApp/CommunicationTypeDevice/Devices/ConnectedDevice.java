package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

import android.util.Log;

/**
 * An ugly hack to administrate the selected Bluetooth device
 * between activities.
 */
public class ConnectedDevice {

    private final static String TAG = "ConnectedDevice";
    private static IDevice theDevice = null;
    private static final Object lock = new Object();

    private ConnectedDevice() { }

    public static IDevice getInstance() {
        synchronized(lock) {
            return theDevice;
        }
    }

    public static void setInstance(IDevice newDevice) {
        synchronized(lock) {
            theDevice = newDevice;
            Log.d(TAG, "setInstance: New device" + theDevice.getName());
        }
    }

    public static void removeInstance() {
        synchronized(lock) {
            theDevice = null;
        }
    }
}