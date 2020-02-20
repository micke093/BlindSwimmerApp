package com.example.BlindSwimmerApp.WirelessCommunicationWithDevices;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.BluetoothDeviceImp;

import java.util.ArrayList;

import java.util.List;

public class ArduinoBLECommunication implements IDeviceCommunication {

    private static final String TAG = "ArduinoBLECommunication";

    private final String arduinoUuid = "19b10001-e8f2-537e-4f6c-d104768a1214";
    private BluetoothGatt selectedGattDevice = null;
    private BluetoothGattService selectedArduinoService = null;
    private ArrayList<String> receivedData;

    public ArduinoBLECommunication() {
        receivedData = new ArrayList<>();
    }

    /**
    * Provides a way to write information to a Arduino Nano 33 with BLE.
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

     /**
     * Provides a way to read data from the selected device.
     **/
    @Override
    public void startAsynchronousReadFromSelectedDevice() {
        BluetoothGattCharacteristic characteristic =
                selectedGattDevice.getService(selectedArduinoService.getUuid()).getCharacteristic(selectedArduinoService.getUuid());
        selectedGattDevice.readCharacteristic(characteristic);
    }


    /**
     * Retruns the data read from the BTLE-device.
     * */
    @Override
    public String getReadDataFromDevice() {
        return "";
    }


    /**
     * Sets the selected ArduinoDevice as the current device
     * */
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

    /**
     * All the BT-callbacks. They handle the communication between the devices asynschronous.
     * */
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
                byte[] rd = characteristic.getValue();
                if(rd != null || rd.length != 0){
                    String st = new String(rd);
                    Log.d(TAG, "onCharacteristicRead: String: " + st);
                    receivedData.add(st);
                } else{
                    Log.d(TAG, "No data received");
                }
            }
        };
    }

    /**
     *Connects to bluetooth device
     * */
    @Override
    public boolean connectToDevice(Object device, Context context) {
        BluetoothDeviceImp temp = (BluetoothDeviceImp) device;
        selectedGattDevice = (BluetoothGatt) temp.getConnection(context, Callbacks());
        Log.d(TAG, "connect: connectGatt called");
        //TODO check if successful
        return true;
    }


    /**
     *Closes BLE connection if there is no device
     **/
    @Override
    public boolean disconnectFromDevice() {
        if (selectedGattDevice != null) {
            selectedGattDevice.close();
        }
        //TODO check if successful
        return true;
    }
}
