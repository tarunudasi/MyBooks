package debugbridge.mybooks.Model;

import java.io.Serializable;

public class BookLists implements Serializable {

    private String id, title, cost, desc, img, contact_person, contact_number;

    public BookLists(String id, String title, String cost, String desc, String img, String contact_person, String contact_number) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.desc = desc;
        this.img = img;
        this.contact_person = contact_person;
        this.contact_number = contact_number;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
