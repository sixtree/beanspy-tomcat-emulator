mkdir activemq && cd activemq
wget http://mirror.mel.bkb.net.au/pub/apache/activemq/apache-activemq/5.9.0/apache-activemq-5.9.0-bin.tar.gz
tar xvf apache-activemq-5.9.0-bin.tar.gz

# beanspy: https://github.com/liupeirong/BeanSpy/downloads
wget https://github.com/downloads/liupeirong/BeanSpy/BeanSpy_binary.zip
unzip BeanSpy_binary.zip BeanSpy.HTTP.NoAuth.war
zip BeanSpy.HTTP.NoAuth.war -d META-INF/*
unzip BeanSpy.HTTP.NoAuth.war WEB-INF/web.xml
cp ../beanspy-web.xml WEB-INF/web.xml
jar -uf BeanSpy.HTTP.NoAuth.war WEB-INF/web.xml
cp BeanSpy.HTTP.NoAuth.war apache-activemq-5.9.0/webapps

# add the following to activemq/conf/jetty.xml
<bean class="org.eclipse.jetty.webapp.WebAppContext">
	<property name="contextPath" value="/BeanSpy"/>
	<property name="war" value="${activemq.home}/webapps/BeanSpy.HTTP.NoAuth.war" />
	<property name="logUrlOnStart" value="true" />
</bean>

# change activemq/conf/log4j.properties Jetty output to INFO
log4j.logger.org.eclipse.jetty=INFO

# add the following to activemq/conf/jetty.xml
<bean class="org.eclipse.jetty.webapp.WebAppContext">
    <property name="contextPath" value="/tomcat-emulator" />
    <property name="war" value="${activemq.home}/webapps/beanspy-tomcat-emulator.war" />
    <property name="logUrlOnStart" value="true" />
    <property name="overrideDescriptor" value="${activemq.home}/conf/beanspy-tomcat-emulator-apps.xml" />
</bean>

# look for the following in activemq log
013-11-08 10:03:36,376 | INFO  | ---> bean context listener started | com.sixtree.esb.monitoring.BeanSpyTomcatEmulator | main
2013-11-08 10:03:36,376 | INFO  | ---> Loading application: App1 | com.sixtree.esb.monitoring.BeanSpyTomcatEmulator | main
2013-11-08 10:03:36,377 | INFO  | ------> created ContextMBean | org.apache.catalina.mbeans.ContextMBean | main
2013-11-08 10:03:36,377 | INFO  | ------> registerMBean ok | com.sixtree.esb.monitoring.BeanSpyTomcatEmulator | main
2013-11-08 10:03:36,378 | INFO  | ---> Loading application: App2 | com.sixtree.esb.monitoring.BeanSpyTomcatEmulator | main
2013-11-08 10:03:36,378 | INFO  | ------> created ContextMBean | org.apache.catalina.mbeans.ContextMBean | main
2013-11-08 10:03:36,378 | INFO  | ------> registerMBean ok | com.sixtree.esb.monitoring.BeanSpyTomcatEmulator | main
2013-11-08 10:03:36,379 | INFO  | ---> Loading application: App3 | com.sixtree.esb.monitoring.BeanSpyTomcatEmulator | main
2013-11-08 10:03:36,379 | INFO  | ------> created ContextMBean | org.apache.catalina.mbeans.ContextMBean | main
2013-11-08 10:03:36,379 | INFO  | ------> registerMBean ok | com.sixtree.esb.monitoring.BeanSpyTomcatEmulator | main
2013-11-08 10:03:36,379 | INFO  | started o.e.j.w.WebAppContext{/tomcat-emulator,file:/Users/yamen/Coding/beanspy-tomcat-emulator/activemq/apache-activemq-5.9.0/webapps/beanspy-tomcat-emulator/} | org.eclipse.jetty.server.handler.ContextHandler | main