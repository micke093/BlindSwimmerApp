package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

public interface IDevice<E> {

    String getName();

    String getAddress();

    void set(E deviceToBeAdded);
}
