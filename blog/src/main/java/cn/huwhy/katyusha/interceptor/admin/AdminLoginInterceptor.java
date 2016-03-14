package cn.huwhy.katyusha.interceptor.admin;

import cn.huwhy.katyusha.controller.BaseController;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class AdminLoginInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation ai) {
        if(ai.getActionKey().startsWith("/katyusha/admin")) {
            BaseController c = (BaseController) ai.getController();
            if(c.getAdmin() == null ){
                c.redirect("/katyusha/login.html");
                return;
            }
        }
        ai.invoke();
    }
}
