package com.ahzx.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.*;

/*
*
 * @date 2026/4/5 20:46
 */
@Configuration
public class PropCache extends PropertyPlaceholderConfigurer {
	
	private static Logger log=LoggerFactory.getLogger(PropCache.class);

	private static Map<String, Object> propertiesMap = new HashMap<String, Object>();

	public static Object getProperty(String name) {
		Object value = propertiesMap.get(name);
		log.info("获取到的配置信息key:{},value:{}",name,value);
		return value;
	}

	public PropCache() throws IOException{
		this.setIgnoreResourceNotFound(true);
		final List<Resource> resourceLst = new ArrayList<Resource>();
		
		Resource  resouce = new ClassPathResource("application.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(resouce);
		String enivName =props.getProperty("spring.profiles.active");//获取环境配置的前缀值
		log.info("获取到的配置环境变量值---------->{}",enivName);
		
		String enivNameSource = "config/config-"+enivName+".properties";
		resourceLst.add(new ClassPathResource(enivNameSource));//加载环境配置参数，根据前缀加载

		//resourceLst.add(new ClassPathResource("config/rescode.properties"));
		this.setLocations(resourceLst.toArray(new Resource[]{}));
	}


	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) {
		super.processProperties(beanFactoryToProcess, props);
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			propertiesMap.put(keyStr, value);
		}
	}

}
