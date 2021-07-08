-- New Messages added for Reset Password failure. Start

insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'title.resetpasswordfailure','Reset Password Failure', 0);

insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'label.resetpasswordfailure','Reset password link has expired or some error occurred while resetting your password. Please try resetting it again.', 0);


-- New Messages added for Reset Password failure. End

--Updated message reset password success page--

update message set [message]='Thank You. Please check your email inbox. If we have your username in our records, you will be sent an email containing instructions to change password.' where [key] = 'label.resetpasswordsuccess';


--Updated message reset password success page--
--Updated message reset password page--
update message set [message]='Please enter your username in the box below and press "Reset Password". We will email you a link to change your password details if you have an account' where [key] = 'help.resetpassword';

--Updated message reset password page--


--Update product name and division id from asset
update product 
set	product.product_name = asset.product_name,product.erights_id=asset.erights_id,product.state=asset.state 
from product as product
inner join asset as asset on  product.asset_id=asset.id
and product.product_type='REGISTERABLE'

update product set product.division_id = asset.division_id
from asset
where asset.id = product.asset_id
and product.product_type = 'REGISTERABLE'

--DIVISION erights id--

--First run division migration scrpipt in rightsuit
update division set erights_id = rs_division.division_id
from erWeb_OUP_EAC_live.dbo.ersmd_division_lookup as rs_division
where division.division_type = rs_division.DIVISION_TYPE 






--
--Above Query all ready run on test and dev env
--Above Query all ready run on test and dev env
--Above Query all ready run on test and dev env
--


--US112/TA335 : Migrate division in product table
update prod set prod.division_erights_id = div.erights_id
from product prod
JOIN division div
on prod.division_id = div.id



--US112/TA336 : Migrate division for Admin User
UPDATE eac_admin SET eac_admin.division_erights_id = er_div.division_id
FROM erWeb_OUP_EAC_live.dbo.ersmd_division_lookup er_div
JOIN division eac_div
ON er_div.DIVISION_ID = eac_div.erights_id
JOIN division_admin_user eac_admin
ON eac_admin.division_id = eac_div.id;


--US112/TA337 : Migrate division for Page Definnition
UPDATE eac_pd SET eac_pd.division_erights_id = er_div.division_id
FROM erWeb_OUP_EAC_live.dbo.ersmd_division_lookup er_div
JOIN division eac_div
ON er_div.DIVISION_ID = eac_div.erights_id
JOIN page_definition eac_pd
ON eac_pd.division_id = eac_div.id

-- US196/TA462 - messsage for reset password email template. Start
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.email.content','We have reset your password. Please click the link below and change your password.', 0);

insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.email.note','Please note that this link is valid for 24 hours only.', 0);

-- US196/TA462 - messsage for reset password email template. Start

update customer set reset_password=0 where reset_password is NULL
update customer set enabled=1 where enabled is NULL
update customer set failed_attempts=0 where failed_attempts is NULL


-- Sprint 7 DP - permission for MERGE_CUSTOMERS:Prod Issue : INC0024726 , EACENH-12
-- Need not enable for all admin users
--insert into role(id,obj_version,name) values (newid(),0,'MERGE_CUSTOMER');

--insert into permission (id, obj_version, name) values (newId() , 0, 'MERGE_CUSTOMER');

--insert into role_permissions (role_id, permission_id) values ((select id from role where name='MERGE_CUSTOMER'),((select id from permission where name='MERGE_CUSTOMER')))


-- 	EAC Admin Console Changes  ---> Bulk Create Activation Code.
insert into role(id,obj_version,name) values (newid(),0,'ROLE_BULK_CREATE_ACTIVTION_CODE');

insert into permission (id, obj_version, name) values (newId() , 0, 'ROLE_BULK_CREATE_ACTIVTION_CODE');

insert into role_permissions (role_id, permission_id) values ((select id from role where name='ROLE_BULK_CREATE_ACTIVTION_CODE'),((select id from permission where name='ROLE_BULK_CREATE_ACTIVTION_CODE')))

-- 	EAC Admin Console Changes  ---> White List Url Changes

insert into role(id,obj_version,name) values (newid(),0,'WHITE_LIST_URLS');

insert into permission (id, obj_version, name) values (newId() , 0, 'WHITE_LIST_URLS');

insert into role_permissions (role_id, permission_id) values ((select id from role where name='WHITE_LIST_URLS'),((select id from permission where name='WHITE_LIST_URLS')))


--DIVISION DUPLICATION US133
UPDATE eri_prod SET eri_prod.division_id = eac_div.erights_id
FROM erWeb_OUP_EAC_live.dbo.ers_products er_prod
JOIN product AS eac_prod
ON er_prod.product_id = eac_prod.erights_id
LEFT JOIN erWeb_OUP_EAC_live.dbo.ersmd_product_info_data eri_prod
ON eri_prod.product_id = er_prod.product_id
JOIN division AS eac_div 
ON eac_div.id = eac_prod.division_id;

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.account.locked','The account is locked.');


insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activationCode','Please see the attached zip file for activation code batch details.
If you have any problems with this then please contact the EAC team.') ;


insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'registration.status.expired','Expired', 0);

--Added to update footer UAT requirement
update message set message='Privacy Policy' where [key]='footer.privacypolicy'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'footer.termsandcondition','Terms & Conditions');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'footer.cookiepolicy','Cookie Policy');

-- messsage for reset password email template. Start
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.email.content.new','You have asked to change your password for your Oxford University Press account.', 0);
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.email.note.new','The link will work for 24 hours after this email was sent.', 0);
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.email.please.note','Please note: you must change your password to sign in. Your old password will not work anymore.', 0);
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.wishes','Best wishes,', 0);
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.customer.support','Customer Support', 0);
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.oup','Oxford University Press', 0);
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.click.here','click here', 0);
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.your.password',' to reset your password.', 0);
insert into message (id,basename,language,country,variant,[key],message,obj_version) values (newId(), 'messages',null,null,null,'password.reset.please','Please ',0);insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'footer.cookiepolicy','Cookie Policy'); 

--Insert Batch Jobs
INSERT INTO BATCH_JOB_TIME VALUES
(1,'CMDP',GETDATE(),GETDATE(),'SUCCESS',NULL);
INSERT INTO BATCH_JOB_TIME VALUES
(2,'OXED',GETDATE(),GETDATE(),'SUCCESS',NULL);
INSERT INTO BATCH_JOB_TIME VALUES
(3,'MALAYSIA',GETDATE(),GETDATE(),'SUCCESS',NULL);
INSERT INTO BATCH_JOB_TIME VALUES
(4,'ORG-UNIT',GETDATE(),GETDATE(),'SUCCESS',NULL);