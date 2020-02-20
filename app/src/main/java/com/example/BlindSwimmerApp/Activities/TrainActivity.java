package com.example.BlindSwimmerApp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.BlindSwimmerApp.R;
import com.example.BlindSwimmerApp.WirelessCommunicationWithDevices.ArduinoBLECommunication;
import com.example.BlindSwimmerApp.WirelessCommunicationWithDevices.IDeviceCommunication;

public class TrainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "Trainactivity";
    private boolean sessionActive;
    private IDeviceCommunication deviceCommunication = null;

    private Button startSwimmingSessionButton;
    private Button endSwimmingSessionButton;
    private Button backButton;
    private Button turnButton;

    @Override
    public void onClick(View v) {
        if (v == startSwimmingSessionButton) sessionActive = true;

        else if (v == turnButton){
            if(sessionActive){
                deviceCommunication.writeToDevice(deviceCommunication.getSwimmerTurnSignal());
                Log.d(TAG, "onClick: After write to device");
            }
        }

        else if(v == endSwimmingSessionButton) sessionActive = false;
        else if(v == backButton) finish();
    }

    //============== SETUP FUNCTION FOR ANDROID APPLICATION =============================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        startSwimmingSessionButton = findViewById(R.id.start_session_swimming_button);
        startSwimmingSessionButton.setOnClickListener(this);
        endSwimmingSessionButton = findViewById(R.id.end_session_swimming_button);
        endSwimmingSessionButton.setOnClickListener(this);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        turnButton = findViewById(R.id.turn_button);
        turnButton.setOnClickListener(this);

        deviceCommunication = ArduinoBLECommunication.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionActive = true;
    }

    protected void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
