package cn.huwhy.katyusha.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.huwhy.katyusha.dao.mybatis.LinkDaoBatis;
import cn.huwhy.katyusha.enums.LinkType;
import cn.huwhy.katyusha.model.Link;

@Service
public class LinkBiz {

    @Autowired
    private LinkDaoBatis linkDao;

    public Link get(Integer id) {
        return linkDao.get(id);
    }

    @Transactional
    public void save(Link link) {
        linkDao.save(link);
    }

    @Transactional
    public void update(Link link) {
        linkDao.update(link);
    }

    public List<Link> findLinksByUserId(int userId) {
        return linkDao.findLinksByUserId(userId, LinkType.navigation);
    }
}
