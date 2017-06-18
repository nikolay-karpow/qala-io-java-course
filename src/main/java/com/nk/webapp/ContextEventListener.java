package com.nk.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextEventListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("Context is initialized");
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context is destroyed");
    }
}
