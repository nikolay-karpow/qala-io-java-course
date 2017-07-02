package com.nk.webapp;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext xmlContext = new ClassPathXmlApplicationContext("/applicationConfiguration.xml");
        MyBean myBeanFromXml = (MyBean) xmlContext.getBean("myBean");

        ApplicationContext javaContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MyBean myBeanFromJavaConfig = (MyBean) javaContext.getBean("myBean");
    }
}
