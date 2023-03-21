package com.example.comicapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.comicapp.R;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class CatActivity extends AppCompatActivity {

    private LottieAnimationView cat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        cat = findViewById(R.id.cat);
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(CatActivity.this,MainActivity.class));
                finish();
            }
        });


    }
}