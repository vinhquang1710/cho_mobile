package com.example.administrator.chotot.model;

import java.util.Comparator;

/**
 * Created by Administrator on 20/10/2016.
 */

public class Product {
    private String id;
    private String title;
    private String idCategory;
    private String price;
    private String status;
    private String describe;
    private String date;
    private String UrlImage;
    private String city;

    private String idUser;

    public Product() {
    }

    public Product(String id, String title, String idCategory, String price, String date, String urlImage) {
        this.id = id;
        this.title = title;
        this.idCategory = idCategory;
        this.price = price;
        this.date = date;
        UrlImage = urlImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlImage() {
        return UrlImage;
    }

    public void setUrlImage(String urlImage) {
        UrlImage = urlImage;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static Comparator<Product> sortStatus = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            Long s1 = Long.parseLong(p1.getPrice());
            Long s2 = Long.parseLong(p2.getPrice());

            return (int)(s1 - s2);
        }
    };

    public static Comparator<Product> sortNew = new Comparator<Product>() {
        @Override
        public int compare(Product p1, Product p2) {
            Long s1 = Long.parseLong(p1.getDate());
            Long s2 = Long.parseLong(p2.getDate());

            return (int)(s2 - s1);
        }
    };
}
