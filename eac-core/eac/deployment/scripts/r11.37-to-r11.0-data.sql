delete from message where language in ('ab', 'af', 'ak', 'sq') and country is null and db_name() = 'eacuat';
GO

update message set message = 'You do not have access to this resource. If you think you should have access, please contact {2}.' where [key] = 'registration.denied.msg.4';
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activation.method','Activation Method');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.problem.registrations','Problem Registrations');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.problem.registrations.error','Atypon does not appear to be providing licence information for the following Registrations.');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registrations.noProblemRegistrations','This customer has no problem registrations.');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.id','Registration Id');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.linked.registration.id','Linked Registration Id');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'tab.problem.registrations','Problem Registrations');
GO


