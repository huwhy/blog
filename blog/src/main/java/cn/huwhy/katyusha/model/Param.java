package cn.huwhy.katyusha.model;

import cn.huwhy.katyusha.enums.ParamType;
import com.jfinal.annotation.EnumVal;
import com.jfinal.annotation.EnumValType;
import com.jfinal.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

public class Param implements Serializable {
    public static final int ITEM_CAT_DISPLAY_NUM_PARAM_ID = 2;
    public static final int SLIDE_DISPLAY_NUM_ID = 3;
    public static final int SITE_NAME_ID = 4;
    public static final int SITE_BEI_AN_ID = 5;
    public static final int CONTACT_ID = 6;

    private int id;
    private String name;

    @EnumVal(EnumValType.Name)
    private ParamType type;
    private String val;
    private Date updated;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParamType getType() {
        return type;
    }

    public void setType(ParamType type) {
        this.type = type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
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

    @Transient
    public String getPath() {
        if (CONTACT_ID == id) {
            return "/setting/contact";
        } else {
            return null;
        }
    }
}
