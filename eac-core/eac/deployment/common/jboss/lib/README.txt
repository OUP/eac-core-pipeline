The logging libraries for Jboss should be updated to version 2.1.1.GA because they 
provide support for filtering log messages based on the application the log
messages belong to.

See http://community.jboss.org/wiki/SeparatingApplicationLogs for full details.

The libraries jboss-logging-jdk.jar, jboss-logging-spi.jar and jboss-logging-log4j.jar
should all be replaced under $JBOSS_HOME/lib.

Configuration to support the filtering of log messages has been added to
$JBOSS_HOME$/server/default/conf/jboss-log4j.xml

For example, we can add the following to an appender 
Note: eacWebService.war must match the name of the .war

<!-- Filter only logging for eacWebService.war -->
<filter class="org.jboss.logging.filter.TCLMCFilter">
   <param name="AcceptOnMatch" value="true"/>
   <param name="DeployURL" value="eacWebService.war"/>
</filter>

<!-- end the filter chain here -->
<filter class="org.apache.log4j.varia.DenyAllFilter"></filter>

