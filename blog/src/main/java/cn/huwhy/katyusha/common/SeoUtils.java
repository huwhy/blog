package cn.huwhy.katyusha.common;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.model.Seo;
import com.google.common.collect.ImmutableMap;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Component
public class SeoUtils {
    private static GroupTemplate groupTemplate;

    @Autowired
    private CacheUtils cacheUtils;

    @PostConstruct
    public void init() {
        try {
            StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(resourceLoader, cfg);
            groupTemplate.setSharedVars(ImmutableMap.<String, Object>of("site_name", cacheUtils.siteName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void parse(Seo seo, Map<String, Object> root) {
        String titleBtl = toBeetl(seo.getTitle());
        seo.setTitle(parse(titleBtl, root));
        String descBtl = toBeetl(seo.getDescription());
        seo.setDescription(parse(descBtl, root));
        String keyBtl = toBeetl(seo.getKeywords());
        seo.setKeywords(parse(keyBtl, root));
    }

    public static String parse(String text, Map<String, Object> root) {
        if (StringUtils.isBlank(text)) return text;
        Template template = groupTemplate.getTemplate(text);
        if (root != null) {
            template.binding(root);
        }
        return template.render();
    }

    public static String toBeetl(String text) {
        if (StringUtils.isBlank(text)) return text;
        return text.replaceAll("\\{", "\\$\\{").replaceAll("\\}", "\\!\\}");
    }

    public static void main(String[] args) {
        String s = "{parentName}";
        String x = toBeetl(s);
        System.out.println(x);
        Map<String, Object> map = ImmutableMap.<String, Object>of("parentName", "test");
        System.out.println(parse(x, map));
    }
}
