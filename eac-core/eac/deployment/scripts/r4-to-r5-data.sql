--messages for 'update product registration'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.productregistration.update','Update Product Registration');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'submit.productregistration.update','Update Registration');

--missing message for 'account registration page'
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.create.login.info','Please create your log in information for accessing Oxford University Press content.');

--messages for basic profile
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.username.taken.update','This username is already taken. Please try another username.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.email','Email Address');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.locale','Locale');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.timezone','Time Zone');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'registration.status.activated','Activated');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'registration.status.denied','Denied');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'registration.status.awaiting.validation','Awaiting Confirmation');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'registration.status.incomplete','Incomplete');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'registration.status.other','In Progress');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.email','Email Address');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.timezone','Time Zone');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.locale','Locale');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.must.be.valid.username','{0} must be a valid username');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.must.be.valid.timezone','{0} must be a valid timezone');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.title','Your Account');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.product.name','Product Name');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.status','Status');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.licence','Licence');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.manage','Edit registration data');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.submit','Update Account');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.change.password','Change Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.redeem.activation.code','Redeem an Activation Code');

--these are TEMPLATE messages used by 'jquery data table plugin' in profileRegistrationsTable.tag
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.table.info','Showing _START_ to _END_ of _TOTAL_ Registrations');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.table.info.none','You have 0 Registrations');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.table.empty','No data available in table');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.title.registration.table','You currently have access to');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.title.form','Your Details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.basic.please.select','Please Select ...');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.none','No Licence');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.start','Start');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.expires','End');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.type.rolling','Rolling');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.type.concurrent','Concurrent');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.type.usage','Usage');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.type.standard','Standard');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.type.unknown','Unknown');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.concurrent.total.concurrency','Total Concurrency');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.concurrent.user.concurrency','User Concurrency');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.usage.allowed.usages','Allowed Usages');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.usage.usages.remaining','Usages Remaining');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.rolling.begin.on','Begin On');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.rolling.first.use','First Use');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.rolling.time.period','Time Period');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.rolling.unit.type','Unit Type');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.rolling.begin.on.FIRST_USE','First Use');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licence.rolling.begin.on.CREATION','Creation');

DECLARE  @ansId nvarchar(255);
DECLARE  @productId nvarchar(255);
DECLARE  @fieldId nvarchar(255);
DECLARE  @fieldExportName nvarchar(255);

DECLARE Answer_Cursor CURSOR FOR SELECT id, field_id FROM answer;
OPEN Answer_Cursor;
FETCH Answer_Cursor INTO @ansid, @fieldId;
WHILE @@Fetch_Status = 0
BEGIN
	select @fieldExportName = export_name from field where id = @fieldId;
	if @fieldExportName = 'hestudent.title1.malaysia.cw.terms.accepted' or 
		@fieldExportName = 'hestudent.title2.malaysia.cw.terms.accepted' or
		@fieldExportName = 'hestudent.title3.malaysia.cw.terms.accepted' or
		@fieldExportName = 'lecturer.title1.book.adoption' or
		@fieldExportName = 'lecturer.title1.course.level' or
		@fieldExportName = 'lecturer.title1.malaysia.cw.terms.accepted' or
		@fieldExportName = 'lecturer.title1.subjects.taught' or
		@fieldExportName = 'lecturer.title2.book.adoption' or
		@fieldExportName = 'lecturer.title2.course.level' or
		@fieldExportName = 'lecturer.title2.malaysia.cw.terms.accepted' or
		@fieldExportName = 'lecturer.title2.subjects.taught' or
		@fieldExportName = 'lecturer.title3.book.adoption' or
		@fieldExportName = 'lecturer.title3.course.level' or
		@fieldExportName = 'lecturer.title3.malaysia.cw.terms.accepted' or
		@fieldExportName = 'lecturer.title3.subjects.taught' or
		@fieldExportName = 'lecturer.title4.book.adoption' or
		@fieldExportName = 'lecturer.title4.course.level' or
		@fieldExportName = 'lecturer.title4.malaysia.cw.terms.accepted' or
		@fieldExportName = 'lecturer.title4.subjects.taught' or
		@fieldExportName = 'malaysia.cw.terms.accepted' or
		@fieldExportName = 'teacher.level' or 
		@fieldExportName = 'teacher.malaysia.cw.terms.accepted' or
		@fieldExportName = 'teacher.subjects.taught'
	BEGIN
		select @productId = product_id from registration_definition rd 
		join page_definition pd on rd.page_definition_id = pd.id
		join page_component pc on pc.page_definition_id = pd.id
		join component c on pc.component_id = c.id
		join field f on f.component_id = c.id
		where f.id = @fieldId;
		update answer set registerable_product_id = @productId, answer_type = 'PRODUCT_SPECIFIC_ANSWER' where id = @ansid;
	END;
	FETCH Answer_Cursor INTO @ansid, @fieldId;        
END;
CLOSE Answer_Cursor;
DEALLOCATE Answer_Cursor;


DECLARE  @id  nvarchar(255);
DECLARE  @exportName  nvarchar(255);
DECLARE  @newId  nvarchar(255);
DECLARE  @elementText  nvarchar(255);
DECLARE  @prodSpec  bit;

DECLARE Element_Cursor CURSOR FOR SELECT id, element_text FROM element;
OPEN Element_Cursor;
FETCH Element_Cursor INTO @id, @elementText;
WHILE @@Fetch_Status = 0
BEGIN
	SET @newId = NEWID();
	SET @prodSpec = 0;
	select top 1 @exportName = export_name from field where element_id = @id;
	if @exportName = 'hestudent.title1.malaysia.cw.terms.accepted' or 
		@exportName = 'hestudent.title2.malaysia.cw.terms.accepted' or
		@exportName = 'hestudent.title3.malaysia.cw.terms.accepted' or
		@exportName = 'lecturer.title1.book.adoption' or
		@exportName = 'lecturer.title1.course.level' or
		@exportName = 'lecturer.title1.malaysia.cw.terms.accepted' or
		@exportName = 'lecturer.title1.subjects.taught' or
		@exportName = 'lecturer.title2.book.adoption' or
		@exportName = 'lecturer.title2.course.level' or
		@exportName = 'lecturer.title2.malaysia.cw.terms.accepted' or
		@exportName = 'lecturer.title2.subjects.taught' or
		@exportName = 'lecturer.title3.book.adoption' or
		@exportName = 'lecturer.title3.course.level' or
		@exportName = 'lecturer.title3.malaysia.cw.terms.accepted' or
		@exportName = 'lecturer.title3.subjects.taught' or
		@exportName = 'lecturer.title4.book.adoption' or
		@exportName = 'lecturer.title4.course.level' or
		@exportName = 'lecturer.title4.malaysia.cw.terms.accepted' or
		@exportName = 'lecturer.title4.subjects.taught' or
		@exportName = 'malaysia.cw.terms.accepted' or
		@exportName = 'teacher.level' or 
		@exportName = 'teacher.malaysia.cw.terms.accepted' or
		@exportName = 'teacher.subjects.taught'
	BEGIN
		SET @prodSpec = 1;
	END;
	insert into question (id, obj_version, export_name, element_text, product_specific) values (@newId, 0, @exportName, @elementText, @prodSpec);
	update element set question_id = @newId where id = @id;
	FETCH Element_Cursor INTO @id, @elementText;        
END;
CLOSE Element_Cursor;
DEALLOCATE Element_Cursor;



DECLARE  @answerId nvarchar(255);
DECLARE  @answerFieldId nvarchar(255);
DECLARE  @answerQuestionId nvarchar(255);

DECLARE Answer_Question_Cursor CURSOR FOR SELECT id, field_id FROM answer;
OPEN Answer_Question_Cursor;
FETCH Answer_Question_Cursor INTO @answerId, @answerFieldId;
WHILE @@Fetch_Status = 0
BEGIN
	select @answerQuestionId = question_id from element e join field f on f.element_id = e.id where f.id = @answerFieldId;
	update answer set question_id = @answerQuestionId where id = @answerId;
	FETCH Answer_Question_Cursor INTO @answerId, @answerFieldId;       
END;
CLOSE Answer_Question_Cursor;
DEALLOCATE Answer_Question_Cursor;



update question set export_name = 'personal.address.line1' where export_name = 'address.line1';
update question set export_name = 'personal.address.line2' where export_name = 'address.line2';
update question set export_name = 'personal.address.line3' where export_name = 'address.line3';
update question set export_name = 'personal.address.line4' where export_name = 'address.line4';
update question set export_name = 'personal.address.line5' where export_name = 'address.line5';
update question set export_name = 'personal.address.line6' where export_name = 'address.line6';
update question set export_name = 'product.terms.accepted' where export_name = 'hestudent.title1.malaysia.cw.terms.accepted';
update question set export_name = 'product.terms.accepted' where export_name = 'hestudent.title2.malaysia.cw.terms.accepted';
update question set export_name = 'product.terms.accepted' where export_name = 'hestudent.title3.malaysia.cw.terms.accepted';
update question set export_name = 'professional.instituition.type' where export_name = 'instituition';
update question set export_name = 'professional.instituition.type' where export_name = 'instituition.type';
update question set export_name = 'professional.instituition.name' where export_name = 'institution.name';
update question set export_name = 'professional.address.line1' where export_name = 'lecturer.address.line1';
update question set export_name = 'professional.address.line2' where export_name = 'lecturer.address.line2';
update question set export_name = 'professional.address.line3' where export_name = 'lecturer.address.line3';
update question set export_name = 'professional.address.line4' where export_name = 'lecturer.address.line4';
update question set export_name = 'professional.address.line5' where export_name = 'lecturer.address.line5';
update question set export_name = 'professional.address.line6' where export_name = 'lecturer.address.line6';
update question set export_name = 'professional.contact.number' where export_name = 'lecturer.institution.contact.number';
update question set export_name = 'professional.contact.number.type' where export_name = 'lecturer.institution.contact.number.type';
update question set export_name = 'professional.domain.email' where export_name = 'lecturer.institution.domain.email';
update question set export_name = 'professional.instituition.name' where export_name = 'lecturer.institution.name';
update question set export_name = 'position' where export_name = 'lecturer.position';
update question set export_name = 'book.adoption' where export_name = 'lecturer.title1.book.adoption';
update question set export_name = 'course.level' where export_name = 'lecturer.title1.course.level';
update question set export_name = 'product.terms.accepted' where export_name = 'lecturer.title1.malaysia.cw.terms.accepted';
update question set export_name = 'subjects.taught' where export_name = 'lecturer.title1.subjects.taught';
update question set export_name = 'book.adoption' where export_name = 'lecturer.title2.book.adoption';
update question set export_name = 'course.level' where export_name = 'lecturer.title2.course.level';
update question set export_name = 'product.terms.accepted' where export_name = 'lecturer.title2.malaysia.cw.terms.accepted';
update question set export_name = 'subjects.taught' where export_name = 'lecturer.title2.subjects.taught';
update question set export_name = 'book.adoption' where export_name = 'lecturer.title3.book.adoption';
update question set export_name = 'course.level' where export_name = 'lecturer.title3.course.level';
update question set export_name = 'product.terms.accepted' where export_name = 'lecturer.title3.malaysia.cw.terms.accepted';
update question set export_name = 'subjects.taught' where export_name = 'lecturer.title3.subjects.taught';
update question set export_name = 'book.adoption' where export_name = 'lecturer.title4.book.adoption';
update question set export_name = 'course.level' where export_name = 'lecturer.title4.course.level';
update question set export_name = 'product.terms.accepted' where export_name = 'lecturer.title4.malaysia.cw.terms.accepted';
update question set export_name = 'subjects.taught' where export_name = 'lecturer.title4.subjects.taught';
update question set export_name = 'product.terms.accepted' where export_name = 'malaysia.cw.terms.accepted';
update question set export_name = 'professional.address.line1' where export_name = 'teacher.address.line1';
update question set export_name = 'professional.address.line2' where export_name = 'teacher.address.line2';
update question set export_name = 'professional.address.line3' where export_name = 'teacher.address.line3';
update question set export_name = 'professional.address.line4' where export_name = 'teacher.address.line4';
update question set export_name = 'professional.address.line5' where export_name = 'teacher.address.line5';
update question set export_name = 'professional.address.line6' where export_name = 'teacher.address.line6';
update question set export_name = 'professional.contact.number' where export_name = 'teacher.institution.contact.number';
update question set export_name = 'professional.contact.number.type' where export_name = 'teacher.institution.contact.number.type';
update question set export_name = 'professional.domain.email' where export_name = 'teacher.institution.domain.email';
update question set export_name = 'professional.instituition.name' where export_name = 'teacher.institution.name';
update question set export_name = 'product.terms.accepted' where export_name = 'teacher.malaysia.cw.terms.accepted';
update question set export_name = 'position' where export_name = 'teacher.position';
update question set export_name = 'subjects.taught' where export_name = 'teacher.subjects.taught';


