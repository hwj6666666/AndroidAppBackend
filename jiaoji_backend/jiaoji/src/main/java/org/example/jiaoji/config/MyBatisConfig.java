package org.example.jiaoji.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.apache.ibatis.session.Configuration;
import javax.sql.DataSource;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class MyBatisConfig {
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);

        // 创建 PageHelper 插件实例
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        pageInterceptor.setProperties(properties);

        // 添加 PageHelper 插件
        sessionFactoryBean.setPlugins(new Interceptor[] { pageInterceptor });

        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sessionFactoryBean.setConfiguration(configuration);

        return sessionFactoryBean.getObject();
    }
}