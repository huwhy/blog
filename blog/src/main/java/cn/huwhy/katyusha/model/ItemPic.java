package cn.huwhy.katyusha.model;

import java.io.Serializable;

public class ItemPic implements Serializable {

    private int id;
    private int itemId;
    private String originalFileName;
    private String path;

    public ItemPic() {
    }

    public ItemPic(String originalFileName, String path) {
        this.originalFileName = originalFileName;
        this.path = path;
    }

    public ItemPic(int itemId, String originalFileName, String path) {
        this.itemId = itemId;
        this.originalFileName = originalFileName;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
