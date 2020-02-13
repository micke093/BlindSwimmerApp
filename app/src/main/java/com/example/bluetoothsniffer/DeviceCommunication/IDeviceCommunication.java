package com.example.bluetoothsniffer.DeviceCommunication;

public interface IDeviceCommunication<E> {

    boolean WriteToDevice(E dataToSend);

    String ReadFromDevice(E receivedData);

    void ShowToast(E dataToDisplay);
}