package org.apache.catalina.mbeans;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Context implements ContextMBean, MBeanRegistration {
	private static Logger logger = LoggerFactory.getLogger(ContextMBean.class);
	private ObjectName objectName;
	
	public Context() {
		logger.info("------> created ContextMBean");
	}
	
	public String getbaseName() {
		return "tomcat-emulator";
	}
	
	public String getobjectName() {
		return objectName.toString();
	}

	public ObjectName preRegister(MBeanServer server, ObjectName name)
			throws Exception {
		
		this.objectName = name;
		return name;
	}

	public void postRegister(Boolean registrationDone) {
	}

	public void preDeregister() throws Exception {		
	}

	public void postDeregister() {
	}
}