--drop table message;

SET IGNORECASE TRUE;

create table message (
	 id varchar(255) not null,
     basename varchar(31) not null,
     language varchar(7) null,
     country varchar(7) null,
     variant varchar(7) null,
     key varchar(255) null,
     message varchar(1000),
     obj_version numeric(19,0) not null);
     
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','test','en',null,null,'error.msg1','error message for English', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','test','en','GB',null,'error.msg1','error message for English GB', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','test','en','US',null,'error.msg1','error message for English US', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','test','es',null,null,'error.msg1','error message for Spanish', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','test','es','ES',null,'error.msg1','error message for Spanish Spain', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','test','es','US',null,'error.msg1','error message for Spanish US', 0);

-- Generated [7] from  emails_es.properties on 12/Sep/2011 11:54:07
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.dear','Al', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.you','imponerle', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.complete','castigo quer\u00eda', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.after.registration','ejemplarizar con \u00e9l', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.regards','ese', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.team','ejemplarizar con \u00e9l', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.respond','(imponerle ese castigo quer\u00eda)', 0);

-- Generated [31] from  emails.properties on 12/Sep/2011 11:54:07
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.dear','Dear', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.you','You have registered for', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.hasregistered','has registered for', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.account','Account', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.application','Application', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.allow','To allow access to', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.deny','To deny access to', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.clickhere','click here', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.yousuccess','Thank you for registering for access to', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.unfortunate','We regret that we cannot approve your access to these resources at this time. We hope you will understand the need to restrict access to these resources to authorized users only.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.yourreg','Thank you for requesting access to', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.regaccepted','We are pleased to inform you that your request has been accepted.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.wehope','We hope that you enjoy using this website and find it useful.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.ifcomments','If you have any comments or feedback please contact us by email at', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.wewelcome','We welcome your feedback.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.regdenied','has been denied', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.ifrequire','If you feel this to be an error on our part, please do let us know why and we will be happy to look into this for you. You can contact us by email at', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.complete','To complete registration please click on the link below:', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.after.registration','and then log in to access', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.regards','Regards', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.team','The OUP Support Team', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.respond','(Please do not respond to this automatically generated email)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.reset','We have reset your password. Please log in with your user name and the temporary password below:', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.password.change','Please change your password after you log in.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.yourlicense','Your access starts', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.to','and will end on', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.regprocess','We are pleased to inform you that your request has been accepted.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'subject.successfullyactivated','Access to {0} successfully activated.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.duration','and will last for {0} from when you first start using the resources', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.usages','and will last for {0} usage(s)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'YEAR','year(s)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'MONTH','month(s)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'WEEK','week(s)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'DAY','day(s)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'HOUR','hour(s)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'MINUTE','minute(s)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'SECOND','second(s)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'MILLISECOND','millisecond(s)', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.now','now', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.on','on', 0);

-- Generated [55] from  messages_es.properties on 12/Sep/2011 11:54:07
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'title.activatelicence','Activar licencia', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'title.changepassword','Cambiar contrase\u00f1a', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'title.error','Error', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'title.login','Iniciar sesi\u00f3n', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'title.accountregistration','Registro de Cuenta', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'title.productregistration','Registro del producto', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'title.registrationsuccess','El \u00e9xito de inscripci\u00f3n', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'title.resetpassword','Perd\u00ed mi contrase\u00f1a', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'header.logo','Oxford University Press Enterprise Access Control', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'header.title','Enterprise Access Control', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'header.country','En todo el mundo', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.activatelicence','Su licencia de este producto requiere de la activaci\u00f3n. Por favor, revise su correo electr\u00f3nico.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.confirmpassword','Confirmar contrase\u00f1a', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.confirmnewpassword','Confirmar nueva contrase\u00f1a', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.details','Detalles', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.english','Ingl\u00e9s', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.errors','Los siguientes errores se han producido', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.familyname','Familia Nombre', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.firstname','Nombre', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.language','Idioma', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.login','Iniciar sesi\u00f3n', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.marketinginformation','Informaci\u00f3n de Marketing', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.newpassword','Nueva Contrase\u00f1a', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.password','Contrase\u00f1a', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.registrationsuccess','Ha confirmado su inscripci\u00f3n.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.productregistration','Registro del producto', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.register','Registrarse', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.resetpassword','Perd\u00ed mi contrase\u00f1a', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.spanish','Espa\u00f1ol', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.username','Nombre de usuario', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'label.unexpectederror','Un error inesperado se ha producido. <br/> Por favor, regrese y vuelva a intentarlo. <br/> Si esto sigue ocurriendo por favor contacte al administrador del sistema.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'submit.login','Iniciar sesi\u00f3n', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'submit.accountregistration','Registrarse', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'submit.productregistration','Registrarse', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'submit.resetpassword','Perd\u00ed mi contrase\u00f1a', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'submit.changepassword','Cambiar contrase\u00f1a', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.notthesame','{0} and {1} are not the same!', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.regularexpression','{0} is invalid!', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.not-specified','{0} is required!', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.must.be.valid.email','{0} must be a valid email address.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.must.be.same','{0} and {1} must be the same.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.please.check.email','Please check your email.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.problem.with.login.credentials','There was a problem with your login credentials.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.username.taken','This username is already taken. Please try another.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.problem.creating.customer','There was a problem creating your account. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.problem.resetting.password','There was a problem resetting your password. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.problem.changing.password','There was a problem changing your password. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.problem.registering.product','There was a problem registering the product. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.changing.customer.password','There was a problem changing your password. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'error.logging.out.customer','There was a problem', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'footer.copyright','Copyright &copy; Oxford University Press, {0}. All Rights Reserved.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'footer.contactus','Contact Us', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'footer.help','Help &amp; Support', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'footer.accessibility','Accessibility', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages','es',null,null,'footer.privacy','Privacy Policy, Cookie Policy and Legal Notice', 0);

