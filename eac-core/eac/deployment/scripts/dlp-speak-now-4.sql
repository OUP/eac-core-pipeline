
-- Speak Now 4 Online practice - 9780194030519S learner
insert into asset(id,obj_version,erights_id, product_name, division_id) values('7A5C137A-4B24-469A-8F28-A49BF888A968', 0, 1010121, 'Speak Now 4 Online practice', (select id from division where division_type='ELT'));
insert into product (product_type, id ,obj_version, asset_id, home_page, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','71763C71-8607-4395-B82C-03BBA1D06E7F', 0, '7A5C137A-4B24-469A-8F28-A49BF888A968', 'https://enrolment.oxfordlearn.com/Account/MyAccount', '', null, 'elt.enquiry@oup.com', '', 'SELF_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '71763C71-8607-4395-B82C-03BBA1D06E7F', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into external_identifier (id, obj_version, external_system_id_type_id, external_id_type, customer_id, product_id, external_id) values (newId(), 0, 'A721E11F-6540-486D-9525-537B75C5D669', 'PRODUCT', null, '71763C71-8607-4395-B82C-03BBA1D06E7F', '9780194030519S');

-- Speak Now 4 Online practice TEACHER - 9780194030519T instructor
insert into asset(id,obj_version,erights_id, product_name, division_id) values('34A0BB50-BED1-471A-9B15-5C8D8146B519', 0, 1010122, 'Speak Now 4 Online practice TEACHER', (select id from division where division_type='ELT'));
insert into product (product_type, id, obj_version, asset_id, home_page, landing_page, registerable_product_id, email, service_level_agreement, registerable_type) values ('REGISTERABLE','11736907-8AB2-4109-BBC6-D98537D70062', 0, '34A0BB50-BED1-471A-9B15-5C8D8146B519', 'https://enrolment.oxfordlearn.com/Account/MyAccount', '', null, 'elt.enquiry@oup.com', '', 'ADMIN_REGISTERABLE');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '11736907-8AB2-4109-BBC6-D98537D70062', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into external_identifier (id, obj_version, external_system_id_type_id, external_id_type, customer_id, product_id, external_id) values (newId(), 0, 'A721E11F-6540-486D-9525-537B75C5D669', 'PRODUCT', null, '11736907-8AB2-4109-BBC6-D98537D70062', '9780194030519T');

