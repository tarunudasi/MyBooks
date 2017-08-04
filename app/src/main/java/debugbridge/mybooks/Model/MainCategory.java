package debugbridge.mybooks.Model;

public class MainCategory {

    String id, name, img;

    public MainCategory() {
    }

    public MainCategory(String id, String name, String img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public MainCategory(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }
}
