
ENV['JAVA_HOME'] = '/usr/lib/jvm/java-11-amazon-corretto.x86_64'

execute 'rebuild_shib' do
	  command '/opt/shibboleth-idp/bin/build.sh'
end

execute 'rebuild_shib_mu' do
	  command '/opt/shibboleth-idp-mu/bin/build.sh'
end

service "tomcat" do
    action :enable
end
service "tomcat" do
  action :start
end
#
