package com.example.shakthi_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextView AlreadyHaveAccount;
    EditText inputEmail, inputPassword, inputCPassword,inputGender;
    Button btnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    EditText inputFirstname, inputLastname, inputPhoneNo;
    private ImageView frontImageView;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    private Bitmap mImageBitmap;



    //firebase authentication
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AlreadyHaveAccount = findViewById(R.id.AlreadyHaveAccount);
        inputFirstname = findViewById(R.id.inputFirstname);
        inputLastname = findViewById(R.id.inputLastname);
        inputPhoneNo = findViewById(R.id.inputPhoneNo);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputCPassword = findViewById(R.id.inputCPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);

        //firebase initialise
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //firestore initialise
        db = FirebaseFirestore.getInstance();
        frontImageView = findViewById(R.id.frontImageView);


        inputGender = findViewById(R.id.inputGender);


        Button ocr = findViewById(R.id.ocr);

        ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractInfo();
            }
        });


        //stores data in firebase
        String fname = inputFirstname.getText().toString();
        String lname = inputLastname.getText().toString();
        String phoneno = inputPhoneNo.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String gen=inputGender.getText().toString();

        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //passes the control back to login page
                startActivity(new Intent(register.this, MainActivity.class));
                finish();


            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();

            }
        });


    }

    private void extractInfo() {
        if(mImageBitmap != null){
            FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mImageBitmap);
            recognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    HashMap<String,String> dataMap = new AadhaarProccessing()
                            .processExtractTextForFrontPic(firebaseVisionText, getApplicationContext());
                    presentFrontOutput(dataMap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@androidx.annotation.NonNull Exception e) {
                    //Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this,"Please Take Both Front and Back Image First",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            mImageBitmap = CameraUtils.getBitmap(mCurrentPhotoPath);
            frontImageView.setImageBitmap(mImageBitmap);


        } else {
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }
    }

    private void presentFrontOutput(HashMap<String,String> datamap) {
        if (datamap != null) {
            inputGender.setText(datamap.get("gender"), TextView.BufferType.EDITABLE);



        }
    }

    public void takePicture(View view) {

        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try{
                photoFile = CameraUtils.createImagefile(this);
                mCurrentPhotoPath = photoFile.getAbsolutePath();
            }catch (IOException ex){
                Toast.makeText(this,"Error Creating File",Toast.LENGTH_SHORT).show();
            }
            if(photoFile != null){
                Uri photoURL = FileProvider.getUriForFile(this,
                        "com.example.shakthi_project.fileprovider",
                        photoFile);

                takePic.putExtra(MediaStore.EXTRA_OUTPUT,photoURL);
                startActivityForResult(takePic,REQUEST_TAKE_PHOTO);
            }
        }

    }

    private void PerformAuth() {

        String fname = inputFirstname.getText().toString();
        String lname = inputLastname.getText().toString();
        String phoneno = inputPhoneNo.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputCPassword.getText().toString();
        String gen=inputGender.getText().toString();
        String state = "Karnataka";




        if (!email.matches(emailPattern)) {
            inputEmail.setError("Error Context Email");

        } else if (password.isEmpty() || password.length() < 6) {
            inputPassword.setError("Enter Proper Password");
        } else if (!password.equals(confirmPassword)) {
            inputCPassword.setError("Password not Matches");
        } else {
            progressDialog.setTitle("Registration");
            progressDialog.setMessage("Please Wait While Registration...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        progressDialog.dismiss();
                        //sendUserToNextActivity();
                        //addding data to database
                        //DocumentReference documentReference = db.collection("users").document(mUser.getUid());
                        Map<String, Object> user = new HashMap<>();
                        user.put("FirstName", fname);
                        user.put("LastLast", lname);
                        user.put("Gender",gen);
                        user.put("State",state);
                        user.put("PhoneNo", phoneno);
                        user.put("Email", email);
                        user.put("Password", password);
                        // Add a new document with a generated ID
                        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(register.this, "Registraton Sucessful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(register.this, dashBoard.class));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });


                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(register.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
}