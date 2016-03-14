package cn.huwhy.katyusha.common;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.dao.ArticleCatalogDao;
import cn.huwhy.katyusha.dao.ItemCatDao;
import cn.huwhy.katyusha.dao.MenuDao;
import cn.huwhy.katyusha.dao.ParamDao;
import cn.huwhy.katyusha.model.ArticleCatalog;
import cn.huwhy.katyusha.model.ItemCat;
import cn.huwhy.katyusha.model.Menu;
import cn.huwhy.katyusha.model.Param;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CacheUtils {
    private static final Map<String, Object> cacheMap = Collections.synchronizedMap(new HashMap<String, Object>());

    private CacheUtils() {
    }

    @Autowired
    private ParamDao paramDao;

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private ArticleCatalogDao catalogDao;

    @Autowired
    private MenuDao menuDao;

    public void cache(String key, Object val) {
        cacheMap.put(key, val);
    }

    public <T> T getCache(String key) {
        return (T) cacheMap.get(key);
    }

    public void clearCache(String key) {
        cacheMap.remove(key);
    }

    public List<ItemCat> getWebCats() {
        List<ItemCat> catList = getCache(ItemCat.class.getSimpleName());
        if (catList == null || catList.isEmpty()) {
            Param param = paramDao.getByPK(Param.ITEM_CAT_DISPLAY_NUM_PARAM_ID);
            catList = itemCatDao.getFirstLevels(Integer.parseInt(param.getVal()));
            for (ItemCat cat : catList) {
                cat.setChildren(itemCatDao.getChildren(cat.getId()));
                for (ItemCat cat2 : cat.getChildren()) {
                    cat2.setChildren(itemCatDao.getChildren(cat2.getId()));
                }
            }
            cache(ItemCat.class.getSimpleName(), catList);
        }
        return catList;
    }

    public List<ArticleCatalog> getArticleCats() {
        List<ArticleCatalog> catList = getCache(ArticleCatalog.class.getSimpleName());
        if (catList == null || catList.isEmpty()) {
            Param param = paramDao.getByPK(Param.ITEM_CAT_DISPLAY_NUM_PARAM_ID);
            catList = catalogDao.getFirstLevels(Integer.parseInt(param.getVal()));
            for (ArticleCatalog cat : catList) {
                cat.setChildren(catalogDao.getChildren(cat.getId()));
                for (ArticleCatalog cat2 : cat.getChildren()) {
                    cat2.setChildren(catalogDao.getChildren(cat2.getId()));
                }
            }
            cache(ArticleCatalog.class.getSimpleName(), catList);
        }
        return catList;
    }

    public void clearWebCats() {
        clearCache(ItemCat.class.getSimpleName());
    }

    public int slideDisplay() {
        Integer num = getCache("SLIDE_DISPLAY_NUM");
        if (num == null) {
            Param param = paramDao.getByPK(Param.SLIDE_DISPLAY_NUM_ID);
            num = Integer.parseInt(param.getVal());
            cache("SLIDE_DISPLAY_NUM", num);
        }
        return num;
    }

    public void clearSlideDisplay() {
        clearCache("SLIDE_DISPLAY_NUM");
    }

    public String siteName() {
        String siteName = getCache("SITE_NAME");
        if (StringUtils.isBlank(siteName)) {
            Param param = paramDao.getByPK(Param.SITE_NAME_ID);
            siteName = param.getVal();
            cache("SITE_NAME", siteName);
        }
        return siteName;
    }

    public String beiAnNo() {
        String beiAnNo = getCache("SITE_BEI_AN_NO");
        if (StringUtils.isBlank(beiAnNo)) {
            Param param = paramDao.getByPK(Param.SITE_BEI_AN_ID);
            beiAnNo = param.getVal();
            cache("SITE_BEI_AN_NO", beiAnNo);
        }
        return beiAnNo;
    }

    public Map<Integer, Menu> menus() {
        List<Menu> menus = getCache("menus");
        if (menus == null) {
            menus = menuDao.firstMenus();
            for (Menu m1 : menus) {
                m1.setSecondMenus(menuDao.secondMenus(m1.getId()));
            }

            cache("menus", menus);
        }
        return Maps.uniqueIndex(menus, Menu.idValue);
    }

    public void clearMenus() {
        clearCache("menus");
    }
}
