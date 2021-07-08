-- New role for Manage External System functionality in Admin
insert into permission values ('447F3E85-80F4-4FAB-9955-95E01D6C1059', 0, 'MANAGE_EXTERNAL_SYSTEM');
GO
insert into role_permissions values ('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', '447F3E85-80F4-4FAB-9955-95E01D6C1059')
GO

-- New role for Manage Account functionality in Admin
insert into permission values ('5A88B534-2007-4B21-9445-B1144FD69600', 0, 'MANAGE_ACCOUNT');
GO
insert into role_permissions values ('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', '5A88B534-2007-4B21-9445-B1144FD69600');
GO

BEGIN--TAIWAN EXAM INTERESTS (CR007)

declare @componentId nvarchar(255);
declare @questionId nvarchar(255);
declare @elementId nvarchar(255);
declare @fieldId nvarchar(255);
declare @tagId nvarchar(255);
declare @ecrId nvarchar(255);
declare @newId1 nvarchar(255);
declare @newId2 nvarchar(255);
declare @newId3 nvarchar(255);
declare @newId4 nvarchar(255);
declare @newId5 nvarchar(255);
declare @newId6 nvarchar(255);
declare @newId7 nvarchar(255);
declare @newId8 nvarchar(255);
declare @newId9 nvarchar(255);


set @componentId = 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09';--existing value
set @questionId  = '17aa6b62-a4be-43c9-881c-d1098f47532f';--new value
set @elementId   = 'c48b8e7d-77a4-4905-8909-4be89dd111ad';--new value
set @fieldId     = '2b5b807b-f109-45af-bb29-ea0b101e4d5b';--new value
set @tagId       = 'f06b1e0b-b1f9-47ab-9e33-1e3d550cfdce';--new value
set @ecrId       = '22ce8e16-8061-489f-ac9c-a95840a7bd7a';--new value

set @newId1 = 'ee286bd3-4cbb-451d-a2ec-4ba4f2cbdce5';--new value
set @newId2 = 'c1bf89eb-a4db-4d5a-8b53-deb2a0730c66';--new value
set @newId3 = '88aa6300-018d-422d-9fa0-5ea2cde0382e';--new value
set @newId4 = 'df8d3dfd-03e9-4e2a-85ac-2e0bfe953f0c';--new value
set @newId5 = '625f3b7a-8945-4348-b92e-1203f38a76e4';--new value
set @newId6 = '9adfc536-6c60-4c77-88d6-bd99e4fbb153';--new value
set @newId7 = '97943bfc-c720-42b9-ae0e-ac44b1afd8ec';--new value
set @newId8 = '9589d3bf-e26f-412b-992b-2782f11f7b3b';--new value
set @newId9 = 'aad9a367-4dbb-4c71-9344-d9add386f199';--new value

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.gept',N'GEPT');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.cambridge.yle',N'Cambridge YLE');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.style.primary.segment',N'STYLE: Primary Segment');

