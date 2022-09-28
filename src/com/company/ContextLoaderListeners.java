package com.company;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextLoaderListeners implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // 在这里创建IOC容器
        String path = servletContextEvent.getServletContext().getInitParameter("IOCConfig");
        BeanFactory beanFactory = new ClassPathXmlApplicationContext(path);
        servletContextEvent.getServletContext().setAttribute("beanFactory", beanFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
