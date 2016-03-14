/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.core;

import com.jfinal.aop.Invocation;
import com.jfinal.aop.tx.Atom;
import com.jfinal.aop.tx.AtomExecutor;
import com.jfinal.config.Constants;
import com.jfinal.handler.Handler;
import com.jfinal.log.Logger;
import com.jfinal.plugin.spring.SpringUtils;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;
import com.jfinal.render.RenderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * ActionHandler
 */
final class ActionHandler extends Handler {

    private final boolean devMode;
    private final ActionMapping actionMapping;
    private static final RenderFactory renderFactory = RenderFactory.me();
    private static AtomExecutor atomExecutor;

    private static final Logger log = Logger.getLogger(ActionHandler.class);

    public ActionHandler(ActionMapping actionMapping, Constants constants) {
        this.actionMapping = actionMapping;
        this.devMode = constants.getDevMode();
        if (constants.isTx()) {
            atomExecutor = new AtomExecutor(constants.getTxProvider());
        }
    }

    /**
     * handle
     * 1: Action action = actionMapping.getAction(target)
     * 2: new Invocation(...).invoke()
     * 3: render(...)
     */
    public final void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        isHandled[0] = true;
        Map<String, String> urlPara = new HashMap<>();
        final Action action = actionMapping.getAction(target, request.getMethod(), urlPara);

        if (action == null) {
            if (log.isWarnEnabled()) {
                String qs = request.getQueryString();
                log.warn("404 Action Not Found: " + (qs == null ? target : target + "?" + qs));
            }
            renderFactory.getErrorRender(404).setContext(request, response).render();
            return;
        }

        try {
            final Controller controller = getController(action.getControllerClass());
            controller.init(request, response, urlPara);
            if (action.getTxLevel() > 0) {
                atomExecutor.call(action.getTxLevel(), new Atom() {
                    @Override
                    public boolean run() throws Exception {
                        if (devMode) {
                            boolean isMultipartRequest = ActionReporter.reportCommonRequest(controller, action);
                            new Invocation(action, controller).invoke();
                            if (isMultipartRequest) ActionReporter.reportMultipartRequest(controller, action);
                        } else {
                            new Invocation(action, controller).invoke();
                        }
                        return true;
                    }
                });
            } else {
                if (devMode) {
                    boolean isMultipartRequest = ActionReporter.reportCommonRequest(controller, action);
                    new Invocation(action, controller).invoke();
                    if (isMultipartRequest) ActionReporter.reportMultipartRequest(controller, action);
                } else {
                    new Invocation(action, controller).invoke();
                }
            }

            request.setAttribute("urlParams", urlPara);
            Render render = controller.getRender();
            if (render instanceof ActionRender) {
                String actionUrl = ((ActionRender) render).getActionUrl();
                if (target.equals(actionUrl))
                    throw new RuntimeException("The forward action url is the same as before.");
                else
                    handle(actionUrl, request, response, isHandled);
                return;
            }

            if (render == null)
                render = renderFactory.getDefaultRender(action.getViewPath() + action.getMethodName());
            render.setContext(request, response, action.getViewPath()).render();
        } catch (RenderException e) {
            if (log.isErrorEnabled()) {
                String qs = request.getQueryString();
                log.error(qs == null ? target : target + "?" + qs, e);
            }
        } catch (ActionException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 404 && log.isWarnEnabled()) {
                String qs = request.getQueryString();
                log.warn("404 Not Found: " + (qs == null ? target : target + "?" + qs));
            } else if (errorCode == 401 && log.isWarnEnabled()) {
                String qs = request.getQueryString();
                log.warn("401 Unauthorized: " + (qs == null ? target : target + "?" + qs));
            } else if (errorCode == 403 && log.isWarnEnabled()) {
                String qs = request.getQueryString();
                log.warn("403 Forbidden: " + (qs == null ? target : target + "?" + qs));
            } else if (log.isErrorEnabled()) {
                String qs = request.getQueryString();
                log.error(qs == null ? target : target + "?" + qs, e);
            }
            e.getErrorRender().setContext(request, response, action.getViewPath()).render();
        } catch (Throwable t) {
            if (log.isErrorEnabled()) {
                String qs = request.getQueryString();
                log.error(qs == null ? target : target + "?" + qs, t);
            } else {
                t.printStackTrace();
            }
            renderFactory.getErrorRender(500).setContext(request, response, action.getViewPath()).render();
        }
    }

    private Controller getController(Class<? extends Controller> clazz) throws IllegalAccessException, InstantiationException {
        Controller c = null;
        if (SpringUtils.isSupport()) {
            c = SpringUtils.getBean(clazz);
        }
        if (c == null) {
            c = clazz.newInstance();
        }
        return c;
    }
}





