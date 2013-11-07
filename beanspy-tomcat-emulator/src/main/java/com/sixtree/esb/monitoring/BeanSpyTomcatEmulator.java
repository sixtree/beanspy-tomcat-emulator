package com.sixtree.esb.monitoring;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.catalina.mbeans.Context;
import org.apache.catalina.mbeans.ContextMBean;

@WebListener
public class BeanSpyTomcatEmulator implements ServletContextListener {
	
	private static Logger logger = LoggerFactory.getLogger(BeanSpyTomcatEmulator.class);
	
	public void contextInitialized(final ServletContextEvent sce) {
        logger.info("---> bean context listener started");  
        ServletContext sc = sce.getServletContext();
        String applications = sc.getInitParameter("com.sixtree.esb.monitoring.TomcatEmulatedApplications");
        
        // TODO split this string and load multiple applications based off it
        logger.info("---> APPLICATIONS: " + applications);

        final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName objectName = new ObjectName("Catalina:J2EEApplication=none,J2EEServer=none,j2eeType=WebModule,name=//localhost/test");
            ContextMBean mbean = new Context();
            mbeanServer.registerMBean(mbean, objectName);
            logger.info("---> registerMBean ok");
        } catch (final Exception e) {
            logger.error(e.toString());
        }
    }
	
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("---> bean context listener stopped");
	}
}
