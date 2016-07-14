package cn.huwhy.katyusha.config;

import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.spring.SpringPlugin;
import com.jfinal.render.IErrorRenderFactory;
import com.jfinal.render.Render;
import com.jfinal.render.ViewType;

import cn.huwhy.katyusha.component.ueditor.UeController;
import cn.huwhy.katyusha.controller.FileController;
import cn.huwhy.katyusha.interceptor.admin.AdminLoginInterceptor;
import cn.huwhy.katyusha.interceptor.admin.CommonInterceptor;

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
        //Spring
        SpringPlugin springPlugin = new SpringPlugin("classpath:spring.xml");
        me.add(springPlugin);
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
