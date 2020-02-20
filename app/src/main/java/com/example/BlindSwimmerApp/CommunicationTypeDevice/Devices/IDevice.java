package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

import android.content.Context;

public interface IDevice<E> {

    /**
     *General methods for devices
     **/

    String getName();

    String getAddress();

    void set(E deviceToBeAdded);

    String toString();

    E getConnection(Context context, E callbacks);
}
