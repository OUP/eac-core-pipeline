
-- MESSAGES

update message set message = 'Thank you. Please check your email inbox. If we have your username in our records, you will be sent an email containing a temporary password. You will be asked to select a new password the next time you log in.' where [key] = 'label.resetpasswordsuccess' and basename = 'messages';
update message set message = 'Unfortunately we can find no record of an account linked to your email address. If you think you may have registered with a different user name please try entering that.' where [key] = 'label.no.account.found' and basename = 'messages';
update message set message = 'Thank you for registering. An email has been sent to the email address you supplied; in the email will be a link to click on to confirm your account. This will activate your access to {0}.' where basename = 'messages' and country is null and [key] = 'label.activatelicence';
update message set message = 'Terms and Conditions' where [key] = 'label.registration.tandc.header' and basename = 'messages';
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.requestedreset','Thank you for requesting a password reset.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.username.account','Username');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.password.account','Password');
insert into url_skin (id,obj_version,url,skin_path,site_name,contact_path) values (newId(),0,'http://elt.oup.com','skin/elt/css/eac-override.css','Oxford Teachers'' Club','http://elt.oup.com/contactus');
update url_skin set site_name = 'Oxford Fajar Companion Website' where site_name is null;
update url_skin set contact_path = 'http://cw.oxfordfajar.com.my/contactus.aspx' where contact_path is null;

--messages for 'warning on basic profile page when the email address and username do not match'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'warning.email.username.mismatch','The Email Address and Username are different.');

--message for 'activationCodeForm.jsp'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.product.return','Return to product');

--update product home pages for MALAYSIA
update product set home_page = 'http://dev.eac.uk.oup.com/eacSampleSite' 
where division_id = (select id from division where division_type='MALAYSIA')
and (home_page is null) OR (rtrim(home_page) = '');

--update product home pages for ELT (OTC)
update product set home_page = 'http://dev.eac.uk.oup.com/eacSampleSite' 
where division_id = (select id from division where division_type='ELT')
and product_name like 'Oxford Teachers%Club'
and (home_page is null) OR (rtrim(home_page) = '')

--update product home pages for ELT (non OTC)
update product set home_page = 'http://dev.eac.uk.oup.com/eacSampleSite' 
where division_id = (select id from division where division_type='ELT')
and product_name not like 'Oxford Teachers%Club'
and (home_page is null) OR (rtrim(home_page) = '')

--profile screen : show # of usages remaining
update message set message='{1} out of {0} use(s) remaining' where [key]='profile.licence.usage';

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.details.updated','Details updated successfully');

--combine the Oxford Teachers' Club Japanese question 'Institution Type' options 'Private Language' and 'School / Juku' into 'Private Language School / Juku'
--1. update answers to move 'school/juku' to 'private language'
--2. delete the option for 'school/juku'
--3. delete the message for 'school/juku'
--4. update the message for 'private language'

update answer
set 
answer_text= (select value from tag_option where label='label.institution.private.language')
where question_id = 
(
	select Q.id from question Q
	join element E on E.question_id = Q.id
	join field F on F.element_id = E.id
	join tag T on T.element_id = E.id
	join tag_option TAGO on TAGO.tag_id = T.id
	and TAGO.label='label.institution.school.juku'
)
and answer_text = (select value from tag_option where label='label.institution.school.juku')

delete from tag_option where value='school juku' and label='label.institution.school.juku'

delete from message where [key]='label.institution.school.juku'

update message set message ='Private Language School/Juku' where [key]='label.institution.private.language'

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.loggedout','Logged Out');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.you.are.logged.out','You are now logged out');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.you.are.logged.out','Thank you for using this Oxford University Press website, please come back soon!');



update message set message = 'Business/Professional' where basename = 'messages' and country is null and [key] = 'label.interested.business.professional';
update message set message = 'University/College/Junior College' where basename = 'messages' and country is null and [key] = 'label.institution.university.college.juniorcollege';
update message set message = 'Business/ESP' where basename = 'messages' and country is null and [key] = 'label.teaching.interest.business';
update message set message = 'Grammar/Vocabulary' where basename = 'messages' and country is null and [key] = 'label.teaching.interest.grammar.vocabulary';
update message set message = 'Listening/Speaking' where basename = 'messages' and country is null and [key] = 'label.teaching.interest.listening.speaking';
update message set message = 'CAE/FCE' where basename = 'messages' and country is null and [key] = 'label.exam.interest.cae.fce';
update message set message = 'IGCSE/GCSE' where basename = 'messages' and country is null and [key] = 'label.exam.interest.igcse.gcse';
update message set message = 'TOEIC Listening/Reading' where basename = 'messages' and country is null and [key] = 'label.exam.interest.toeic.listening.reading';
update message set message = 'TOEIC Speaking/Writing' where basename = 'messages' and country is null and [key] = 'label.exam.interest.toeic.speaking.writing';

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bq','Bonaire, Sint Eustatius and Saba');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ie','Ireland');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cw','Curaçao');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sx','Sint Maarten (Dutch Part)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ss','South Sudan');

