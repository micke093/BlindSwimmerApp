package com.example.BlindSwimmerApp.WirelessCommunicationWithDevices;

import android.content.Context;

public interface IDeviceCommunication<E> {

    void WriteToDevice(E dataToSend);

    String ReadFromDevice(E receivedData);

    E Callbacks();

    boolean connectToDevice(E device, Context context);

    boolean disconnectFromDevice();
}