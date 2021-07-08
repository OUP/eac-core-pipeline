--EAC stored procedures/functions for managing Admin user, the roles they have and the divisions they can manage

/*
1  EAC_MERGE_ADMIN_ROLE
2  EAC_CLEANUP_ADMIN_ROLES
3  EAC_CLEANUP_ALL_ADMIN_ROLES
4  EAC_REMOVE_ADMIN_ROLES
5  EAC_CREATE_ADMIN
6  EAC_SET_ADMIN_ROLES
7  EAC_ROLE_TOKENIZER
8  EAC_SET_ADMIN_DIVISIONS
9  EAC_DIVISION_TOKENIZER
10 EAC_REMOVE_ADMIN_ROLES
*/

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_SET_ADMIN_DIVISIONS') --AND type in (N'SP')
)
  DROP  procedure [dbo].EAC_SET_ADMIN_DIVISIONS
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_REMOVE_DIVISIONS_FROM_ADMIN') --AND type in (N'SP')
)
  DROP  procedure [dbo].EAC_REMOVE_DIVISIONS_FROM_ADMIN
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_DIVISION_TOKENIZER') --AND type in (N'SP')
)
  DROP  function [dbo].EAC_DIVISION_TOKENIZER
GO


IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_DOES_ROLE_EXIST') --AND type in (N'SP')
)
  DROP  function [dbo].EAC_DOES_ROLE_EXIST
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_IS_CHILD_ROLE') --AND type in (N'SP')
)
  DROP  function [dbo].EAC_IS_CHILD_ROLE
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_MERGE_ADMIN_ROLE') --AND type in (N'SP')
)
  DROP  procedure [dbo].EAC_MERGE_ADMIN_ROLE
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_CLEANUP_ADMIN_ROLES') --AND type in (N'SP')
)
  DROP  procedure [dbo].EAC_CLEANUP_ADMIN_ROLES
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_CLEANUP_ALL_ADMIN_ROLES') --AND type in (N'SP')
)
  DROP  procedure [dbo].EAC_CLEANUP_ALL_ADMIN_ROLES
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_REMOVE_ADMIN_ROLES') --AND type in (N'SP')
)
  DROP  procedure [dbo].EAC_REMOVE_ADMIN_ROLES
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_CREATE_ADMIN') --AND type in (N'SP')
)
  DROP  procedure [dbo].EAC_CREATE_ADMIN
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_ROLE_TOKENIZER') --AND type in (N'SP')
)
  DROP  function [dbo].EAC_ROLE_TOKENIZER
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_SET_ADMIN_ROLES') --AND type in (N'SP')
)
  DROP  PROCEDURE [dbo].EAC_SET_ADMIN_ROLES
GO

--	Used to convert a list of short & full division names into a table of full division names.
--	Used by EAC_SET_ADMIN_DIVISIONS
CREATE FUNCTION eac_division_tokenizer (@list nvarchar(MAX))
   RETURNS @tbl TABLE (division nvarchar(255) NOT NULL) AS
BEGIN
   DECLARE @pos        int,
           @nextpos    int,
           @valuelen   int

   SELECT @pos = 0, @nextpos = 1

   WHILE @nextpos > 0
   BEGIN
      SELECT @nextpos = charindex(',', @list, @pos + 1)
      SELECT @valuelen = CASE WHEN @nextpos > 0
                              THEN @nextpos
                              ELSE len(@list) + 1
                         END - @pos - 1
      declare @tokenCode nvarchar(255);
      declare @token nvarchar(255);
      set @tokenCode = substring(@list, @pos + 1, @valuelen);
      
      set @token = (SELECT CASE rtrim(ltrim(@tokenCode))
        WHEN 'CAN'  THEN 'CANADA'
        WHEN 'CHI'  THEN 'CHINA'
        WHEN 'GABA' THEN 'GAB_ACADEMIC'
        WHEN 'GABJ' THEN 'GAB_JOURNALS'
        WHEN 'GABU' THEN 'GAB_USA'
        WHEN 'IND'  THEN 'INDIA'
        WHEN 'KEN'  THEN 'KENYA'
        WHEN 'MY'   THEN 'MALAYSIA'
        WHEN 'MEX'  THEN 'MEXICO'
		WHEN 'PAK'  THEN 'PAKISTAN'
		WHEN 'SA'   THEN 'SOUTH_AFRICA'
		WHEN 'SPA'  THEN 'SPAIN'
		WHEN 'TAN'  THEN 'TANZANIA'
        ELSE @tokenCode
        END
        );
        
      IF @token is not null AND (select count(*) from division where division_type=@token) > 0
      BEGIN  
        declare @tbl_count int;
        set @tbl_count = (select count(*) from @tbl where division = @token);
        if @tbl_count = 0
            INSERT @tbl (division) VALUES (@token)
      END;
      SELECT @pos = @nextpos
   END
   RETURN
