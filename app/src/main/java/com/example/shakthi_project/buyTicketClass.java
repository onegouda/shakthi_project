package com.example.shakthi_project;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class buyTicketClass extends AppCompatActivity {

    EditText source1,destination1;
    Button btnBuyTicket;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;
    String gender1,address1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket_class);
        source1=findViewById(R.id.source1);
        destination1=findViewById(R.id.destination1);
        btnBuyTicket=findViewById(R.id.btnBuyTicket);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        //firestore initialise
        db = FirebaseFirestore.getInstance();


        btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot!=null){

                            gender1=documentSnapshot.getString("Gender");
                            address1=documentSnapshot.getString("State");



                            Toast.makeText(buyTicketClass.this, "Data Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(buyTicketClass.this, "Unable to Fetch", Toast.LENGTH_SHORT).show();
                    }
                });
                if (gender1.isEmpty() || address1.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(buyTicketClass.this);
                    builder.setMessage("Verify Aadhaar First");
                    builder.setTitle("Alert !");
                    builder.setCancelable(false);
                    builder.setIcon(R.drawable.logo);
                    builder.setPositiveButton("Proceed", (DialogInterface.OnClickListener) (dialog, which) -> {
                        Intent i = new Intent(buyTicketClass.this, verifyAadhaar.class);
                        startActivity(i);
                        finish();
                    });
                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                        Intent i = new Intent(buyTicketClass.this, dashBoard.class);
                        startActivity(i);
                        Toast.makeText(buyTicketClass.this, "Sorry,You didn't Verify Aadhaar.", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(buyTicketClass.this, "Your Aadhaar is Verified.", Toast.LENGTH_SHORT).show();

                    Toast.makeText(buyTicketClass.this, "Data Found", Toast.LENGTH_SHORT).show();
                    if(gender1.matches("Female")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(buyTicketClass.this);
                        builder.setMessage("congratulation");
                        builder.setMessage("You are eligible for free services under SHAKTHI Scheme.");
                        builder.setTitle("Shakthi INC");
                        builder.setCancelable(false);
                        builder.setIcon(R.drawable.logo);
                        builder.setPositiveButton("Proceed", (DialogInterface.OnClickListener) (dialog, which) -> {
                            Intent i = new Intent(buyTicketClass.this, ticketPage.class);
                            startActivity(i);
                            finish();
                        });
                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                            Intent i = new Intent(buyTicketClass.this, dashBoard.class);
                            startActivity(i);
                            Toast.makeText(buyTicketClass.this, "Sorry,You didn't Verify Aadhaar.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        String s=source1.getText().toString();
                        String d=destination1.getText().toString();
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        String fare="0";
                        String finalFare="0";
                        String scheme="Applied";
                        if (s.isEmpty()) {
                            source1.setError("Required Source");
                        } else if (d.isEmpty()) {
                            destination1.setError("Required Destination");
                        }  else {
                            Map<String, Object> ticket = new HashMap<>();
                            ticket.put("Source",s);
                            ticket.put("Destination",d);
                            ticket.put("Date",date);
                            ticket.put("Fare",fare);
                            ticket.put("FinalFare",finalFare);
                            ticket.put("Scheme",scheme);
                            db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("Ticket_History").document().set(ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(buyTicketClass.this, "Bought Ticket Sucessful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(buyTicketClass.this, dashBoard.class));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                        }

                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(buyTicketClass.this);
                        builder.setMessage("Sorry !!!");
                        builder.setMessage("You are not eligible for free services under SHAKTHI Scheme.");
                        builder.setTitle("Shakthi INC");
                        builder.setCancelable(false);
                        builder.setIcon(R.drawable.logo);
                        builder.setPositiveButton("Proceed", (DialogInterface.OnClickListener) (dialog, which) -> {
                            Intent i = new Intent(buyTicketClass.this, ticketPage.class);
                            startActivity(i);
                            finish();
                        });
                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                            Intent i = new Intent(buyTicketClass.this, dashBoard.class);
                            startActivity(i);
                            Toast.makeText(buyTicketClass.this, "Sorry,You didn't Verify Aadhaar.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        String s=source1.getText().toString();
                        String d=destination1.getText().toString();
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        String fare="Under Calculation";
                        String finalFare="Under Calculation";
                        String scheme="Not Applied";
                        if (s.isEmpty()) {
                            source1.setError("Required Source");
                        } else if (d.isEmpty()) {
                            destination1.setError("Required Destination");
                        }  else {
                            Map<String, Object> ticket = new HashMap<>();
                            ticket.put("Source",s);
                            ticket.put("Destination",d);
                            ticket.put("Date",date);
                            ticket.put("Fare",fare);
                            ticket.put("FinalFare",finalFare);
                            ticket.put("Scheme",scheme);
                            db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("Ticket_History").document().set(ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(buyTicketClass.this, "Bought Ticket Sucessful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(buyTicketClass.this, dashBoard.class));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                        }
                    }
                }
            }
        });
        /*btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        gender1 = document.getString("Gender");
                                        address1 = document.getString("State");
                                        if (gender1.isEmpty() || address1.isEmpty()) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(buyTicketClass.this);
                                            builder.setMessage("Verify Aadhaar First");
                                            builder.setTitle("Alert !");
                                            builder.setCancelable(false);
                                            builder.setIcon(R.drawable.logo);
                                            builder.setPositiveButton("Proceed", (DialogInterface.OnClickListener) (dialog, which) -> {
                                                Intent i = new Intent(buyTicketClass.this, verifyAadhaar.class);
                                                startActivity(i);
                                                finish();
                                            });
                                            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                                                Intent i = new Intent(buyTicketClass.this, dashBoard.class);
                                                startActivity(i);
                                                Toast.makeText(buyTicketClass.this, "Sorry,You didn't Verify Aadhaar.", Toast.LENGTH_SHORT).show();
                                                dialog.cancel();
                                            });

                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        } else {
                                            Toast.makeText(buyTicketClass.this, "Your Aadhaar is Verified.", Toast.LENGTH_SHORT).show();
                                            DocumentReference documentReference=db.collection("users").document(mUser.getUid());




                                            db.collection("users")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                                    gender1 = document.getString("Gender");
                                                                    address1 = document.getString("State");


                                                                    if (gender1.matches("Female") && address1.contains("Karnataka")) {

                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(buyTicketClass.this);
                                                                        builder.setMessage("congratulation");
                                                                        builder.setMessage("You are eligible for free services under SHAKTHI Scheme.");
                                                                        builder.setTitle("Shakthi INC");
                                                                        builder.setCancelable(false);
                                                                        builder.setIcon(R.drawable.logo);
                                                                        builder.setPositiveButton("Proceed", (DialogInterface.OnClickListener) (dialog, which) -> {
                                                                            Intent i = new Intent(buyTicketClass.this, ticketPage.class);
                                                                            startActivity(i);
                                                                            finish();
                                                                        });
                                                                        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                                                                            Intent i = new Intent(buyTicketClass.this, dashBoard.class);
                                                                            startActivity(i);
                                                                            Toast.makeText(buyTicketClass.this, "Sorry,You didn't Verify Aadhaar.", Toast.LENGTH_SHORT).show();
                                                                            dialog.cancel();
                                                                        });
                                                                        AlertDialog alertDialog = builder.create();
                                                                        alertDialog.show();


                                                                        //uploading data
                                                                        String s=source1.getText().toString();
                                                                        String d=destination1.getText().toString();
                                                                        //Intent i =new Intent(buyTicketClass.this, ticketPage.class);
                                                                        //startActivity(i);

                                                                        if (s.isEmpty()) {
                                                                            source1.setError("Required Source");
                                                                        } else if (d.isEmpty()) {
                                                                            destination1.setError("Required Destination");
                                                                        }  else {


                                                                            db.collection("users").document(mUser.getUid());

                                                                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                                                                            String fare="0";
                                                                            String finalFare="0";
                                                                            String scheme="Applied";


                                                                            Map<String, Object> ticket = new HashMap<>();
                                                                            ticket.put("Source",s);
                                                                            ticket.put("Destination",d);
                                                                            ticket.put("Date",date);
                                                                            ticket.put("Fare",fare);
                                                                            ticket.put("FinalFare",finalFare);
                                                                            ticket.put("Scheme",scheme);


                                                                            db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("Ticket_History").add(ticket).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                    Toast.makeText(buyTicketClass.this, "Data Sent to Depot", Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(buyTicketClass.this, ticketPage.class));



                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Toast.makeText(buyTicketClass.this, "Unable to Buy A Ticket", Toast.LENGTH_SHORT).show();


                                                                                }
                                                                            });


                                                                        }



                                                                    } else {


                                                                        Toast.makeText(buyTicketClass.this, "Sorry For inconviniance. we are tring our best to resolve this issue.", Toast.LENGTH_SHORT).show();
                                                                        buyTicket();

                                                                    }
                                                                }
                                                            } else {

                                                                Log.w(TAG, "Error getting documents.", task.getException());
                                                            }
                                                        }


                                                    });
                                        }
                                    }
                                } else {

                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }


                        });
            }


        });
    }

    private void buyTicket() {
        if(gender1.equalsIgnoreCase("Male")){

            AlertDialog.Builder builder = new AlertDialog.Builder(buyTicketClass.this);
            builder.setMessage("Sorry !!!");
            builder.setMessage("You are not eligible for free services under SHAKTHI Scheme.");
            builder.setTitle("Shakthi INC");
            builder.setCancelable(false);
            builder.setIcon(R.drawable.logo);
            builder.setPositiveButton("Proceed", (DialogInterface.OnClickListener) (dialog, which) -> {
                Intent i = new Intent(buyTicketClass.this, ticketPage.class);
                startActivity(i);
                finish();
            });
            builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                Intent i = new Intent(buyTicketClass.this, dashBoard.class);
                startActivity(i);
                Toast.makeText(buyTicketClass.this, "Sorry,You didn't Verify Aadhaar.", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            String s=source1.getText().toString();
            String d=destination1.getText().toString();
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            String fare="Under Calculation";
            String finalFare="Under Calculation";
            String scheme="Not Applied";
            if (s.isEmpty()) {
                source1.setError("Required Source");
            } else if (d.isEmpty()) {
                destination1.setError("Required Destination");
            }  else {
                Map<String, Object> ticket = new HashMap<>();
                ticket.put("Source",s);
                ticket.put("Destination",d);
                ticket.put("Date",date);
                ticket.put("Fare",fare);
                ticket.put("FinalFare",finalFare);
                ticket.put("Scheme",scheme);
                db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("Ticket_History").document().set(ticket).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(buyTicketClass.this, "Bought Ticket Sucessful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(buyTicketClass.this, dashBoard.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });*/
            }
        }
