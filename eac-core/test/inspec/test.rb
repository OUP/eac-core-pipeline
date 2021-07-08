describe port(22) do
  it { should be_listening }
end

describe port(8080) do
 it { should be_listening }
end

#describe http('http://localhost:8080/idp/', method: 'GET', open_timeout: 60, read_timeout: 60, max_redirects: 3) do
#   its('status') { should eq 200 }
#end

#describe http('http://localhost:8080/idp2/', method: 'GET', open_timeout: 60, read_timeout: 60, max_redirects: 3) do
#   its('status') { should eq 200 }
#end

describe package('java-11-amazon-corretto') do
  it { should be_installed }
end

describe package('tomcat') do
  it { should be_installed }
end

# package %w(httpd libtool httpd-devel) do
#   action :install
# end
describe package('httpd') do
  it { should be_installed }
end
describe package('libtool') do
  it { should be_installed }
end
describe package('httpd-devel') do
  it { should be_installed }
end

# package %w(autoconf.noarch gcc make) do
#   action :install
# end
describe package('autoconf') do
  it { should be_installed }
end
describe package('make') do
  it { should be_installed }
end

# package "sudo" do
#   action :install
# end
describe package('sudo') do
  it { should be_installed }
end

# execute 'rebuild_shib' do
#   command '/opt/shibboleth-idp/bin/build.sh'
# end
describe file('/opt/shibboleth-idp/bin/build.sh') do
  it { should exist }
end

# execute 'rebuild_shib_mu' do
#   command '/opt/shibboleth-idp-mu/bin/build.sh'
# end
describe file('/opt/shibboleth-idp-mu/bin/build.sh') do
  it { should exist }
end

# directory '/opt/chefhelper' do
#   owner 'root'
#   group 'root'
#   mode '0700'
#   action :create
#  end
describe directory('/opt/chefhelper') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'root' }
  its('mode') { should cmp '0700' }
end

#  cookbook_file '/opt/chefhelper/var2node.py' do
#   source 'var2node.py'
#   owner 'root'
#   group 'root'
#   mode '0700' 
#   action :create
#  end
describe file('/opt/chefhelper/var2node.py') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'root' }
  its('mode') { should cmp '0700' }
end

#  cookbook_file '/opt/chefhelper/EnvVariables-infra-primary.properties' do
#   source 'EnvVariables-infra-primary.properties'
#   owner 'root'
#   group 'root'
#   mode '0700'
#   action :create
#  end
describe file('/opt/chefhelper/EnvVariables-infra-primary.properties') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'root' }
  its('mode') { should cmp '0700' }
end

# remote_directory "/opt/shibboleth-idp" do
#   source 'shibboleth-idp'
#   files_owner 'root'                                                                 
#   files_group 'tomcat'
#   files_mode '0750'
#   action :create
#   recursive true                                                                      
# end
describe directory('/opt/shibboleth-idp') do
  it { should exist }
  it { should be_owned_by 'root' }
  its('mode') { should cmp '0755' }
end

# remote_directory "/opt/shibboleth-idp-mu" do
#   source 'shibboleth-idp-mu'
#   files_owner 'root'
#   files_group 'tomcat'
#   files_mode '0750'
#   action :create
#   recursive true
# end
describe directory('/opt/shibboleth-idp-mu') do
  it { should exist }
  it { should be_owned_by 'root' }
  its('mode') { should cmp '0755' }
end

# directory '/opt/shibboleth-idp/logs' do
#   owner 'root'
#   group 'tomcat'
#   mode '0775'
#   action :create
# end
describe directory('/opt/shibboleth-idp/logs') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0775' }
end

# directory '/opt/shibboleth-idp-mu/logs' do
#   owner 'root'
#   group 'tomcat'
#   mode '0775'
#   action :create
# end
describe directory('/opt/shibboleth-idp-mu/logs') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0775' }
end

