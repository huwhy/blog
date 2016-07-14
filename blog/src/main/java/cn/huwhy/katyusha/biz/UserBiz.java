package cn.huwhy.katyusha.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.huwhy.common.utils.MD5Utils;
import cn.huwhy.katyusha.dao.mybatis.UserDaoBatis;
import cn.huwhy.katyusha.model.User;

@Service
public class UserBiz {

    @Autowired
    private UserDaoBatis userDao;

    public User getUserForLogin(String username, String password) {
        return userDao.getUserForLogin(username, MD5Utils.string2MD5(password));
    }

}
