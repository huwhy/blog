package cn.huwhy.katyusha.model;

import cn.huwhy.katyusha.enums.MemberSource;
import com.jfinal.annotation.EnumVal;
import com.jfinal.annotation.EnumValType;

import java.io.Serializable;
import java.util.Date;

public class Member implements Serializable {

    private int id;
    private String name;
    private String email;
    private String nick;
    private String password;
    @EnumVal(EnumValType.Name)
    private MemberSource source;
    private String sourceId;
    private String header;
    private Date lastLogin;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MemberSource getSource() {
        return source;
    }

    public void setSource(MemberSource source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
