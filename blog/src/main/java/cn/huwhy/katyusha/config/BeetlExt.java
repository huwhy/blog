package cn.huwhy.katyusha.config;

import cn.huwhy.katyusha.model.Article;
import cn.huwhy.katyusha.model.Item;
import cn.huwhy.katyusha.model.Slide;
import cn.huwhy.katyusha.tags.*;
import cn.huwhy.katyusha.virtualattr.ArticleAttribute;
import cn.huwhy.katyusha.virtualattr.ItemVirtualClassAttribute;
import cn.huwhy.katyusha.virtualattr.SlideStatusVal;

import static org.beetl.ext.jfinal.BeetlRenderFactory.groupTemplate;

public class BeetlExt {
    public static void extConfig() {
        groupTemplate.registerTag("small_page", PageTag.class);
        groupTemplate.registerTag("web_page", WebPageTag.class);
        groupTemplate.registerTag("site_name", SiteName.class);
        groupTemplate.registerTag("bei_an", BeiAn.class);
        groupTemplate.registerFunction("currency", new CurrencyFunction());
        groupTemplate.registerVirtualAttributeClass(Item.class, new ItemVirtualClassAttribute());
        groupTemplate.registerVirtualAttributeClass(Slide.class, new SlideStatusVal());
        groupTemplate.registerVirtualAttributeClass(Article.class, new ArticleAttribute());
    }
}
