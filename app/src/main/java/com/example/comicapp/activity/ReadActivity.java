package com.example.comicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.example.comicapp.Constants;
import com.example.comicapp.R;
import com.example.comicapp.fragment.DetailFragment;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReadActivity extends AppCompatActivity {

    private PDFView pdfView;
    private ImageButton btBackRead;
    private LottieDialog lottieDialog;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        pdfView = findViewById(R.id.pdfView);
        btBackRead = findViewById(R.id.btBackRead);
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setMessage("Please wait to loading....");
//        progressDialog.show();
        lottieDialog = new LottieDialog(this);
        lottieDialog.setAnimation(R.raw.loading)
                .setAutoPlayAnimation(true)
                .setMessage("Please wait to loading book...")
                .setDialogBackground(Color.WHITE)
                .setMessageColor(Color.BLACK)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setAnimationRepeatCount(LottieDialog.INFINITE)
                .setDialogWidth(600)
                .setDialogHeight(300)
                .show();
        btBackRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        title = intent.getStringExtra("titleBook");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Book");
        reference.child(title)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String pdfUrl = ""+snapshot.child("url").getValue();
                        loadBookFromUrl(pdfUrl);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadBookFromUrl(String pdfUrl) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        storageReference.getBytes(Constants.MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
//                        progressDialog.dismiss();
                        lottieDialog.dismiss();
                        pdfView.fromBytes(bytes)
                                .swipeHorizontal(false)
                                .load();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
                        lottieDialog.dismiss();
                        Toast.makeText(ReadActivity.this,"Load false",Toast.LENGTH_SHORT).show();
                    }
                });
    }

}