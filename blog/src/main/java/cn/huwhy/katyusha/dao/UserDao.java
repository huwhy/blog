package cn.huwhy.katyusha.dao;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.model.User;
import com.jfinal.plugin.spring.jdbc.Dao;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.PageParam;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class UserDao extends Dao<User, Integer> {
    public UserDao() {
        super("users", User.class);
    }

    public User getUserByUsername(String username) {
        return getByWhere("username=?", username);
    }

    public User getUserForLogin(String username, String password) {
        return getByWhere("username=? and password=?", username, password);
    }

    public void resetPassword(int uid, String password) {
        updateByWhere("password=?", "id=?", password, uid);
    }

    public void updateRealName(int uid, String realName) {
        updateByWhere("real_name=?", "id=?", realName, uid);
    }

    public void lock(int uid) {
        updateByWhere("locked=?", "id=?", true, uid);
    }

    public void unlock(int uid) {
        updateByWhere("locked=?", "id=?", false, uid);
    }

    public PageList<User> findUsers(String username, String realName, Boolean locked, Date updatedStart,
            Date updatedEnd, Date createdStart, Date createdEnd, PageParam pageParam) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from users where 1=1");
        List<Object> params = new ArrayList<>();
        if (StringUtils.notBlank(username)) {
            sql.append(" and username like ?");
            params.add(username + "%");
        }
        if (StringUtils.notBlank(realName)) {
            sql.append(" and real_name like ?");
            params.add(realName + "%");
        }

        if (locked != null) {
            sql.append(" and locked=?");
            params.add(locked);
        }

        if (updatedStart != null) {
            sql.append(" and updated >=?");
            params.add(updatedStart);
        }

        if (updatedEnd != null) {
            sql.append(" and updated < ?");
            params.add(updatedEnd);
        }

        if (createdStart != null) {
            sql.append(" and created >=?");
            params.add(createdStart);
        }

        if (createdEnd != null) {
            sql.append(" and created < ?");
            params.add(createdEnd);
        }

        return findPaging(sql, pageParam, params);
    }
}
