-- Huw Ramsay
declare @huw_customer_id nvarchar(255);
set @huw_customer_id = '11b73f60-b3b5-11e1-afa6-0800200c9a66';

insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) 
values (@huw_customer_id, 0, 'huw_ramsay', 'huw.ramsay@oup.com', 'a92b5e4023936c278394398cda12d64f', 'Huw', 'Ramsay', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), @huw_customer_id);
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='MALAYSIA'), @huw_customer_id);

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'SYSTEM_ADMIN'), @huw_customer_id);
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'DIVISION_ADMIN'), @huw_customer_id);
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), @huw_customer_id);
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PUBLICATIONS_OFFICER'), @huw_customer_id);
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), @huw_customer_id);
