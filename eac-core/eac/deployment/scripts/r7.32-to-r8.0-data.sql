update registration_definition set validation_required = 0;
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.validateemail','Thank you for your interest in OUP resources. Please click on the following link to validate your email address.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.emailvalidation1','We have sent an email to the address you supplied; this email contains a link which you will need to click to verfiy that you have entered a valid email address.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.emailvalidation2','If your email does not arrive within 30 mintues please look in your spam or junk mail folder. You may need to make sure that do_not_reply@oup.com is on your list of allowed email addresses. If the email is not there please');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.emailvalidation3','request a new email');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.emailvalidation4','or contact');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.emailvalidation5','for assistance.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.validateemail','Validate Email Address');
GO
update message set message = 'Taiwan' where [key] = 'label.registration.country.tw';
GO
update message set message = 'Country/Region' where [key] in ('title.registration.address.line6', 'label.registration.address.line6'); 
GO
update licence_template set end_date = '2016-09-30T23:59:59.997' where id in (select t1.licence_template_id from registration_definition t1, asset t2, product t3 where t2.product_name in ('Financial Mgmt 2e (Lecturer)', 'Cost Accounting 5e (Lecturer)') and t3.asset_id = t2.id and t1.product_id = t3.id and t1.registration_definition_type = 'PRODUCT_REGISTRATION');
GO
insert into url_skin (id, obj_version, url, skin_path, site_name, contact_path, customiser_bean_name) values (newId(), 0, 'http://www.oup.com/uk/orc', 'skin/gab/orcs/css/eac-override.css', 'Online Resource Centres', 'http://www.oup.com/uk/orc/feedback_form/', null);
GO
update url_skin set url = 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release8/orcs' where db_name() = 'eactest' and url = 'http://www.oup.com/uk/orc';
GO
update url_skin set url = 'http://global.uat.oup.com/uk/orc' where db_name() = 'eacuat' and url = 'http://www.oup.com/uk/orc';
GO
update url_skin set url = 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release3/elt' where db_name() = 'eactest' and url = 'http://elt.oup.com';
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
-- Taiwan registartion questions 
-- number of students
insert into element_country_restriction (id, obj_version, element_id, locale) values (newId(), 0, 'B0D11F96-8B2A-4CD5-BCAD-28B83C5BBB9A', 'zh_TW');
GO
--Teaching status
insert into element_country_restriction (id, obj_version, element_id, locale) values (newId(), 0, '787171E7-47C1-4956-B1B4-AE3643AF1C36', 'zh_TW');
GO
--Teaching interests
insert into element_country_restriction (id, obj_version, element_id, locale) values (newId(), 0, '8B4ED14E-555E-4CC7-B17C-92BA7F625B94', 'zh_TW');
GO
--Mobile
insert into element_country_restriction (id, obj_version, element_id, locale) values (newId(), 0, '00a84972-6cf9-4807-a88b-57e9bb716936', 'zh_TW');
GO

-- Fixes to Oxford Fajar landing pages - http://mantisoup.idm.fr/view.php?id=13975#c26407
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/principlesofeconomics2e' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Principles of Economics 2e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/MatricBioSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Biology for Matriculation Semester 2 3e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Principles of Economics 2e (Lecturer Account)' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Essentials of Managerial Economics (Lecturer Account)' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Financial Mgmt 2e (Lecturer)' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/fm2e' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Financial Mgmt 2e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/MatricPhysicsSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Physics for Matriculation Semester 2 4e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/businessresearchmethods' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Business Research Methods' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Business Research Methods (Lecturer Account)' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/costaccounting5e' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Cost Accounting 5e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/MatricChemSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Chemistry for Matriculation Semester 2 4e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/me' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Essentials of Managerial Economics' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/MatricMathSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Maths for Matriculation Semester 2 4e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Cost Accounting 5e (Lecturer)' and product_type = 'REGISTERABLE');
GO


-- PROGRESS BAR UPDATES

update progress_bar set account_validated = 'NON_VALIDATED';
GO

-- PROGRESS BARS

