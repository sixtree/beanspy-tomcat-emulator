package com.sixtree.esb.monitoring;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.catalina.mbeans.Context;
import org.apache.catalina.mbeans.ContextMBean;

public class BeanSpyTomcatEmulator implements ServletContextListener {
	
	private static Logger logger = LoggerFactory.getLogger(BeanSpyTomcatEmulator.class);
	
	public void contextInitialized(final ServletContextEvent sce) {
        logger.info("---> bean context listener started");  
        ServletContext sc = sce.getServletContext();
        final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        String applicationsParam = sc.getInitParameter("com.sixtree.esb.monitoring.TomcatEmulatedApplications");
        String[] applications = applicationsParam.split(",");
        
        for (String application: applications) {
        	String trimmed = application.trim();
        	logger.info("---> Loading application: " + trimmed);
        	try {
	        	ObjectName objectName = new ObjectName("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=" + trimmed);
				ContextMBean mbean = new Context();
				mbeanServer.registerMBean(mbean, objectName);
				logger.info("------> registerMBean ok");
        	} catch (final Exception e) {
        		logger.error("------> Failed: " + e.getMessage());
        	}
        }
    }
	
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("---> bean context listener stopped");
	}
}
