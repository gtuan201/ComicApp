package com.example.comicapp.model;

public class Book2 {
   private String title,imgUrl,category,id,uid,mota, imgBackgroundUrl;
   long timestamp;

    public Book2(long timestamp) {
        this.timestamp = timestamp;
    }

    public Book2() {
    }

    public Book2(String title, String imgUrl, String category, String id, String uid, String mota, String imgBackgroundUrl, long timestamp) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.category = category;
        this.id = id;
        this.uid = uid;
        this.mota = mota;
        this.imgBackgroundUrl = imgBackgroundUrl;
        this.timestamp = timestamp;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getImgBackgroundUrl() {
        return imgBackgroundUrl;
    }

    public void setImgBackgroundUrl(String imgBackgroundUrl) {
        this.imgBackgroundUrl = imgBackgroundUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
