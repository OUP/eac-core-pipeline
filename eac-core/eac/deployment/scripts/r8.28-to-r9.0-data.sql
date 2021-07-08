insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.report','Please find your report attached.');
GO

insert into permission (id, obj_version, name) values ('5b6414f1-dee2-4aa5-a41e-4228c84df7be', 0, 'ROLE_CREATE_REGISTRATION_REPORT');
GO
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='ROLE_CREATE_REGISTRATION_REPORT'));
GO
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='ROLE_CREATE_REGISTRATION_REPORT'));
GO
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='ROLE_CREATE_REGISTRATION_REPORT'));
GO
-- START Role Based Access Control for Web Services 

insert into permission (id,obj_version,name) values ('97c45d19-1835-4722-8afa-409c36cd1349',0,'WS_AUTHENTICATE');
GO
insert into permission (id,obj_version,name) values ('5e0ab466-4abe-4666-af40-43136606b023',0,'WS_CHANGE_PASSWORD');
GO
insert into permission (id,obj_version,name) values ('6a1427f3-cd34-4575-9f02-6b44a544e63e',0,'WS_CREATE_ACTIVATION_CODE_BATCH');
GO
insert into permission (id,obj_version,name) values ('772ff239-0913-4610-8123-fef6ce575ad0',0,'WS_CREATE_USER_ACCOUNT');
GO

insert into permission (id,obj_version,name) values ('12130c94-7ab4-4e8c-a97f-77514c1d28a2',0,'WS_GET_USER_ENTITLEMENTS');
GO
insert into permission (id,obj_version,name) values ('07fade7b-9291-45c1-b15a-8e980b7760c3',0,'WS_LOGOUT');
GO
insert into permission (id,obj_version,name) values ('af41f46b-42bf-449e-bfc0-c8bfc2114db4',0,'WS_PRODUCT_REGISTRATION');
GO
insert into permission (id,obj_version,name) values ('4bfc9ccf-39a6-4ad5-9321-2c354df22cac',0,'WS_REDEEM_ACTIVATION_CODE');
GO

insert into permission (id,obj_version,name) values ('4346ea1a-8c73-446c-a1c3-08e90198019b',0,'WS_RESET_PASSWORD');
GO
insert into permission (id,obj_version,name) values ('0381b327-df25-4202-aaba-4598a4fd58fe',0,'WS_SEARCH_ACTIVATION_CODE');
GO
insert into permission (id,obj_version,name) values ('43f6a31c-0b1d-4ce4-918b-9c4a44f2c129',0,'WS_SET_EXTERNAL_PRODUCT_IDS');
GO
insert into permission (id,obj_version,name) values ('72ab1d30-13ff-40b5-b652-245ec0cb6bf8',0,'WS_SET_EXTERNAL_USER_IDS');
GO

insert into permission (id,obj_version,name) values ('3ac9986f-3301-4d2c-9e43-cd798c7b1888',0,'WS_UPDATE_USER_ACCOUNT');
GO
insert into permission (id,obj_version,name) values ('3b309cbc-d60c-48ed-ba4a-f3d0ebe6fc23',0,'WS_VALIDATE_ACTIVATION_CODE');
GO
insert into permission (id,obj_version,name) values ('f38abb82-18e2-4ceb-ab4b-ec71059f6b49',0,'WS_VALIDATE_PASSWORD_CREDENTIALS');
GO
insert into permission (id,obj_version,name) values ('4196ed75-6305-41be-83b7-2e48dc9e6287',0,'WS_REGISTRATION_INFORMATION');
GO

insert into permission (id,obj_version,name) values ('1228e2a2-7435-452f-9403-bcb0d333bc89',0,'WS_USER_NAME');
GO

insert into role(id,obj_version,name) values ('d433061f-7541-48bc-9460-e3e30b54f4b1',0,'WS_READONLY');
GO
insert into role(id,obj_version,name) values ('48fb4802-480f-4693-a8dd-034f54940d11',0,'WS_BASIC');
GO
insert into role(id,obj_version,name) values ('279f2044-5232-4a54-af1a-7fb8d52a1b00',0,'WS_READWRITE');
GO
insert into role(id,obj_version,name) values ('586244dd-b5fa-40f4-a060-70a4d3608564',0,'WS_FULL');
GO

BEGIN
declare @ws_permission nvarchar(255)

--1/WS_AUTHENTICATE
set @ws_permission = 'WS_AUTHENTICATE'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),    ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),    ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'),((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),     ((select id from permission where name=@ws_permission)))

--2/WS_CHANGE_PASSWORD
set @ws_permission = 'WS_CHANGE_PASSWORD'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),    ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),    ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'),((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),     ((select id from permission where name=@ws_permission)))
--3/WS_CREATE_ACTIVATION_CODE_BATCH
set @ws_permission = 'WS_CREATE_ACTIVATION_CODE_BATCH'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),   ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),      ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'),  ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),       ((select id from permission where name=@ws_permission)))

