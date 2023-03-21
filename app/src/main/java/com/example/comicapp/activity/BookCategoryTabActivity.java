package com.example.comicapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.comicapp.R;
import com.example.comicapp.fragment.BookCategoryFragment;
import com.example.comicapp.model.Category;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookCategoryTabActivity extends AppCompatActivity {
    public ArrayList<Category> categoryArrayList; //show lên tab
    public ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton btBackTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_category_tab);
        viewPager = findViewById(R.id.viewpagerTab);
        tabLayout = findViewById(R.id.tabLayout);
        btBackTab = findViewById(R.id.btBackTab);
        btBackTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookCategoryTabActivity.this, MainActivity.class));
            }
        });
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }
    private void setUpViewPager(ViewPager viewPager){
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this);
        categoryArrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Category");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Category category = dataSnapshot.getValue(Category.class);
                    categoryArrayList.add(category);
                    //add data to viewPagerAdapter
                    viewPagerAdapter.addFragment(BookCategoryFragment.newInstance(
                            ""+category.getId(),
                            ""+category.getCategory(),
                            ""+category.getUid()), category.getCategory());
                    viewPagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPager.setAdapter(viewPagerAdapter);
    }
    public class ViewPagerAdapter extends FragmentPagerAdapter{
        private ArrayList<BookCategoryFragment> fragmentArrayList = new ArrayList<>() ;
        private ArrayList<String> fragmentTitleList = new ArrayList<>();
        private Context context;
        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
            super(fm, behavior);
            this.context = context;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }
        private void addFragment(BookCategoryFragment fragment, String title){
            fragmentArrayList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BookCategoryTabActivity.this,MainActivity.class));
    }
}