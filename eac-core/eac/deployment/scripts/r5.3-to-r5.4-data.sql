update message set message = 'Your Profile' where [key] = 'profile.basic.title' and basename = 'messages' and language is null;
update message set message = 'Registration' where [key] = 'profile.basic.manage' and basename = 'messages' and language is null;
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.update.registration','Update registration');

--remove 3 redundant options from Profession Drop Down for Oxford Teachers Club.
delete tag_option
from tag_option TAGO
where TAGO.tag_id='A864D579-78C9-4F82-8CD5-2945669382D2'
and TAGO.value in ('administrator','parent','student');

delete message
from message M
where M.[key] in (
	'label.job.administrator',
	'label.job.parent',
	'label.job.student');
	
	