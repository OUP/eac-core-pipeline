shibboleth_secrets = Chef::EncryptedDataBagItem.load("secrets", node.chef_environment)

remote_directory "/etc/tomcat" do
  source 'tomcat'
  files_owner 'root' 
  files_group 'tomcat'
  files_mode '0750'
  action :create
  recursive true                                                                      
end

remote_directory "/usr/share/tomcat/keypair" do
  source node.chef_environment + '/' + 'keypair'
  files_owner 'root'
  files_group 'tomcat'
  files_mode '0750'
  action :create
  recursive true
end

remote_directory '/usr/share/tomcat/lib' do
  source 'tomcatexternaljars'
  files_owner 'root'
  files_group 'root'
  files_mode '0644'
  action :create
  recursive true
end

directory '/usr/share/tomcat/resources' do
  owner 'root'
  group 'tomcat'
  mode '0750'
  action :create
end

template '/usr/share/tomcat/resources/erightsConnection.properties' do
  source 'erightsConnection.properties.erb'
  owner 'root'
  group 'tomcat'
  mode '0750'
  action :create
  variables(
   :erightsconnusername => node['EnvVariables']['ERIGHTSCONNUSERNAME'],
   :erightsconnpasswd => shibboleth_secrets['erightsconnpasswd'],
   :internalatyponlburl => node['EnvVariables']['INTERNALATYPONLBURL'],
   :erightsconnmin => node['EnvVariables']['ERIGHTSCONNMIN'],
   :erightsconnmax => node['EnvVariables']['ERIGHTSCONNMAX'],
   :lambdaprincipalarn => node['EnvVariables']['LAMBDAPRINCIPALARN'],
   :lambdaprincipalarnmu => node['EnvVariables']['LAMBDAPRINCIPALARNMU'],
   :awsaccount => node['EnvVariables']['AWSACCOUNT'],
   :rolesuffixwithseparator => node['EnvVariables']['ROLESUFFIXWITHSEPARATOR'],
   :internalatyponlbdnsurl => node['EnvVariables']['INTERNALATYPONLBDNSURL']
  )
end

template '/opt/shibboleth-idp/edit-webapp/erightsConnection.properties' do
  source 'erightsConnection.properties.erb'
  owner 'root'
  group 'tomcat'
  mode '0750'
  action :create
  variables(
   :erightsconnusername => node['EnvVariables']['ERIGHTSCONNUSERNAME'],
   :erightsconnpasswd => shibboleth_secrets['erightsconnpasswd'],
   :internalatyponlburl => node['EnvVariables']['INTERNALATYPONLBURLDEFAULT'],
   :erightsconnmin => node['EnvVariables']['ERIGHTSCONNMIN'],
   :erightsconnmax => node['EnvVariables']['ERIGHTSCONNMAX'],
   :lambdaprincipalarn => node['EnvVariables']['LAMBDAPRINCIPALARNDEFAULT'],
   :lambdaprincipalarnmu => node['EnvVariables']['LAMBDAPRINCIPALARNMUDEFAULT'],
   :awsaccount => node['EnvVariables']['AWSACCOUNT'],
   :rolesuffixwithseparator => node['EnvVariables']['ROLESUFFIXWITHSEPARATOR'],
   :internalatyponlbdnsurl => node['EnvVariables']['INTERNALATYPONLBDNSURLDEFAULT']
  )
end

template '/opt/shibboleth-idp-mu/edit-webapp/erightsConnection.properties' do
  source 'erightsConnection.properties-mu.erb'
  owner 'root'
  group 'tomcat'
  mode '0750'
  action :create
  variables(
   :erightsconnusername => node['EnvVariables']['ERIGHTSCONNUSERNAME'],
   :erightsconnpasswd => shibboleth_secrets['erightsconnpasswd'],
   :internalatyponlburl => node['EnvVariables']['INTERNALATYPONLBURLDEFAULT'],
   :erightsconnmin => node['EnvVariables']['ERIGHTSCONNMIN'],
   :erightsconnmax => node['EnvVariables']['ERIGHTSCONNMAX'],
   :lambdaprincipalarn => node['EnvVariables']['LAMBDAPRINCIPALARN'],
   :lambdaprincipalarnmu => node['EnvVariables']['MULAMBDAPRINCIPALARNMUDEFAULT'],
   :awsaccount => node['EnvVariables']['AWSACCOUNTDEFAULT'],
   :rolesuffixwithseparator => node['EnvVariables']['ROLESUFFIXWITHSEPARATOR'],
   :internalatyponlbdnsurl => node['EnvVariables']['INTERNALATYPONLBDNSURLDEFAULT'],
  )
