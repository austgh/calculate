package com.ahzx.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
//@MapperScan(basePackages = "com.ahzx.mdfc", sqlSessionFactoryRef = "hshySqlSessionFactory")
public class DataSourceConfig {
	private static Logger log = LoggerFactory.getLogger(DataSourceConfig.class);
	@Bean(name = "hshyDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource getDateSource2() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "hshySqlSessionFactory")
	@Primary
	public SqlSessionFactory hshySqlSessionFactory(@Qualifier("hshyDataSource") DataSource datasource)
			throws Exception {
		log.info("查询到的数据源连接池信息是{}", datasource);
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(datasource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:*Mapper.xml"));
		return bean.getObject();
	}

	@Bean(name="hshySqlSessionTemplate")
	public SqlSessionTemplate hshysqlsessiontemplate(
			@Qualifier("hshySqlSessionFactory") SqlSessionFactory sessionfactory) {
		return new SqlSessionTemplate(sessionfactory);
	}
}
