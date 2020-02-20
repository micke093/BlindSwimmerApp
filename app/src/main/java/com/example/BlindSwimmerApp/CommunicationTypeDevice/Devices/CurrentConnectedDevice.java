package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

import android.util.Log;

/**
 * Administration of the selected Wireless device between activities.
 */
public class CurrentConnectedDevice {

    private final static String TAG = "CurrentConnectedDevice";
    private static IDevice theDevice = null;
    private static final Object lock = new Object();

    private CurrentConnectedDevice() { }

    /**
    *Returns the current device being used
     */
    public static IDevice getInstance() {
        synchronized(lock) {
            return theDevice;
        }
    }

    /**
     *Sets the current device
     */
    public static void setInstance(IDevice newDevice) {
        synchronized(lock) {
            theDevice = newDevice;
            Log.d(TAG, "setInstance: New device" + theDevice.getName());
        }
    }

    /**
     *Removes the current device
     */
    public static void removeInstance() {
        synchronized(lock) {
            theDevice = null;
        }
    }
}