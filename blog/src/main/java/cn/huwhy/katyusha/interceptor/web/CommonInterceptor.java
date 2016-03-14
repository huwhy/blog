package cn.huwhy.katyusha.interceptor.web;

import cn.huwhy.katyusha.controller.BaseController;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CommonInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation ai) {

        HttpServletRequest request = ai.getController().getRequest();
        Enumeration<String> names = request.getParameterNames();
        Map<String, Object> params = new HashMap<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            params.put(name, request.getParameter(name));
        }
        ai.getController().setAttr("params", params);
        if (!ai.getActionKey().startsWith("/katyusha")) {
            BaseController c = (BaseController) ai.getController();
            c.initWebCats();
        } else {

        }
        ai.invoke();
    }
}
