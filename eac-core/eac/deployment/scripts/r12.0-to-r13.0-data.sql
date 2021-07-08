
update message set message='Your password must be minimum of 6 characters, with no whitespace, one or more letters (a-z) and one or more capital letters (A-Z).' 
where [key]='error.password.strength' and language is null;
GO

update message set message='Your password must be minimum of 6 characters, with no whitespace, one or more letters (a-z) and one or more capital letters (A-Z).' 
where [key]='info.password.strength' and language is null;
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.password.strength','Password strength');
GO
