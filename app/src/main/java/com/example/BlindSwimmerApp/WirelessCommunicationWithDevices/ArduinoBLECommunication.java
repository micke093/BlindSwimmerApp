package com.example.BlindSwimmerApp.WirelessCommunicationWithDevices;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.BluetoothDeviceImp;

import java.util.List;

public class ArduinoBLECommunication implements IDeviceCommunication {

    private static final String TAG = "ArduinoBLECommunication";

    private final String arduinoUuid = "19b10001-e8f2-537e-4f6c-d104768a1214";
    private BluetoothGatt gatt = null;
    //TODO change name to selectedArduinoService
    private BluetoothGattService gattService = null;

    /**
    * Writes information to a Arduino Nano 33 with BLE
    * */
    @Override
    public void WriteToDevice(Object dataToSend) {
        Log.d(TAG, "Data to send: " + dataToSend.toString() + " Gatt service: " + gattService.getUuid());
        if(gattService != null){
            BluetoothGattCharacteristic characteristic =
                    gatt.getService(gattService.getUuid()).getCharacteristic(gattService.getUuid());
            characteristic.setValue(dataToSend.toString());
            Callbacks().onCharacteristicWrite(gatt,characteristic,1);
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
            gattService = arduinoService;
            this.gatt = gatt;
            Log.d(TAG, "DeviceDiscovered: Gatt: " + this.gatt.toString());
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
                    ArduinoBLECommunication.this.gatt = null;
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
                Log.d(TAG, characteristic.getUuid().toString());
                gatt.writeCharacteristic(characteristic);
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
        BluetoothDeviceImp temp = (BluetoothDeviceImp) device;
        gatt = (BluetoothGatt) temp.getConnection(context, Callbacks());
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
