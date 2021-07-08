-- OTE PRODUCTS

insert into external_system (id, obj_version, name, description ) values ('9c318f21-c559-4f9b-a2cb-461f7ec94e8f',0, 'OTE', 'Oxford Teachers English');
GO
insert into external_system_id_type (id, obj_version, name, description, external_system_id ) values ('a493d026-ddf3-4a9e-b307-c92822ffa5d6',0, 'ISBN-13', 'ISBN-13', '9c318f21-c559-4f9b-a2cb-461f7ec94e8f');
GO

--CONCURRENT OTE LICENCE
insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) values ('cf22810f-7836-4200-b315-5a1d0505ebcd', 0, 'CONCURRENT', null, null, 1, 1, null, null);
GO

--ROLLING OTE LICENCE
insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,begin_on,time_period,unit_type,allowed_usages) values ('f20674be-2dea-472b-8395-6d7edd613bb8', 0, 'ROLLING', null, null, null, null, 'FIRST_USE', 48, 'HOUR',NULL);
GO



--OTEB READING ONLINE

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('02d15d4c-c4b0-40af-b302-0d97e19eb903', 0, 33001, 'OTEB READING ONLINE', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');
GO

insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', '8c57778c-e784-4d62-911f-0331e8f200a3', 0, '02d15d4c-c4b0-40af-b302-0d97e19eb903', 'http://ttk-staging.oup.thetestfactory.com/9780194571593', null, 'ADMIN_REGISTERABLE', 'eacuatadmin@oup.com', null, 'http://ttk-staging.oup.thetestfactory.com/9780194571593');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '8c57778c-e784-4d62-911f-0331e8f200a3', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, 'f20674be-2dea-472b-8395-6d7edd613bb8');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, '8c57778c-e784-4d62-911f-0331e8f200a3', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);
GO

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'a493d026-ddf3-4a9e-b307-c92822ffa5d6', '9780194571593', 'PRODUCT', null, '8c57778c-e784-4d62-911f-0331e8f200a3');
GO


--OTEB WRITING ONLINE

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('1222e4c2-f64f-436c-9f4f-e39efb01f3c4', 0, 33002, 'OTEB WRITING ONLINE', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');
GO

insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', 'de275c36-9173-4204-ae5c-89db65b9fbeb', 0, '1222e4c2-f64f-436c-9f4f-e39efb01f3c4', 'http://ttk-staging.oup.thetestfactory.com/9780194571760', null, 'ADMIN_REGISTERABLE', 'eacuatadmin@oup.com', null, 'http://ttk-staging.oup.thetestfactory.com/9780194571760');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'de275c36-9173-4204-ae5c-89db65b9fbeb', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, 'f20674be-2dea-472b-8395-6d7edd613bb8');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, 'de275c36-9173-4204-ae5c-89db65b9fbeb', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);
GO

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'a493d026-ddf3-4a9e-b307-c92822ffa5d6', '9780194571760', 'PRODUCT', null, 'de275c36-9173-4204-ae5c-89db65b9fbeb');
GO


--OTEB SPEAKING ONLINE

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('47f3f934-8414-430c-ac0a-dd5a3aa5d90b', 0, 33003, 'OTEB SPEAKING ONLINE', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');
GO

insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', 'a23d69c6-01e6-42de-a403-e099aa88160a', 0, '47f3f934-8414-430c-ac0a-dd5a3aa5d90b', 'http://ttk-staging.oup.thetestfactory.com/9780194571777', null, 'ADMIN_REGISTERABLE', 'eacuatadmin@oup.com', null, 'http://ttk-staging.oup.thetestfactory.com/9780194571777');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'a23d69c6-01e6-42de-a403-e099aa88160a', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, 'f20674be-2dea-472b-8395-6d7edd613bb8');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, 'a23d69c6-01e6-42de-a403-e099aa88160a', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);
GO

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'a493d026-ddf3-4a9e-b307-c92822ffa5d6', '9780194571777', 'PRODUCT', null, 'a23d69c6-01e6-42de-a403-e099aa88160a');
GO


--OTEB LISTENING ONLINE

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('186910a3-40c9-48e2-a663-20819b4a1629', 0, 33004, 'OTEB LISTENING ONLINE', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');
GO

insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', '8ecfbc35-ef87-4c1b-8fe9-c6602db8d6dd', 0, '186910a3-40c9-48e2-a663-20819b4a1629', 'http://ttk-staging.oup.thetestfactory.com/9780194571784', null, 'ADMIN_REGISTERABLE', 'eacuatadmin@oup.com', null, 'http://ttk-staging.oup.thetestfactory.com/9780194571784');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '8ecfbc35-ef87-4c1b-8fe9-c6602db8d6dd', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, 'f20674be-2dea-472b-8395-6d7edd613bb8');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, '8ecfbc35-ef87-4c1b-8fe9-c6602db8d6dd', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);
GO

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'a493d026-ddf3-4a9e-b307-c92822ffa5d6', '9780194571784', 'PRODUCT', null, '8ecfbc35-ef87-4c1b-8fe9-c6602db8d6dd');
GO


