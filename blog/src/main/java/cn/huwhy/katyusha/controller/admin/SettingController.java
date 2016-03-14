package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.ParamDao;
import cn.huwhy.katyusha.model.Param;
import com.jfinal.core.AK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@AK("/katyusha/admin/setting")
public class SettingController extends BaseController {

    @Autowired
    private ParamDao paramDao;

    public void contact() {
        Param param = paramDao.getContactParam();
        setAttr("param", param);
        view("setting/contact");
    }
}
