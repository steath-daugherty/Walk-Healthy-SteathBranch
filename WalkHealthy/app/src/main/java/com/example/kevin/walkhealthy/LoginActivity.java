package com.example.kevin.walkhealthy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    //Views and widget fields
    Button loginButton;
    EditText userEmailEdit, userPasswordEdit;

    //String fields
    String userEmailString, userPasswordString;


    private ProgressDialog progressDialog;

    //Firebase authentication fields
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabaseRef;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);

        //Assign ID's
        loginButton = (Button) findViewById(R.id.loginActivityButton);
        userEmailEdit = (EditText) findViewById(R.id.loginEmailEditText);
        userPasswordEdit = (EditText) findViewById(R.id.loginPasswordEditText);

        //Assign Instances

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {

                    final String email = user.getEmail();

                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            checkUserValidation(dataSnapshot, email);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));

                }
                else
                {

                }

            }
        };

        //On click listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Perform login operation
                userEmailString = userEmailEdit.getText().toString().trim();
                userPasswordString = userPasswordEdit.getText().toString().trim();

                if(!TextUtils.isEmpty(userEmailString) && !TextUtils.isEmpty(userPasswordString))
                {
                    progressDialog.setMessage("Logging in....");
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(userEmailString, userPasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful())
                            {

                                //startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));


                                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        checkUserValidation(dataSnapshot, userEmailString);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                            }
                            else
                            {

                                Toast.makeText(LoginActivity.this, "User Login Failed", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }

            }
        });



    }
       //delete this if program stops working
    private void checkUserValidation(DataSnapshot dataSnapshot, String passedEmail)
    {

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {

            DataSnapshot dataUser = (DataSnapshot) iterator.next();

            if (dataUser.child("emailUser").getValue().toString().equals(passedEmail))
            {
                if (dataUser.child("isVerified").getValue().toString().equals("unverified"))
                {
                    Intent in = new Intent(LoginActivity.this, ProfileActivity.class);
                    in.putExtra("USER_KEY", dataUser.child("userKey").getValue().toString());
                    startActivity(in);

                }
                else
                {
                    startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                }
            }

        }

    }


}
