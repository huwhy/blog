package cn.huwhy.katyusha.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.huwhy.katyusha.dao.mybatis.MenuDaoBatis;
import cn.huwhy.katyusha.model.Menu;

@Service
public class MenuBiz {

    @Autowired
    private MenuDaoBatis menuDao;

    public List<Menu> getFirstMenus() {
        List<Menu> menus = menuDao.getFirst();
        if (menus != null) {
            for (Menu m1 : menus) {
                m1.setSecondMenus(menuDao.getSecond(m1.getId()));
            }
        }
        return menus;
    }

    public List<Menu> getSecondMenus(int pid) {
        return menuDao.getSecond(pid);
    }

}
