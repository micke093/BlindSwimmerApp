package com.example.BlindSwimmerApp.DeviceCommunication;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.List;

public class ArduinoBLECommunication implements IDeviceCommunication {

    private static final String TAG = "ArduinoBLECommunication";

    /**
    * Writes information to a Arduino Nano 33 with BLE
    * */
    @Override
    public void WriteToDevice(Object dataToSend) {
        BluetoothGattCharacteristic sd = (BluetoothGattCharacteristic) dataToSend;
        Log.d(TAG, sd.getUuid().toString());
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
    public void DeviceDiscovered(Object discoveredDevice) {
        BluetoothGatt gatt = (BluetoothGatt) discoveredDevice;
        if (gatt.getConnectionState(gatt.getDevice()) == BluetoothGatt.GATT_SUCCESS) {

            // debug, list services

            BluetoothGattService arduinoService = null;
            List<BluetoothGattService> services = gatt.getServices();

            for (BluetoothGattService service : services) {
                String uuid = service.getUuid().toString();
                Log.d(TAG, "service: " + uuid);

                //Unique for every Arduino
                //TODO
                //change so it is not hardcoded.
                if(uuid.equals("19b10001-e8f2-537e-4f6c-d104768a1214"))
                {
                    arduinoService = service;
                }
            }

            if(arduinoService == null)
            {
                Log.d(TAG, "Could not find a matching service UUID name..");
            }
            else
            {
                BluetoothGattCharacteristic characteristic = gatt.getService(arduinoService.getUuid()).getCharacteristic(arduinoService.getUuid());
                gatt.readCharacteristic(characteristic);
            }
        }
    }
}