DECLARE  @dupCount int;
DECLARE  @dupCustomerId nvarchar(255);
DECLARE  @dupQuestionId nvarchar(255);
DECLARE  @dupAnsId nvarchar(255);

DECLARE Clean_Answer_Cursor CURSOR FOR select count(*), customer_id, question_id from answer where answer_type = 'ANSWER' group by customer_id, question_id having count(*) > 1 order by customer_id, question_id;
OPEN Clean_Answer_Cursor;
FETCH Clean_Answer_Cursor INTO @dupCount, @dupCustomerId, @dupQuestionId;
WHILE @@Fetch_Status = 0
BEGIN
	SET @dupAnsId = '';
	select top 1 @dupAnsId = id from answer where customer_id = @dupCustomerId and question_id = @dupQuestionId and answer_type = 'ANSWER' order by created_date desc;
	if @dupAnsId != ''
	BEGIN
		DELETE FROM answer where customer_id = @dupCustomerId and question_id = @dupQuestionId and id != @dupAnsId;
	END;
	FETCH Clean_Answer_Cursor INTO @dupCount, @dupCustomerId, @dupQuestionId;   
END;
CLOSE Clean_Answer_Cursor;
DEALLOCATE Clean_Answer_Cursor;


update message set message = 'Please enter your username in the box below and press submit. We will email you your password details if you have an account.' where basename = 'messages' and country is null and [key] = 'help.resetpassword';

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.validate.cookie.error','Validate Cookie Error');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.validate.cookie.error','Validate Cookie Error');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'validate.cookie.success.url.blank','The success url parameter {0} is blank');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'validate.cookie.success.url.invalid','The success url parameter {0} is invalid');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'validate.cookie.failure.url.blank','The failure url parameter {0} is blank');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'validate.cookie.failure.url.invalid','The failure url parameter {0} is invalid');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'validate.cookie.error.url.invalid','The error url parameter {0} is invalid');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'validate.cookie.problem.validating.cookie','Unexpected problem trying to validate cookie value {0}');

-- label.hello
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.hello',N'Hello');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','bg',null,null,'label.hello',N'Здравейте');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.hello',N'ようこそ');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.hello',N'Olá');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.hello',N'Привіт');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.hello',N'Привет');

-- label.view.profile
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.view.profile',N'View Profile');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','de',null,null,'label.view.profile',N'Profil ansehen');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.view.profile',N'プロフィールを見る');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.view.profile',N'Ver perfil');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.view.profile',N'Продивитись профіль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.view.profile',N'Продивитись профіль');

-- footer.contactus
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','it',null,null,'footer.contactus',N'Contattaci');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','hu',null,null,'footer.contactus',N'Kapcsolat');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','sk',null,null,'footer.contactus',N'Kontakt');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','cs',null,null,'footer.contactus',N'Kontakty');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','de',null,null,'footer.contactus',N'Kontaktieren Sie uns');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','bg',null,null,'footer.contactus',N'Свържете се  с нас');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'footer.contactus',N'お問い合わせ');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'footer.contactus',N'Contacte-nos');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','fr',null,null,'footer.contactus',N'Contact');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'footer.contactus',N'Контакти');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'footer.contactus',N'Контакты');


-- label.logout
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','it',null,null,'label.logout',N'Esci');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','de',null,null,'label.logout',N'Abmelden');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.logout',N'ログアウト');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.logout',N'Sair');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','fr',null,null,'label.logout',N'Déconnecter');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.logout',N'Вийти');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.logout',N'Выйти');

-- footer.copyright
update message set message = N'Copyright © Oxford University Press, {0}. All Rights Reserved.' where [key] = 'footer.copyright' and language is null;
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','it',null,null,'footer.copyright',N'Oxford University Press Srl - P IVA 09467390150<br />Copyright &copy; Oxford University Press, {0}. All Rights Reserved.');

--label.home
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.home',N'Home');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','hu',null,null,'label.home',N'Főoldal');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','sk',null,null,'label.home',N'Domov');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','de',null,null,'label.home',N'Startseite');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','bg',null,null,'label.home',N'Начало');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.home',N'ホーム');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.home',N'Início');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.home',N'Домашня сторінка');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.home',N'Домашняя страница');

--welcome.message
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'welcome.message',N'Welcome. To update your details click on View profile. If you''''re on a shared computer don''''t forget to log out when you''''re finished.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'welcome.message',N'ようこそ！ 情報を更新するには「プロフィールを見るにクリックを。');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'welcome.message',N'Bem-vindo. Para atualizar a sua informação clique em Ver perfil. Se estiver a utilizar um computador partilhado, não se esqueça de encerrar a sessão quando terminar.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'welcome.message',N'Ласкаво просимо. Щоб поновити ваші дані, натисніть "Продивитись профіль". Якщо ви працюєте за комп’ютером загального користування, коли закінчите, не забудьте вийти з системи.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'welcome.message',N'Добро пожаловать. Если вы пользуетесь общественным компьютером, по окончании работы не забудьте выйти из системы.');

--label.login
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.login',N'ログイン');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.login',N'Entrar');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.login',N'Логін');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.login',N'Логин');

--label.username
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.username',N'ユーザー名');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.username',N'Nome de utilizador');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.username',N'Ім''''я користувача');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.username',N'Имя пользователя');

--label.password
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.password',N'パスワード');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.password',N'Пароль');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.password',N'Пароль');

--label.login.button
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.login.button',N'Login');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','it',null,null,'label.login.button',N'Entra');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','de',null,null,'label.login.button',N'Anmelden');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.login.button',N'ログイン');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.login.button',N'Entrar');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','fr',null,null,'label.login.button',N'Identifiez-vous');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.login.button',N'Ввійти');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.login.button',N'Войти');

--label.username.title
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.username.title',N'Username. This is a required field');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','it',null,null,'label.username.title',N'Campo Obbligatorio');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','de',null,null,'label.username.title',N'Benutzername. Dies ist ein Pflichtfeld');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.username.title',N'ユーザー名を入力してください。');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.username.title',N'Nome de utilizador. Este campo é obrigatório Username.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','fr',null,null,'label.username.title',N'Champ obligatoire');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.username.title',N'Ім''''я користувача. Цей пункт обов''''язковий');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.username.title',N'Имя пользователя. Этот пункт обязателен');

