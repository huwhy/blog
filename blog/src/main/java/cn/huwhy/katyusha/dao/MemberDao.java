package cn.huwhy.katyusha.dao;

import cn.huwhy.katyusha.model.Member;
import com.jfinal.plugin.spring.jdbc.Dao;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao extends Dao<Member, Integer> {
    public MemberDao() {
        super("member", Member.class);
    }

    public Member getMemberByResourceId(String resourceId) {
        return getByWhere("resource_id=?", resourceId);
    }
}
