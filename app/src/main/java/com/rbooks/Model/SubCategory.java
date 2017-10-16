package com.rbooks.Model;

public class SubCategory {
    private String id, categoryId, title, img;

    public SubCategory(String id, String categoryId,String title, String img) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.img = img;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
