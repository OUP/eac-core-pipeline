#
# Cookbook:: role_app_shib
# Recipe:: default
#
# Copyright:: 2019, The Authors, All Rights Reserved.
include_recipe 'tomcat::install'
include_recipe 'role_app_shib::install'
include_recipe 'chefhelper::install'
include_recipe 'tcs_users::install'
