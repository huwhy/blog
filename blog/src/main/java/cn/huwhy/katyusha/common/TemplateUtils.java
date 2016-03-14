package cn.huwhy.katyusha.common;

import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.ext.jfinal.BeetlRenderFactory;
import org.beetl.ext.web.ParameterWrapper;
import org.beetl.ext.web.SessionWrapper;
import org.beetl.ext.web.WebVariable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class TemplateUtils {
    public static String viewExtension = ".html";
    public static GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate;

    public static String html(String view, HttpServletRequest request) {
        if (!view.endsWith(viewExtension)) {
            view = view + viewExtension;
        }
        Template template = groupTemplate.getTemplate(view);

        Enumeration<String> names = request.getParameterNames();
        Map<String, Object> params = new HashMap<>();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            params.put(key, request.getParameter(key));
        }
        request.setAttribute("params", params);

        Enumeration attrs = request.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String webVariable = (String) attrs.nextElement();
            template.binding(webVariable, request.getAttribute(webVariable));
        }
        WebVariable webVariable = new WebVariable();
        webVariable.setRequest(request);
        template.binding("session", new SessionWrapper(request, request.getSession(false)));

        template.binding("servlet", webVariable);
        template.binding("request", request);
        template.binding("ctxPath", request.getContextPath());
        template.binding("$page", new HashMap());
        template.binding("parameter", new ParameterWrapper(request));


        return template.render();
    }

    public static void staticHtml(String view, String staticRoot, String htmlName,
                                  HttpServletRequest request) throws FileNotFoundException {
        if (!view.endsWith(viewExtension)) {
            view = view + viewExtension;
        }
        Template e = groupTemplate.getTemplate(view);

        Enumeration<String> names = request.getParameterNames();
        Map<String, Object> params = new HashMap<>();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            params.put(key, request.getParameter(key));
        }
        request.setAttribute("params", params);

        Enumeration attrs = request.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String webVariable = (String) attrs.nextElement();
            e.binding(webVariable, request.getAttribute(webVariable));
        }
        WebVariable webVariable1 = new WebVariable();
        webVariable1.setRequest(request);
        webVariable1.setSession(request.getSession());
        e.binding("session", new SessionWrapper(request, request.getSession()));
        e.binding("servlet", webVariable1);
        e.binding("request", request);
        e.binding("ctxPath", request.getContextPath());

        File root = new File(staticRoot);
        if (!root.exists()) {
            root.mkdirs();
        }

        PrintWriter writer = new PrintWriter(new File(root, htmlName));
        try {
            e.renderTo(writer);
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}