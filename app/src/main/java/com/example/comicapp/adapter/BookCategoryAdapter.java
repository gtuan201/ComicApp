package com.example.comicapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comicapp.Constants;
import com.example.comicapp.R;
import com.example.comicapp.activity.ReadActivity;
import com.example.comicapp.fragment.DetailFragment;
import com.example.comicapp.model.Book2;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ramotion.foldingcell.FoldingCell;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class BookCategoryAdapter extends RecyclerView.Adapter<BookCategoryAdapter.BookCategoryViewHoder>{
    private final Context context;
    public ArrayList<Book2> book2ArrayList;
    boolean isInFavorite = true;

    public BookCategoryAdapter(Context context, ArrayList<Book2> book2ArrayList) {
        this.context = context;
        this.book2ArrayList = book2ArrayList;
    }

    @NonNull
    @Override
    public BookCategoryViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_category,parent,false);
        return new BookCategoryViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookCategoryViewHoder holder, int position) {
        Book2 book2 = book2ArrayList.get(position);
        holder.getBindingAdapterPosition();
        String title = book2.getTitle();
        String category = book2.getCategory();
        String mota = book2.getMota();
        String imgUrl = book2.getImgUrl();
        holder.progressDialog.setMessage("Downloading");
        holder.progressDialog.setCanceledOnTouchOutside(false);
        holder.bookNameContent.setText(title);
        holder.categoryContent.setText(category);
        holder.motaBookDetail.setText(mota);
        holder.bookNameDetail.setText(title);
        Glide.with(holder.imageView.getContext()).load(imgUrl).into(holder.imageView);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(firebaseAuth.getUid())
            .child("Favorite")
            .child(title)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    isInFavorite = snapshot.exists();
                    if (isInFavorite) {
                        holder.btFavorite.setBackgroundResource(R.drawable.favorite_red);
                    } else {
                        holder.btFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                    }


                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });
        holder.btReadFolding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivities(new Intent[]{new Intent(context,ReadActivity.class).putExtra("titleBook",title)});
            }
        });
        holder.btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.progressDialog.show();
                getBook(title,holder.progressDialog);
            }
        });
        holder.btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

//                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                if (isInFavorite){
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
                else {
                    HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("title",title);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                        ref.child(firebaseAuth.getUid()).child("Favorite").child(title)
                                .setValue(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context,"Added to your favorite list",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context,"Failed add to your favorite list",Toast.LENGTH_SHORT).show();
                                    }
                                });

                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                reference.child(firebaseAuth.getUid())
                        .child("Favorite")
                        .child(title)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                isInFavorite = snapshot.exists();
                                if (isInFavorite) {
                                    holder.btFavorite.setBackgroundResource(R.drawable.favorite_red);
                                } else {
                                    holder.btFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

    }


    private void getBook(String title, ProgressDialog progressDialog) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Book");
        reference.child(title)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String pdfUrl = ""+snapshot.child("url").getValue();
                        downloadBook(pdfUrl,progressDialog,title);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void downloadBook(String pdfUrl, ProgressDialog progressDialog, String title) {
        String nameWithExtension = title + ".pdf";
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        storageReference.getBytes(Constants.MAX_BYTES_PDF)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        saveDownloadBook(bytes,nameWithExtension,progressDialog);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"Download false",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveDownloadBook(byte[] bytes, String nameWithExtension, ProgressDialog progressDialog) {
        try {
            File downLoadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            downLoadFolder.mkdirs();
            String filePath = downLoadFolder.getPath() + "/" + nameWithExtension;
            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(bytes);
            outputStream.close();
            progressDialog.dismiss();
            Toast.makeText(context,"Saved to download folder",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(context,"Saved false",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return book2ArrayList.size();
    }

    public class BookCategoryViewHoder extends RecyclerView.ViewHolder {
        private FoldingCell foldingCell;
        private ImageView imageView;
        private TextView bookNameContent,categoryContent;
        private TextView bookNameDetail, motaBookDetail;
        private AppCompatButton btReadFolding;
        private ImageButton btDownload,btFavorite;
        private ProgressDialog progressDialog;

        public BookCategoryViewHoder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgViewContent);
            bookNameContent = itemView.findViewById(R.id.bookNameContent);
            categoryContent = itemView.findViewById(R.id.bookCategoryContent);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            bookNameDetail = itemView.findViewById(R.id.bookNameDetail);
            motaBookDetail = itemView.findViewById(R.id.motaFold);
            btReadFolding = itemView.findViewById(R.id.btReadFold);
            btDownload = itemView.findViewById(R.id.downloadBook);
            btFavorite = itemView.findViewById(R.id.favoriteBook);
            progressDialog = new ProgressDialog(context);
            
        }
    }
}
