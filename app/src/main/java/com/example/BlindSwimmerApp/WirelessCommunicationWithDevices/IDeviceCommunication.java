package com.example.BlindSwimmerApp.WirelessCommunicationWithDevices;

import android.content.Context;

public interface IDeviceCommunication<E> {

    void WriteToDevice(E dataToSend);

    void startAsynchronousReadFromSelectedDevice();

    String getReadDataFromDevice();

    E Callbacks();

    boolean connectToDevice(E device, Context context);

    boolean disconnectFromDevice();

    String CHANGE_MODE_TO_CONNECTING_MODE = "mode_0";
    String CHANGE_MODE_TO_TRAIN_MODE = "mode_1";
    String CHANGE_MODE_TO_RUNNING_MODE = "mode_2";
    String BLUETOOTH_BEACON_ONE_SET_NAME = "SN_1";
    String BLUETOOTH_BEACON_TWO_SET_NAME = "SN_2";

    String getChangeModeToConnectingMode();
    String getChangeModeToRunningMode();
    String getChangeModeToTrainMode();
    String getBluetoothBeaconOneSetName();
    String getBluetoothBeaconTwoSetName();
}