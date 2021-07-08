secrets = Chef::EncryptedDataBagItem.load("secrets", node.chef_environment)

group 'tcs-installer' do
  action :create
  append true
end

group 'tcs-developer' do
  action :create
  append true
end

user 'tcs-installer' do
  comment 'tcs-installer'
  uid 503
  gid 'tcs-installer'
  home '/home/tcs-installer'
  shell '/bin/bash'
  password '$1$JJsvHslasdfjVEroftprNn4JHtDi'
end

user 'tcs-developer' do
  comment 'tcs-developer'
  uid 502
  gid 'tcs-developer'
  home '/home/tcs-developer'
  shell '/bin/bash'
  password '$1$JJsvHslasdfjVEroftprNn4JHtDi'
end

directory '/home/tcs-developer' do
  owner 'tcs-developer'
  group 'tcs-developer'
  mode '0755'
  action :create
end

directory '/home/tcs-installer' do
  owner 'tcs-installer'
  group 'tcs-installer'
  mode '0755'
  action :create
end

directory '/home/tcs-developer/.ssh' do
  owner 'tcs-developer'
  group 'tcs-developer'
  mode '0755'
  action :create
end

directory '/home/tcs-installer/.ssh' do
  owner 'tcs-installer'
  group 'tcs-installer'
  mode '0755'
  action :create
end

sudo 'passwordless-tcs-installer' do
  commands ['/bin/chown', '/etc/init.d/tomcat', '/bin/kill']
  user 'tcs-installer'
  nopasswd true
end

execute "pubkey-tcs-installer" do
  command "echo #{secrets['tcsinstaller-pubkey']} >>/home/tcs-installer/.ssh/authorized_keys"
  not_if "grep #{secrets['tcsdeveloper-pubkey']} /home/tcs-installer/.ssh/authorized_keys"
end

execute "pubkey-tcs-developer" do
  command "echo #{secrets['tcsdeveloper-pubkey']} >>/home/tcs-developer/.ssh/authorized_keys"
  not_if "grep #{secrets['tcsinstaller-pubkey']} /home/tcs-developer/.ssh/authorized_keys"
end

execute "pubkey-tcs-installer-perms" do
  command "chown tcs-installer:tcs-installer /home/tcs-installer/.ssh/authorized_keys"
end

execute "pubkey-tcs-developer-perms" do
  command "chown tcs-developer:tcs-developer /home/tcs-developer/.ssh/authorized_keys"
end