-- ACCOUNT CREATION

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('e9b63e12-82ca-41dd-8c71-dd821cbc6df5', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'REGULAR', 'NA', 'NEW', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', 'e9b63e12-82ca-41dd-8c71-dd821cbc6df5', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'INCOMPLETE_STEP', 'label.progress.awaiting.validation', 'e9b63e12-82ca-41dd-8c71-dd821cbc6df5', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', 'e9b63e12-82ca-41dd-8c71-dd821cbc6df5', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, 'e9b63e12-82ca-41dd-8c71-dd821cbc6df5', 4);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('899de11f-59fe-43b1-bdaf-7bd37b28905a', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'REGULAR', 'NA', 'NEW', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '899de11f-59fe-43b1-bdaf-7bd37b28905a', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'INCOMPLETE_STEP', 'label.progress.awaiting.validation', '899de11f-59fe-43b1-bdaf-7bd37b28905a', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '899de11f-59fe-43b1-bdaf-7bd37b28905a', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '899de11f-59fe-43b1-bdaf-7bd37b28905a', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '899de11f-59fe-43b1-bdaf-7bd37b28905a', 5);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('786eb618-57a7-4ab2-b43f-19b262029d9c', 0, 'UNKNOWN', 'VALIDATED', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'REGULAR', 'NA', 'NEW', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '786eb618-57a7-4ab2-b43f-19b262029d9c', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'INCOMPLETE_STEP', 'label.progress.awaiting.validation', '786eb618-57a7-4ab2-b43f-19b262029d9c', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '786eb618-57a7-4ab2-b43f-19b262029d9c', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', '786eb618-57a7-4ab2-b43f-19b262029d9c', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '786eb618-57a7-4ab2-b43f-19b262029d9c', 5);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('bd98cb50-dab2-4702-bc6f-aa2006b31557', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'NO', 'NEW', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', 'bd98cb50-dab2-4702-bc6f-aa2006b31557', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'INCOMPLETE_STEP', 'label.progress.awaiting.validation', 'bd98cb50-dab2-4702-bc6f-aa2006b31557', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', 'bd98cb50-dab2-4702-bc6f-aa2006b31557', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', 'bd98cb50-dab2-4702-bc6f-aa2006b31557', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', 'bd98cb50-dab2-4702-bc6f-aa2006b31557', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, 'bd98cb50-dab2-4702-bc6f-aa2006b31557', 6);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('2e0dc716-877f-44bf-b91a-a4f7386eb4be', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'YES', 'NEW', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '2e0dc716-877f-44bf-b91a-a4f7386eb4be', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'INCOMPLETE_STEP', 'label.progress.awaiting.validation', '2e0dc716-877f-44bf-b91a-a4f7386eb4be', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '2e0dc716-877f-44bf-b91a-a4f7386eb4be', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '2e0dc716-877f-44bf-b91a-a4f7386eb4be', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '2e0dc716-877f-44bf-b91a-a4f7386eb4be', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '2e0dc716-877f-44bf-b91a-a4f7386eb4be', 6);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('38bf45b2-1926-489a-9a41-c253f962413e', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'NO', 'NEW', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '38bf45b2-1926-489a-9a41-c253f962413e', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'INCOMPLETE_STEP', 'label.progress.awaiting.validation', '38bf45b2-1926-489a-9a41-c253f962413e', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '38bf45b2-1926-489a-9a41-c253f962413e', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '38bf45b2-1926-489a-9a41-c253f962413e', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '38bf45b2-1926-489a-9a41-c253f962413e', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '38bf45b2-1926-489a-9a41-c253f962413e', 6);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('79a81a83-1d29-4777-ac8e-ed1d9ef17772', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'DIRECT', 'NEW', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '79a81a83-1d29-4777-ac8e-ed1d9ef17772', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'INCOMPLETE_STEP', 'label.progress.awaiting.validation', '79a81a83-1d29-4777-ac8e-ed1d9ef17772', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '79a81a83-1d29-4777-ac8e-ed1d9ef17772', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '79a81a83-1d29-4777-ac8e-ed1d9ef17772', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '79a81a83-1d29-4777-ac8e-ed1d9ef17772', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '79a81a83-1d29-4777-ac8e-ed1d9ef17772', 6);
GO



/**************************
 * 
 * 
 * ACCOUNT VALIDATION
 * 
 * 
 * ***************************************/


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('a7e97500-700e-401a-990e-970f3a4b3574', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_VALIDATION', 'UNKNOWN', 'REGULAR', 'NA', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'a7e97500-700e-401a-990e-970f3a4b3574', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting.validation', 'a7e97500-700e-401a-990e-970f3a4b3574', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', 'a7e97500-700e-401a-990e-970f3a4b3574', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', 'a7e97500-700e-401a-990e-970f3a4b3574', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, 'a7e97500-700e-401a-990e-970f3a4b3574', 5);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('218a4b38-db71-4461-88f0-457391038017', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_VALIDATION', 'UNKNOWN', 'REGULAR', 'NA', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '218a4b38-db71-4461-88f0-457391038017', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting.validation', '218a4b38-db71-4461-88f0-457391038017', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '218a4b38-db71-4461-88f0-457391038017', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '218a4b38-db71-4461-88f0-457391038017', 4);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('166a5944-15e3-4622-8350-8a53f65e5f78', 0, 'UNKNOWN', 'VALIDATED', 'ACCOUNT_VALIDATION', 'UNKNOWN', 'REGULAR', 'NA', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '166a5944-15e3-4622-8350-8a53f65e5f78', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting.validation', '166a5944-15e3-4622-8350-8a53f65e5f78', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '166a5944-15e3-4622-8350-8a53f65e5f78', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', '166a5944-15e3-4622-8350-8a53f65e5f78', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '166a5944-15e3-4622-8350-8a53f65e5f78', 5);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('ed699eb7-9f01-4d97-be30-9b44624510eb', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_VALIDATION', 'UNKNOWN', 'TOKEN', 'YES', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ed699eb7-9f01-4d97-be30-9b44624510eb', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting.validation', 'ed699eb7-9f01-4d97-be30-9b44624510eb', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ed699eb7-9f01-4d97-be30-9b44624510eb', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ed699eb7-9f01-4d97-be30-9b44624510eb', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', 'ed699eb7-9f01-4d97-be30-9b44624510eb', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'COMPLETE_NO_STEP', null, 'ed699eb7-9f01-4d97-be30-9b44624510eb', 6);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('7ef19215-aa91-4894-be77-7620a532f841', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_VALIDATION', 'UNKNOWN', 'TOKEN', 'NO', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '7ef19215-aa91-4894-be77-7620a532f841', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting.validation', '7ef19215-aa91-4894-be77-7620a532f841', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '7ef19215-aa91-4894-be77-7620a532f841', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '7ef19215-aa91-4894-be77-7620a532f841', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '7ef19215-aa91-4894-be77-7620a532f841', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '7ef19215-aa91-4894-be77-7620a532f841', 6);
GO

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('fa253c49-182d-4dea-9eeb-fe68ac35c7ef', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_VALIDATION', 'UNKNOWN', 'TOKEN', 'DIRECT', 'NEW', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'fa253c49-182d-4dea-9eeb-fe68ac35c7ef', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting.validation', 'fa253c49-182d-4dea-9eeb-fe68ac35c7ef', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', 'fa253c49-182d-4dea-9eeb-fe68ac35c7ef', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', 'fa253c49-182d-4dea-9eeb-fe68ac35c7ef', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', 'fa253c49-182d-4dea-9eeb-fe68ac35c7ef', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, 'fa253c49-182d-4dea-9eeb-fe68ac35c7ef', 6);
GO


-- PRODUCT REGISTRATION

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('e574f32c-17f2-45bb-ad28-644ce164897e', 0, 'NONE', 'INSTANT', 'PRODUCT_REGISTRATION', 'NONE', 'REGULAR', 'NA', 'LOGGEDIN', 'VALIDATED');
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'e574f32c-17f2-45bb-ad28-644ce164897e', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'e574f32c-17f2-45bb-ad28-644ce164897e', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', 'e574f32c-17f2-45bb-ad28-644ce164897e', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'COMPLETE_NO_STEP', null, 'e574f32c-17f2-45bb-ad28-644ce164897e', 4);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('fa71c5f0-6695-46fd-bdb9-b6ff640dd5e2', 0, 'NONE', 'SELF', 'PRODUCT_REGISTRATION', 'NONE', 'REGULAR', 'NA', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'fa71c5f0-6695-46fd-bdb9-b6ff640dd5e2', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'fa71c5f0-6695-46fd-bdb9-b6ff640dd5e2', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', 'fa71c5f0-6695-46fd-bdb9-b6ff640dd5e2', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', 'fa71c5f0-6695-46fd-bdb9-b6ff640dd5e2', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, 'fa71c5f0-6695-46fd-bdb9-b6ff640dd5e2', 5);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('ab0a9131-b1a7-45ec-b158-2c27a8c4405e', 0, 'NONE', 'VALIDATED', 'PRODUCT_REGISTRATION', 'NONE', 'REGULAR', 'NA', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ab0a9131-b1a7-45ec-b158-2c27a8c4405e', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ab0a9131-b1a7-45ec-b158-2c27a8c4405e', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', 'ab0a9131-b1a7-45ec-b158-2c27a8c4405e', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', 'ab0a9131-b1a7-45ec-b158-2c27a8c4405e', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, 'ab0a9131-b1a7-45ec-b158-2c27a8c4405e', 5);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('2e7c2d2a-6d51-4121-ab98-75738018ee66', 0, 'NONE', 'INSTANT', 'PRODUCT_REGISTRATION', 'NONE', 'TOKEN', 'YES', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '2e7c2d2a-6d51-4121-ab98-75738018ee66', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting.validation', '2e7c2d2a-6d51-4121-ab98-75738018ee66', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '2e7c2d2a-6d51-4121-ab98-75738018ee66', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '2e7c2d2a-6d51-4121-ab98-75738018ee66', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', '2e7c2d2a-6d51-4121-ab98-75738018ee66', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '2e7c2d2a-6d51-4121-ab98-75738018ee66', 6);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('21fef1a6-b6a4-44b2-9398-bdae61e3c064', 0, 'NONE', 'SELF', 'PRODUCT_REGISTRATION', 'NONE', 'TOKEN', 'YES', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '21fef1a6-b6a4-44b2-9398-bdae61e3c064', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting account validation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting.validation', '21fef1a6-b6a4-44b2-9398-bdae61e3c064', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '21fef1a6-b6a4-44b2-9398-bdae61e3c064', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', '21fef1a6-b6a4-44b2-9398-bdae61e3c064', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '21fef1a6-b6a4-44b2-9398-bdae61e3c064', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '21fef1a6-b6a4-44b2-9398-bdae61e3c064', 6);
GO

-- ACTIVATE LICENCE

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('42f420ba-2287-4302-9628-9e38e2f44e58', 0, 'AWAITING', 'SELF', 'ACTIVATE_LICENCE', 'COMPLETE', 'REGULAR', 'NA', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '42f420ba-2287-4302-9628-9e38e2f44e58', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '42f420ba-2287-4302-9628-9e38e2f44e58', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '42f420ba-2287-4302-9628-9e38e2f44e58', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', '42f420ba-2287-4302-9628-9e38e2f44e58', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '42f420ba-2287-4302-9628-9e38e2f44e58', 5);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('2c28568e-0ba4-4087-b37c-409bbe52fe5b', 0, 'AWAITING', 'SELF', 'ACTIVATE_LICENCE', 'COMPLETE', 'TOKEN', 'YES', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '2c28568e-0ba4-4087-b37c-409bbe52fe5b', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '2c28568e-0ba4-4087-b37c-409bbe52fe5b', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '2c28568e-0ba4-4087-b37c-409bbe52fe5b', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '2c28568e-0ba4-4087-b37c-409bbe52fe5b', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', '2c28568e-0ba4-4087-b37c-409bbe52fe5b', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '2c28568e-0ba4-4087-b37c-409bbe52fe5b', 6);
GO


-- AWAITING LICENCE ACTIVATION

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('0e4e9ca9-3bfb-430d-84e6-d5ef9aa521a5', 0, 'AWAITING', 'VALIDATED', 'AWAITING_LICENCE_ACTIVATION', 'COMPLETE', 'REGULAR', 'NA', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '0e4e9ca9-3bfb-430d-84e6-d5ef9aa521a5', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '0e4e9ca9-3bfb-430d-84e6-d5ef9aa521a5', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '0e4e9ca9-3bfb-430d-84e6-d5ef9aa521a5', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting confirmation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting', '0e4e9ca9-3bfb-430d-84e6-d5ef9aa521a5', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '0e4e9ca9-3bfb-430d-84e6-d5ef9aa521a5', 5);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('8adf0328-aa56-48a3-beae-7cf0a5251ddd', 0, 'AWAITING', 'VALIDATED', 'AWAITING_LICENCE_ACTIVATION', 'COMPLETE', 'TOKEN', 'YES', 'LOGGEDIN', 'VALIDATED');
GO


insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '8adf0328-aa56-48a3-beae-7cf0a5251ddd', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '8adf0328-aa56-48a3-beae-7cf0a5251ddd', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '8adf0328-aa56-48a3-beae-7cf0a5251ddd', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '8adf0328-aa56-48a3-beae-7cf0a5251ddd', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting confirmation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting', '8adf0328-aa56-48a3-beae-7cf0a5251ddd', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '8adf0328-aa56-48a3-beae-7cf0a5251ddd', 6);
GO


-- VALIDATION DENIED

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('035b50bc-1b3e-4213-a4fc-3d4b1b97495e', 0, 'DENIED', 'VALIDATED', 'VALIDATION_DENIED', 'COMPLETE', 'REGULAR', 'NA', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '035b50bc-1b3e-4213-a4fc-3d4b1b97495e', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '035b50bc-1b3e-4213-a4fc-3d4b1b97495e', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '035b50bc-1b3e-4213-a4fc-3d4b1b97495e', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment denied', 'CURRENT_COMPLETED_STEP', 'label.progress.denied', '035b50bc-1b3e-4213-a4fc-3d4b1b97495e', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '035b50bc-1b3e-4213-a4fc-3d4b1b97495e', 5);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('d7af0538-3ff0-472b-980d-374fde9279fe', 0, 'DENIED', 'VALIDATED', 'VALIDATION_DENIED', 'COMPLETE', 'TOKEN', 'YES', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'd7af0538-3ff0-472b-980d-374fde9279fe', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'd7af0538-3ff0-472b-980d-374fde9279fe', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'd7af0538-3ff0-472b-980d-374fde9279fe', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'd7af0538-3ff0-472b-980d-374fde9279fe', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment denied', 'CURRENT_COMPLETED_STEP', 'label.progress.denied', 'd7af0538-3ff0-472b-980d-374fde9279fe', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'COMPLETE_NO_STEP', null, 'd7af0538-3ff0-472b-980d-374fde9279fe', 6);
GO


-- ACTIVATION CODE

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('1ce4b2b4-3476-4f39-94b6-85e21249b829', 0, 'UNKNOWN', 'SELF', 'ACTIVATION_CODE', 'UNKNOWN', 'TOKEN', 'NO', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '1ce4b2b4-3476-4f39-94b6-85e21249b829', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '1ce4b2b4-3476-4f39-94b6-85e21249b829', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Activate', 'CURRENT_COMPLETED_STEP', 'label.progress.activate', '1ce4b2b4-3476-4f39-94b6-85e21249b829', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '1ce4b2b4-3476-4f39-94b6-85e21249b829', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '1ce4b2b4-3476-4f39-94b6-85e21249b829', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '1ce4b2b4-3476-4f39-94b6-85e21249b829', 6);
GO


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated) values('12cba82c-06c0-4157-b070-be01c079c106', 0, 'UNKNOWN', 'VALIDATED', 'ACTIVATION_CODE', 'UNKNOWN', 'TOKEN', 'NO', 'LOGGEDIN', 'VALIDATED');
GO

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '12cba82c-06c0-4157-b070-be01c079c106', 1);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '12cba82c-06c0-4157-b070-be01c079c106', 2);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Activate', 'CURRENT_COMPLETED_STEP', 'label.progress.activate', '12cba82c-06c0-4157-b070-be01c079c106', 3);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '12cba82c-06c0-4157-b070-be01c079c106', 4);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', '12cba82c-06c0-4157-b070-be01c079c106', 5);
GO
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence) values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '12cba82c-06c0-4157-b070-be01c079c106', 6);
GO


insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.progress.awaiting.validation', 'Confirm');
GO
update message set message = 'Sign up' where [key] = 'label.progress.enrolment' and basename = 'messages' and country is null;
GO
update message set message = 'Pending' where [key] = 'label.progress.awaiting' and basename = 'messages' and country is null;
GO

-- Fixes to Oxford Fajar landing pages - http://mantisoup.idm.fr/view.php?id=13975#c26407
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/principlesofeconomics2e' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Principles of Economics 2e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/MatricBioSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Biology for Matriculation Semester 2 3e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Principles of Economics 2e (Lecturer Account)' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Essentials of Managerial Economics (Lecturer Account)' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Financial Mgmt 2e (Lecturer)' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/fm2e' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Financial Mgmt 2e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/MatricPhysicsSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Physics for Matriculation Semester 2 4e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/businessresearchmethods' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Business Research Methods' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Business Research Methods (Lecturer Account)' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/costaccounting5e' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Cost Accounting 5e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/MatricChemSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Chemistry for Matriculation Semester 2 4e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/me' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Essentials of Managerial Economics' and product_type = 'REGISTERABLE');
GO
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/MatricMathSem2' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Maths for Matriculation Semester 2 4e' and product_type = 'REGISTERABLE');
GO
update product set landing_page = '' where id = (select product.id from product, asset where product.asset_id = asset.id and asset.product_name = 'Cost Accounting 5e (Lecturer)' and product_type = 'REGISTERABLE');
GO


