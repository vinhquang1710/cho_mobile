package com.example.administrator.chotot.model;

/**
 * Created by Administrator on 16/10/2016.
 */

public class Category {
    private String id;
    private String category;
    private int urlImage;

    public Category() {
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

    public int getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(int urlImage) {
        this.urlImage = urlImage;
    }
}
