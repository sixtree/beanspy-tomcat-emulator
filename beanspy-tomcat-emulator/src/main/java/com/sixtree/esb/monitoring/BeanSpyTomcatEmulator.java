package com.sixtree.esb.monitoring;

import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

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
        Set<String> applications = new HashSet<String>();
        for (String application: applicationsParam.split(",")) {
        	applications.add(application.trim());
        }
        sc.setAttribute("applications", applications);
        
        for (String application: applications) {
        	logger.info("---> Loading application: " + application);
        	try {
	        	ObjectName objectName = new ObjectName("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=" + application);
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
