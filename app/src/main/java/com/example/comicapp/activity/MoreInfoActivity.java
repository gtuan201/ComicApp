package com.example.comicapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.comicapp.R;

public class MoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MoreInfoActivity.this,MainActivity.class));
    }
}