--OTEB TEST PACK ONLINE

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('ae7c0405-f1fe-44fc-b2d4-ec8b8b8660fa', 0, 33005, 'OTEB TEST PACK ONLINE', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');
GO

insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', 'a64907ba-1e76-40c5-b6cf-eabf1709944a', 0, 'ae7c0405-f1fe-44fc-b2d4-ec8b8b8660fa', 'http://ttk-staging.oup.thetestfactory.com/9780194571821', null, 'ADMIN_REGISTERABLE', 'eacuatadmin@oup.com', null, 'http://ttk-staging.oup.thetestfactory.com/9780194571821');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'a64907ba-1e76-40c5-b6cf-eabf1709944a', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, 'f20674be-2dea-472b-8395-6d7edd613bb8');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, 'a64907ba-1e76-40c5-b6cf-eabf1709944a', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);
GO

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'a493d026-ddf3-4a9e-b307-c92822ffa5d6', '9780194571821', 'PRODUCT', null, 'a64907ba-1e76-40c5-b6cf-eabf1709944a');
GO


--OTE TEST TAKER

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('575a75e4-a650-489d-853c-d3403f80eae5', 0, 33006, 'OTE TEST TAKER', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');
GO

insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', 'c79fca93-5a96-4520-857a-141c80af2dc0', 0, '575a75e4-a650-489d-853c-d3403f80eae5', null, null, 'ADMIN_REGISTERABLE', 'eacuatadmin@oup.com', null, 'http://cnd-staging.oup.thetestfactory.com');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'c79fca93-5a96-4520-857a-141c80af2dc0', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, 'cf22810f-7836-4200-b315-5a1d0505ebcd');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, 'c79fca93-5a96-4520-857a-141c80af2dc0', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);
GO

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'a493d026-ddf3-4a9e-b307-c92822ffa5d6', 'cnd001', 'PRODUCT', null, 'c79fca93-5a96-4520-857a-141c80af2dc0');
GO


--OTE INVIGILATOR

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('3dc2ac5a-5b43-4d3a-a658-3787b322c6d2', 0, 33007, 'OTE INVIGILATOR', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');
GO

insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', '0364f5cc-1766-4613-9c51-be680e005090', 0, '3dc2ac5a-5b43-4d3a-a658-3787b322c6d2', null, null, 'ADMIN_REGISTERABLE', 'eacuatadmin@oup.com', null, 'http://ivg-staging.oup.thetestfactory.com');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '0364f5cc-1766-4613-9c51-be680e005090', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, 'cf22810f-7836-4200-b315-5a1d0505ebcd');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, '0364f5cc-1766-4613-9c51-be680e005090', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);
GO

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'a493d026-ddf3-4a9e-b307-c92822ffa5d6', 'ivg001', 'PRODUCT', null, '0364f5cc-1766-4613-9c51-be680e005090');
GO


--OTE ASSESSOR

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('06aeece2-a2c9-4ef9-a5a7-b71a65334fdd', 0, 33008, 'OTE ASSESSOR', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');
GO

insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', '8414955b-9703-46a9-bb05-a1e4039c3edd', 0, '06aeece2-a2c9-4ef9-a5a7-b71a65334fdd', null, null, 'ADMIN_REGISTERABLE', 'eacuatadmin@oup.com', null, 'http://exm-staging.oup.thetestfactory.com');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '8414955b-9703-46a9-bb05-a1e4039c3edd', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, 'cf22810f-7836-4200-b315-5a1d0505ebcd');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, '8414955b-9703-46a9-bb05-a1e4039c3edd', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);
GO

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'a493d026-ddf3-4a9e-b307-c92822ffa5d6', 'exm001', 'PRODUCT', null, '8414955b-9703-46a9-bb05-a1e4039c3edd');
GO


--OTE TEST CTR MGR

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('2c60581a-1c87-4c44-ba59-7f50b9f56431', 0, 33009, 'OTE TEST CTR MGR', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');
GO

insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', 'c8e26e70-51e5-42c0-9e2f-fc18c3848c6e', 0, '2c60581a-1c87-4c44-ba59-7f50b9f56431', null, null, 'ADMIN_REGISTERABLE', 'eacuatadmin@oup.com', null, 'http://tcm-staging.oup.thetestfactory.com');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'c8e26e70-51e5-42c0-9e2f-fc18c3848c6e', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, 'cf22810f-7836-4200-b315-5a1d0505ebcd');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, 'c8e26e70-51e5-42c0-9e2f-fc18c3848c6e', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, null);
GO

insert into external_identifier (id, obj_version, external_system_id_type_id, external_id, external_id_type, customer_id, product_id) values (newId(), 0, 'a493d026-ddf3-4a9e-b307-c92822ffa5d6', 'tcm001', 'PRODUCT', null, 'c8e26e70-51e5-42c0-9e2f-fc18c3848c6e');
GO
