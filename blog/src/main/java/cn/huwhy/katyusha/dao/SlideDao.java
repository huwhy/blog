package cn.huwhy.katyusha.dao;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.enums.SlideStatus;
import cn.huwhy.katyusha.model.Slide;
import com.jfinal.plugin.spring.jdbc.Dao;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.PageParam;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SlideDao extends Dao<Slide, Integer> {
    public SlideDao() {
        super("slide", Slide.class);
    }

    public List<Slide> findSlides(int size) {
        return findByWhere("status=? limit ?", SlideStatus.display.name(), size);
    }

    public PageList<Slide> findSlides(String title, String content, SlideStatus status, PageParam pageParam) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from slide where 1=1 ");
        List<Object> params = new ArrayList<>();
        if (StringUtils.notBlank(title)) {
            sql.append(" and title like ?");
            params.add(title + "%");
        }

        if (StringUtils.notBlank(content)) {
            sql.append(" and content like ?");
            params.add("%" + content + "%");
        }

        if (status != null && status != SlideStatus.unknown) {
            sql.append(" and status=?");
            params.add(status.name());
        }
        return findPaging(sql, pageParam, params);
    }

    public void display(int id) {
        updateByWhere("status=?", "id=?", SlideStatus.display.name(), id);
    }

    public void hide(int id) {
        updateByWhere("status=?", "id=?", SlideStatus.display.name(), id);
    }
}
