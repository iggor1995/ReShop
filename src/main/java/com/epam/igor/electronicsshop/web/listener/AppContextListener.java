package com.epam.igor.electronicsshop.web.listener;

import com.epam.igor.electronicsshop.pool.ConnectionPool;
import com.epam.igor.electronicsshop.pool.ConnectionPoolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Class-listener does the necessary work at application startup.
 */
public class AppContextListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(AppContextListener.class);
    private static final String CREATE_NEW_SINGLETON_INSTANCE_OF_CONNECTION_POOL = "Create new singleton instance of connection pool";
    private static final String POOL = "pool";
    private static final String CANNOT_CLOSE_ALL_CONNECTION_IN_POOL = "Cannot close all connection in pool";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ConnectionPool pool = new ConnectionPool();
        LOG.info(CREATE_NEW_SINGLETON_INSTANCE_OF_CONNECTION_POOL);
        ConnectionPool.InstanceHolder.setInstance(pool);
        servletContext.setAttribute(POOL, pool);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ConnectionPool pool = (ConnectionPool) servletContext.getAttribute(POOL);
        try {
            pool.close();
        } catch (ConnectionPoolException e) {
            LOG.error(CANNOT_CLOSE_ALL_CONNECTION_IN_POOL, e);
        }

    }
}
