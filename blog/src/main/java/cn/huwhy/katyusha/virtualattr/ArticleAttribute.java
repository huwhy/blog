package cn.huwhy.katyusha.virtualattr;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.model.Article;
import org.beetl.core.Context;
import org.beetl.core.VirtualClassAttribute;

public class ArticleAttribute implements VirtualClassAttribute {
    @Override
    public Object eval(Object o, String s, Context context) {
        if (o == null) return null;
        Article article = (Article) o;
        if ("path".equals(s)) {
            return StringUtils.isBlank(article.getUrl()) ? "/article/" + article.getId() + ".html" : article.getUrl();
        }
        return null;
    }
}
