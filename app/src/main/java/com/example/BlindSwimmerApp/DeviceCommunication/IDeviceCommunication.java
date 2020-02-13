package com.example.BlindSwimmerApp.DeviceCommunication;

public interface IDeviceCommunication<E> {

    void WriteToDevice(E dataToSend);

    String ReadFromDevice(E receivedData);

    void DeviceDiscovered(E discoveredDevice);
}