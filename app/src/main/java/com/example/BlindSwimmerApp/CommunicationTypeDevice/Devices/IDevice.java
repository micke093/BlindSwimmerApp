package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

public interface IDevice<E> {

    String getName();

    String getAddress();

    E get();

    void set(E deviceToBeAdded);

    void remove();

    String toString();
}
