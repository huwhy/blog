package cn.huwhy.katyusha.dao;

import cn.huwhy.katyusha.model.ItemProp;
import com.jfinal.plugin.spring.jdbc.Dao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemPropDao extends Dao<ItemProp, Integer> {
    public ItemPropDao() {
        super("item_prop", ItemProp.class);
    }

    public List<ItemProp> getPropsByIid(int iid) {
        return findByWhere("iid=?", iid);
    }
}
