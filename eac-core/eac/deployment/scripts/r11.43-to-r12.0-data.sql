--Release 11.43 was the production R11 release.

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.atypon.is.missing.licence','Atypon does not seem have erightsLicenceId {0}');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.extra.licence.in.atypon','This Atypon Licence is not associated with an EAC Registration. erightsLicenceId {0}');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.parent.registration.problem','This parent Registration (id {0}) of this LinkedRegistration has a problem.');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.product.in.atypon.but.not.eac','erightsProductId:{0} in Atypon but not in EAC');
GO

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.linked.licence.not.in.atypon','The Linked Registration''''s Licence does not seem to be in Atyon. ErightsLicenceId {0}');
GO

