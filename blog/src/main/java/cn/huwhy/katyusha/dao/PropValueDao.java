package cn.huwhy.katyusha.dao;

import cn.huwhy.katyusha.model.PropValue;
import com.google.common.collect.Maps;
import com.jfinal.plugin.spring.jdbc.Dao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PropValueDao extends Dao<PropValue, Integer> {

    public PropValueDao() {
        super("prop_value", PropValue.class);
    }

    public List<PropValue> findValuesByPropId(int propId) {
        return findByWhere("prop_id=?", propId);
    }

    public Map<Integer, PropValue> findIdValuesByPropId(int propId) {
        List<PropValue> pvs = findByWhere("prop_id=?", propId);
        if (pvs.isEmpty()) {
            return new HashMap<>();
        } else {
            return Maps.uniqueIndex(pvs, PropValue.idValue);
        }
    }

    public PropValue getByPidAndVal(int id, String val) {
        return getByWhere("prop_id=? and value=?", id, val);
    }

    public Map<Integer, PropValue> getValuesByIds(Integer[] ids) {
        List<PropValue> propValues = findByWhere(inSql("id", ids.length), ids);
        return Maps.uniqueIndex(propValues, PropValue.idValue);
    }
}
