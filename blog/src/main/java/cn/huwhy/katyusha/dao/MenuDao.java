package cn.huwhy.katyusha.dao;

import cn.huwhy.katyusha.model.Menu;
import com.jfinal.plugin.spring.jdbc.Dao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuDao extends Dao<Menu, Integer> {
    public MenuDao() {
        super("menu", Menu.class);
    }

    public List<Menu> firstMenus() {
        return findByWhere("pid=0 and level=1");
    }

    public List<Menu> secondMenus(int pid) {
        return findByWhere("pid=? and level=2", pid);
    }

    public List<Menu> thirdMenus(int pid) {
        return findByWhere("pid=? and level=3", pid);
    }
}
