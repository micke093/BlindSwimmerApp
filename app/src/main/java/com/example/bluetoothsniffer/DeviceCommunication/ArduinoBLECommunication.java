package com.example.bluetoothsniffer.DeviceCommunication;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

public class ArduinoBLECommunication implements IDeviceCommunication {

    private static final String TAG = "ArduinoBLECommunication";

    /*
    * Writes information to a Arduino Nano 33 with BLE
    * */
    @Override
    public boolean WriteToDevice(Object dataToSend) {
        BluetoothGattCharacteristic sd = (BluetoothGattCharacteristic) dataToSend;
        Log.d(TAG, sd.getUuid().toString());
        return true;
    }

    @Override
    public String ReadFromDevice(Object receivedData) {
        BluetoothGattCharacteristic rd = (BluetoothGattCharacteristic) receivedData;
        byte[] bytesReceived = rd.getValue();

        StringBuilder strReceived = new StringBuilder();
        for (byte b : bytesReceived) {
            strReceived.append((char) b);
        }

        Log.d(TAG, "characteristic: "  + strReceived.toString());
        Log.d(TAG, rd.getUuid().toString());
        return  strReceived.toString();
    }


    @Override
    public void ShowToast(Object dataToDisplay) {

    }
}
