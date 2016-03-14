package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.common.json.Json;
import cn.huwhy.katyusha.common.CacheUtils;
import cn.huwhy.katyusha.controller.BaseController;
import com.jfinal.core.AK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@AK("/katyusha/admin/cache")
public class CacheController extends BaseController{

    @Autowired
    private CacheUtils cacheUtils;

    public void index() {
        view("cache/index");
    }

    public void clearMenus() {
        cacheUtils.clearMenus();
        renderJson(Json.SUCC("清理成功"));
    }

    public void clearWebCats() {
        cacheUtils.clearWebCats();
        jsonView(Json.SUCC("清理成功"));
    }
}
