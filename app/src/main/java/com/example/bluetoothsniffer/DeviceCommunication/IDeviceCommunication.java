package com.example.bluetoothsniffer.DeviceCommunication;

public interface IDeviceCommunication<E> {

    void WriteToDevice(E dataToSend);

    String ReadFromDevice(E receivedData);

    void DeviceDiscovered(E discoveredDevice);
}