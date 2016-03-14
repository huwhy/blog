package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.common.json.Json;
import cn.huwhy.common.utils.MD5Utils;
import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.UserDao;
import cn.huwhy.katyusha.model.User;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.core.RequestMethod;
import com.jfinal.plugin.spring.jdbc.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
@Scope("prototype")
@AK("/katyusha/admin/user")
public class UserController extends BaseController {

    @Autowired
    private UserDao userDao;

    public void list() {
        String username = getPara("username");
        String realName = getPara("realName");
        Boolean locked = getParaToBoolean("locked");
        Date updatedStart = getParaToDate("updatedStart");
        Date updatedEnd = getParaToDate("updatedEnd");
        Date createdStart = getParaToDate("createdStart");
        Date createdEnd = getParaToDate("createdEnd");

        PageList<User> pageList = userDao.findUsers(username, realName, locked, updatedStart, updatedEnd, createdStart, createdEnd, getPageParam());
        setPageData(pageList);
        view("user/list");
    }

    public void add() {
        view("user/add");
    }

    @ActionKey(method = RequestMethod.POST)
    public void save() {
        String username = getPara("username");
        if (StringUtils.isBlank(username) || username.length() < 2 || username.length() > 30) {
            jsonView(Json.ERR("帐号必须为2 - 30 字符"));
            return;
        }
        User user = userDao.getUserByUsername(username);

        if (user != null) {
            jsonView(Json.ERR("帐号已存在"));
            return;
        }

        String realName = getPara("realName");
        if (StringUtils.isBlank(realName) || realName.length() < 2 || realName.length() > 30) {
            jsonView(Json.ERR("姓名必须为2 - 30 字符"));
            return;
        }

        String password = getPara("password");
        if (StringUtils.isBlank(password) || password.length() < 2 || password.length() > 30) {
            jsonView(Json.ERR("密码必须为2 - 30 字符"));
            return;
        }

        user = new User();
        user.setUsername(username);
        user.setRealName(realName);
        user.setPassword(MD5Utils.string2MD5(password));
        user.setUpdated(new Date());
        user.setCreated(new Date());

        userDao.add(user);
        jsonView(Json.SUCC("添加成功"));
    }

    @ActionKey(method = RequestMethod.POST)
    public void resetPassword() {
        int id = getParaToInt("id", 0);
        String password = getPara("password");
        if (StringUtils.isBlank(password) || password.length() < 2 || password.length() > 30) {
            jsonView(Json.ERR("密码必须为2 - 30 字符"));
            return;
        }
        userDao.resetPassword(id, MD5Utils.string2MD5(password));

        jsonView(Json.SUCC("修改密码成功"));
    }

    @ActionKey(method = RequestMethod.POST)
    public void resetRealName() {
        int id = getParaToInt("id", 0);

        String realName = getPara("realName");
        if (StringUtils.isBlank(realName) || realName.length() < 2 || realName.length() > 30) {
            jsonView(Json.ERR("姓名必须为2 - 30 字符"));
            return;
        }

        userDao.updateRealName(id, realName);
        jsonView(Json.SUCC("修改姓名成功"));
    }

    @ActionKey(method = RequestMethod.POST)
    public void lock() {
        int id = getParaToInt("id", 0);
        userDao.lock(id);
        jsonView(Json.SUCC("锁定成功"));
    }

    @ActionKey(method = RequestMethod.POST)
    public void unlock() {
        int id = getParaToInt("id", 0);
        userDao.unlock(id);
        jsonView(Json.SUCC("解锁成功"));
    }
}
