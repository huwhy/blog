package cn.huwhy.katyusha.dao.mybatis;

import org.apache.ibatis.annotations.Param;

import cn.huwhy.katyusha.model.User;

public interface UserDaoBatis extends BaseDao<User, Integer> {

    User getUserForLogin(@Param("username") String username,
                         @Param("password") String password);

}
