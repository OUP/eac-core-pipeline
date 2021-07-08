-- Mantis 12691 - Translations for Reset Password (Japanese)
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'title.resetpassword',        N'パスワードの再発行');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.resetpassword',        N'パスワードの再発行');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'submit.resetpassword',       N'パスワードの再発行');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.password.reset',       N'パスワードの再発行');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'title.resetpasswordsuccess', N'パスワードの再発行');
-- Mantis 12691 - Translations for Reset Password (Portuguese)
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'title.resetpassword',        N'Alterar Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.resetpassword',        N'Alterar Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'submit.resetpassword',       N'Alterar Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.password.reset',       N'Alterar Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'title.resetpasswordsuccess', N'Alterar Password');
-- Mantis 12691 - Translations for Reset Password (Ukranian)
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'title.resetpassword',        N'змінити пароль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.resetpassword',        N'змінити пароль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'submit.resetpassword',       N'змінити пароль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.password.reset',       N'змінити пароль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'title.resetpasswordsuccess', N'змінити пароль');
-- Mantis 12691 - Translations for Reset Password (Russian)
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'title.resetpassword',        N'поменять пароль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.resetpassword',        N'поменять пароль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'submit.resetpassword',       N'поменять пароль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.password.reset',       N'поменять пароль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'title.resetpasswordsuccess', N'поменять пароль');

-- Mantis 12315 - correcting 'adminsitrator' typo
update message set message = 'There was a problem notifying the customer that their account was not found. Please contact the system administrator.' where [key] = 'error.notifying.customer.when.no.account';

-- Removal of duplicate page definitions
update registration_definition set page_definition_id = '13AC20F6-0414-4569-B6F0-D92FC3AE24A0' where page_definition_id = 'C816B7E7-7138-49A6-84AF-555BEA53B825';
update registration_definition set page_definition_id = '207BFAE7-57CD-4570-BA7B-2F10B7BAA337' where page_definition_id = 'EE67C91C-A753-4C1B-B5CB-F54430C289AA';
delete from page_component where page_definition_id in 
('C816B7E7-7138-49A6-84AF-555BEA53B825', 
'EE67C91C-A753-4C1B-B5CB-F54430C289AA');
delete from page_definition where id in
('C816B7E7-7138-49A6-84AF-555BEA53B825', 
'EE67C91C-A753-4C1B-B5CB-F54430C289AA');

-- Mantis 12686 - shorten Korean instition type names to fit in drop-down
update message set message = 'Elementary School' where [key] = 'label.institution.korean.elementary';
update message set message = 'PLS - Kindergarten & Primary' where [key] = 'label.institution.korean.kindergarten';
update message set message = 'After School Program' where [key] = 'label.institution.korean.after.school';
update message set message = 'Home School/Tutoring' where [key] = 'label.institution.korean.home.schooling';
update message set message = 'University' where [key] = 'label.institution.korean.university';
update message set message = 'High School' where [key] = 'label.institution.korean.high';
update message set message = 'PLS - Secondary' where [key] = 'label.institution.korean.secondary';
update message set message = 'Middle School ' where [key] = 'label.institution.korean.middle';
update message set message = 'Pre-service Teachers' where [key] = 'label.institution.korean.preservice';
update message set message = 'PLS - Adult' where [key] = 'label.institution.korean.adult';

-- Mantis 12670 - updated translations for Korean labels
update message set message=N'아이디' where language='ko' and [key]='label.username';
update message set message=N'아이디를 입력해주세요.' where language='ko' and [key]='label.username.title';
update message set message=N'비밀번호를 입력해주세요.' where language='ko' and [key]='label.password.title';
update message set message=N'로그인이 안되시나요?' where language='ko' and [key]='problems.logging.in';
update message set message=N'비밀번호 재발급' where language='ko' and [key] = 'title.resetpassword';
update message set message=N'비밀번호 재발급' where language='ko' and [key] = 'label.resetpassword';
update message set message=N'비밀번호 재발급' where language='ko' and [key] = 'submit.resetpassword';
update message set message=N'비밀번호 재발급' where language='ko' and [key] = 'label.password.reset';
update message set message=N'안녕하세요.' where language='ko' and [key] = 'label.hello';
update message set message=N'환영합니다. 아래 ''''회원정보 보기'''' 클릭하여 개인정보를 업데이트 해주세요. 만약 공유 컴퓨터를 사용 중 이시면 사용이 끝난 후 반드시 로그아웃 해주세요.' where language='ko' and [key] = 'welcome.message';

