# Sridhar
This branch has my version of the code.

	git checkout mavenized.immutable
	
The main class is:

        a.Main.java

## Build
	ant

## Run

### Standalone

    CATALINA_HOME= ;"/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/bin/java" "-Djava.util.logging.config.file=/Users/ssarnobat/trash/tomcat.trash/output/build/conf/logging.properties" -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catalina.webresources -Denv=dev -Xms1024M -Xmx2048M -XX:PermSize=256M -XX:MaxPermSize=768m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9088 -Djava.security.egd=file:/dev/./urandom -Dlog4j.configurationFile=file:///Users/ssarnobat/control4/log4j2.xml -Dlog4j2.debug=true -Duser.home=/Users/ssarnobat -Dignore.endorsed.dirs="" -classpath "/Users/ssarnobat/trash/tomcat.trash/output/build/bin/bootstrap.jar:/Users/ssarnobat/trash/tomcat.trash/output/build/bin/tomcat-juli.jar" -Dcatalina.base="/Users/ssarnobat/trash/tomcat.trash/output/build" -Dcatalina.home="/Users/ssarnobat/trash/tomcat.trash/output/build" -Djava.io.tmpdir="/Users/ssarnobat/trash/tomcat.trash/output/build/temp" org.apache.catalina.startup.Bootstrap start

### Embedded

	CATALINA_HOME= ;"/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/bin/java" "-Djava.util.logging.config.file=/Users/ssarnobat/trash/tomcat.trash/output/build/conf/logging.properties" -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catalina.webresources -Denv=dev -Xms1024M -Xmx2048M -XX:PermSize=256M -XX:MaxPermSize=768m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9088 -Djava.security.egd=file:/dev/./urandom -Dlog4j.configurationFile=file:///Users/ssarnobat/control4/log4j2.xml -Dlog4j2.debug=true -Duser.home=/Users/ssarnobat -Dignore.endorsed.dirs="" -classpath "/Users/ssarnobat/trash/tomcat.trash/output/build/bin/bootstrap.jar:/Users/ssarnobat/trash/tomcat.trash/output/build/bin/tomcat-juli.jar" -Dcatalina.base="/Users/ssarnobat/trash/tomcat.trash/output/build" -Dcatalina.home="/Users/ssarnobat/trash/tomcat.trash/output/build" -Djava.io.tmpdir="/Users/ssarnobat/trash/tomcat.trash/output/build/temp" org.apache.catalina.startup.Main start
	
## Other info:	
	unset CATALINA_HOME
    output/build/bin/catalina.sh start
    
    CATALINA_HOME= ;"/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/bin/java" "-Djava.util.logging.config.file=/Users/ssarnobat/trash/tomcat.trash/output/build/conf/logging.properties" -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.protocol.handler.pkgs=org.apache.catalina.webresources -Denv=dev -Xms1024M -Xmx2048M -XX:PermSize=256M -XX:MaxPermSize=768m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=9088 -Djava.security.egd=file:/dev/./urandom -Dlog4j.configurationFile=file:///Users/ssarnobat/control4/log4j2.xml -Dlog4j2.debug=true -Duser.home=/Users/ssarnobat -Dignore.endorsed.dirs="" -classpath "/Users/ssarnobat/trash/tomcat.trash/output/build/bin/bootstrap.jar:/Users/ssarnobat/trash/tomcat.trash/output/build/bin/tomcat-juli.jar" -Dcatalina.base="/Users/ssarnobat/trash/tomcat.trash/output/build" -Dcatalina.home="/Users/ssarnobat/trash/tomcat.trash/output/build" -Djava.io.tmpdir="/Users/ssarnobat/trash/tomcat.trash/output/build/temp" org.apache.catalina.startup.Bootstrap start
    "/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/bin/java" -agentlib:jdwp=transport=dt_socket,suspend=y,address=localhost:57749 -Dfile.encoding=UTF-8 -classpath /Users/ssarnobat/trash/tomcat.trash/target/classes:/Users/ssarnobat/.m2/repository/javax/xml/jaxrpc-api/1.1/jaxrpc-api-1.1.jar:/Users/ssarnobat/.m2/repository/org/apache/ant/ant/1.10.2/ant-1.10.2.jar:/Users/ssarnobat/.m2/repository/org/apache/ant/ant-launcher/1.10.2/ant-launcher-1.10.2.jar:/Users/ssarnobat/.m2/repository/wsdl4j/wsdl4j/1.6.3/wsdl4j-1.6.3.jar:/Users/ssarnobat/.m2/repository/org/eclipse/jdt/core/compiler/ecj/4.6.1/ecj-4.6.1.jar:/Users/ssarnobat/.m2/repository/junit/junit/3.8.1/junit-3.8.1.jar org.apache.catalina.startup.Bootstrap


## Welcome to Apache Tomcat!

### What Is It?

The Apache Tomcat® software is an open source implementation of the Java
Servlet, JavaServer Pages, Java Expression Language and Java WebSocket
technologies. The Java Servlet, JavaServer Pages, Java Expression Language and
Java WebSocket specifications are developed under the
[Java Community Process](http://jcp.org/en/introduction/overview).

The Apache Tomcat software is developed in an open and participatory
environment and released under the
[Apache License version 2](http://www.apache.org/licenses/). The Apache Tomcat
project is intended to be a collaboration of the best-of-breed developers from
around the world. We invite you to participate in this open development
project. To learn more about getting involved,
[click here](http://tomcat.apache.org/getinvolved.html) or keep reading.

Apache Tomcat software powers numerous large-scale, mission-critical web
applications across a diverse range of industries and organizations. Some of
these users and their stories are listed on the
[PoweredBy wiki page](http://wiki.apache.org/tomcat/PoweredBy).

Apache Tomcat, Tomcat, Apache, the Apache feather, and the Apache Tomcat
project logo are trademarks of the Apache Software Foundation.

### The Latest Version

The current latest version in this branch (trunk) can be found on the [Tomcat 9.0](https://tomcat.apache.org/download-90.cgi) page.

### Documentation

The documentation available as of the date of this release is
included in the docs webapp which ships with tomcat. You can access that webapp
by starting tomcat and visiting http://localhost:8080/docs/ in your browser.
The most up-to-date documentation can be found at
http://tomcat.apache.org/tomcat-9.0-doc/.

### Installation

Please see [RUNNING.txt](RUNNING.txt) for more info.

### Licensing

Please see [LICENSE](LICENSE) for more info.

### Support and Mailing List Information

* Free community support is available through the
[tomcat-users](http://tomcat.apache.org/lists.html#tomcat-users) email list and
a dedicated [IRC channel](http://tomcat.apache.org/irc.html) (#tomcat on
Freenode).

* If you want freely available support for running Apache Tomcat, please see the
resources page [here](http://tomcat.apache.org/findhelp.html).

* If you want to be informed about new code releases, bug fixes,
security fixes, general news and information about Apache Tomcat, please
subscribe to the
[tomcat-announce](http://tomcat.apache.org/lists.html#tomcat-announce) email
list.

* If you have a concrete bug report for Apache Tomcat, please see the
instructions for reporting a bug
[here](http://tomcat.apache.org/bugreport.html).

### Contributing

Please see [CONTRIBUTING](CONTRIBUTING.md) for more info.
