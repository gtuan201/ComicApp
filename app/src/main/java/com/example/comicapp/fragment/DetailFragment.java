package com.example.comicapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.comicapp.Constants;
import com.example.comicapp.R;
import com.example.comicapp.activity.ReadActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Objects;

public class DetailFragment extends Fragment {

    public static final String TAG = DetailFragment.class.getName();
    String title, imgUrl, category,mota;
    boolean isInFavorite = false;
    AppCompatButton btFavorite;


    public DetailFragment() {

    }
    public DetailFragment(String title, String imgUrl, String category, String mota) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.category = category;
        this.mota = mota;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ImageView imgDetail = view.findViewById(R.id.img_Detail);
        TextView titleDetail = view.findViewById(R.id.toolbarBookname);
        TextView cateDetail = view.findViewById(R.id.toolbarCate);
        TextView motaDetail = view.findViewById(R.id.desBookdetail);
        AppCompatButton btRead = view.findViewById(R.id.btReadbook);
        btFavorite = view.findViewById(R.id.favorite);
        ImageButton btDownload = view.findViewById(R.id.btDownload);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Downloading");
        progressDialog.setCanceledOnTouchOutside(false);
        AppCompatButton btBackDetail = view.findViewById(R.id.btBackDetail);
        Glide.with(imgDetail).load(imgUrl).into(imgDetail);
        titleDetail.setText(title);
        cateDetail.setText(category);
        motaDetail.setText(mota);
        isCheckFavorite();
        btRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                intent.putExtra("titleBook",title);
                startActivity(intent);
            }
        });
        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                getBook(title,progressDialog);
            }
        });
        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInFavorite){
                    removeFavorite(getActivity(),title);
                }
                else {
                    addToFavorite(getActivity(),title);
                }
                isCheckFavorite();

            }
        });
        btBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }

            }
        });

        return view;
    }
    private void isCheckFavorite(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(firebaseAuth.getUid()).child("Favorite").child(title)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isInFavorite = snapshot.exists();
                        if (isInFavorite){
                            btFavorite.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.favorite_red,0,0);
                            btFavorite.setText("Remove");
                        }
                        else {
                            btFavorite.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_baseline_favorite_border_24,0,0);
                            btFavorite.setText("Add Favorite");
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void removeFavorite(FragmentActivity activity, String title) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(firebaseAuth.getUid()).child("Favorite").child(title)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(activity,"Removed to your favorite list",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity,"Failed remove to your favorite list",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void addToFavorite(FragmentActivity activity, String title) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("title",title);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(firebaseAuth.getUid()).child("Favorite").child(title)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(activity,"Added to your favorite list",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity,"Failed add to your favorite list",Toast.LENGTH_SHORT).show();
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
                        downloadBook(pdfUrl,progressDialog);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void downloadBook(String pdfUrl, ProgressDialog progressDialog) {
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
                        Toast.makeText(getActivity(),"Download false",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(),"Saved to download folder",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(getActivity(),"Saved false",Toast.LENGTH_SHORT).show();
        }
    }
}