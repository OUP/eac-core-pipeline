declare @erightsFREE int;
declare @erightsBRM int;
declare @erightsEME int;
declare @erightsLecBRM int;
declare @erightsLecEME int;
declare @erightsLecPOE2E int;
declare @homePage nvarchar(255);
declare @landingBRM nvarchar(255);
declare @landingEME nvarchar(255);
declare @landingLecBRM nvarchar(255);
declare @landingLecEME nvarchar(255);
declare @landingLecPOE2E nvarchar(255);

set @erightsFREE     = 5901;

set @erightsBRM      = 134206;
set @erightsEME      = 134207;
set @erightsLecPOE2E = 134222;
set @erightsLecBRM   = 134224;
set @erightsLecEME   = 134223;

set @homePage       ='http://dev.eac.uk.oup.com/eacSampleSite';
set @landingBRM     ='http://dev.eac.uk.oup.com/eacSampleSite/protected/release5/oxfordfajar/he-business-research-methods.jsp';
set @landingLecBRM  ='http://dev.eac.uk.oup.com/eacSampleSite/protected/release5/oxfordfajar/he-business-research-methods-la.jsp';
set @landingEME     ='http://dev.eac.uk.oup.com/eacSampleSite/protected/release5/oxfordfajar/he-essentials-of-managerial-economics.jsp';
set @landingLecEME  ='http://dev.eac.uk.oup.com/eacSampleSite/protected/release5/oxfordfajar/he-essentials-of-managerial-economics-la.jsp';
set @landingLecPOE2E='http://dev.eac.uk.oup.com/eacSampleSite/protected/release5/oxfordfajar/he-principles-of-economics2e-la.jsp';

declare @businessResearchMethodsId nvarchar(255);
declare @essManEcoId nvarchar(255);
declare @lecAccBusinessResearchMethodsId nvarchar(255)
declare @lecAccEssManEcoId nvarchar(255);
declare @lecAccPrinEco2eId nvarchar(255);

set @businessResearchMethodsId  ='B9BE0355-9F57-4142-8BA0-2084872569F4';
set @essManEcoId 				= 'E2651583-3023-4FA5-85A3-A2A7F6AB9032';
set @lecAccBusinessResearchMethodsId	='c132af19-b1bd-42c4-9d08-271ebc2a1861';
set @lecAccEssManEcoId 					= '3f01854c-cbf6-4122-bca8-3cf26797b559';
set @lecAccPrinEco2eId 					= '3504eb26-7ce5-4b3c-9846-d0ba57f9315f';
--
-- 1/5 BUSINESS RESEARCH METHODS (STUDENT)
--
update P set 
  P.landing_page = @landingBRM,
  P.home_page = @homePage,
  P.erights_id = @erightsBRM
from registration_definition RD
join product P on RD.product_id = P.id
where P.id = @businessResearchMethodsId;

update RD set 
  RD.page_definition_id = '13AC20F6-0414-4569-B6F0-D92FC3AE24A0'
from registration_definition RD
join product P on RD.product_id = P.id
where P.id = @businessResearchMethodsId;

--linked product
insert into product (
  registerable_product_id, product_type, id, obj_version, landing_page,
  product_name, division_id, email,
  service_level_agreement, activation_method, home_page, erights_id
)values(
  @businessResearchMethodsId, 'LINKED', NEWID(), 0, '',
  'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'cw.info@oxfordfajar.com.my',
  '30 minutes', 'PRE_PARENT', @homePage, @erightsFREE
)

--account registration definition
insert into registration_definition
( 
  product_id, id, obj_version,
  registration_activation_id, page_definition_id, registration_definition_type
)values(
  @businessResearchMethodsId, NEWID(), 0,
  'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', 'ACCOUNT_REGISTRATION'
);

--
-- 2/5 ESSENTIALS OF MANAGERIAL ECONOMICS (STUDENT)
--

update P set 
  P.landing_page = @landingEME, 
  P.home_page = @homePage,
  P.erights_id = @erightsEME
from registration_definition RD
join product P on RD.product_id = P.id
where P.id = @essManEcoId;

update RD set 
  RD.page_definition_id = '13AC20F6-0414-4569-B6F0-D92FC3AE24A0'
from registration_definition RD
join product P on RD.product_id = P.id
where P.id = @essManEcoId;

--linked product
insert into product (
  registerable_product_id,product_type,id,obj_version,landing_page,
  product_name,division_id,email,
  service_level_agreement,activation_method,home_page,erights_id
)values(
  @essManEcoId, 'LINKED', NEWID(), 0, '',
  'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'cw.info@oxfordfajar.com.my',
  '30 minutes', 'PRE_PARENT', @homePage, @erightsFREE
)

--account registration definition
insert into registration_definition
( 
  product_id, id, obj_version,
  registration_activation_id, page_definition_id, registration_definition_type
)values(
  @essManEcoId, NEWID(), 0,
  'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', 'ACCOUNT_REGISTRATION'
);

--
-- 3/5 BUSINESS RESEARCH METHODS (LECTURER ACCOUNT)
--

insert into product (
  id, obj_version,
  product_name, division_id,
  email, service_level_agreement,
  product_type, home_page,
  landing_page, erights_id
) values(
  @lecAccBusinessResearchMethodsId,0,
  'Business Research Methods (Lecturer Account)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd',
  'cw.info@oxfordfajar.com.my', '30 minutes',
  'REGISTERABLE', @homePage,
  @landingLecBRM, @erightsLecBRM
);

