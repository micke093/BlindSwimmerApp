package com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices;

import android.content.Context;

public interface IDevice<E> {

    /**
     *General methods for wireless devices
     **/

    String getName();

    String getAddress();

    void set(E deviceToBeAdded);

    String toString();

    E getConnection(Context context, E callbacks);
}
