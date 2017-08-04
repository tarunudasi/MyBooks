package debugbridge.mybooks.Model;

public class Viewbooks {

    private String id, title, cost, desc, img;

    public Viewbooks(String id, String title, String cost, String desc, String img) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.desc = desc;
        this.img = img;
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
