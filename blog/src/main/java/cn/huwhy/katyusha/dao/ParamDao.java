package cn.huwhy.katyusha.dao;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.enums.ParamType;
import cn.huwhy.katyusha.model.Param;
import com.google.common.collect.Lists;
import com.jfinal.plugin.spring.jdbc.Dao;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.PageParam;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ParamDao extends Dao<Param, Integer> {
    public ParamDao() {
        super("params", Param.class);
    }

    public PageList<Param> findParams(String name, PageParam pageParam) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from params where 1=1");
        List<Object> args = Lists.newArrayList();
        if (StringUtils.notBlank(name)) {
            sql.append(" and name like ?");
            args.add(name + "%");
        }
        sql.append(" order by id asc");
        return findPaging(sql, pageParam, args);
    }

    public Param getDisplayCatParam() {
        Param param = getByPK(Param.ITEM_CAT_DISPLAY_NUM_PARAM_ID);
        if (param == null) {
            param = new Param();
            param.setId(Param.ITEM_CAT_DISPLAY_NUM_PARAM_ID);
            param.setName("一级类目显示数");
            param.setType(ParamType.Str);
            param.setVal("5");
            param.setUpdated(new Date());
            param.setCreated(new Date());
            add(param);
        }
        return param;
    }

    public Param getDisplaySlideParam() {
        Param param = getByPK(Param.SLIDE_DISPLAY_NUM_ID);
        if (param == null) {
            param = new Param();
            param.setId(Param.SLIDE_DISPLAY_NUM_ID);
            param.setName("首页广告图显示数");
            param.setType(ParamType.Str);
            param.setVal("5");
            param.setUpdated(new Date());
            param.setCreated(new Date());
            add(param);
        }
        return param;
    }

    public Param getSiteNameParam() {
        Param param = getByPK(Param.SITE_NAME_ID);
        if (param == null) {
            param = new Param();
            param.setId(Param.SITE_NAME_ID);
            param.setName("网站名");
            param.setType(ParamType.Str);
            param.setVal("Katyusha");
            param.setUpdated(new Date());
            param.setCreated(new Date());
            add(param);
        }
        return param;
    }

    public Param getSiteBeiAnParam() {
        Param param = getByPK(Param.SITE_BEI_AN_ID);
        if (param == null) {
            param = new Param();
            param.setId(Param.SITE_BEI_AN_ID);
            param.setName("网站备案号");
            param.setType(ParamType.Str);
            param.setVal("粤ICP备XXXXXXX号");
            param.setUpdated(new Date());
            param.setCreated(new Date());
            add(param);
        }
        return param;
    }

    public Param getContactParam() {
        Param param = getByPK(Param.CONTACT_ID);
        if (param == null) {
            param = new Param();
            param.setId(Param.CONTACT_ID);
            param.setName("联系页");
            param.setType(ParamType.Str);
            param.setVal("<p>邮箱：test@qq.com</p><p>地址：中国浙江省杭州市XXX街XXX号</p><p>手机号码：12340000222</p>");
            param.setUpdated(new Date());
            param.setCreated(new Date());
            add(param);
        }
        return param;
    }
}
