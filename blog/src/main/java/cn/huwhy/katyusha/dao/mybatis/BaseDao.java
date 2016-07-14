package cn.huwhy.katyusha.dao.mybatis;

import java.io.Serializable;
import java.util.List;

import com.jfinal.plugin.mybatis.Term;

public interface BaseDao<T, PK extends Serializable> {

    void save(T entity);

    void update(T entity);

    T get(PK pk);

    void delete(PK pk);

    List<T> findPaging(Term term);
}
