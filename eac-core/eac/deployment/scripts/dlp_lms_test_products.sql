
--Test Learner product
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','B67E2AEC-5EB4-455C-94C0-D8496615DB09', 0, 28401, '', 'DLP Test - 1234567890000 Learner', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', null, 'eacsystemadmin@oup.com', '30 minutes', null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'B67E2AEC-5EB4-455C-94C0-D8496615DB09', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');

--Test Instructor product
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','5B0FE460-C869-4BA2-B418-4236C7CC907D', 0, 28403, '', 'DLP Test - 1234567890000 Instructor', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', null, 'eacsystemadmin@oup.com', '30 minutes', null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '5B0FE460-C869-4BA2-B418-4236C7CC907D', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'F63E9309-3145-4035-A264-E10C058792A1', 0, 28401, '', 'DLP Test - 1234567890000 Learner', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', '5B0FE460-C869-4BA2-B418-4236C7CC907D', 'eacsystemadmin@oup.com', '30 minutes', 'POST_PARENT');

--Test Admin product
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','6F15C1C7-86BC-4D26-9335-19ED15FE921F', 0, 28402, '', 'DLP Test - Admin', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', null, 'eacsystemadmin@oup.com', '30 minutes', null);
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '6F15C1C7-86BC-4D26-9335-19ED15FE921F', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '53D51186-2CBE-403E-BFC5-87CBADC69D46', 0, 28404, '', 'DLP Test - Everything', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12', '6F15C1C7-86BC-4D26-9335-19ED15FE921F', 'eacsystemadmin@oup.com', '30 minutes', 'POST_PARENT');


-- clean up for testing
--delete from registration_definition where product_id in ('F63E9309-3145-4035-A264-E10C058792A1', '6F15C1C7-86BC-4D26-9335-19ED15FE921F', 'B67E2AEC-5EB4-455C-94C0-D8496615DB09', 'BF31457B-95E3-4F46-90B5-401C8AE722FB', '5B0FE460-C869-4BA2-B418-4236C7CC907D', '53D51186-2CBE-403E-BFC5-87CBADC69D46', '9D07D65C-8E52-4047-B692-4387FCBE72C3');
--delete from product where id in ('F63E9309-3145-4035-A264-E10C058792A1', '6F15C1C7-86BC-4D26-9335-19ED15FE921F', 'B67E2AEC-5EB4-455C-94C0-D8496615DB09', 'BF31457B-95E3-4F46-90B5-401C8AE722FB', '5B0FE460-C869-4BA2-B418-4236C7CC907D', '53D51186-2CBE-403E-BFC5-87CBADC69D46', '9D07D65C-8E52-4047-B692-4387FCBE72C3');
