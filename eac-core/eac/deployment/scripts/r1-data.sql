
-- Clear database
delete from activation_code;
delete from activation_code_batch;
delete from answer;
delete from component;
delete from customer_activation_code;
delete from customer_organisation;
delete from division;
delete from division_admin_user;
delete from external_identifier;
delete from organisation;
delete from registration;
delete from customer;
delete from product;
delete from tag_option;
delete from component;
delete from tag;
delete from element;
delete from page_component;
delete from page_definition;
delete from licence_template;
delete from registration_activation;
delete from registration_definition;


insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ecc', 0, 'admin', 'eacsystemadmin@oup.com', 'd41e98d1eafa6d6011d3a70f1a5b92f0', 'Super', 'User', 1, 0, 0, 0, 'ADMIN');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 0, 'MALAYSIA');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ec1', 0, 'MEXICO');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ec2', 0, 'ANZ');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ec3', 0, 'CANADA');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ec4', 0, 'TANZANIA');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ec5', 0, 'CHINA');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ec6', 0, 'KENYA');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ec7', 0, 'INDIA');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ec8', 0, 'PAKISTAN');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25ec9', 0, 'SOUTH_AFRICA');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25e12', 0, 'ELT');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25e13', 0, 'OXED');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25e14', 0, 'SPAIN');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25e15', 0, 'GAB_USA');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25e16', 0, 'GAB_ACADEMIC');
insert into division(id, obj_version,division_type) values('bfdc5888-c26b-4ed5-a580-6aa363a25e17', 0, 'GAB_JOURNALS');

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ecd','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec1','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec2','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec3','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec4','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec5','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec6','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec7','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec8','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25ec9','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e12','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e13','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e14','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e15','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e16','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,'bfdc5888-c26b-4ed5-a580-6aa363a25e17','bfdc5888-c26b-4ed5-a580-6aa363a25ecc');

-- License template definition

insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) values ('552C200A-7FC4-42E6-91FD-730720D02895', 0, 'CONCURRENT', null, null, 1, 1, null, null);

-- Product definition

insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','E6CC13C4-1D79-4FC6-B0AC-19C2A767B2CB', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes');

insert into registration_activation(activation_type,id,obj_version,validator_email) values ('SELF','119452A7-07BB-4B13-A334-BF44D04EAF57',0,null);
insert into registration_activation(activation_type,id,obj_version,validator_email) values ('INSTANT','D7FEFBDF-94D9-4D15-9579-4EF312E4054E',0,null);

-- Page definition

