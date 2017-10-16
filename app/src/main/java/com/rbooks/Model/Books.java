package com.rbooks.Model;

import java.io.Serializable;

public class Books implements Serializable {

    private String id, name, amount, description, author, publication, img, user, latitude, longitude, verify, sold;

    public Books(String id, String name, String amount, String description, String author, String publication, String img, String user, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.author = author;
        this.publication = publication;
        this.img = img;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Books(String id, String name, String amount, String description, String author, String publication, String img, String user, String latitude, String longitude, String verify, String sold) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.author = author;
        this.publication = publication;
        this.img = img;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.verify = verify;
        this.sold = sold;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}