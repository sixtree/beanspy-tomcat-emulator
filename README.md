mkdir activemq && cd activemq
wget http://mirror.mel.bkb.net.au/pub/apache/activemq/apache-activemq/5.9.0/apache-activemq-5.9.0-bin.tar.gz
tar xvf apache-activemq-5.9.0-bin.tar.gz

# beanspy: https://github.com/liupeirong/BeanSpy/downloads
wget https://github.com/downloads/liupeirong/BeanSpy/BeanSpy_binary.zip
unzip BeanSpy_binary.zip BeanSpy.HTTP.NoAuth.war
zip BeanSpy.HTTP.NoAuth.war -d META-INF/*
unzip BeanSpy.HTTP.NoAuth.war WEB-INF/web.xml
cp ../web.xml WEB-INF/web.xml
jar -uf BeanSpy.HTTP.NoAuth.war WEB-INF/web.xml
cp BeanSpy.HTTP.NoAuth.war apache-activemq-5.9.0/webapps/BeanSpy.war

# add the following to activemq/conf/jetty.xml
<bean class="org.eclipse.jetty.webapp.WebAppContext">
 <property name="contextPath" value="/BeanSpy"/>
 <property name="war" value="${activemq.home}/webapps/BeanSpy.war" />
 <property name="logUrlOnStart" value="true" />
</bean>

