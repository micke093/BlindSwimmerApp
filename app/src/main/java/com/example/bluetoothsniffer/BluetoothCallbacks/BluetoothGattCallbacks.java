package com.example.bluetoothsniffer.BluetoothCallbacks;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import com.example.bluetoothsniffer.DeviceCommunication.ArduinoNano33BLECommunication;
import com.example.bluetoothsniffer.DeviceCommunication.IDeviceCommunication;

import java.util.List;

public class BluetoothGattCallbacks {

    private BluetoothGattCallback bluetoothGattCallback;
    private static final String TAG = "BluetoothGattCallbacks";
    private BluetoothGatt mBluetoothGatt = null;
    private IDeviceCommunication deviceCommunication;


    public BluetoothGattCallbacks (){
        deviceCommunication = new ArduinoNano33BLECommunication();
           bluetoothGattCallback = new BluetoothGattCallback() {
               @Override
               public void onConnectionStateChange(BluetoothGatt gatt, int status, int State) {
                   if (State == BluetoothGatt.STATE_CONNECTED) {
                       mBluetoothGatt = gatt;
                       gatt.discoverServices();
                   } else if (State == BluetoothGatt.STATE_DISCONNECTED){
                       mBluetoothGatt = null;
                   }
               }

               @Override
               public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
                   if (status == BluetoothGatt.GATT_SUCCESS) {

                       // debug, list services

                       BluetoothGattService arduinoServices = null;
                       List<BluetoothGattService> services = gatt.getServices();
                       for (BluetoothGattService service : services) {
                           String uuid = service.getUuid().toString();
                           Log.d(TAG, "service: " + uuid);

                           //Unique for every Arduino
                           //TODO
                           //make it not reliant on a hardcoded uuid.
                           if(uuid.equals("19b10001-e8f2-537e-4f6c-d104768a1214"))
                           {
                               arduinoServices = service;
                           }
                       }

                       if(arduinoServices == null)
                       {
                           Log.d(TAG, "Could not find a matching service UUID name..");
                       }
                       else
                       {
                           BluetoothGattCharacteristic characteristic = gatt.getService(arduinoServices.getUuid()).getCharacteristic(arduinoServices.getUuid());
                           gatt.readCharacteristic(characteristic);
                       }
                   }
               }

               @Override
               public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                   Log.d(TAG, characteristic.getUuid().toString());
               }

               @Override
               public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                   Log.d(TAG, "onCharacteristicRead: " + deviceCommunication.ReadFromDevice(characteristic));
               }
           };
    }

    public BluetoothGattCallback getBluetoothGattCallback() {
        return bluetoothGattCallback;
    }

    public BluetoothGatt getmBluetoothGatt() {
        return mBluetoothGatt;
    }
}
