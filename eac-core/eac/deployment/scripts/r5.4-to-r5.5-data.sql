update message set message = '{0} must be a valid email address.' where [key] = 'error.must.be.valid.email' and basename = 'messages' and language is null;
update message set message = 'Your email address and username are different.' where [key] = 'warning.email.username.mismatch' and basename = 'messages' and language is null;

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.logindetails','Login details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'info.username.rules','Your username must be at least 5 characters, with no spaces.');

delete from message where [key] = 'label.username.email' and basename = 'messages' and language is null;
delete from message where [key] = 'title.username.email' and basename = 'messages' and language is null;
delete from message where [key] = 'info.username' and basename = 'messages' and language is null;

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','en','IT',null,'footer.copyright',N'Oxford University Press Srl - P IVA 09467390150<br />Copyright &copy; Oxford University Press, {0}. All Rights Reserved.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activation.error','We are having trouble activating the account, this could be because the activation link was broken on to separate lines by your email program. This deems the link invalid. To try again, please copy and paste the whole activation link (line by line if necessary) into your browsers address bar (with no spaces). Then press enter.');

update url_skin set site_name = 'ELT Home Page' where url = 'http://elt.uat.oup.com' or url = 'http://elt.oup.com';

update message set message = 'Please enter your username in the box below and press reset password. We will email you your password details if you have an account.' where basename = 'messages' and country is null and [key] = 'help.resetpassword';

--Remove unwanted multi-select option 'other' for 'Oxford Teachers' Club' Interests 
delete from tag_option where label='label.interested.other' and tag_id='5A276655-EDD3-42C4-932B-CC0F16AB275B';
delete from message where [key]='label.interested.other';

update message set message = 'This username is already taken. Please <a id="username_taken_login_link" href="{0}">log in</a> to use your existing account or try another username.' where [key] = 'error.username.taken' and basename = 'messages' and language is null;
update message set message = 'Once you complete this information you''''ll have access to our thousands of online resources. The information you provide here will help us to ensure you receive information relevant to you.' where [key] = 'label.teachers.club.preamble' and basename = 'messages' and language is null;

create index answer_customer_idx on answer (customer_id);
