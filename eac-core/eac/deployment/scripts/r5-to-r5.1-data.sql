
-- MESSAGES

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.notifying.customer.when.no.account','There was a problem notifying the customer that their account was not found. Please contact the system adminsitrator.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.no.account.found','No account could be found for the email address you entered.');