-- ACCOUNT REG PHASE 1 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('1B037300-852D-45F8-B45F-F02BD0C18C15', 0, '','title.accountregistration', 'ACCOUNT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');
-- PRODUCT REG PHASE 1 (SCHOOLS STUDENT & HE STUDENT EDITIONS)
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('27B8A1E0-1504-4763-A70D-B74DC2F6D568', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');

-- Registration Definition

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, 'E6CC13C4-1D79-4FC6-B0AC-19C2A767B2CB', '119452A7-07BB-4B13-A334-BF44D04EAF57', '27B8A1E0-1504-4763-A70D-B74DC2F6D568', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, 'E6CC13C4-1D79-4FC6-B0AC-19C2A767B2CB', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', '552C200A-7FC4-42E6-91FD-730720D02895');

-- Components

-- ACCOUNT REG
-- MARKETING
insert into component (id,obj_version,label_key) values ('87782783-21DE-4301-B208-43DF5F62CAAA', 0, 'label.registration.marketing.header');
-- TERMS AND CONDITIONS
insert into component (id,obj_version,label_key) values ('328D4DCF-E39B-4BC8-9900-92EEBB82D30F', 0, 'label.registration.tandc.header');

--PRODUCT REG

-- PERSONAL DETAILS
insert into component (id,obj_version,label_key) values ('0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, 'label.registration.customer.header');
-- ADDRESS
insert into component (id,obj_version,label_key) values ('21054566-EF39-4E7A-ACA0-F544EA860B6C', 0, 'label.registration.address.header');
-- SCHOOL DETAILS
insert into component (id,obj_version,label_key) values ('D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 0, 'label.registration.school.details');
-- EDUCATION
insert into component (id,obj_version,label_key) values ('9921178D-2B92-4696-8215-CB8F3BAD2E97', 0, 'label.registration.education.header');
-- MARKETING
insert into component (id,obj_version,label_key) values ('88957E05-9AFE-42D2-942F-4A85B96833CD', 0, 'label.registration.marketing.header');
-- TERMS AND CONDITIONS
insert into component (id,obj_version,label_key) values ('3804D5C4-3AAB-4BD8-9E00-15CFB6A3B53F', 0, 'label.registration.tandc.header');

-- Page Components

-- ***************** PHASE 1 STUDENT ***********************

-- ACCOUNT REG 

-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 0, '1B037300-852D-45F8-B45F-F02BD0C18C15');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '328D4DCF-E39B-4BC8-9900-92EEBB82D30F', 1, '1B037300-852D-45F8-B45F-F02BD0C18C15');

-- PRODUCT REG PHASE 1

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, '27B8A1E0-1504-4763-A70D-B74DC2F6D568');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, '27B8A1E0-1504-4763-A70D-B74DC2F6D568');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, '27B8A1E0-1504-4763-A70D-B74DC2F6D568');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, '27B8A1E0-1504-4763-A70D-B74DC2F6D568');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '3804D5C4-3AAB-4BD8-9E00-15CFB6A3B53F', 5, '27B8A1E0-1504-4763-A70D-B74DC2F6D568');



-- Elements, Tags, Options, Components

insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('29ACE041-16F4-4490-B4DF-116618549078', 0, 'label.registration.tandc.link', 'title.registration.tandc.link', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url, new_window) values ('2488883A-D577-4D45-8374-401AE5688CAF', 0, 'URLLINK', '', 0, '29ACE041-16F4-4490-B4DF-116618549078', 'privacyAndLegal.htm', 1);
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values ('38514418-BD3B-4448-8869-85BBDD884B29', 0, 1, '29ACE041-16F4-4490-B4DF-116618549078', '328D4DCF-E39B-4BC8-9900-92EEBB82D30F', '', 0);

