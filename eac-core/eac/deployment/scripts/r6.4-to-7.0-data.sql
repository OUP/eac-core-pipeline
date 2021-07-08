--update system admin email addresses to reflect environments
DECLARE @nameofdb NVARCHAR(80)
DECLARE @emailAddress NVARCHAR(255)
SET @nameofdb = (SELECT DB_NAME())


IF @nameofdb = 'eactest' set @emailAddress='eacdevadmin@oup.com';
IF @nameofdb = 'eacuat'  set @emailAddress='eacuatadmin@oup.com';

print 'DB:' + @nameofdb + ', Sys Admin Email:'+@emailAddress;

if @nameofdb = 'eactest' OR @nameofdb = 'eacuat' 
BEGIN
    DECLARE @updateSql NVARCHAR(2000)
    DECLARE @updateRegistratnActivationSql NVARCHAR(2000)
    set @updateSql = 'update C set C.email_address = ''' + @emailAddress + ''' from  customer C where C.user_type=''ADMIN'' and C.email_address like ''eac%admin@oup.com'' ';
    set @updateRegistratnActivationSql = 'update ra set ra.validator_email = ''' + @emailAddress + ''' from registration_activation ra where ra.activation_type = ''VALIDATED'' and ra.validator_email like ''eac%admin@oup.com'' ';
    print 'executing SQL  [' + @updateSql +  ']'
    execute (@updateSql);
    print 'executing SQL  [' + @updateRegistratnActivationSql +  ']'
    execute (@updateRegistratnActivationSql);
END
GO

-- Fix for missing school address line 3 (Mantis 13263)
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.school.address.line3','School Address Line 3');
GO

-- Fix for tool tip (Mantis 12316)
update message set message='I have read and agreed to the terms and conditions of use for Oxford Teachers'''' Club' where [key]='title.registration.teachers.club.tandc';
GO