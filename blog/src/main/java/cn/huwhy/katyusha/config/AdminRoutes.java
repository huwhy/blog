package cn.huwhy.katyusha.config;


import cn.huwhy.katyusha.controller.admin.*;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;

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
        add(clazz, "");
    }
}
