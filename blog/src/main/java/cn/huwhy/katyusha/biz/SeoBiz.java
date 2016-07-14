package cn.huwhy.katyusha.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.huwhy.katyusha.dao.mybatis.SeoDaoBatis;
import cn.huwhy.katyusha.enums.SeoKey;
import cn.huwhy.katyusha.model.Seo;

@Service
public class SeoBiz {

    @Autowired
    private SeoDaoBatis seoDao;


    public Seo getHomeSeo() {
        return getSeo(SeoKey.Home, 0);
    }

    public Seo getCatSeo(Integer catId) {
        return getSeo(SeoKey.Cat, catId);
    }

    public Seo getItemSeo(Integer itemId) {
        return getSeo(SeoKey.Item, itemId);
    }



    private Seo getSeo(SeoKey key, Integer targetId) {
        return seoDao.getByKeyAndTarget(key, targetId);
    }

}
