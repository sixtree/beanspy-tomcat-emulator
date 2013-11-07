package org.apache.catalina.mbeans;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class Context implements ContextMBean {
	private static Logger logger = LoggerFactory.getLogger(ContextMBean.class);
	
	public Context() {
		logger.info("------> created ContextMBean");
	}
}