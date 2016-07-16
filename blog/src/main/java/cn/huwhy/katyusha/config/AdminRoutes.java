package cn.huwhy.katyusha.config;


import com.jfinal.config.Routes;
import com.jfinal.core.Controller;

import cn.huwhy.katyusha.controller.admin.ArticleCatalogController;
import cn.huwhy.katyusha.controller.admin.ArticleController;
import cn.huwhy.katyusha.controller.admin.CacheController;
import cn.huwhy.katyusha.controller.admin.HomeController;
import cn.huwhy.katyusha.controller.admin.LoginController;
import cn.huwhy.katyusha.controller.admin.ParamController;
import cn.huwhy.katyusha.controller.admin.SeoController;
import cn.huwhy.katyusha.controller.admin.SettingController;
import cn.huwhy.katyusha.controller.admin.SlideController;
import cn.huwhy.katyusha.controller.admin.UserController;

public class AdminRoutes extends Routes {
    @Override
    public void config() {
        addRoute(HomeController.class);
        addRoute(LoginController.class);
        addRoute(SlideController.class);
        addRoute(UserController.class);
        addRoute(ParamController.class);
        addRoute(CacheController.class);
        addRoute(SeoController.class);
        addRoute(SettingController.class);
        addRoute(ArticleCatalogController.class);
        addRoute(ArticleController.class);
    }

    private void addRoute(Class<? extends Controller> clazz) {
        add(clazz, "admin");
    }
}
