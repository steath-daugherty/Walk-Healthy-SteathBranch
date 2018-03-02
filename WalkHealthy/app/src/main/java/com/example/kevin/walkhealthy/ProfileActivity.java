package com.example.kevin.walkhealthy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    //Declare the fields
    EditText userName, userCity, userState, userCollege;
    Button saveProfileButton;

    //FIREBASE DATABASEREFERENCE
    DatabaseReference mDataRef;


    //users string key
    String keyUser;

    String userNameString, userCityString, userStateString, userCollegeString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get user key from intent
        keyUser = getIntent().getStringExtra("USER_KEY");

        //Firebase database reference
        mDataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(keyUser);

        //Assign ID's
        userName = (EditText) findViewById(R.id.usernameEditText);
        userCity = (EditText) findViewById(R.id.cityEditText);
        userState = (EditText) findViewById(R.id.stateEditText);
        userCollege = (EditText) findViewById(R.id.collegeEditText);
        saveProfileButton = (Button) findViewById(R.id.initialSaveUserProfileBtn);



        //Save Button logic
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userNameString = userName.getText().toString().trim();
                userCityString = userCity.getText().toString().trim();
                userStateString = userState.getText().toString().trim();
                userCollegeString = userCollege.getText().toString().trim();

                Log.d("contactInfo", userNameString + ":" + userCityString + ":" + userStateString + ":" + userCollegeString);

                if (!TextUtils.isEmpty(userNameString) && !TextUtils.isEmpty(userCityString) && !TextUtils.isEmpty(userStateString) && !TextUtils.isEmpty(userCollegeString))
                {

                    mDataRef.child("username").setValue(userNameString);
                    mDataRef.child("city").setValue(userCityString);
                    mDataRef.child("state").setValue(userStateString);
                    mDataRef.child("college").setValue(userCollegeString);
                    mDataRef.child("isVerified").setValue("verified");

                    Toast.makeText(ProfileActivity.this, "Profile Information Saved!", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(ProfileActivity.this, WelcomeActivity.class));

                }


            }
        });


    }

}
