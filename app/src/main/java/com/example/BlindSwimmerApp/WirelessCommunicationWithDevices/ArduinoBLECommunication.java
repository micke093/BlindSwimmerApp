package com.example.BlindSwimmerApp.WirelessCommunicationWithDevices;

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

    private static ArduinoBLECommunication instance = new ArduinoBLECommunication();
    private final String arduinoUuid = "19b10001-e8f2-537e-4f6c-d104768a1214";
    private BluetoothGatt selectedGattDevice = null;
    private BluetoothGattService selectedArduinoService = null;
    private ArrayList<String> receivedData;

    public static ArduinoBLECommunication getInstance(){ return instance; }
    private ArduinoBLECommunication() {
        receivedData = new ArrayList<>();
    }


    /**
    * Writes information to a Arduino Nano 33 with BLE
    * */
    @Override
    public void writeToDevice(String dataToSend) {
        Log.d(TAG, "Gatt service: " + selectedArduinoService.getUuid() + ", Data to send: " + dataToSend);
        if(selectedArduinoService != null){
            BluetoothGattCharacteristic characteristic =
                    selectedGattDevice.getService(selectedArduinoService.getUuid()).getCharacteristic(selectedArduinoService.getUuid());
            characteristic.setValue(dataToSend.toString());
            Log.d(TAG, "writeToDevice: characteristics value: " + characteristic.getStringValue(0));
            selectedGattDevice.writeCharacteristic(characteristic);
        }
        else{ Log.d(TAG, "writeToDevice: Gatt service is null"); }
    }

    @Override
    public void startAsynchronousReadFromSelectedDevice() {
        BluetoothGattCharacteristic characteristic =
                selectedGattDevice.getService(selectedArduinoService.getUuid()).getCharacteristic(selectedArduinoService.getUuid());
        selectedGattDevice.readCharacteristic(characteristic);
    }

    @Override
    public String getReadDataFromDevice() {
        return "";
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
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                byte[] rd = characteristic.getValue();
                if(rd != null || rd.length != 0){
                    String st = new String(rd);
                    Log.d(TAG, "onCharacteristicRead: String: " + st);
                    receivedData.add(st);
                } else Log.d(TAG, "No data received");
            }
        };
    }

    @Override
    public boolean connectToDevice(Object device, Context context) {
        BluetoothDeviceImp temp = (BluetoothDeviceImp) device;
        selectedGattDevice = (BluetoothGatt) temp.getConnection(context, Callbacks());
        Log.d(TAG, "connect: connectGatt called");
        //TODO check if successful
        return true;
    }

    @Override
    public boolean disconnectFromDevice() {
        if (selectedGattDevice != null) {
            selectedGattDevice.close();
            selectedGattDevice = null;
            selectedArduinoService = null;
        }
        //TODO check if successful
        return true;
    }

    @Override
    public String getChangeModeToConnectingMode() {
        return CHANGE_MODE_TO_CONNECTING_MODE;
    }
    @Override
    public String getChangeModeToRunningMode() {
        return CHANGE_MODE_TO_RUNNING_MODE;
    }
    @Override
    public String getChangeModeToTrainMode() {
        return CHANGE_MODE_TO_TRAIN_MODE;
    }
    @Override
    public String getBluetoothBeaconOneSetName() {
        return BLUETOOTH_BEACON_ONE_SET_NAME;
    }
    @Override
    public String getBluetoothBeaconTwoSetName() {
        return BLUETOOTH_BEACON_TWO_SET_NAME;
    }
    @Override
    public String getSwimmerTurnSignal() {
        return SWIMMER_TURN_SIGNAL;
    }

    //============================ PRIVATE FUNCTIONS =========================================

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
            if(uuid.equals(arduinoUuid)) {
                arduinoService = service;
                Log.d(TAG, "Found matching uuid");
                break;
            }
        }

        if(arduinoService == null) Log.d(TAG, "Could not find a matching service UUID name.");
        else {
            selectedArduinoService = arduinoService;
            this.selectedGattDevice = gatt;
            Log.d(TAG, "DeviceDiscovered: Gatt: " + this.selectedGattDevice.toString());
        }
    }
}
