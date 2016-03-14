package cn.huwhy.katyusha.model;

import cn.huwhy.katyusha.enums.ItemStatus;
import com.jfinal.annotation.EnumVal;
import com.jfinal.annotation.EnumValType;
import com.jfinal.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Item implements Serializable {

    private int id;
    private int firstCid;
    private int secondCid;
    private int thirdCid;
    private String name;
    private String summary;
    private String tags;
    //价格 单位分
    private long price;

    private String content;

    @EnumVal(EnumValType.Name)
    private ItemStatus status;

    private String mainPicture;

    private String url;

    private Date updated;

    private Date created;

    @Transient
    private List<ItemPic> picList;
    @Transient
    private List<ItemProp> colorProps;
    @Transient
    private List<ItemProp> sizeProps;
    private String firstName;
    private String secondName;
    private String thirdName;

    public Item() {
    }

    public Item(int firstCid, int secondCid, int thirdCid, String name, String summary, String tags, long price,
            String content, ItemStatus status) {
        this.firstCid = firstCid;
        this.secondCid = secondCid;
        this.thirdCid = thirdCid;
        this.name = name;
        this.summary = summary;
        this.tags = tags;
        this.price = price;
        this.content = content;
        this.status = status;
        this.updated = new Date();
        this.created = new Date();
    }

    public Item(int firstCid, int secondCid, int thirdCid, String name, String summary, String tags, long price,
            String content, ItemStatus status, String mainPicture) {
        this.firstCid = firstCid;
        this.secondCid = secondCid;
        this.thirdCid = thirdCid;
        this.name = name;
        this.summary = summary;
        this.tags = tags;
        this.price = price;
        this.content = content;
        this.status = status;
        this.mainPicture = mainPicture;
        this.updated = new Date();
        this.created = new Date();
    }

    public Item(String name, String picture) {
        this.name = name;
        this.mainPicture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirstCid() {
        return firstCid;
    }

    public void setFirstCid(int firstCid) {
        this.firstCid = firstCid;
    }

    public int getSecondCid() {
        return secondCid;
    }

    public void setSecondCid(int secondCid) {
        this.secondCid = secondCid;
    }

    public int getThirdCid() {
        return thirdCid;
    }

    public void setThirdCid(int thirdCid) {
        this.thirdCid = thirdCid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Transient
    public long getPricePre() {
        return getPrice() / 100;
    }

    @Transient
    public long getPriceEnd() {
        return getPrice() % 100;
    }

    public String getMainPicture() {
        return mainPicture;
    }

    public void setMainPicture(String mainPicture) {
        this.mainPicture = mainPicture;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<ItemPic> getPicList() {
        return picList;
    }

    public void setPicList(List<ItemPic> picList) {
        this.picList = picList;
    }

    public List<ItemProp> getColorProps() {
        return colorProps;
    }

    public void setColorProps(List<ItemProp> colorProps) {
        this.colorProps = colorProps;
    }

    public List<ItemProp> getSizeProps() {
        return sizeProps;
    }

    public void setSizeProps(List<ItemProp> sizeProps) {
        this.sizeProps = sizeProps;
    }

    @Transient
    public boolean isShow() {
        return ItemStatus.display.equals(getStatus());
    }

    @Transient
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Transient
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Transient
    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    @Transient
    public String getCatName() {
        if (thirdCid > 0) {
            return thirdName;
        } else if (secondCid > 0) {
            return secondName;
        } else {
            return firstName;
        }
    }

    @Transient
    public int getCatId() {
        if (thirdCid > 0) {
            return thirdCid;
        } else if (secondCid > 0) {
            return secondCid;
        } else {
            return firstCid;
        }
    }
}
