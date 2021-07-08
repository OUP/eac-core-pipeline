DECLARE @nameofdb NVARCHAR(80);
SET @nameofdb = (SELECT DB_NAME());
if @nameofdb = 'eactest'
BEGIN
-- ORCS Test Student
insert into asset(id, obj_version, erights_id, product_name, division_id) values ('4A7477A9-E59B-4396-B145-6CF629734E60', 0, 148513, 'ORCS Test Student', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5', 0, '4A7477A9-E59B-4396-B145-6CF629734E60', 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs/orcs-test-student.jsp', null, 'ADMIN_REGISTERABLE', 'eacdevadmin@oup.com', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null);

-- ORCS Test Lecturer 1
insert into asset(id, obj_version, erights_id, product_name, division_id) values ('A82B8531-251E-42E4-8B67-F61036D9165F', 0, 149226, 'ORCS Test Lecturer 1', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', 'E442209A-3BEE-4C89-A87A-CCFAA707B261', 0, 'A82B8531-251E-42E4-8B67-F61036D9165F', 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs/orcs-test-lecturer1.jsp', null, 'SELF_REGISTERABLE', 'eacdevadmin@oup.com', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, 'E442209A-3BEE-4C89-A87A-CCFAA707B261', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63', '8a008728321aebe001321af8eae90001');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id, validation_required) values ('ACCOUNT_REGISTRATION', newId(), 0, 'E442209A-3BEE-4C89-A87A-CCFAA707B261', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null, 1);

-- ORCS Test Lecturer 2
insert into asset(id, obj_version, erights_id, product_name, division_id) values ('D26B3852-BEFB-4E1A-987B-329DF4218334', 0, 149126, 'ORCS Test Lecturer 2', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', 0, 'D26B3852-BEFB-4E1A-987B-329DF4218334', 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs/orcs-test-lecturer2.jsp', null, 'SELF_REGISTERABLE', 'eacdevadmin@oup.com', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, activation_method, home_page) values ('LINKED', newId(), 0, '4A7477A9-E59B-4396-B145-6CF629734E60', '', 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', null, 'eacdevadmin@oup.com', '30 minutes', 'POST_PARENT', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63', '8a008728321aebe001321af8eae90001');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id, validation_required) values ('ACCOUNT_REGISTRATION', newId(), 0, 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null, 1);
END
GO

DECLARE @nameofdb NVARCHAR(80);
SET @nameofdb = (SELECT DB_NAME());
if @nameofdb = 'eacuat'
BEGIN
-- ORCS Test Student
insert into asset(id, obj_version, erights_id, product_name, division_id) values ('4A7477A9-E59B-4396-B145-6CF629734E60', 0, 31901, 'ORCS Test Student', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5', 0, '4A7477A9-E59B-4396-B145-6CF629734E60', 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs/orcs-test-student.jsp', null, 'ADMIN_REGISTERABLE', 'eacdevadmin@oup.com', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null);

-- ORCS Test Lecturer 1
insert into asset(id, obj_version, erights_id, product_name, division_id) values ('A82B8531-251E-42E4-8B67-F61036D9165F', 0, 32101, 'ORCS Test Lecturer 1', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', 'E442209A-3BEE-4C89-A87A-CCFAA707B261', 0, 'A82B8531-251E-42E4-8B67-F61036D9165F', 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs/orcs-test-lecturer1.jsp', null, 'SELF_REGISTERABLE', 'eacdevadmin@oup.com', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, 'E442209A-3BEE-4C89-A87A-CCFAA707B261', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63', '8a008728321aebe001321af8eae90001');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id, validation_required) values ('ACCOUNT_REGISTRATION', newId(), 0, 'E442209A-3BEE-4C89-A87A-CCFAA707B261', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null, 1);

-- ORCS Test Lecturer 2
insert into asset(id, obj_version, erights_id, product_name, division_id) values ('D26B3852-BEFB-4E1A-987B-329DF4218334', 0, 32102, 'ORCS Test Lecturer 2', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', 0, 'D26B3852-BEFB-4E1A-987B-329DF4218334', 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs/orcs-test-lecturer2.jsp', null, 'SELF_REGISTERABLE', 'eacdevadmin@oup.com', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, activation_method, home_page) values ('LINKED', newId(), 0, '4A7477A9-E59B-4396-B145-6CF629734E60', '', 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', null, 'eacdevadmin@oup.com', '30 minutes', 'POST_PARENT', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63', '8a008728321aebe001321af8eae90001');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id, validation_required) values ('ACCOUNT_REGISTRATION', newId(), 0, 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null, 1);
END
GO

DECLARE @nameofdb NVARCHAR(80);
SET @nameofdb = (SELECT DB_NAME());
if @nameofdb = 'eacprod'
BEGIN
-- ORCS Test Student
insert into asset(id, obj_version, erights_id, product_name, division_id) values ('4A7477A9-E59B-4396-B145-6CF629734E60', 0, 999999, 'ORCS Test Student', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5', 0, '4A7477A9-E59B-4396-B145-6CF629734E60', 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs/orcs-test-student.jsp', null, 'ADMIN_REGISTERABLE', 'eacdevadmin@oup.com', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', null, '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null);

-- ORCS Test Lecturer 1
insert into asset(id, obj_version, erights_id, product_name, division_id) values ('A82B8531-251E-42E4-8B67-F61036D9165F', 0, 999999, 'ORCS Test Lecturer 1', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', 'E442209A-3BEE-4C89-A87A-CCFAA707B261', 0, 'A82B8531-251E-42E4-8B67-F61036D9165F', 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs/orcs-test-lecturer1.jsp', null, 'SELF_REGISTERABLE', 'eacdevadmin@oup.com', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, 'E442209A-3BEE-4C89-A87A-CCFAA707B261', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63', '8a008728321aebe001321af8eae90001');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id, validation_required) values ('ACCOUNT_REGISTRATION', newId(), 0, 'E442209A-3BEE-4C89-A87A-CCFAA707B261', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null, 1);

-- ORCS Test Lecturer 2
insert into asset(id, obj_version, erights_id, product_name, division_id) values ('D26B3852-BEFB-4E1A-987B-329DF4218334', 0, 999999, 'ORCS Test Lecturer 2', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', 0, 'D26B3852-BEFB-4E1A-987B-329DF4218334', 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs/orcs-test-lecturer2.jsp', null, 'SELF_REGISTERABLE', 'eacdevadmin@oup.com', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, activation_method, home_page) values ('LINKED', newId(), 0, '4A7477A9-E59B-4396-B145-6CF629734E60', '', 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', null, 'eacdevadmin@oup.com', '30 minutes', 'POST_PARENT', 'http://dev.eac.uk.oup.com/eacSampleSite');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63', '8a008728321aebe001321af8eae90001');
insert into registration_definition (registration_definition_type, id, obj_version, product_id, registration_activation_id, page_definition_id, licence_template_id, validation_required) values ('ACCOUNT_REGISTRATION', newId(), 0, 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null, 1);
END
GO