# remote_directory "/etc/tomcat" do
#   source 'tomcat'
#   files_owner 'root' 
#   files_group 'tomcat'
#   files_mode '0750'
#   action :create
#   recursive true                                                                      
# end
describe directory('/etc/tomcat') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0755' }
end

# remote_directory "/usr/share/tomcat/keypair" do
#   source node.chef_environment + '/' + 'keypair'
#   files_owner 'root'
#   files_group 'tomcat'
#   files_mode '0750'
#   action :create
#   recursive true
# end
describe directory('/usr/share/tomcat/keypair') do
  it { should exist }
  it { should be_owned_by 'root' }
  its('mode') { should cmp '0755' }
end

# directory '/usr/share/tomcat/resources' do
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
# end
describe directory('/usr/share/tomcat/resources') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/usr/share/tomcat/resources/erightsConnection.properties' do
#   source 'erightsConnection.properties.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
# end
describe file('/usr/share/tomcat/resources/erightsConnection.properties') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/edit-webapp/erightsConnection.properties' do
#   source 'erightsConnection.properties.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
# end
describe file('/opt/shibboleth-idp/edit-webapp/erightsConnection.properties') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/edit-webapp/erightsConnection.properties' do
#   source 'erightsConnection.properties-mu.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
# end
describe file('/opt/shibboleth-idp-mu/edit-webapp/erightsConnection.properties') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/conf/idp.properties' do
#   source 'idp2.properties.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
# end
describe file('/opt/shibboleth-idp-mu/conf/idp.properties') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/conf/idp.properties' do
#   source 'idp.properties.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
# end
describe file('/opt/shibboleth-idp/conf/idp.properties') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/conf/attribute-resolver.xml' do
#   source 'attribute-resolver.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:idparn => node['EnvVariables']['IDPARN'])
#  end
describe file('/opt/shibboleth-idp/conf/attribute-resolver.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/conf/attribute-resolver.xml' do
#   source 'attribute-resolver.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:idparn => node['EnvVariables']['IDPARNMU'])
#  end
describe file('/opt/shibboleth-idp-mu/conf/attribute-resolver.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/messages/messages.properties' do
#   source 'messages.properties.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
#  end
describe file('/opt/shibboleth-idp/messages/messages.properties') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/messages/messages.properties' do
#   source 'messages.properties.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
#  end
describe file('/opt/shibboleth-idp-mu/messages/messages.properties') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/CES.xml' do
#   source 'CES.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp/metadata/CES.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/EREADER.xml' do
#   source 'EREADER.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp/metadata/EREADER.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/metadata/EREADER.xml' do
#   source 'EREADER.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp-mu/metadata/EREADER.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/EREADER_MOBILE.xml' do
#   source 'EREADER_MOBILE.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp/metadata/EREADER_MOBILE.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/metadata/EREADER_MOBILE.xml' do
#   source 'EREADER_MOBILE.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp-mu/metadata/EREADER_MOBILE.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/OALD.xml' do
#   source 'OALD.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp/metadata/OALD.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/OALD_MOBILE.xml' do
#   source 'OALD_MOBILE.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp/metadata/OALD_MOBILE.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/OLB.xml' do
#   source 'OLB.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp/metadata/OLB.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/OLB_MOBILE.xml' do
#   source 'OLB_MOBILE.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp/metadata/OLB_MOBILE.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/ORB.xml' do
#   source 'ORB.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp/metadata/ORB.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/metadata/ORB.xml' do
#   source 'ORB.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp-mu/metadata/ORB.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/ORB_MOBILE.xml' do
#   source 'ORB_MOBILE.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp/metadata/ORB.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/metadata/ORB_MOBILE.xml' do
#   source 'ORB_MOBILE.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])
#  end
describe file('/opt/shibboleth-idp-mu/metadata/ORB_MOBILE.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/edit-webapp/OLB/logout.html' do
#   source 'logout.html.olb.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
#  end
describe file('/opt/shibboleth-idp/edit-webapp/OLB/logout.html') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/edit-webapp/ORB/logout.html' do
#   source 'logout.html.orb.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
#  end
describe file('/opt/shibboleth-idp/edit-webapp/ORB/logout.html') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# cookbook_file '/opt/shibboleth-idp-mu/edit-webapp/ORB/logout.js' do
#   source 'logout.js'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
# end
describe file('/opt/shibboleth-idp-mu/edit-webapp/ORB/logout.js') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/edit-webapp/WEB-INF/web.xml' do
#   source 'web.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
#  end
describe file('/opt/shibboleth-idp/edit-webapp/WEB-INF/web.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/edit-webapp/WEB-INF/web.xml' do
#   source 'web.xml.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables()
#  end
describe file('/opt/shibboleth-idp-mu/edit-webapp/WEB-INF/web.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/credentials/idp-encryption.key' do
#   source 'idp-encryption.key.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(
#    :key => shibboleth_secrets["idp-encryption-key"]
#   )
#  end
describe file('/opt/shibboleth-idp/credentials/idp-encryption.key') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/credentials/idp-encryption.key' do
#   source 'idp-encryption.key.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(
#    :key => shibboleth_secrets["idp-encryption-key-mu"]
#   )
#  end
describe file('/opt/shibboleth-idp-mu/credentials/idp-encryption.key') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/credentials/idp-signing.key' do
#   source 'idp-signing.key.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(
#    :key => shibboleth_secrets["idp-signing-key"]
#   )
#  end
describe file('/opt/shibboleth-idp/credentials/idp-signing.key') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/credentials/idp-signing.key' do
#   source 'idp-signing.key.erb'
#   owner 'root'
#   group 'tomcat'
#   mode '0750'
#   action :create
#   variables(
#    :key => shibboleth_secrets["idp-signing-key-mu"]
#   )
#  end
describe file('/opt/shibboleth-idp-mu/credentials/idp-signing.key') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/NGS_UK.xml' do                              
#   source 'NGS_UK.xml.erb'                                                           
#   owner 'root'                                                                   
#   group 'tomcat'                                                                 
#   mode '0750'                                                                    
#   action :create                                                                 
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])                     
#  end
describe file('/opt/shibboleth-idp/metadata/NGS_UK.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp/metadata/NGS_ANZ.xml' do                              
#   source 'NGS_ANZ.xml.erb'                                                           
#   owner 'root'                                                                   
#   group 'tomcat'                                                                 
#   mode '0750'                                                                    
#   action :create                                                                 
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])                     
#  end
describe file('/opt/shibboleth-idp/metadata/NGS_ANZ.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/metadata/NGS_UK.xml' do                              
#   source 'NGS_UK.xml.erb'                                                           
#   owner 'root'                                                                   
#   group 'tomcat'                                                                 
#   mode '0750'                                                                    
#   action :create                                                                 
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])                     
#  end
describe file('/opt/shibboleth-idp-mu/metadata/NGS_UK.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# template '/opt/shibboleth-idp-mu/metadata/NGS_ANZ.xml' do                              
#   source 'NGS_ANZ.xml.erb'                                                           
#   owner 'root'                                                                   
#   group 'tomcat'                                                                 
#   mode '0750'                                                                    
#   action :create                                                                 
#   variables(:apicesurl => node['EnvVariables']['APICESURL'])                     
#  end
describe file('/opt/shibboleth-idp-mu/metadata/NGS_ANZ.xml') do
  it { should exist }
  it { should be_owned_by 'root' }
  it { should be_grouped_into 'tomcat' }
  its('mode') { should cmp '0750' }
end

# group 'tcs-installer' do
#   action :create
#   append true
# end
describe group('tcs-installer') do
  it { should exist }
end

# group 'tcs-developer' do
#   action :create
#   append true
# end
describe group('tcs-developer') do
  it { should exist }
end

# user 'tcs-installer' do
#   comment 'tcs-installer'
#   uid 503
#   gid 'tcs-installer'
#   home '/home/tcs-installer'
#   shell '/bin/bash'
#   password '$1$JJsvHslasdfjVEroftprNn4JHtDi'
# end
describe user('tcs-installer') do
  it { should exist }
  its('home') { should eq '/home/tcs-installer' }
  its('shell') { should eq '/bin/bash' }
end

# user 'tcs-developer' do
#   comment 'tcs-developer'
#   uid 502
#   gid 'tcs-developer'
#   home '/home/tcs-developer'
#   shell '/bin/bash'
#   password '$1$JJsvHslasdfjVEroftprNn4JHtDi'
# end
describe user('tcs-developer') do
  it { should exist }
  its('home') { should eq '/home/tcs-developer' }
  its('shell') { should eq '/bin/bash' }
end

# directory '/home/tcs-developer' do
#   owner 'tcs-developer'
#   group 'tcs-developer'
#   mode '0755'
#   action :create
# end
describe directory('/home/tcs-developer') do
  it { should exist }
  it { should be_owned_by 'tcs-developer' }
  it { should be_grouped_into 'tcs-developer' }
  its('mode') { should cmp '0755' }
end

# directory '/home/tcs-installer' do
#   owner 'tcs-installer'
#   group 'tcs-installer'
#   mode '0755'
#   action :create
# end
describe directory('/home/tcs-installer') do
  it { should exist }
  it { should be_owned_by 'tcs-installer' }
  it { should be_grouped_into 'tcs-installer' }
  its('mode') { should cmp '0755' }
end

# directory '/home/tcs-developer/.ssh' do
#   owner 'tcs-developer'
#   group 'tcs-developer'
#   mode '0755'
#   action :create
# end
describe directory('/home/tcs-developer/.ssh') do
  it { should exist }
  it { should be_owned_by 'tcs-developer' }
  it { should be_grouped_into 'tcs-developer' }
  its('mode') { should cmp '0755' }
end

# directory '/home/tcs-installer/.ssh' do
#   owner 'tcs-installer'
#   group 'tcs-installer'
#   mode '0755'
#   action :create
# end
describe directory('/home/tcs-installer/.ssh') do
  it { should exist }
  it { should be_owned_by 'tcs-installer' }
  it { should be_grouped_into 'tcs-installer' }
  its('mode') { should cmp '0755' }
end

# sudo 'passwordless-tcs-installer' do
#   commands ['/bin/chown', '/etc/init.d/tomcat', '/bin/kill']
#   user 'tcs-installer'
#   nopasswd true
# end
describe command('/bin/chown') do
  it { should exist }
end
# describe command('/etc/init.d/tomcat') do
#   it { should exist }
# end
describe command('/bin/kill') do
  it { should exist }
end

# execute "pubkey-tcs-installer" do
#   command "echo #{secrets['tcsinstaller-pubkey']} >>/home/tcs-installer/.ssh/authorized_keys"
#   not_if "grep #{secrets['tcsdeveloper-pubkey']} /home/tcs-installer/.ssh/authorized_keys"
# end
# execute "pubkey-tcs-installer-perms" do
#   command "chown tcs-installer:tcs-installer /home/tcs-installer/.ssh/authorized_keys"
# end
describe file('/home/tcs-installer/.ssh/authorized_keys') do
  it { should be_owned_by 'tcs-installer' }
  it { should be_grouped_into 'tcs-installer' }
  its('size') { should > 0 }
end

# execute "pubkey-tcs-developer" do
#   command "echo #{secrets['tcsdeveloper-pubkey']} >>/home/tcs-developer/.ssh/authorized_keys"
#   not_if "grep #{secrets['tcsinstaller-pubkey']} /home/tcs-developer/.ssh/authorized_keys"
# end
# execute "pubkey-tcs-developer-perms" do
#   command "chown tcs-developer:tcs-developer /home/tcs-developer/.ssh/authorized_keys"
# end
describe file('/home/tcs-developer/.ssh/authorized_keys') do
  it { should be_owned_by 'tcs-developer' }
  it { should be_grouped_into 'tcs-developer' }
  its('size') { should > 0 }
end
