
-- Role / Permissions for kill user session

insert into permission (id, obj_version, name) values ('C93AACB1-BF65-4408-9D30-98492685E27F', 0, 'WS_KILL_USER_SESSION');
GO

insert into permission (id, obj_version, name) values ('4A3F43F6-EE6B-4C9A-8544-511DA235932E', 0, 'KILL_USER_SESSION');
GO

insert into role (id, obj_version, name) values ('17B049CE-2243-40B6-AE43-068F243D53D3', 0, 'KILL_USER_SESSION');
GO

insert into role_permissions (role_id, permission_id) values('17B049CE-2243-40B6-AE43-068F243D53D3', (select id from permission where name='WS_KILL_USER_SESSION'));
GO

insert into role_permissions (role_id, permission_id) values('17B049CE-2243-40B6-AE43-068F243D53D3', (select id from permission where name='KILL_USER_SESSION'));
GO


-- Role / Permissions for Setting user concurrency

insert into permission (id, obj_version, name) values ('07661F45-B135-4AD1-BC3E-5C2B9D2514EE', 0, 'WS_CREATE_USER_WITH_CONCURRENCY');
GO

insert into permission (id, obj_version, name) values ('E979E598-373E-4788-958F-465B0D845D2C', 0, 'WS_UPDATE_USER_WITH_CONCURRENCY');
GO

insert into permission (id, obj_version, name) values ('54230888-05DE-4C61-BE3C-6BCAB1E40350', 0, 'CAN_SET_USER_CONCURRENCY');
GO

insert into role (id, obj_version, name) values ('2290A3D2-DE35-469A-8827-9C6C8F70E119', 0, 'SET_USER_CONCURRENCY');
GO

insert into role_permissions (role_id, permission_id) values('2290A3D2-DE35-469A-8827-9C6C8F70E119', (select id from permission where name='WS_CREATE_USER_WITH_CONCURRENCY'));
GO

insert into role_permissions (role_id, permission_id) values('2290A3D2-DE35-469A-8827-9C6C8F70E119', (select id from permission where name='WS_UPDATE_USER_WITH_CONCURRENCY'));
GO

insert into role_permissions (role_id, permission_id) values('2290A3D2-DE35-469A-8827-9C6C8F70E119', (select id from permission where name='CAN_SET_USER_CONCURRENCY'));
GO