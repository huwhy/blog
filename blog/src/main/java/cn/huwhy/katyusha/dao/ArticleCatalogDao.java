package cn.huwhy.katyusha.dao;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.model.ArticleCatalog;
import com.jfinal.plugin.spring.jdbc.Dao;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.PageParam;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleCatalogDao extends Dao<ArticleCatalog, Integer> {
    public ArticleCatalogDao() {
        super("article_catalog", ArticleCatalog.class);
    }

    public PageList<ArticleCatalog> findCats(int pid, String name, int level, Boolean isParent, PageParam pageParam) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select a.*, b.name parentName from article_catalog a left join article_catalog b on a.pid=b.id where 1=1");
        List<Object> params = new ArrayList<>();
        if (pid >= 0) {
            sql.append(" and a.pid=?");
            params.add(pid);
        }
        if (StringUtils.notBlank(name)) {
            sql.append(" and a.name like ?");
            params.add(name + "%");
        }
        if (isParent != null) {
            sql.append(" and a.parent = ? ");
            params.add(isParent);
        }
        if (level > 0) {
            sql.append(" and a.level=?");
            params.add(level);
        }
        sql.append(" order by a.id asc");
        return findPaging(sql, pageParam, params);
    }

    public List<ArticleCatalog> getFirstLevels(int size) {
        return findByWhere("level=? limit ?", 1, size);
    }

    public ArticleCatalog getByName(String name) {
        return getByWhere("name = ? ", name);
    }

    public List<ArticleCatalog> getChildren(int id) {
        return findByWhere("pid=?", id);
    }

    public Boolean hasChildren(int id) {
        Long count = getSingle("select count(1) from article_catalog where pid=?", Long.class, id);
        return count > 0;
    }
}
