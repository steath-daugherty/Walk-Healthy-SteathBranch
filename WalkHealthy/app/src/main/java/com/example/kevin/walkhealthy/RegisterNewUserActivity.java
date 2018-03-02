package com.example.kevin.walkhealthy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterNewUserActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabaseRef, mUserCheckData;


    Button moveToProfileInfo;
    EditText email, password, confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);



        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUserCheckData = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();



        moveToProfileInfo = (Button)findViewById(R.id.buttonContinue);
        email = (EditText)findViewById(R.id.editTextEmail);
        password = (EditText)findViewById(R.id.editTextPassword);
        confirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);


        moveToProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uEmail = email.getText().toString();
                final String uPass = password.getText().toString();
                final String uConfirmPass = confirmPassword.getText().toString();

                if(!uEmail.isEmpty() && !uPass.isEmpty())
                {
                    if(mUserCheckData.child("emailUser").toString().equals(uEmail))
                    {
                        Toast.makeText(RegisterNewUserActivity.this, "That email is already in use", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        startActivity(new Intent(RegisterNewUserActivity.this, ProfileActivity.class));
                    }
                }
                else{
                    Toast.makeText(RegisterNewUserActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
