package cn.huwhy.katyusha.model;

import com.google.common.base.Function;
import com.jfinal.annotation.Transient;

import java.io.Serializable;

public class PropValue implements Serializable {

    public static final Function<PropValue, Integer> idValue = new Function<PropValue, Integer>() {
        @Override
        public Integer apply(PropValue pv) {
            return pv.getId();
        }
    };

    private int id;
    private int propId;
    private String value;

    @Transient
    private boolean checked;

    public PropValue() {
    }

    public PropValue(int propId, String value) {
        this.propId = propId;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
