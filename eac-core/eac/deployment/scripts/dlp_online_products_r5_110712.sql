--to be run against r5 schema in prod

-- Aim High 1 Online practice - fecdd2f9-648f-42bb-acd1-d7e2d02db24a
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','fecdd2f9-648f-42bb-acd1-d7e2d02db24a', 0, 999999, 'http://dummypage', 'Aim High 1 Online practice', (select id from division where division_type='ELT'), null, null, null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'fecdd2f9-648f-42bb-acd1-d7e2d02db24a', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Aim High 2 Online practice - fd480111-030b-4750-83b3-665f7cd01886
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','fd480111-030b-4750-83b3-665f7cd01886', 0, 999999, 'http://dummypage', 'Aim High 2 Online practice', (select id from division where division_type='ELT'), null, null, null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'fd480111-030b-4750-83b3-665f7cd01886', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Aim High 1 Online practice TEACHER - 2866db6f-bf6f-478b-b077-63160fa955f1
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','2866db6f-bf6f-478b-b077-63160fa955f1', 0, 999999, 'http://dummypage', 'Aim High 1 Online practice TEACHER', (select id from division where division_type='ELT'), null, null, null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '2866db6f-bf6f-478b-b077-63160fa955f1', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Aim High 2 Online practice TEACHER - e170e3a7-06b2-4f44-8f00-c8cf40ea9341
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','e170e3a7-06b2-4f44-8f00-c8cf40ea9341', 0, 999999, 'http://dummypage', 'Aim High 2 Online practice TEACHER', (select id from division where division_type='ELT'), null, null, null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'e170e3a7-06b2-4f44-8f00-c8cf40ea9341', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Network 4 Online practice - d7c846aa-ee7a-4b77-a001-3da50a25ed03
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','d7c846aa-ee7a-4b77-a001-3da50a25ed03', 0, 999999, 'http://dummypage', 'Network 4 Online practice', (select id from division where division_type='ELT'), null, null, null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'd7c846aa-ee7a-4b77-a001-3da50a25ed03', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Network 4 Online practice TEACHER - 486b15b5-d32e-41cc-b29f-6f39fd60b2d3
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','486b15b5-d32e-41cc-b29f-6f39fd60b2d3', 0, 999999, 'http://dummypage', 'Network 4 Online practice TEACHER', (select id from division where division_type='ELT'), null, null, null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '486b15b5-d32e-41cc-b29f-6f39fd60b2d3', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Solutions Elementary A1 Online Practice (TR) TEACHER - 6ced6078-671c-48b5-95ef-123497b32e4b
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','6ced6078-671c-48b5-95ef-123497b32e4b', 0, 999999, 'http://dummypage', 'Solutions Elementary A1 Online Practice (TR) TEACHER', (select id from division where division_type='ELT'), null, null, null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '6ced6078-671c-48b5-95ef-123497b32e4b', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Solutions Pre-Intermediate A2 Online Practice (TR) TEACHER - 96a9d126-1aba-4e9a-8b9e-2634cac70ed6
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','96a9d126-1aba-4e9a-8b9e-2634cac70ed6', 0, 999999, 'http://dummypage', 'Solutions Pre-Intermediate A2 Online Practice (TR) TEACHER', (select id from division where division_type='ELT'), null, null, null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '96a9d126-1aba-4e9a-8b9e-2634cac70ed6', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
