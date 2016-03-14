package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.common.json.Json;
import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.common.CacheUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.ParamDao;
import cn.huwhy.katyusha.enums.ParamType;
import cn.huwhy.katyusha.model.ItemCat;
import cn.huwhy.katyusha.model.Param;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.core.RequestMethod;
import com.jfinal.plugin.spring.jdbc.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.regex.Pattern;

import static cn.huwhy.katyusha.enums.ParamType.*;

@Controller
@Scope("prototype")
@AK("/katyusha/admin/param")
public class ParamController extends BaseController {

    @Autowired
    private ParamDao paramDao;

    @Autowired
    private CacheUtils cacheUtils;

    public void list() {
        String name = getPara("name");
        PageList<Param> pageList = paramDao.findParams(name, getPageParam());
        setPageData(pageList);
        view("param/list");
    }

    public void add() {
        view("param/add");
    }

    public void edit() {
        int id = getParaToInt("id", 0);
        Param param = paramDao.getByPK(id);
        setAttr("param", param);
        view("param/edit");
    }

    @ActionKey(method = RequestMethod.POST)
    public void save() {
        int id = getParaToInt("id", 0);
        String name = getPara("name");
        String typeName = getPara("type");
        String val = getPara("val");
        if (id == 0 && StringUtils.isBlank(name)) {
            jsonView(Json.ERR("参数名不能为空"));
            return;
        }
        if (StringUtils.isBlank(val)) {
            jsonView(Json.ERR("参数值不能为空"));
            return;
        }

        if (id == 0 && (StringUtils.isBlank(typeName) || typeName.equals(unknown.name()))) {
            jsonView(Json.ERR("请选择参数类型"));
            return;
        }

        if (Bool.name().equals(typeName)) {
            if (!BoolP.matcher(val).matches()) {
                jsonView(Json.ERR("参数值请设置： 'true' 或 'false'"));
                return;
            }
        }
        if (Int.name().equals(typeName)) {
            if (!IntP.matcher(val).matches()) {
                jsonView(Json.ERR("参数值请设置: 阿拉伯数字"));
                return;
            }
        }
        if (Db.name().equals(typeName)) {
            if (!DbP.matcher(val).matches()) {
                jsonView(Json.ERR("参数值请设置: 精度形如'9.9'"));
                return;
            }
        }
        Param param = new Param();
        param.setVal(val);
        if (id == 0) {
            param.setName(name);
            param.setType(ParamType.valueOf(typeName));
            param.setUpdated(new Date());
            param.setCreated(new Date());
            paramDao.add(param);
        } else {
            param.setId(id);
            paramDao.update(param);
            if (id == Param.ITEM_CAT_DISPLAY_NUM_PARAM_ID) {
                cacheUtils.clearCache(ItemCat.class.getSimpleName());
            }
        }
        jsonView(Json.SUCC("保存成功"));
    }

    private static Pattern BoolP = Pattern.compile("(true|false)");
    private static Pattern IntP = Pattern.compile("\\d+");
    private static Pattern DbP = Pattern.compile("\\d+\\.\\d+");
}
