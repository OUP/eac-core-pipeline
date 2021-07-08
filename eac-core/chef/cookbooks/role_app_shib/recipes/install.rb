
remote_directory "/opt/shibboleth-idp" do
  source 'shibboleth-idp'
  files_owner 'root'                                                                 
  files_group 'tomcat'
  files_mode '0750'
  action :create
  recursive true                                                                      
end

remote_directory "/opt/shibboleth-idp-mu" do
  source 'shibboleth-idp-mu'
  files_owner 'root'
  files_group 'tomcat'
  files_mode '0750'
  action :create
  recursive true
end

directory '/opt/shibboleth-idp/logs' do
  owner 'root'
  group 'tomcat'
  mode '0775'
  action :create
end

directory '/opt/shibboleth-idp-mu/logs' do
  owner 'root'
  group 'tomcat'
  mode '0775'
  action :create
end





	

