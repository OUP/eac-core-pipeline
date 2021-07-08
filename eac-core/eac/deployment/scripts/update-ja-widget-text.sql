
-- update japanese welcome text
update message set message = N'ご登録情報を更新するには「プロフィールを見る」をクリックしてください。共有のコンピューターをお使いの方は、ご利用終了時に必ず「ログアウト」してください。' where [key] = 'welcome.message' and language='ja';

-- rollback
--update message set message = N'ようこそ！ 情報を更新するには「プロフィールを見るにクリックを。' where [key] = 'welcome.message' and language='ja';
