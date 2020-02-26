package com.example.BlindSwimmerApp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.BlindSwimmerApp.R;
import com.example.BlindSwimmerApp.WirelessCommunicationWithDevices.ArduinoBLECommunication;
import com.example.BlindSwimmerApp.WirelessCommunicationWithDevices.IDeviceCommunication;

public class TrainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "Trainactivity";
    private boolean sessionActive = true;
    private IDeviceCommunication deviceCommunication = null;

    private Button resetButton;
    private Button backButton;
    private Button turnButton;
    private Button pauseButton;
    private Button sendButton;

    private TextView inputText;

    @Override
    public void onClick(View v) {
        if (v == turnButton){
            if(sessionActive){
                deviceCommunication.writeToDevice(deviceCommunication.getSwimmerTurnSignal());
                Log.d(TAG, "onClick: After write to device");
            }
        }

        if(v==resetButton){
            deviceCommunication.writeToDevice(deviceCommunication.getSwimmerClearSdcard());
        }

        if(v==pauseButton){
            deviceCommunication.writeToDevice(deviceCommunication.getSwimmerPause());
        }

        if(v==sendButton){
            String message = inputText.getText().toString();

            if(message.isEmpty())
                System.out.println("The message is empty!");

            showToast("Message sent");

            deviceCommunication.writeToDevice(deviceCommunication.getHeaderMessage() + message);
        }

        //TODO should we get the data when we end a session?
        else if(v == resetButton) {
            sessionActive = false;
        }
        else if(v == backButton) finish();
    }

    //============== SETUP FUNCTION FOR ANDROID APPLICATION =============================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        inputText = findViewById(R.id.inputText);


        sendButton = findViewById(R.id.send_button);
        pauseButton = findViewById(R.id.pause_button);
        resetButton = findViewById(R.id.reset_button);
        backButton = findViewById(R.id.back_button);
        turnButton = findViewById(R.id.turn_button);

        turnButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        sendButton.setOnClickListener(this);

        deviceCommunication = ArduinoBLECommunication.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionActive = true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        deviceCommunication.writeToDevice(deviceCommunication.getChangeModeToConnectingMode());
        //TODO implement getting info from device for the session.
    }

    protected void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}