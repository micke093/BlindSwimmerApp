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

    private final String arduinoUuid = "19b10001-e8f2-537e-4f6c-d104768a1214";
    private BluetoothGatt selectedGattDevice = null;
    private BluetoothGattService selectedArduinoService = null;

    /**
    * Writes information to a Arduino Nano 33 with BLE
    * */
    @Override
    public void WriteToDevice(Object dataToSend) {
        Log.d(TAG, "Gatt service: " + selectedArduinoService.getUuid() + ", Data to send: " + dataToSend.toString());
        if(selectedArduinoService != null){
            BluetoothGattCharacteristic characteristic =
                    selectedGattDevice.getService(selectedArduinoService.getUuid()).getCharacteristic(selectedArduinoService.getUuid());
            characteristic.setValue(dataToSend.toString());
            selectedGattDevice.writeCharacteristic(characteristic);
        }
        else{
            Log.d(TAG, "WriteToDevice: Gatt service is null");
        }
    }

    @Override
    //TODO change so it reads from an array and return the values from there
    public String ReadFromDevice(Object receivedData) {
        Log.d(TAG, receivedData.toString());
        /*BluetoothGattCharacteristic rd = (BluetoothGattCharacteristic) receivedData;
        byte[] bytesReceived = rd.getValue();

        StringBuilder strReceived = new StringBuilder();
        for (byte b : bytesReceived) {
            strReceived.append((char) b);
        }

        Log.d(TAG, "characteristic: "  + strReceived.toString());
        Log.d(TAG, rd.getUuid().toString());
        return  strReceived.toString();*/
        return "";
    }

    private void SetSelectedArduinoService(BluetoothGatt gatt){
        Log.d(TAG, "Services discovered");
        BluetoothGattService arduinoService = null;
        List<BluetoothGattService> services = gatt.getServices();

        Log.d(TAG, "Number of services found: " + services.size());

        for (BluetoothGattService service : services) {
            String uuid = service.getUuid().toString();
            Log.d(TAG, "service: " + uuid);

            //Unique for every Arduino
            //TODO change so it is not hardcoded. Maybe get and store the UUID when specifying sensors with phone
            //maybe a list of uuid or a standardised naming convention
            if(uuid.equals(arduinoUuid))
            {
                arduinoService = service;
                Log.d(TAG, "Found matching uuid");
                break;
            }
        }

        if(arduinoService == null)
        {
            Log.d(TAG, "Could not find a matching service UUID name.");
        }
        else {
            selectedArduinoService = arduinoService;
            this.selectedGattDevice = gatt;
            Log.d(TAG, "DeviceDiscovered: Gatt: " + this.selectedGattDevice.toString());
        }
    }

    public BluetoothGattCallback Callbacks() {
        return new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int state) {
                if (state == BluetoothGatt.STATE_CONNECTED || state == BluetoothGatt.STATE_CONNECTING) {
                    Log.d(TAG, "Connecting to device: " + gatt.getDevice().getName());
                    //Async function to discover the service on the remote device
                    //triggers the onServicesDiscovered callback method.
                    gatt.discoverServices();

                } else if (state == BluetoothGatt.STATE_DISCONNECTED || state == BluetoothGatt.STATE_DISCONNECTING) {
                    ArduinoBLECommunication.this.selectedGattDevice = null;
                    Log.d(TAG, "Device disconnected");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                Log.d(TAG, "Number of service discovered: " + gatt.getServices().size());
                ArduinoBLECommunication.this.SetSelectedArduinoService(gatt);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                Log.d(TAG, "Message sent to device: " + characteristic.getUuid().toString()
                        + ", Message was: " + characteristic.getStringValue(0));
            }

            @Override
            //TODO change so it reads from characteristics and adds the values to an array
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

                Log.d(TAG, "onCharacteristicRead: " + ReadFromDevice(characteristic));
            }
        };
    }

    @Override
    public boolean connectToDevice(Object device, Context context) {
        selectedGattDevice = ((BluetoothDevice) device).connectGatt(context, false, Callbacks());
        Log.d(TAG, "connect: connectGatt called");
        //TODO check if successful
        return true;
    }

    @Override
    public boolean disconnectFromDevice() {
        if (selectedGattDevice != null) {
            selectedGattDevice.close();
        }
        //TODO check if successful
        return true;
    }
}
