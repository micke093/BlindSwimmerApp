package com.example.BlindSwimmerApp.CommunicationTypeDevice;

import android.content.Intent;

public interface ICommunicationTypeDevice {

    boolean foundCorrectAction(Intent intent);

    int getRSSIValueFromIntent(Intent intent);

    String actionFound();

    String extraDevice();
}
