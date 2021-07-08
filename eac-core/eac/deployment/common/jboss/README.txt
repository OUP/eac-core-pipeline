JBoss has a bug where its instance tmp directory vfs-nested.tmp grows very large. 
The fix is as follows

Open $JBOSS_HOME/server/${instance}/conf/bootstrap/vfs.xml

Make sure the entry below has been added

<entry>
<key>${jboss.server.home.url}farm</key>
<value><inject bean="VfsNamesExceptionHandler"/></value>
</entry>