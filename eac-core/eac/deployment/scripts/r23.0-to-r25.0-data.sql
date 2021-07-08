--Change reset password template
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.your.password.new','Please click the link below to change your password.', 0);

--Component management role and permission
insert into role(id,obj_version,name) values (newid(),0,'COMPONENT_MANAGER');
insert into permission (id, obj_version, name) values (newId() , 0, 'MODIFY_COMPONENT');
insert into role_permissions (role_id, permission_id) values ((select id from role where name='COMPONENT_MANAGER'),((select id from permission where name='MODIFY_COMPONENT')))

--Platform master role and permission
insert into role(id,obj_version,name) values (newid(),0,'PLATFORM_MASTER');
insert into permission (id, obj_version, name) values (newId() , 0, 'MANAGE_PLATFORM');
insert into role_permissions (role_id, permission_id) values ((select id from role where name='PLATFORM_MASTER'),((select id from permission where name='MANAGE_PLATFORM')))

--Product search label for platform
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'label.platform.code','Platform Code', 0);

--

insert into role(id,obj_version,name) values (newid(),0,'USER_REPORT_MASTER');
insert into permission (id, obj_version, name) values (newId() , 0, 'CREATE_USER_REPORT');
insert into role_permissions (role_id, permission_id) values ((select id from role where name='USER_REPORT_MASTER'),((select id from permission where name='CREATE_USER_REPORT'))) ;

-- insert script for dynamic message
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'delete.confirm.customer','This action can\''''t be un-done.<br/>This action will delete Oxford Id and its related data of this user from EAC.', 0);
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'status.userdetails.submit.success','Your request is scheduled for report generation and report will be uploaded at secure location.', 0);
