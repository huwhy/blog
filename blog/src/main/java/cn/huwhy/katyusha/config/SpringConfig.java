package cn.huwhy.katyusha.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.jfinal.plugin.dbcp.DbcpPlugin;
import com.jfinal.plugin.spring.SpringUtils;

import cn.huwhy.katyusha.common.CacheUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.LinkDao;

@Configuration
@ComponentScan(basePackageClasses = {BaseController.class, LinkDao.class, CacheUtils.class})
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
    public DataSource dataSource() throws PropertyVetoException {
        Properties props = new Properties();
        try {
            PropertiesLoaderUtils
                    .fillProperties(props, new EncodedResource(new ClassPathResource("jdbc.properties"), "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DbcpPlugin dbPlugin = new DbcpPlugin(props.getProperty("jdbcUrl"), props.getProperty("user"),
                props.getProperty("password").trim());
        dbPlugin.start();
        return dbPlugin.getDataSource();
    }

    @Bean
    @Autowired
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