end

template '/opt/shibboleth-idp-mu/conf/idp.properties' do
  source 'idp2.properties.erb'
  owner 'root'
  group 'tomcat'
  mode '0750'
  action :create
  variables(
   :dbuser => node['EnvVariables']['DBUSER'],
   :dbpasswd => node['EnvVariables']['DBPASSWD'],
   :environment => node['EnvVariables']['ENVIRONMENT'],
   :domain => node['EnvVariables']['DOMAIN'],
   :dbname => node['EnvVariables']['IDPDBNAMEMU'],
   :sealer_storepass => shibboleth_secrets['idp-storepass-mu'],
   :sealer_keypass => shibboleth_secrets['idp-keypass-mu'],
   :idp_entityid => node['EnvVariables']['ENTITYIDMU'],
   :idp_leakdetectionthreshold => node['EnvVariables']['IDP_LEAKDETECTIONTHRESHOLD'],
   :idp_maximumpoolsize => node['EnvVariables']['IDP_MAXIMUMPOOLSIZE'],
   :idp_minimumidle => node['EnvVariables']['IDP_MINIMUMIDLE'],
   :idp_transaction_isolation => node['EnvVariables']['IDP_TRANSACTION_ISOLATION']
  )
end

template '/opt/shibboleth-idp/conf/idp.properties' do
  source 'idp.properties.erb'
  owner 'root'
  group 'tomcat'
  mode '0750'
  action :create
  variables(
   :dbuser => node['EnvVariables']['DBUSER'],
   :dbpasswd => node['EnvVariables']['DBPASSWD'],
   :environment => node['EnvVariables']['ENVIRONMENT'],
   :domain => node['EnvVariables']['DOMAIN'],
   :dbname => node['EnvVariables']['IDPDBNAME'],
   :sealer_storepass => shibboleth_secrets['idp-storepass'],
   :sealer_keypass => shibboleth_secrets['idp-keypass'],
   :idp_entityid => node['EnvVariables']['ENTITYID'],
   :idp_leakdetectionthreshold => node['EnvVariables']['IDP_LEAKDETECTIONTHRESHOLD'],
   :idp_maximumpoolsize => node['EnvVariables']['IDP_MAXIMUMPOOLSIZE'],
   :idp_minimumidle => node['EnvVariables']['IDP_MINIMUMIDLE'],
   :idp_transaction_isolation => node['EnvVariables']['IDP_TRANSACTION_ISOLATION']
  )
end

template '/opt/shibboleth-idp-mu/conf/global.xml' do
  source 'global.xml.erb'
  owner 'root'
  group 'tomcat'
  mode '0750'
  action :create
  variables(
   :memcacheep => node['EnvVariables']['MEMCACHE_ENDPOINT'],
  )
end

template '/opt/shibboleth-idp/conf/global.xml' do
  source 'global.xml.erb'
  owner 'root'
  group 'tomcat'
  mode '0750'
  action :create
  variables(
   :memcacheep => node['EnvVariables']['MEMCACHE_ENDPOINT'],
  )
end

template '/opt/shibboleth-idp/conf/attribute-resolver.xml' do
 source 'attribute-resolver.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:idparn => node['EnvVariables']['IDPARN'])
end

template '/opt/shibboleth-idp-mu/conf/attribute-resolver.xml' do
 source 'attribute-resolver.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:idparn => node['EnvVariables']['IDPARNMU'])
end

