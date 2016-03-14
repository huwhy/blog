package cn.huwhy.katyusha.dao;

import cn.huwhy.katyusha.model.Link;
import com.jfinal.plugin.spring.jdbc.Dao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LinkDao2 extends Dao<Link, Integer> {

    public LinkDao2() {
        super("link", Link.class);
    }

    public List<Link> findLinksByUid(int uid) {
        return findByWhere("creator=?", uid);
    }

}
