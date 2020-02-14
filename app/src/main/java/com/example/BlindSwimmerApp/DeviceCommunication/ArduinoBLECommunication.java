package com.example.BlindSwimmerApp.DeviceCommunication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class ArduinoBLECommunication implements IDeviceCommunication {

    private static final String TAG = "ArduinoBLECommunication";

    private BluetoothGatt gatt = null;

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
                //TODO change so it is not hardcoded.
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

    public BluetoothGattCallback Callbacks() {
        return new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int State) {
                if (State == BluetoothGatt.STATE_CONNECTED) {
                    Log.d(TAG, "Connected to device");
                    ArduinoBLECommunication.this.gatt = gatt;
                    gatt.discoverServices();
                } else if (State == BluetoothGatt.STATE_DISCONNECTED) {
                    ArduinoBLECommunication.this.gatt = null;
                    Log.d(TAG, "Device disconnected");
                }
            }

            @Override
            public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                DeviceDiscovered(gatt);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.d(TAG, characteristic.getUuid().toString());
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.d(TAG, "onCharacteristicRead: " + ReadFromDevice(characteristic));
            }
        };
    }

    @Override
    public boolean connectToDevice(Object device, Context context) {
        gatt = ((BluetoothDevice) device).connectGatt(context, false, Callbacks());
        Log.d(TAG, "connect: connectGatt called");
        //TODO check if successful
        return true;
    }

    @Override
    public boolean disconnectFromDevice() {
        if (gatt != null) {
            gatt.close();
        }
        //TODO check if successful
        return true;
    }
}
