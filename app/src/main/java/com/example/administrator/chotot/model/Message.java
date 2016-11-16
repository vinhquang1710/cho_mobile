package com.example.administrator.chotot.model;

import java.util.Comparator;

/**
 * Created by Administrator on 05/11/2016.
 */

public class Message {
    private String idSender;
    private String content;
    private String img;
    private String time;
    private String idChat;

    private String idProduct;
    private String idUser;

    public Message() {
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdChat() {
        return idChat;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public static Comparator<Message> sortNew = new Comparator<Message>() {
        @Override
        public int compare(Message p1, Message p2) {
            Long s1 = Long.parseLong(p1.getTime());
            Long s2 = Long.parseLong(p2.getTime());

            return (int)(s2 - s1);
        }
    };
}
