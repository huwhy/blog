package cn.huwhy.katyusha.controller.web;

import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.ItemCatDao;
import cn.huwhy.katyusha.dao.ItemDao;
import cn.huwhy.katyusha.dao.SeoDao;
import cn.huwhy.katyusha.model.Item;
import cn.huwhy.katyusha.model.ItemCat;
import cn.huwhy.katyusha.model.Seo;
import cn.huwhy.katyusha.vo.Crumb;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@Scope("prototype")
@AK("/item")
public class ItemController extends BaseController {

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SeoDao seoDao;

    @ActionKey("{id:\\d{1,}}.html")
    public void index() {
        int id = getUrlParaToInt("id");
        Item item = itemDao.getItem(id, true);
        addCrumb(new Crumb("Home", "/"));
        Map<String, Object> root = new HashMap<>();
        root.put("name", item.getName());
        root.put("summary", item.getSummary());
        root.put("tags", item.getTags());
        ItemCat firstCat = itemCatDao.getByPK(item.getFirstCid());
        if (firstCat != null) {
            addCrumb(new Crumb(firstCat.getName(), firstCat.isParent() ? firstCat.getPath() : ""));
            root.put("firstName", firstCat.getName());
        }
        ItemCat secondCat = itemCatDao.getByPK(item.getSecondCid());
        if (secondCat != null) {
            addCrumb(new Crumb(secondCat.getName(), secondCat.isParent() ? secondCat.getPath() : ""));
            root.put("secondName", secondCat.getName());
        }
        ItemCat thirdCat = itemCatDao.getByPK(item.getThirdCid());
        if (thirdCat != null) {
            addCrumb(new Crumb(thirdCat.getName(), thirdCat.isParent() ? thirdCat.getPath() : ""));
            root.put("thirdName", thirdCat.getName());
        }
        addCrumb(new Crumb(item.getName()));
        Seo seo = seoDao.getItemSeo(0);
        setSeo(seo, root);
        setAttr("item", item);
        view("item");
    }

    @ActionKey("{id:\\d{1,}}")
    public void item() {
        int id = getUrlParaToInt("id");
        Item item = itemDao.getItem(id, true);
        setAttr("item", item);
        view("item");
    }
}
