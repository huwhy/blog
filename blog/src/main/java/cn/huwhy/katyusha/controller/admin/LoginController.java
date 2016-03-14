package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.common.utils.MD5Utils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.UserDao;
import cn.huwhy.katyusha.model.User;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.core.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@AK("/katyusha")
public class LoginController extends BaseController {

    @Autowired
    private UserDao userDao;

    @ActionKey("login.html")
    public void loginHtml() {
        view("login");
    }

    @ActionKey(method = RequestMethod.POST)
    public void login() {
        String username = getPara("username");
        String password = getPara("password");
        User user = userDao.getUserForLogin(username, MD5Utils.string2MD5(password));
        if (user != null && !user.isLocked()) {
            setAdmin(user);
            redirect("/katyusha/admin/");
        } else {
            redirect("/katyusha/login.html");
        }
    }

    @ActionKey("/logout.html")
    public void logout() {
        setAdmin(null);
        redirect("/katyusha/login.html");
    }
}
