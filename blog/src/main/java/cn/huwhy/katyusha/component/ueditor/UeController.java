package cn.huwhy.katyusha.component.ueditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.huwhy.common.utils.FileUtils;
import cn.huwhy.katyusha.controller.BaseController;
import com.jfinal.core.AK;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;
import com.sun.javafx.scene.shape.PathUtils;

@AK("/component/ue")
public class UeController extends BaseController {
    public void index() throws IOException {
        String action = getPara("action");
        Object result = null;
        switch (action) {
            case "config":
                result = config();
                break;
            case "uploadimage":
                result = uploadImage(action);
                break;
        }
        renderJson(result);
    }

    private UeditorState uploadImage(String action) throws IOException {
        UploadFile uploadFile = getFile();
        Map<String, Object> conf = getConfig(action);
        String saveFile = PathFormat.parse((String) conf.get("savePath"), (String) conf.get("filename"));
        FileUtils.copyFile(uploadFile.getFile(), JFinal.me().getConstants().getFileRenderPath() + "/desc/" + saveFile,
                uploadFile.getFileName());
        return new UeditorState(UeditorState.SUCCESS,
                "/file/desc" + saveFile + File.separator + uploadFile.getFileName(), saveFile,
                uploadFile.getOriginalFileName());
    }

    private static Map<String, Object> configMap;

    private Map<String, Object> config() throws IOException {
        if (configMap == null) {
            StringBuilder builder = new StringBuilder();
            try {
                InputStreamReader reader = new InputStreamReader(
                        new FileInputStream(PathKit.getRootClassPath() + "/config.json"), "UTF-8");
                BufferedReader bfReader = new BufferedReader(reader);

                String tmpContent;
                while ((tmpContent = bfReader.readLine()) != null) {
                    builder.append(tmpContent);
                }
                bfReader.close();
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            }
            String config = filter(builder.toString());
            configMap = (Map<String, Object>) com.alibaba.fastjson.JSONObject.parse(config);
        }
        return configMap;
    }

    public Map<String, Object> getConfig(String action) {
        Map<String, Object> conf = new HashMap<>();
        String savePath = null;
        int type = ActionMap.getType(action);
        switch (type) {
            case 4:
                conf.put("isBase64", "false");
                conf.put("maxSize", configMap.get("fileMaxSize"));
                conf.put("allowFiles", configMap.get("fileAllowFiles"));
                conf.put("fieldName", configMap.get("fileFieldName"));
                savePath = configMap.get("filePathFormat").toString();
                break;
            case 1:
                conf.put("isBase64", "false");
                conf.put("maxSize", configMap.get("imageMaxSize"));
                conf.put("allowFiles", configMap.get("imageAllowFiles"));
                conf.put("fieldName", configMap.get("imageFieldName"));
                savePath = configMap.get("imagePathFormat").toString();
                break;
            case 3:
                conf.put("maxSize", configMap.get("videoMaxSize"));
                conf.put("allowFiles", configMap.get("videoAllowFiles"));
                conf.put("fieldName", configMap.get("videoFieldName"));
                savePath = configMap.get("videoPathFormat").toString();
                break;
            case 2:
                conf.put("filename", "scrawl");
                conf.put("maxSize", configMap.get("scrawlMaxSize"));
                conf.put("fieldName", configMap.get("scrawlFieldName"));
                conf.put("isBase64", "true");
                savePath = configMap.get("scrawlPathFormat").toString();
                break;
            case 5:
                conf.put("filename", "remote");
                conf.put("filter", configMap.get("catcherLocalDomain"));
                conf.put("maxSize", configMap.get("catcherMaxSize"));
                conf.put("allowFiles", configMap.get("catcherAllowFiles"));
                conf.put("fieldName", configMap.get("catcherFieldName") + "[]");
                savePath = configMap.get("catcherPathFormat").toString();
                break;
            case 7:
                conf.put("allowFiles", configMap.get("imageManagerAllowFiles"));
                conf.put("dir", configMap.get("imageManagerListPath"));
                conf.put("count", configMap.get("imageManagerListSize"));
                break;
            case 6:
                conf.put("allowFiles", configMap.get("fileManagerAllowFiles"));
                conf.put("dir", configMap.get("fileManagerListPath"));
                conf.put("count", configMap.get("fileManagerListSize"));
        }
        conf.put("savePath", savePath);
        conf.put("rootPath", "/");

        return conf;
    }

    private String filter(String input) {
        return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
    }
}
