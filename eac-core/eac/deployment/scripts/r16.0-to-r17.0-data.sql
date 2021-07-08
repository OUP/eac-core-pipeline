
--- Role and permission for Upload Products

insert into permission (id, obj_version, name) values ('3B117C8F-FCF8-49D4-B129-2536D4D7CE10', 0, 'UPLOAD_PRODUCTS');
GO

insert into role (id, obj_version, name) values ('2C13465C-3582-4B4B-B504-B839671751CC', 0, 'UPLOAD_PRODUCTS');
GO

insert into role_permissions (role_id, permission_id) values('2C13465C-3582-4B4B-B504-B839671751CC', (select id from permission where name='UPLOAD_PRODUCTS'));
GO

--- Message for Upload Products

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.upload','Your upload of products has completed, please see the attached zip file for details of the EAC GUIDs.
 If you have any problems with this then please contact the EAC team.');
GO