--label.password.title
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.password.title',N'Password. This is a required field');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','it',null,null,'label.password.title',N'Password. Campo Obbligatorio');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','de',null,null,'label.password.title',N'Passwort. Dies ist ein Pflichtfeld');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'label.password.title',N'パスワードを入力してください。');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'label.password.title',N'Password. Este campo é obrigatório');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','fr',null,null,'label.password.title',N'Password.Champ obligatoire');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'label.password.title',N'Пароль. Цей пункт обов''''язковий');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'label.password.title',N'Пароль. Этот пункт обязателен');

--problems.logging.in
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'problems.logging.in',N'Problems logging in?');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','it',null,null,'problems.logging.in',N'Problemi di accesso?');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','de',null,null,'problems.logging.in',N'Probleme beim Anmelden?');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ja',null,null,'problems.logging.in',N'ログインできない場合はこちら');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','pt',null,null,'problems.logging.in',N'Problemas para entrar?');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','fr',null,null,'problems.logging.in',N'Problèmes de connexion?');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','uk',null,null,'problems.logging.in',N'Не можеш ввійти?');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','ru',null,null,'problems.logging.in',N'Не можешь войти?');

update message set message =  REPLACE(REPLACE(REPLACE(REPLACE(message,'\u00ed','í'),'\u00e9','é'),'\u00F1','ñ'),'\u00F3','ó') where language='es';




-- TEACHERS CLUB 

-- PRODUCT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) 
values ('REGISTERABLE','8FE6FD5B-F136-4A86-A9FB-7AC3724A8298', 0, 124608, '', 'Oxford Teachers'' Club', 'bfdc5888-c26b-4ed5-a580-6aa363a25e13', null, 'elt.enquiry@oup.com', '30 minutes');

-- PAGE DEFINITIONS

-- TEACHERS CLUB PRODUCT
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) 
values ('8992DD30-D16F-405F-B2C0-7A492491EEC4', 0, '','title.teachersclub.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25e13');

-- LICENCE TEMPLATES

-- LICENCE
insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) 
values ('7F6379BD-068B-4455-A3DD-41D878F831DD', 0, 'CONCURRENT', null, null, 1, 1, null, null);

--REGISTRATION DEFINITIONS


-- ACCOUNT
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, '8FE6FD5B-F136-4A86-A9FB-7AC3724A8298', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);
-- PRODUCT
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('PRODUCT_REGISTRATION', '71109884-F4BB-4DCC-AF22-E1051F41AF20', 0, '8FE6FD5B-F136-4A86-A9FB-7AC3724A8298', '119452A7-07BB-4B13-A334-BF44D04EAF57', '8992DD30-D16F-405F-B2C0-7A492491EEC4', '7F6379BD-068B-4455-A3DD-41D878F831DD');

-- COMPONENTS

-- PRODUCT

-- PERSONAL DETAILS
insert into component (id,obj_version,label_key) values ('CD0E27F2-3C08-471A-959F-929913FC0F0B', 0, 'label.registration.customer.header');
-- PREFERENCES
insert into component (id,obj_version,label_key) values ('CAD9A9DF-9D7A-49E3-8657-4107E3464A09', 0, 'label.preferences');
-- CONTACT INFORMATION
insert into component (id,obj_version,label_key) values ('69323313-B3B4-46C5-A4A2-B501B9583829', 0, 'label.registration.marketing.header');
-- TERMS AND CONDITIONS
insert into component (id,obj_version,label_key) values ('CDB6784D-AB31-49AF-873A-487F01468BF0', 0, 'label.registration.tandc.header');

--PAGE COMPONENTS

-- PRODUCT
-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'CD0E27F2-3C08-471A-959F-929913FC0F0B', 0, '8992DD30-D16F-405F-B2C0-7A492491EEC4');
-- PREFERENCES
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', 1, '8992DD30-D16F-405F-B2C0-7A492491EEC4');
-- CONTACT INFORMATION
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '69323313-B3B4-46C5-A4A2-B501B9583829', 2, '8992DD30-D16F-405F-B2C0-7A492491EEC4');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'CDB6784D-AB31-49AF-873A-487F01468BF0', 3, '8992DD30-D16F-405F-B2C0-7A492491EEC4');



-- Questions, Elements, Tags, Options, Components

-- PERSONAL DETAILS

-- SCHOOL/UNIVERSITY NAME
insert into question(id,obj_version,export_name,element_text,product_specific) values ('986DFC87-8EFF-4F89-9963-94E1224DF10A', 0, '[Institution]', 'label.school.uni.name', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('10A82FFD-1749-46E2-82FE-C5563B6801D9', 0, '986DFC87-8EFF-4F89-9963-94E1224DF10A', 'title.school.uni.name', 'help.school.uni.name', null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 0, '10A82FFD-1749-46E2-82FE-C5563B6801D9', 'CD0E27F2-3C08-471A-959F-929913FC0F0B', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '10A82FFD-1749-46E2-82FE-C5563B6801D9', '');

-- SCHOOL ADDRESS LINE 1
insert into question(id,obj_version,export_name,element_text,product_specific) values ('E4FD6DF9-00C1-4C9B-959F-E55D56D5B9C0', 0, '[Address1]', 'label.school.address.line1', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('D88D4086-4D8B-4C4B-B906-E3ED57B5E3AD', 0, 'E4FD6DF9-00C1-4C9B-959F-E55D56D5B9C0', 'title.school.address.line1', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 1, 'D88D4086-4D8B-4C4B-B906-E3ED57B5E3AD', 'CD0E27F2-3C08-471A-959F-929913FC0F0B', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'D88D4086-4D8B-4C4B-B906-E3ED57B5E3AD', '');

-- SCHOOL ADDRESS LINE 2
insert into question(id,obj_version,export_name,element_text,product_specific) values ('B3B8E765-7012-4C9B-9144-C35363CC66CE', 0, '[Address2]', 'label.school.address.line2', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('0955FFB7-D9B8-4575-A14F-9F9355282860', 0, 'B3B8E765-7012-4C9B-9144-C35363CC66CE', 'title.school.address.line2', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 2, '0955FFB7-D9B8-4575-A14F-9F9355282860', 'CD0E27F2-3C08-471A-959F-929913FC0F0B', null, 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '0955FFB7-D9B8-4575-A14F-9F9355282860', '');

-- SCHOOL ADDRESS LINE 3
insert into question(id,obj_version,export_name,element_text,product_specific) values ('17900E62-5898-4F14-B745-D370F913E8E2', 0, '[Address3]', 'label.school.address.line3', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('29C447D8-33AE-46F9-90BE-BC6730DB6A48', 0, '17900E62-5898-4F14-B745-D370F913E8E2', 'title.school.address.line3', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 3, '29C447D8-33AE-46F9-90BE-BC6730DB6A48', 'CD0E27F2-3C08-471A-959F-929913FC0F0B', null, 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '29C447D8-33AE-46F9-90BE-BC6730DB6A48', '');

-- SCHOOL TOWN CITY
insert into question(id,obj_version,export_name,element_text,product_specific) values ('A606D28E-6E7B-4F33-B2F2-F7E51DEE257D', 0, '[City]', 'label.school.address.town.city', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('E5097EF5-BF16-4706-818E-7850E8DF2B40', 0, 'A606D28E-6E7B-4F33-B2F2-F7E51DEE257D', 'title.school.address.town.city', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 4, 'E5097EF5-BF16-4706-818E-7850E8DF2B40', 'CD0E27F2-3C08-471A-959F-929913FC0F0B', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'E5097EF5-BF16-4706-818E-7850E8DF2B40', '');

-- SCHOOL POSTAL CODE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('29DDA557-F9BC-476F-85D1-41EDBD362D3A', 0, '[Postcode]', 'label.school.address.postcode.zipcode', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('34A847B6-BF70-4176-BF9F-C39A34B39B17', 0, '29DDA557-F9BC-476F-85D1-41EDBD362D3A', 'title.school.address.postcode.zipcode', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 5, '34A847B6-BF70-4176-BF9F-C39A34B39B17', 'CD0E27F2-3C08-471A-959F-929913FC0F0B', null, 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '34A847B6-BF70-4176-BF9F-C39A34B39B17', '');

-- SCHOOL COUNTRY
insert into question(id,obj_version,export_name,element_text,product_specific) values ('DAA7CFAA-A4C9-4A9E-9A20-7826F49C7A98', 0, 'professional.address.line6', 'label.registration.address.line6', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('E33B969E-E2A8-4D92-A996-4537DB64B209', 0, 'DAA7CFAA-A4C9-4A9E-9A20-7826F49C7A98', 'title.registration.address.line6', '', '');
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 6, 'E33B969E-E2A8-4D92-A996-4537DB64B209', 'CD0E27F2-3C08-471A-959F-929913FC0F0B', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('09853008-7BC1-43E5-A1BC-BB69474164E2', 0, 'SELECT', '', 1, 'E33B969E-E2A8-4D92-A996-4537DB64B209', '');
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
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ba', 27, 'ba', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bw', 28, 'bw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bv', 29, 'bv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.br', 30, 'br', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.io', 31, 'io', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bn', 32, 'bn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bg', 33, 'bg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bf', 34, 'bf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bi', 35, 'bi', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kh', 36, 'kh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cm', 37, 'cm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ca', 38, 'ca', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cv', 39, 'cv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ky', 40, 'ky', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cf', 41, 'cf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.td', 42, 'td', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cl', 43, 'cl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cn', 44, 'cn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cx', 45, 'cx', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cc', 46, 'cc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.co', 47, 'co', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.km', 48, 'km', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cg', 49, 'cg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cd', 50, 'cd', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ck', 51, 'ck', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cr', 52, 'cr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ci', 53, 'ci', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hr', 54, 'hr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cu', 55, 'cu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cy', 56, 'cy', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.cz', 57, 'cz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.dk', 58, 'dk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.dj', 59, 'dj', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.dm', 60, 'dm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.do', 61, 'do', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ec', 62, 'ec', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.eg', 63, 'eg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sv', 64, 'sv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gq', 65, 'gq', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.er', 66, 'er', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ee', 67, 'ee', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.et', 68, 'et', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fk', 69, 'fk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fo', 70, 'fo', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fj', 71, 'fj', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fi', 72, 'fi', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fr', 73, 'fr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gf', 74, 'gf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pf', 75, 'pf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tf', 76, 'tf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ga', 77, 'ga', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gm', 78, 'gm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ge', 79, 'ge', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.de', 80, 'de', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gh', 81, 'gh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gi', 82, 'gi', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gr', 83, 'gr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gl', 84, 'gl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gd', 85, 'gd', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gp', 86, 'gp', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gu', 87, 'gu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gt', 88, 'gt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gg', 89, 'gg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gn', 90, 'gn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gw', 91, 'gw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gy', 92, 'gy', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ht', 93, 'ht', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hm', 94, 'hm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.va', 95, 'va', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hn', 96, 'hn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hk', 97, 'hk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.hu', 98, 'hu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.is', 99, 'is', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.in', 100, 'in', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.id', 101, 'id', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ir', 102, 'ir', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.iq', 103, 'iq', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.im', 104, 'im', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.il', 105, 'il', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.it', 106, 'it', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.jm', 107, 'jm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.jp', 108, 'jp', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.je', 109, 'je', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.jo', 110, 'jo', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kz', 111, 'kz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ke', 112, 'ke', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ki', 113, 'ki', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kp', 114, 'kp', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kr', 115, 'kr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kw', 116, 'kw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kg', 117, 'kg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.la', 118, 'la', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lv', 119, 'lv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lb', 120, 'lb', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ls', 121, 'ls', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lr', 122, 'lr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ly', 123, 'ly', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.li', 124, 'li', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lt', 125, 'lt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lu', 126, 'lu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mo', 127, 'mo', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mk', 128, 'mk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mg', 129, 'mg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mw', 130, 'mw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.my', 131, 'my', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mv', 132, 'mv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ml', 133, 'ml', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mt', 134, 'mt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mh', 135, 'mh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mq', 136, 'mq', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mr', 137, 'mr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mu', 138, 'mu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.yt', 139, 'yt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mx', 140, 'mx', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.fm', 141, 'fm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.md', 142, 'md', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mc', 143, 'mc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mn', 144, 'mn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.me', 145, 'me', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ms', 146, 'ms', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ma', 147, 'ma', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mz', 148, 'mz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mm', 149, 'mm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.na', 150, 'na', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nr', 151, 'nr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.np', 152, 'np', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nl', 153, 'nl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.an', 154, 'an', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nc', 155, 'nc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nz', 156, 'nz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ni', 157, 'ni', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ne', 158, 'ne', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ng', 159, 'ng', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nu', 160, 'nu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.nf', 161, 'nf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mp', 162, 'mp', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.no', 163, 'no', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.om', 164, 'om', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pk', 165, 'pk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pw', 166, 'pw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ps', 167, 'ps', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pa', 168, 'pa', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pg', 169, 'pg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.py', 170, 'py', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pe', 171, 'pe', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ph', 172, 'ph', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pn', 173, 'pn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pl', 174, 'pl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pt', 175, 'pt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pr', 176, 'pr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.qa', 177, 'qa', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.re', 178, 're', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ro', 179, 'ro', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ru', 180, 'ru', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.rw', 181, 'rw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bl', 182, 'bl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sh', 183, 'sh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.kn', 184, 'kn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lc', 185, 'lc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.mf', 186, 'mf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.pm', 187, 'pm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vc', 188, 'vc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ws', 189, 'ws', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sm', 190, 'sm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.st', 191, 'st', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sa', 192, 'sa', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sn', 193, 'sn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sc', 195, 'sc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sl', 196, 'sl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sg', 197, 'sg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sk', 198, 'sk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.si', 199, 'si', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sb', 200, 'sb', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.so', 201, 'so', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.za', 202, 'za', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gs', 203, 'gs', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.es', 204, 'es', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.lk', 205, 'lk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sd', 206, 'sd', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sr', 207, 'sr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sj', 208, 'sj', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sz', 209, 'sz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.se', 210, 'se', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ch', 211, 'ch', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sy', 212, 'sy', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tw', 213, 'tw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tj', 214, 'tj', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tz', 215, 'tz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.th', 216, 'th', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tl', 217, 'tl', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tg', 218, 'tg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tk', 219, 'tk', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.to', 220, 'to', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tt', 221, 'tt', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tn', 222, 'tn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tr', 223, 'tr', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tm', 224, 'tm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tc', 225, 'tc', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.tv', 226, 'tv', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ug', 227, 'ug', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ua', 228, 'ua', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ae', 229, 'ae', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.gb', 230, 'gb', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.us', 231, 'us', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.um', 232, 'um', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.uy', 233, 'uy', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.uz', 234, 'uz', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vu', 235, 'vu', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ve', 236, 've', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vn', 237, 'vn', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vg', 238, 'vg', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vi', 239, 'vi', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.wf', 240, 'wf', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.eh', 241, 'eh', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ye', 242, 'ye', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.zm', 243, 'zm', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.zw', 244, 'zw', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.global', 245, 'global', '09853008-7BC1-43E5-A1BC-BB69474164E2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.other', 8, 'other', '4FE1DF6A-8925-4E93-81F4-4D0E0AA9786D');
--Added by David Hay
--rs is Serbia
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.rs', 246, 'rs', '09853008-7BC1-43E5-A1BC-BB69474164E2');

-- PREFERENCES

-- JOB TYPE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('31B5F283-0C64-4613-ABD8-BA8E9C851CBC', 0, '[JobType]', 'label.job.type', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('BAF6153F-2887-4571-ABA7-1390EDA9EEF2', 0, '31B5F283-0C64-4613-ABD8-BA8E9C851CBC', 'title.job.type', '', '');
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 0, 'BAF6153F-2887-4571-ABA7-1390EDA9EEF2', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', 'teacher', 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('A864D579-78C9-4F82-8CD5-2945669382D2', 0, 'SELECT', '', 0, 'BAF6153F-2887-4571-ABA7-1390EDA9EEF2', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.job.teacher', 0, 'teacher', 'A864D579-78C9-4F82-8CD5-2945669382D2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.job.teacher.trainer', 1, 'teacher trainer', 'A864D579-78C9-4F82-8CD5-2945669382D2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.job.trainee.teacher', 2, 'trainee teacher', 'A864D579-78C9-4F82-8CD5-2945669382D2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.job.freelance.teacher', 3, 'freelance teacher', 'A864D579-78C9-4F82-8CD5-2945669382D2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.job.academic', 4, 'academic', 'A864D579-78C9-4F82-8CD5-2945669382D2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.job.other', 5, 'other', 'A864D579-78C9-4F82-8CD5-2945669382D2');
--added by David Hay
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.job.student', 6, 'student', 'A864D579-78C9-4F82-8CD5-2945669382D2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.job.parent', 7, 'parent', 'A864D579-78C9-4F82-8CD5-2945669382D2');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.job.administrator', 8, 'administrator', 'A864D579-78C9-4F82-8CD5-2945669382D2');

-- OTHER
insert into question(id,obj_version,export_name,element_text,product_specific) values ('0188973E-D047-4599-8D3C-0CCA9EA6640F', 0, '[JobType Other]', 'label.job.type.other', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('BCF4202A-A401-46FC-A672-CB8921535883', 0, '0188973E-D047-4599-8D3C-0CCA9EA6640F', 'title.job.type.other', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 1, 'BCF4202A-A401-46FC-A672-CB8921535883', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'BCF4202A-A401-46FC-A672-CB8921535883', '');

-- INTERESTED IN
insert into question(id,obj_version,export_name,element_text,product_specific) values ('6BCDFF2F-4D55-435F-94D0-AD5AE5C1F155', 0, '[Interests]', 'label.interests', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('5A208FAB-716A-485F-BAC5-5C6BDD58215E', 0, '6BCDFF2F-4D55-435F-94D0-AD5AE5C1F155', 'title.interests', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 2, '5A208FAB-716A-485F-BAC5-5C6BDD58215E', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('5A276655-EDD3-42C4-932B-CC0F16AB275B', 0, 'MULTISELECT', '', 0, '5A208FAB-716A-485F-BAC5-5C6BDD58215E', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.interested.young.learners', 0, 'young learners', '5A276655-EDD3-42C4-932B-CC0F16AB275B');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.interested.teenage', 1, 'teenage', '5A276655-EDD3-42C4-932B-CC0F16AB275B');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.interested.adult', 2, 'adult', '5A276655-EDD3-42C4-932B-CC0F16AB275B');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.interested.business.professional', 3, 'business professional', '5A276655-EDD3-42C4-932B-CC0F16AB275B');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.interested.exams', 4, 'exams', '5A276655-EDD3-42C4-932B-CC0F16AB275B');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.interested.applied.linguistics', 5, 'applied linguistics', '5A276655-EDD3-42C4-932B-CC0F16AB275B');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.interested.teacher.development', 6, 'teacher development', '5A276655-EDD3-42C4-932B-CC0F16AB275B');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.interested.other', 7, 'other', '5A276655-EDD3-42C4-932B-CC0F16AB275B');

-- HEARD ABOUT
insert into question(id,obj_version,export_name,element_text,product_specific) values ('A530B896-F281-482C-B92F-EBB722E00A4C', 0, '[ReferredBy]', 'label.heard.about', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('4C9935F5-C657-4DD2-BE4E-D102C0DCC59C', 0, 'A530B896-F281-482C-B92F-EBB722E00A4C', 'title.heard.about', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 3, '4C9935F5-C657-4DD2-BE4E-D102C0DCC59C', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('06AFC85C-C1D1-44B9-95BE-FB89C973B24F', 0, 'SELECT', '', 1, '4C9935F5-C657-4DD2-BE4E-D102C0DCC59C', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.heard.website.banner', 0, 'website banner', '06AFC85C-C1D1-44B9-95BE-FB89C973B24F');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.heard.website.link', 1, 'website link', '06AFC85C-C1D1-44B9-95BE-FB89C973B24F');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.heard.magazine.advert', 2, 'magazine advert', '06AFC85C-C1D1-44B9-95BE-FB89C973B24F');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.heard.mailing', 3, 'mailing', '06AFC85C-C1D1-44B9-95BE-FB89C973B24F');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.heard.institution', 4, 'institution', '06AFC85C-C1D1-44B9-95BE-FB89C973B24F');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.heard.recommendation', 5, 'recommendation', '06AFC85C-C1D1-44B9-95BE-FB89C973B24F');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.heard.leaflet', 6, 'leaflet', '06AFC85C-C1D1-44B9-95BE-FB89C973B24F');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.heard.other', 7, 'other', '06AFC85C-C1D1-44B9-95BE-FB89C973B24F');

-- CAMPUS
insert into question(id,obj_version,export_name,element_text,product_specific) values ('0CF71E02-BB10-40A1-A6D2-94ACA1CFFD65', 0, '[Campus]', 'label.campus', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('F5B0709F-EF24-4F3F-9111-7E832B1D954E', 0, '0CF71E02-BB10-40A1-A6D2-94ACA1CFFD65', 'title.campus', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 4, 'F5B0709F-EF24-4F3F-9111-7E832B1D954E', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'F5B0709F-EF24-4F3F-9111-7E832B1D954E', '');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, 'F5B0709F-EF24-4F3F-9111-7E832B1D954E', 'ja');

-- DEPARTMENT
insert into question(id,obj_version,export_name,element_text,product_specific) values ('8B8E2949-3EA1-4838-848B-CD8902F03A33', 0, '[Department]', 'label.department', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('1772E44A-B874-414F-88C7-4831CCC65159', 0, '8B8E2949-3EA1-4838-848B-CD8902F03A33', 'title.department', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 5, '1772E44A-B874-414F-88C7-4831CCC65159', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '1772E44A-B874-414F-88C7-4831CCC65159', '');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, '1772E44A-B874-414F-88C7-4831CCC65159', 'ja');

-- INSTITUTION TYPE
insert into question(id,obj_version,export_name,element_text,product_specific) values ('F790C5A0-2C4C-4FCC-ACB4-8A1E752BC03D', 0, '[InstitutionType]', 'label.institutiontype', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('42D37462-0CB0-4EFC-B5D1-04F1BF602F46', 0, 'F790C5A0-2C4C-4FCC-ACB4-8A1E752BC03D', 'title.institutiontype', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 6, '42D37462-0CB0-4EFC-B5D1-04F1BF602F46', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('5AD7C387-683B-4220-A65D-0CBF865B806D', 0, 'SELECT', '', 1, '42D37462-0CB0-4EFC-B5D1-04F1BF602F46', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.elementary', 0, 'elementary', '5AD7C387-683B-4220-A65D-0CBF865B806D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.junior', 1, 'junior', '5AD7C387-683B-4220-A65D-0CBF865B806D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.senior', 2, 'senior', '5AD7C387-683B-4220-A65D-0CBF865B806D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.university.college.juniorcollege', 3, 'university/college/junior college', '5AD7C387-683B-4220-A65D-0CBF865B806D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.private.language', 4, 'language', '5AD7C387-683B-4220-A65D-0CBF865B806D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.school.juku', 5, 'school juku', '5AD7C387-683B-4220-A65D-0CBF865B806D');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.institution.other', 6, 'other', '5AD7C387-683B-4220-A65D-0CBF865B806D');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, '42D37462-0CB0-4EFC-B5D1-04F1BF602F46', 'ja');

-- PRIVATE OR PUBLIC INSTITUTION
insert into question(id,obj_version,export_name,element_text,product_specific) values ('E6C46326-D6F2-4F95-B3BB-25B2336AA245', 0, '[PublicOrPrivateInstitution]', 'label.private.public.institution', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('8006B734-872A-40F5-B9F3-0BFB14E59B77', 0, 'E6C46326-D6F2-4F95-B3BB-25B2336AA245', 'title.private.public.institution', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 7, '8006B734-872A-40F5-B9F3-0BFB14E59B77', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('BA95A8D9-B07B-446E-9E0C-F7EDC3F8451C', 0, 'SELECT', '', 1, '8006B734-872A-40F5-B9F3-0BFB14E59B77', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.privatepublic.private', 0, 'private', 'BA95A8D9-B07B-446E-9E0C-F7EDC3F8451C');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.privatepublic.public', 1, 'public', 'BA95A8D9-B07B-446E-9E0C-F7EDC3F8451C');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.privatepublic.other', 2, 'other', 'BA95A8D9-B07B-446E-9E0C-F7EDC3F8451C');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, '8006B734-872A-40F5-B9F3-0BFB14E59B77', 'ja');

-- NO OF STUDENTS
insert into question(id,obj_version,export_name,element_text,product_specific) values ('DF946911-E13A-49CF-AA74-0BF636CD271D', 0, '[NumberOfStudents]', 'label.number.of.students', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('B0D11F96-8B2A-4CD5-BCAD-28B83C5BBB9A', 0, 'DF946911-E13A-49CF-AA74-0BF636CD271D', 'title.number.of.students', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value,required) values (newId(), 0, 8, 'B0D11F96-8B2A-4CD5-BCAD-28B83C5BBB9A', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'B0D11F96-8B2A-4CD5-BCAD-28B83C5BBB9A', '');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, 'B0D11F96-8B2A-4CD5-BCAD-28B83C5BBB9A', 'ja');

-- TEACHING STATUS
insert into question(id,obj_version,export_name,element_text,product_specific) values ('E06491CC-65BE-4CD9-97F3-3D4FAAEE3B96', 0, '[TeachingStatus]', 'label.teaching.status', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('787171E7-47C1-4956-B1B4-AE3643AF1C36', 0, 'E06491CC-65BE-4CD9-97F3-3D4FAAEE3B96', 'title.teaching.status', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 9, '787171E7-47C1-4956-B1B4-AE3643AF1C36', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('568C98B3-AB7C-440C-BC6B-614A70E3D452', 0, 'SELECT', '', 1, '787171E7-47C1-4956-B1B4-AE3643AF1C36', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.status.full.time', 0, 'full time', '568C98B3-AB7C-440C-BC6B-614A70E3D452');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.status.part.time', 1, 'part time', '568C98B3-AB7C-440C-BC6B-614A70E3D452');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.status.other', 2, 'other', '568C98B3-AB7C-440C-BC6B-614A70E3D452');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, '787171E7-47C1-4956-B1B4-AE3643AF1C36', 'ja');

-- TEACHING INTERESTS
insert into question(id,obj_version,export_name,element_text,product_specific) values ('50007908-5D2A-4776-919A-CCC490CB9258', 0, '[TeachingInterests]', 'label.teaching.interests', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('8B4ED14E-555E-4CC7-B17C-92BA7F625B94', 0, '50007908-5D2A-4776-919A-CCC490CB9258', 'title.teaching.interests', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 10, '8B4ED14E-555E-4CC7-B17C-92BA7F625B94', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('5B54806B-E486-46B8-8E6C-9221662B0410', 0, 'MULTISELECT', '', 0, '8B4ED14E-555E-4CC7-B17C-92BA7F625B94', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.interest.business', 0, 'business', '5B54806B-E486-46B8-8E6C-9221662B0410');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.interest.culturalstudies', 1, 'cultural studies', '5B54806B-E486-46B8-8E6C-9221662B0410');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.interest.dictionaries', 2, 'dictionaries', '5B54806B-E486-46B8-8E6C-9221662B0410');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.interest.grammar.vocabulary', 3, 'grammar vocabulary', '5B54806B-E486-46B8-8E6C-9221662B0410');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.interest.listening.speaking', 4, 'listening speaking', '5B54806B-E486-46B8-8E6C-9221662B0410');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.interest.readers', 5, 'readers', '5B54806B-E486-46B8-8E6C-9221662B0410');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.interest.reading', 6, 'reading', '5B54806B-E486-46B8-8E6C-9221662B0410');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.interest.writing', 7, 'writing', '5B54806B-E486-46B8-8E6C-9221662B0410');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.teaching.status.other', 8, 'other', '5B54806B-E486-46B8-8E6C-9221662B0410');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, '8B4ED14E-555E-4CC7-B17C-92BA7F625B94', 'ja');

-- EXAM INTERESTS
insert into question(id,obj_version,export_name,element_text,product_specific) values ('AD205BE2-93D1-418D-BF1E-FC9CF35B2FF1', 0, '[ExamInterests]', 'label.exam.interests', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('FB332E13-7394-4312-AE7F-12C3C94CD645', 0, 'AD205BE2-93D1-418D-BF1E-FC9CF35B2FF1', 'title.exam.interests', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 11, 'FB332E13-7394-4312-AE7F-12C3C94CD645', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('6F668EB5-1A03-4024-8B14-B41D84A07A07', 0, 'MULTISELECT', '', 0, 'FB332E13-7394-4312-AE7F-12C3C94CD645', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.exam.interest.cae.fce', 0, 'cae fce', '6F668EB5-1A03-4024-8B14-B41D84A07A07');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.exam.interest.eiken', 1, 'eiken', '6F668EB5-1A03-4024-8B14-B41D84A07A07');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.exam.interest.ielts', 2, 'ielts', '6F668EB5-1A03-4024-8B14-B41D84A07A07');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.exam.interest.igcse.gcse', 3, 'igcse gcse', '6F668EB5-1A03-4024-8B14-B41D84A07A07');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.exam.interest.jido.eiken', 4, 'jido eiken', '6F668EB5-1A03-4024-8B14-B41D84A07A07');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.exam.interest.tofel', 5, 'tofel', '6F668EB5-1A03-4024-8B14-B41D84A07A07');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.exam.interest.toeic.listening.reading', 6, 'toeic listening reading', '6F668EB5-1A03-4024-8B14-B41D84A07A07');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.exam.interest.toeic.speaking.writing', 7, 'toeic speaking writing', '6F668EB5-1A03-4024-8B14-B41D84A07A07');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.exam.interest.other', 8, 'other', '6F668EB5-1A03-4024-8B14-B41D84A07A07');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, 'FB332E13-7394-4312-AE7F-12C3C94CD645', 'ja');

-- CLUB INTERESTS
insert into question(id,obj_version,export_name,element_text,product_specific) values ('46E6EEE4-5D91-4996-A02A-96F694379968', 0, '[ClubInterests]', 'label.club.interests', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('55A00AA4-055A-4408-BF9F-4A8ABF3CA886', 0, '46E6EEE4-5D91-4996-A02A-96F694379968', 'title.club.interests', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 12, '55A00AA4-055A-4408-BF9F-4A8ABF3CA886', 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('7FAEDE4A-E074-45D3-B93E-B948C4159A8F', 0, 'RADIO', '', 1, '55A00AA4-055A-4408-BF9F-4A8ABF3CA886', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.club.interest.kid.club', 0, 'full time', '7FAEDE4A-E074-45D3-B93E-B948C4159A8F');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.club.interest.other', 1, 'other', '7FAEDE4A-E074-45D3-B93E-B948C4159A8F');
insert into element_country_restriction(id, obj_version, element_id, locale) values(newId(), 0, '55A00AA4-055A-4408-BF9F-4A8ABF3CA886', 'ja');

--CONTACT INFO OPT IN-OUT
insert into question(id,obj_version,export_name,element_text,product_specific) values ('4C974745-A2F1-41EB-90E9-B875EA17620D', 0, '[Marketing]', 'label.registration.marketing', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('0FF40CFB-5154-4B90-85AB-926C1638901A', 0, '4C974745-A2F1-41EB-90E9-B875EA17620D', 'title.registration.marketing', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 0, '0FF40CFB-5154-4B90-85AB-926C1638901A', '69323313-B3B4-46C5-A4A2-B501B9583829', 'opt in', 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('B868DD0D-F2B3-4D76-AB0D-B01F7BE351C5', 0, 'RADIO', '', 1, '0FF40CFB-5154-4B90-85AB-926C1638901A', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.marketing.optin', 1, 'opt in', 'B868DD0D-F2B3-4D76-AB0D-B01F7BE351C5');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.marketing.optout', 2, 'opt out', 'B868DD0D-F2B3-4D76-AB0D-B01F7BE351C5');

--TEACHERS CLUB ACCEPT TERMS AND CONDITIONS
insert into question(id,obj_version,export_name,element_text,product_specific) values ('6112915F-80B4-49CD-B05C-1D164D854ADE', 0, '[TANDC]', 'label.registration.teachers.club.tandc', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('A6D3AC65-E5EF-4F77-8230-94F0BA4145AB', 0, '6112915F-80B4-49CD-B05C-1D164D854ADE', 'title.registration.teachers.club.tandc', null, null);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 0, 'A6D3AC65-E5EF-4F77-8230-94F0BA4145AB', 'CDB6784D-AB31-49AF-873A-487F01468BF0', null, 1);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'CHECKBOX', '', 0, 'A6D3AC65-E5EF-4F77-8230-94F0BA4145AB', '');


--TEACHERS CLUB ACCEPT TERMS AND CONDITIONS LINK
insert into question(id,obj_version,export_name,element_text,product_specific) values ('4B997DAE-9B18-44DB-A36C-83A9937DFF16', 0, '[TANDCLINK]', 'label.registration.teachers.club.tandc.link', 0);
insert into element (id,obj_version,question_id,title_text,help_text,regular_expression) values ('E24D2AC5-3832-4AD1-A346-5224150EDF51', 0, '4B997DAE-9B18-44DB-A36C-83A9937DFF16', 'title.registration.teachers.club.tandc.link', '', '');
insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 1, 'E24D2AC5-3832-4AD1-A346-5224150EDF51', 'CDB6784D-AB31-49AF-873A-487F01468BF0', '', 0);
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url, new_window) values (newId(), 0, 'URLLINK', '', 0, 'E24D2AC5-3832-4AD1-A346-5224150EDF51', 'http://elt.oup.com/termsandconditions', 1);


update field set default_value = 'opt in' where element_id = '068fe4e6-2d28-11e0-be2b-a4badbe688c6';

-- MESSAGES

update message set message = 'Register your account' where [key] = 'title.accountregistration' and basename = 'messages' and language is null;
update message set message = 'Account details' where [key] = 'label.personaldetails' and basename = 'messages' and language is null;
update message set message = 'Contact Information' where [key] = 'label.registration.marketing.header' and basename = 'messages' and language is null;
update message set message = 'Fields marked with <span class="mandatory">*</span> are mandatory' where [key] = 'label.mandatory' and basename = 'messages' and language is null;
update message set message = 'If you are a new customer, please create the account information you will use to log in.<br/>If you have previously registered your details with Oxford University Press, <a href="{0}">please try logging in now</a> to get access.' where [key] = 'label.create.login.info' and basename = 'messages' and language is null;
update message set message = 'Read our Terms and Conditions and Privacy Policy' where [key] = 'label.registration.tandc.header' and basename = 'messages' and language is null;

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.username.email','Username/Email address');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.username.email','Username/Email address');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.school.uni.name','School/University name');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.school.uni.name','School/University name');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'help.school.uni.name','If you are a freelance teacher please enter “Freelance” in the School / University name field and enter your home address in the school address fields');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.school.address.line1','School Address Line 1');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.school.address.line1','School Address Line 1');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.school.address.line2','School Address Line 2');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.school.address.line2','School Address Line 2');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.school.address.line3','School Address Line 3');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.school.address.town.city','Town/City');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.school.address.town.city','Town/City');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.school.address.postcode.zipcode','Postal / zip code');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.school.address.postcode.zipcode','Postal / zip code');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.type','I am a / an');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.job.type','I am a / an');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.type.other','If other, please specify');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.job.type.other','If other, please specify');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.interests','I am interested in');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.interests','I am interested in');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.preferences','Preferences');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.heard.about','I heard about this website from');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.heard.about','I heard about this website from');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.campus','Campus');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.campus','Campus');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institutiontype','Institution Type');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.institutiontype','Institution Type');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.private.public.institution','Private or public institution');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.private.public.institution','Private or public institution');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.number.of.students','Number of Students');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.number.of.students','Number of Students');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.status','Teaching Status');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.teaching.status','Teaching Status');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interests','Teaching Interests');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.teaching.interests','Teaching Interests');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interests','Exam Interests');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.exam.interests','Exam Interests');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.club.interests','Club Interests');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.club.interests','Club Interests');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.teachers.club.tandc','I have read and agreed to the terms and conditions of use for Oxford Teachers&#39; Club');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.teachers.club.tandc','I have read and agreed to the terms and conditions of use for Oxford Teachers&#39; Club');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.teachers.club.tandc.link','Read our Terms and Conditions and Privacy Policy');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.teachers.club.tandc.link','Read our Terms and Conditions and Privacy Policy');




insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.teacher','Teacher');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.teacher.trainer','Teacher Trainer');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.trainee.teacher','Trainee Teacher');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.freelance.teacher','Freelance Teacher');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.academic','Academic');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.other','Other - not listed');
--added by David Hay
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.student','Student');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.parent','Parent');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.job.administrator','Administrator');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.interested.young.learners','Young Learners');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.interested.teenage','Teenage');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.interested.adult','Adult');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.interested.business.professional','Business / Professional');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.interested.exams','Exams');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.interested.applied.linguistics','Applied Linguistics');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.interested.teacher.development','Teacher Development');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.interested.other','Other');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.heard.website.banner','Website banner');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.heard.website.link','Website link');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.heard.magazine.advert','Magazine advert');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.heard.mailing','Mailing');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.heard.institution', 'Visit to my institution');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.heard.recommendation','Recommendation');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.heard.leaflet','Leaflet at a conference');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.heard.other','Other');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.elementary','Elementary School');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.junior','Junior High School');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.senior','Senior High School');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.university.college.juniorcollege','University / College / Junior College');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.private.language', 'Private Language');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.school.juku','School / Juku');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.other','Other');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.privatepublic.private','Private');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.privatepublic.public','Public');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.privatepublic.other','Other');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.status.full.time','Full Time Teacher');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.status.part.time','Part Time Teacher');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.status.other','Other');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interest.business','Business / ESP');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interest.culturalstudies','Cultural Studies');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interest.dictionaries','Dictionaries');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interest.grammar.vocabulary','Grammar / Vocabulary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interest.listening.speaking','Listening / Speaking');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interest.readers','Readers');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interest.reading','Reading');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interest.writing','Writing');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.teaching.interest.other','Other');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.cae.fce','CAE / FCE');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.eiken','Eiken (STEP)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.ielts','IELTS');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.igcse.gcse','IGCSE / GCSE');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.jido.eiken','Jido Eiken (Junior STEP)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.tofel','TOEFL');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.toeic.listening.reading','TOEIC Listening / Reading');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.toeic.speaking.writing','TOEIC Speaking / Writing');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.exam.interest.other','Other');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.club.interest.kid.club','Kids'''' Club');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.club.interest.other','Other');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.af','Afghanistan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ax','Åland Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.al','Albania');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.dz','Algeria');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.as','American Samoa');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ad','Andorra');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ao','Angola');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ai','Anguilla');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.aq','Antarctica');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ag','Antigua and Barbuda');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ar','Argentina');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.am','Armenia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.aw','Aruba');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.au','Australia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.at','Austria');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.az','Azerbaijan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bs','Bahamas');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bh','Bahrain');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bd','Bangladesh');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bb','Barbados');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.by','Belarus');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.be','Belgium');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bz','Belize');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bj','Benin');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bm','Bermuda');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bt','Bhutan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bo','Bolivia, Plurinational State of');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ba','Bosnia and Herzegovina');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bw','Botswana');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bv','Bouvet Island');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.br','Brazil');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.io','British Indian Ocean Territory');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bg','Bulgaria');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bf','Burkina Faso');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bi','Burundi');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.kh','Cambodia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cm','Cameroon');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ca','Canada');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cv','Cape Verde');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ky','Cayman Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cf','Central African Republic');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.td','Chad');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cl','Chile');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cn','China');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cx','Christmas Island');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cc','Cocos (Keeling) Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.co','Colombia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.km','Comoros');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cg','Congo');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cd','Zaire');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ck','Cook Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cr','Costa Rica');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ci','Côte d&#039;Ivoire');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.hr','Croatia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cu','Cuba');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cy','Cyprus');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.cz','Czech Republic');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.dk','Denmark');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.dj','Djibouti');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.dm','Dominica');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.do','Dominican Republic');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ec','Ecuador');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.eg','Egypt');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sv','El Salvador');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gq','Equatorial Guinea');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.er','Eritrea');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ee','Estonia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.et','Ethiopia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.fk','Falkland Islands (Malvinas)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.fo','Faroe Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.fj','Fiji');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.fi','Finland');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.fr','France');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gf','French Guiana');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pf','French Polynesia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tf','French Southern Territories');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ga','Gabon');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gm','Gambia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ge','Georgia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.de','Germany and Austria');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gh','Ghana');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gi','Gibraltar');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gr','Greece and Cyprus');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gl','Greenland');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gd','Grenada');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gp','Guadeloupe');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gu','Guam');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gt','Guatemala');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gg','Guernsey');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gn','Guinea');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gw','Guinea-Bissau');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gy','Guyana');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ht','Haiti');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.hm','Heard Island and McDonald Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.va','Holy See (Vatican City State)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.hn','Honduras');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.hk','Hong Kong');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.hu','Hungary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.is','Iceland');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.in','India');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ir','Iran, Islamic Republic of');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.iq','Iraq');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.im','Isle of Man');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.il','Israel');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.it','Italy');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.jm','Jamaica');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.jp','Japan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.je','Jersey');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.jo','Jordan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.kz','Kazakhstan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ke','Kenya');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ki','Kiribati');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.kp','Korea, Democratic People&#039;s Republic of');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.kr','Korea, Republic of');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.kw','Kuwait');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.kg','Kyrgyzstan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.la','Lao People&#039;s Democratic Republic');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.lv','Latvia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.lb','Lebanon');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ls','Lesotho');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.lr','Liberia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ly','Libyan Arab Jamahiriya');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.li','Liechtenstein');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.lt','Lithuania');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.lu','Luxembourg');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mo','Macao');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mk','Macedonia, The Former Yugoslav Republic of');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mg','Madagascar');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mw','Malawi');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mv','Maldives');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ml','Mali');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mt','Malta');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mh','Marshall Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mq','Martinique');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mr','Mauritania');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mu','Mauritius');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.yt','Mayotte');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mx','Mexico');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.fm','Micronesia, Federated States of');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.md','Moldova, Republic of');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mc','Monaco');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mn','Mongolia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.me','Montenegro');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ms','Montserrat');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ma','Morocco');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mz','Mozambique');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mm','Myanmar');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.na','Namibia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.nr','Nauru');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.np','Nepal');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.nl','Netherlands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.an','Netherlands Antilles');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.nc','New Caledonia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.nz','New Zealand');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ni','Nicaragua');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ne','Niger');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ng','Nigeria');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.nu','Niue');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.nf','Norfolk Island');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mp','Northern Mariana Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.no','Norway');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.om','Oman');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pk','Pakistan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pw','Palau');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ps','Palestinian Territory, Occupied');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pa','Panama');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pg','Papua New Guinea');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.py','Paraguay');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pe','Peru');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pn','Pitcairn');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pl','Poland');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pt','Portugal');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pr','Puerto Rico');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.qa','Qatar');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.re','Réunion');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ro','Romania');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ru','Russian Federation');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.rw','Rwanda');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bl','Saint Barthélemy');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sh','Saint Helena, Ascension and Tristan da Cunha');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.kn','Saint Kitts and Nevis');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.lc','Saint Lucia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.mf','Saint Martin');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.pm','Saint Pierre and Miquelon');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.vc','Saint Vincent and the Grenadines');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ws','Samoa');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sm','San Marino');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.st','Sao Tome and Principe');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sa','Saudi Arabia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sn','Senegal');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.rs','Serbia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sc','Seychelles');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sl','Sierra Leone');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sk','Slovakia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.si','Slovenia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sb','Solomon Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.so','Somalia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.za','South Africa');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gs','South Georgia And The South Sandwich Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.es','Spain');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.lk','Sri Lanka');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sd','Sudan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sr','Suriname');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sj','Svalbard And Jan Mayen');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sz','Swaziland');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.se','Sweden');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ch','Switzerland');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sy','Syrian Arab Republic');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tw','Taiwan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tj','Tajikistan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tz','Tanzania, United Republic of');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tl','Timor-Leste');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tg','Togo');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tk','Tokelau');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.to','Tonga');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tt','Trinidad and Tobago');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tn','Tunisia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tr','Turkey');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tm','Turkmenistan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tc','Turks and Caicos Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.tv','Tuvalu');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ug','Uganda');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ua','Ukraine');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ae','United Arab Emirates');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.gb','United Kingdom and Ireland');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.us','United States');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.um','United States Minor Outlying Islands');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.uy','Uruguay');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.uz','Uzbekistan');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.vu','Vanuatu');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ve','Venezuela, Bolivarian Republic of');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.vg','Virgin Islands, British');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.vi','Virgin Islands, U.S.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.wf','Wallis And Futuna');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.eh','Western Sahara');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ye','Yemen');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.zm','Zambia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.zw','Zimbabwe');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.global','Worldwide');



