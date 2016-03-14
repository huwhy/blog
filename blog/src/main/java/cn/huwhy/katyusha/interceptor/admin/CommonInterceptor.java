package cn.huwhy.katyusha.interceptor.admin;

import cn.huwhy.katyusha.common.CacheUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.model.Menu;
import com.google.common.collect.Lists;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.plugin.spring.SpringUtils;

import java.util.List;
import java.util.Map;

public class CommonInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation ai) {
        BaseController c = (BaseController) ai.getController();
        if (ai.getActionKey().startsWith("/katyusha")) {
            c.setAttr("basePath", "/katyusha/admin");

            Map<Integer, Menu> firstMenuMap = SpringUtils.getBean(CacheUtils.class).menus();
            List<Menu> firstMenus = Lists.newArrayList(firstMenuMap.values());
            c.firstMenu(firstMenus);

            int firstMenuId = c.getParaToInt("firstMenuId", 0);
            if (firstMenuId > 0) {
                c.firstMenuId(firstMenuId);
            }

            if (c.firstMenuId() == null) {
                c.firstMenuId(firstMenus.get(0).getId());
            }
            Menu selectedFirstMenu = firstMenuMap.get(c.firstMenuId());
            c.leftMenu(selectedFirstMenu.getSecondMenus());

            int secondMenuId = c.getParaToInt("secondMenuId", 0);
            if (secondMenuId > 0) {
                c.secondMenuId(secondMenuId);
            }
            if (c.secondMenuId() == null) {
                c.secondMenuId(c.leftMenu().get(0).getId());
            }
        } else {
            c.initWebCats();
        }
        ai.invoke();
    }
}