-- Generated [207] from  messages.properties on 12/Sep/2011 11:54:07
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.activatelicence','Activate Licence', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.changepassword','Change Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.error','Error', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.login','Log in', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.accountregistration','Account Creation', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.productregistration','Product Registration', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registrationsuccess','Registration Success', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.resetpassword','Reset Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.resetpasswordsuccess','Reset Password Success', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.contactus','Contact Us', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.accessibility','Accessibility', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.privacypolicy','Terms, Conditions and Privacy Policy', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.version','EAC Version', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.redeemcode','Redeem Activation Code', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.licencealreadyactive','Licence Already Active', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.validatorlicenceallowed','Licence Request Successful', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.validatorlicencedenied','Licence Request Unsuccessful', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.awaitinglicenceactivation','Awaiting Licence Activation', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'header.logo','Oxford University Press Enterprise Access Control', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'header.title','Enterprise Access Control', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'header.country','Worldwide', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'help.resetpassword','Please enter your username or email address in the box below and press submit. We will email you your password details.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'help.dob','The date of birth must be in the format dd/mm/yyyy e.g. 18/12/1988.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'help.activationcode','Please enter your activation code in the box below and click the Activate button. If you are not already logged in you will be asked to log in or register.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.activatelicence','Thank you for completing the registration form. An activation email has been sent to your registered email address. Please check your email and follow the instructions to complete your registration to {0}.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.activatelicenceemail','If your activation email does not arrive within {1} please look in your spam or junk mail folder. If the email is not there please contact {2}.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.confirmpassword','Confirm Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.confirmnewpassword','Confirm New Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.denyreason','Deny Reason', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.personaldetails','Personal Details', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.english','English', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.errors','The following errors have occurred:', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.familyname','Surname', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.firstname','First Name', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.language','Language', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.login','Log in', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.marketinginformation','Marketing Information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.mandatory','Field is mandatory', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.newpassword','New Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.password','Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registrationsuccess','You have successfully confirmed your registration.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.productregistration','Product Registration', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registrationAddress','Address', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registrationPosition','Position', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.register','Register', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.resetpassword','Reset Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.resetpasswordsuccess','Thank you. If we have your email address in our records, you will be sent an email containing a new password. You will be asked to select a new password the next time you log in. NOTE: You will not receive an email if your email address is not in our records.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.spanish','Spanish', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.username','Username', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.unexpectederror','An unexpected error has occurred.<br/>Please go back and try again.<br/>If this continues to happen please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.version','EAC Version', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.redeemcode','Activation Code', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.licencealreadyactive','Licence Already Active', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.validatorlicenceallowed','You have successfully approved', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.awaitinglicenceactivation','Thank you for completing the registration form. We will contact you within a week regarding your access to {0}.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.awaitinglicenceactivationconfirm','As soon as your access has been confirmed you will receive an email notifying you of your access to the site. If your notification email has not arrived within a week, please check for it in your spam or junk mail folder. If the email is not there please contact <a href="mailto:{1}">{1}</a>.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.licenceisdenied','Thank you for requesting access to {0}. Unfortunately your request has been denied. If you require further information please contact eacsystemadmin@oup.com.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.validatorlicencedenied','You have successfully denied', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.touse','to use', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.important','Important Information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.productinfo','Only teachers may register for access to this content. Registration details will be verified before access to Teacher Resources is granted. Please allow one week for verification. Access to Student Resources is immediate upon completing registration.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.lecturer.important.info','Only lecturers may register for access to this content. Registration details will be verified before access is granted. Please allow one week for verification. This Lecturer Account will give access to both Student and Lecturer resources', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.customer.header','Personal Details', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.customer.sex','Gender', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.sex.male','Male', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.sex.female','Female', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.customer.dob','Date of Birth', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.address.header','Address', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.address.line1','Line 1', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.address.line2','Line 2', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.address.line3','City/Town', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.address.line4','Postcode', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.address.line5','State', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.address.line6','Country', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.country.my','Malaysia', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.country.sg','Singapore', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.country.id','Indonesia', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.country.bn','Brunei', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.country.ph','Philippines', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.country.th','Thailand', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.country.vn','Vietnam', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.country.other','Other', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.institution.registration.address.line1','Institution Address Line 1', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.institution.registration.address.line2','Institution Address Line 2', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.institution.domain.email','Institution Email Address', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.institution.contact.number','Contact Number', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.header','Institution Details', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.position','Position', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.position.student','Student', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.position.teacher','Teacher/Lecturer', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution','Institution Type', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.national.primary','National Primary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.national.type.primary','National Type Primary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.private.primary','Private Primary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.national.secondary','National Secondary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.private.secondary','Private Secondary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.form6','Form6/Pre-U', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.matriculation.centre','Matriculation Centre', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.polytechnic','Polytechnic', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.private.college','Private College', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.private.language.school','Private Language School', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.public.university','Public University', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.private.university','Private University', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.other','Other', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.institution.name','Institution Name', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.marketing.header','Marketing Information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.marketing','Oxford University Press Marketing Information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.marketing.optin','I am happy to receive information about products and services from OUP and associated companies', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.marketing.optout','I do not wish to receive any correspondence from OUP and associated companies apart from critical user information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.my.cw.marketing','Oxford Fajar Marketing Information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.my.cw.marketing.optin','I am happy to receive information about related products and services from Oxford Fajar', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.my.cw.marketing.optout','I do not wish to receive any correspondence from Oxford  Fajar apart from critical user information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.lvl.lower.primary','Lower Primary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.lvl.upper.primary','Upper Primary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.lvl.lower.secondary','Lower Secondary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.lvl.upper.secondary','Upper Secondary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.lvl.form.six','Form Six', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.lvl.other','Other', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.courselvl.foundation','Foundation', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.courselvl.certification','Certification', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.courselvl.diploma','Diploma', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.courselvl.professional','Professional/Post Graduate', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.adopted.book','I have adopted this book', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.adopted.consider','I am considering adopting this book', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.adopted.status','Adoption status', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.contact.type.mobile','Mobile', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.contact.type.institution','Institution', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.contact.type.home','Home', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.contact.number.type','Contact Number Type', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.education.faculty.department','Faculty/ Department', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.tandc.header','Terms and Conditions', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.preferred.contact','Preferred Contact Details', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.tandc','I have read and agreed to Oxford University Press''''s Terms and Conditions for Account Creation and Privacy Policy', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.tandc.link','Terms and Conditions', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.my.cw.tandc','I have read and agreed to the Terms and Conditions of the Oxford Fajar Companion Website', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.my.cw.tandc.link','Terms and Conditions', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.subjects.taught','Subjects Taught', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.level','Level', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.school.details','School Details', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.institution.details','Institution Details', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.course.info','Course Information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.registration.book.adoption','Book Adoption', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.password','Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.confirmpassword','Confirm Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.confirmnewpassword','Confirm New Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.newpassword','New Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.username','Username', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.familyname','Last Name', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.firstname','First Name', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.customer.sex','Gender', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.customer.dob','Date of Birth', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.address.line1','Line 1', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.address.line2','Line 2', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.address.line3','City/Town', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.address.line4','Postcode', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.address.line5','State', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.address.line6','Country', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.institution.registration.address.line1','Institution Address Line 1', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.institution.registration.address.line2','Institution Address Line 2', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.institution.domain.email','Institution Email Address', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.institution.contact.number','Contact Number', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.education.position','Position', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.education.institution','Institution Type', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.education.institution.name','Institution Name', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.marketing','Preference on receiving Oxford University Press Marketing Information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.tandc','Terms and Conditions acceptance', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.tandc.link','Terms and Conditions', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.my.cw.tandc','Terms and Conditions acceptance', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.my.cw.tandc.link','Terms and Conditions', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.subjects.taught','Subjects Taught', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.level','Level', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.lvl.lower.primary','Lower Primary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.lvl.upper.primary','Upper Secondary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.lvl.lower.secondary','Lower Secondary', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.lvl.form.six','Form Six', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.contact.number.type','Contact Number Type', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.adopted.status','Adoption status', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.registration.education.faculty.department','Faculty/Department', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.productinfo','Important Information', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'info.username','Your username must be a valid email address.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'info.password.strength','Your password must be from 6 to 15 characters long and it must contain at least one lower case character, one upper case character and at least one digit. No other characters are permitted.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'info.redeem.code','Please enter your activation code', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'info.institution.domain.email','If provided, will help to expedite validation process', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'submit.login','Log in', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'submit.accountregistration','Create Account', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'submit.productregistration','Register', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'submit.resetpassword','Reset Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'submit.changepassword','Change Password', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'submit.redeemcode','Activate', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.notthesame','{0} and {1} are not the same', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.regularexpression','{0} is invalid', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.not-specified','{0} is required', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.password.strength','Your password must be from 6 to 15 characters long and it must contain at least one lower case character, one upper case character and at least one digit. No other characters are permitted.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.must.be.valid.email','{0} must be a valid email address.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.must.be.same','{0} and {1} must be the same.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.please.check.email','Please check your email.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.problem.with.login.credentials','There was a problem with your login credentials.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.username.taken','This username is already taken. Please try another.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.problem.creating.customer','There was a problem creating your account. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.problem.resetting.password','There was a problem resetting your password. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.problem.changing.password','There was a problem changing your password. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.problem.registering.product','There was a problem registering the product. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.changing.customer.password','There was a problem changing your password. Please contact the system administrator.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.aquiring.product.information','There was a problem with your product. Please go back to the product page and choose the item again.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.logging.out.customer','There was a problem', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'error.problem.activating.token','There was a problem with your activation code.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'footer.copyright','Copyright &copy; Oxford University Press, {0}. All Rights Reserved.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'footer.contactus','Contact Us', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'footer.help','Help &amp; Support', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'footer.accessibility','Accessibility', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'footer.privacypolicy','Terms, Conditions and Privacy Policy', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.concurrencyexceeded1','We''''re sorry. You have reached the maximum number of users who are permitted to access these resources simultaneously using this account. Please try again later.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.noactivelicence','We''''re sorry. You no longer have access to these resources. If you wish to continue to use this web site you will need to', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.confidentiality1','Confidentiality Notice:', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.confidentiality2','This message is confidential and intended solely for the addressee(s).', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.confidentiality3','You may use and apply the information for the intended purpose only.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.confidentiality4','Any disclosure, copying, distribution or use of this communication to someone other than the intended recipient is prohibited.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.confidentiality5','If this email has come to you in error, please delete it, along with any attachments.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.admin.password.reset','Your request to reset your password has been accepted. Please click the link below and change your password.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.admin.password.pleaseNote','Please note that this link is valid for 8 hours only.', 0);


insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.email','Email Address', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.register.thanks','Thank you for registering with Oxford University Press.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.validateemail','To complete registration, please click on the following link:', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.please.ignore','If you didn’t register for an account with Oxford University Press, please ignore this e-mail.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.comments','Please send any comments or feedback to', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'registration.denied.msg.1','You are logged on with a shared user account that does not permit you to access this resource. Please {3} and then log back in again with your personal username/password. In case of difficulty please contact  {2}.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'registration.denied.msg.2','You are logged on with a shared user account that does not permit you to access this resource. Please check that you have entered the correct username/password for the product you are trying to access. In case of difficulty please contact {2}.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'registration.denied.msg.3','You have attempted to access a resource which does not exist. Please check you have entered the address correctly and {0} if you require any further assistance.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'registration.denied.msg.4','You do not have access this resource. If you think you should have access, please contact {2}.', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'registration.denied.msg.5','It is not currently possible to register for {1}.  For more information please contact {2}.', 0);

insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.locale','Locale', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.timezone','Time Zone', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'licence.description.email.prefix','Licence Details :', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.end','End', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.licence.expires','End', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.access.period','Access period: {0} from', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.from.to','Access from {0} to {1}', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.licence.start','Start', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'profile.licence.concurrent','Access for {0} concurrent users', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.registration','Registration', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.first.use','First Use', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.start','Start', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'profile.licence.usage','{1} out of {0} use(s) remaining', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.licence.rolling.begin.on','Begin On', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.licence.rolling.begin.on.CREATION','Creation', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'title.licence.rolling.begin.on.FIRST_USE','First Use', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'MINUTE','minute(s)', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'WEEK','week(s)', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'MONTH','month(s)', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'MILLISECOND','millisecond(s)', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'SECOND','second(s)', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'HOUR','hour(s)', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'YEAR','year(s)', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'DAY','day(s)', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.subject.to','Subject to', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.from.to','Access from {0} to {1}', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.access.period','Access period: {0} from', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.registration','Registration', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.first.use','First Use', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.subject.to','Subject to', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.start','Start', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'label.rolling.end','End', 0);
insert into message (id,basename,language,country,variant,key,message,obj_version) values ('','messages',null,null,null,'email.licence.description.prefix','Licence Details :',0);

