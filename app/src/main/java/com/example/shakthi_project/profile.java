package com.example.shakthi_project;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class profile extends AppCompatActivity {

    Button btnLogout,btnVerifyEmail;
    TextView firstNameProfile,lastNameProfile,emailProfile,phoneNoProfile,genderProfile,addressProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnLogout=findViewById(R.id.btnLogout);
        btnVerifyEmail=findViewById(R.id.btnVerifyEmail);
        firstNameProfile=findViewById(R.id.firstNameProfile);
        lastNameProfile=findViewById(R.id.lastNameProfile);
        emailProfile=findViewById(R.id.emailProfile);
        phoneNoProfile=findViewById(R.id.phoneNoProfile);
        genderProfile=findViewById(R.id.genderProfile);
        addressProfile=findViewById(R.id.addressProfile);

        //firebase initialise
        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(profile.this,MainActivity.class);
                startActivity(i);
            }
        });





        //firestore initialise


        if(!auth.getCurrentUser().isEmailVerified()){

            btnVerifyEmail.setVisibility(android.view.View.VISIBLE);


        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {


            FirebaseFirestore db = FirebaseFirestore.getInstance();


            db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot!=null){
                        firstNameProfile.setText(documentSnapshot.getString("FirstName"));
                        lastNameProfile.setText(documentSnapshot.getString("LastLast"));
                        phoneNoProfile.setText(documentSnapshot.getString("PhoneNo"));
                        emailProfile.setText(documentSnapshot.getString("Email"));
                        genderProfile.setText(documentSnapshot.getString("Gender"));
                        addressProfile.setText(documentSnapshot.getString("State"));
                        Toast.makeText(profile.this, "Data Found", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(profile.this, "Unable to Fetch", Toast.LENGTH_SHORT).show();


                }
            });

        }else{
            Toast.makeText(this, "No User Logged in", Toast.LENGTH_SHORT).show();
        }


    }
}