insert into external_system (id, obj_version, name, description ) values ('514257a6-3ff4-4a73-974a-7e8527e6f283',0, 'ORCS', 'Online Resource Centres');
GO
insert into external_system_id_type (id, obj_version, name, description, external_system_id ) values ('6395fb93-e8d0-4135-86d2-42e78b4eb1ce',0, 'ISBN', 'ORC ISBN', '514257a6-3ff4-4a73-974a-7e8527e6f283');
GO



-- ORCS FORMS

-- MESSAGES
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.orcs.lecturer.preamble','<p>Students will not be granted access to this part of the site. All applications are verified by OUP.</p><br/><p>All registrations are verified before activation so please ensure that you have notified your sales representative of your adoption. You can do so via this page: <a href="http://www.oup.co.uk/contactus/academic/sales/hecontact/">Find your sales representative</a></p><br/><p>Once you have done this, please fill in your details below and press the ''''Register'''' button.</p><br/><p>Please note that if there is no record of your adoption with your sales representative, we will not be able to process your application. Where adoptions have been confirmed we aim to activate your account within three working days and your access will expire after one year. If you continue to adopt this book after that period you can then request a renewal of your subscription.</p>');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.orcs.staff.preamble','<p>Only employees may register for access to this content. Registration details will be verified before access is granted. Please allow one week for verification. This Employee Account will give access to all employee resources. </p>');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.orcs.personal.details','Your Personal Details');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.orcs.institution.details','Institution Details');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.orcs.course.details','Course Details');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.orcs.employee.details','Employee Details');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.orcs.oup.marketing','OUP Marketing Information');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.telephone.contact.number','Telephone contact number');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.telephone.contact.number','Telephone contact number');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.telephone.number.type','Telephone number type');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.telephone.number.type','Telephone number type');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.module.title','Title of module for which this textbook and Online Resource Centre will be used');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.module.title','Title of module for which this textbook and Online Resource Centre will be used');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.level','Level');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.level','Level');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.1st.year.undergraduate','1st year undergraduate');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.2nd.year.undergraduate','2nd year undergraduate');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.3rd.year.undergraduate','3rd year undergraduate');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.4th.year.undergraduate','4th year undergraduate');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.5th.year.undergraduate','5th year undergraduate');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.6th.year.undergraduate','6th year undergraduate');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.level.other','Other');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.please.specify.other','If other, please specify');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.please.specify.other','If other, please specify');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.textbook.used','Textbook previously used on course');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.textbook.used','Textbook previously used on course');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about','Where did you hear about this Online Resource Centre?');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.hear.about','Where did you hear about this Online Resource Centre?');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about.textbook.cover','Textbook cover ');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about.rep','Rep');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about.direct.mail','Direct mail');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about.email.promotion','Email Promotion');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about.website','Web site');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about.lecturer.recommendation','Lecturer recommendation');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about.oup.rep','Class presentation by OUP rep');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about.promotional.material','Promotional material on campus');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.hear.about.other','Other');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.text.book.adoption','Textbook Adoption');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.text.book.adoption','Textbook Adoption');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.text.book.adopted','I have adopted this textbook for my course');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.text.book.reviewing','I am reviewing this textbook for potential course adoption');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.fullname','Full Name');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.fullname','Full Name');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.enrolmentyear','Enrolment year');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.enrolmentyear','Enrolment year');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.graduationyear','Graduation year');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.graduationyear','Graduation year');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.coursename','Course name');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.coursename','Course name');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.moduletitle','Module title');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.moduletitle','Module title');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.lecturername','Lecturer name');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.lecturername','Lecturer name');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.telephone.type.mobile','Mobile');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.telephone.type.institution','Institution');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.telephone.type.home','Home');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.postgraduate','Postgraduate');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.header.registration.please.note','Please note');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.registration.please.note','Your email address must be correct, as this is how we will contact you to confirm your subscription. For lecturers it should end in .ac.uk, .edu, or similar, not hotmail.com, aol.com, etc.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'title.registration.please.note','Your email address must be correct, as this is how we will contact you to confirm your subscription. For lecturers it should end in .ac.uk, .edu, or similar, not hotmail.com, aol.com, etc.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.orcs.tandc','I have read and agreed to the Terms and Conditions of use for OUP Online Resource Centres');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.orcs.tandc','Terms and Conditions acceptance');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.orcs.tandc.link','Read our Terms and Conditions and Privacy Policy');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.orcs.tandc.link','Read our Terms and Conditions and Privacy Policy');
GO
 
-- ACCOUNT REG 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('e13a8201-bf7e-4c44-b3b0-32b73dc60a36', 0, 'ORCS Account Registration','title.accountregistration', 'ACCOUNT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
GO
-- TERMS AND CONDITIONS
insert into component (id,obj_version,label_key) values ('70755604-3a35-4a90-acff-127bb68767cc', 0, 'label.registration.tandc.header');
GO
-- PLEASE NOTE
insert into component (id,obj_version,label_key) values ('9619c39c-d9fa-4e51-b19f-cdf4a00c81d2', 0, 'label.header.registration.please.note');
GO

-- PLEASE NOTE 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9619c39c-d9fa-4e51-b19f-cdf4a00c81d2', 0, 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36');
GO
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '69323313-B3B4-46C5-A4A2-B501B9583829', 1, 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36');
GO
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '70755604-3a35-4a90-acff-127bb68767cc', 2, 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36');
GO


--ACCOUNT ACCEPT TERMS AND CONDITIONS
insert into field (id, obj_version, sequence, element_id, component_id, required) values (newId(), 0, 0, 'FD84EC92-5401-4C32-8A01-7FD2A3F1BEC7', '70755604-3a35-4a90-acff-127bb68767cc', 1);
GO

--ACCOUNT ACCEPT TERMS AND CONDITIONS LINK
insert into question(id,obj_version,export_name,element_text,product_specific) values ('2a2a8b77-fd14-43a5-ac4f-f1b76e438af2', 0, '', 'label.registration.tandc.link', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('515d1829-257f-4505-9053-154bcfc6b206', 0, '2a2a8b77-fd14-43a5-ac4f-f1b76e438af2', 'title.registration.tandc.link', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 1, '515d1829-257f-4505-9053-154bcfc6b206', '70755604-3a35-4a90-acff-127bb68767cc', null, 0);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url, new_window) values (newId(), 0, 'URLLINK', '', 1, '515d1829-257f-4505-9053-154bcfc6b206', 'privacyAndLegal.htm', 1);
GO

