package com.example.comicapp.model;

public class Category {
    String id,category,uid;

    public Category() {
    }

    public Category(String id, String category, String uid) {
        this.id = id;
        this.category = category;
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
