package com.ahzx.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/*
*
 *
 * @date 2026/4/5 23:01
 */
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext  applicationContext = null;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        if(SpringUtil. applicationContext == null){
            SpringUtil. applicationContext = applicationContext;
        }
        System. out .println("---------------------------------------------------------------------");
        System. out .println("---------------------------------------------------------------------");
        System. out .println("---------------simple.plugin.spring.SpringUtil-------------------------------------- ----------------");
        System. out .println("========ApplicationContext 配 置 成 功 , 在 普 通 类 可 以 通 过 调 用 SpringUtils.getAppContext() 获 取 applicationContext 对 象,applicationContext="+SpringUtil. applicationContext +"========");
        System. out .println("---------------------------------------------------------------------");
    }
    //获取 applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext ;
    }
    //通过 name 获取 Bean.
    public static Object getBean(String name){
        return  getApplicationContext ().getBean(name);
    }
    //通过 class 获取 Bean.
    public static <T> T getBean(Class<T> clazz){
        return  getApplicationContext ().getBean(clazz);
    }
    //通过 name,以及 Clazz 返回指定的 Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return  getApplicationContext ().getBean(name, clazz);
    }
}
