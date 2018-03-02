package com.example.kevin.walkhealthy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventCreateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseReference mDataRef;

    private EditText eventName, eventStartingLocation, eventEndingLocation, eventTime;
    private Button eventCreateBtn;
    private Spinner eSpinner;
    private String  eName, eStart, eEnd, eTime, eIntensity;
    private String eYear, eMonth, eDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);


        //Assign ID's
        eventName = (EditText) findViewById(R.id.txtEventName);
        eventStartingLocation = (EditText)findViewById(R.id.txtEventStartingLocation);
        eventEndingLocation = (EditText)findViewById(R.id.txtEventEndingLocation);
        eventTime = (EditText)findViewById(R.id.editTextTime);
        eventCreateBtn = (Button) findViewById(R.id.btnCreateEvent);
        final CalendarView eventCalendar = (CalendarView)findViewById(R.id.calendarEventCreate);
        final EditText eventTime = (EditText)findViewById(R.id.editTextTime);
        eSpinner = (Spinner)findViewById(R.id.spinnerEventIntensity);

        //spinner options
        final String[] spinnerOptions = {"Select Intensity", "Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eSpinner.setAdapter(adapter);
        eSpinner.setOnItemSelectedListener(this);


        mDataRef = FirebaseDatabase.getInstance().getReference().child("Event");

        eventCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                int temp = month+1; //because for some reason calendar view returns the month number off by 1. I.e. December is represented as 11 instead of 12.
                eYear = String.valueOf(year);
                eMonth = String.valueOf(temp);
                eDay = String.valueOf(day);

            }
        });

        eventCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eName = eventName.getText().toString().trim().toLowerCase();
                eStart = eventStartingLocation.getText().toString().trim().toLowerCase();
                eEnd = eventEndingLocation.getText().toString().trim();
                eTime = eventTime.getText().toString().trim();

                //add the new event to the firebase if all fields are occupied
                if(!TextUtils.isEmpty(eName) && !TextUtils.isEmpty(eStart) && !TextUtils.isEmpty(eEnd) &&!TextUtils.isEmpty(eTime) && !TextUtils.isEmpty(eDay) && !TextUtils.isEmpty(eIntensity))
                {
                    DatabaseReference mChildDatabase = mDataRef.push();
                    mChildDatabase.child("EventDay").setValue(eDay);
                    mChildDatabase.child("EventMonth").setValue(eMonth);
                    mChildDatabase.child("EventYear").setValue(eYear);
                    mChildDatabase.child("EventName").setValue(eName);
                    mChildDatabase.child("EventStartingLocation").setValue(eStart);
                    mChildDatabase.child("EventEndingLocation").setValue(eEnd);
                    mChildDatabase.child("EventTime").setValue(eTime);
                    mChildDatabase.child("EventIntensity").setValue(eIntensity);

                    //display success message and move user back to the main page
                    Toast.makeText(EventCreateActivity.this, "Event Created", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EventCreateActivity.this, EventActivity.class));
                }
                else
                    Toast.makeText(EventCreateActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });


    }//onCreate


    //spinner selected item logic
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                eIntensity=null;
                break;
            case 1:
                eIntensity="Walk";
                break;
            case 2:
                eIntensity="Jog";
                break;
            case 3:
                eIntensity="Run";
                break;
            default:
                eIntensity=null;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}//EventCreateActivity
