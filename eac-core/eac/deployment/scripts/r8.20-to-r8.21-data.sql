delete from message where basename = 'messages' and language = 'zh' and country = 'TW';
GO
-- Taiwan translations
--1 label.hello translation 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.hello',N'您好');
GO
--2 label.login 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.login',N'登入');
GO
--3 label.logout  
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.logout',N'登出');
GO
--4 label.password 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.password',N'密碼');
GO
--5 label.password.title 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.password.title',N'密碼 必填欄位');
GO
--6 label.username 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.username',N'使用者名稱');
GO
--7 label.username.title 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.username.title',N'使用者名稱 必填欄位');
GO
--8 label.view.profile 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.view.profile',N'檢視檔案'); 
GO
--9 problems.logging.in 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'problems.logging.in',N'登入問題？');
GO
--10 welcome.message 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'welcome.message',N'歡迎！若要更新您的資料，請按檢視檔案，若您是使用共用電腦，結束時請記得登出。');
GO
--11 title.resetpassword 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'title.resetpassword',        N'密碼重設');
GO
--12 label.resetpassword 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.resetpassword',        N'密碼重設');
GO
--13 submit.resetpassword 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'submit.resetpassword',       N'密碼重設');
GO
--14 label.password.reset 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.password.reset',       N'密碼重設');
GO
--15 title.resetpasswordsuccess 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'title.resetpasswordsuccess', N'密碼重設');
GO
--16 label.home
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'label.home', N'首頁');
GO
--17 footer.contactus
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','zh','TW',null,'footer.contactus', N'連絡我們');
GO

if (SELECT DB_NAME()) = 'eacuat'
BEGIN
    update registration_activation set validator_email = 'eacuatadmin@oup.com' where id = 'efa3f45c-d85f-4aa8-a7f1-5ab0b4da3d60';
END
GO

if (SELECT DB_NAME()) = 'eacprod'
BEGIN
    update registration_activation set validator_email = 'orc.help@oup.com' where id = 'efa3f45c-d85f-4aa8-a7f1-5ab0b4da3d60';
END
GO

DECLARE @nameofdb NVARCHAR(80);
SET @nameofdb = (SELECT DB_NAME());
if @nameofdb = 'eacuat'
BEGIN
-- ORCS Test Student
update product set landing_page = 'http://www.oupeacproducts.co.cc/eacSampleSite/protected/release8/orcs/orcs-test-student.jsp' where id = '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5';
update product set email = 'eacuatadmin@oup.com' where id = '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5';
update product set home_page = 'http://www.oupeacproducts.co.cc/eacSampleSite' where id = '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5';

-- ORCS Test Lecturer 1
update product set landing_page = 'http://www.oupeacproducts.co.cc/eacSampleSite/protected/release8/orcs/orcs-test-lecturer1.jsp' where id = 'E442209A-3BEE-4C89-A87A-CCFAA707B261';
update product set email = 'eacuatadmin@oup.com' where id = 'E442209A-3BEE-4C89-A87A-CCFAA707B261';
update product set home_page = 'http://www.oupeacproducts.co.cc/eacSampleSite' where id = 'E442209A-3BEE-4C89-A87A-CCFAA707B261';

-- ORCS Test Lecturer 2
update product set landing_page = 'http://www.oupeacproducts.co.cc/eacSampleSite/protected/release8/orcs/orcs-test-lecturer2.jsp' where id = 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788';
update product set email = 'eacuatadmin@oup.com' where id = 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788';
update product set home_page = 'http://www.oupeacproducts.co.cc/eacSampleSite' where id = 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788';
update product set email = 'eacuatadmin@oup.com' where registerable_product_id = 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788';
update product set home_page = 'http://www.oupeacproducts.co.cc/eacSampleSite' where registerable_product_id = 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788';
END
GO

DECLARE @nameofdb NVARCHAR(80);
SET @nameofdb = (SELECT DB_NAME());
if @nameofdb = 'eacuat'
BEGIN
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/principlesofeconomics2e' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Principles of Economics 2e' and product_type = 'REGISTERABLE');
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/MatricBioSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Biology for Matriculation Semester 2 3e' and product_type = 'REGISTERABLE');
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/fm2e' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Financial Mgmt 2e' and product_type = 'REGISTERABLE');
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/MatricPhysicsSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Physics for Matriculation Semester 2 4e' and product_type = 'REGISTERABLE');
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/businessresearchmethods' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Business Research Methods' and product_type = 'REGISTERABLE');
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/costaccounting5e' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Cost Accounting 5e' and product_type = 'REGISTERABLE');
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/MatricChemSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Chemistry for Matriculation Semester 2 4e' and product_type = 'REGISTERABLE');
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/me' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Essentials of Managerial Economics' and product_type = 'REGISTERABLE');
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/MatricMathSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Maths for Matriculation Semester 2 4e' and product_type = 'REGISTERABLE');
END
GO

