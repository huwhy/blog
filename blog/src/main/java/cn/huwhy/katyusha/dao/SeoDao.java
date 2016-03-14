package cn.huwhy.katyusha.dao;

import cn.huwhy.common.utils.BeanUtils;
import cn.huwhy.katyusha.enums.SeoKey;
import cn.huwhy.katyusha.model.Seo;
import com.jfinal.plugin.spring.jdbc.Dao;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.PageParam;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class SeoDao extends Dao<Seo, Integer> {
    public SeoDao() {
        super("seo", Seo.class);
    }

    public PageList<Seo> findSeo(SeoKey seoKey, Integer targetId, PageParam pageParam) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from seo where 1=1");
        List<Object> args = new ArrayList<>();
        if (seoKey != null && seoKey != SeoKey.Unknown) {
            sql.append(" and seo_key = ?");
            args.add(seoKey.name());
        }

        if (targetId != null) {
            sql.append(" and target_id=?");
            args.add(targetId);
        }

        return findPaging(sql, pageParam, args);
    }

    public Seo getHomeSeo() {
        return getSeo(SeoKey.Home, 0);
    }

    public Seo getCatSeo(Integer catId) {
        return getSeo(SeoKey.Cat, catId);
    }

    public Seo getItemSeo(Integer itemId) {
        return getSeo(SeoKey.Item, itemId);
    }

    public void save(Seo seo) {
        Seo seoDb = getSeo(seo.getSeoKey(), seo.getTargetId());
        if (seoDb != null) {
            BeanUtils.copyProperties(seo, seoDb, "id", "seoKey", "targetId", "created", "updated");
            update(seoDb);
        } else {
            seo.setUpdated(new Date());
            seo.setCreated(new Date());
            add(seo);
        }
    }

    private Seo getSeo(SeoKey key, Integer targetId) {
        return getByWhere("seo_key =? and target_id=?", key.name(), targetId);
    }
}
