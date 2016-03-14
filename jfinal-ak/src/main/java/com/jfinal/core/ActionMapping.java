/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.core;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Tx;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Routes;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

/**
 * ActionMapping
 */
final class ActionMapping {

    private static final String SLASH = "/";
    private Routes routes;
    private Interceptors interceptors;

    private final Map<String, Action> mapping = new HashMap<>();
    private final Map<AntPathMatcher, Action> antMapping = new HashMap<>();
    private final Map<AntPathMatcher, Action> likeMapping = new HashMap<>();

    ActionMapping(Routes routes, Interceptors interceptors) {
        this.routes = routes;
        this.interceptors = interceptors;
    }

    private Set<String> buildExcludedMethodName() {
        Set<String> excludedMethodName = new HashSet<String>();
        Method[] methods = Controller.class.getMethods();
        for (Method m : methods) {
            if (m.getParameterTypes().length == 0)
                excludedMethodName.add(m.getName());
        }
        return excludedMethodName;
    }

    void buildActionMapping() {
        mapping.clear();
        Set<String> excludedMethodName = buildExcludedMethodName();
        ActionInterceptorBuilder interceptorBuilder = new ActionInterceptorBuilder();
        Interceptor[] globalInters = interceptors.getGlobalActionInterceptor();
        interceptorBuilder.addToInterceptorsMap(globalInters);
        for (Entry<String, Class<? extends Controller>> entry : routes.getEntrySet()) {
            Class<? extends Controller> controllerClass = entry.getValue();
            Interceptor[] controllerInters = interceptorBuilder.buildControllerInterceptors(controllerClass);

            //            boolean sonOfController = (controllerClass.getSuperclass() == Controller.class);
            Tx tx = controllerClass.getAnnotation(Tx.class);
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if (excludedMethodName.contains(methodName) || method.getParameterTypes().length != 0)
                    continue;
                if (!Modifier.isPublic(method.getModifiers()))
                    continue;
                if (tx == null) {
                    tx = method.getAnnotation(Tx.class);
                }
                int txLevel = 0;
                if (tx != null) {
                    txLevel = tx.level();
                }

                Interceptor[] methodInters = interceptorBuilder.buildMethodInterceptors(method);
                Interceptor[] actionInters = interceptorBuilder
                        .buildActionInterceptors(globalInters, controllerInters, methodInters, method);
                String controllerKey = entry.getKey();

                ActionKey ak = method.getAnnotation(ActionKey.class);
                String[] actionKeys;
                String actionKey;
                String baseViewPath = routes.getViewPath(controllerKey);
                RequestMethod[] requestMethods = RequestMethod.ALL;
                if (ak != null && ak.method() != null && ak.method().length > 0) {
                    requestMethods = ak.method();
                }
                if (ak != null && ak.value() != null && ak.value().length > 0) {
                    actionKeys = ak.value();
                    for (String ack : actionKeys) {
                        if ("".equals(ack))
                            throw new IllegalArgumentException(controllerClass
                                    .getName() + "." + methodName + "(): The argument of ActionKey can not be blank.");
                        actionKey = ack;
                        if (!actionKey.startsWith(SLASH)) {
                            actionKey = controllerKey + SLASH + actionKey;
                        }
                        addMapping(controllerClass, controllerKey, baseViewPath, method, methodName, txLevel,
                                actionInters, requestMethods, actionKey);
                    }
                } else if (methodName.equals("index")) {
                    actionKey = controllerKey;

                    addMapping(controllerClass, controllerKey, baseViewPath, method, methodName, txLevel, actionInters,
                            requestMethods, actionKey + "/index");

                    addMapping(controllerClass, controllerKey, baseViewPath, method, methodName, txLevel, actionInters,
                            requestMethods, actionKey);
                } else {
                    actionKey = controllerKey.equals(SLASH) ? SLASH + methodName : controllerKey + SLASH + methodName;

                    addMapping(controllerClass, controllerKey, baseViewPath, method, methodName, txLevel, actionInters,
                            requestMethods, actionKey);
                }
            }
        }


