package com.example.shakthi_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class dashBoard extends AppCompatActivity {

    ImageView buyTicketImg,navigateImg,historyImg,profileImg;
    TextView shakthiAbt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        shakthiAbt=findViewById(R.id.shakthiAbt);
        buyTicketImg=findViewById(R.id.buyTicketImg);
        navigateImg=findViewById(R.id.navigateImg);
        historyImg=findViewById(R.id.historyImg);
        profileImg=findViewById(R.id.profileImg);


        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(dashBoard.this,profile.class);
                startActivity(i);
            }
        });

        buyTicketImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(dashBoard.this,buyTicketClass.class);
                startActivity(i);
            }
        });


        navigateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(dashBoard.this,navigateClass.class);
                startActivity(i);
            }
        });

        historyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(dashBoard.this,history.class);
                startActivity(i);
            }
        });

        shakthiAbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(dashBoard.this,aboutShakthi.class);
                startActivity(i);
            }
        });

    }
}