template '/opt/shibboleth-idp/messages/messages.properties' do
 source 'messages.properties.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :shibbolethpublickey => shibboleth_secrets["pubkey"],
  :ceshostedzone => node['EnvVariables']['CESHOSTEDZONE'],
  :olbhostedzone => node['EnvVariables']['OLBHOSTEDZONE'],
  :orbhostedzone => node['EnvVariables']['ORBHOSTEDZONE'],
  :telluswhatyouthink => node['EnvVariables']['TELLUSWHATYOUTHINK'],
  :contactus => node['EnvVariables']['CONTACTUS'],
  :helpandsupport => node['EnvVariables']['HELPANDSUPPORT'],
  :elturl => node['EnvVariables']['ELTURL'],
  :otcurl => node['EnvVariables']['OTCURL'],
  :olburl => node['EnvVariables']['OLBURL'],
  :oldurl => node['EnvVariables']['OLDURL'],
  :aboutusurl => node['EnvVariables']['ABOUTUSURL'],
  :ourhistoryurl => node['EnvVariables']['OURHISTORYURL'],
  :annualreporturl => node['EnvVariables']['ANNUALREPORTURL'],
  :theweworkurl => node['EnvVariables']['THEWEWORKURL'],
  :workingforoupurl => node['EnvVariables']['WORKINGFOROUPURL'],
  :privacypolicyurl => node['EnvVariables']['PRIVACYPOLICYURL'],
  :cookiepolicyurl => node['EnvVariables']['COOKIEPOLICYURL'],
  :tcurl => node['EnvVariables']['TCURL'],
  :accessibilityurl => node['EnvVariables']['ACCESSIBILITYURL']
 )
end

template '/opt/shibboleth-idp-mu/messages/messages.properties' do
 source 'messages.properties.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :shibbolethpublickey => shibboleth_secrets["pubkey"],
  :ceshostedzone => node['EnvVariables']['CESHOSTEDZONE'],
  :olbhostedzone => node['EnvVariables']['OLBHOSTEDZONE'],
  :orbhostedzone => node['EnvVariables']['ORBHOSTEDZONE'],
  :telluswhatyouthink => node['EnvVariables']['TELLUSWHATYOUTHINK'],
  :contactus => node['EnvVariables']['CONTACTUS'],
  :helpandsupport => node['EnvVariables']['HELPANDSUPPORT'],
  :elturl => node['EnvVariables']['ELTURL'],
  :otcurl => node['EnvVariables']['OTCURL'],
  :olburl => node['EnvVariables']['OLBURL'],
  :oldurl => node['EnvVariables']['OLDURL'],
  :aboutusurl => node['EnvVariables']['ABOUTUSURL'],
  :ourhistoryurl => node['EnvVariables']['OURHISTORYURL'],
  :annualreporturl => node['EnvVariables']['ANNUALREPORTURL'],
  :theweworkurl => node['EnvVariables']['THEWEWORKURL'],
  :workingforoupurl => node['EnvVariables']['WORKINGFOROUPURL'],
  :privacypolicyurl => node['EnvVariables']['PRIVACYPOLICYURL'],
  :cookiepolicyurl => node['EnvVariables']['COOKIEPOLICYURL'],
  :tcurl => node['EnvVariables']['TCURL'],
  :accessibilityurl => node['EnvVariables']['ACCESSIBILITYURL']
 )
end

template '/opt/shibboleth-idp/metadata/CES.xml' do
 source 'CES.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp/metadata/EREADER.xml' do
 source 'EREADER.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp-mu/metadata/EREADER.xml' do
 source 'EREADER.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp/metadata/EREADER_MOBILE.xml' do
 source 'EREADER_MOBILE.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp-mu/metadata/EREADER_MOBILE.xml' do
 source 'EREADER_MOBILE.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp/metadata/OALD.xml' do
 source 'OALD.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp/metadata/OALD_MOBILE.xml' do
 source 'OALD_MOBILE.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end


template '/opt/shibboleth-idp/metadata/OLB.xml' do
 source 'OLB.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp/metadata/OLB_MOBILE.xml' do
 source 'OLB_MOBILE.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp/metadata/ORB.xml' do
 source 'ORB.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp-mu/metadata/ORB.xml' do
 source 'ORB.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp/metadata/ORB_MOBILE.xml' do
 source 'ORB_MOBILE.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp-mu/metadata/ORB_MOBILE.xml' do
 source 'ORB_MOBILE.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp/metadata/HUB.xml' do
 source 'HUB.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(:apicesurl => node['EnvVariables']['APICESURL'])
end

template '/opt/shibboleth-idp/edit-webapp/OLB/logout.html' do
 source 'logout.html.olb.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :ceshostedzone => node['EnvVariables']['CESHOSTEDZONE'],
  :olbhostedzone => node['EnvVariables']['OLBHOSTEDZONE']
 )
