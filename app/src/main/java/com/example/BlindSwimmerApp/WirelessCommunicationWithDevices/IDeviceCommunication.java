package com.example.BlindSwimmerApp.WirelessCommunicationWithDevices;

import android.content.Context;

public interface IDeviceCommunication<E> {

    void WriteToDevice(E dataToSend);

    void startAsynchronousReadFromSelectedDevice();

    String getReadDataFromDevice();

    E Callbacks();

    boolean connectToDevice(E device, Context context);

    boolean disconnectFromDevice();
}