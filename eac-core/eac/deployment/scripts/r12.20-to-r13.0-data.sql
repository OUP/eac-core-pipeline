
insert into permission (id, obj_version, name) values ('44BD73B9-5CEA-4350-9A2C-18532F0C8527', 0, 'EXTRACT_LARGER_REPORT');
GO

insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='EXTRACT_LARGER_REPORT'));
GO