END
GO

CREATE PROCEDURE EAC_REMOVE_DIVISIONS_FROM_ADMIN(@adminId nvarchar(255))
AS
BEGIN
	declare @adminName nvarchar(255)
	
	--validate admin id
	select @adminName = username from customer where id=@adminId and user_type='ADMIN'
	if (@adminName is null)
	BEGIN
		print 'ERROR : no admin user found with id ' +@adminId
		return
	END
		
	delete from division_admin_user where admin_user_id = @adminId
	
END
GO

--Used to create a division_admin_user row for this admin user and each of the divisions specified
--Calls eac_division_tokenizer
CREATE PROC EAC_SET_ADMIN_DIVISIONS
  @adminId nvarchar(255),
  @divisions nvarchar(255)
AS
BEGIN
  declare @divisionsNamesToAdd TABLE (division_name nvarchar(255) NOT NULL);
  declare @divisionName nvarchar(255);
  declare @adminName nvarchar(255);

  --validate admin id
  select @adminName = username from customer where id=@adminId and user_type='ADMIN'
  if (@adminName is null)
  BEGIN
		print 'ERROR : no admin user found with id ' +@adminId
		return
  END

  insert into @divisionsNamesToAdd
  select division from dbo.eac_division_tokenizer(@divisions)
  
  declare @check int;
  set @check = -1;

  BEGIN TRANSACTION

	exec dbo.EAC_REMOVE_DIVISIONS_FROM_ADMIN @adminId

	DECLARE C1 CURSOR FOR  
	SELECT division_name  FROM @divisionsNamesToAdd

	OPEN C1;
	FETCH NEXT FROM C1 INTO @divisionName;

	declare @count int;
	set @count = 0;

	WHILE @@FETCH_STATUS = 0   
	BEGIN
        
        insert into division_admin_user (id, obj_version, division_id, admin_user_id ) values (newId(),0,(select id from division where division_type=@divisionName),@adminId)
		FETCH NEXT FROM C1 INTO @divisionName;
	END
	CLOSE C1  
	DEALLOCATE C1

  COMMIT;

END
GO

--For merging a role into the set of roles the admin user already has.
--If the admin user has a role which is a superset of the new role - the new role is not added.
--If the admin user has a role which is a subset of the new role - the subset role is removed and the superset role added. admin's set of roles to the minimum size)
CREATE PROC EAC_MERGE_ADMIN_ROLE(@adminId nvarchar(255), @newRoleName nvarchar(255)) 
AS
BEGIN

declare @adminName nvarchar(255);
declare @existingRoleName nvarchar(255);
declare @existingRoleId   nvarchar(255);
declare @ignore int;

--validate admin id
select @adminName = username from customer where id=@adminId and user_type='ADMIN'
if (@adminName is null)
BEGIN
	print 'ERROR : no admin user found with id ' +@adminId
	return
END

--validate role name
IF (dbo.EAC_DOES_ROLE_EXIST(@newRoleName) < 1) 
BEGIN
	print 'ERROR : ROLE [' + @newRoleName + '] does not exist'
	return
END

set @ignore = 0;
DECLARE db_cursor1 CURSOR FOR  
	SELECT R.name  FROM role R 
	join customer_roles CR on R.id = CR.role_id
	where CR.customer_id = @adminId;

OPEN db_cursor1;
FETCH NEXT FROM db_cursor1 INTO @existingRoleName;

