package cn.huwhy.katyusha.tags;

import cn.huwhy.katyusha.common.CacheUtils;
import com.jfinal.plugin.spring.SpringUtils;
import org.beetl.core.GeneralVarTagBinding;

import java.io.IOException;

public class SiteName extends GeneralVarTagBinding {

    @Override
    public void render() {
        try {
            ctx.byteWriter.writeString(SpringUtils.getBean(CacheUtils.class).siteName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
