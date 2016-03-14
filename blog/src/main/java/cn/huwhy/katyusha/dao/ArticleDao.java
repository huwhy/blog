package cn.huwhy.katyusha.dao;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.enums.ArticleStatus;
import cn.huwhy.katyusha.model.Article;
import cn.huwhy.katyusha.model.ArticleCatalog;
import com.jfinal.plugin.spring.jdbc.Dao;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ArticleDao extends Dao<Article, Integer> {

    @Autowired
    private ArticleCatalogDao catalogDao;

    public ArticleDao() {
        super("article", Article.class);
    }

    public PageList<Article> findArticle(int firstCid, int secondCid, int thirdCid, String title, String tag,
                                         ArticleStatus status, Date updatedStart, Date updatedEnd, Date createdStart, Date createdEnd,
                                         PageParam pageParam) {
        StringBuilder sql = new StringBuilder();
        sql.append("select art.*, a.name firstName, b.name secondName, c.name thirdName from article art")
                .append(" left join article_catalog a on art.first_cid=a.id")
                .append(" left join article_catalog b on art.second_cid=b.id")
                .append(" left join article_catalog c on art.third_cid=c.id").append(" where 1=1");
        List<Object> args = new ArrayList<>();
        if (firstCid > 0) {
            sql.append(" and art.first_cid=?");
            args.add(firstCid);
        }
        if (secondCid > 0) {
            sql.append(" and art.second_cid=?");
            args.add(secondCid);
        }
        if (thirdCid > 0) {
            sql.append(" and art.third_cid=?");
            args.add(thirdCid);
        }
        if (StringUtils.notBlanks(title)) {
            sql.append(" and art.title like ?");
            args.add(title + "%");
        }
        if (StringUtils.notBlank(tag)) {
            sql.append(" and art.tags like ?");
            args.add(tag + "%");
        }
        if (status != null && ArticleStatus.unknown != status) {
            sql.append(" and art.status=?");
            args.add(status.name());
        }
        if (updatedStart != null) {
            sql.append(" and art.updated >=?");
            args.add(updatedStart);
        }
        if (updatedEnd != null) {
            sql.append(" and art.updated < ?");
            args.add(updatedEnd);
        }
        if (createdStart != null) {
            sql.append(" and art.created >=?");
            args.add(createdStart);
        }
        if (createdEnd != null) {
            sql.append(" and art.created <?");
            args.add(createdEnd);
        }
        sql.append(" order by art.updated desc");
        return findPaging(sql, pageParam, args);
    }

    public List<Integer> findDisplayItemIds() {
        return findSingleList("select id from article where status=?", Integer.class, ArticleStatus.display.name());
    }

    public Article getArticleInfo(int id) {
        Article article = getByPK(id);
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
        return article;
    }

    public void save(Article article) {
        if (article.getId() > 0) {
            update(article, "created");
        } else {
            add(article);
        }
    }

    public void display(int id) {
        updateByWhere("status=?", "id=?", ArticleStatus.display.name(), id);
    }

    public void hide(int id) {
        updateByWhere("status=?", "id=?", ArticleStatus.hide.name(), id);
    }
}
