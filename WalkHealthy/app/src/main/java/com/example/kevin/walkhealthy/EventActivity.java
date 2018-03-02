package com.example.kevin.walkhealthy;

import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference mDataRef;
    public int clickCount;
    public String timeStart, duration, intensity, eDay, eMonth, eYear;
    private ListView mDrawerList;
    private ListView mItemList;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> dsAdapter;


    //spinner options
    private Spinner eventSpinner;
    private static final String[] eventSpinnerOptions = {"Filter Options", "Time", "Day", "Intensity"};

    //intensity spinner options
    private Spinner intensityFilterSpinner;
    private static final String[] intensityOptions = {"Walk", "Run", "Jog"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        //Assign IDs
        mDrawerList = (ListView)findViewById(R.id.navList);
        mItemList = (ListView)findViewById(R.id.eventList);
        final DrawerLayout mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        final Button searchRefinement = (Button) findViewById(R.id.searchRefinement);
        searchRefinement.setVisibility(View.INVISIBLE);
        final Button searchEventBtn = (Button) findViewById(R.id.btnSearchEvent);
        final Button createEventBtn = (Button) findViewById(R.id.btnCreateEvent);
        final EditText editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        final EditText timeFilter = (EditText)findViewById(R.id.editTextTime);
        final CalendarView calendar = (CalendarView)findViewById(R.id.calendarView);
        calendar.setVisibility(View.INVISIBLE);


        //Spinner Options
        eventSpinner = findViewById(R.id.spinnerFilterIntensity);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(EventActivity.this, android.R.layout.simple_spinner_dropdown_item, eventSpinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);
        eventSpinner.setOnItemSelectedListener(this);

        //set spinner to invisible until there are some search criteria
        eventSpinner.setVisibility(View.GONE);


        //radio group and buttons
        final Button rButton = (Button)findViewById(R.id.btnFilterIntensity);
        final RadioGroup eRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        final RadioButton rWalk = (RadioButton)findViewById(R.id.radioWalk);
        final RadioButton rJog = (RadioButton)findViewById(R.id.radioJog);
        final RadioButton rRun = (RadioButton)findViewById(R.id.radioRun);

        //set radio group and buttons to invisible until needed
        rButton.setVisibility(View.INVISIBLE);
        eRadioGroup.setVisibility(View.INVISIBLE);
        rWalk.setVisibility(View.INVISIBLE);
        rJog.setVisibility(View.INVISIBLE);
        rRun.setVisibility(View.INVISIBLE);

        createEventBtn.setVisibility(View.GONE);

        searchEventBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //get the location to search
                //set all buttons and fields to invisible to clear room
                searchEventBtn.setVisibility(View.INVISIBLE);
                editTextLocation.setVisibility(View.INVISIBLE);
                searchRefinement.setVisibility(View.INVISIBLE);
                //set the spinner to visible again
                eventSpinner.setVisibility(View.VISIBLE);

                final String strLocation = editTextLocation.getText().toString();


                mDataRef = FirebaseDatabase.getInstance().getReference().child("Event");

                mDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        addEventItem(dataSnapshot, strLocation);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        searchRefinement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
    }//OnCreate




    //Event Search Results
    private void addEventItem(DataSnapshot dataSnapshot, String strLocation){
        ArrayList<String> eventItems = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            if(ds.child("EventStartingLocation").getValue().toString().equalsIgnoreCase(strLocation))
            {
                addEvents(eventItems, ds);
            }
        }
        dsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventItems);
        mItemList.setAdapter(dsAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        //declare the "filtering fields"
        //user can filter by time, location, days, intensity, and so forth
        final EditText loc = (EditText)findViewById(R.id.editTextLocation);
        final EditText time = (EditText)findViewById(R.id.editTextTime);
        final Button btnTime = (Button)findViewById(R.id.btnFilterTime);

        //date filtering variables
        final CalendarView calendar = (CalendarView)findViewById(R.id.calendarView);
        final Button btnDate = (Button)findViewById(R.id.btnSelectDate);


        //radio group and buttons
        final Button rButton = (Button)findViewById(R.id.btnFilterIntensity);
        final RadioGroup eRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        final RadioButton rWalk = (RadioButton)findViewById(R.id.radioWalk);
        final RadioButton rJog = (RadioButton)findViewById(R.id.radioJog);
        final RadioButton rRun = (RadioButton)findViewById(R.id.radioRun);

        //set radio group and buttons to invisible until needed
        rButton.setVisibility(View.INVISIBLE);
        eRadioGroup.setVisibility(View.INVISIBLE);
        rWalk.setVisibility(View.INVISIBLE);
        rJog.setVisibility(View.INVISIBLE);
        rRun.setVisibility(View.INVISIBLE);

        final String strLocation = loc.getText().toString(); //since location has already been set, we can initialize its value now


        /*
        Case # 0: I couldn't figure out how to add a title to the spinner, so option 0 does nothing, it's just a placeholder for a temporary title
        Case # 1: the user filters the search results by a specified time (for now it is just 1 time that we will set to "Events starting at <insert time here>
        Case # 2: the user filters by a day and results are modified
        Case # 3: the user filters by an intensity and results are modified.
         */
        switch(i){
            case 0:
                break;
            case 1:
                //implement time filtering
                //set the list view to invisible for the time being, to reduce clutter on screen
                mItemList.setVisibility(View.INVISIBLE);

                //next, present the user with an input box accepting a time that will filter the search results
                //and set the time filter button to visible
                time.setVisibility(View.VISIBLE);
                btnTime.setVisibility(View.VISIBLE);

                btnTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String strTime=time.getText().toString();
                        mDataRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                addFilteredItem(dataSnapshot, strLocation, strTime);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //set the filter fields and buttons back to invisible to reduce clutter
                        btnTime.setVisibility(View.INVISIBLE);
                        time.setVisibility(View.INVISIBLE);
                        //set the list view to visible again
                        mItemList.setVisibility(View.VISIBLE);
                    }
                });
                break;

            //if the user chooses to filter by date
            case 2:

                //set the list view to invisible for the time being, to reduce clutter on screen
                mItemList.setVisibility(View.INVISIBLE);
                calendar.setVisibility(View.VISIBLE);
                btnDate.setVisibility(View.VISIBLE);

                calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                        int temp = month+1; //because calendarview returns a month off by 1
                        eYear = String.valueOf(year);
                        eMonth = String.valueOf(temp);
                        eDay = String.valueOf(dayOfMonth);

                        //implement day filtering
                        btnDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mDataRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        addFilteredDateItem(dataSnapshot, strLocation, eMonth, eDay);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                calendar.setVisibility(View.INVISIBLE);
                                btnDate.setVisibility(View.INVISIBLE);
                                mItemList.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                });
                break;

            //if the user chooses to filter by event intensity
            case 3:
                //hide the list view to clear up screen area and make the radio buttons visible
                mItemList.setVisibility(View.INVISIBLE);
                eRadioGroup.setVisibility(View.VISIBLE);
                rWalk.setVisibility(View.VISIBLE);
                rJog.setVisibility(View.VISIBLE);
                rRun.setVisibility(View.VISIBLE);
                rButton.setVisibility(View.VISIBLE);

                rButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int checked = eRadioGroup.getCheckedRadioButtonId();
                        View radButton = eRadioGroup.findViewById(checked);
                        int i = eRadioGroup.indexOfChild(radButton);
                        final String selected;

                        switch (i){
                            case 0:
                                selected="Walk";
                                break;
                            case 1:
                                selected="Jog";
                                break;
                            case 2:
                                selected="Run";
                                break;
                            default:
                                selected="";
                        }

                        mDataRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                addFilteredIntensityItem(dataSnapshot, strLocation, selected);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //hide the radio buttons and set the list view visible again with modified search results
                        eRadioGroup.setVisibility(View.INVISIBLE);
                        rWalk.setVisibility(View.INVISIBLE);
                        rJog.setVisibility(View.INVISIBLE);
                        rRun.setVisibility(View.INVISIBLE);
                        rButton.setVisibility(View.INVISIBLE);
                        mItemList.setVisibility(View.VISIBLE);
                    }
                });
        }
    }



    /*
    Filter the search results by the Time chosen by the user
     */
    private void addFilteredItem(DataSnapshot dataSnapshot, String strLocation, String strTime)
    {
        ArrayList<String> filteredItems = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            if(ds.child("EventStartingLocation").getValue().toString().equalsIgnoreCase(strLocation) && ds.child("EventTime").getValue().toString().equalsIgnoreCase(strTime))
                addEvents(filteredItems, ds);
        }
        dsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filteredItems);
        mItemList.setAdapter(dsAdapter);
    }


    /*
    Filter the search results by the date chosen by the user
     */
    private void addFilteredDateItem(DataSnapshot dataSnapshot, String strLocation, String month, String day)
    {
        ArrayList<String> filteredItems = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            if(ds.child("EventStartingLocation").getValue().toString().equalsIgnoreCase(strLocation) && ds.child("EventMonth").getValue().toString().equals(month) && ds.child("EventDay").getValue().toString().equals(day))
                addEvents(filteredItems, ds);
        }
        dsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filteredItems);
        mItemList.setAdapter(dsAdapter);
    }

    /*
    Filter the search results by the intensity chosen by the user
     */
    private void addFilteredIntensityItem(DataSnapshot dataSnapshot, String strLocation, String intensity)
    {
        ArrayList<String> filteredItems = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            if(ds.child("EventStartingLocation").getValue().toString().equalsIgnoreCase(strLocation) && ds.child("EventIntensity").getValue().toString().equals(intensity))
                addEvents(filteredItems, ds);
        }
        dsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filteredItems);
        mItemList.setAdapter(dsAdapter);
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void addEvents(ArrayList<String> lst, DataSnapshot ds)
    {
        lst.add("Event: "+ds.child("EventName").getValue().toString() + "\n" +
                "Starts At: "+ds.child("EventStartingLocation").getValue().toString() + "\n" +
                "On: "+ds.child("EventMonth").getValue().toString()+" - "+ds.child("EventDay").getValue().toString() + "\n"+
                "Beginning Time: "+ds.child("EventTime").getValue().toString() +"\n"+
                "Intensity: "+ds.child("EventIntensity").getValue().toString());
    }
}
