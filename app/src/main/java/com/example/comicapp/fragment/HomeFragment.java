package com.example.comicapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.example.comicapp.activity.CatActivity;
import com.example.comicapp.adapter.BookAdapter;
import com.example.comicapp.adapter.BookAdapter2;
import com.example.comicapp.adapter.PhotoAdapter;
import com.example.comicapp.R;
import com.example.comicapp.model.Book;
import com.example.comicapp.model.Book2;
import com.example.comicapp.model.Photo;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {
    String strNameFeedBack, strEmailFeedback,strFeedback;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private RecyclerView recyclerViewBookTrend, recyclerViewNew;
    private LottieAnimationView catAnimation,discordAnimation,feedbackAnimation;
    private BookAdapter bookAdapter;
    private BookAdapter2 bookAdapter2;
    private List<Photo> mlistPhoto;
    private LottieDialog lottieDialog;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager.getCurrentItem()==mlistPhoto.size()-1){
                viewPager.setCurrentItem(0);
            }
            else {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPagerHome);
        circleIndicator = view.findViewById(R.id.circle_indicator);
        catAnimation = view.findViewById(R.id.lottieAnimationView2);
        discordAnimation = view.findViewById(R.id.lottieAnimationView);
        feedbackAnimation = view.findViewById(R.id.lottieAnimationView4);
        recyclerViewBookTrend = view.findViewById(R.id.recyclerViewBookTrend);
        lottieDialog = new LottieDialog(getContext());
        lottieDialog.setAnimation(R.raw.feedback)
                .setAutoPlayAnimation(true)
                .setMessage("Please wait to sending your feedback...")
                .setDialogBackground(Color.WHITE)
                .setMessageColor(Color.BLACK)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setAnimationRepeatCount(LottieDialog.INFINITE)
                .setDialogWidth(600)
                .setDialogHeight(300);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Book");
        recyclerViewNew = view.findViewById(R.id.recyclerViewBookDeXuat);
        mlistPhoto = getListPhoto();
        PagerAdapter adapter = new PhotoAdapter(mlistPhoto);
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        handler.postDelayed(runnable,4000);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,4000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        catAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CatActivity.class));
            }
        });
        discordAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               gotoUrl("https://discord.gg/4e2m26C4");
            }
        });
        feedbackAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedBackBottomSheet();
            }
        });
        Query query = ref.orderByChild("category").equalTo("New");
        Query query1 = ref.orderByChild("category").equalTo("Trend");
        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                    .setQuery(query1, Book.class)
                    .build();
        bookAdapter = new BookAdapter(options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewBookTrend.setLayoutManager(layoutManager);
        recyclerViewBookTrend.setAdapter(bookAdapter);

        FirebaseRecyclerOptions<Book2> options1 =
                new FirebaseRecyclerOptions.Builder<Book2>()
                .setQuery(query,Book2.class)
                .build();
        bookAdapter2 = new BookAdapter2(options1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewNew.setLayoutManager(linearLayoutManager);
        recyclerViewNew.setAdapter(bookAdapter2);

    }

    private void openFeedBackBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        EditText nameUser, emailUser, feedback;
        nameUser = bottomSheetView.findViewById(R.id.nameFeedback);
        emailUser = bottomSheetView.findViewById(R.id.emailFeedback);
        feedback = bottomSheetView.findViewById(R.id.feedback);
        AppCompatButton btCancelFeedback = bottomSheetView.findViewById(R.id.cancel_feedback);
        AppCompatButton btSubmitFeedback = bottomSheetView.findViewById(R.id.submit_Feedback);
        btCancelFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        btSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strNameFeedBack = nameUser.getText().toString();
                strEmailFeedback = emailUser.getText().toString();
                strFeedback= feedback.getText().toString();
                if (TextUtils.isEmpty(strEmailFeedback)|TextUtils.isEmpty(strFeedback)){
                    Toast.makeText(getActivity(),"Email or Feedback empty",Toast.LENGTH_SHORT).show();
                }
                else {
                    lottieDialog.show();
                    long timestamp = System.currentTimeMillis();
                    HashMap<Object,String> hashMap = new HashMap<>();
                    hashMap.put("id",""+timestamp);
                    hashMap.put("UserName", strNameFeedBack);
                    hashMap.put("Email", strEmailFeedback);
                    hashMap.put("Feedback", strFeedback);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Feedback");
                    reference.child(""+timestamp)
                            .setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    lottieDialog.dismiss();
                                    Toast.makeText(getContext(),"Send successfully",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    lottieDialog.dismiss();
                                    Toast.makeText(getContext(),"Send false",Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    @Override
    public void onStart() {
        super.onStart();
        bookAdapter.startListening();
        bookAdapter2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        bookAdapter.stopListening();
        bookAdapter2.stopListening();

    }


    private List<Photo> getListPhoto(){
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img_5));
        list.add(new Photo(R.drawable.img_6));
        list.add(new Photo(R.drawable.img_7));
        list.add(new Photo(R.drawable.img_8));
        list.add(new Photo(R.drawable.img_9));
        list.add(new Photo(R.drawable.img_10));
        return list;

    }

}
