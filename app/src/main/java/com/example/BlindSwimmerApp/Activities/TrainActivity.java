package com.example.BlindSwimmerApp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.ConnectedDevice;
import com.example.BlindSwimmerApp.CommunicationTypeDevice.Devices.IDevice;
import com.example.BlindSwimmerApp.R;

public class TrainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "Trainactivity";
    private boolean sessionActive;
    private IDevice connectedDevice = null;

    private Button startSwimmingSessionButton;
    private Button endSwimmingSessionButton;
    private Button backButton;
    private Button turnButton;

    @Override
    public void onClick(View v) {
        if (v == startSwimmingSessionButton) sessionActive = true;

        else if (v == turnButton){
            if(sessionActive){

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectedDevice = ConnectedDevice.getInstance();
        Log.d(TAG, "onStart: Connected device is: " + connectedDevice.getName());
        if(connectedDevice == null){ showToast("mDeviceView == null");}
    }

    protected void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
