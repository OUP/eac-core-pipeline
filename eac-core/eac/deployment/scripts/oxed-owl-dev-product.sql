-----------------------------------
-- Oxford Owl Dev Product Script --
-----------------------------------

IF EXISTS (select 1 from sys.databases WHERE name = 'eactest')
BEGIN

insert into asset (id, obj_version, erights_id, product_name, division_id, [state]) values ('8af2bf8d3ceb2ea5013cecbac9e30000', 0, 162897, 'Oxford Owl free eBooks', 'bfdc5888-c26b-4ed5-a580-6aa363a25e13', 'ACTIVE');

insert into product (id, product_type, obj_version, landing_page, registerable_product_id, email, service_level_agreement, activation_method, home_page, asset_id, registerable_type) values ('04e51113-a859-4c24-a8ae-f7bdddf81b75', 'REGISTERABLE', '1', null, null, 'eacdevadmin@oup.com', '1 week', null, 'http://dev.eac.uk.oup.com/eacSampleSite', '8af2bf8d3ceb2ea5013cecbac9e30000', 'SELF_REGISTERABLE');

insert into licence_template (id,licence_type,obj_version,end_date,start_date,total_concurrency,user_concurrency,begin_on,time_period,unit_type,allowed_usages,updated_date,created_date) values ('8af2bf8d3ceb2ea5013cecbe3a1a0003', 'CONCURRENT', 0, null, null, 1, 1, null, null, null, null, null, '2013-02-18');

insert into registration_activation (id,activation_type,obj_version,validator_email,matched_activation,unmatched_activation,iso_country_list,description) values('8af2bf8d3ceb2ea5013cecbe399d0001', 'COUNTRY_MATCH', 1, null, '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', '119452A7-07BB-4B13-A334-BF44D04EAF57', 'AU,CN,HK,NZ,TR', 'Oxford Owl validation');

insert into registration_definition(id,registration_definition_type,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id,validation_required,confirmation_email_enabled) values('8af2bf8d3ceb2ea5013cecbe3a100002', 'ACCOUNT_REGISTRATION', 0, '04e51113-a859-4c24-a8ae-f7bdddf81b75', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '6AD3DAE4-94D5-46DD-A3E8-9336639E467D', null, 0, null);

insert into registration_definition(id,registration_definition_type,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id,validation_required,confirmation_email_enabled) values('8af2bf8d3ceb2ea5013cecbe3a260004', 'PRODUCT_REGISTRATION', 0, '04e51113-a859-4c24-a8ae-f7bdddf81b75', '8af2bf8d3ceb2ea5013cecbe399d0001', '2DBF57A4-9F9F-4651-8A42-6474E88C6B7D', '8af2bf8d3ceb2ea5013cecbe3a1a0003', 0, 0);

insert into url_skin(id,obj_version,url,skin_path,site_name,contact_path,customiser_bean_name,primary_site) values ('8af2bf8d3ceb2ea5013ced0910f80005', 0, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release11/oxed/', '../eacSkin/skin/oxed/oxfordowl/css/eac-override.css', 'Oxford Owl free eBooks', 'mailto:edtechsupport@oup.com?subject=Oxford%20Owl%20access%20query', null, 1);

END
GO