--4/WS_CREATE_USER_ACCOUNT
set @ws_permission = 'WS_CREATE_USER_ACCOUNT'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),   ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--5/WS_GET_USER_ENTITLEMENTS
set @ws_permission = 'WS_GET_USER_ENTITLEMENTS'
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--6/WS_LOGOUT
set @ws_permission = 'WS_LOGOUT'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--7/WS_PRODUCT_REGISTRATION
set @ws_permission = 'WS_PRODUCT_REGISTRATION'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--8/WS_REDEEM_ACTIVATION_CODE
set @ws_permission = 'WS_REDEEM_ACTIVATION_CODE'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--9/WS_RESET_PASSWORD
set @ws_permission = 'WS_RESET_PASSWORD'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--10/WS_SEARCH_ACTIVATION_CODE
set @ws_permission = 'WS_SEARCH_ACTIVATION_CODE'
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--11/WS_SET_EXTERNAL_PRODUCT_IDS
set @ws_permission = 'WS_SET_EXTERNAL_PRODUCT_IDS'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--12/WS_SET_EXTERNAL_USER_IDS
set @ws_permission = 'WS_SET_EXTERNAL_USER_IDS'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--13/WS_UPDATE_USER_ACCOUNT
set @ws_permission = 'WS_UPDATE_USER_ACCOUNT'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--14/WS_VALIDATE_ACTIVATION_CODE
set @ws_permission = 'WS_VALIDATE_ACTIVATION_CODE'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--15/WS_VALIDATE_PASSWORD_CREDENTIALS
set @ws_permission = 'WS_VALIDATE_PASSWORD_CREDENTIALS'
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
--insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--16/WS_REGISTRATION_INFORMATION
set @ws_permission = 'WS_REGISTRATION_INFORMATION'
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

--17/WS_USER_NAME
set @ws_permission = 'WS_USER_NAME'
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READONLY'),  ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_BASIC'),     ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_READWRITE'), ((select id from permission where name=@ws_permission)))
insert into role_permissions (role_id, permission_id) values ((select id from role where name='WS_FULL'),      ((select id from permission where name=@ws_permission)))

END
GO

insert into customer_roles(customer_id, role_id)
values ((select id from customer where username='eacuser'), (select id from role where name='WS_READONLY'))
GO

insert into customer_roles(customer_id, role_id)
values ((select id from customer where username='eltdlpuser'), (select id from role where name='WS_READWRITE'))
GO

insert into customer_roles(customer_id, role_id)
values ((select id from customer where username='eltoteuser'), (select id from role where name='WS_FULL'))
GO

insert into customer_roles(customer_id, role_id)
values ((select id from customer where username='admin'), (select id from role where name='WS_FULL'))
GO

IF db_name() = 'eactest'
BEGIN
	--create admin user 'wsbasic' with same password as admin user so we can test 'WS_BASIC' role.
	insert into customer(id,obj_version,username, email_address,first_name,family_name,password,user_type,failed_attempts,locked,reset_password,created_date,enabled)
	values (newId(),0,'wsbasic','eacdevadmin@oup.com','webservice','basic',(select password from customer where username='admin'),'ADMIN',0,0,0,getdate(),1) 
	
	insert into division_admin_user (id,obj_version,admin_user_id,division_id)
	values (newId(),0,(select id from customer where username ='wsbasic'),(select id from division where division_type='MALAYSIA'))
	
	insert into customer_roles(customer_id, role_id)  values ((select id from customer where username='wsbasic'),(select id from role where name='WS_BASIC')) 
END
GO

-- END Role Based Access Control for Web Services 

-- Rename OFCW account registration page (http://mantisoup.idm.fr/view.php?id=15004)
update page_definition set name = 'OFCW Account Registration' where id = '1B037300-852D-45F8-B45F-F02BD0C18C15'
GO


-- Add GAB roles to HUW & set up account for Jerome
BEGIN
-- Huw Ramsay
declare @huw_customer_id nvarchar(255);
set @huw_customer_id = '11b73f60-b3b5-11e1-afa6-0800200c9a66';
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='GAB_ACADEMIC'), @huw_customer_id);


-- Jerome Jones
declare @jerome_customer_id nvarchar(255);
set @jerome_customer_id = '9121D48A-487C-4A1A-A9AD-51483429EFF0';

insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verification_state, reset_password, locked, failed_attempts, user_type, created_date, enabled) 
values (@jerome_customer_id, 0, 'jerome_jones', 'jerome.jones@oup.com', 'c34523428e81159151ec58664c96c513', 'Jerome', 'Jones', 'UNKNOWN', 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), @jerome_customer_id);
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='MALAYSIA'), @jerome_customer_id);
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='GAB_ACADEMIC'), @jerome_customer_id);

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'SYSTEM_ADMIN'), @jerome_customer_id);

END
GO

-- Add confidentiality notice to report email - EAC-243
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.confidentiality1','Confidentiality Notice:');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.confidentiality2','This message is confidential and intended solely for the addressee(s).');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.confidentiality3','You may use and apply the information for the intended purpose only.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.confidentiality4','Any disclosure, copying, distribution or use of this communication to someone other than the intended recipient is prohibited.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.confidentiality5','If this email has come to you in error, please delete it, along with any attachments.');
GO

-- update UAT ORCS admin product home_page
update product set home_page = 'http://global.uat.oup.com/uk/orc/' where id = '985eb4ed-6b7f-45c1-85e0-41059cc930a11' and db_name() = 'eacuat';
GO

-- update ORCS staff product name
update asset set product_name = 'Sales Rep Access to all ORCs' where product_name = 'ORCS Admin Product';
GO
