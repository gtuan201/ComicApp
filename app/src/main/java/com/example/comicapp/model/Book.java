package com.example.comicapp.model;

public class Book {
    String title,imgUrl, mota,category, imgBackgroundUrl;
    boolean favorite;

    public Book() {
    }

    public Book(String title, String imgUrl, String mota, String category, String imgBackgroundUrl, boolean favorite) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.mota = mota;
        this.category = category;
        this.imgBackgroundUrl = imgBackgroundUrl;
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgBackgroundUrl() {
        return imgBackgroundUrl;
    }

    public void setImgBackgroundUrl(String imgBackgroundUrl) {
        this.imgBackgroundUrl = imgBackgroundUrl;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
