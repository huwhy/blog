package cn.huwhy.katyusha.controller.web;

import cn.huwhy.katyusha.common.CacheUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.ItemCatDao;
import cn.huwhy.katyusha.dao.ItemDao;
import cn.huwhy.katyusha.dao.SeoDao;
import cn.huwhy.katyusha.dao.SlideDao;
import cn.huwhy.katyusha.enums.ItemStatus;
import cn.huwhy.katyusha.model.Item;
import cn.huwhy.katyusha.model.ItemCat;
import cn.huwhy.katyusha.model.Seo;
import cn.huwhy.katyusha.model.Slide;
import cn.huwhy.katyusha.vo.Crumb;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.spring.jdbc.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
@AK("/cat")
public class CatController extends BaseController {

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private SlideDao slideDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SeoDao seoDao;

    @Autowired
    private CacheUtils cacheUtils;

    @ActionKey("{id:\\d{1,}}.html")
    public void list() {
        int id = getUrlParaToInt("id");
        ItemCat cat = itemCatDao.getByPK(id);
        Map<String, Object> root = new HashMap<>();
        root.put("name", cat.getName());
        setAttr("cat", cat);
        addCrumb(new Crumb("Home", "/"));
        if (cat.getPid() > 0) {
            ItemCat parentCat = itemCatDao.getByPK(cat.getPid());
            if (parentCat != null) {
                if (parentCat.getPid() > 0) {
                    ItemCat ppCat = itemCatDao.getByPK(parentCat.getPid());
                    if (ppCat != null) {
                        addCrumb(new Crumb(ppCat.getName(), ppCat.getPath()));
                    }
                }
                addCrumb(new Crumb(parentCat.getName(), parentCat.getPath()));
                root.put("parentName", parentCat.getName());
            }
        }
        addCrumb(new Crumb(cat.getName()));
        List<Slide> slides = slideDao.findSlides(cacheUtils.slideDisplay());
        setAttr("slides", slides);
        int firstCid = 0, secondCid = 0, thirdCid = 0;
        if (cat != null) {
            if (cat.getLevel() == 1) {
                firstCid = cat.getId();
            } else if (cat.getLevel() == 2) {
                secondCid = cat.getId();
            } else {
                thirdCid = id;
            }
        } else {
            thirdCid = id;
        }
        PageList<Item> pageList = itemDao.findItems(firstCid, secondCid, thirdCid, null, null, null, null, ItemStatus.display, null, null, null, null, getPageParam());
        setPageData(pageList);
        Seo seo = seoDao.getCatSeo(0);
        setSeo(seo, root);
        view("cat");
    }
}
