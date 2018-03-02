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

/**
 * Created by gigta on 10/7/2017.
 */

public class GroupCreate extends AppCompatActivity {
    EditText groupName, groupDifficulty, groupMeeting, groupCollege;
    Button saveGroupButton;

    DatabaseReference mDataRef;

    String userNameString, userCityString, userStateString, userCollegeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);


        //Assign ID's
        groupName = (EditText) findViewById(R.id.usernameEditText);
        groupDifficulty = (EditText) findViewById(R.id.cityEditText);
        groupMeeting = (EditText) findViewById(R.id.stateEditText);
        groupCollege = (EditText) findViewById(R.id.collegeEditText);
        saveGroupButton = (Button) findViewById(R.id.initialSaveUserProfileBtn);

        mDataRef = FirebaseDatabase.getInstance().getReference();


        //Save Button logic
        saveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                userNameString = groupName.getText().toString().trim();
                userCityString = groupDifficulty.getText().toString().trim();
                userStateString = groupMeeting.getText().toString().trim();
                userCollegeString = groupCollege.getText().toString().trim();

                Log.d("GroupInfo", userNameString + ":" + userCityString + ":" + userStateString + ":" + userCollegeString);

                if (!TextUtils.isEmpty(userNameString) && !TextUtils.isEmpty(userCityString) && !TextUtils.isEmpty(userStateString) && !TextUtils.isEmpty(userCollegeString))
                {

                    DatabaseReference mChildDatabase = mDataRef.child("Groups").push();
                    mChildDatabase.child("GroupName").setValue(R.id.usernameEditText);
                    mChildDatabase.child("Difficulty").setValue(R.id.cityEditText);
                    mChildDatabase.child("MeetingLocation").setValue(R.id.stateEditText);
                    mChildDatabase.child("College").setValue(R.id.collegeEditText);

                    Toast.makeText(GroupCreate.this, "Group Created!", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(GroupCreate.this, WelcomeActivity.class));

                }


            }
        });


    }
}
