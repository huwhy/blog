package cn.huwhy.katyusha.dao;

import cn.huwhy.katyusha.model.Prop;
import com.jfinal.plugin.spring.jdbc.Dao;
import org.springframework.stereotype.Repository;

@Repository
public class PropDao extends Dao<Prop, Integer> {
    public PropDao() {
        super("prop", Prop.class);
    }
}
