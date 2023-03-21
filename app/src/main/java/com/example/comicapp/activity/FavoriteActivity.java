package com.example.comicapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.comicapp.R;
import com.example.comicapp.adapter.FavoriteAdapter;
import com.example.comicapp.model.Book;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private RecyclerView favoriteRev;
    private FavoriteAdapter favoriteAdapter;
    private ArrayList<Book> bookArrayList;
    boolean isExpanded = true;
    private Menu menuToolbar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        favoriteRev = findViewById(R.id.recyclerview_favorite);
        bookArrayList = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        initToolbar();
        reference.child(firebaseAuth.getUid()).child("Favorite")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookArrayList.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String title = ""+dataSnapshot.child("title").getValue();
                            Book book = new Book();
                            book.setTitle(title);
                            bookArrayList.add(book);

                        }
                        favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this,bookArrayList);
                        favoriteRev.setAdapter(favoriteAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        favoriteRev.setLayoutManager(manager);
        favoriteRev.setHasFixedSize(true);
        initToolbarAnimation();



    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    private void  initToolbarAnimation()
    {
        collapsingToolbarLayout.setTitle("My Favorite");
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_18);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {
                int myColor = palette.getVibrantColor(getResources().getColor(R.color.teal_200));
                collapsingToolbarLayout.setContentScrimColor(myColor);
                collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.black_tran));
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)>230){
                    isExpanded = false;
                }
                else {
                    isExpanded = true;
                }
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbarcollap,menu);
        menuToolbar = menu;
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FavoriteActivity.this,MainActivity.class));
    }
}