package cn.huwhy.katyusha.dao;

import cn.huwhy.katyusha.model.ItemPic;
import com.jfinal.plugin.spring.jdbc.Dao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemPicDao extends Dao<ItemPic, Integer> {
    public ItemPicDao() {
        super("item_pic", ItemPic.class);
    }

    public List<ItemPic> getPicsByIid(int iid) {
        return findByWhere("item_id=?", iid);
    }
}
