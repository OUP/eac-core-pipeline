--to be run against r6 schema pre 6.1

-- Aim High 1 Online practice - fecdd2f9-648f-42bb-acd1-d7e2d02db24a learner
insert into asset(id,obj_version,erights_id, product_name, division_id) values('38EA3A8E-B7CB-4716-BF64-0FAA024DDAC1', 0, 990001, 'Aim High 1 Online practice', (select id from division where division_type='ELT'));
insert into product (product_type, id ,obj_version, asset_id, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','fecdd2f9-648f-42bb-acd1-d7e2d02db24a', 0, '38EA3A8E-B7CB-4716-BF64-0FAA024DDAC1', '', null, '', '', 'SELF_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'fecdd2f9-648f-42bb-acd1-d7e2d02db24a', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Aim High 2 Online practice - fd480111-030b-4750-83b3-665f7cd01886 learner
insert into asset(id,obj_version,erights_id, product_name, division_id) values('968235F7-3472-45E9-9820-4B21D662D333', 0, 990002, 'Aim High 2 Online practice', (select id from division where division_type='ELT'));
insert into product (product_type, id ,obj_version, asset_id, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','fd480111-030b-4750-83b3-665f7cd01886', 0, '968235F7-3472-45E9-9820-4B21D662D333', '', null, '', '', 'SELF_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'fd480111-030b-4750-83b3-665f7cd01886', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Aim High 1 Online practice TEACHER - 2866db6f-bf6f-478b-b077-63160fa955f1 instructor
insert into asset(id,obj_version,erights_id, product_name, division_id) values('C29A6A4D-8ED1-43C8-9852-C7E38B9DFC2F', 0, 990003, 'Aim High 1 Online practice TEACHER', (select id from division where division_type='ELT'));
insert into product (product_type, id ,obj_version, asset_id, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','2866db6f-bf6f-478b-b077-63160fa955f1', 0, 'C29A6A4D-8ED1-43C8-9852-C7E38B9DFC2F', '', null, '', '', 'ADMIN_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '2866db6f-bf6f-478b-b077-63160fa955f1', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Aim High 2 Online practice TEACHER - e170e3a7-06b2-4f44-8f00-c8cf40ea9341 instructor
insert into asset(id,obj_version,erights_id, product_name, division_id) values('CCEE26B3-B922-492D-959B-533803C8715E', 0, 990004, 'Aim High 2 Online practice TEACHER', (select id from division where division_type='ELT'));
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','e170e3a7-06b2-4f44-8f00-c8cf40ea9341', 0, 'CCEE26B3-B922-492D-959B-533803C8715E', '', null, '', '', 'ADMIN_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, 'e170e3a7-06b2-4f44-8f00-c8cf40ea9341', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Network 4 Online practice - d7c846aa-ee7a-4b77-a001-3da50a25ed03 learner
insert into asset(id,obj_version,erights_id, product_name, division_id) values('4E0716E5-715A-4B2F-923B-6B30186178AD', 0, 990005, 'Network 4 Online practice', (select id from division where division_type='ELT'));
insert into product (product_type, id ,obj_version, asset_id, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','d7c846aa-ee7a-4b77-a001-3da50a25ed03', 0, '4E0716E5-715A-4B2F-923B-6B30186178AD', '', null, '', '', 'SELF_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'd7c846aa-ee7a-4b77-a001-3da50a25ed03', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Network 4 Online practice TEACHER - 486b15b5-d32e-41cc-b29f-6f39fd60b2d3 instructor
insert into asset(id,obj_version,erights_id, product_name, division_id) values('446B3A73-7A04-4018-B14E-99186C4C45BF', 0, 990006, 'Network 4 Online practice TEACHER', (select id from division where division_type='ELT'));
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','486b15b5-d32e-41cc-b29f-6f39fd60b2d3', 0, '446B3A73-7A04-4018-B14E-99186C4C45BF', '', null, '', '', 'ADMIN_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '486b15b5-d32e-41cc-b29f-6f39fd60b2d3', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Solutions Elementary A1 Online Practice (TR) TEACHER - 6ced6078-671c-48b5-95ef-123497b32e4b instructor
insert into asset(id,obj_version,erights_id, product_name, division_id) values('D4B26EAA-E013-4593-A870-2CA67634246E', 0, 990007, 'Solutions Elementary A1 Online Practice (TR) TEACHER', (select id from division where division_type='ELT'));
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','6ced6078-671c-48b5-95ef-123497b32e4b', 0, 'D4B26EAA-E013-4593-A870-2CA67634246E', '', null, '', '', 'ADMIN_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '6ced6078-671c-48b5-95ef-123497b32e4b', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

-- Solutions Pre-Intermediate A2 Online Practice (TR) TEACHER - 96a9d126-1aba-4e9a-8b9e-2634cac70ed6 instructor
insert into asset(id,obj_version,erights_id, product_name, division_id) values('FFB7C445-EBA3-4D88-8832-682ED89651E5', 0, 990008, 'Solutions Pre-Intermediate A2 Online Practice (TR) TEACHER', (select id from division where division_type='ELT'));
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','96a9d126-1aba-4e9a-8b9e-2634cac70ed6', 0, 'D4B26EAA-E013-4593-A870-2CA67634246E', '', null, '', '', 'ADMIN_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '96a9d126-1aba-4e9a-8b9e-2634cac70ed6', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
