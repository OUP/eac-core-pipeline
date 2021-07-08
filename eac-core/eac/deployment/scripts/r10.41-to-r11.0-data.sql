-- New role for Manage Skins functionality in Admin
insert into permission values ('EB0A5FA1-1AC7-4773-A591-B2977C61B70F', 0, 'MANAGE_SKIN');
GO
insert into role_permissions values ('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', 'EB0A5FA1-1AC7-4773-A591-B2977C61B70F')
GO
update url_skin set primary_site = 1;
GO

-- New role for Manage Languages functionality in Admin
insert into permission values ('173BB13E-B942-4FD2-A4C1-CCACC7C483CF', 0, 'MANAGE_LANGUAGE');
GO
insert into role_permissions values ('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', '173BB13E-B942-4FD2-A4C1-CCACC7C483CF')
GO

update registration_definition set confirmation_email_enabled = 1 where registration_definition_type in ('ACTIVATION_CODE_REGISTRATION', 'PRODUCT_REGISTRATION');
GO

-- Remove references to registration activations from account registration definitions. They're not used, but they cause issues when set.
update registration_definition set registration_activation_id = NULL where registration_definition_type = 'ACCOUNT_REGISTRATION';
GO



-- Fixing Department

-- Updating answers & elements


BEGIN
    declare @questionId nvarchar(255);
    
    select @questionId = id from question where description = 'employee.department' and element_text = 'label.department';
    
    update answer set question_id = @questionId where question_id in (select id from question where element_text = 'label.department' and id != @questionId);
    update element set question_id = @questionId where question_id in (select id from question where element_text = 'label.department' and id != @questionId);
    
    -- Removing unsed questions
    delete from question where id in (select id from question where element_text = 'label.department' and id != @questionId);
    
    -- Creating CMDP export name overide
    insert into export_name(id, obj_version, created_date, export_type, question_id, name) values('45d68731-e61b-40e1-a2ba-ce7baa4a2cc2', 0, getdate(), 'CMDP', @questionId, '[Department]');
END
GO


-- Fixing Job Title

BEGIN
    declare @questionId nvarchar(255);
    
    select @questionId = id from question where description = 'employee.jobtitle' and element_text = 'label.job.title';

    -- Updating answers & elements
    update answer set question_id = @questionId where question_id in (select id from question where element_text = 'label.department' and id != @questionId);
    update element set question_id = @questionId where question_id in (select id from question where element_text = 'label.department' and id != @questionId);
    
    -- Removing unsed questions
    delete from question where id in (select id from question where element_text = 'label.department' and id != @questionId);
    
END
GO

-- Fixing Registration Link

BEGIN
    declare @questionId nvarchar(255);
    
    select @questionId = id from question where element_text = 'label.registration.tandc.link' and id != '2a2a8b77-fd14-43a5-ac4f-f1b76e438af2';

    -- Updating answers & elements
    update answer set question_id = @questionId where question_id in (select id from question where element_text = 'label.department' and id != @questionId);
    update element set question_id = @questionId where question_id in (select id from question where element_text = 'label.department' and id != @questionId);
    
    -- Removing unsed questions
    delete from question where id in (select id from question where element_text = 'label.department' and id != @questionId);
    
END
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'tab.usage','Usage');--1
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.product.id','Product ID');--2
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.division','Division');--3
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registerable.type','Registerable Type');--4
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activation','Activation');--5
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.direct','Direct');--6
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.no.direct.products','No Registered Products');--7
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.linked','Linked');--8
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.product.name','Product Name');--9
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.no.linked.products','No Linked Products');--10
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.definition.type.ACCOUNT_REGISTRATION','Account');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.definition.type.PRODUCT_REGISTRATION','Product');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.definition.type.ACTIVATION_CODE_REGISTRATION','Activation Code');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.definition.type','Registration Definition Type');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.product.id.linked','Product ID (Linked)');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.product.id.registered','Product ID (Registered)');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.asset.usage.products.registered','Registered Products associated with this Asset directly');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.asset.usage.products.linked.indirect','Linked Products associated with the Registered Products');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.asset.usage.products.linked.direct','Linked Products associated with this Asset directly');
GO

-- New role for Manage Questions functionality in Admin
insert into permission values ('885C9B6E-AB02-42B0-B570-F293BCD88545', 0, 'MANAGE_PAGE');
GO
insert into role_permissions values ('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', '885C9B6E-AB02-42B0-B570-F293BCD88545')
GO

update message set message='Org Unit' where [key]='label.division';
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.org.unit.short','Org Unit');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.org.unit.long','Organisational Unit');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.org.units.short','Org Units');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.org.units.long','Organisational Units');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.manage.org.units','Manage Organisational Units');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.add.org.unit','Add Org Unit');
GO
update role set name ='ORG_UNIT_ADMIN' where name='DIVISION_ADMIN';
GO
insert into permission values ('04fbc9d7-96e7-4d36-901b-43e13148c301', 0, 'MANAGE_ORG_UNITS');
GO
insert into role_permissions ( role_id , permission_id) values ((select id from role where name='SYSTEM_ADMIN'),'04fbc9d7-96e7-4d36-901b-43e13148c301')
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.org.unit.blank','The Org Unit cannot be blank');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.org.unit.duplicate','The Org Unit must be unique');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.org.unit.invalid','Only use A..Z,a..z,0..9,-,_,( and )');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.org.unit.global.blank','At least one Org Unit is blank.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.org.unit.global.duplidate','The Org Units contain duplicates.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.org.unit.global.invalid','At least one Org Unit is invalid.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'status.org.units.update.success','The Org Units have been successfully updated.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'status.org.units.update.nothing','No Org Unit updates were requested.');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.rolling.from.to','Access from {0} to {1}');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.rolling.access.period','Access period: {0} from');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.rolling.registration','Registration');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.rolling.first.use','First Use');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.rolling.subject.to','Subject to');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.rolling.start','Start');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.rolling.end','End');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'licence.description.email.prefix','Licence Details :');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.forgot.password','Forgotten password');
GO
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.language.details','Language details');
GO