package cn.huwhy.katyusha.model;

import java.io.Serializable;

public class ItemProp implements Serializable {

    private int id;
    private int iid;
    private int propId;
    private int valueId;
    private String val;

    public ItemProp() {
    }

    public ItemProp(int iid, int propId, int valueId, String val) {
        this.iid = iid;
        this.propId = propId;
        this.valueId = valueId;
        this.val = val;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
