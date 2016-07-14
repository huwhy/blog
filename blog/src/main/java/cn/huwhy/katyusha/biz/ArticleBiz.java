package cn.huwhy.katyusha.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jfinal.plugin.mybatis.Term;

import cn.huwhy.katyusha.common.Paging;
import cn.huwhy.katyusha.dao.mybatis.ArticleCatalogDaoBatis;
import cn.huwhy.katyusha.dao.mybatis.ArticleDaoBatis;
import cn.huwhy.katyusha.model.Article;
import cn.huwhy.katyusha.model.ArticleCatalog;
import cn.huwhy.katyusha.model.Param;
import cn.huwhy.katyusha.term.ArticleTerm;
import cn.huwhy.katyusha.term.CatalogTerm;

@Service
public class ArticleBiz {

    @Autowired
    private ArticleDaoBatis articleDao;

    @Autowired
    private ArticleCatalogDaoBatis articleCatalogDaoBatis;

    @Autowired
    private ParamBiz paramBiz;

    public Paging<Article> findArticle(ArticleTerm term) {
        term.addSort("art.created", Term.Sort.DESC);
        List<Article> list = articleDao.findPaging(term);
        Paging<Article> paging = new Paging<>(term, list);
        return paging;
    }

    public Article getArticleInfo(Integer id) {
        Article article = articleDao.get(id);
        if (article.getFirstCid() > 0) {
            ArticleCatalog firstCat = articleCatalogDaoBatis.get(article.getFirstCid());
            if (firstCat != null) {
                article.setFirstName(firstCat.getName());
            }
        }
        if (article.getSecondCid() > 0) {
            ArticleCatalog secondCat = articleCatalogDaoBatis.get(article.getSecondCid());
            if (secondCat != null) {
                article.setSecondName(secondCat.getName());
            }
        }
        if (article.getThirdCid() > 0) {
            ArticleCatalog cat = articleCatalogDaoBatis.get(article.getThirdCid());
            if (cat != null) {
                article.setThirdName(cat.getName());
            }
        }
        return article;
    }

    public Paging<ArticleCatalog> findCatalogs(CatalogTerm term) {
        term.setIdSort(Term.Sort.ASC);
        List<ArticleCatalog> data = articleCatalogDaoBatis.findPaging(term);
        return new Paging<>(term, data);
    }

    public ArticleCatalog getCatalog(Integer id) {
        return articleCatalogDaoBatis.get(id);
    }

    public List<ArticleCatalog> getArticleCatalogs() {
        Param param = paramBiz.getById(Param.ITEM_CAT_DISPLAY_NUM_PARAM_ID);
        List<ArticleCatalog> catList = articleCatalogDaoBatis.getFirstLevels(1, Integer.parseInt(param.getVal()));
        for (ArticleCatalog cat : catList) {
            cat.setChildren(articleCatalogDaoBatis.getChildren(cat.getId()));
            for (ArticleCatalog cat2 : cat.getChildren()) {
                cat2.setChildren(articleCatalogDaoBatis.getChildren(cat2.getId()));
            }
        }
        return catList;
    }

    public ArticleCatalog getCatalogByName(String name) {
        return articleCatalogDaoBatis.getByName(name);
    }

    public boolean hasChildren(int id) {
        return articleCatalogDaoBatis.hasChildren(id) > 0;
    }

    public void updateCatalog(ArticleCatalog cat) {
        articleCatalogDaoBatis.update(cat);
    }

    public void addCatalog(ArticleCatalog cat) {
        articleCatalogDaoBatis.save(cat);
    }

    public void deleteCatalog(int cid) {
        articleCatalogDaoBatis.delete(cid);
    }
}
