
--- Role and permission for Product Group

insert into permission (id, obj_version, name) values ('D357A0FE-FAF2-4B34-A1FE-DF08B60399BC', 0, 'CREATE_PRODUCT_GROUP');
GO

insert into permission (id, obj_version, name) values ('62555802-D6AC-4E6F-9DE9-5F6A792724C2', 0, 'READ_PRODUCT_GROUP');
GO

insert into permission (id, obj_version, name) values ('356DA152-0463-45C3-AFCC-38281CD90B85', 0, 'UPDATE_PRODUCT_GROUP');
GO

insert into permission (id, obj_version, name) values ('F1D30E89-DFE1-46B5-8226-DF3781DF6903', 0, 'DELETE_PRODUCT_GROUP');
GO

insert into role (id, obj_version, name) values ('B69BE21B-AB2A-4F0A-9FCC-479EF54D1CBB', 0, 'PRODUCT_GROUP');
GO

insert into role_permissions (role_id, permission_id) values('B69BE21B-AB2A-4F0A-9FCC-479EF54D1CBB', (select id from permission where name='CREATE_PRODUCT_GROUP'));
GO

insert into role_permissions (role_id, permission_id) values('B69BE21B-AB2A-4F0A-9FCC-479EF54D1CBB', (select id from permission where name='READ_PRODUCT_GROUP'));
GO

insert into role_permissions (role_id, permission_id) values('B69BE21B-AB2A-4F0A-9FCC-479EF54D1CBB', (select id from permission where name='UPDATE_PRODUCT_GROUP'));
GO

insert into role_permissions (role_id, permission_id) values('B69BE21B-AB2A-4F0A-9FCC-479EF54D1CBB', (select id from permission where name='DELETE_PRODUCT_GROUP'));
GO

insert into permission (id, obj_version, name) values ('D36AD2CE-5739-4E96-8F41-608B3026C0BC', 0, 'CHANGE_ISEDITABLE_PRODUCT_GROUP');
GO

insert into role (id, obj_version, name) values ('4DD5F86A-CC35-4361-B59B-0A925C29AB6F', 0, 'CHANGE_ISEDITABLE_PRODUCT_GROUP');
GO

insert into role_permissions (role_id, permission_id) values('4DD5F86A-CC35-4361-B59B-0A925C29AB6F', (select id from permission where name='CHANGE_ISEDITABLE_PRODUCT_GROUP'));
GO

insert into permission (id, obj_version, name) values ('83C6224A-B7A6-427A-8C56-ACA3212463F0', 0, 'LIST_PRODUCT_GROUP');
GO

insert into role_permissions (role_id, permission_id) values('B69BE21B-AB2A-4F0A-9FCC-479EF54D1CBB', (select id from permission where name='LIST_PRODUCT_GROUP'));
GO
