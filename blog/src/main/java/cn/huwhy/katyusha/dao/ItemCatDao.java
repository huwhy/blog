package cn.huwhy.katyusha.dao;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.model.ItemCat;
import com.jfinal.plugin.spring.jdbc.Dao;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.PageParam;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemCatDao extends Dao<ItemCat, Integer> {
    public ItemCatDao() {
        super("item_cat", ItemCat.class);
    }

    public PageList<ItemCat> findCats(int pid, String name, int level, Boolean isParent, PageParam pageParam) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.*, b.name parentName from item_cat a left join item_cat b on a.pid=b.id where 1=1");
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

    public List<ItemCat> getFirstLevels(int size) {
        return findByWhere("level=? limit ?", 1, size);
    }

    public ItemCat getByName(String name) {
        return getByWhere("name = ? ", name);
    }

    public List<ItemCat> getChildren(int id) {
        return findByWhere("pid=?", id);
    }

    public Boolean hasChildren(int id) {
        Long count = getSingle("select count(1) from item_cat where pid=?", Long.class, id);
        return count > 0;
    }
}