update message set message = 'Copyright &#169; Oxford University Press, {0}. All Rights Reserved.' where basename = 'messages' and country is null and [key] = 'footer.copyright' and language <> 'it';
update message set message = 'Thank you for registering. An email has been sent to the email address you supplied, in the email will be a link to click on to confirm your account. This will activate your access to {0}.' where basename = 'messages' and country is null and [key] = 'label.activatelicence';
update message set message = 'If your activation email does not arrive within {1} please look in your spam or junk mail folder. If the email is not there please contact {2} for assistance.' where basename = 'messages' and country is null and [key] = 'label.activatelicenceemail';

insert into message (id,basename,language,country,variant,[key],message) values (newid(), 'messages',null,null,null,'label.teachers.club.preamble','Once you complete this information you’ll have access to our thousands of online resources.');
update page_definition set preamble = 'label.teachers.club.preamble' where id = '8992DD30-D16F-405F-B2C0-7A492491EEC4'

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.licence.rolling','Access will last for {0} from ');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.licence.usage','Access will last for {0} usage(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.licence.concurrent','Access for {0} concurrent users');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'licence.status.active','Active');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'licence.status.inactive','Inactive');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'licence.status.expired','Expired');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'licence.status.disabled','Disabled');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'profile.licence.subject.to','Subject to');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'licence.status.no.licence','No Licence');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'.status.no.registration','No Registration');

