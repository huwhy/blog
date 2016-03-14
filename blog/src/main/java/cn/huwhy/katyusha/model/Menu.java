package cn.huwhy.katyusha.model;

import com.google.common.base.Function;

import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable {

    public static final Function<Menu, Integer> idValue = new Function<Menu, Integer>() {
        @Override
        public Integer apply(Menu m) {
            return m.getId();
        }
    };

    private int id;
    private int pid;
    private int level;
    private String name;
    private String path;

    private List<Menu> secondMenus;

    public Menu() {
    }

    public Menu(String name, String path) {
        this.name = name;
        this.path = path;
        this.level = 1;
    }

    public Menu(int pid, String name, String path, int level) {
        this.pid = pid;
        this.name = name;
        this.path = path;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Menu> getSecondMenus() {
        return secondMenus;
    }

    public void setSecondMenus(List<Menu> secondMenus) {
        this.secondMenus = secondMenus;
    }
}
