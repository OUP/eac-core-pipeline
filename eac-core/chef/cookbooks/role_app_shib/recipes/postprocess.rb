#
# Cookbook:: role_app_shib
# Recipe:: postprocess
#
# Copyright:: 2019, The Authors, All Rights Reserved.
include_recipe 'role_app_shib::configure'
include_recipe 'role_app_shib::service'
include_recipe 'tcs_users::users'
