package cn.huwhy.katyusha.controller.web;

import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.*;
import cn.huwhy.katyusha.enums.ArticleStatus;
import cn.huwhy.katyusha.model.*;
import cn.huwhy.katyusha.vo.Crumb;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.spring.jdbc.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("webHomeController")
@Scope("prototype")
public class HomeController extends BaseController {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private SeoDao seoDao;

    @Autowired
    private ParamDao paramDao;

    @Autowired
    private ArticleCatalogDao articleCatalogDao;

    @Autowired
    private LinkDao linkDao;

    @Autowired
    private LinkDao2 linkDao2;

    public void index() {
        PageList<Article> pageList = articleDao.findArticle(0, 0, 0, null, null, ArticleStatus.display, null, null, null, null, getPageParam());
        setPageData(pageList);
        Seo seo = seoDao.getHomeSeo();
        setSeo(seo, null);
        view("home.html");
    }


    @ActionKey("/contact-me.html")
    public void contactMe() {
        Param param = paramDao.getContactParam();
        setAttr("content", param.getVal());
        view("contact-me");
    }

    @ActionKey("/search.html")
    public void search() {
        String key = getPara("searchKey");
        addCrumb(new Crumb("首页", "/"));
        addCrumb(new Crumb(key));
        PageList<Article> pageList = articleDao.findArticle(0, 0, 0, key, null, ArticleStatus.display, null, null, null, null, getPageParam());
        setPageData(pageList);
        Seo seo = seoDao.getHomeSeo();
        setSeo(seo, null);
        view("catalog.html");
    }

    @ActionKey("/catalog/{id:\\d+}.html")
    public void catalog() {
        int id = getUrlParaToInt("id", 0);
        addCrumb(new Crumb("首页", "/"));
        Map<String, Object> root = new HashMap<>();
        ArticleCatalog cat = articleCatalogDao.getByPK(id);
        root.put("name", cat.getName());
        if (cat.getPid() > 0) {
            ArticleCatalog parentCat = articleCatalogDao.getByPK(cat.getPid());
            if (parentCat != null) {
                if (parentCat.getPid() > 0) {
                    ArticleCatalog ppCat = articleCatalogDao.getByPK(parentCat.getPid());
                    if (ppCat != null) {
                        addCrumb(new Crumb(ppCat.getName(), ppCat.getPath()));
                    }
                }
                addCrumb(new Crumb(parentCat.getName(), parentCat.getPath()));
                root.put("parentName", parentCat.getName());
            }
        }
        addCrumb(new Crumb(cat.getName()));
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
        PageList<Article> pageList = articleDao.findArticle(firstCid, secondCid, thirdCid, null, null, ArticleStatus.display, null, null, null, null, getPageParam());
        setPageData(pageList);
        Seo seo = seoDao.getHomeSeo();
        setSeo(seo, null);
        view("catalog.html");
    }

    @ActionKey({"/article/{id:\\d+}.html", "/article/{id:\\d+}/{title}.html", "/article/{id:\\d+}-{title}.html"})
    public void article() {
        int id = getUrlParaToInt("id");
        Article article = articleDao.getArticleInfo(id);
        setAttr("article", article);
        addCrumb(new Crumb("首页", "/"));
        Map<String, Object> root = new HashMap<>();
        ArticleCatalog cat = articleCatalogDao.getByPK(article.getCatalogId());
        root.put("首页", cat.getName());
        if (cat.getPid() > 0) {
            ArticleCatalog parentCat = articleCatalogDao.getByPK(cat.getPid());
            if (parentCat != null) {
                if (parentCat.getPid() > 0) {
                    ArticleCatalog ppCat = articleCatalogDao.getByPK(parentCat.getPid());
                    if (ppCat != null) {
                        addCrumb(new Crumb(ppCat.getName(), ppCat.getPath()));
                    }
                }
                addCrumb(new Crumb(parentCat.getName(), parentCat.getPath()));
                root.put("parentName", parentCat.getName());
            }
        }
        addCrumb(new Crumb(cat.getName(), cat.getPath()));
        addCrumb(new Crumb(article.getTitle()));
        view("article.html");
    }

    @ActionKey("/hao123.html")
    public void hao123() {
        List<Link> links = linkDao2.findLinksByUid(0);
        setAttr("links", links);
        if (uid() > 0) {
            List<Link> myLinks = linkDao2.findLinksByUid(uid());
            setAttr("myLinks", myLinks);
        }
        view("hao123.html");
    }
}
