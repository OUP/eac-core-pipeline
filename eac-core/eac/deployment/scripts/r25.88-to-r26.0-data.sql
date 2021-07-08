--Trusted System role and permission
insert into role(id,obj_version,name) values (newid(),0,'TRUSTED_SYSTEM_MASTER');
insert into permission (id, obj_version, name) values (newId() , 0, 'MANAGE_TRUSTED_SYSTEM');
insert into role_permissions (role_id, permission_id) values ((select id from role where name='TRUSTED_SYSTEM_MASTER'),((select id from permission where name='MANAGE_TRUSTED_SYSTEM')));
--Internal support role and permission
insert into role(id,obj_version,name) values (newid(),0,'INTERNAL_SUPPORT');
insert into permission (id, obj_version, name) values (newId() , 0, 'MANAGE_INTERNAL_SUPPORT');
insert into role_permissions (role_id, permission_id) values ((select id from role where name='INTERNAL_SUPPORT'),((select id from permission where name='MANAGE_INTERNAL_SUPPORT')));

insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'msg.trusted.user.password.encrypt','Please note down your encrypted password and this will not be displayed 2nd time. It will be required while integrating with EAC system.', 0);