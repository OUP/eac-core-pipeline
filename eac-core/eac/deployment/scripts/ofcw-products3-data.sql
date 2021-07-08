-- Bare shells of OFCW HE Student Products to generate tokens, pages not specified

--HE STUDENT Essentials of Managerial Economics
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','E2651583-3023-4FA5-85A3-A2A7F6AB9032', 0, 9999, 'http://dummypage', 'Essentials of Managerial Economics', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dummypage');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'E2651583-3023-4FA5-85A3-A2A7F6AB9032', '119452A7-07BB-4B13-A334-BF44D04EAF57', null, '552C200A-7FC4-42E6-91FD-730720D02895');

--HE STUDENT Business Research Methods
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','B9BE0355-9F57-4142-8BA0-2084872569F4', 0, 9999, 'http://dummypage', 'Business Research Methods', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dummypage');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'B9BE0355-9F57-4142-8BA0-2084872569F4', '119452A7-07BB-4B13-A334-BF44D04EAF57', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- HE STUDENT Principles of Economics 2e
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','156781E9-CB55-4E59-93D6-8C1BA542E279', 0, 9999, 'http://dummypage', 'Principles of Economics 2e', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dummypage');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '156781E9-CB55-4E59-93D6-8C1BA542E279', '119452A7-07BB-4B13-A334-BF44D04EAF57', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- clean up for testing
--delete from registration_definition where product_id in ('E2651583-3023-4FA5-85A3-A2A7F6AB9032', 'B9BE0355-9F57-4142-8BA0-2084872569F4', '156781E9-CB55-4E59-93D6-8C1BA542E279');
--delete from product where id in ('E2651583-3023-4FA5-85A3-A2A7F6AB9032', 'B9BE0355-9F57-4142-8BA0-2084872569F4', '156781E9-CB55-4E59-93D6-8C1BA542E279');
