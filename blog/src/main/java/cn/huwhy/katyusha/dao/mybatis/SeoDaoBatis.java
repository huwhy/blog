package cn.huwhy.katyusha.dao.mybatis;

import org.apache.ibatis.annotations.Param;

import cn.huwhy.katyusha.enums.SeoKey;
import cn.huwhy.katyusha.model.Seo;

public interface SeoDaoBatis extends BaseDao<Seo, Integer> {

    Seo getByKeyAndTarget(@Param("seoKey") SeoKey seoKey,
                          @Param("targetId") Integer targetId);

}