--PLEASE NOTE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('0304a616-39e8-439b-aaaf-59f305b9e99c', 0, '', 'label.registration.please.note', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('7c3d2114-f82b-4d20-acc9-1ee159928076', 0, '0304a616-39e8-439b-aaaf-59f305b9e99c', 'title.registration.please.note', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 1, '7c3d2114-f82b-4d20-acc9-1ee159928076', '9619c39c-d9fa-4e51-b19f-cdf4a00c81d2', null, 0);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url, new_window) values (newId(), 0, 'LABEL', '', 1, '7c3d2114-f82b-4d20-acc9-1ee159928076', null, 1);
GO




--CONCURRENT STUDENT UNLIMITED
insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) values ('d6823fe9-f16f-416b-9b9e-33f9dd39d562', 0, 'CONCURRENT', null, null, -1, -1, null, null);
GO


-- LECTURERS FORM

-- REGITRATION ACTIVATION

insert into registration_activation(activation_type,id,obj_version,validator_email) values ('VALIDATED','efa3f45c-d85f-4aa8-a7f1-5ab0b4da3d60',0,'eacdevadmin@oup.com');
GO

-- LICENCE

insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,begin_on,time_period,unit_type,allowed_usages) values ('bdbdc319-a5ae-4023-b2ab-0950adc94e84', 0, 'ROLLING', null, null, null, null, 'CREATION', 1, 'YEAR',NULL);
GO
-- PAGE DEFINITION

insert into page_definition (id, obj_version, name, title, preamble, page_definition_type, division_id) values ('b0568f60-93f6-42fb-a5d6-3f8e32d5ad63', 0, 'ORCS Lecturer', null, 'label.orcs.lecturer.preamble', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
GO

--COMPONENTS

-- PERSONAL DETAILS
insert into component (id,obj_version,label_key) values ('79e3c04b-8317-4d4a-b1a5-213ecc4973f6', 0, 'label.orcs.personal.details');
GO
-- INSTITUTION DETAILS
insert into component (id,obj_version,label_key) values ('b5931730-97e9-4416-9e09-f88d8e8ff785', 0, 'label.orcs.institution.details');
GO
-- COURSE DETAILS
insert into component (id,obj_version,label_key) values ('3477403f-8768-4340-be96-cabc3938de85', 0, 'label.orcs.course.details');
GO
-- TERMS AND CONDITIONS
insert into component (id,obj_version,label_key) values ('904e00c1-fc30-4891-9d32-399740b2e28e', 0, 'label.registration.tandc.header');
GO

--PAGE COMPONENTS

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '79e3c04b-8317-4d4a-b1a5-213ecc4973f6', 0, 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63');
GO
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'b5931730-97e9-4416-9e09-f88d8e8ff785', 1, 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63');
GO
-- COURSE DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '3477403f-8768-4340-be96-cabc3938de85', 2, 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63');
GO
-- CONTACT INFORMATION
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '69323313-B3B4-46C5-A4A2-B501B9583829', 3, 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63');
GO
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '904e00c1-fc30-4891-9d32-399740b2e28e', 4, 'b0568f60-93f6-42fb-a5d6-3f8e32d5ad63');
GO

--QUESTIONS

-- PERSONAL DETAILS

-- JOB TITLE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('324bd833-91ed-47cf-8da1-9416ca83780a', 0, '[JobTitle]', 'label.job.title', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('eb2e70fe-7272-4113-a4e4-76e4bbe008f0', 0, '324bd833-91ed-47cf-8da1-9416ca83780a', 'title.job.title', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 0, 'eb2e70fe-7272-4113-a4e4-76e4bbe008f0', '79e3c04b-8317-4d4a-b1a5-213ecc4973f6', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'eb2e70fe-7272-4113-a4e4-76e4bbe008f0', '');
GO

-- TELEPHONE CONTACT NUMBER
insert into question(id,obj_version,export_name,element_text,product_specific) values ('36f22b44-a362-43f8-a6b8-5507c590c788', 0, '[TelephoneContactNumber]', 'label.telephone.contact.number', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('b8b9f080-4cc9-4b7e-b5cb-86d4b357411d', 0, '36f22b44-a362-43f8-a6b8-5507c590c788', 'title.telephone.contact.number', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 1, 'b8b9f080-4cc9-4b7e-b5cb-86d4b357411d', '79e3c04b-8317-4d4a-b1a5-213ecc4973f6', null, 0);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'b8b9f080-4cc9-4b7e-b5cb-86d4b357411d', '');
GO

-- TELEPHONE NUMBER TYPE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('c23ffede-5e52-4bfa-909e-57d1bd26f33c', 0, '[TelephoneContactType]', 'label.telephone.number.type', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('c0c443f3-c585-403b-8a8a-844c28c1e2f0', 0, 'c23ffede-5e52-4bfa-909e-57d1bd26f33c', 'title.telephone.number.type', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 2, 'c0c443f3-c585-403b-8a8a-844c28c1e2f0', '79e3c04b-8317-4d4a-b1a5-213ecc4973f6', 'institution', 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('c19643e5-1d7c-497c-948e-4ae0e6c472a4', 0, 'SELECT', '', 0, 'c0c443f3-c585-403b-8a8a-844c28c1e2f0', '');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.telephone.type.mobile', 0, 'mobile', 'c19643e5-1d7c-497c-948e-4ae0e6c472a4');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.telephone.type.institution', 1, 'institution', 'c19643e5-1d7c-497c-948e-4ae0e6c472a4');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.telephone.type.home', 2, 'home', 'c19643e5-1d7c-497c-948e-4ae0e6c472a4');
GO

-- INSTITION DETAILS

-- SCHOOL/UNIVERSITY NAME
insert into question(id,obj_version,export_name,element_text,product_specific) values ('33164b0d-f6f2-4714-b005-bbf8691b41b5', 0, '[Institution]', 'label.school.uni.name', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('5a3a9d7f-7c84-489c-8baa-aad739222395', 0, '33164b0d-f6f2-4714-b005-bbf8691b41b5', 'title.school.uni.name', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 0, '5a3a9d7f-7c84-489c-8baa-aad739222395', 'b5931730-97e9-4416-9e09-f88d8e8ff785', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '5a3a9d7f-7c84-489c-8baa-aad739222395', '');
GO

-- DEPARTMENT
insert into question(id,obj_version,export_name,element_text,product_specific) values ('3215462e-1986-4e22-86aa-212478c4d594', 0, '[Department]', 'label.department', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('19cd370e-94d1-4947-ab01-60e62482a43a', 0, '3215462e-1986-4e22-86aa-212478c4d594', 'title.department', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 1, '19cd370e-94d1-4947-ab01-60e62482a43a', 'b5931730-97e9-4416-9e09-f88d8e8ff785', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '19cd370e-94d1-4947-ab01-60e62482a43a', '');
GO

-- SCHOOL ADDRESS LINE 1
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 2, 'D88D4086-4D8B-4C4B-B906-E3ED57B5E3AD', 'b5931730-97e9-4416-9e09-f88d8e8ff785', null, 1);
GO

-- SCHOOL ADDRESS LINE 2
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 3, '0955FFB7-D9B8-4575-A14F-9F9355282860', 'b5931730-97e9-4416-9e09-f88d8e8ff785', null, 0);
GO

-- SCHOOL ADDRESS LINE 3
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 4, '29C447D8-33AE-46F9-90BE-BC6730DB6A48', 'b5931730-97e9-4416-9e09-f88d8e8ff785', null, 0);
GO

-- SCHOOL TOWN CITY
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 5, 'E5097EF5-BF16-4706-818E-7850E8DF2B40', 'b5931730-97e9-4416-9e09-f88d8e8ff785', null, 1);
GO