DECLARE @nameofdb varchar(80)
SET @nameofdb = (SELECT DB_NAME())

IF @nameofdb = 'eactest'
  BEGIN
    PRINT 'Making Teachers Club Product have a landing page'; 
    
    update product  
        set landing_page='http://dev.eac.uk.oup.com/eacSampleSite/protected/teachersclub/testResource1.jsp' 
        where id = ( 
            select P.id from product P where P.product_name like 'Oxford Teachers%Club'
         )
  END
ELSE
  BEGIN
    PRINT 'NOT Making Teachers Club Product have a landing page'; 
  END;
  

IF @nameofdb = 'eactest'
BEGIN
  PRINT 'Adding an external id for teachers club product for testing direct registration link'; 
-- add an external id for teachers club so they can register via  http://dev.eac.uk.oup.com/eac/register.htm?systemId=OTC&typeId=eacProdId&prodId=OTC1

insert into external_system (id, obj_version, name, description ) values ('A18D78F1-0D8E-421B-9A2B-6D5D77D41A4A',0, 'OTC', 'Oxford Teachers Club');

insert into external_system_id_type (id, obj_version, name, description, external_system_id ) values ('A473E919-82E3-4073-B671-53821A1470BF',0, 'eacProdId', 'OTC EAC Product Id', 'A18D78F1-0D8E-421B-9A2B-6D5D77D41A4A');

