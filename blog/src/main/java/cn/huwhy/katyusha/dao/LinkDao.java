package cn.huwhy.katyusha.dao;

import cn.huwhy.katyusha.enums.LinkType;
import cn.huwhy.katyusha.model.Link;
import com.jfinal.plugin.spring.jdbc.Dao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LinkDao extends Dao<Link, Integer> {
    public LinkDao() {
        super("link", Link.class);
    }

    public List<Link> findLinksByUid(int uid) {
        return findByWhere("creator=? and type=?", uid, LinkType.navigation.name());
    }
}