WHILE @@FETCH_STATUS = 0   
BEGIN   
    --print 'processing ' + @newRoleName + ' ' + @existingRoleName
	IF (dbo.EAC_IS_CHILD_ROLE(@existingRoleName,@newRoleName) > 0) 
	BEGIN
		set @ignore = @ignore+1
		print 'Not adding new role [' + @newRoleName + '] to [' + @adminName + '] as it is covered by [' + @existingRoleName + ']';
	END
	FETCH NEXT FROM db_cursor1 INTO @existingRoleName;
END
CLOSE db_cursor1   
DEALLOCATE db_cursor1

IF (@ignore = 0)
BEGIN
	BEGIN TRANSACTION
	set @ignore = 0	

	DECLARE db_cursor2 CURSOR FOR  
	SELECT R.name, R.id  FROM role R 
	join customer_roles CR on R.id = CR.role_id
	where CR.customer_id = @adminId;

	OPEN db_cursor2;
	FETCH NEXT FROM db_cursor2 INTO @existingRoleName,@existingRoleId;

	WHILE @@FETCH_STATUS = 0   
	BEGIN   
		IF (dbo.EAC_IS_CHILD_ROLE(@newRoleName,@existingRoleName) > 0) 
		BEGIN
		    print 'Removing association with child role [' + @existingRoleName + ']'			
			delete from customer_roles where customer_id=@adminId and role_id=@existingRoleId
		END
		FETCH NEXT FROM db_cursor2 INTO @existingRoleName,@existingRoleId;
	END
	CLOSE db_cursor2   
	DEALLOCATE db_cursor2
	
	insert into customer_roles (customer_id,role_id) values (@adminId, (select id from role where name=@newRoleName))
	COMMIT
	print 'Added [' + @newRoleName + ']';
END

END
GO

-- Calls EAC_CLEANUP_ADMIN_ROLES on each admin user.
-- Called as part of the script r9.0-to-r9.1-data.sql
CREATE PROC EAC_CLEANUP_ADMIN_ROLES(@adminId nvarchar(255))
AS
BEGIN
declare @roleName nvarchar(255);
declare @adminName nvarchar(255);
declare @adminRoles TABLE(
role_name nvarchar(255) NOT NULL
);

select @adminName = username from customer where user_type='ADMIN' and id=@adminId;

BEGIN TRANSACTION

	declare @total int;

	print 'START : Cleanup Admin Roles for [' + @adminName + ']';

	--Record the existing roles, we will add them back in 1 by 1
	insert @adminRoles
	select R.name FROM customer_roles CR
	join role R on CR.role_id = R.id
	where CR.customer_id = @adminId;

	select @total = count(*) from @adminRoles;

	--delete the existing roles
	delete from customer_roles where customer_id = @adminId;

	--add the roles back in 1 by 1
	DECLARE db_cursor3 CURSOR FOR  
	SELECT role_name  FROM @adminRoles

	OPEN db_cursor3;
	FETCH NEXT FROM db_cursor3 INTO @roleName;

	declare @count int;
	set @count = 0;

	WHILE @@FETCH_STATUS = 0   
	BEGIN
		set @count = @count + 1
		print ''
		print '-------------------------------------------'
		print cast(@count as nvarchar) + '/' +cast(@total as nvarchar) + ' adding role ' + @roleName + ' to ' + @adminName   
		exec dbo.EAC_MERGE_ADMIN_ROLE @adminId,@roleName
		print '-------------------------------------------'
		FETCH NEXT FROM db_cursor3 INTO @roleName;
	END
	CLOSE db_cursor3  
	DEALLOCATE db_cursor3

COMMIT
print 'END : Cleanup Admin Roles for [' + @adminName + ']';
END
GO

--Calls EAC_CLEANUP_ADMIN_ROLES on each admin user.
--Called as part of the script r9.0-to-r9.1-data.sql
CREATE PROC EAC_CLEANUP_ALL_ADMIN_ROLES
AS
BEGIN
DECLARE @adminId nvarchar(255);

print 'START : Cleanup ALL Admin Roles'
BEGIN TRANSACTION

	DECLARE db_cursor4 CURSOR FOR  
	SELECT id FROM customer where user_type='ADMIN'

	OPEN db_cursor4;
	FETCH NEXT FROM db_cursor4 INTO @adminId;

	WHILE @@FETCH_STATUS = 0   
	BEGIN
		exec dbo.EAC_CLEANUP_ADMIN_ROLES @adminId
		FETCH NEXT FROM db_cursor4 INTO @adminId;
	END
	CLOSE db_cursor4  
	DEALLOCATE db_cursor4

