-- Division Admin
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6', 0, 'division_admin', 'eacsystemadmin@oup.com', 'd41e98d1eafa6d6011d3a70f1a5b92f0', 'Division', 'Admin', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ecd','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec1','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec2','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec3','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec4','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec5','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec6','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec7','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec8','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec9','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e12','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e13','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e14','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e15','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e16','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e17','D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');

insert into customer_roles(role_id, customer_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', 'D87BBAD9-C446-48D1-BDB5-D91F1EC23CD6');

-- Production Controller
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('A2402969-C50D-4DE4-B563-07ACF5FF71F1', 0, 'production_controller', 'eacsystemadmin@oup.com', 'd41e98d1eafa6d6011d3a70f1a5b92f0', 'Production', 'Controller', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ecd','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec1','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec2','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec3','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec4','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec5','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec6','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec7','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec8','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec9','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e12','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e13','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e14','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e15','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e16','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e17','A2402969-C50D-4DE4-B563-07ACF5FF71F1');

insert into customer_roles(role_id, customer_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', 'A2402969-C50D-4DE4-B563-07ACF5FF71F1');

-- Publications Officer
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('CABF4EA6-21F6-45FF-AD2E-1832B6CFC669', 0, 'publication_officer', 'eacsystemadmin@oup.com', 'd41e98d1eafa6d6011d3a70f1a5b92f0', 'Publication', 'Officer', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ecd','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec1','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec2','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec3','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec4','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec5','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec6','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec7','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec8','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec9','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e12','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e13','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e14','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e15','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e16','A2402969-C50D-4DE4-B563-07ACF5FF71F1');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e17','A2402969-C50D-4DE4-B563-07ACF5FF71F1');

insert into customer_roles(role_id, customer_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', 'CABF4EA6-21F6-45FF-AD2E-1832B6CFC669');

-- Field Rep
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('C7F50362-A426-4DE4-A527-AF854A6C63EE', 0, 'field_rep', 'eacsystemadmin@oup.com', 'd41e98d1eafa6d6011d3a70f1a5b92f0', 'Field', 'Rep', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ecd','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec1','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec2','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec3','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec4','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec5','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec6','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec7','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec8','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec9','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e12','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e13','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e14','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e15','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e16','C7F50362-A426-4DE4-A527-AF854A6C63EE');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e17','C7F50362-A426-4DE4-A527-AF854A6C63EE');

insert into customer_roles(role_id, customer_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', 'C7F50362-A426-4DE4-A527-AF854A6C63EE');





