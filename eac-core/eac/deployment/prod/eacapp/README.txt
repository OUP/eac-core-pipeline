############################
#JBoss Memory
############################

The memory settings for JBoss need to changed to fully utilize the memory available. The following settings are for a server of 8GB.

1) Open $JBOSS_HOME/bin/run.conf
2) Find the line something like

if [ "x$JAVA_OPTS" = "x" ]; then
   JAVA_OPTS="-Xms128m -Xmx512m -XX:MaxPermSize=256m -Dorg.jboss.resolver.warning=true -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000"
fi

3) Change the following values to something like:

-Xms512m 
-Xmx6144m 
-XX:MaxPermSize=512m

############################
#JBoss port conflict
############################

There is port that conflicts with a process already using the port on a Verizon server. To fix this change the JBoss port by doing the following:

1) Open $JBOSS_HOME/server/$INSTANCE/conf/bindingservice.beans/META-INF/bindings-jboss-beans.xml
2) Find

    <!-- Pooled invoker -->
    <bean class="org.jboss.services.binding.ServiceBindingMetadata">
       <property name="serviceName">jboss:service=invoker,type=pooled</property>
       <property name="port">4445</property>
       <property name="description">Socket for the legacy Pooled invoker</property>
    </bean> 
            
3) Change the port from 4445 to another port not in already in use.

############################
#JBoss VFS issue
############################

JBoss has a problem with symlinks that makes the tmp directory grow uncontrollably. This has been fixed by replacing the jboss-vfs.jar library.
Please see http://community.jboss.org/message/549424#549424