package cn.huwhy.katyusha.config;

import cn.huwhy.katyusha.controller.web.CatController;
import cn.huwhy.katyusha.controller.web.HomeController;
import cn.huwhy.katyusha.controller.web.ItemController;
import cn.huwhy.katyusha.controller.web.LinkController;
import com.jfinal.config.Routes;

public class WebRoutes extends Routes {
    @Override
    public void config() {
        add("/", HomeController.class, "blog");
        add("cat", CatController.class, "blog");
        add(ItemController.class, "blog");
        add(LinkController.class, "blog");
    }

}
