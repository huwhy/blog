package com.jfinal.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntPathMatcher {

    private static final Pattern GLOB_PATTERN = Pattern
            .compile("\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");

    private static final String DEFAULT_VARIABLE_PATTERN = "([^/]+?)";

    private static final String LIKE_ONE = "{*}";
    private static final String LIKE_ONE_PATTERN = "[^/]+?";

    private static final String LIKE_TWO = "{**}";
    private static final String LIKE_TWO_PATTERN = ".+";

    private static final String SLASH = "/";

    private final Pattern pattern;

    private final List<String> variableNames = new LinkedList<>();

    public static void main(String[] args) {
        AntPathMatcher ant2 = new AntPathMatcher("/item/{**}/{a}/{name}/{id:^abc\\d{2,}}.html");
        Map<String, String> map2 = new HashMap<>();
        ant2.match2Url("/item/abc/aaa/cc/18.html", map2);
        System.out.println(map2);
    }

    public boolean match2Url(String url, Map<String, String> uriTemplateVariables) {
        final Matcher matcher = this.pattern.matcher(url);
        if (matcher.matches()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                String key = variableNames.get(i - 1);
                uriTemplateVariables.put(key, matcher.group(i));
            }
            return true;
        }
        return false;
    }

    public AntPathMatcher(String uri) {
        StringBuilder patternBuilder = new StringBuilder();
        String[] paths = uri.split(SLASH);
        int i = 0;
        for (String path : paths) {
            if (i > 0 && path.equals("")) {
                i++;
                continue;
            }
            Matcher m = GLOB_PATTERN.matcher(path);
            int end = 0;
            while (m.find()) {
                patternBuilder.append(quote(path, end, m.start()));
                String match = m.group();
                if (match.startsWith("{") && match.endsWith("}")) {
                    if (match.equals(LIKE_ONE)) {
                        patternBuilder.append(LIKE_ONE_PATTERN);
                    } else if (match.equals(LIKE_TWO)) {
                        patternBuilder.append(LIKE_TWO_PATTERN);
                    } else {
                        String pat = match.substring(1, match.length() - 1);
                        int index = pat.indexOf(':');
                        if (index == -1) {
                            patternBuilder.append(DEFAULT_VARIABLE_PATTERN);
                            this.variableNames.add(pat);
                        } else {
                            this.variableNames.add(pat.substring(0, index));
                            patternBuilder.append('(').append(pat.substring(index + 1)).append(')');
                        }
                    }
                }
                end = m.end();
            }
            patternBuilder.append(quote(path, end, path.length()));
            i++;
            if (i < paths.length) {
                patternBuilder.append(Pattern.quote(SLASH));
            }
        }
        this.pattern = Pattern.compile(patternBuilder.toString());
    }

    private String quote(String s, int start, int end) {
        if (start == end) {
            return "";
        }
        return Pattern.quote(s.substring(start, end));
    }

    @Override
    public int hashCode() {
        return this.pattern.pattern().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        AntPathMatcher ant = (AntPathMatcher) obj;
        return this.pattern.pattern().equals(ant.pattern.pattern());
    }
}
