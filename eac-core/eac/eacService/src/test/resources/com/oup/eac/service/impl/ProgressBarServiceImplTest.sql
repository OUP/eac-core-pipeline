-- PROGRESS BARS

-- ACCOUNT CREATION

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('0991FD82-09FA-4162-A091-1ED244C31F6B', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'REGULAR', 'NA', 'NEW', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '0991FD82-09FA-4162-A091-1ED244C31F6B', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '0991FD82-09FA-4162-A091-1ED244C31F6B', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '0991FD82-09FA-4162-A091-1ED244C31F6B', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '0991FD82-09FA-4162-A091-1ED244C31F6B', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('2FD50913-2A6C-4402-A2C0-818113A42431', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'REGULAR', 'NA', 'NEW', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '2FD50913-2A6C-4402-A2C0-818113A42431', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '2FD50913-2A6C-4402-A2C0-818113A42431', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '2FD50913-2A6C-4402-A2C0-818113A42431', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '2FD50913-2A6C-4402-A2C0-818113A42431', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('255FEDF3-9435-40D4-BCCF-09FACCDD8562', 0, 'UNKNOWN', 'VALIDATED', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'REGULAR', 'NA', 'NEW', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '255FEDF3-9435-40D4-BCCF-09FACCDD8562', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '255FEDF3-9435-40D4-BCCF-09FACCDD8562', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', '255FEDF3-9435-40D4-BCCF-09FACCDD8562', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '255FEDF3-9435-40D4-BCCF-09FACCDD8562', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('396B1384-E039-4813-A7FC-C3A72F0A52AE', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'NO', 'NEW', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '396B1384-E039-4813-A7FC-C3A72F0A52AE', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '396B1384-E039-4813-A7FC-C3A72F0A52AE', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '396B1384-E039-4813-A7FC-C3A72F0A52AE', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '396B1384-E039-4813-A7FC-C3A72F0A52AE', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '396B1384-E039-4813-A7FC-C3A72F0A52AE', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('D28A080D-61BA-4CCE-9D65-397B00DAC284', 0, 'UNKNOWN', 'INSTANT', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'YES', 'NEW', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, 'D28A080D-61BA-4CCE-9D65-397B00DAC284', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'NO', 'NEW', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '1E56EE8D-2DB2-46F4-9947-8BBE776DAEB5', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('8c8aa1b9-8126-42fb-9903-0833b3f8600b', 0, 'UNKNOWN', 'SELF', 'ACCOUNT_REGISTRATION', 'UNKNOWN', 'TOKEN', 'DIRECT', 'NEW', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Your details', 'CURRENT_COMPLETED_STEP', 'label.progress.yourdetails', '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Activate', 'INCOMPLETE_STEP', 'label.progress.activate', '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '8c8aa1b9-8126-42fb-9903-0833b3f8600b', 5);



-- PRODUCT REGISTRATION

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('973AD2B3-9875-4394-9A59-3828701BAF92', 0, 'NONE', 'INSTANT', 'PRODUCT_REGISTRATION', 'NONE', 'REGULAR', 'NA', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '973AD2B3-9875-4394-9A59-3828701BAF92', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', '973AD2B3-9875-4394-9A59-3828701BAF92', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'COMPLETE_NO_STEP', null, '973AD2B3-9875-4394-9A59-3828701BAF92', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'COMPLETE_NO_STEP', null, '973AD2B3-9875-4394-9A59-3828701BAF92', 4);



insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('67BCA8B5-7802-44E1-B9F8-51342D388377', 0, 'NONE', 'SELF', 'PRODUCT_REGISTRATION', 'NONE', 'REGULAR', 'NA', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '67BCA8B5-7802-44E1-B9F8-51342D388377', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', '67BCA8B5-7802-44E1-B9F8-51342D388377', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '67BCA8B5-7802-44E1-B9F8-51342D388377', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '67BCA8B5-7802-44E1-B9F8-51342D388377', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 0, 'NONE', 'VALIDATED', 'PRODUCT_REGISTRATION', 'NONE', 'REGULAR', 'NA', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', '1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', '1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '1FF647B6-0F4E-4188-8CFF-D53A0C3A81A8', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('ED121E2F-9923-400D-A416-8D4FEA06813B', 0, 'NONE', 'INSTANT', 'PRODUCT_REGISTRATION', 'NONE', 'TOKEN', 'YES', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ED121E2F-9923-400D-A416-8D4FEA06813B', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ED121E2F-9923-400D-A416-8D4FEA06813B', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'ED121E2F-9923-400D-A416-8D4FEA06813B', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', 'ED121E2F-9923-400D-A416-8D4FEA06813B', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'COMPLETE_NO_STEP', null, 'ED121E2F-9923-400D-A416-8D4FEA06813B', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 0, 'NONE', 'SELF', 'PRODUCT_REGISTRATION', 'NONE', 'TOKEN', 'YES', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'CURRENT_COMPLETED_STEP', 'label.progress.enrolment', '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '8896BDD8-D838-4967-AEFD-F19E6EC2A4BF', 5);

-- ACTIVATE LICENCE

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('9E327F16-1C2D-4B59-BB78-EF20E963E610', 0, 'AWAITING', 'SELF', 'ACTIVATE_LICENCE', 'COMPLETE', 'REGULAR', 'NA', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '9E327F16-1C2D-4B59-BB78-EF20E963E610', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '9E327F16-1C2D-4B59-BB78-EF20E963E610', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', '9E327F16-1C2D-4B59-BB78-EF20E963E610', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'COMPLETE_NO_STEP', null, '9E327F16-1C2D-4B59-BB78-EF20E963E610', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 0, 'AWAITING', 'SELF', 'ACTIVATE_LICENCE', 'COMPLETE', 'TOKEN', 'YES', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'CURRENT_COMPLETED_STEP', 'label.progress.confirm', 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'COMPLETE_NO_STEP', null, 'E6E30ED2-8E2E-4202-AA64-AD0C31BA8CCA', 5);


-- AWAITING LICENCE ACTIVATION

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('5A666935-A64D-4E23-9E63-3178759FFC31', 0, 'AWAITING', 'VALIDATED', 'AWAITING_LICENCE_ACTIVATION', 'COMPLETE', 'REGULAR', 'NA', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '5A666935-A64D-4E23-9E63-3178759FFC31', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '5A666935-A64D-4E23-9E63-3178759FFC31', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Awaiting confirmation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting', '5A666935-A64D-4E23-9E63-3178759FFC31', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'COMPLETE_NO_STEP', null, '5A666935-A64D-4E23-9E63-3178759FFC31', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('4BA78049-76DE-419E-87D4-4DA9730268FE', 0, 'AWAITING', 'VALIDATED', 'AWAITING_LICENCE_ACTIVATION', 'COMPLETE', 'TOKEN', 'YES', 'LOGGEDIN', 'NON_VALIDATED');


insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '4BA78049-76DE-419E-87D4-4DA9730268FE', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '4BA78049-76DE-419E-87D4-4DA9730268FE', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '4BA78049-76DE-419E-87D4-4DA9730268FE', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Awaiting confirmation', 'CURRENT_COMPLETED_STEP', 'label.progress.awaiting', '4BA78049-76DE-419E-87D4-4DA9730268FE', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'COMPLETE_NO_STEP', null, '4BA78049-76DE-419E-87D4-4DA9730268FE', 5);


-- VALIDATION DENIED

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 0, 'DENIED', 'VALIDATED', 'VALIDATION_DENIED', 'COMPLETE', 'REGULAR', 'NA', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, 'C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment denied', 'CURRENT_COMPLETED_STEP', 'label.progress.denied', 'C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'COMPLETE_NO_STEP', null, 'C5D8A52A-4BBB-41B9-A58D-06BE18268D87', 4);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('19D6334C-61D0-406F-8AED-D3C5AD358CA4', 0, 'DENIED', 'VALIDATED', 'VALIDATION_DENIED', 'COMPLETE', 'TOKEN', 'YES', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment denied', 'CURRENT_COMPLETED_STEP', 'label.progress.denied', '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'COMPLETE_NO_STEP', null, '19D6334C-61D0-406F-8AED-D3C5AD358CA4', 5);


-- ACTIVATION CODE

insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 0, 'UNKNOWN', 'SELF', 'ACTIVATION_CODE', 'UNKNOWN', 'TOKEN', 'NO', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Activate', 'CURRENT_COMPLETED_STEP', 'label.progress.activate', '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Confirm', 'INCOMPLETE_STEP', 'label.progress.confirm', '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '7F06EFBF-9EC6-4072-B7EC-D4AFC94FDA33', 5);


insert into progress_bar(id, obj_version, activation_state, activation_strategy, page, registration_state, registration_type, token_state, user_state, account_validated)
values('2B535F10-21FF-4CE2-AC89-06050BD83E9E', 0, 'UNKNOWN', 'VALIDATED', 'ACTIVATION_CODE', 'UNKNOWN', 'TOKEN', 'NO', 'LOGGEDIN', 'NON_VALIDATED');

insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'CURRENT_COMPLETED_STEP', null, '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 1);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Activate', 'CURRENT_COMPLETED_STEP', 'label.progress.activate', '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 2);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Enrolment', 'INCOMPLETE_STEP', 'label.progress.enrolment', '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 3);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, 'Awaiting confirmation', 'INCOMPLETE_STEP', 'label.progress.awaiting', '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 4);
insert into progress_bar_element(id, obj_version, default_message, element_type, label, progress_bar_id, sequence)
values("java.util.UUID.randomUUID"(), 0, null, 'INCOMPLETE_NO_STEP', null, '2B535F10-21FF-4CE2-AC89-06050BD83E9E', 5);