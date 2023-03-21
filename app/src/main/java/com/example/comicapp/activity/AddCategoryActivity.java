package com.example.comicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.comicapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddCategoryActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private AppCompatButton btSubmit;
    private String strCategory;
    private EditText category;
    private ImageButton btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        btSubmit = findViewById(R.id.btSubmidAddCate);
        category = findViewById(R.id.categoryET);
        btBack = findViewById(R.id.btBack);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validData();
            }
        });
    }
    private void validData() {
        strCategory = category.getText().toString().trim();
        if (TextUtils.isEmpty(strCategory)){
            Toast.makeText(AddCategoryActivity.this,"Please enter category",Toast.LENGTH_SHORT).show();
        }
        else {
            addCategoryFirebase();
        }
    }

    private void addCategoryFirebase() {
        progressDialog.setMessage("Adding category...");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id",""+timestamp);
        hashMap.put("category",strCategory);
        hashMap.put("uid",""+firebaseAuth.getUid());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");
        ref.child(strCategory)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //success
                        progressDialog.dismiss();
                        Toast.makeText(AddCategoryActivity.this,"Category added successfully...",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //false
                        progressDialog.dismiss();
                        Toast.makeText(AddCategoryActivity.this,"Add failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}