--GENDER
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('70A68F87-2AD3-4EDE-BE46-D7F9318C3403', 0, 'label.registration.customer.sex', 'title.registration.customer.sex', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('bac87391-23b1-11e0-844a-a4badbe688c6', 0, 'SELECT', '', 0, '70A68F87-2AD3-4EDE-BE46-D7F9318C3403', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.sex.male', 1, 'M', 'bac87391-23b1-11e0-844a-a4badbe688c6');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.sex.female', 2, 'F', 'bac87391-23b1-11e0-844a-a4badbe688c6');
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values ('E41A7599-362B-4C1E-B0E1-DD7E5EE679A6', 0, 0, '70A68F87-2AD3-4EDE-BE46-D7F9318C3403', '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 'M','sex', 0);

--DATE OF BIRTH
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('1B037300-852D-45F8-B45F-F02BD0C18C15', 0, 'label.registration.customer.dob', 'title.registration.customer.dob', 'help.dob', '^(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\d\d$');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '1B037300-852D-45F8-B45F-F02BD0C18C15', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '1B037300-852D-45F8-B45F-F02BD0C18C15', '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 'dob', 0);

--ADDRESS LINE 1
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('CA8CA2BF-14FB-4487-9A9E-36C67652F241', 0, 'label.registration.address.line1', 'title.registration.address.line1', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'CA8CA2BF-14FB-4487-9A9E-36C67652F241', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 2, 'CA8CA2BF-14FB-4487-9A9E-36C67652F241', '21054566-EF39-4E7A-ACA0-F544EA860B6C', 'address.line1', 0);

--ADDRESS LINE 2
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('5ABB0AA1-9A38-44E5-8467-69B8FC253A1C', 0, 'label.registration.address.line2', 'title.registration.address.line2', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '5ABB0AA1-9A38-44E5-8467-69B8FC253A1C', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 3, '5ABB0AA1-9A38-44E5-8467-69B8FC253A1C', '21054566-EF39-4E7A-ACA0-F544EA860B6C', 'address.line2', 0);

--CITY/TOWN
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('AA094315-E195-4952-8763-53CBA7CC1C8F', 0, 'label.registration.address.line3', 'title.registration.address.line3', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'AA094315-E195-4952-8763-53CBA7CC1C8F', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 4, 'AA094315-E195-4952-8763-53CBA7CC1C8F', '21054566-EF39-4E7A-ACA0-F544EA860B6C', 'address.line3', 0);

--POSTCODE
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('CFE611D0-71EE-415E-98DC-35838B786830', 0, 'label.registration.address.line4', 'title.registration.address.line4', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'CFE611D0-71EE-415E-98DC-35838B786830', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 5, 'CFE611D0-71EE-415E-98DC-35838B786830', '21054566-EF39-4E7A-ACA0-F544EA860B6C', 'address.line4', 0);

--STATE
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('25B2B382-9228-4B37-8E10-5530C97B050C', 0, 'label.registration.address.line5', 'title.registration.address.line5', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '25B2B382-9228-4B37-8E10-5530C97B050C', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 6, '25B2B382-9228-4B37-8E10-5530C97B050C', '21054566-EF39-4E7A-ACA0-F544EA860B6C', 'address.line5', 0);

--COUNTRY
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('55B49191-65F9-4D9B-BF59-9E78E8F9580F', 0, 'label.registration.address.line6', 'title.registration.address.line6', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D', 0, 'SELECT', '', 0, '55B49191-65F9-4D9B-BF59-9E78E8F9580F', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.my', 1, 'my', '4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sg', 2, 'sg', '4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.id', 3, 'id', '4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bn', 4, 'bn', '4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ph', 5, 'ph', '4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.th', 6, 'th', '4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vn', 7, 'vn', '4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.other', 8, 'other', '4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D');
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 7, '55B49191-65F9-4D9B-BF59-9E78E8F9580F', '21054566-EF39-4E7A-ACA0-F544EA860B6C', 'my', 'address.line6', 1);


--POSITION - STUDENT/TEACHER-LECTURER
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('8EFCF8EE-36B9-4B45-820A-2F61ADAAC225', 0, 'label.registration.education.position', 'title.registration.education.position', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('B0AE87E6-40E4-4ED0-9B73-FC29894AE3BA', 0, 'SELECT', '', 0, '8EFCF8EE-36B9-4B45-820A-2F61ADAAC225', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.position.student', 1, 'Student', 'B0AE87E6-40E4-4ED0-9B73-FC29894AE3BA');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.position.teacher', 2, 'Teacher', 'B0AE87E6-40E4-4ED0-9B73-FC29894AE3BA');
-- PHASE 1 STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '8EFCF8EE-36B9-4B45-820A-2F61ADAAC225', '9921178D-2B92-4696-8215-CB8F3BAD2E97', 'Student', 'position', 1);


--INSTITUTION TYPE
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('025A165B-CDBD-4B2C-8AAE-5097793FD5DE', 0, 'label.registration.education.institution', 'title.registration.education.institution', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('025A165B-CDBD-4B2C-8AAE-5097793FD5DE', 0, 'SELECT', '', 0, '025A165B-CDBD-4B2C-8AAE-5097793FD5DE', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.national.primary', 1, 'national primary', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.national.type.primary', 2, 'national type primary', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.private.primary', 3, 'private primary', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.national.secondary', 4, 'national secondary', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.private.secondary', 5, 'private secondary', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.form6', 6, 'Form6/Pre-U', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.matriculation.centre', 7, 'matriculation centre', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.polytechnic', 8, 'polytechnic', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.private.college', 9, 'private college', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.private.language.school', 10, 'private language school', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.public.university', 11, 'public university', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.private.university', 12, 'private university', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.institution.other', 13, 'other', '025A165B-CDBD-4B2C-8AAE-5097793FD5DE');
-- PHASE 1 STUDENT 
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 1, '025A165B-CDBD-4B2C-8AAE-5097793FD5DE', '9921178D-2B92-4696-8215-CB8F3BAD2E97', 'national primary', 'instituition', 1);
 

--INSTITUTION NAME
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('9ae814a7-2eeb-11e0-be2b-a4badbe688c6', 0, 'label.registration.education.institution.name', 'title.registration.education.institution.name', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '9ae814a7-2eeb-11e0-be2b-a4badbe688c6', '');
-- PHASE 1 STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 2, '9ae814a7-2eeb-11e0-be2b-a4badbe688c6', '9921178D-2B92-4696-8215-CB8F3BAD2E97', 'institution.name', 0); 

--FACULTY/DEPARTMENT
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('E14668C5-20B8-442D-AF86-A47A3F8688FE', 0, 'label.registration.education.faculty.department', 'title.registration.education.faculty.department', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'E14668C5-20B8-442D-AF86-A47A3F8688FE', '');

--MARKETING OPT IN-OUT
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('068fe4e6-2d28-11e0-be2b-a4badbe688c6', 0, 'label.registration.marketing', 'title.registration.marketing', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('35366c80-2d2d-11e0-be2b-a4badbe688c6', 0, 'RADIO', '', 0, '068fe4e6-2d28-11e0-be2b-a4badbe688c6', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.marketing.optin', 1, 'opt in', '35366c80-2d2d-11e0-be2b-a4badbe688c6');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.marketing.optout', 2, 'opt out', '35366c80-2d2d-11e0-be2b-a4badbe688c6');
-- PHASE 1 STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, '068fe4e6-2d28-11e0-be2b-a4badbe688c6', '87782783-21DE-4301-B208-43DF5F62CAAA', 'marketing.pref', 1);

--ACCOUNT ACCEPT TERMS AND CONDITIONS
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('FD84EC92-5401-4C32-8A01-7FD2A3F1BEC7', 0, 'label.registration.tandc', 'title.registration.tandc', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('C5138478-33CA-4B96-9202-41F6B8528801', 0, 'CHECKBOX', '', 0, 'FD84EC92-5401-4C32-8A01-7FD2A3F1BEC7', '');
-- PHASE 1 STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'FD84EC92-5401-4C32-8A01-7FD2A3F1BEC7', '328D4DCF-E39B-4BC8-9900-92EEBB82D30F', 'account.terms.accepted', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('D61AA669-B769-44DC-9248-859ED232B856', 0, 'label.registration.my.cw.tandc', 'title.registration.my.cw.tandc', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('24937553-DB2C-4FEA-97DA-ED14F06385EB', 0, 'CHECKBOX', '', 0, 'D61AA669-B769-44DC-9248-859ED232B856', '');
-- PHASE 1 STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '3804D5C4-3AAB-4BD8-9E00-15CFB6A3B53F', 'malaysia.cw.terms.accepted', 1);


--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 0, 'label.registration.my.cw.tandc.link', 'title.registration.my.cw.tandc.link', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url, new_window) values ('2D47D941-C3BE-45D7-8408-C5C20210A147', 0, 'URLLINK', '', 0, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 'http://cw.oxfordfajar.com.my/termsandconditions.aspx', 1);
-- PHASE 1 STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '3804D5C4-3AAB-4BD8-9E00-15CFB6A3B53F', '', 0);