insert into question(id,obj_version,export_name,element_text,product_specific) values (@questionId, 0, '[ExamInterestsTW]', 'label.exam.interests', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values (@elementId, 0, @questionId, 'title.exam.interests', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (@fieldId, 0, 11, @elementId, @componentId, null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (@tagId, 0, 'MULTISELECT', '', 0, @elementId, '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (@newId1, 0, 'label.exam.interest.cambridge.yle',           0, 'kr-cambridge-yle', @tagId);--NEW
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (@newId2, 0, 'label.exam.interest.style.primary.segment',   1, 'kr-style-pri-seg', @tagId);--NEW
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (@newId3, 0, 'label.exam.interest.gept',                    2, 'kr-gept',          @tagId);--NEW
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (@newId4, 0, 'label.exam.interest.cae.fce',                 3, 'kr-cae-fce',       @tagId);
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (@newId5, 0, 'label.exam.interest.ielts',                   4, 'kr-ielts',         @tagId);
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (@newId6, 0, 'label.exam.interest.tofel',                   5, 'kr-tofel',         @tagId);
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (@newId7, 0, 'label.exam.interest.toeic.listening.reading', 6, 'kr-toeic-listening-reading', @tagId);
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (@newId8, 0, 'label.exam.interest.toeic.speaking.writing',  7, 'kr-toeic-speaking-writing',  @tagId);
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (@newId9, 0, 'label.exam.interest.other',                   8, 'kr-other', @tagId);
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, @elementId, 'zh_TW');

END--TAIWAN EXAM INTERESTS (CR007)
GO

BEGIN --DISPLAY PRODUCT NAMES ON PRODUCT REGISTRATION PAGES -- CR008
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.productregistration.create.sign.up',N'Sign up for');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.productregistration.update.sign.up',N'Edit sign up data for');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.productregistration.read.only.sign.up',N'Sign up data for');
END   --DISPLAY PRODUCT NAMES ON PRODUCT REGISTRATION PAGES -- CR008
GO

update url_skin set skin_path = '../eacSkin/' + skin_path;
GO

BEGIN--OFCW HE LECTURER : COURSE CODE (CR009)

declare @componentId nvarchar(255);
declare @questionId nvarchar(255);
declare @elementId nvarchar(255);
declare @fieldId nvarchar(255);
declare @tagId nvarchar(255);
declare @ecrId nvarchar(255);
declare @newId1 nvarchar(255);
declare @newId2 nvarchar(255);
declare @newId3 nvarchar(255);
declare @newId4 nvarchar(255);
declare @newId5 nvarchar(255);
declare @newId6 nvarchar(255);
declare @newId7 nvarchar(255);
declare @newId8 nvarchar(255);
declare @newId9 nvarchar(255);


set @componentId = '49CEF46A-58AA-4EE5-8F7B-BC26BF80243F';--existing value
set @questionId  = '36fd8cb0-3dfe-11e2-a25f-0800200c9a66';--new value
set @elementId   = '36fd8cb1-3dfe-11e2-a25f-0800200c9a66';--new value
set @fieldId     = '36fd8cb2-3dfe-11e2-a25f-0800200c9a66';--new value
set @tagId       = '36fd8cb3-3dfe-11e2-a25f-0800200c9a66';--new value



insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.course.code',N'Course Code');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.course.code',N'Course Code');

insert into question(id,obj_version,export_name,element_text,product_specific) values (@questionId, 0, 'course.code', 'label.course.code', 1);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values (@elementId, 0, @questionId, 'title.course.code', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (@fieldId, 0, 2, @elementId, @componentId, null, 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (@tagId, 0, 'TEXTFIELD', '', 0, @elementId, '');

END--OFCW HE LECTURER : COURSE CODE (CR009)
GO

-- Messages for reset admin password - EAC-256
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.admin.password.reset', 'Your request to reset your password has been accepted. Please click the link below and change your password.');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.admin.password.pleaseNote', 'Please note that this link is valid for 8 hours only.');
GO

-- New role for Manage External System functionality in Admin
insert into permission values ('93B0C289-624E-4C57-8B07-9FE38FBA5EEE', 0, 'MANAGE_ROLES');
GO
insert into role_permissions values ('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', '93B0C289-624E-4C57-8B07-9FE38FBA5EEE')
GO

--Removing all answers where the answer is either null or empty - EAC-258
delete from answer where answer_text is null or answer_text = '';
GO

BEGIN-- updated messages for emails

--1. for Email Address Validation : emailValidation.vm
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.register.thanks', 'Thank you for registering with Oxford University Press.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.please.ignore'  , 'If you didn''''t register for an account with Oxford University Press, please ignore this e-mail.');
update message set message='To complete registration, please click on the following link:' where [key]='label.validateemail' and language is null;
--2. for product registration : productRegistration.vm
update message set message='To complete registration please click on the link below:' where [key]='label.complete' and language is null;
--3. for licence activated : licenceActivated.vm
update message set message='We are pleased to inform you that your request has been accepted.' where [key]='label.regprocess' and language is null;
--4. for validatedRegistation.vm
--5. for licenceAccepted.vm [actually licenceDetails.vm]
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.comments'  , 'Please send any comments or feedback to');
update message set message='We are pleased to inform you that your request has been accepted.' where [key]='label.regaccepted' and language is null;
--6. for licenceDenied.vm
update message set message='We regret that we cannot approve your access to these resources at this time. We hope you will understand the need to restrict access to these resources to authorized users only.' where [key]='label.unfortunate' and language is null  
--7. for passwordReset.vm
update message set message='We have reset your password. Please log in with your user name and the temporary password below:' where [key]='label.reset' and language is null;
update message set message='Please change your password after you log in.' where [key]='label.password.change' and language is null;
END  -- updated messages for emails
GO


insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.requestProcessing','Your request is being processed. Please wait a few minutes and then try again.');
GO

BEGIN-- updated messages for customizable 'registration not allowed' messages
insert into message (id,basename,language,country,variant,[key],message) values (newId(),'messages',null,null,null,'registration.denied.msg.1','You are logged on with a shared user account that does not permit you to access this resource. Please {3} and then log back in again with your personal username/password. In case of difficulty please contact  {2}.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(),'messages',null,null,null,'registration.denied.msg.2','You are logged on with a shared user account that does not permit you to access this resource. Please check that you have entered the correct username/password for the product you are trying to access. In case of difficulty please contact {2}.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(),'messages',null,null,null,'registration.denied.msg.3','You have attempted to access a resource which does not exist. Please check you have entered the address correctly and {0} if you require any further assistance.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(),'messages',null,null,null,'registration.denied.msg.4','You do not have access this resource. If you think you should have access, please contact {2}.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(),'messages',null,null,null,'registration.denied.msg.5','It is not currently possible to register for {1}.  For more information please contact {2}.');
END-- updated messages for customizable 'registration not allowed' messages
GO