        // support url = controllerKey + urlParas with "/" of controllerKey
        Action action = mapping.get("/");
        if (action != null)
            mapping.put("", action);
    }

    private void addMapping(Class<? extends Controller> controllerClass, String controllerKey, String viewBasePath,
            Method method, String methodName, int txLevel, Interceptor[] actionInters, RequestMethod[] requestMethods,
            String actionKey) {
        String key = actionKey.replace("//", SLASH);
        if (key.contains("*")) {
            AntPathMatcher ant = new AntPathMatcher(key);
            if (antMapping.containsKey(ant)) {
                throw new RuntimeException(buildMsg(actionKey, controllerClass, method));
            }
            Action action = new Action(controllerKey, key, controllerClass, method, methodName, actionInters,
                    viewBasePath, requestMethods, txLevel);
            likeMapping.put(ant, action);
        } else if (key.indexOf('{') != -1) {
            AntPathMatcher ant = new AntPathMatcher(key);
            if (antMapping.containsKey(ant)) {
                throw new RuntimeException(buildMsg(actionKey, controllerClass, method));
            }
            Action action = new Action(controllerKey, key, controllerClass, method, methodName, actionInters,
                    viewBasePath, requestMethods, txLevel);
            antMapping.put(ant, action);
        } else {
            if (mapping.containsKey(key)) {
                throw new RuntimeException(buildMsg(actionKey, controllerClass, method));
            }
            Action action = new Action(controllerKey, key, controllerClass, method, methodName, actionInters,
                    viewBasePath, requestMethods, txLevel);
            if (key.length() > 1 && key.endsWith(SLASH)) {
                actionKey = actionKey.substring(0, key.length() - 1);
            }
            mapping.put(actionKey.replace("//", SLASH), action);
        }
    }


    private static final String buildMsg(String actionKey, Class<? extends Controller> controllerClass, Method method) {
        StringBuilder sb = new StringBuilder("The action \"").append(controllerClass.getName()).append(".")
                .append(method.getName()).append("()\" can not be mapped, ").append("actionKey \"").append(actionKey)
                .append("\" is already in use.");

        String msg = sb.toString();
        System.err.println("\nException: " + msg);
        return msg;
    }

    /**
     * Support four types of url
     * 1: http://abc.com/controllerKey                 ---> 00
     * 2: http://abc.com/controllerKey/para            ---> 01
     * 3: http://abc.com/controllerKey/method          ---> 10
     * 4: http://abc.com/controllerKey/method/para     ---> 11
     * The controllerKey can also contains "/"
     * Example: http://abc.com/uvw/xyz/method/para
     */
    Action getAction(String url, String requestMethod, Map<String, String> urlPara) {
        String actionKey = url;
        if (url.length() > 1 && url.endsWith("/")) {
            actionKey = url.substring(0, url.length() - 1);
        }
        Action action = mapping.get(actionKey);
        if (action != null) {
            return action;
        }
        if (action == null) {
            for (Entry<AntPathMatcher, Action> entry : antMapping.entrySet()) {
                if (entry.getKey().match2Url(actionKey, urlPara)) {
                    action = entry.getValue();
                    break;
                }
            }
        }

        if (action == null) {
            for (Entry<AntPathMatcher, Action> entry : likeMapping.entrySet()) {
                if (entry.getKey().match2Url(actionKey, urlPara)) {
                    action = entry.getValue();
                    break;
                }
            }
        }

        if (action != null && action.matchMethods(requestMethod)) {
            return action;
        } else {
            return null;
        }
    }

    List<String> getAllActionKeys() {
        List<String> allActionKeys = new ArrayList<String>(mapping.keySet());
        Collections.sort(allActionKeys);
        return allActionKeys;
    }
}













