
-- DLP product external identifiers
insert into external_system (id, obj_version, name, description) values ('7D79DEA9-64CD-476F-8942-6F82E1C7D9E7', 0, 'DLP', 'ELT Digital Learning Platform');
insert into external_system_id_type (id, obj_version, name, description, external_system_id) values ('A721E11F-6540-486D-9525-537B75C5D669', 0, 'ISBN-13', 'ISBN 13', '7D79DEA9-64CD-476F-8942-6F82E1C7D9E7');

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'A721E11F-6540-486D-9525-537B75C5D669', '9780194739733', 'PRODUCT', null, '1F031C45-796F-47EB-9578-CDC38A50C0D6');
insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'A721E11F-6540-486D-9525-537B75C5D669', '9780194739740', 'PRODUCT', null, '76620B77-C4EB-4372-B8BA-AA71E4916DA3');
insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'A721E11F-6540-486D-9525-537B75C5D669', '9780194739757', 'PRODUCT', null, '497B34B1-6FEA-4A67-A9ED-F67C26F1FD98');
insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'A721E11F-6540-486D-9525-537B75C5D669', '9780194739764', 'PRODUCT', null, '07807968-C708-498E-8440-B7C9C28496DC');
insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'A721E11F-6540-486D-9525-537B75C5D669', '9780194739771', 'PRODUCT', null, 'B8DD67DA-9C3A-47FD-A9F9-09CD442F2D30');

-- DLP Admin user
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, updated_date) values('42B4060C-69F5-41F4-B8C7-B1850D7D4969', 0, 'eltdlpuser', 'eacsystemadmin@oup.com', 'fbc7ec314020250bc8c04be8ed85de27', 'ELT-DLP-Admin', 'User', 1, 0, 0, 0, 'ADMIN', getdate(), getdate());
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0,(select id from division where division_type='ELT'),'42B4060C-69F5-41F4-B8C7-B1850D7D4969');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.customer.does.not.exist','Erights was unable to find this user.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registrationnotallowed','Registration not allowed');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registrationnotallowed','If you would like to have access to {0} please contact your divisional OUP sales rep.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.licenceisdeniedcircumstance','If your circumstances have changed and you believe you now meet our criteria for accessing the site, please complete a new');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.licenceisdeniedcircumstance2','registration form');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.licenceisdeniedcircumstance3','and we will reconsider your request for access.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.concurrencyexceeded','Concurrency Exceeded');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.concurrencyexceeded1','We''''re sorry. You have reached the maximum number of users who are permitted to access these resources simultaneously using this account. Please try again later.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.concurrencyexceeded2','If you are still blocked from using these resources after several attempts, please contact your Administrator and request that they pay to increase the number of permitted users. If you do not know your Administrator, please contact us directly at {0}.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.concurrencyexceeded3','Remember: please log out after you have finished using this web site so your user licence can be freed-up for the next person wishing to access the web site using this account.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.noactivelicence','Licence expired');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.noactivelicence','We''''re sorry. You no longer have access to these resources. If you wish to continue to use this web site you will need to');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.noactivelicence2','renew your access.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.noactivelicence3','In case of difficulty please contact us at {0}');

update message set message = 'Thank you for requesting access to {0}. Unfortunately your request has been denied. If you require further information about why your application has been denied please contact {1}.' where basename = 'messages' and [key] = 'label.licenceisdenied';


-- Employee OFCW validated registration for all OFCW products

-- License template definition
insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) 
values ('3C3EA07E-2304-4171-9948-CC7DA9EBA88E', 0, 'CONCURRENT', null, null, 1, 1, null, null);

-- Product reg page definition
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) 
values ('874ED74E-B97B-41A5-B5A5-E051F112BBB0', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');

-- Employee info
insert into component (id,obj_version,label_key) values ('5B3DBC40-F988-4163-BC8B-E12D39AF8794', 0, 'label.registration.employee.info');

-- Employee Id
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('6E8D9D5E-F320-497A-9121-CDF3F52B0907', 0, 'label.employee.id', 'title.employee.id', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '6E8D9D5E-F320-497A-9121-CDF3F52B0907', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) 
values (newId(), 0, 1, '6E8D9D5E-F320-497A-9121-CDF3F52B0907', '5B3DBC40-F988-4163-BC8B-E12D39AF8794', 'employee.id', 1);

--Job title
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('6E08E917-0291-4818-88C9-4B54C2C79B6B', 0, 'label.job.title', 'title.job.title', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '6E08E917-0291-4818-88C9-4B54C2C79B6B', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) 
values (newId(), 0, 2, '6E08E917-0291-4818-88C9-4B54C2C79B6B', '5B3DBC40-F988-4163-BC8B-E12D39AF8794', 'employee.jobtitle', 1);

--Department
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('D321DEAA-201B-44FA-B678-5D6985F73C0F', 0, 'label.department', 'title.department', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'D321DEAA-201B-44FA-B678-5D6985F73C0F', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) 
values (newId(), 0, 0, 'D321DEAA-201B-44FA-B678-5D6985F73C0F', '5B3DBC40-F988-4163-BC8B-E12D39AF8794', 'employee.department', 1);

-- Info
insert into component (id,obj_version,label_key) values ('19D05445-7667-434D-97DC-5AB260ECF5D2', 0, 'label.important');
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('F8CCD575-47A0-4833-BFC8-222038FE2B4A', 0, 'label.employee.important.info', 'title.productinfo', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'LABEL', '', 0, 'F8CCD575-47A0-4833-BFC8-222038FE2B4A', '');
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) 
values (newId(), 0, 0, 'F8CCD575-47A0-4833-BFC8-222038FE2B4A', '19D05445-7667-434D-97DC-5AB260ECF5D2', '', 0);

