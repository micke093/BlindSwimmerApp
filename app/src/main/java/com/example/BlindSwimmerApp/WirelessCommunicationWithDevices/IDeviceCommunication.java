package com.example.BlindSwimmerApp.WirelessCommunicationWithDevices;

import android.content.Context;

public interface IDeviceCommunication<E> {
    /**
     *General methods for device communication
     **/

    void WriteToDevice(E dataToSend);

    void startAsynchronousReadFromSelectedDevice();

    String getReadDataFromDevice();

    E Callbacks();

    boolean connectToDevice(E device, Context context);

    boolean disconnectFromDevice();
}