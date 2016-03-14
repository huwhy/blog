package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.common.json.Json;
import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.common.TemplateUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.ArticleCatalogDao;
import cn.huwhy.katyusha.model.ArticleCatalog;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.core.RequestMethod;
import com.jfinal.plugin.spring.jdbc.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;

@AK("/katyusha/admin/catalog")
@Controller
@Scope("prototype")
public class ArticleCatalogController extends BaseController {

    @Autowired
    private ArticleCatalogDao catalogDao;

    @ActionKey({"list"})
    public void list() {
        String name = getPara("name");
        int level = getParaToInt("level", 0);
        int pid = getParaToInt("pid", -1);
        Boolean isParent = getParaToBoolean("parent");
        PageList<ArticleCatalog> pageList = catalogDao.findCats(pid, name, level, isParent, getPageParam());
        setPageData(pageList);
        view("catalog/list");
    }


    @ActionKey
    public void add() {
        view("catalog/add");
    }

    @ActionKey
    public void edit() {
        int id = getParaToInt("id");
        ArticleCatalog cat = catalogDao.getByPK(id);
        if (cat != null) {
            ArticleCatalog parentCat = catalogDao.getByPK(cat.getPid());
            if (parentCat != null) {
                cat.setParentName(parentCat.getName());
            }
            setAttr("cat", cat);
        }
        view("catalog/edit");
    }

    @ActionKey
    public void sel() {
        String name = getPara("name");
        int level = getParaToInt("level", 0);
        int pid = getParaToInt("pid", -1);
        Boolean isParent = getParaToBoolean("parent");
        PageList<ArticleCatalog> pageList = catalogDao.findCats(pid, name, level, isParent, getPageParam());
        setPageData(pageList);
        String html = TemplateUtils.html("admin/catalog/sel", getRequest());
        renderJson(html);
    }

    @ActionKey("/sel/query")
    public void selQuery() {
        String name = getPara("name");
        int level = getParaToInt("level", 0);
        int pid = getParaToInt("pid", -1);
        Boolean isParent = getParaToBoolean("parent", true);
        PageList<ArticleCatalog> pageList = catalogDao.findCats(pid, name, level, isParent, getPageParam());
        setPageData(pageList);
        String html = TemplateUtils.html("admin/catalog/sel-list", getRequest());
        Json json = Json.SUCC("");
        json.setData(html);
        renderJson(json);
    }

    @ActionKey(method = RequestMethod.POST)
    public void save() {
        int id = getParaToInt("id", 0);
        String name = getPara("name");
        Boolean parent = getParaToBoolean("parent");
        int pid = getParaToInt("pid", 0);
        if (StringUtils.isBlank(name) || name.length() < 2 || name.length() > 10) {
            renderJson(Json.ERR("名称长度为2-10"));
            return;
        }
        ArticleCatalog p = catalogDao.getByPK(pid);
        if (pid > 0 && p == null) {
            renderJson(Json.ERR("父类目不存在"));
            return;
        }

        if (parent == null) {
            renderJson(Json.ERR("设置是否父类目"));
            return;
        }

        int level = pid > 0 ? p.getLevel() + 1 : 1;

        if (parent && level == 3) {
            renderJson(Json.ERR("第三级类目不能设为父类目"));
            return;
        }

        ArticleCatalog cat = catalogDao.getByName(name);
        if (cat != null && cat.getId() != id) {
            renderJson(Json.ERR("类目名已存在"));
            return;
        }
        if (cat == null) {
            cat = new ArticleCatalog();
        } else if (cat.isParent() && !parent) {
            if (catalogDao.hasChildren(cat.getId())) {
                renderJson(Json.ERR("类目下已有子类目，不能设为非父类目"));
                return;
            }
        }
        cat.setName(name);
        cat.setPid(pid);
        cat.setParent(parent);
        cat.setLevel(level);

        if (id > 0) {
            cat.setId(id);
            catalogDao.update(cat);
        } else {
            cat.setUpdated(new Date());
            cat.setCreated(new Date());
            catalogDao.add(cat);
        }
        renderJson(new Json(Json.SUCCESS, "操作成功"));
    }

    @ActionKey(method = RequestMethod.POST)
    public void del() {
        int cid = getParaToInt("cid", 0);
        if (cid > 0) {
            catalogDao.deleteById(cid);
        }
        renderJson(new Json(Json.SUCCESS, "删除成功"));
    }
}
