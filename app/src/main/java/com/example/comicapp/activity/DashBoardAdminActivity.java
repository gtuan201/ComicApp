package com.example.comicapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.comicapp.R;
import com.example.comicapp.adapter.CategoryAdapter;
import com.example.comicapp.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoardAdminActivity extends AppCompatActivity {

    private EditText eSerach;
    private ImageButton btLogout;
    private AppCompatButton btAddCategory;
    private FloatingActionButton addPDF;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Category> categoryArrayList;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerViewCate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_admin);
        btLogout = findViewById(R.id.btLogout);
        btAddCategory = findViewById(R.id.addCategory);
        addPDF = findViewById(R.id.addPdf);
        recyclerViewCate = findViewById(R.id.categoryRecyclerView);
        eSerach = findViewById(R.id.tvSearch);
        firebaseAuth = FirebaseAuth.getInstance();
        loadCategory();
        btAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardAdminActivity.this,AddCategoryActivity.class));
            }
        });
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(DashBoardAdminActivity.this,LoginActivity.class));
                finish();
            }
        });
        eSerach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    categoryAdapter.getFilter().filter(s);
                }
                catch (Exception e){

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardAdminActivity.this,AddPdfActivity.class));
            }
        });
    }

    private void loadCategory() {
        categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    //get data
                    Category category = dataSnapshot.getValue(Category.class);
                    categoryArrayList.add(category);
                }
                categoryAdapter = new CategoryAdapter(DashBoardAdminActivity.this,categoryArrayList);
                recyclerViewCate.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}