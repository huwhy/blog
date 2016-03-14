package cn.huwhy.katyusha.config;

import cn.huwhy.katyusha.component.ueditor.UeController;
import cn.huwhy.katyusha.controller.FileController;
import cn.huwhy.katyusha.interceptor.admin.AdminLoginInterceptor;
import cn.huwhy.katyusha.interceptor.admin.CommonInterceptor;
import com.jfinal.config.*;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.ArTxProvider;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.spring.SpringPlugin;
import com.jfinal.render.IErrorRenderFactory;
import com.jfinal.render.Render;
import com.jfinal.render.ViewType;
import org.beetl.ext.jfinal.BeetlRenderFactory;

public class KatyushaConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        loadPropertyFile("config.properties");
        String templateRoot = PathKit.getWebRootPath() + "/WEB-INF/template";
        final BeetlRenderFactory mainRenderFactory = new BeetlRenderFactory(templateRoot);
        BeetlExt.extConfig();
        me.setMainRenderFactory(mainRenderFactory);
        me.setErrorRenderFactory(new IErrorRenderFactory() {
            @Override
            public Render getRender(int errorCode, String view) {
                return mainRenderFactory.getRender(view);
            }
        });
        me.setErrorRenderFactory(new IErrorRenderFactory() {
            @Override
            public Render getRender(int errorCode, String view) {
                return mainRenderFactory.getRender(view);
            }
        });
        me.setViewType(ViewType.OTHER);
        me.openTx(new ArTxProvider());
        me.addStaticBasePaths("/css", "/js", "/images", "/img", "/static", "/ue", "/favicon");
        me.setUploadedFileSaveDirectory(getProperty("upload.root.path"));
        me.setFileRenderPath(getProperty("file.view.root.path"));

    }

    @Override
    public void configRoute(Routes me) {
        me.add(new WebRoutes());
        me.add(new AdminRoutes());
        me.add(UeController.class, "");
        me.add(FileController.class, "");
    }

    @Override
    public void configPlugin(Plugins me) {
//        //配置C3p0数据库连接池插件
//        C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"),
//                getProperty("password").trim());
//        me.add(c3p0Plugin);
//
//        // 配置ActiveRecord插件
//        ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
//        me.add(arp);

//        //Spring
//        SpringPlugin springPlugin = new SpringPlugin(SpringConfig.class);
//        me.add(springPlugin);
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new AdminLoginInterceptor());
        me.add(new CommonInterceptor());
        me.add(new cn.huwhy.katyusha.interceptor.web.CommonInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {
    }
}
