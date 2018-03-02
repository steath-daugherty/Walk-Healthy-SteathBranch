package com.example.kevin.walkhealthy;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class WelcomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SensorEventListener {

    //Sensor for Step Counting
    public SensorManager SM;
    public TextView textView_Steps;
    public Button btn_ResetSteps;
    public boolean running = false;
    public boolean restart = false;

    //Declare Fields
    Button logoutBtn;
    Button createGroupBtn;
    Button navigateToSearchBtn;
    TextView textViewUserEmail;
    Spinner spinner;

    //Spinner options
    private static final String[]spinnerOptions = {"Event Actions", "Create New Event", "Search for an Event", "Create a New Group"};

    //Firebase authentication fields
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    public FirebaseUser user;
    public String name;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Sensor Functionality for Step Counting
        textView_Steps = (TextView)findViewById(R.id.textView_Steps);
        SM = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        btn_ResetSteps = (Button)findViewById(R.id.btnResetSteps);

        //Assign ID's
        logoutBtn = (Button) findViewById(R.id.logoutButton);
        textViewUserEmail = (TextView) findViewById(R.id.welcomeUserEmailTextView) ;



        //Deleting Buttons causes the app to crash. Set invisible instead
        createGroupBtn = findViewById(R.id.createGroupButton);
        navigateToSearchBtn = findViewById(R.id.btnNavigateToSearch);
        createGroupBtn.setVisibility(View.GONE);
        navigateToSearchBtn.setVisibility(View.GONE);



        //Spinner options
        spinner = findViewById(R.id.mainActivitySpinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(WelcomeActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Assign Instances
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();

                if (user != null)
                {
                    String uid = user.getUid();
                    String email = user.getEmail();
                    name = user.getDisplayName().toString();
                    databaseReference = FirebaseDatabase.getInstance().getReference();

                    String msg = "Welcome back, "+name;
                    textViewUserEmail.setText(msg);
                }
                else
                {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            }
        };


        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WelcomeActivity.this, GroupActivity.class));
            }
        });

        navigateToSearchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(WelcomeActivity.this, EventActivity.class));
            }
        });



        btn_ResetSteps.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                restart = true;
            }
        });

        //Set on click listener to logout
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                finish();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch(i){
            case 0:
                break;
            case 1:
                startActivity(new Intent(WelcomeActivity.this, EventCreateActivity.class));
                break;
            case 2:
                startActivity(new Intent(WelcomeActivity.this, EventActivity.class));
                break;
            case 3:
                startActivity(new Intent(WelcomeActivity.this, GroupActivity.class));

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /*
    ______________________________________________________________________________________

    Step Counter Sensor Methods
    _______________________________________________________________________________________
     */

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(running){
            textView_Steps.setText(String.valueOf(sensorEvent.values[0]));
        }
        if(restart){
            sensorEvent.values[0] = 0;
            restart = false;
        }

        textView_Steps.setText(String.valueOf(sensorEvent.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void onResume(){
        super.onResume();
        running = true;
        Sensor counter = SM.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(counter != null){

            SM.registerListener(this, counter, SensorManager.SENSOR_DELAY_FASTEST);
        }
        else
            Toast.makeText(this, "Error: Sensor Not Found!", Toast.LENGTH_SHORT).show();
        
    }

    protected void onPause(){
        super.onPause();
        running = false;
        //Uncomment if unregistered listener
        // /SM.unregisterListener(this);
    }

}
