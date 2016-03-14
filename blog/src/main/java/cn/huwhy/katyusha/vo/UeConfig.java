package cn.huwhy.katyusha.vo;

public class UeConfig {
    private String imageUrl;
    private String imagePath;
    private String imageFieldName;
    private int imageMaxSize;
    private String[] imageAllowFiles;

    public UeConfig() {
        this.imageUrl = "/file/uploadItemPic";
        this.imagePath = "/file/list";
        this.imageFieldName = "upfile";
        this.imageMaxSize = 2048;
        this.imageAllowFiles = new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp"};
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageFieldName() {
        return imageFieldName;
    }

    public void setImageFieldName(String imageFieldName) {
        this.imageFieldName = imageFieldName;
    }

    public int getImageMaxSize() {
        return imageMaxSize;
    }

    public void setImageMaxSize(int imageMaxSize) {
        this.imageMaxSize = imageMaxSize;
    }

    public String[] getImageAllowFiles() {
        return imageAllowFiles;
    }

    public void setImageAllowFiles(String[] imageAllowFiles) {
        this.imageAllowFiles = imageAllowFiles;
    }
}
