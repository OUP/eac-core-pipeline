alter table registration_definition add validation_required bit not null DEFAULT(0);
GO
alter table customer add email_verification_state nvarchar(255);
GO
alter table customer drop column email_verified;
GO
alter table progress_bar add account_validated nvarchar(255) not null DEFAULT('NON_VALIDATED');
GO
-- EAC-196 - increasing performance of answer searches in Admin
create index answer_text_idx on answer (answer_text);
GO