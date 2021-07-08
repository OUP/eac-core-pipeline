insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.changing.customer.password.no.email','There was a problem changing your password. Please contact the system adminsitrator.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.customer.newpassword.same.as.oldpassword','Your new password can not be the same as your old password. Please try another.');
update message set message = 'Our records show that access to this web site has already been activated successfully. Please contact {0} if there are any problems using the web site' where [key] = 'label.licencealreadyactive' and basename = 'messages';

update registration set updated_date = created_date;

--create a seperate 'admin user' for MALAYSIA
delete from customer where username='eacuser';
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, updated_date) values('4259c882-aa57-45a7-9bee-0de4d85d8d9d', 0, 'eacuser', 'eacsystemadmin@oup.com', 'fbc7ec314020250bc8c04be8ed85de27', 'Malaysian-Admin', 'User', 1, 0, 0, 0, 'ADMIN', getdate(), getdate());
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values ('33D74189-F59F-4932-9321-6903CF9243D0',0,(select id from division D where division_type='MALAYSIA'),'4259c882-aa57-45a7-9bee-0de4d85d8d9d');

--ELT Products
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','1F031C45-796F-47EB-9578-CDC38A50C0D6', 0, 32502, '', 'Business Result Elementary SB online practice', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', null, null, null);
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','76620B77-C4EB-4372-B8BA-AA71E4916DA3', 0, 32503, '', 'Business Result Pre-Intermediate SB online practice', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', null, null, null);
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','497B34B1-6FEA-4A67-A9ED-F67C26F1FD98', 0, 32504, '', 'Business Result Intermediate SB online practice', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', null, null, null);
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','07807968-C708-498E-8440-B7C9C28496DC', 0, 32505, '', 'Business Result Upper-Intermediate SB online practice', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', null, null, null);
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','B8DD67DA-9C3A-47FD-A9F9-09CD442F2D30', 0, 32506, '', 'Business Result Advanced SB online practice', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', null, null, null);

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', '36FFFECD-3DB6-4016-9360-D4E972536439', 0, '1F031C45-796F-47EB-9578-CDC38A50C0D6', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '1F031C45-796F-47EB-9578-CDC38A50C0D6', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', 'C5F839CB-CF70-4406-9BA5-78822A45603C', 0, '76620B77-C4EB-4372-B8BA-AA71E4916DA3', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '76620B77-C4EB-4372-B8BA-AA71E4916DA3', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', '74F32FFE-3A35-44D4-B4F1-2CB4CF16AB58', 0, '497B34B1-6FEA-4A67-A9ED-F67C26F1FD98', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '497B34B1-6FEA-4A67-A9ED-F67C26F1FD98', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', 'E2F4FE41-CDD7-456C-8E99-62A702EBE3D5', 0, '07807968-C708-498E-8440-B7C9C28496DC', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '07807968-C708-498E-8440-B7C9C28496DC', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', '3D449AFB-D2C6-469A-A363-7BEEEC61CF49', 0, 'B8DD67DA-9C3A-47FD-A9F9-09CD442F2D30', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, 'B8DD67DA-9C3A-47FD-A9F9-09CD442F2D30', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);