-- Terms and conditions
insert into component (id,obj_version,label_key) values ('046A7718-0D2C-4827-894A-03CBEBF00A19', 0, 'label.registration.tandc.header');
--Accept check box
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) 
values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '046A7718-0D2C-4827-894A-03CBEBF00A19', 'employee.malaysia.cw.terms.accepted', 1);
-- Link
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) 
values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '046A7718-0D2C-4827-894A-03CBEBF00A19', '', 0);

-- Page components
-- IMPORTANT INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '19D05445-7667-434D-97DC-5AB260ECF5D2', 0, '874ED74E-B97B-41A5-B5A5-E051F112BBB0');
-- EMPLOYEE INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '5B3DBC40-F988-4163-BC8B-E12D39AF8794', 1, '874ED74E-B97B-41A5-B5A5-E051F112BBB0');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '046A7718-0D2C-4827-894A-03CBEBF00A19', 2, '874ED74E-B97B-41A5-B5A5-E051F112BBB0');

-- Product
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) 
values ('REGISTERABLE','9AE8CE9A-2495-48EA-898C-4BD8CAA8E291', 0, 52218, '', 'OFCW Admin Product', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes');
--Linked all content
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) 
values ('LINKED', '7D8DEFE7-04AC-469F-99F7-86BB2EF7958F', 0, 52217, '', 'OFCW Unlimited Access', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '9AE8CE9A-2495-48EA-898C-4BD8CAA8E291', 'cw.info@oxfordfajar.com.my', '30 minutes', 'POST_PARENT');

-- Registration validated activation as per HE titles

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('PRODUCT_REGISTRATION', newId(), 0, '9AE8CE9A-2495-48EA-898C-4BD8CAA8E291', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', '874ED74E-B97B-41A5-B5A5-E051F112BBB0', '3C3EA07E-2304-4171-9948-CC7DA9EBA88E');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, '9AE8CE9A-2495-48EA-898C-4BD8CAA8E291', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);

-- New message strings
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.employee.info','Employee Details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.employee.id','Employee Id');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.employee.id','Employee Id');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.title','Job title');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.job.title','Job title');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.department','Department');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.department','Department');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.employee.important.info','Only employees may register for access to this content. Registration details will be verified before access is granted. Please allow one week for verification. This Employee Account will give access to all employee resources.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'header.logo.mid','Oxford University Press Enterprise Access Control - Mid');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'header.logo.right','Oxford University Press Enterprise Access Control - Right');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.session.token.error','Establish Eac Cookie : Error');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.session.token.error','Error establishing Eac Cookie from Session Token');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'session.token.error.invalid.session','The sessionToken is invalid.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'session.token.error.decrypt.problem','Problem decrypting the sessionToken.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'session.token.error.url.blank','The successUrl cannot be blank.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'session.token.error.url.invalid','The successUrl is invalid.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'session.token.error.token.blank','The sessionToken cannot be blank.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'tile.basic.login.error', 'Basic Login : Error');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.basic.login.error','Error during Basic Login');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'basic.login.error.user.name.blank','The username cannot be blank.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'basic.login.error.password.blank','The password cannot be blank.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'basic.login.error.success.url.invalid','The success_url is invalid.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'basic.login.error.login.failed','There was a problem with your login credentials.');

-- Updated message strings
update message set message = 'Your registration for {0} is now being processed.' where [key] = 'label.awaitinglicenceactivation';
update message set message = 'Access will be confirmed within a week from date of registration, and you will be notified via email. If you do not receive the notification email within a week, please check for it in your spam or junk mail folder. If the email is not there please contact <a href="mailto:{1}">{1}</a>.' where [key] = 'label.awaitinglicenceactivationconfirm';

-- welcome message for login widget 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.welcome','Welcome');

-- spanish messages for login widget - not required but handy for testing localisation
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.welcome','Bienvenida');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.username','Nombre de usuario');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.password','Contrase\u00F1a');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.login','Iniciar sesi\u00F3n');

-- insert logout messages for login widget
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.logout','Logout');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.logout','Desconectarse');

