--Fixing eright
DECLARE  @fixDummyErightsId int;
DECLARE  @fixDummyTempId  nvarchar(255);

SET @fixDummyErightsId = 666666;

DECLARE Fix_Dummy_Cursor CURSOR FOR select id from product where erights_id = 999999;
OPEN Fix_Dummy_Cursor;
FETCH Fix_Dummy_Cursor INTO @fixDummyTempId;
WHILE @@Fetch_Status = 0
BEGIN
	update product set erights_id = @fixDummyErightsId where id = @fixDummyTempId;
	SET @fixDummyErightsId = @fixDummyErightsId + 1;
	FETCH Fix_Dummy_Cursor INTO @fixDummyTempId;      
END;
CLOSE Fix_Dummy_Cursor;
DEALLOCATE Fix_Dummy_Cursor;

--Teachers club landing page needs updating on DEV
update product set landing_page = 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release3/elt/teachers-club.jsp' where product_name like 'Oxford Teachers%Club'
and db_name() = 'eactest';

--mantis 8582
update message set message='We''''re sorry. You no longer have access to these resources. If you wish to continue to use these resources you will need to' where [key]='label.noactivelicence'

--mantis 12170
update message set message='Last Name' where [key]='label.familyname';
update message set message='Last Name' where [key]='title.familyname';

--mantis 12161
declare @passwordText nvarchar(1000)
set @passwordText='Your password must be from 6 to 15 characters long and it must contain at least one lower case character, one upper case character and at least one digit.';

update message set message=@passwordText where [key]='info.password.strength';
update message set message=@passwordText where [key]='error.password.strength';

DECLARE  @assetErightsId int;
DECLARE  @assetProductName nvarchar(255);
DECLARE  @assetDivisionId nvarchar(255);
DECLARE  @assetNewId  nvarchar(255);
DECLARE  @assetTempId  nvarchar(255);

DECLARE Product_Cursor CURSOR FOR select distinct (erights_id) from product;
OPEN Product_Cursor;
FETCH Product_Cursor INTO @assetErightsId;
WHILE @@Fetch_Status = 0
BEGIN
	SET @assetNewId = NEWID();
	SET @assetTempId = '';
	select top 1 @assetTempId = id from product where erights_id = @assetErightsId and product_type = 'REGISTERABLE';
	if @assetTempId = '' 
	BEGIN
		select top 1 @assetTempId = id from product where erights_id = @assetErightsId and product_type = 'LINKED';
	END;
	select @assetProductName = product_name, @assetDivisionId = division_id from product where id = @assetTempId;
	insert into asset(id, obj_version, erights_id, product_name, division_id) values(@assetNewId, 0, @assetErightsId, @assetProductName, @assetDivisionId);
	update product set asset_id = @assetNewId where erights_id = @assetErightsId;
	FETCH Product_Cursor INTO @assetErightsId;      
END;
CLOSE Product_Cursor;
DEALLOCATE Product_Cursor;

update product set registerable_type = 'SELF_REGISTERABLE' where product_type = 'REGISTERABLE';

alter table asset add constraint FKED8DCCEFBDF051EC foreign key (division_id) references division;
    
drop index erights_id_idx on product;

alter table product drop column erights_id;

alter table product drop column division_id;

alter table product drop column product_name;

alter table product alter column asset_id nvarchar(255) not null;


insert into asset(id,obj_version,erights_id, product_name, division_id) values('b495459c-4994-439b-a014-46dbcf1264fa', 0, 137055, 'Admin Product', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');

insert into product (product_type, id ,obj_version, asset_id, landing_page, registerable_product_id, email, service_level_agreement) 
values ('REGISTERABLE','971af4e9-1622-46ed-bd9d-dcf8c381fe15', 0, 'b495459c-4994-439b-a014-46dbcf1264fa', '', null, 'eacsystemadmin@oup.com', '30 minutes');

insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('PRODUCT_REGISTRATION', 'bc4eafba-e78e-4a48-a94e-551de2e05a11', 0, '971af4e9-1622-46ed-bd9d-dcf8c381fe15', '119452A7-07BB-4B13-A334-BF44D04EAF57', '8992DD30-D16F-405F-B2C0-7A492491EEC4', '7F6379BD-068B-4455-A3DD-41D878F831DD');


insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, '971af4e9-1622-46ed-bd9d-dcf8c381fe15', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);

delete from message where [key] = 'label.activatelicenceemail' and basename = 'messages' and country is null;
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activatelicenceemail_1','If your activation email does not arrive within {0} please look in your spam or junk mail folder. If you can''''t find the activation email and need us to send you a new one, please ');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activatelicenceemail_2','click here');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activatelicenceemail_3','. You may need to make sure that do_not_reply@oup.com is on your list of allowed email addresses.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activatelicenceemail_4','If you still have problems receiving the activation email please contact ');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.resendingEmail','Resending email...');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.out.of.synch.line1','You have attempted to submit a form out of sequence.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.out.of.synch.line2','Click <a href="{0}">here</a> to re-display the form with fresh data.');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.password.reset.denied', 'Unfortunately we cannot reset your password as you have a shared account. If you are experiencing difficulties logging in, please contact Customer Support.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.password.reset.denied', 'There was a problem during a Password Reset attempt. Please contact the System Administrator.');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.change.password.denied', 'Change Password Denied');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.change.password.denied', 'Unfortunately we cannot change your password as you have a shared account.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.change.password.denied.continue', 'Click <a href="{0}">here</a> to continue.');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.amend.registrations.denied', 'Amend Registrations Denied');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.amend.registrations.denied', 'Unfortunately we cannot amend your registrations as you have a shared account.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.amend.registrations.denied.continue', 'Click <a href="{0}">here</a> to continue.');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.productregistration.read.only', 'Product Registration Details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.productregistration.read.only.info', '( Read Only )');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.title.form.read.only', '( Read Only )');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.mobile', 'Mobile');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.mobile', 'Mobile');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.kindergarten', 'Private Language School - Kindergarten & Primary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.secondary', 'Private Language School - Secondary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.adult', 'Private Language School - Adult');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.elementary', 'Elementary School');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.middle', 'Middle School');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.high', 'High School');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.university', 'University');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.preservice', 'Pre-service Teachers');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.home.schooling', 'Home School/Tutoring');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.korean.after.school', 'After School Program');

insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, 'B0D11F96-8B2A-4CD5-BCAD-28B83C5BBB9A', 'ko');

-- KOREAN INSTITUTION TYPE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('8620cb2a-dd6e-4559-b4fc-c5045f577330', 0, '[InstitutionTypeKR]', 'label.institutiontype', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('973c55f3-83f9-4ec3-b3c0-e4f01870718f', 0, '8620cb2a-dd6e-4559-b4fc-c5045f577330', 'title.institutiontype', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 13, '973c55f3-83f9-4ec3-b3c0-e4f01870718f', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('4022641c-753d-4314-9895-bb11d1e96661', 0, 'SELECT', '', 1, '973c55f3-83f9-4ec3-b3c0-e4f01870718f', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.kindergarten', 0, 'korean.inst.kindergarten', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.secondary', 1, 'korean.inst.secondary', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.adult', 2, 'korean.inst.adult', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.elementary', 3, 'korean.inst.elementary', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.middle', 4, 'korean.inst.middle', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.high', 5, 'korean.inst.high', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.university', 6, 'korean.inst.university', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.preservice', 7, 'korean.inst.preservice', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.home.schooling', 8, 'korean.inst.home.schooling', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.korean.after.school', 9, 'korean.inst.after.school', '4022641c-753d-4314-9895-bb11d1e96661');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.other', 10, 'other', '4022641c-753d-4314-9895-bb11d1e96661');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, '973c55f3-83f9-4ec3-b3c0-e4f01870718f', 'ko');

-- MOBILE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('bc665536-7fed-4a06-b359-5a39e23a8d73', 0, '[Mobile]', 'label.mobile', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('00a84972-6cf9-4807-a88b-57e9bb716936', 0, 'bc665536-7fed-4a06-b359-5a39e23a8d73', 'title.mobile', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 14, '00a84972-6cf9-4807-a88b-57e9bb716936', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '00a84972-6cf9-4807-a88b-57e9bb716936', '');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, '00a84972-6cf9-4807-a88b-57e9bb716936', 'ko');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.password.reset', 'Reset password');

-- MANTIS 12421 start
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','de',null,null,'label.login', 'Anmelden');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','fr',null,null,'label.login', 'Identifiez-vous');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','it',null,null,'label.login', 'Entra');
-- MANTIS 12421 end

--1 label.hello translation : Korean is 'Hello' : GOOD
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.hello',N'안녕하세요');

--2 label.login : Korean is 'Login' : GOOD
--insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.login',N'로그인');

--3 label.logout : Korean is 'Log out' : GOOD 
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.logout',N'로그아웃');

--4 label.password : Korean is 'Password' : GOOD
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.password',N'비밀번호');

--5 label.password.title : Korean is 'Password' : BAD - should be 'Password. This is a required field'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.password.title',N'비밀번호');

--6 label.username : Korean is 'ID' : BAD - we want 'username'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.username',N'아이디');

--7 label.username.title : Korean is 'ID' : BAD - we want 'Username. This is a required field'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.username.title',N'아이디');

--8 label.view.profile : Korean is 'View Profile' : GOOD
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.view.profile',N'회원정보 보기'); 

--9 problems.logging.in : Korean is 'Like andoesi login?' : BAD we want 'problems logging in?'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'problems.logging.in',N'로그인이 안되시나요?');

