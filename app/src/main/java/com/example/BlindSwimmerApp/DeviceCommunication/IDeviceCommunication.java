package com.example.BlindSwimmerApp.DeviceCommunication;

import android.content.Context;

public interface IDeviceCommunication<E> {

    void WriteToDevice(E dataToSend);

    String ReadFromDevice(E receivedData);

    void DeviceDiscovered(E discoveredDevice);

    E Callbacks();

    boolean connectToDevice(E device, Context context);

    boolean disconnectFromDevice();
}