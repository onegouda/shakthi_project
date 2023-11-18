package com.example.shakthi_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ticketPage extends AppCompatActivity {
    TextView textViewSource,textViewDestination,textViewDate,textViewFare,textViewScheme,textViewFinalFare;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_page);
        textViewSource = findViewById(R.id.textViewSource);
        textViewDestination=findViewById(R.id.textViewDestination);
        textViewDate=findViewById(R.id.textViewDate);
        textViewFare=findViewById(R.id.textViewFare);
        textViewScheme=findViewById(R.id.textViewScheme);
        textViewFinalFare=findViewById(R.id.textViewFinalFare);

        //firebase initialise
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        //firestore initialise
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference=db.collection("users").document(mAuth.getCurrentUser().getUid());

        documentReference.collection("Ticket_History").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                    textViewSource.setText(document.getString("Source"));
                    textViewDestination.setText(document.getString("Destination"));
                    textViewDate.setText(document.getString("Date"));
                    textViewFare.setText(document.getString("Fare"));
                    textViewScheme.setText(document.getString("Scheme"));
                    textViewFinalFare.setText(document.getString("FinalFare"));
                    Toast.makeText(ticketPage.this, "Data sent to Database", Toast.LENGTH_SHORT).show();


                }
            }
        });
      /*  db.collection("users").document().collection("Ticket_History").document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot!=null){
                    textViewSource.setText(documentSnapshot.getString("Source"));
                    textViewDestination.setText(documentSnapshot.getString("Destination"));
                    textViewDate.setText(documentSnapshot.getString("Date"));
                    textViewFare.setText(documentSnapshot.getString("Fare"));
                    textViewScheme.setText(documentSnapshot.getString("Scheme"));
                    textViewFinalFare.setText(documentSnapshot.getString("FinalFare"));
                    Toast.makeText(ticketPage.this, "Data sent to Database", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ticketPage.this, "Unable to Fetch", Toast.LENGTH_SHORT).show();


            }
        });*/
    }
}