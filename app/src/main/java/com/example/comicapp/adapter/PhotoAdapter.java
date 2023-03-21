package com.example.comicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.comicapp.R;
import com.example.comicapp.model.Photo;

import java.util.List;

public class PhotoAdapter extends PagerAdapter {
    private List<Photo> mListPhoto;

    public PhotoAdapter(List<Photo> mListPhoto) {
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;
        view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo,container,false);
        ImageView img = view.findViewById(R.id.img_photo);
        Photo photo = mListPhoto.get(position);
        img.setImageResource(photo.getRes());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (mListPhoto!=null)
            return mListPhoto.size();
        return 0 ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