-- SCHOOL POSTAL CODE
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 6, '34A847B6-BF70-4176-BF9F-C39A34B39B17', 'b5931730-97e9-4416-9e09-f88d8e8ff785', null, 1);
GO

-- SCHOOL COUNTRY
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 7, 'E33B969E-E2A8-4D92-A996-4537DB64B209', 'b5931730-97e9-4416-9e09-f88d8e8ff785', 'gb', 1);
GO

-- COURSE DETAILS

-- TITLE OF MODULE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('1a5d9861-779e-4f26-8091-161db1d6d8b7', 0, '[TitleOfModule]', 'label.module.title', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('1a00ae1d-7ada-4a97-9c5f-44f56747ec3b', 0, '1a5d9861-779e-4f26-8091-161db1d6d8b7', 'title.module.title', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 0, '1a00ae1d-7ada-4a97-9c5f-44f56747ec3b', '3477403f-8768-4340-be96-cabc3938de85', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '1a00ae1d-7ada-4a97-9c5f-44f56747ec3b', '');
GO

-- LEVEL
insert into question(id,obj_version,export_name,element_text,product_specific) values ('5081f0e7-303e-49d1-96a5-70bfbbdf72fd', 0, '[Level]', 'label.level', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('d0450337-2fe4-4a73-b846-365f505a9bf9', 0, '5081f0e7-303e-49d1-96a5-70bfbbdf72fd', 'title.level', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 1, 'd0450337-2fe4-4a73-b846-365f505a9bf9', '3477403f-8768-4340-be96-cabc3938de85', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('b32a6da9-25c9-4cd5-aac1-e5bae402ad06', 0, 'MULTISELECT', '', 0, 'd0450337-2fe4-4a73-b846-365f505a9bf9', '');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.1st.year.undergraduate', 0, '1st year undergraduate', 'b32a6da9-25c9-4cd5-aac1-e5bae402ad06');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.2nd.year.undergraduate', 1, '2nd year undergraduate', 'b32a6da9-25c9-4cd5-aac1-e5bae402ad06');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.3rd.year.undergraduate', 2, '3rd year undergraduate', 'b32a6da9-25c9-4cd5-aac1-e5bae402ad06');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.4th.year.undergraduate', 3, '4th year undergraduate', 'b32a6da9-25c9-4cd5-aac1-e5bae402ad06');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.5th.year.undergraduate', 4, '5th year undergraduate', 'b32a6da9-25c9-4cd5-aac1-e5bae402ad06');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.6th.year.undergraduate', 5, '6th year undergraduate', 'b32a6da9-25c9-4cd5-aac1-e5bae402ad06');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.postgraduate', 6, 'postgraduate', 'b32a6da9-25c9-4cd5-aac1-e5bae402ad06');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.level.other', 7, 'other', 'b32a6da9-25c9-4cd5-aac1-e5bae402ad06');
GO

-- LEVEL OTHER
insert into question(id,obj_version,export_name,element_text,product_specific) values ('800da845-5534-41a0-aff2-4838c16362fb', 0, '[LevelOther]', 'label.please.specify.other', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('68c58d5d-eeba-4c1f-90ce-6c8415fa44a3', 0, '800da845-5534-41a0-aff2-4838c16362fb', 'title.please.specify.other', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 2, '68c58d5d-eeba-4c1f-90ce-6c8415fa44a3', '3477403f-8768-4340-be96-cabc3938de85', null, 0);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '68c58d5d-eeba-4c1f-90ce-6c8415fa44a3', '');
GO

-- NO OF STUDENTS
insert into question(id,obj_version,export_name,element_text,product_specific) values ('ad80e829-2ae1-4467-8f7b-e8aa306b56b0', 0, '[NumberOfStudents]', 'label.number.of.students', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('a1279ee5-e846-48ef-96d1-bda18ef689f7', 0, 'ad80e829-2ae1-4467-8f7b-e8aa306b56b0', 'title.number.of.students', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 3, 'a1279ee5-e846-48ef-96d1-bda18ef689f7', '3477403f-8768-4340-be96-cabc3938de85', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'a1279ee5-e846-48ef-96d1-bda18ef689f7', '');
GO

-- TEXTBOOK PREVIOUSLY USED
insert into question(id,obj_version,export_name,element_text,product_specific) values ('bf12d4b7-a648-4277-8e12-0fdcd5dcdc65', 0, '[TextBookPreviouslyUsed]', 'label.textbook.used', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('cfa3ec3c-f777-424d-8b4a-cdd9a8602671', 0, 'bf12d4b7-a648-4277-8e12-0fdcd5dcdc65', 'title.textbook.used', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 4, 'cfa3ec3c-f777-424d-8b4a-cdd9a8602671', '3477403f-8768-4340-be96-cabc3938de85', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'cfa3ec3c-f777-424d-8b4a-cdd9a8602671', '');
GO

-- WHERE DID YOU HEAR ABOUT?
insert into question(id,obj_version,export_name,element_text,product_specific) values ('8bdaecc0-0a3c-4c6f-930e-0728ccf1cf48', 0, '[HearAbout]', 'label.hear.about', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('3223-2a0e-4ff5-af15-7831bf1a2196', 0, '8bdaecc0-0a3c-4c6f-930e-0728ccf1cf48', 'title.hear.about', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 5, '3223-2a0e-4ff5-af15-7831bf1a2196', '3477403f-8768-4340-be96-cabc3938de85', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('80a69d44-f35e-459c-9f04-69ffdc0763f7', 0, 'SELECT', '', 1, '3223-2a0e-4ff5-af15-7831bf1a2196', '');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.textbook.cover', 0, 'textbook cover', '80a69d44-f35e-459c-9f04-69ffdc0763f7');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.rep', 1, 'rep', '80a69d44-f35e-459c-9f04-69ffdc0763f7');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.direct.mail', 2, 'direct mail', '80a69d44-f35e-459c-9f04-69ffdc0763f7');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.email.promotion', 3, 'email promotion', '80a69d44-f35e-459c-9f04-69ffdc0763f7');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.website', 4, 'website', '80a69d44-f35e-459c-9f04-69ffdc0763f7');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.other', 5, 'other', '80a69d44-f35e-459c-9f04-69ffdc0763f7');
GO

-- HEAR ABOUT OTHER
insert into question(id,obj_version,export_name,element_text,product_specific) values ('6e979582-ed2f-4407-b35c-66427e986721', 0, '[HearAboutOther]', 'label.please.specify.other', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('24c92677-a895-461d-9bb3-58e1cb46ced5', 0, '6e979582-ed2f-4407-b35c-66427e986721', 'title.please.specify.other', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 6, '24c92677-a895-461d-9bb3-58e1cb46ced5', '3477403f-8768-4340-be96-cabc3938de85', null, 0);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '24c92677-a895-461d-9bb3-58e1cb46ced5', '');
GO

--BOOK ADOTION
insert into question(id,obj_version,export_name,element_text,product_specific) values ('0138bf09-3d21-413c-90b9-bff8d297fd66', 0, '[ORCSBookAdoption]', 'label.text.book.adoption', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('ff8d365b-068c-40d8-b3af-2dd055f6b0bf', 0, '0138bf09-3d21-413c-90b9-bff8d297fd66', 'title.text.book.adoption', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 7, 'ff8d365b-068c-40d8-b3af-2dd055f6b0bf', '3477403f-8768-4340-be96-cabc3938de85', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('9cadeece-b058-412a-b55e-caebd2c03e7b', 0, 'RADIO', '', 0, 'ff8d365b-068c-40d8-b3af-2dd055f6b0bf', '');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.text.book.adopted', 1, 'adopted', '9cadeece-b058-412a-b55e-caebd2c03e7b');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.text.book.reviewing', 2, 'reviewing', '9cadeece-b058-412a-b55e-caebd2c03e7b');
GO

--ORCS ACCEPT TERMS AND CONDITIONS
insert into question(id,obj_version,export_name,element_text,product_specific) values ('97cdf944-3df2-479e-8a5d-6e59e435be3c', 0, '[TANDC]', 'label.registration.orcs.tandc', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('354f84e3-7d61-4d36-ab0d-f43d47ce53e4', 0, '97cdf944-3df2-479e-8a5d-6e59e435be3c', 'title.registration.orcs.tandc', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 0, '354f84e3-7d61-4d36-ab0d-f43d47ce53e4', '904e00c1-fc30-4891-9d32-399740b2e28e', null, 1);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'CHECKBOX', '', 0, '354f84e3-7d61-4d36-ab0d-f43d47ce53e4', '');
GO

--ORCS ACCEPT TERMS AND CONDITIONS LINK
insert into question(id,obj_version,export_name,element_text,product_specific) values ('7fa7c2dc-8113-4ea0-8120-60f1f9f2d370', 0, '[TANDCLINK]', 'label.registration.orcs.tandc.link', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('d02c6661-2112-46e2-8858-bf6265870346', 0, '7fa7c2dc-8113-4ea0-8120-60f1f9f2d370', 'title.registration.orcs.tandc.link', '', '');
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 1, 'd02c6661-2112-46e2-8858-bf6265870346', '904e00c1-fc30-4891-9d32-399740b2e28e', '', 0);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url, new_window) values (newId(), 0, 'URLLINK', '', 0, 'd02c6661-2112-46e2-8858-bf6265870346', 'http://www.oup.com/uk/orc/terms/', 1);
GO


-- STAFF FORM

-- LICENCE

insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) values ('84888c71-7efb-4b47-8765-4b9b717027ec', 0, 'CONCURRENT', null, null, 1, 1, null, null);
GO

