package com.example.comicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.comicapp.R;
import com.example.comicapp.activity.MainActivity;
import com.example.comicapp.activity.ReadActivity;
import com.example.comicapp.model.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{
    private Context context;
    private ArrayList<Book> bookArrayList;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public FavoriteAdapter(Context context, ArrayList<Book> bookArrayList) {
        this.context = context;
        this.bookArrayList = bookArrayList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite,parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Book book = bookArrayList.get(position);
        String title = book.getTitle();
        viewBinderHelper.bind(holder.swipeRevealLayout,book.getTitle());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookArrayList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                ref.child(firebaseAuth.getUid()).child("Favorite").child(title)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context,"Removed to your favorite list",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Failed remove to your favorite list",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        holder.btReadCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivities(new Intent[]{new Intent(context, ReadActivity.class).putExtra("titleBook",title)});

            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Book");
        reference.child(title)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String titleBook = ""+snapshot.child("title").getValue();
                        String categoryBook = ""+snapshot.child("category").getValue();
                        String imgUrl = ""+snapshot.child("imgUrl").getValue();
                        book.setFavorite(true);
                        book.setCategory(categoryBook);
                        book.setImgUrl(imgUrl);
                        holder.titleBook.setText(titleBook);
                        holder.categoryBook.setText(categoryBook);
                        Glide.with(holder.imgBook).load(book.getImgUrl()).into(holder.imgBook);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private TextView titleBook,categoryBook;
        private ImageView imgBook;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout layout;
        private AppCompatButton btReadCircle;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleBook = itemView.findViewById(R.id.bookNameFar);
            categoryBook = itemView.findViewById(R.id.bookCategoryFar);
            imgBook = itemView.findViewById(R.id.imgFar);
            btReadCircle = itemView.findViewById(R.id.btReadCir);
            swipeRevealLayout = itemView.findViewById(R.id.item_far);
            layout = itemView.findViewById(R.id.layout_delete);
        }
    }
}
