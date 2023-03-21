package com.example.comicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.comicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private EditText regMail, regpassword;
    private AppCompatButton btSignup;
    private AppCompatButton btgoback;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressDialog = new ProgressDialog(this);
        regMail = findViewById(R.id.mail);
        regpassword = findViewById(R.id.dkmatkhau);
        btSignup = findViewById(R.id.btsignUp);
        btgoback = findViewById(R.id.btGoback);
        firebaseAuth = FirebaseAuth.getInstance();
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });
        btgoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });

    }


    private void onClickSignUp() {
        String strEmail = regMail.getText().toString().trim();
        String strPass = regpassword.getText().toString().trim();

        if (TextUtils.isEmpty(strEmail)||TextUtils.isEmpty(strPass)){
            Toast.makeText(SignUpActivity.this,"Please enter your email and password",Toast.LENGTH_SHORT).show();
        }

        else {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(strEmail,strPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()){
                        updateUserInfo();
                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(SignUpActivity.this,"Register false",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    private void updateUserInfo() {
        progressDialog.setMessage("Saving user info");
        String uid = firebaseAuth.getUid();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("userImg","");
        hashMap.put("userType","user");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this,"Register Successfully, Please login...",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this,"Register False",Toast.LENGTH_SHORT).show();
                    }
                });

    }

}