insert into external_identifier (id,obj_version,external_system_id_type_id, external_id, external_id_type,product_id) values 
('03A4ACA9-9E49-4588-A612-77CE1BC12465',0,'A473E919-82E3-4073-B671-53821A1470BF', 'OTC1', 'PRODUCT','8FE6FD5B-F136-4A86-A9FB-7AC3724A8298');

END;



-- PROGRESS BARS

-- ACCOUNT CREATION

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('0991FD82-09FA-4162-A091-1ED244C31F6B', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'REGULAR', 'NA', 'NEW');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '0991FD82-09FA-4162-A091-1ED244C31F6B', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '0991FD82-09FA-4162-A091-1ED244C31F6B', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '0991FD82-09FA-4162-A091-1ED244C31F6B', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '0991FD82-09FA-4162-A091-1ED244C31F6B', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('2FD50913-2A6C-4402-A2C0-818113A42431', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'REGULAR', 'NA', 'NEW');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '2FD50913-2A6C-4402-A2C0-818113A42431', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '2FD50913-2A6C-4402-A2C0-818113A42431', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '2FD50913-2A6C-4402-A2C0-818113A42431', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '2FD50913-2A6C-4402-A2C0-818113A42431', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('255FEDF3-9435-40D4-BCCF-09FACCDD8562', 0, 'UNKNOWN', 'VALIDATED', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'REGULAR', 'NA', 'NEW');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '255FEDF3-9435-40D4-BCCF-09FACCDD8562', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '255FEDF3-9435-40D4-BCCF-09FACCDD8562', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', '255FEDF3-9435-40D4-BCCF-09FACCDD8562', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '255FEDF3-9435-40D4-BCCF-09FACCDD8562', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('396B1384-E039-4813-A7FC-C3A72F0A52AE', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'NO', 'NEW');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '396B1384-E039-4813-A7FC-C3A72F0A52AE', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '396B1384-E039-4813-A7FC-C3A72F0A52AE', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '396B1384-E039-4813-A7FC-C3A72F0A52AE', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '396B1384-E039-4813-A7FC-C3A72F0A52AE', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '396B1384-E039-4813-A7FC-C3A72F0A52AE', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('D28A080D-61BA-4CCE-9D65-397B00DAC284', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'YES', 'NEW');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'NO', 'NEW');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('8c8aa1b9-8126-42fb-9903-0833b3f8600b', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'DIRECT', 'NEW');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 5);



