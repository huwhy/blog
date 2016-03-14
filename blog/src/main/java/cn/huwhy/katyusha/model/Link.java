package cn.huwhy.katyusha.model;

import cn.huwhy.katyusha.enums.LinkType;
import com.jfinal.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Link implements Serializable {
    public static final Pattern PATTERN = Pattern
            .compile("(http://|https://){1}([^\\.]+)\\.([^\\.]+)\\.([^\\./]{2,5})");


    private int id;
    private String name;
    private String url;

    @EnumVal(EnumValType.Name)
    private LinkType type;

    private Date created;
    private int creator;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LinkType getType() {
        return type;
    }

    @Transient
    public String getIco() {
        Matcher matcher = PATTERN.matcher(getUrl());
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public void setType(LinkType type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }
}
