package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.common.json.Json;
import cn.huwhy.common.utils.FileUtils;
import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.SlideDao;
import cn.huwhy.katyusha.enums.SlideStatus;
import cn.huwhy.katyusha.model.Slide;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.core.JFinal;
import com.jfinal.core.RequestMethod;
import com.jfinal.plugin.spring.jdbc.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
@Scope("prototype")
@AK("/katyusha/admin/slide")
public class SlideController extends BaseController {

    @Autowired
    private SlideDao slideDao;

    @ActionKey("/list")
    public void index() {
        String title = getPara("title");
        String content = getPara("content");
        String status = getPara("status", "unknown");
        PageList<Slide> pageList = slideDao.findSlides(title, content, SlideStatus.valueOf(status), getPageParam());
        setPageData(pageList);
        view("slide/list");
    }

    @ActionKey
    public void add() {
        view("slide/add");
    }

    @ActionKey
    public void edit() {
        int id = getParaToInt("id", 0);
        Slide slide = slideDao.getByPK(id);
        setAttr("slide", slide);
        view("slide/edit");
    }

    @ActionKey(method = RequestMethod.POST)
    public void save() throws IOException {
        int id = getParaToInt("id", 0);
        String title = getPara("title");
        String content = getPara("summary");
        String url = getPara("url");

        if (StringUtils.isBlank(url)) {
            jsonView(Json.ERR("请输入广告链接地址"));
            return;
        } else {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
        }

        if (StringUtils.isBlank(title) || title.length() < 2 || title.length() > 60) {
            jsonView(Json.ERR("标题必须为2-60的字符"));
            return;
        }

        String uploadFile = getPara("uploadFile");
        if (id == 0 && StringUtils.isBlank(uploadFile)) {
            jsonView(Json.ERR("图片不能为空"));
            return;
        }

        Slide slide = new Slide();
        slide.setTitle(title);
        slide.setContent(content);
        slide.setUrl(url);
        if (StringUtils.notBlank(uploadFile)) {
            final String picSavePath = getSlidePicSavePath(uploadFile);
            FileUtils.copyFile(new File(JFinal.me().getConstants().getUploadedFileSaveDirectory() + "/slide/" + uploadFile),
                    JFinal.me().getConstants().getFileRenderPath(), picSavePath);
            slide.setPicture(picSavePath);
        } else {
            String picture = getPara("picture");
            slide.setPicture(picture);
        }
        if (id == 0) {
            slide.setCreated(new Date());
            slide.setStatus(SlideStatus.hide);
            slideDao.add(slide);
        } else {
            slide.setId(id);
            slideDao.update(slide, "status", "created");
        }
        jsonView(Json.SUCC("保存成功"));
    }

    @ActionKey(method = RequestMethod.POST)
    public void display() {
        int id = getParaToInt("id", 0);
        slideDao.display(id);
        jsonView(Json.SUCC("显示成功"));
    }

    @ActionKey(method = RequestMethod.POST)
    public void hide() {
        int id = getParaToInt("id", 0);
        slideDao.hide(id);
        jsonView(Json.SUCC("隐藏成功"));
    }

}
