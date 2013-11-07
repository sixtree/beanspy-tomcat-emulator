Overview
========

Want to monitor a Java application not deployed to an Application Server from SCOM?

BeanSpy from Microsoft bridges JMX to SCOM nicely, but the associated SCOM Management Pack expects to interrogate an Application Server for a list of applications before it allows access to general JMX attributes. 

This is a simple web application that can be loaded using (say) an embedded Jetty engine into any Java application. It emulates some Tomcat Catalina MBeans so that SCOM thinks it's talking to Tomcat. Once you're over that hurdle, all JMX attribtues on that JVM are available to you.

Build
=====

```
cd beanspy-tomcat-emulator
mvn clean package
```

Resulting package is in target/beanspy-tomcat-emulator.war

Installation for ActiveMQ
=========================

The following instructions assume ActiveMQ is installed in ${activemq.home}

- In a temporary directory, copy ```beanspy-web.xml``` from this repo, then run

```
wget https://github.com/downloads/liupeirong/BeanSpy/BeanSpy_binary.zip
unzip BeanSpy_binary.zip BeanSpy.HTTP.NoAuth.war
zip BeanSpy.HTTP.NoAuth.war -d META-INF/*
unzip BeanSpy.HTTP.NoAuth.war WEB-INF/web.xml
cp beanspy-web.xml WEB-INF/web.xml
jar -uf BeanSpy.HTTP.NoAuth.war WEB-INF/web.xml
```

- Copy the resulting ```BeanSpy.HTTP.NoAuth.war``` to ```${activemq.home}/webapps```
- Add the following lines to ```${activemq.home}/conf/jetty.xml```

```
<bean class="org.eclipse.jetty.webapp.WebAppContext">
	<property name="contextPath" value="/BeanSpy"/>
	<property name="war" value="${activemq.home}/webapps/BeanSpy.HTTP.NoAuth.war" />
	<property name="logUrlOnStart" value="true" />
</bean>
```

- Copy the built ```beanspy-tomcat-emulator.war``` to ```${activemq.home}/webapps```
- Add the following lines to ```${activemq.home}/conf/jetty.xml```

```
<bean class="org.eclipse.jetty.webapp.WebAppContext">
    <property name="contextPath" value="/tomcat-emulator" />
    <property name="war" value="${activemq.home}/webapps/beanspy-tomcat-emulator.war" />
    <property name="logUrlOnStart" value="true" />
    <property name="overrideDescriptor" value="${activemq.home}/conf/beanspy-tomcat-emulator-apps.xml" />
</bean>
```

- Copy the ```beanspy-tomcat-emulator-apps.xml``` from this repo to ```${activemq.home}/webapps```
- Modify the contents of that file contain the comma separated list of applications you actually want to be available in SCOM
- Ensure ```${activemq.home}/conf/log4j.properties``` contains the line ```log4j.logger.org.eclipse.jetty=INFO``` (default level is WARN)
- Start or restart ActiveMQ, and look for the following lines or similar in ```activemq.log```:

```
2013-11-08 10:03:36,376 | INFO  | ---> bean context listener started | com.sixtree.esb.monitoring.BeanSpyTomcatEmulator | main
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
```

- Check out the output for the relevant BeanSpy query at http://localhost:8161/BeanSpy/MBeans?JMXQuery=Catalina:j2eeType=WebModule,*&MaxDepth=0