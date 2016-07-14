package cn.huwhy.katyusha.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.huwhy.katyusha.model.Menu;

public interface MenuDaoBatis extends BaseDao<Menu, Integer> {

    List<Menu> getFirst();

    List<Menu> getSecond(@Param("pid") Integer pid);

}
