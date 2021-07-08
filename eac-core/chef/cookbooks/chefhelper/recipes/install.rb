directory '/opt/chefhelper' do
 owner 'root'
 group 'root'
 mode '0700'
 action :create
end

cookbook_file '/opt/chefhelper/var2node.py' do
 source 'var2node.py'
 owner 'root'
 group 'root'
 mode '0700' 
 action :create
end

cookbook_file '/opt/chefhelper/EnvVariables-infra-primary.properties' do
 source 'EnvVariables-infra-primary.properties'
 owner 'root'
 group 'root'
 mode '0700'
 action :create
end

cookbook_file '/opt/chefhelper/EnvVariables-dev-primary.properties' do
    source 'EnvVariables-dev-primary.properties'
    owner 'root'
    group 'root'
    mode '0700'
    action :create
end

cookbook_file '/opt/chefhelper/EnvVariables-test-primary.properties' do
    source 'EnvVariables-test-primary.properties'
    owner 'root'
    group 'root'
    mode '0700'
    action :create
end
