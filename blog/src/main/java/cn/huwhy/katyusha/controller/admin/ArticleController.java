package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.common.json.Json;
import cn.huwhy.common.utils.DateUtils;
import cn.huwhy.common.utils.FileUtils;
import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.common.TemplateUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.ArticleCatalogDao;
import cn.huwhy.katyusha.dao.ArticleDao;
import cn.huwhy.katyusha.enums.ArticleStatus;
import cn.huwhy.katyusha.model.Article;
import cn.huwhy.katyusha.model.ArticleCatalog;
import cn.huwhy.katyusha.vo.Crumb;
import com.jfinal.aop.Tx;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.core.JFinal;
import com.jfinal.core.RequestMethod;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.spring.jdbc.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@Scope("prototype")
@AK("/katyusha/admin/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleCatalogDao catalogDao;

    @Autowired
    private ArticleDao articleDao;

    @ActionKey({"list"})
    public void list() {
        int cid = getParaToInt("cid", 0);
        String title = getPara("title");
        String tag = getPara("tag");
        String status = getPara("status", "unknown");
        Date updatedStart = getParaToDate("updatedStart");
        Date updatedEnd = getParaToDate("updatedEnd");
        Date createdStart = getParaToDate("createdStart");
        Date createdEnd = getParaToDate("createdEnd");

        ArticleCatalog cat = catalogDao.getByPK(cid);
        int firstCid = 0, secondCid = 0, thirdCid = 0;
        if (cat != null) {
            if (cat.getLevel() == 1) {
                firstCid = cat.getId();
            } else if (cat.getLevel() == 2) {
                secondCid = cat.getId();
            } else {
                thirdCid = cid;
            }
        } else {
            thirdCid = cid;
        }

        PageList<Article> pageList = articleDao
                .findArticle(firstCid, secondCid, thirdCid, title, tag, ArticleStatus.valueOf(status), updatedStart,
                        updatedEnd, createdStart, createdEnd, getPageParam());
        setPageData(pageList);
        view("article/list");
    }

    @ActionKey
    public void add() {
        view("article/add");
    }

    @ActionKey
    public void edit() {
        int id = getParaToInt("id");
        Article article = articleDao.getByPK(id);
        if (article.getFirstCid() > 0) {
            ArticleCatalog firstCat = catalogDao.getByPK(article.getFirstCid());
            if (firstCat != null) {
                article.setFirstName(firstCat.getName());
            }
        }
        if (article.getSecondCid() > 0) {
            ArticleCatalog secondCat = catalogDao.getByPK(article.getSecondCid());
            if (secondCat != null) {
                article.setSecondName(secondCat.getName());
            }
        }
        if (article.getThirdCid() > 0) {
            ArticleCatalog cat = catalogDao.getByPK(article.getThirdCid());
            if (cat != null) {
                article.setThirdName(cat.getName());
            }
        }
        setAttr("article", article);
        view("article/edit");
    }

    @ActionKey
    public void syncAll() throws FileNotFoundException {
        List<Integer> ids = articleDao.findDisplayItemIds();
        for (int id : ids) {
            clearCrumb();
            syncHtml(id);
        }
        renderJson(Json.SUCC("静态化完成"));
    }

    @ActionKey
    public void unSync() {
        int id = getParaToInt("id");
        Article article = articleDao.getByPK(id);
        if (article != null) {
            if (StringUtils.notBlank(article.getUrl())) {
                File file = new File(PathKit.getWebRootPath() + article.getUrl());
                if (file.exists()) {
                    file.delete();
                }
                article.setUrl("");
                articleDao.update(article);
            }
        }
        renderJson(Json.SUCC("操作成功"));
    }

    @ActionKey
    public void sync() throws FileNotFoundException {
        int id = getParaToInt("id");
        Article article = articleDao.getByPK(id);
        if (article != null) {
            syncHtml(id);
            renderJson(Json.SUCC("静态化完成"));
        } else {
            renderJson(Json.ERR("请选中要静态化的商品"));
        }
    }

    private void syncHtml(int id) throws FileNotFoundException {
        initWebCats();
        Article article = articleDao.getArticleInfo(id);
        if (article != null) {
            if (StringUtils.notBlank(article.getUrl())) {
                File file = new File(PathKit.getWebRootPath() + article.getUrl());
                if (file.exists()) {
                    file.delete();
                }
            }
            setAttr("article", article);

            addCrumb(new Crumb("Home", "/"));
            ArticleCatalog firstCat = catalogDao.getByPK(article.getFirstCid());
            if (firstCat != null) {
                addCrumb(new Crumb(firstCat.getName(), firstCat.isParent() ? firstCat.getPath() : ""));
            }
            ArticleCatalog secondCat = catalogDao.getByPK(article.getSecondCid());
            if (secondCat != null) {
                addCrumb(new Crumb(secondCat.getName(), secondCat.isParent() ? secondCat.getPath() : ""));
            }
            ArticleCatalog thirdCat = catalogDao.getByPK(article.getThirdCid());
            if (thirdCat != null) {
                addCrumb(new Crumb(thirdCat.getName(), thirdCat.isParent() ? thirdCat.getPath() : ""));
            }
            addCrumb(new Crumb(article.getTitle()));
            final String path = "/static/article/" + DateUtils.getFormatString(new Date(), "yyyy-MM-dd") + "/";
            TemplateUtils.staticHtml("blog/article.html", PathKit.getWebRootPath() + path, article.getId() + ".html",
                    getRequest());
            article.setUrl(path + article.getId() + ".html");
            articleDao.update(article, "created");
        }
    }

    @ActionKey(method = RequestMethod.POST)
    @Tx
    public void save() throws IOException {
        int id = getParaToInt("id", 0);
        String title = getPara("title");
        if (StringUtils.isBlank(title) || title.length() < 2 || title.length() > 60) {
            renderJson(Json.ERR("请输入2到60个字符的文章标题"));
            return;
        }
        String summary = getPara("summary", "");
        String tags = getPara("tag", "");
        int cid = getParaToInt("cid", 0);
        ArticleCatalog cat;
        if (cid == 0) {
            renderJson(Json.ERR("请选择文章类目"));
            return;
        } else {
            cat = catalogDao.getByPK(cid);
            if (cat == null || cat.isParent()) {
                renderJson(Json.ERR("请选择非父类的文章类目"));
                return;
            }
        }
        int firstCid = 0, secondCid = 0, thirdCid = 0;
        if (cat.getLevel() == 1) {
            firstCid = cat.getId();
        } else if (cat.getLevel() == 2) {
            secondCid = cat.getId();
            firstCid = cat.getPid();
        } else {
            thirdCid = cid;
            secondCid = cat.getPid();
            ArticleCatalog firstCat = catalogDao.getByPK(secondCid);
            if (firstCat != null) {
                firstCid = firstCat.getPid();
            }
        }

        String fileName = getPara("uploadFile");
        String picUrl = getPara("picUrl");


        String content = getPara("content", "");
        Article article;
        if (id > 0) {
            article = articleDao.getArticleInfo(id);
        } else {
            article = new Article();
            article.setId(id);
            article.setUpdated(new Date());
            article.setCreated(new Date());
            article.setStatus(ArticleStatus.draft);
            article.setAuthor("湖外");
        }
        article.setTitle(title);
        article.setTags(tags);
        article.setSummary(summary);
        article.setFirstCid(firstCid);
        article.setSecondCid(secondCid);
        article.setThirdCid(thirdCid);
        article.setContent(content);
        if (StringUtils.notBlank(picUrl)) {
            article.setImgUrl(picUrl);
        }
        if (StringUtils.notBlank(fileName)) {
            String savePath = getArticlePicSavePath(fileName);
            FileUtils.copyFile(
                    new File(JFinal.me().getConstants().getUploadedFileSaveDirectory() + "/article/" + fileName),
                    JFinal.me().getConstants().getFileRenderPath(), savePath);
            article.setImgUrl(savePath);
        }
        articleDao.save(article);
        renderJson(Json.SUCC("添加商品成功"));
    }

    @ActionKey(method = RequestMethod.POST)
    public void display() {
        int id = getParaToInt("id", 0);
        articleDao.display(id);
        renderJson(Json.SUCC("显示成功"));
    }

    @ActionKey(method = RequestMethod.POST)
    public void hide() {
        int id = getParaToInt("id", 0);
        articleDao.hide(id);
        renderJson(Json.SUCC("隐藏成功"));
    }
}