-- PAGE DEFINITION

insert into page_definition (id, obj_version, name, title, preamble, page_definition_type, division_id) values ('c909c3b8-1b96-4dc3-90ae-494efd981b3b', 0, 'ORCS Staff', null, 'label.orcs.staff.preamble', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
GO

--COMPONENTS

-- EMPLOYEE DETAILS
insert into component (id,obj_version,label_key) values ('dc97f5ea-f962-4ea1-ad56-ff60ee2d6876', 0, 'label.orcs.employee.details');
GO

--PAGE COMPONENTS

-- EMPLOYEE DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'dc97f5ea-f962-4ea1-ad56-ff60ee2d6876', 0, 'c909c3b8-1b96-4dc3-90ae-494efd981b3b');
GO
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '904e00c1-fc30-4891-9d32-399740b2e28e', 1, 'c909c3b8-1b96-4dc3-90ae-494efd981b3b');
GO

--QUESTIONS

-- EMPLOYEE DETAILS

-- FULL NAME
insert into question(id,obj_version,export_name,element_text,product_specific) values ('8a27e0d9-840f-4f4e-a911-1454cebb8ec6', 0, '[StaffFullName]', 'label.fullname', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('e91627c8-676d-461f-a3f0-0d0c10dbe629', 0, '8a27e0d9-840f-4f4e-a911-1454cebb8ec6', 'title.fullname', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 1, 'e91627c8-676d-461f-a3f0-0d0c10dbe629', 'dc97f5ea-f962-4ea1-ad56-ff60ee2d6876', null, 1);                                                                                          
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'e91627c8-676d-461f-a3f0-0d0c10dbe629', '');
GO

-- JOB TITLE
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 2, 'eb2e70fe-7272-4113-a4e4-76e4bbe008f0', 'dc97f5ea-f962-4ea1-ad56-ff60ee2d6876', null, 1);
GO
-- DEPARTMENT
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 3, '19cd370e-94d1-4947-ab01-60e62482a43a', 'dc97f5ea-f962-4ea1-ad56-ff60ee2d6876', null, 1);
GO


insert into asset(id, obj_version, erights_id, product_name, division_id) values ('b3437e99-99be-4a75-90d0-83f3776abf89', 0, 54321, 'ORCS Admin Product', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
GO

insert into asset(id, obj_version, erights_id, product_name, division_id) values ('6fa75e17-9ac5-4a81-b6a2-b6b8f807ca4d', 0, 12345, 'ORCS Unlimited Access', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
GO

-- Product
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) 
values ('REGISTERABLE', '985eb4ed-6b7f-45c1-85e0-41059cc930a11', 0, 'b3437e99-99be-4a75-90d0-83f3776abf89', '', null, 'SELF_REGISTERABLE', 'orc.help@oup.com', '30 minutes', 'http://orc.uat.oup.com');
GO
--Linked all content
insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, activation_method, home_page) 
values ('LINKED', '7b082b45-f5e9-42d6-8e39-28f3539a92bb', 0, '6fa75e17-9ac5-4a81-b6a2-b6b8f807ca4d', '', '985eb4ed-6b7f-45c1-85e0-41059cc930a11', 'SELF_REGISTERABLE', 'orc.help@oup.com', '30 minutes', 'POST_PARENT', 'http://orc.uat.oup.com');
GO

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('PRODUCT_REGISTRATION', 'be910969-c464-447e-b214-c42018156dbb', 0, '985eb4ed-6b7f-45c1-85e0-41059cc930a11', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'c909c3b8-1b96-4dc3-90ae-494efd981b3b', '84888c71-7efb-4b47-8765-4b9b717027ec');
GO

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, '985eb4ed-6b7f-45c1-85e0-41059cc930a11', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', 'e13a8201-bf7e-4c44-b3b0-32b73dc60a36', null);
GO



-- STUDENT FORM

-- LICENCE

insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,begin_on,time_period,unit_type,allowed_usages) values ('a5ce2f11-c712-4b76-b2ec-87b717fc2d62', 0, 'ROLLING', null, null, null, null, 'CREATION', 3, 'YEAR',NULL);
GO

-- PAGE DEFINITION

