package cn.huwhy.katyusha.controller;

import cn.huwhy.common.json.Json;
import cn.huwhy.katyusha.common.CacheUtils;
import cn.huwhy.katyusha.common.SeoUtils;
import cn.huwhy.katyusha.component.ueditor.PathFormat;
import cn.huwhy.katyusha.model.Menu;
import cn.huwhy.katyusha.model.Seo;
import cn.huwhy.katyusha.model.User;
import cn.huwhy.katyusha.vo.Crumb;
import com.google.common.collect.Lists;
import com.jfinal.core.Controller;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.PageParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BaseController extends Controller {

    @Autowired
    private CacheUtils cacheUtils;

    public void addCrumb(Crumb... crumbs) {
        List<Crumb> crumbList = getAttr("crumbs");
        if (crumbList == null) {
            crumbList = Lists.newArrayList();
        }
        crumbList.addAll(Arrays.asList(crumbs));
        setAttr("crumbs", crumbList);
    }

    public void clearCrumb() {
        removeAttr("crumbs");
    }

    public void setAdmin(Object admin) {
        setSessionAttr("admin", admin);
    }

    public Object getAdmin() {
        return getSessionAttr("admin");
    }

    public void addUser(User user) {
        setSessionAttr("curUser", user);
    }

    public User user() {
        return getSessionAttr("curUser");
    }

    public int uid() {
        User user = user();
        return user == null ? 0 : user.getId();
    }

    public List<Menu> firstMenu() {
        return getSessionAttr("first_menus");
    }

    public void firstMenu(List<Menu> menus) {
        setSessionAttr("first_menus", menus);
    }

    public List<Menu> leftMenu() {
        return getSessionAttr("left_menus");
    }

    public void leftMenu(List<Menu> menus) {
        setSessionAttr("left_menus", menus);
    }

    public void firstMenuId(int firstId) {
        setSessionAttr("first_menu_id", firstId);
    }

    public Object firstMenuId() {
        return getSessionAttr("first_menu_id");
    }

    public void secondMenuId(int secondId) {
        setSessionAttr("second_menu_id", secondId);
    }

    public Object secondMenuId() {
        return getSessionAttr("second_menu_id");
    }

    public int getCurNo() {
        return getParaToInt("curNo", 1);
    }

    public int getPerSize() {
        return getParaToInt("perSize", 10);
    }

    public PageParam getPageParam() {
        return new PageParam(getPerSize(), getCurNo());
    }

    public PageParam getPageParam(int perSize, int curNo) {
        return new PageParam(perSize, curNo);
    }

    public <T> void setPageData(PageList<T> pageList) {
        setAttr("data", pageList.getData());
        setAttr("page", pageList.getPage());
    }

    public void initWebCats() {
        setSessionAttr("catList", cacheUtils.getArticleCats());
    }

    public static String getItemPicSavePath(String fileName) {
        String path = "/item/{yyyy}{mm}{dd}/{time}{rand:6}" + getSuffix(fileName);
        return PathFormat.parse(path);
    }

    public static String getArticlePicSavePath(String fileName) {
        String path = "/article/{yyyy}{mm}{dd}/{time}{rand:6}" + getSuffix(fileName);
        return PathFormat.parse(path);
    }

    public static String getSlidePicSavePath(String fileName) {
        String path = "/slide/{yyyy}{mm}{dd}/{time}{rand:6}" + getSuffix(fileName);
        return PathFormat.parse(path);
    }

    private static String getSuffix(String fileName) {
        return fileName.substring(fileName.indexOf('.'));
    }

    public void setSeo(Seo seo, Map<String, Object> root) {
        if (seo == null)
            return;
        SeoUtils.parse(seo, root);
        setAttr("seo", seo);
    }

    public void jsonView(Json json) {
        renderJson(json);
    }

    public void view(String view) {
        render(view);
    }
}