COMMIT
print 'END : Cleanup ALL Admin Roles'
END
GO

--Used to convert a list of short & full role names into a table of full role names.
--Used by EAC_SET_ADMIN_ROLES.
--NEW VERSION (old version in r5.6-to-r6.0-stored-create-admin.sql)
--The short role names are defined in the body of this function.
CREATE FUNCTION eac_role_tokenizer (@list nvarchar(MAX))
   RETURNS @tbl TABLE (token nvarchar(255) NOT NULL) AS
BEGIN
   DECLARE @pos        int,
           @nextpos    int,
           @valuelen   int

   SELECT @pos = 0, @nextpos = 1

   WHILE @nextpos > 0
   BEGIN
      SELECT @nextpos = charindex(',', @list, @pos + 1)
      SELECT @valuelen = CASE WHEN @nextpos > 0
                              THEN @nextpos
                              ELSE len(@list) + 1
                         END - @pos - 1
      declare @tokenCode nvarchar(255);
      declare @token nvarchar(255);
      set @tokenCode = substring(@list, @pos + 1, @valuelen);
      
      set @token = (SELECT CASE rtrim(ltrim(@tokenCode))
        WHEN 'SA' THEN 'SYSTEM_ADMIN'
        WHEN 'DA' THEN 'DIVISION_ADMIN'
        WHEN 'PC' THEN 'PRODUCTION_CONTROLLER'
        WHEN 'PO' THEN 'PUBLICATIONS_OFFICER'
        WHEN 'FR' THEN 'FIELD_REP'
        WHEN 'WSFU' THEN 'WS_FULL'
        WHEN 'WSRO' THEN 'WS_READONLY'
        WHEN 'WSBA' THEN 'WS_BASIC'
        WHEN 'WSRW' THEN 'WS_READWRITE'
        ELSE @tokenCode
        END
        );
        
      IF @token is not null AND DBO.EAC_DOES_ROLE_EXIST(@token) > 0
      BEGIN  
        declare @tbl_count int;
        set @tbl_count = (select count(*) from @tbl where token = @token);
        if @tbl_count = 0
            INSERT @tbl (token) VALUES (@token)
      END;
      SELECT @pos = @nextpos
   END
   RETURN
END
GO

--Removes all roles from an admin user.
--This is useful if you want to (say) 'downgrade' from SYSTEM_ADMIN to FIELD_REP:
--You call eac_remove_admin_roles then call eac_merge_admin_role with FIELD_REP.
--Called from EAC_SET_ADMIN_ROLES
CREATE PROCEDURE EAC_REMOVE_ADMIN_ROLES(@adminId nvarchar(255))
AS
BEGIN
	declare @adminName nvarchar(255)
	
	--validate admin id
	select @adminName = username from customer where id=@adminId and user_type='ADMIN'
	if (@adminName is null)
	BEGIN
		print 'ERROR : no admin user found with id ' +@adminId
		return
	END
		
	delete from customer_roles where customer_id = @adminId
	
END
GO

--Sets the admin users roles. 
--The roles are provided as a comma seperated list of role names (short or full). The short role names are defined in the body of EAC_ROLE_TOKENIZER.
--Any roles which do not exist are ignored.
--Called by EAC_CREATE_ADMIN
CREATE PROC EAC_SET_ADMIN_ROLES
  @adminId nvarchar(255),
  @roles nvarchar(255)
AS
BEGIN
  declare @roleNamesToAdd TABLE (full_role_name nvarchar(255) NOT NULL);
  declare @roleName nvarchar(255);
  declare @adminName nvarchar(255);

  --validate admin id
  select @adminName = username from customer where id=@adminId and user_type='ADMIN'
  if (@adminName is null)
  BEGIN
		print 'ERROR : no admin user found with id ' +@adminId
		return
  END

  insert into @roleNamesToAdd
  select token from dbo.eac_role_tokenizer(@roles)
  
  declare @check int;
  set @check = -1;

  BEGIN TRANSACTION

	exec dbo.EAC_REMOVE_ADMIN_ROLES @adminId

	DECLARE C1 CURSOR FOR  
	SELECT full_role_name  FROM @roleNamesToAdd

	OPEN C1;
	FETCH NEXT FROM C1 INTO @roleName;

	declare @count int;
	set @count = 0;

	WHILE @@FETCH_STATUS = 0   
	BEGIN
        print 'merging ' + @roleName + ' role with user [' + @adminName + ']s roles.'
        exec dbo.EAC_MERGE_ADMIN_ROLE @adminId, @roleName        
		FETCH NEXT FROM C1 INTO @roleName;
	END
	CLOSE C1  
	DEALLOCATE C1

  COMMIT;

