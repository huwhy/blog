package cn.huwhy.katyusha.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.huwhy.katyusha.enums.LinkType;
import cn.huwhy.katyusha.model.Link;

public interface LinkDaoBatis extends BaseDao<Link, Integer> {

    Link getLink(Integer id);

    List<Link> findLinksByUserId(@Param("userId") int uid,
                                 @Param("type") LinkType type);

}
