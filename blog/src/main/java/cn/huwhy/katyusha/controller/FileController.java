package cn.huwhy.katyusha.controller;

import cn.huwhy.common.utils.FileUtils;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.core.JFinal;
import com.jfinal.upload.UploadFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Scope("prototype")
@AK("/file")
public class FileController extends BaseController {

    public void index() {
        String file = getPara("file");
        if (file.startsWith("/")) {
            file = file.substring(1);
        }
        renderFile(file);
    }

    @ActionKey("/uploadItemPic")
    public void uploadItemPic() throws IOException {
        UploadFile file = getFile();
        FileUtils.moveFile(file.getFile(), JFinal.me().getConstants().getUploadedFileSaveDirectory() + "/item/",
                new String(file.getOriginalFileName().getBytes("ISO-8859-1")));
        renderText("OK");
    }

    @ActionKey("/item/{ymd}/{filename}")
    public void itemPic() {
        String file = "item/" + getUrlPara("ymd") + "/" + getUrlPara("filename");
        renderFile(file);
    }

    @ActionKey("/uploadSlidePic")
    public void uploadSlidePic() throws IOException {
        UploadFile file = getFile();
        FileUtils.moveFile(file.getFile(), JFinal.me().getConstants().getUploadedFileSaveDirectory() + "/slide/",
                new String(file.getOriginalFileName().getBytes("ISO-8859-1")));
        renderText("OK");
    }

    @ActionKey("/slide/{ymd}/{filename}")
    public void slidePic() {
        String file = "slide/" + getUrlPara("ymd") + "/" + getUrlPara("filename");
        renderFile(file);
    }

    @ActionKey("/uploadArticlePic")
    public void uploadArticlePic() throws IOException {
        UploadFile file = getFile();
        FileUtils.moveFile(file.getFile(), JFinal.me().getConstants().getUploadedFileSaveDirectory() + "/article/",
                new String(file.getOriginalFileName().getBytes("ISO-8859-1")));
        renderText("OK");
    }

    @ActionKey("/article/{ymd}/{filename}")
    public void articlePic() {
        String file = "article/" + getUrlPara("ymd") + "/" + getUrlPara("filename");
        renderFile(file);
    }

    @ActionKey("/desc/image/{ymd}/{filename}")
    public void descPic() {
        String file = "desc/image/" + getUrlPara("ymd") + "/" + getUrlPara("filename");
        renderFile(file);
    }
}
