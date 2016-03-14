package cn.huwhy.katyusha.config;

import cn.huwhy.katyusha.common.CacheUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.LinkDao;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.jredis.JredisPool;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackageClasses = {BaseController.class, LinkDao.class, CacheUtils.class})
@EnableRedisHttpSession
public class SpringConfig {

    @Autowired
    @Bean
    public SpringUtils springUtils(ApplicationContext context) {
        SpringUtils.setContext(context);
        return new SpringUtils();
    }

    @Bean
    public PropertyPlaceholderConfigurer placeholderConfigurer() {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setLocations(new ClassPathResource("config.properties"));
        return configurer;
    }

    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory redis = new JedisConnectionFactory();
//        redis.setPassword("12345678");
        JedisPoolConfig pool = new JedisPoolConfig();
//        pool.setMaxIdle(200);
//        pool.setMaxTotal(600);
//        pool.setMaxWaitMillis(1000);
        redis.setPoolConfig(pool);
        return redis;
    }

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        Properties props = new Properties();
        try {
            PropertiesLoaderUtils
                    .fillProperties(props, new EncodedResource(new ClassPathResource("jdbc.properties"), "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        C3p0Plugin c3p0Plugin = new C3p0Plugin(props.getProperty("jdbcUrl"), props.getProperty("user"),
                props.getProperty("password").trim());
        c3p0Plugin.start();
        return c3p0Plugin.getDataSource();
    }

    //    @Bean
    //    @Autowired
    //    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    //        return new JdbcTemplate(dataSource);
    //    }

    @Bean
    @Autowired
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
