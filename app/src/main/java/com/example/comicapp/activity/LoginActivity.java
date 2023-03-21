package com.example.comicapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.example.comicapp.R;
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

public class LoginActivity extends AppCompatActivity {

    private EditText logMail,logPass;
    private AppCompatButton btLogin,btSignup;
    private String userMail,password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView forgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        logMail = (EditText) findViewById(R.id.userMail);
        logPass = (EditText) findViewById(R.id.matkhau);
        forgotPass = findViewById(R.id.forgotPass);
        btLogin = findViewById(R.id.btLogin);
        btSignup = findViewById(R.id.btSignup);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMail = logMail.getText().toString().trim();
                password = logPass.getText().toString().trim();
                if (TextUtils.isEmpty(userMail)||TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"Email or Password is empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    onClickLogin();
                }


            }
        });
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

    }

    private void onClickLogin() {
        progressDialog.setMessage("Please wait....");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(userMail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    checkUser();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Login false",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String userType = ""+snapshot.child("userType").getValue();
                        if (userType.equals("user")){
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }
                        else if (userType.equals("admin")){
                            startActivity(new Intent(LoginActivity.this,DashBoardAdminActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}