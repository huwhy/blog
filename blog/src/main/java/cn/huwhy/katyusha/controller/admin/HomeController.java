package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.katyusha.controller.BaseController;
import com.jfinal.core.AK;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("adminHomeController")
@Scope("prototype")
@AK("/katyusha/admin/")
public class HomeController extends BaseController {

    public void index() {
        view("admin.html");
    }
}
