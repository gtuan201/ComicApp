package com.example.comicapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comicapp.R;
import com.example.comicapp.fragment.DetailFragment;
import com.example.comicapp.model.Book;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BookAdapter extends FirebaseRecyclerAdapter<Book, BookAdapter.BookViewHolder> {
    public BookAdapter(@NonNull FirebaseRecyclerOptions<Book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull Book model) {
        holder.textTitle.setText(model.getTitle());
        Glide.with(holder.imgBook.getContext()).load(model.getImgUrl()).into(holder.imgBook);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,new DetailFragment(model.getTitle(),model.getImgUrl(), model.getCategory(), model.getMota())).addToBackStack(DetailFragment.TAG).commit();
            }
        });
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book,parent,false);
        return new BookViewHolder(view);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        CardView itemBookTrend;
        ImageView imgBook;
        TextView textTitle;

        public BookViewHolder(@NonNull View itemview) {
            super(itemview);
            imgBook = itemview.findViewById(R.id.imgBook);
            textTitle = itemview.findViewById(R.id.textTitle);
            itemBookTrend = itemview.findViewById(R.id.item_booktrend);
        }
    }
}
