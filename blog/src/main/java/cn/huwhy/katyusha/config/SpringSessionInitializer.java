package cn.huwhy.katyusha.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

public class SpringSessionInitializer extends AbstractHttpSessionApplicationInitializer {
    public SpringSessionInitializer() {
        super(SpringConfig.class);
    }
}