END
GO

-- Used to create an admin user with a set of admin roles. 
-- The roles are provided as a comma seperated list of role names (short or full). The short role names are defined in the body of EAC_ROLE_TOKENIZER.
-- Any roles which do not exist are ignored.
-- Calls EAC_SET_ADMIN_ROLES.
-- NEW VERSION (old version in r5.6-to-r6.0-stored-create-admin.sql)
CREATE PROC EAC_CREATE_ADMIN
  @first nvarchar(255),
  @last nvarchar(255),
  @username nvarchar(255),
  @email  nvarchar(255),
  @password nvarchar(255),
  @roles nvarchar(255),
  @divisions nvarchar(255),
  @resetPassword char(1) = 1
AS
BEGIN
  print 'START -------------------------------------------------';
  print 'first='+@first+', last='+@last+', username='+@username+', password='+@password+', roles='+@roles;
  declare @cust_id nvarchar(255);
  set @cust_id = (select newId());
  print 'cust_id='+@cust_id;

  declare @check int;
  set @check = -1;

  BEGIN TRANSACTION
    insert into customer (id,       obj_version,username,  email_address, password, first_name,family_name,reset_password,locked,failed_attempts,user_type,created_date,enabled, email_verification_state)
    values               (@cust_id, 0,          @username, @email, @password, @first, @last, 0, 0, 0, 'ADMIN', getdate(), 1, 'UNKNOWN');

    exec dbo.EAC_SET_ADMIN_DIVISIONS @cust_id, @divisions

	exec EAC_SET_ADMIN_ROLES @cust_id, @roles
	
    set @check = (select count(*) from customer where id=@cust_id);
  COMMIT;
  print 'END check -------------------------------------------------' + cast(@check as nvarchar);
  print ' ';  

END
GO

CREATE FUNCTION EAC_DOES_ROLE_EXIST (@name nvarchar(255)) RETURNS INT
AS
BEGIN
   DECLARE @retval INT

   select @retval=count(*) from role where name=@name

   RETURN @retval
END
GO

CREATE FUNCTION EAC_IS_CHILD_ROLE(@parent nvarchar(255), @child nvarchar(255)) RETURNS INT
AS
BEGIN

    declare @result int
	set @result = -1

	IF (dbo.EAC_DOES_ROLE_EXIST(@parent) < 1) 
	BEGIN
		--print 'ROLE ' + @parent + 'does not exist'
		return -11
	END

	IF (dbo.EAC_DOES_ROLE_EXIST(@child) < 1)
	BEGIN
		--print 'ROLE ' + @child + 'does not exist'
		return -111
	END

	declare @extra int
	set @extra = -1

	declare @missing int
	set @missing = -1

	select @extra = count(*)
	from permission P1	
	join role_permissions RP1 on P1.id = RP1.permission_id
	join role R1 on RP1.role_id = R1.id
	where R1.name=@parent
	and not exists
	(
	select P2.id
	from permission P2	
	join role_permissions RP2 on P2.id = RP2.permission_id
	join role R2 on RP2.role_id = R2.id
	where R2.name=@child and P2.id = P1.id)

	select @missing = count(*)
	from permission P1	
	join role_permissions RP1 on P1.id = RP1.permission_id
	join role R1 on RP1.role_id = R1.id
	where R1.name=@child
	and not exists
	(
	select P2.id
	from permission P2	
	join role_permissions RP2 on P2.id = RP2.permission_id
	join role R2 on RP2.role_id = R2.id
	where R2.name=@parent and P2.id = P1.id)

    IF @extra >= 0 and @missing = 0
	BEGIN
		set @result = 1
	END
		ELSE
	BEGIN
		set @result = 0
	END

	RETURN @result
END
GO

