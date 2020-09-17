package com.beyond.cloud.framework.seata;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * 数据源代理
 *
 * @author lucifer
 * @date 2020/7/29 16:57
 */
@Configuration
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
public class DataSourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources("classpath*:/com/beyond/cloud/svc/**/mapper/**/*.xml"));
        SqlSessionFactory factory;
        try {
            factory = bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return factory;
    }
}
