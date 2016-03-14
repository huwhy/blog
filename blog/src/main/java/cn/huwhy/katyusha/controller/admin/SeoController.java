package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.common.json.Json;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.SeoDao;
import cn.huwhy.katyusha.enums.SeoKey;
import cn.huwhy.katyusha.model.Seo;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.core.RequestMethod;
import com.jfinal.plugin.spring.jdbc.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
@Scope("prototype")
@AK("/katyusha/admin/seo")
public class SeoController extends BaseController {

    @Autowired
    private SeoDao seoDao;

    @ActionKey({"list", "index"})
    public void index() {
        String seoKeyName = getPara("seoKey", "Unknown");
        Integer targetId = getParaToInt("targetId");
        PageList<Seo> pageList = seoDao.findSeo(SeoKey.valueOf(seoKeyName), targetId, getPageParam());
        setPageData(pageList);
        view("seo/list");
    }

    @ActionKey
    public void add() {
        String seoKeyName = getPara("seoKey", "Unknown");
        Integer targetId = getParaToInt("targetId");
        SeoKey seoKey = SeoKey.valueOf(seoKeyName);
        if (seoKey == SeoKey.Unknown) {
            renderError(505);
            return;
        }
        if ((seoKey == SeoKey.Cat || seoKey == SeoKey.Item) && targetId == null) {
            renderError(505);
            return;
        }
        Seo seo = null;
        switch (SeoKey.valueOf(seoKeyName)) {
            case Home:
                seo = seoDao.getHomeSeo();
                break;
            case Cat:
                seo = seoDao.getCatSeo(targetId);
                break;
            case Item:
                seo = seoDao.getItemSeo(targetId);
                break;
        }
        setAttr("seo", seo);
        view("seo/add");
    }

    @ActionKey(method = RequestMethod.POST)
    public void save() {
        int id = getParaToInt("id", 0);
        Integer targetId = getParaToInt("targetId", 0);
        String seoKeyName = getPara("seoKey", "Unknown");
        SeoKey seoKey = SeoKey.valueOf(seoKeyName);
        if (seoKey == SeoKey.Unknown) {
            renderError(505);
            return;
        }
        String title = getPara("title", "");
        String description = getPara("description", "");
        String keywords = getPara("keywords", "");
        Seo seo = new Seo();
        seo.setId(id);
        seo.setTargetId(targetId);
        seo.setSeoKey(seoKey);
        seo.setTitle(title);
        seo.setDescription(description);
        seo.setKeywords(keywords);
        seo.setUpdated(new Date());
        seo.setCreated(new Date());
        seoDao.save(seo);
        renderJson(Json.SUCC("设置Seo成功"));
    }
}
