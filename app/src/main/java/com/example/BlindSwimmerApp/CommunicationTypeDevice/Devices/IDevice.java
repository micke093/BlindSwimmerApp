package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

import android.content.Context;

public interface IDevice<E> {

    String getName();

    String getAddress();

    void set(E deviceToBeAdded);

    String toString();

    E getConnection(Context context, E callbacks);
}