end

template '/opt/shibboleth-idp/edit-webapp/ORB/logout.html' do
 source 'logout.html.orb.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :ceshostedzone => node['EnvVariables']['CESHOSTEDZONE'],
  :olbhostedzone => node['EnvVariables']['OLBHOSTEDZONE'],
  :orbhostedzone => node['EnvVariables']['ORBHOSTEDZONE']
 )
end

cookbook_file '/opt/shibboleth-idp-mu/edit-webapp/ORB/logout.js' do
  source 'logout.js'
  owner 'root'
  group 'tomcat'
  mode '0750'
  action :create
end


ruby_block "update logout.js" do
  block do
    fe = Chef::Util::FileEdit.new("/opt/shibboleth-idp-mu/edit-webapp/ORB/logout.js")
    fe.search_file_replace(/\|ORBTARGETURL\|/, node['EnvVariables']['ORBTARGETURL'])
    fe.write_file
  end
end

template '/opt/shibboleth-idp/edit-webapp/WEB-INF/web.xml' do
 source 'web.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :idphome => '/opt/shibboleth-idp',
  :corsallowedorigins => node['EnvVariables']['CORSALLOWEDORIGINS']
 )
end

template '/opt/shibboleth-idp-mu/edit-webapp/WEB-INF/web.xml' do
 source 'web.xml.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :idphome => '/opt/shibboleth-idp-mu',
  :corsallowedorigins => node['EnvVariables']['CORSALLOWEDORIGINSIDP2']
 )
end

template '/opt/shibboleth-idp/credentials/idp-encryption.key' do
 source 'idp-encryption.key.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :key => shibboleth_secrets["idp-encryption-key"]
 )
end

template '/opt/shibboleth-idp-mu/credentials/idp-encryption.key' do
 source 'idp-encryption.key.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :key => shibboleth_secrets["idp-encryption-key-mu"]
 )
end

template '/opt/shibboleth-idp/credentials/idp-signing.key' do
 source 'idp-signing.key.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :key => shibboleth_secrets["idp-signing-key"]
 )
end

template '/opt/shibboleth-idp-mu/credentials/idp-signing.key' do
 source 'idp-signing.key.erb'
 owner 'root'
 group 'tomcat'
 mode '0750'
 action :create
 variables(
  :key => shibboleth_secrets["idp-signing-key-mu"]
 )
end

template '/opt/shibboleth-idp/metadata/NGS_UK.xml' do                              
  source 'NGS_UK.xml.erb'                                                           
  owner 'root'                                                                   
  group 'tomcat'                                                                 
  mode '0750'                                                                    
  action :create                                                                 
  variables(:apicesurl => node['EnvVariables']['APICESURL'])                     
 end

 template '/opt/shibboleth-idp/metadata/NGS_ANZ.xml' do                              
  source 'NGS_ANZ.xml.erb'                                                           
  owner 'root'                                                                   
  group 'tomcat'                                                                 
  mode '0750'                                                                    
  action :create                                                                 
  variables(:apicesurl => node['EnvVariables']['APICESURL'])                     
 end

 template '/opt/shibboleth-idp-mu/metadata/NGS_UK.xml' do                              
  source 'NGS_UK.xml.erb'                                                           
  owner 'root'                                                                   
  group 'tomcat'                                                                 
  mode '0750'                                                                    
  action :create                                                                 
  variables(:apicesurl => node['EnvVariables']['APICESURL'])                     
 end

 template '/opt/shibboleth-idp-mu/metadata/NGS_ANZ.xml' do                              
  source 'NGS_ANZ.xml.erb'                                                           
  owner 'root'                                                                   
  group 'tomcat'                                                                 
  mode '0750'                                                                    
  action :create                                                                 
  variables(:apicesurl => node['EnvVariables']['APICESURL'])                     
 end

 template '/opt/shibboleth-idp/metadata/OIDC.xml' do                              
  source 'OIDC.xml.erb'                                                           
  owner 'root'                                                                   
  group 'tomcat'                                                                 
  mode '0750'                                                                    
  action :create                                                                 
  variables(:oidcurl => node['EnvVariables']['OIDC_URL'])                     
 end
