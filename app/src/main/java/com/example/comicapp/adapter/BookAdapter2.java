package com.example.comicapp.adapter;

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
import com.example.comicapp.model.Book2;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Objects;

public class BookAdapter2  extends FirebaseRecyclerAdapter<Book2,BookAdapter2.BookViewHolder2>{
    public BookAdapter2(@NonNull FirebaseRecyclerOptions<Book2> options1) {
        super(options1);

    }

    @Override
    protected void onBindViewHolder(@NonNull BookViewHolder2 holder, int position, @NonNull Book2 model) {

        String id = model.getId();
        holder.titleBookNew.setText(model.getTitle());
        holder.motaBook.setText(model.getMota());
        holder.categorybook.setText(model.getCategory());
        Glide.with(holder.imgURL.getContext()).load(model.getImgUrl()).into(holder.imgURL);
        holder.itemBookNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Objects.requireNonNull(activity).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new DetailFragment(model.getTitle(),model.getImgUrl(),model.getCategory(),model.getMota())).addToBackStack(DetailFragment.TAG).commit();
            }
        });


    }

    @NonNull
    @Override
    public BookViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_new,parent,false);
        return new BookViewHolder2(view);
    }

    public static class BookViewHolder2 extends RecyclerView.ViewHolder {
        CardView itemBookNew;
        TextView titleBookNew, categorybook, motaBook;
        ImageView imgURL;

        public BookViewHolder2(@NonNull View itemView) {
            super(itemView);
            titleBookNew = itemView.findViewById(R.id.title_book_new);
            categorybook = itemView.findViewById(R.id.category_new);
            imgURL = itemView.findViewById(R.id.img_book_new);
            motaBook = itemView.findViewById(R.id.motaBook);
            itemBookNew = itemView.findViewById(R.id.item_bookNew);

        }

    }

}