update message set message = 'Congo, The Democratic Republic of the' where basename = 'messages' and country is null and [key] = 'label.registration.country.cd';
update message set message = 'Germany' where basename = 'messages' and country is null and [key] = 'label.registration.country.de';
update message set message = 'Greece' where basename = 'messages' and country is null and [key] = 'label.registration.country.gr';
update message set message = 'Libya' where basename = 'messages' and country is null and [key] = 'label.registration.country.ly';
update message set message = 'Taiwan, Province of China' where basename = 'messages' and country is null and [key] = 'label.registration.country.tw';
update message set message = 'United Kingdom' where basename = 'messages' and country is null and [key] = 'label.registration.country.gb';


delete from tag_option where tag_id = '09853008-7BC1-43E5-A1BC-BB69474164E2';

insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.af', 0, 'af', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ax', 1, 'ax', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.al', 2, 'al', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.dz', 3, 'dz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.as', 4, 'as', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ad', 5, 'ad', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ao', 6, 'ao', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ai', 7, 'ai', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.aq', 8, 'aq', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ag', 9, 'ag', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ar', 10, 'ar', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.am', 11, 'am', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.aw', 12, 'aw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.au', 13, 'au', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.at', 14, 'at', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.az', 15, 'az', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bs', 16, 'bs', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bh', 17, 'bh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bd', 18, 'bd', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bb', 19, 'bb', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.by', 20, 'by', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.be', 21, 'be', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bz', 22, 'bz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bj', 23, 'bj', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bm', 24, 'bm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bt', 25, 'bt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bo', 26, 'bo', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bq', 27, 'bq', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ba', 28, 'ba', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bw', 29, 'bw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bv', 30, 'bv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.br', 31, 'br', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.io', 32, 'io', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bn', 33, 'bn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bg', 34, 'bg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bf', 35, 'bf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bi', 36, 'bi', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kh', 37, 'kh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cm', 38, 'cm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ca', 39, 'ca', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cv', 40, 'cv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ky', 41, 'ky', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cf', 42, 'cf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.td', 43, 'td', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cl', 44, 'cl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cn', 45, 'cn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cx', 46, 'cx', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cc', 47, 'cc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.co', 48, 'co', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.km', 49, 'km', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cg', 50, 'cg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cd', 51, 'cd', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ck', 52, 'ck', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cr', 53, 'cr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ci', 54, 'ci', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hr', 55, 'hr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cu', 56, 'cu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cw', 57, 'cw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cy', 58, 'cy', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cz', 59, 'cz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.dk', 60, 'dk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.dj', 61, 'dj', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.dm', 62, 'dm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.do', 63, 'do', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ec', 64, 'ec', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.eg', 65, 'eg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sv', 66, 'sv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gq', 67, 'gq', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.er', 68, 'er', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ee', 69, 'ee', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.et', 70, 'et', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fk', 71, 'fk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fo', 72, 'fo', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fj', 73, 'fj', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fi', 74, 'fi', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fr', 75, 'fr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gf', 76, 'gf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pf', 77, 'pf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tf', 78, 'tf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ga', 79, 'ga', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gm', 80, 'gm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ge', 81, 'ge', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.de', 82, 'de', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gh', 83, 'gh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gi', 84, 'gi', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gr', 85, 'gr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gl', 86, 'gl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gd', 87, 'gd', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gp', 88, 'gp', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gu', 89, 'gu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gt', 90, 'gt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gg', 91, 'gg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gn', 92, 'gn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gw', 93, 'gw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gy', 94, 'gy', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ht', 95, 'ht', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hm', 96, 'hm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.va', 97, 'va', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hn', 98, 'hn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hk', 99, 'hk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hu', 100, 'hu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.is', 101, 'is', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.in', 102, 'in', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.id', 103, 'id', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ir', 104, 'ir', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.iq', 105, 'iq', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ie', 106, 'ie', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.im', 107, 'im', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.il', 108, 'il', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.it', 109, 'it', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.jm', 110, 'jm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.jp', 111, 'jp', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.je', 112, 'je', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.jo', 113, 'jo', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kz', 114, 'kz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ke', 115, 'ke', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ki', 116, 'ki', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kp', 117, 'kp', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kr', 118, 'kr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kw', 119, 'kw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kg', 120, 'kg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.la', 121, 'la', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lv', 122, 'lv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lb', 123, 'lb', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ls', 124, 'ls', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lr', 125, 'lr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ly', 126, 'ly', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.li', 127, 'li', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lt', 128, 'lt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lu', 129, 'lu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mo', 130, 'mo', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mk', 131, 'mk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mg', 132, 'mg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mw', 133, 'mw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.my', 134, 'my', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mv', 135, 'mv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ml', 136, 'ml', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mt', 137, 'mt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mh', 138, 'mh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mq', 139, 'mq', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mr', 140, 'mr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mu', 141, 'mu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.yt', 142, 'yt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mx', 143, 'mx', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fm', 144, 'fm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.md', 145, 'md', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mc', 146, 'mc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mn', 147, 'mn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.me', 148, 'me', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ms', 149, 'ms', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ma', 150, 'ma', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mz', 151, 'mz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mm', 152, 'mm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.na', 153, 'na', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nr', 154, 'nr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.np', 155, 'np', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nl', 156, 'nl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nc', 157, 'nc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nz', 158, 'nz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ni', 159, 'ni', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ne', 160, 'ne', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ng', 161, 'ng', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nu', 162, 'nu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nf', 163, 'nf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mp', 164, 'mp', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.no', 165, 'no', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.om', 166, 'om', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pk', 167, 'pk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pw', 168, 'pw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ps', 169, 'ps', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pa', 170, 'pa', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pg', 171, 'pg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.py', 172, 'py', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pe', 173, 'pe', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ph', 174, 'ph', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pn', 175, 'pn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pl', 176, 'pl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pt', 177, 'pt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pr', 178, 'pr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.qa', 179, 'qa', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.re', 180, 're', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ro', 181, 'ro', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ru', 182, 'ru', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.rw', 183, 'rw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bl', 184, 'bl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sh', 185, 'sh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kn', 186, 'kn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lc', 187, 'lc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mf', 188, 'mf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pm', 189, 'pm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vc', 190, 'vc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ws', 191, 'ws', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sm', 192, 'sm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.st', 193, 'st', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sa', 194, 'sa', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sn', 195, 'sn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.rs', 196, 'rs', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sc', 197, 'sc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sl', 198, 'sl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sg', 199, 'sg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sx', 200, 'sx', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sk', 201, 'sk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.si', 202, 'si', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sb', 203, 'sb', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.so', 204, 'so', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.za', 205, 'za', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gs', 206, 'gs', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ss', 207, 'ss', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.es', 208, 'es', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lk', 209, 'lk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sd', 210, 'sd', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sr', 211, 'sr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sj', 212, 'sj', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sz', 213, 'sz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.se', 214, 'se', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ch', 215, 'ch', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sy', 216, 'sy', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tw', 217, 'tw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tj', 218, 'tj', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tz', 219, 'tz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.th', 220, 'th', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tl', 221, 'tl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tg', 222, 'tg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tk', 223, 'tk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.to', 224, 'to', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tt', 225, 'tt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tn', 226, 'tn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tr', 227, 'tr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tm', 228, 'tm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tc', 229, 'tc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tv', 230, 'tv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ug', 231, 'ug', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ua', 232, 'ua', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ae', 233, 'ae', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gb', 234, 'gb', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.us', 235, 'us', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.um', 236, 'um', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.uy', 237, 'uy', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.uz', 238, 'uz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vu', 239, 'vu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ve', 240, 've', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vn', 241, 'vn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vg', 242, 'vg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vi', 243, 'vi', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.wf', 244, 'wf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.eh', 245, 'eh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ye', 246, 'ye', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.zm', 247, 'zm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.zw', 248, 'zw', '09853008-7BC1-43E5-A1BC-BB69474164E2');