insert into page_definition (id, obj_version, name, title, preamble, page_definition_type, division_id) values ('cfea91e0-c68a-4614-b674-dacb7cfeb8a7', 0, 'ORCS Student', null, null, 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25e16');
GO

--COMPONENTS

-- PERSONAL DETAILS
insert into component (id,obj_version,label_key) values ('06a055f6-25ab-41a5-bb18-8ee1374e6b15', 0, 'label.orcs.personal.details');
GO
-- COURSE DETAILS
insert into component (id,obj_version,label_key) values ('b40d6642-31ec-486c-875d-17a811c1eb07', 0, 'label.orcs.course.details');
GO


--PAGE COMPONENTS

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '06a055f6-25ab-41a5-bb18-8ee1374e6b15', 0, 'cfea91e0-c68a-4614-b674-dacb7cfeb8a7');
GO
-- COURSE DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'b40d6642-31ec-486c-875d-17a811c1eb07', 2, 'cfea91e0-c68a-4614-b674-dacb7cfeb8a7');
GO
-- CONTACT INFORMATION
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '69323313-B3B4-46C5-A4A2-B501B9583829', 3, 'cfea91e0-c68a-4614-b674-dacb7cfeb8a7');
GO
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '904e00c1-fc30-4891-9d32-399740b2e28e', 4, 'cfea91e0-c68a-4614-b674-dacb7cfeb8a7');
GO


--QUESTIONS

-- PERSONAL DETAILS

-- SCHOOL/UNIVERSITY NAME
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 0, '5a3a9d7f-7c84-489c-8baa-aad739222395', '06a055f6-25ab-41a5-bb18-8ee1374e6b15', null, 1);
GO
-- DEPARTMENT
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 1, '19cd370e-94d1-4947-ab01-60e62482a43a', '06a055f6-25ab-41a5-bb18-8ee1374e6b15', null, 1);
GO
-- SCHOOL TOWN CITY
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 2, 'E5097EF5-BF16-4706-818E-7850E8DF2B40', '06a055f6-25ab-41a5-bb18-8ee1374e6b15', null, 1);
GO
-- SCHOOL COUNTRY
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 3, 'E33B969E-E2A8-4D92-A996-4537DB64B209', '06a055f6-25ab-41a5-bb18-8ee1374e6b15', 'gb', 1);
GO

-- ENROLEMENT YEAR
insert into question(id,obj_version,export_name,element_text,product_specific) values ('8ca91737-1772-4cbc-827f-0ed5da9406c9', 0, '[EnrolmentYear]', 'label.enrolmentyear', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('1ccb902d-eb32-412d-b55e-e342e82973a2', 0, '8ca91737-1772-4cbc-827f-0ed5da9406c9', 'title.enrolmentyear', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 4, '1ccb902d-eb32-412d-b55e-e342e82973a2', '06a055f6-25ab-41a5-bb18-8ee1374e6b15', null, 1);                                                                                          
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '1ccb902d-eb32-412d-b55e-e342e82973a2', '');
GO

-- GRADUATION YEAR
insert into question(id,obj_version,export_name,element_text,product_specific) values ('45fd4a6e-fa59-451d-b335-5d608e53c62d', 0, '[GraduationYear]', 'label.graduationyear', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('2d54cf0c-d826-4db5-b0d3-ae8153a21aff', 0, '45fd4a6e-fa59-451d-b335-5d608e53c62d', 'title.graduationyear', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 5, '2d54cf0c-d826-4db5-b0d3-ae8153a21aff', '06a055f6-25ab-41a5-bb18-8ee1374e6b15', null, 1);                                                                                          
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '2d54cf0c-d826-4db5-b0d3-ae8153a21aff', '');
GO

-- COURSE DETAILS

-- COURSE NAME
insert into question(id,obj_version,export_name,element_text,product_specific) values ('87534d47-1087-40f2-a3f0-b58d33feb47c', 0, '[CourseName]', 'label.coursename', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('57052807-9f43-4278-858a-cded6c0408cf', 0, '87534d47-1087-40f2-a3f0-b58d33feb47c', 'title.coursename', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 1, '57052807-9f43-4278-858a-cded6c0408cf', 'b40d6642-31ec-486c-875d-17a811c1eb07', null, 0);                                                                                          
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '57052807-9f43-4278-858a-cded6c0408cf', '');
GO

-- MODULE TITLE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('33d70685-0135-4b29-adc3-ab7ab61b5d3b', 0, '[ModuleTitle]', 'label.moduletitle', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('618dffba-425c-4fab-9055-db61f5a4fc01', 0, '33d70685-0135-4b29-adc3-ab7ab61b5d3b', 'title.moduletitle', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 2, '618dffba-425c-4fab-9055-db61f5a4fc01', 'b40d6642-31ec-486c-875d-17a811c1eb07', null, 0);                                                                                          
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '618dffba-425c-4fab-9055-db61f5a4fc01', '');
GO

-- LECTURER NAME
insert into question(id,obj_version,export_name,element_text,product_specific) values ('130b0a87-5159-48e8-9856-94d0361516fa', 0, '[LecturerName]', 'label.lecturername', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('35433a30-fc58-4039-91ac-60f9b2bc5de2', 0, '130b0a87-5159-48e8-9856-94d0361516fa', 'title.lecturername', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 3, '35433a30-fc58-4039-91ac-60f9b2bc5de2', 'b40d6642-31ec-486c-875d-17a811c1eb07', null, 0);                                                                                          
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '35433a30-fc58-4039-91ac-60f9b2bc5de2', '');
GO

-- WHERE DID YOU HEAR ABOUT?
insert into question(id,obj_version,export_name,element_text,product_specific) values ('a9f5f118-81fd-4e25-aa42-bc6de62883ef', 0, '[HearAbout]', 'label.hear.about', 0);
GO
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('05b9b450-6d11-4601-be44-ef534dc8aa62', 0, 'a9f5f118-81fd-4e25-aa42-bc6de62883ef', 'title.hear.about', null, null);
GO
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 4, '05b9b450-6d11-4601-be44-ef534dc8aa62', 'b40d6642-31ec-486c-875d-17a811c1eb07', null, 0);
GO
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('287895ab-395e-4e6c-8f5d-c887ff685464', 0, 'SELECT', '', 1, '05b9b450-6d11-4601-be44-ef534dc8aa62', '');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.textbook.cover', 0, 'textbook cover', '287895ab-395e-4e6c-8f5d-c887ff685464');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.lecturer.recommendation', 1, 'lecturer recommendation', '287895ab-395e-4e6c-8f5d-c887ff685464');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.oup.rep', 2, 'oup rep', '287895ab-395e-4e6c-8f5d-c887ff685464');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.promotional.material', 3, 'promotional material', '287895ab-395e-4e6c-8f5d-c887ff685464');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.website', 4, 'website', '287895ab-395e-4e6c-8f5d-c887ff685464');
GO
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.hear.about.other', 5, 'other', '287895ab-395e-4e6c-8f5d-c887ff685464');
GO

-- HEAR ABOUT OTHER
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 5, '24c92677-a895-461d-9bb3-58e1cb46ced5', 'b40d6642-31ec-486c-875d-17a811c1eb07', null, 0);
GO



