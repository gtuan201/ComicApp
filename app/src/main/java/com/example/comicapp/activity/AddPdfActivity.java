package com.example.comicapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicapp.R;
import com.example.comicapp.model.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AddPdfActivity extends AppCompatActivity {
    private ImageButton btBack2;
    private EditText bookTitle, motaBook;
    private TextView cateSelect,filePdf;
    private Button btAddBook;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ArrayList<Category> modelCategoryList;
    private Uri pdfUri = null;
    private static final int PDF_PICK_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pdf);
        btBack2 = findViewById(R.id.btBack2);
        filePdf = findViewById(R.id.filePdf);
        cateSelect = findViewById(R.id.spinerCate);
        btAddBook = findViewById(R.id.btSubmidAddBook);
        bookTitle = findViewById(R.id.bookET);
        motaBook = findViewById(R.id.moTa);
        firebaseAuth = FirebaseAuth.getInstance();
        loadCategoryPdf();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        btBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        filePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfPickIntent();
            }
        });
        cateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickDialog();
            }
        });
        btAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private String title="";
    private String cate="";
    private String mota="";
    private void validateData() {
        title = bookTitle.getText().toString();
        cate = cateSelect.getText().toString();
        mota = motaBook.getText().toString();
        if (TextUtils.isEmpty(title)||TextUtils.isEmpty(mota)||TextUtils.isEmpty(cate)){
            Toast.makeText(this,"Enter Information of Book",Toast.LENGTH_SHORT).show();
        }
        else if (pdfUri==null){
            Toast.makeText(this,"Pick Pdf",Toast.LENGTH_SHORT).show();
        }
        else {
            uploadPdfToStorage();
        }

    }

    private void uploadPdfToStorage() {
        progressDialog.setMessage("Uploading Pdf");
        progressDialog.show();
        long timestamp = System.currentTimeMillis();
        String filePathAndName = "Book/"+timestamp;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String uploadPdfUrl = ""+uriTask.getResult();
                        upLoadPdfInforToDB(uploadPdfUrl,timestamp);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddPdfActivity.this,"PDF upload false due to"+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void upLoadPdfInforToDB(String uploadPdfUrl, long timestamp) {
        progressDialog.setMessage("Uploading....");
        String uid = firebaseAuth.getUid();
        //setup data to upload
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("id",""+timestamp);
        hashMap.put("title",""+title);
        hashMap.put("category",""+cate);
        hashMap.put("mota",mota);
        hashMap.put("url",""+uploadPdfUrl);
        hashMap.put("timestamp",timestamp);
        hashMap.put("imgUrl","");
        //db ref to Book
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Book");
        ref.child(""+title)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(AddPdfActivity.this,"Upload Successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddPdfActivity.this,"False to upload pdf due to"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCategoryPdf() {
        modelCategoryList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Category");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelCategoryList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Category category = dataSnapshot.getValue(Category.class);
                    modelCategoryList.add(category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void categoryPickDialog() {
        String[] categoryArray = new String[modelCategoryList.size()];
        for (int i = 0; i < modelCategoryList.size(); i++){
            categoryArray[i] = modelCategoryList.get(i).getCategory();
        }
        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoryArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = categoryArray[which];
                        cateSelect.setText(category);
                    }
                })
                .show();
    }

    private void pdfPickIntent() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"Select PDF"),PDF_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PDF_PICK_CODE){
            if (resultCode==RESULT_OK){
                pdfUri = data.getData();
            }
        }
        else {
            Toast.makeText(this,"Cancel picking ",Toast.LENGTH_SHORT).show();
        }

    }


}