package com.example.comicapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.comicapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileUserActivity extends AppCompatActivity {
    private CircleImageView avatarUpdate;
    private EditText nameUpdate;
    private EditText emailUpdate;
    private AppCompatButton btUpdate, btBack,btUpdateEmail;
    public Uri imgUrl;
    private static final int IMG_PICK_CODE = 1;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_user);
        avatarUpdate = findViewById(R.id.updateAvt);
        nameUpdate = findViewById(R.id.nameUpdate);
        emailUpdate = findViewById(R.id.emailUpdate);
        btUpdate = findViewById(R.id.updateProfile);
        btBack = findViewById(R.id.btBackUpdate);
        btUpdateEmail = findViewById(R.id.updateEmail);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        userUpdateProfile();
        avatarUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPicker();
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateUser();
            }
        });
        btUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateEmail();
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateProfileUserActivity.this,MainActivity.class));
            }
        });

    }



    private void userUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
        {
            return;
        }
        nameUpdate.setText(user.getDisplayName());
        emailUpdate.setText(user.getEmail());
        Glide.with(UpdateProfileUserActivity.this).load(user.getPhotoUrl()).error(R.drawable.avatar).into(avatarUpdate);
    }
    private void imgPicker(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select img"),IMG_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_PICK_CODE){
            if (resultCode==RESULT_OK){
                imgUrl = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUrl);
                    setBitmapImg(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            Toast.makeText(this,"Picking false",Toast.LENGTH_SHORT).show();
        }
    }
    private void onClickUpdateUser(){
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        String strName = nameUpdate.getText().toString().trim();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(strName)
                .setPhotoUri(imgUrl)
                .build();
        user.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(UpdateProfileUserActivity.this,"Update Successfully",Toast.LENGTH_SHORT).show();
                            userUpdateProfile();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileUserActivity.this,"Update False",Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void onClickUpdateEmail() {
        progressDialog.setMessage("Please wait to update email....");
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String strEmail = emailUpdate.getText().toString().trim();
        user.updateEmail(strEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileUserActivity.this,"Update Successfully",Toast.LENGTH_SHORT).show();
                        userUpdateProfile();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileUserActivity.this,"Update Email False",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setBitmapImg(Bitmap bitmapImg){
        avatarUpdate.setImageBitmap(bitmapImg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UpdateProfileUserActivity.this,MainActivity.class));
    }
}