insert into product (
  registerable_product_id, product_type,
  id, obj_version, landing_page,
  product_name, division_id, email,
  service_level_agreement, activation_method, home_page, erights_id
)values(
  @lecAccBusinessResearchMethodsId, 'LINKED',
  NEWID(), 0, '',
  'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'cw.info@oxfordfajar.com.my',
  '30 minutes', 'PRE_PARENT', @homePage, @erightsFREE
);

insert into product (
  registerable_product_id, product_type,
  id, obj_version, landing_page,
  product_name, division_id, email,
  service_level_agreement, activation_method, home_page, erights_id
)values(
  @lecAccBusinessResearchMethodsId, 'LINKED',
  NEWID(), 0, '',
  'Business Research Methods', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'cw.info@oxfordfajar.com.my',
  '30 minutes', 'POST_PARENT', @homePage, @erightsBRM
);

insert into registration_definition
( 
  product_id, id, obj_version,
  registration_activation_id, page_definition_id, registration_definition_type
)values(
  @lecAccBusinessResearchMethodsId, NEWID(), 0,
'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', 'ACCOUNT_REGISTRATION'
);

insert into registration_definition
( 
  product_id, id, obj_version,
  registration_activation_id, page_definition_id, registration_definition_type, licence_template_id
)values(
  @lecAccBusinessResearchMethodsId, NEWID(),0,
  '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', '207BFAE7-57CD-4570-BA7B-2F10B7BAA337', 'PRODUCT_REGISTRATION', '6A0386AC-69AB-4E51-BBB6-8670F303F09C'
);

--
-- 4/5 ESSENTIALS OF MANAGEERIAL ECONOMICS (LECTURER ACCOUNT)
--

insert into product (
  id,obj_version,
  product_name,division_id,
  email,service_level_agreement,
  product_type,home_page,
  landing_page,erights_id
) values(
  @lecAccEssManEcoId, 0,
  'Essentials of Managerial Economics (Lecturer Account)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd',
  'cw.info@oxfordfajar.com.my', '30 minutes',
  'REGISTERABLE', @homePage,
  @landingLecEME, @erightsLecEME
);

insert into product (
  registerable_product_id,product_type,
  id,obj_version,landing_page,
  product_name,division_id,email,
  service_level_agreement,activation_method,home_page,erights_id
)values(
  @lecAccEssManEcoId, 'LINKED',
  NEWID(),0,'',
  'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'cw.info@oxfordfajar.com.my',
  '30 minutes', 'PRE_PARENT', @homePage, @erightsFREE
);

insert into product (
  registerable_product_id, product_type,
  id, obj_version, landing_page,
  product_name, division_id, email,
  service_level_agreement, activation_method, home_page, erights_id
)values(
  @lecAccEssManEcoId, 'LINKED',
  NEWID(), 0, '',
  'Essentials of Managerial Economics', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'cw.info@oxfordfajar.com.my',
  '30 minutes', 'POST_PARENT', @homePage, @erightsEME
);

insert into registration_definition
( 
  product_id, id, obj_version,
  registration_activation_id, page_definition_id, registration_definition_type
)values(
  @lecAccEssManEcoId, NEWID(),0,
  'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', 'ACCOUNT_REGISTRATION'
);

insert into registration_definition
( 
  product_id, id, obj_version,
  registration_activation_id, page_definition_id,
  registration_definition_type, licence_template_id
)values(
  @lecAccEssManEcoId, NEWID(), 0,
  '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', '207BFAE7-57CD-4570-BA7B-2F10B7BAA337',
  'PRODUCT_REGISTRATION', '6A0386AC-69AB-4E51-BBB6-8670F303F09C'
);

--
-- 5/5 PRICIPLES OF ECONOMICS 2E (LECTURER ACCOUNT)
--

insert into product (
  id,obj_version,
  product_name,division_id,
  email,service_level_agreement,
  product_type,home_page,
  landing_page,erights_id
) values(
  @lecAccPrinEco2eId, 0,
  'Principles of Economics 2e (Lecturer Account)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd',
  'cw.info@oxfordfajar.com.my', '30 minutes',
  'REGISTERABLE', @homePage,
  @landingLecPOE2E, @erightsLecPOE2E 
);

insert into product (
  registerable_product_id, product_type,
  id,obj_version, landing_page,
  product_name, division_id, email,
  service_level_agreement, activation_method, home_page,erights_id
)values(
  @lecAccPrinEco2eId,'LINKED',
  NEWID(), 0, '',
  'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'cw.info@oxfordfajar.com.my',
  '30 minutes', 'PRE_PARENT', @homePage, @erightsFREE
);

insert into registration_definition
( 
  product_id, id, obj_version,
  registration_activation_id, page_definition_id, registration_definition_type
)values(
  @lecAccPrinEco2eId, NEWID(), 0,
  'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', 'ACCOUNT_REGISTRATION'
);

insert into registration_definition
( 
  product_id, id, obj_version,
  registration_activation_id, page_definition_id, registration_definition_type, licence_template_id
)values(
  @lecAccPrinEco2eId, NEWID(), 0,
  '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', '207BFAE7-57CD-4570-BA7B-2F10B7BAA337', 'PRODUCT_REGISTRATION', '6A0386AC-69AB-4E51-BBB6-8670F303F09C'
);




