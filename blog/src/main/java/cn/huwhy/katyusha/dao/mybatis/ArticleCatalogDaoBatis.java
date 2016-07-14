package cn.huwhy.katyusha.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.huwhy.katyusha.model.ArticleCatalog;

public interface ArticleCatalogDaoBatis extends BaseDao<ArticleCatalog, Integer> {

    List<ArticleCatalog> getFirstLevels(@Param("level") int level,
                                        @Param("size") int size);

    List<ArticleCatalog> getChildren(@Param("pid") int pid);


    ArticleCatalog getByName(@Param("name") String name);

    Long hasChildren(@Param("pid") int pid);

}
