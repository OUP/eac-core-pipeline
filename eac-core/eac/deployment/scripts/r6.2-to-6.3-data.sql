-- Mantis 12691 - Translations for Reset Password (Japanese)
update message set message = N'パスワードの再発行' where basename = 'messages' and [key] = 'title.resetpassword' and country = 'ja';
update message set message = N'パスワードの再発行' where basename = 'messages' and [key] = 'label.resetpassword' and country = 'ja';
update message set message = N'パスワードの再発行' where basename = 'messages' and [key] = 'submit.resetpassword' and country = 'ja';
update message set message = N'パスワードの再発行' where basename = 'messages' and [key] = 'label.password.reset' and country = 'ja';
update message set message = N'パスワードの再発行' where basename = 'messages' and [key] = 'title.resetpasswordsuccess' and country = 'ja';
-- Mantis 12691 - Translations for Reset Password (Portuguese)
update message set message = N'Alterar Password' where basename = 'messages' and [key] = 'title.resetpassword' and country = 'pt';
update message set message = N'Alterar Password' where basename = 'messages' and [key] = 'label.resetpassword' and country = 'pt';
update message set message = N'Alterar Password' where basename = 'messages' and [key] = 'submit.resetpassword' and country = 'pt';
update message set message = N'Alterar Password' where basename = 'messages' and [key] = 'label.password.reset' and country = 'pt';
update message set message = N'Alterar Password' where basename = 'messages' and [key] = 'title.resetpasswordsuccess' and country = 'pt';
-- Mantis 12691 - Translations for Reset Password (Ukranian)
update message set message = N'змінити пароль' where basename = 'messages' and [key] = 'title.resetpassword' and country = 'uk';
update message set message = N'змінити пароль' where basename = 'messages' and [key] = 'label.resetpassword' and country = 'uk';
update message set message = N'змінити пароль' where basename = 'messages' and [key] = 'submit.resetpassword' and country = 'uk';
update message set message = N'змінити пароль' where basename = 'messages' and [key] = 'label.password.reset' and country = 'uk';
update message set message = N'змінити пароль' where basename = 'messages' and [key] = 'title.resetpasswordsuccess' and country = 'uk';
-- Mantis 12691 - Translations for Reset Password (Russian)
update message set message = N'поменять пароль' where basename = 'messages' and [key] = 'title.resetpassword' and country = 'ru';
update message set message = N'поменять пароль' where basename = 'messages' and [key] = 'label.resetpassword' and country = 'ru';
update message set message = N'поменять пароль' where basename = 'messages' and [key] = 'submit.resetpassword' and country = 'ru';
update message set message = N'поменять пароль' where basename = 'messages' and [key] = 'label.password.reset' and country = 'ru';
update message set message = N'поменять пароль' where basename = 'messages' and [key] = 'title.resetpasswordsuccess' and country = 'ru';

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

