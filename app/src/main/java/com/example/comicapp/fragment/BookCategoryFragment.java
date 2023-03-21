package com.example.comicapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comicapp.R;
import com.example.comicapp.adapter.BookCategoryAdapter;
import com.example.comicapp.model.Book2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BookCategoryFragment extends Fragment {
    private String categoryID;
    private String category;
    private String uid;
    private ArrayList<Book2> book2ArrayList = new ArrayList<>();
    private BookCategoryAdapter adapter;
    private RecyclerView recyclerView;


    public BookCategoryFragment() {

    }


    public static BookCategoryFragment newInstance(String categoryID, String category, String uid) {
        BookCategoryFragment fragment = new BookCategoryFragment();
        Bundle args = new Bundle();
        args.putString("categoryID", categoryID);
        args.putString("category", category);
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryID = getArguments().getString("categoryID");
            category = getArguments().getString("category");
            uid = getArguments().getString("uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_book_category, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewBookCategory);
        loadBook();
        return view;
    }

    private void loadBook() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Book");
        reference.orderByChild("category").equalTo(category)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        book2ArrayList.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Book2 book2 = dataSnapshot.getValue(Book2.class);
                            book2ArrayList.add(book2);
                        }
                        adapter = new BookCategoryAdapter(getContext(),book2ArrayList);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}