update message set message = 'Please wait' where [key] = 'label.progress.awaiting' and basename = 'messages' and country is null;
GO
update question set product_specific = 1 where id = '1a5d9861-779e-4f26-8091-161db1d6d8b7';
GO
update question set product_specific = 1 where id = '5081f0e7-303e-49d1-96a5-70bfbbdf72fd';
GO
update question set product_specific = 1 where id = '800da845-5534-41a0-aff2-4838c16362fb';
GO
update question set product_specific = 1 where id = 'ad80e829-2ae1-4467-8f7b-e8aa306b56b0';
GO
update question set product_specific = 1 where id = 'bf12d4b7-a648-4277-8e12-0fdcd5dcdc65';
GO
update question set product_specific = 1 where id = '8bdaecc0-0a3c-4c6f-930e-0728ccf1cf48';
GO
update question set product_specific = 1 where id = '6e979582-ed2f-4407-b35c-66427e986721';
GO
update question set product_specific = 1 where id = '0138bf09-3d21-413c-90b9-bff8d297fd66';
GO
update asset set erights_id = 151916 where id = 'b3437e99-99be-4a75-90d0-83f3776abf89';
GO
update asset set erights_id = 151917 where id = '6fa75e17-9ac5-4a81-b6a2-b6b8f807ca4d';
GO

-- Fix for http://mantisoup.idm.fr/view.php?id=14195
update message set message = 'Access will be confirmed within {0} from date of registration, and you will be notified via email. If you do not receive the notification email within 30 minutes, please check for it in your spam or junk mail folder. If the email is not there please contact <a href="mailto:{1}">{1}</a>.' where [key] = 'label.awaitinglicenceactivationconfirm';
GO
update message set message = 'If your activation email does not arrive within 30 minutes please look in your spam or junk mail folder. If you can''''t find the activation email and need us to send you a new one, please ' where [key] = 'label.activatelicenceemail_1';
GO
update product set service_level_agreement = 'one week' where service_level_agreement = '30 minutes';
GO
update product set service_level_agreement = 'three working days' where id = '48F1893D-E916-4F17-A9AE-84B7E8C0BBC5';
GO
update product set service_level_agreement = 'three working days' where id = 'E442209A-3BEE-4C89-A87A-CCFAA707B261';
GO
update product set service_level_agreement = 'three working days' where id = 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788';
GO

-- Fix for http://mantisoup.idm.fr/view.php?id=14242
update registration_definition set licence_template_id = 'bdbdc319-a5ae-4023-b2ab-0950adc94e84' where product_id = 'E442209A-3BEE-4C89-A87A-CCFAA707B261' and licence_template_id = '8a008728321aebe001321af8eae90001';
GO
update registration_definition set licence_template_id = 'bdbdc319-a5ae-4023-b2ab-0950adc94e84' where product_id = 'A5CD020E-A65C-4ACB-A9D9-ED28F0A44788' and licence_template_id = '8a008728321aebe001321af8eae90001';
GO

update product set home_page = 'http://dev.eac.uk.oup.com/eacSampleSite/' where id = '985eb4ed-6b7f-45c1-85e0-41059cc930a11';
GO
update product set home_page = 'http://dev.eac.uk.oup.com/eacSampleSite/' where id = '7b082b45-f5e9-42d6-8e39-28f3539a92bb';
GO

update message set message = '<p>Students will not be granted access to this part of the site. </p><br/><p>All registrations are verified before activation so please ensure that you have notified your sales representative of your adoption. You can do so via this page: <a onclick="window.open(this.href);" href="http://www.oup.co.uk/contactus/academic/sales/hecontact/">Find your sales representative</a></p><br/><p>Please note that if there is no record of your adoption with your sales representative, we will not be able to process your application. Where adoptions have been confirmed we aim to activate your account within three working days and your access will expire after one year. If you continue to adopt this book after that period you can then request a renewal of your subscription.</p>' where [key] = 'label.orcs.lecturer.preamble' and language is null;
GO


update message set message = 'Institution Address 1' where [key] = 'label.school.address.line1' and language is null;
GO
update message set message = 'Institution Address 1' where [key] = 'title.school.address.line1' and language is null;
GO
update message set message = 'Institution Address 2' where [key] = 'label.school.address.line2' and language is null;
GO
update message set message = 'Institution Address 2' where [key] = 'title.school.address.line2' and language is null;
GO
update message set message = 'Institution Address 3' where [key] = 'label.school.address.line3' and language is null;
GO
update message set message = 'Institution Address 3' where [key] = 'title.school.address.line3' and language is null;
GO

update message set message = 'Title of module for which this textbook will be used' where [key] = 'label.module.title' and language is null;
GO
update message set message = 'Title of module for which this textbook will be used' where [key] = 'title.module.title' and language is null;
GO

update message set message = 'Institution name' where [key] = 'label.school.uni.name' and language is null;
GO
update message set message = 'Institution name' where [key] = 'title.school.uni.name' and language is null;
GO

update element set question_id = '986DFC87-8EFF-4F89-9963-94E1224DF10A' where id = '5a3a9d7f-7c84-489c-8baa-aad739222395';
GO
delete from question where id = '33164b0d-f6f2-4714-b005-bbf8691b41b5';
GO

update product set home_page = 'http://global.uat.oup.com/uk/orc/' where db_name() = 'eacuat' and id in ('985eb4ed-6b7f-45c1-85e0-41059cc930a11', '7b082b45-f5e9-42d6-8e39-28f3539a92bb');
GO

update product set home_page = 'http://www.oup.com/uk/orc/' where db_name() = 'eacprod' and id in ('985eb4ed-6b7f-45c1-85e0-41059cc930a11', '7b082b45-f5e9-42d6-8e39-28f3539a92bb');
GO