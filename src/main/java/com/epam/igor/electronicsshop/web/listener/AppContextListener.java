package com.epam.igor.electronicsshop.web.listener;

import com.epam.igor.electronicsshop.pool.ConnectionPool;
import com.epam.igor.electronicsshop.pool.ConnectionPoolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 * Created by User on 16.09.2017.
 */
public class AppContextListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(AppContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ConnectionPool pool = new ConnectionPool();
        LOG.info("Create new singleton instance of connection pool");
        ConnectionPool.InstanceHolder.setInstance(pool);
        servletContext.setAttribute("pool", pool);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ConnectionPool pool = (ConnectionPool) servletContext.getAttribute("pool");
        try {
            pool.close();
        } catch (ConnectionPoolException e) {
            LOG.error("Cannot close all connection in pool", e);
        }

    }
}
