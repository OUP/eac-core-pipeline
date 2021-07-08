
execute 'amazon-linux-extras-enable-java11' do
  command 'amazon-linux-extras enable java-openjdk11'
end

execute 'java-openjdk11' do
  command 'amazon-linux-extras install java-openjdk11'
end

execute 'amazon-linux-extras-enable-tomcat' do
  command 'amazon-linux-extras enable tomcat9'
end

execute 'tomcat9' do
  command 'amazon-linux-extras install tomcat9'
end

package %w(httpd libtool httpd-devel) do
    action :install
end

package %w(autoconf.noarch gcc make) do
    action :install
end
