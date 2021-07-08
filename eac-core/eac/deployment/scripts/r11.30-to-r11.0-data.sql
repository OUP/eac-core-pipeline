BEGIN
declare @rowCount int ;
declare @message nvarchar(255);
declare @lang nvarchar(255);

--1/6
set @message=N'パスワードの再発行';
set @lang = 'ja'
select @rowCount = count(*) from message where [key]='label.forgot.password' and language=@lang;
IF @rowCount = 0
BEGIN
	print 'insert for ' + @lang;
	insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',@lang,null,null,'label.forgot.password',@message);
END
ELSE
BEGIN
	print 'update for ' + @lang;
	update message set message=@message where [key]='label.forgot.password' and language=@lang;
END

--2/6
set @message=N'비밀번호 재발급';
set @lang='ko';
select @rowCount = count(*) from message where [key]='label.forgot.password' and language=@lang;
IF @rowCount = 0
BEGIN
	print 'insert for ' + @lang;
	insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',@lang,null,null,'label.forgot.password',@message);
END
ELSE
BEGIN
	print 'update for ' + @lang;
	update message set message=@message where [key]='label.forgot.password' and language=@lang;
END

--3/6
set @message=N'Alterar Password';
set @lang='pt';

select @rowCount = count(*) from message where [key]='label.forgot.password' and language=@lang;
IF @rowCount = 0
BEGIN
	print 'insert for ' + @lang;
	insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',@lang,null,null,'label.forgot.password',@message);
END
ELSE
BEGIN
	print 'update for ' + @lang;
	update message set message=@message where [key]='label.forgot.password' and language=@lang;
END

--4/6
set @message=N'поменять пароль';
set @lang='ru';

select @rowCount = count(*) from message where [key]='label.forgot.password' and language=@lang;
IF @rowCount = 0
BEGIN
	print 'insert for ' + @lang;
	insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',@lang,null,null,'label.forgot.password',@message);
END
ELSE
BEGIN
	print 'update for ' + @lang;
	update message set message=@message where [key]='label.forgot.password' and language=@lang;
END

--5/6
set @message=N'змінити пароль';
set @lang='uk';

select @rowCount = count(*) from message where [key]='label.forgot.password' and language=@lang;
IF @rowCount = 0
BEGIN
	print 'insert for ' + @lang;
	insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',@lang,null,null,'label.forgot.password',@message);
END
ELSE
BEGIN
	print 'update for ' + @lang;
	update message set message=@message where [key]='label.forgot.password' and language=@lang;
END


--6/6
set @message=N'密碼重設';
set @lang='zh';

select @rowCount = count(*) from message where [key]='label.forgot.password' and language=@lang;
IF @rowCount = 0
BEGIN
	print 'insert for ' + @lang;
	insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',@lang,null,null,'label.forgot.password',@message);
END
ELSE
BEGIN
	print 'update for ' + @lang;
	update message set message=@message where [key]='label.forgot.password' and language=@lang;
END

END
GO

update message set message='If your activation email does not arrive within 30 minutes please look in your spam or junk mail folder. If you can''t find the activation email and need us to send you a new one, please' 
where [key]='label.activatelicenceemail_1' and language is null;
GO

update message set message='If you still have problems receiving the activation email please contact' 
where [key]='label.activatelicenceemail_4' and language is null;
GO

