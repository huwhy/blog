package cn.huwhy.katyusha.dao;

import cn.huwhy.katyusha.model.Auth;
import com.jfinal.plugin.spring.jdbc.Dao;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDao extends Dao<Auth, Integer> {
    public AuthDao() {
        super("auth", Auth.class);
    }

    public void save(Auth auth) {

    }

    private Auth getMax() {
        return getBySQL("select * from auth order by id desc limit 1");
    }
}