delete from answer where question_id = 'f98db45d-b7d6-4129-ab57-3e6da1bf9ead';
delete from tag_option where tag_id = '1d92fd87-be27-43c2-8c8d-bbf83a3851d4';
delete from tag where id = '1d92fd87-be27-43c2-8c8d-bbf83a3851d4';
delete from field where element_id = '02ab3ae7-182d-433d-a68f-63ccc818178c';
delete from element where id = '02ab3ae7-182d-433d-a68f-63ccc818178c';
delete from question where id = 'f98db45d-b7d6-4129-ab57-3e6da1bf9ead';

update field set default_value = 'opt in' where element_id = '0FF40CFB-5154-4B90-85AB-926C1638901A' and component_id = '69323313-B3B4-46C5-A4A2-B501B9583829';

update answer set answer_text = 'opt in' where question_id = '4C974745-A2F1-41EB-90E9-B875EA17620D' and answer_text = 'Y';
update answer set answer_text = 'opt out' where question_id = '4C974745-A2F1-41EB-90E9-B875EA17620D' and answer_text = 'N';

update tag_option set value = 'opt in' where tag_id = 'B868DD0D-F2B3-4D76-AB0D-B01F7BE351C5' and value = 'Y';
update tag_option set value = 'opt out' where tag_id = 'B868DD0D-F2B3-4D76-AB0D-B01F7BE351C5' and value = 'N';