--10 welcome.message : Korean is "Welcome. Under 'View Profile' click Please update your personal information. If you use a shared computer, always log out after use of the yisimyeon please."  : BAD
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'welcome.message',N'환영합니다. 아래 ''''회원정보 보기'''' 클릭하여 개인정보를 업데이트 해주세요. 만약 공유 컴퓨터를 사용 중 이시면 사용이 끝난 후 반드시 로그아웃 해주세요.');

--11 title.resetpassword : Korean is 'Forgot Password' : BAD we want 'Reset Password'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'title.resetpassword',        N'비밀번호 찾기');

--12 label.resetpassword : Korean is 'Forgot Password' : BAD we want 'Reset Password'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.resetpassword',        N'비밀번호 찾기');

--13 submit.resetpassword : Korean is 'Forgot Password' : BAD we want 'Reset Password'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'submit.resetpassword',       N'비밀번호 찾기');

--14 label.password.reset : Korean is 'Forgot Password' : BAD we want 'Reset Password'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'label.password.reset',       N'비밀번호 찾기');

--15 title.resetpasswordsuccess : Korean is 'Forgot Password' : BAD we want 'Reset Password'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ko',null,null,'title.resetpasswordsuccess', N'비밀번호 찾기');


--
--Better Translations for Korean messages
--

--label.username : Korean is 'User name'
update message set message=N'사용자 이름' where language='ko' and [key]='label.username';

--problems.logging.in : Korean is 'Having trouble logging in?'
update message set message=N'로그인하는 데 문제가?' where language='ko' and [key]='problems.logging.in';

--title.resetpassword : Korean is 'Reset Password' [PASSWORD REST FORM-html title]
update message set message=N'비밀 번호 재설정' where language='ko' and [key] = 'title.resetpassword';

--label.resetpassword : Korean is 'Reset Password' [LOGIN FORM-reset password link, PASSWORD RESET FORM-password reset h1, PASSWORD RESET SUCCESS FORM-password reset h1]
update message set message=N'비밀 번호 재설정' where language='ko' and [key] = 'label.resetpassword';

--submit.resetpassword : Korean is 'Reset Password' [TEXT ON RESET PASSWORD FORM SUBMIT BUTTON]
update message set message=N'비밀 번호 재설정' where language='ko' and [key] = 'submit.resetpassword';

--label.password.reset : Korean is 'Reset Password' [LOGIN WIDGET RESET PASSWORD LINK]
update message set message=N'비밀 번호 재설정' where language='ko' and [key] = 'label.password.reset';

--title.resetpasswordsuccess : Korean is 'Password Reset Success' [PASSWORD RESET FORM - html title]
update message set message=N'암호 성공을 재설정' where language='ko' and [key] = 'title.resetpasswordsuccess';

--welcome.message : Korean is "Welcome. To update your details click 'View Profile'. If you are connected to a shared computer, do not forget to logout at the end."
update message set message=
N'환영합니다. 귀하의 세부 정보를 업데이 트하려면 클릭하십시오 ''''프로필보기''''. 당신이 공유 컴퓨터에 연결되어있다면, 마지막에 로그아웃하는 것을 잊지 세요.' 
where language='ko' and [key] ='welcome.message';

--label.password.title : Korean is 'Password. This is a required field.'
update message set message=N'비밀 번호. 이것은 필수 입력란입니다.' where language='ko' and [key]='label.password.title';

--label.username.title : Korean is 'Username. This is a required field.'
update message set message=N'사용자 이름. 이것은 필수 입력란입니다.' where language='ko' and [key]='label.username.title';