-- PRODUCT REGISTRATION

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('973AD2B3-9875-4394-9A59-3828701BAF92', 0, 'NONE', 'INSTANT', 'PRODUCT_REGISTRATION', 'NONE', 'REGULAR', 'NA', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '973AD2B3-9875-4394-9A59-3828701BAF92', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', '973AD2B3-9875-4394-9A59-3828701BAF92', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '973AD2B3-9875-4394-9A59-3828701BAF92', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '973AD2B3-9875-4394-9A59-3828701BAF92', 4);



insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('67BCA8B5-7802-44E1-B9F8-51342D388377', 0, 'NONE', 'SELF', 'PRODUCT_REGISTRATION', 'NONE', 'REGULAR', 'NA', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '67BCA8B5-7802-44E1-B9F8-51342D388377', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', '67BCA8B5-7802-44E1-B9F8-51342D388377', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '67BCA8B5-7802-44E1-B9F8-51342D388377', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '67BCA8B5-7802-44E1-B9F8-51342D388377', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 0, 'NONE', 'VALIDATED', 'PRODUCT_REGISTRATION', 'NONE', 'REGULAR', 'NA', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', '1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', '1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('ED121E2F-9923-400D-A416-8D4FEA06813B', 0, 'NONE', 'INSTANT', 'PRODUCT_REGISTRATION', 'NONE', 'TOKEN', 'YES', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ED121E2F-9923-400D-A416-8D4FEA06813B', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ED121E2F-9923-400D-A416-8D4FEA06813B', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ED121E2F-9923-400D-A416-8D4FEA06813B', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', 'ED121E2F-9923-400D-A416-8D4FEA06813B', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'COMPLETE_NO_STEP', null, 'ED121E2F-9923-400D-A416-8D4FEA06813B', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 0, 'NONE', 'SELF', 'PRODUCT_REGISTRATION', 'NONE', 'TOKEN', 'YES', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 5);

-- ACTIVATE LICENCE

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('9E327F16-1C2D-4B59-BB78-EF20E963E610', 0, 'AWAITING', 'SELF', 'ACTIVATE_LICENCE', 'COMPLETE', 'REGULAR', 'NA', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '9E327F16-1C2D-4B59-BB78-EF20E963E610', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '9E327F16-1C2D-4B59-BB78-EF20E963E610', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', '9E327F16-1C2D-4B59-BB78-EF20E963E610', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '9E327F16-1C2D-4B59-BB78-EF20E963E610', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 0, 'AWAITING', 'SELF', 'ACTIVATE_LICENCE', 'COMPLETE', 'TOKEN', 'YES', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'COMPLETE_NO_STEP', null, 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 5);


-- AWAITING LICENCE ACTIVATION

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('5A666935-A64D-4E23-9E63-3178759FFC31', 0, 'AWAITING', 'VALIDATED', 'AWAITING_LICENCE_ACTIVATION', 'COMPLETE', 'REGULAR', 'NA', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '5A666935-A64D-4E23-9E63-3178759FFC31', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '5A666935-A64D-4E23-9E63-3178759FFC31', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Awaiting confirmation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting', '5A666935-A64D-4E23-9E63-3178759FFC31', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '5A666935-A64D-4E23-9E63-3178759FFC31', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('4BA78049-76DE-419E-87D4-4DA9730268FE', 0, 'AWAITING', 'VALIDATED', 'AWAITING_LICENCE_ACTIVATION', 'COMPLETE', 'TOKEN', 'YES', 'LOGGEDIN');


insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '4BA78049-76DE-419E-87D4-4DA9730268FE', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '4BA78049-76DE-419E-87D4-4DA9730268FE', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '4BA78049-76DE-419E-87D4-4DA9730268FE', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Awaiting confirmation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting', '4BA78049-76DE-419E-87D4-4DA9730268FE', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '4BA78049-76DE-419E-87D4-4DA9730268FE', 5);


-- VALIDATION DENIED

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 0, 'DENIED', 'VALIDATED', 'VALIDATION_DENIED', 'COMPLETE', 'REGULAR', 'NA', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment denied', 'CURRENT_COMPLETED_STEP', 'label.progress.denied', 'C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'COMPLETE_NO_STEP', null, 'C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('19D6334C-61D0-406F-8AED-D3C5AD358CA4', 0, 'DENIED', 'VALIDATED', 'VALIDATION_DENIED', 'COMPLETE', 'TOKEN', 'YES', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment denied', 'CURRENT_COMPLETED_STEP', 'label.progress.denied', '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'COMPLETE_NO_STEP', null, '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 5);


-- ACTIVATION CODE

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 0, 'UNKNOWN', 'SELF', 'ACTIVATION_CODE', 'UNKNOWN', 'TOKEN', 'NO', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Activate', 'CURRENT_COMPLETED_STEP', 'label.progress.activate', '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state)
values('2B535F10-21FF-4CE2-AC89-06050BD83E9E', 0, 'UNKNOWN', 'VALIDATED', 'ACTIVATION_CODE', 'UNKNOWN', 'TOKEN', 'NO', 'LOGGEDIN');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'CURRENT_COMPLETED_STEP', null, '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Activate', 'CURRENT_COMPLETED_STEP', 'label.progress.activate', '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values(newId(), 0, null, 'INCOMPLETE_NO_STEP', null, '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 5);


insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.progress.yourdetails', 'Your details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.progress.enrolment', 'Enrolment');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.progress.activate', 'Activate');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.progress.awaiting', 'Awaiting confirmation');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.progress.denied', 'Access denied');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.progress.confirm', 'Confirm');








insert into role (id, obj_version, name) values ('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', 0, 'SYSTEM_ADMIN');
insert into role (id, obj_version, name) values ('04B20C68-DF39-41C3-B70E-B4A3F4F27657', 0, 'DIVISION_ADMIN');
insert into role (id, obj_version, name) values ('353FB7EC-8F39-4A41-B4A7-28C06B52E410', 0, 'PRODUCTION_CONTROLLER');
insert into role (id, obj_version, name) values ('D5F17EEA-BDB0-426D-8328-45FCBF83433E', 0, 'PUBLICATIONS_OFFICER');
insert into role (id, obj_version, name) values ('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', 0, 'FIELD_REP');

insert into permission (id, obj_version, name) values ('E4DBC4A1-2B1C-4753-9D6E-5DD341748CDC', 0, 'CREATE_PRODUCT');
insert into permission (id, obj_version, name) values ('2305CCAF-1F9D-407B-8917-99B5ECB471FA', 0, 'LIST_PRODUCT');
insert into permission (id, obj_version, name) values ('4DACE198-88E2-46AD-ADF9-CCA22CD30974', 0, 'READ_PRODUCT');
insert into permission (id, obj_version, name) values ('D3BB14B7-0A83-424D-9294-23AB76577683', 0, 'UPDATE_PRODUCT');
insert into permission (id, obj_version, name) values ('D7A27D8E-2AA4-4F50-AF47-6FD9F0F7FA2F', 0, 'DELETE_PRODUCT');

insert into permission (id, obj_version, name) values ('DC68DDCB-B7C9-46C7-A7D3-DF154C56E44E', 0, 'CREATE_CUSTOMER');
insert into permission (id, obj_version, name) values ('C0F2E02B-921D-4DD0-8C56-16F60B2337CD', 0, 'LIST_CUSTOMER');
insert into permission (id, obj_version, name) values ('E32FE82F-95AC-44EB-AFFA-BDD8932491A7', 0, 'READ_CUSTOMER');
insert into permission (id, obj_version, name) values ('53DC354A-B96B-4BE8-AE86-67CB9F72FC46', 0, 'UPDATE_CUSTOMER');
insert into permission (id, obj_version, name) values ('C1959FC0-97D1-4710-821E-D69AACFEAD86', 0, 'DELETE_CUSTOMER');

insert into permission (id, obj_version, name) values ('91CB7A26-E805-4C36-9D74-1E887F9B0F0E', 0, 'CREATE_REGISTRATION');
insert into permission (id, obj_version, name) values ('D5780F67-8B0D-4398-953F-80D9FE06363B', 0, 'LIST_REGISTRATION');
insert into permission (id, obj_version, name) values ('11265F0E-F765-411D-A406-4FD9D3BC9203', 0, 'READ_REGISTRATION');
insert into permission (id, obj_version, name) values ('37B99122-E8EE-47CA-89F7-E18D4D526C57', 0, 'UPDATE_REGISTRATION');
insert into permission (id, obj_version, name) values ('87B197D7-697C-405B-8C59-7FA4C89F471B', 0, 'DELETE_REGISTRATION');

insert into permission (id, obj_version, name) values ('D5CD4186-6920-4166-BBE7-A6C246C6D056', 0, 'CREATE_REGISTRATION_DEFINITION');
insert into permission (id, obj_version, name) values ('AA77640E-6D1D-41B0-A23F-05430189C98A', 0, 'LIST_REGISTRATION_DEFINITION');
insert into permission (id, obj_version, name) values ('8023598D-D18A-4D46-AAFF-3E62A4EDD31B', 0, 'READ_REGISTRATION_DEFINITION');
insert into permission (id, obj_version, name) values ('BDB528F8-34A3-4702-97A3-CD414E6FAF8E', 0, 'UPDATE_REGISTRATION_DEFINITION');
insert into permission (id, obj_version, name) values ('F63C2C35-7585-40C5-A17E-3F61321DF434', 0, 'DELETE_REGISTRATION_DEFINITION');

insert into permission (id, obj_version, name) values ('828613D8-BAA9-42B7-A469-D32269FB398D', 0, 'CREATE_ACTIVATION_CODE_BATCH');
insert into permission (id, obj_version, name) values ('909A333A-6E54-45B7-9140-D0B1B511E82E', 0, 'LIST_ACTIVATION_CODE_BATCH');
insert into permission (id, obj_version, name) values ('7A00CA5F-7843-4C47-978D-CA5F6F443750', 0, 'READ_ACTIVATION_CODE_BATCH');
insert into permission (id, obj_version, name) values ('61E1522E-EC5B-4E58-B2A1-C82FDD47F566', 0, 'UPDATE_ACTIVATION_CODE_BATCH');
insert into permission (id, obj_version, name) values ('7E8179BD-B6AA-48EA-980C-94008D17B712', 0, 'DELETE_ACTIVATION_CODE_BATCH');

insert into permission (id, obj_version, name) values ('C4CFD5AE-3EB9-4711-9CE2-4D6469ADBDF8', 0, 'CREATE_ACTIVATION_CODE');
insert into permission (id, obj_version, name) values ('451BCC17-9B0A-4DEC-BBAC-36BA41CCCFC4', 0, 'LIST_ACTIVATION_CODE');
insert into permission (id, obj_version, name) values ('FFE1676C-8438-4359-93CF-B8AFE800F397', 0, 'READ_ACTIVATION_CODE');
insert into permission (id, obj_version, name) values ('D3CCCF1E-CA7F-4389-BA2D-48F473AEB97A', 0, 'UPDATE_ACTIVATION_CODE');
insert into permission (id, obj_version, name) values ('FBABCF0F-FD36-4A24-87FC-60FB20164694', 0, 'DELETE_ACTIVATION_CODE');


-- SA
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='CREATE_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='LIST_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='READ_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='UPDATE_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='DELETE_PRODUCT'));

insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='CREATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='LIST_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='READ_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='UPDATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='DELETE_CUSTOMER'));

insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='CREATE_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='LIST_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='READ_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='UPDATE_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='DELETE_REGISTRATION'));

insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='CREATE_REGISTRATION_DEFINITION'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='LIST_REGISTRATION_DEFINITION'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='READ_REGISTRATION_DEFINITION'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='UPDATE_REGISTRATION_DEFINITION'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='DELETE_REGISTRATION_DEFINITION'));

insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='CREATE_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='LIST_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='READ_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='UPDATE_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='DELETE_ACTIVATION_CODE_BATCH'));

insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='CREATE_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='LIST_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='READ_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='UPDATE_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', (select id from permission where name='DELETE_ACTIVATION_CODE'));

-- DA
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='CREATE_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='LIST_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='READ_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='UPDATE_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='DELETE_PRODUCT'));

insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='CREATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='LIST_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='READ_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='UPDATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='DELETE_CUSTOMER'));

insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='READ_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='UPDATE_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='DELETE_REGISTRATION'));

insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='CREATE_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='LIST_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='READ_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='UPDATE_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='DELETE_ACTIVATION_CODE_BATCH'));

insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='LIST_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='READ_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('04B20C68-DF39-41C3-B70E-B4A3F4F27657', (select id from permission where name='UPDATE_ACTIVATION_CODE'));

-- PC
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='LIST_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='READ_PRODUCT'));

insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='CREATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='LIST_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='READ_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='UPDATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='DELETE_CUSTOMER'));

insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='READ_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='UPDATE_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='DELETE_REGISTRATION'));

insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='CREATE_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='LIST_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='READ_ACTIVATION_CODE_BATCH'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='UPDATE_ACTIVATION_CODE_BATCH'));

insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='LIST_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='READ_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('353FB7EC-8F39-4A41-B4A7-28C06B52E410', (select id from permission where name='UPDATE_ACTIVATION_CODE'));

-- PO
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='CREATE_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='LIST_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='READ_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='UPDATE_PRODUCT'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='DELETE_PRODUCT'));

insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='CREATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='LIST_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='READ_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='UPDATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='DELETE_CUSTOMER'));

insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='READ_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='UPDATE_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='DELETE_REGISTRATION'));

insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='LIST_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='READ_ACTIVATION_CODE'));
insert into role_permissions (role_id, permission_id) values('D5F17EEA-BDB0-426D-8328-45FCBF83433E', (select id from permission where name='UPDATE_ACTIVATION_CODE'));


-- FR
insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='LIST_PRODUCT'));

insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='CREATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='LIST_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='READ_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='UPDATE_CUSTOMER'));
insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='DELETE_CUSTOMER'));

insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='READ_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='UPDATE_REGISTRATION'));
insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='DELETE_REGISTRATION'));

insert into role_permissions (role_id, permission_id) values('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', (select id from permission where name='LIST_ACTIVATION_CODE'));

-- System Admin with SYSTEM_ADMIN role
insert into customer_roles(role_id, customer_id) values('E6EE25CD-19B3-40D8-880D-FB5B18F54FBF', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecc');


-- Add admin users and roles

-- Casper Lai
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('435A9A99-AF93-4F64-815F-57B78041D0F0', 0, 'casper_lai', 'laizc@oxfordfajar.com.my', '9271effffa426199eb7d278f3c4d5a3f', 'Casper', 'Lai', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='MALAYSIA'), '435A9A99-AF93-4F64-815F-57B78041D0F0');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'DIVISION_ADMIN'), '435A9A99-AF93-4F64-815F-57B78041D0F0');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), '435A9A99-AF93-4F64-815F-57B78041D0F0');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PUBLICATIONS_OFFICER'), '435A9A99-AF93-4F64-815F-57B78041D0F0');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), '435A9A99-AF93-4F64-815F-57B78041D0F0');

-- Rad Novakovic
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('3AD7881A-9DA4-45EA-A478-C359FD2FBD2D', 0, 'rad_novakovic', 'radmila.novakovic@oup.com', '78b0728f3c18b2f02579635bbb5a8375', 'Radmila', 'Novakovic', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), '3AD7881A-9DA4-45EA-A478-C359FD2FBD2D');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'DIVISION_ADMIN'), '3AD7881A-9DA4-45EA-A478-C359FD2FBD2D');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), '3AD7881A-9DA4-45EA-A478-C359FD2FBD2D');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PUBLICATIONS_OFFICER'), '3AD7881A-9DA4-45EA-A478-C359FD2FBD2D');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), '3AD7881A-9DA4-45EA-A478-C359FD2FBD2D');

-- Andrew Scott
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('E63DEBBF-A959-409F-B9FD-2C3E6DA86A74', 0, 'andrew_scott', 'andrew.scott@oup.com', '7beb513314ff81a1113c84862d69d623', 'Andrew', 'Scott', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), 'E63DEBBF-A959-409F-B9FD-2C3E6DA86A74');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), 'E63DEBBF-A959-409F-B9FD-2C3E6DA86A74');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), 'E63DEBBF-A959-409F-B9FD-2C3E6DA86A74');

-- Tom Moffatt
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('77C85DFD-A9CE-45B2-82CB-97621B5099C5', 0, 'tom_moffatt', 'thomas.moffatt@oup.com', '63e8b890c4d0f614afc64cf1eecf6573', 'Thomas', 'Moffatt', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='MALAYSIA'), '77C85DFD-A9CE-45B2-82CB-97621B5099C5');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), '77C85DFD-A9CE-45B2-82CB-97621B5099C5');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'SYSTEM_ADMIN'), '77C85DFD-A9CE-45B2-82CB-97621B5099C5');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'DIVISION_ADMIN'), '77C85DFD-A9CE-45B2-82CB-97621B5099C5');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), '77C85DFD-A9CE-45B2-82CB-97621B5099C5');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PUBLICATIONS_OFFICER'), '77C85DFD-A9CE-45B2-82CB-97621B5099C5');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), '77C85DFD-A9CE-45B2-82CB-97621B5099C5');


-- Kate Hawkins
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('CA9D1020-A37E-426A-B319-6BFD8CEB6572', 0, 'kate_hawkins', 'kate.hawkins@oup.com', 'e01fac77d2759f214afa19b7037a278c', 'Kate', 'Hawkins', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), 'CA9D1020-A37E-426A-B319-6BFD8CEB6572');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), 'CA9D1020-A37E-426A-B319-6BFD8CEB6572');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), 'CA9D1020-A37E-426A-B319-6BFD8CEB6572');


-- Clare Maguire
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('E53ED501-0084-4238-B4A2-7FB6AC444500', 0, 'clare_maguire', 'clare.maguire@oup.com', 'fddf2127e64769fe03179d701ea93013', 'Clare', 'Maguire', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), 'E53ED501-0084-4238-B4A2-7FB6AC444500');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), 'E53ED501-0084-4238-B4A2-7FB6AC444500');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), 'E53ED501-0084-4238-B4A2-7FB6AC444500');

-- Anna Jeffery
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('9773B254-AB69-44D9-AE2B-7CD42AD56FB0', 0, 'anna_jeffery', 'anna.jeffery@oup.com', '89da3a65e245f8a3555c0a99db89cac4', 'Anna', 'Jeffery', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), '9773B254-AB69-44D9-AE2B-7CD42AD56FB0');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), '9773B254-AB69-44D9-AE2B-7CD42AD56FB0');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), '9773B254-AB69-44D9-AE2B-7CD42AD56FB0');

-- Rachel Oliver
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('B221C304-C821-4E70-A0A7-5A3417C4D538', 0, 'rachel_oliver', 'rachel.oliver@oup.com', 'bcc7867aa1465cb58afddc62dfbd2060', 'Rachel', 'Oliver', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), 'B221C304-C821-4E70-A0A7-5A3417C4D538');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), 'B221C304-C821-4E70-A0A7-5A3417C4D538');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), 'B221C304-C821-4E70-A0A7-5A3417C4D538');

-- Ben Oldfield
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('50EAF39A-6EDA-4EAB-9710-3704862FB8D3', 0, 'ben_oldfield', 'ben.oldfield@oup.com', '614d915ef0e8cb540072822a63659d76', 'Ben', 'Oldfield', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), '50EAF39A-6EDA-4EAB-9710-3704862FB8D3');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), '50EAF39A-6EDA-4EAB-9710-3704862FB8D3');

-- Ben Sampson
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('D47D67CF-198E-4A48-BFF6-F4FE68150CA9', 0, 'ben_sampson', 'ben.sampson@oup.com', '88a7e67ca503d076605c21f396b18d72', 'Ben', 'Sampson', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), 'D47D67CF-198E-4A48-BFF6-F4FE68150CA9');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), 'D47D67CF-198E-4A48-BFF6-F4FE68150CA9');

-- Aaron Clements
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('DB693AED-F00A-4A9D-BD0F-EAAC1EA0AD36', 0, 'aaron_clements', 'aaron.clements@oup.com', 'e47d1b110093c89e9fe95c43a4c269d1', 'Aaron', 'Clements', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), 'DB693AED-F00A-4A9D-BD0F-EAAC1EA0AD36');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), 'DB693AED-F00A-4A9D-BD0F-EAAC1EA0AD36');

-- Clare Weston
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('55EEC1F9-0603-4FED-AC7C-11B352AD16CE', 0, 'clare_weston', 'clare.weston@oup.com', '86591a5545ea94713fb83147ca47bed6', 'Clare', 'Weston', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), '55EEC1F9-0603-4FED-AC7C-11B352AD16CE');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), '55EEC1F9-0603-4FED-AC7C-11B352AD16CE');

-- Tom Peacock
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('2314336C-0449-4E31-95C7-AB5E2B6931B6', 0, 'tom_peacock', 'tom.peacock@oup.com', '036f1e42a2ce87137929d51c3eeb569f', 'Tom', 'Peacock', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='MALAYSIA'), '2314336C-0449-4E31-95C7-AB5E2B6931B6');
insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), '2314336C-0449-4E31-95C7-AB5E2B6931B6');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'SYSTEM_ADMIN'), '2314336C-0449-4E31-95C7-AB5E2B6931B6');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'DIVISION_ADMIN'), '2314336C-0449-4E31-95C7-AB5E2B6931B6');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PRODUCTION_CONTROLLER'), '2314336C-0449-4E31-95C7-AB5E2B6931B6');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PUBLICATIONS_OFFICER'), '2314336C-0449-4E31-95C7-AB5E2B6931B6');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), '2314336C-0449-4E31-95C7-AB5E2B6931B6');

-- Rachel Oliver
insert into customer (id, obj_version, username, email_address, password, first_name, family_name, email_verified, reset_password, locked, failed_attempts, user_type, created_date, enabled) values('0E2BECFF-DF7F-4A79-A21A-F4C1B42732DE', 0, 'karen_harris', 'karen.harris@oup.com', '5574423570fabdb32141fa7b79c2c125', 'Karen', 'Harris', 1, 0, 0, 0, 'ADMIN', getdate(), 1);

insert into division_admin_user(id, obj_version,division_id, admin_user_id) values (newId(),0, (select id from division where division_type='ELT'), '0E2BECFF-DF7F-4A79-A21A-F4C1B42732DE');

insert into customer_roles(role_id, customer_id) values((select id from role where name = 'PUBLICATIONS_OFFICER'), '0E2BECFF-DF7F-4A79-A21A-F4C1B42732DE');
insert into customer_roles(role_id, customer_id) values((select id from role where name = 'FIELD_REP'), '0E2BECFF-DF7F-4A79-A21A-F4C1B42732DE');

-- Update existing customers to have last login date same as updated date, where possible
update customer set last_login = updated_date where last_login is null;