update element set question_id = (select q.id from question q join element e on e.question_id = q.id where e.id = '068fe4e6-2d28-11e0-be2b-a4badbe688c6') where question_id = '4C974745-A2F1-41EB-90E9-B875EA17620D';
update answer set question_id = (select q.id from question q join element e on e.question_id = q.id where e.id = '068fe4e6-2d28-11e0-be2b-a4badbe688c6') where question_id = '4C974745-A2F1-41EB-90E9-B875EA17620D';

DECLARE  @duplicateCount int;
DECLARE  @duplicateCustomerId nvarchar(255);
DECLARE  @duplicateQuestionId nvarchar(255);
DECLARE  @duplicateAnsId nvarchar(255);

DECLARE Dup_Answer_Cursor CURSOR FOR select count(*), customer_id, question_id from answer where answer_type = 'ANSWER' group by customer_id, question_id having count(*) > 1 order by customer_id, question_id;
OPEN Dup_Answer_Cursor;
FETCH Dup_Answer_Cursor INTO @duplicateCount, @duplicateCustomerId, @duplicateQuestionId;
WHILE @@Fetch_Status = 0
BEGIN
	SET @duplicateAnsId = '';
	select top 1 @duplicateAnsId = id from answer where customer_id = @duplicateCustomerId and question_id = @duplicateQuestionId and answer_type = 'ANSWER' order by created_date desc;
	if @duplicateAnsId != ''
	BEGIN
		DELETE FROM answer where customer_id = @duplicateCustomerId and question_id = @duplicateQuestionId and id != @duplicateAnsId;
	END;
	FETCH Dup_Answer_Cursor INTO @duplicateCount, @duplicateCustomerId, @duplicateQuestionId;   
END;
CLOSE Dup_Answer_Cursor;
DEALLOCATE Dup_Answer_Cursor;


delete from question where id = '4C974745-A2F1-41EB-90E9-B875EA17620D';

update product set division_id = 'bfdc5888-c26b-4ed5-a580-6aa363a25e12' where id = '8FE6FD5B-F136-4A86-A9FB-7AC3724A8298';

update page_definition set division_id = 'bfdc5888-c26b-4ed5-a580-6aa363a25e12' where id = '8992DD30-D16F-405F-B2C0-7A492491EEC4';

-- ACCOUNT REG PHASE 1 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('262e8967-2067-4a72-bb8a-1847ac0249f7', 0, '','title.accountregistration', 'ACCOUNT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25e12');

update registration_definition set page_definition_id = '262e8967-2067-4a72-bb8a-1847ac0249f7' where page_definition_id = '1B037300-852D-45F8-B45F-F02BD0C18C15' and registration_definition_type = 'ACCOUNT_REGISTRATION' and product_id = '8FE6FD5B-F136-4A86-A9FB-7AC3724A8298' and registration_activation_id = 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E';


-- ACCOUNT

-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '69323313-B3B4-46C5-A4A2-B501B9583829', 0, '262e8967-2067-4a72-bb8a-1847ac0249f7');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '328D4DCF-E39B-4BC8-9900-92EEBB82D30F', 1, '262e8967-2067-4a72-bb8a-1847ac0249f7');

update question set export_name = '[Country]' where id = 'DAA7CFAA-A4C9-4A9E-9A20-7826F49C7A98';
update question set export_name = '[Clubs]' where id = '46E6EEE4-5D91-4996-A02A-96F694379968';
update tag_option set value = 'Kids'' Club' where label = 'label.club.interest.kid.club' and tag_id = '7FAEDE4A-E074-45D3-B93E-B948C4159A8F';
update tag_option set value = 'Other' where label = 'label.club.interest.other' and tag_id = '7FAEDE4A-E074-45D3-B93E-B948C4159A8F';
update message set message = 'Kids'''' Club' where basename = 'messages' and country is null and [key] = 'label.club.interest.kid.club';







