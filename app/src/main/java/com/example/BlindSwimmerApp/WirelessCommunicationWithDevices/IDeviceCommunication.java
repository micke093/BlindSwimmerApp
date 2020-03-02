package com.example.BlindSwimmerApp.WirelessCommunicationWithDevices;

import android.content.Context;

public interface IDeviceCommunication<E>{

    boolean isConnectedToDevice();

    void writeToDevice(String dataToSend);

    void startAsynchronousReadFromSelectedDevice();

    String getReadDataFromDevice();

    E Callbacks();

    boolean connectToDevice(E device, Context context);

    boolean disconnectFromDevice();

    String CHANGE_TO_CONNECTING_MODE = "mode_0";
    String CHANGE_TO_TRAIN_MODE = "mode_1";
    String CHANGE_TO_RUNNING_MODE = "mode_2";
    String BLUETOOTH_BEACON_ONE_SET_NAME = "SN_1";
    String BLUETOOTH_BEACON_TWO_SET_NAME = "SN_2";
    String SWIMMER_TURN_SIGNAL = "T";
    String SWIMMER_SEND_TIMESTAMP = "TS_";
    String SWIMMER_CLEAR_SDCARD = "SD_CLEAR";
    String SWIMMER_PAUSE = "PAUSE";
    String HEADER_MESSAGE = "HEADER_MSG";

    String getChangeToConnectingMode();
    String getChangeToRunningMode();
    String getChangeToTrainMode();
    String getBluetoothBeaconOneSetName();
    String getBluetoothBeaconTwoSetName();
    String getSwimmerTurnSignal();
    String getSwimmerSendTimestamp();
    String getSwimmerClearSdcard();
    String getSwimmerPause();
    String getHeaderMessage();
}

