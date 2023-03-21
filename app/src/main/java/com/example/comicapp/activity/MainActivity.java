package com.example.comicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.comicapp.R;
import com.example.comicapp.fragment.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CircleImageView avatarUser;
    private TextView tvname,tvemail;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigation_view);
        tvname = navigationView.getHeaderView(0).findViewById(R.id.nameDrawer);
        tvemail = navigationView.getHeaderView(0).findViewById(R.id.emailDrawer);
        avatarUser = navigationView.getHeaderView(0).findViewById(R.id.avatar);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment();
        navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
        inforUser();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_home){
            replaceFragment();
        }
        else if (id == R.id.menu_favorite){
            Intent intent = new Intent(this,FavoriteActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.menu_type){
            Intent intent = new Intent(this,BookCategoryTabActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.menu_infor){
            Intent intent = new Intent(this,MoreInfoActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.profileUser){
            Intent intent = new Intent(this,UpdateProfileUserActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.menu_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    private void replaceFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new HomeFragment()).commit();
    }
    @SuppressLint("CheckResult")
    private void inforUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            return;
        }
        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri avatarUrl = user.getPhotoUrl();
        if (name == null)
        {
            tvname.setVisibility(View.GONE);
        }
        else {
            tvname.setVisibility(View.VISIBLE);
            tvname.setText(name);
        }
        tvemail.setText(email);
        Glide.with(this).load(avatarUrl).error(R.drawable.avatar).into(avatarUser);
    }
}