update message set message = '<p>Students will not be granted access to this part of the site. </p><br/><p>All registrations are verified before activation so please ensure that you have notified your sales representative of your adoption. You can do so via this page: <a onclick="window.open(this.href); return false;" href="http://www.oup.co.uk/contactus/academic/sales/hecontact/">Find your sales representative</a></p><br/><p>Please note that if there is no record of your adoption with your sales representative, we will not be able to process your application. Where adoptions have been confirmed we aim to activate your account within three working days and your access will expire after one year. If you continue to adopt this book after that period you can then request a renewal of your subscription.</p>' where [key] = 'label.orcs.lecturer.preamble' and language is null;
GO
update message set message = 'Access will be confirmed within {0} from date of registration, and you will be notified via email. If you do not receive the notification email within this time, please check for it in your spam or junk mail folder. If the email is not there please contact <a href="mailto:{1}">{1}</a>.' where [key] = 'label.awaitinglicenceactivationconfirm';
GO
update message set message = 'If your email does not arrive within 30 minutes please look in your spam or junk mail folder. You may need to make sure that do_not_reply@oup.com is on your list of allowed email addresses. If the email is not there please' where [key] = 'label.emailvalidation2';
GO


drop procedure eac_create_admin;
GO

CREATE PROC eac_create_admin
  @first nvarchar(255),
  @last nvarchar(255),
  @username nvarchar(255),
  @email  nvarchar(255),
  @password nvarchar(255),
  @roles nvarchar(255),
  @division nvarchar(255),
  @resetPassword char(1) = 1
AS
BEGIN
  print 'START -------------------------------------------------';
  print 'first='+@first+', last='+@last+', username='+@username+', password='+@password+', roles='+@roles;
  declare @cust_id nvarchar(255);
  set @cust_id = (select newId());
  print 'cust_id='+@cust_id;
  declare @tbl_roles TABLE (number int NOT NULL);


  declare @isSA char(1); 
  declare @isDA char(2);
  declare @isPC char(3);
  declare @isPO char(4);
  declare @isFR char(5);
  declare @tcount int;

  set @isSA = '_';
  set @isDA = '_';
  set @isPC = '_';
  set @isPO = '_';
  set @isFR = '_';

  set @tcount = (select count(*) from dbo.eac_role_tokenizer(@roles));

  set @isSA = (select count(*) from  role R
  join dbo.eac_role_tokenizer(@roles) TT on TT.token = R.name
  and R.name='SYSTEM_ADMIN');

  set @isDA = (select count(*) from  role R
  join dbo.eac_role_tokenizer(@roles) TT on TT.token = R.name
  and R.name='DIVISION_ADMIN');

  set @isPC = (select count(*) from  role R
  join dbo.eac_role_tokenizer(@roles) TT on TT.token = R.name
  and R.name='PRODUCTION_CONTROLLER');

  set @isPO = (select count(*) from  role R
  join dbo.eac_role_tokenizer(@roles) TT on TT.token = R.name
  and R.name='PUBLICATIONS_OFFICER');

  set @isFR = (select count(*) from  role R
  join dbo.eac_role_tokenizer(@roles) TT on TT.token = R.name
  and R.name='FIELD_REP');
  
  print 'tCount' + cast(@tCount as nvarchar); 
  print 'isSA ' + cast(@isSA as nvarchar);
  print 'isDA ' + cast(@isDA as nvarchar);
  print 'isPC ' + cast(@isPC as nvarchar);
  print 'isPO ' + cast(@isPO as nvarchar);
  print 'isFR ' + cast(@isFR as nvarchar);

  declare @check int;
  set @check = -1;

  BEGIN TRANSACTION
    insert into customer (id,       obj_version,username,  email_address, password, first_name,family_name,email_verification_state,reset_password,locked,failed_attempts,user_type,created_date,enabled)
    values               (@cust_id, 0,          @username, @email, @password, @first, @last, 'VERIFIED', 0, 0, 0, 'ADMIN', getdate(), 1);

    insert into division_admin_user (id,obj_version,division_id,admin_user_id) values (newId(),0,(select id from division where division_type=@division),@cust_id);

    declare @roles_added nvarchar(255);
    set @roles_added = '';
    if @isSA = '1'
    BEGIN
        print 'adding SYSTEM_ADMIN role to user ' + @username;
        insert into customer_roles(role_id, customer_id) values((select id from role where name = 'SYSTEM_ADMIN'), @cust_id);
        set @roles_added = @roles_added + 'SA,';
    END;

    if @isDA = '1'
    BEGIN
        print 'adding DIVISION_ADMIN role to user ' + @username;
        insert into customer_roles(role_id, customer_id) values((select id from role where name = 'DIVISION_ADMIN'), @cust_id);
        set @roles_added = @roles_added + 'DA,';
    END;

    if @isPC = '1'
    BEGIN
        print 'adding PRODUCTION_CONTROLLER role to user ' + @username;
        insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), @cust_id);
        set @roles_added = @roles_added + 'PC,';
    END;

    if @isPO = '1'
    BEGIN
        print 'adding PUBLICATIONS_OFFICER role to user ' + @username;
        insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PUBLICATIONS_OFFICER'), @cust_id);
        set @roles_added = @roles_added + 'PO,';
    END;

    if @isFR = '1'
    BEGIN
        print 'adding FIELD_REP(5) role to user ' + @username;
        insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), @cust_id);
        set @roles_added = @roles_added + 'FR,';
    END;

    if len(@roles_added) > 1
        set @roles_added = substring(@roles_Added,1,len(@roles_added)-1);

  print 'ROLES REQUESTED ['+@roles+'] ROLES ADDED [' + @roles_added + ']';  
  set @check = (select count(*) from customer where id=@cust_id);
  COMMIT;
  print 'END check -------------------------------------------------' + cast(@check as nvarchar);
  print ' ';
END
GO
