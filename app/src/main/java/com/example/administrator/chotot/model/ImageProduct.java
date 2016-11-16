package com.example.administrator.chotot.model;

/**
 * Created by Administrator on 20/10/2016.
 */

public class ImageProduct {
    private String id;
    private String imgUrl;

    public ImageProduct() {
    }

    public ImageProduct(String id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
