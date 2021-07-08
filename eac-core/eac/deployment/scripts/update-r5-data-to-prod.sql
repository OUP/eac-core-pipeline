-- put data in the new URL_SKIN columns for the 1 url_skin entry
update url_skin set site_name='Oxford Fajar Companion Website',contact_path ='http://cw.oxfordfajar.com.my/contactus.aspx' where url='http://cw.oxfordfajar.com.my';

--update product home pages for MALAYSIA
update product set home_page = 'http://cw.oxfordfajar.com.my' 
where division_id = (select id from division where division_type='MALAYSIA');

--update product home pages for ELT (OTC) **REPLACE ERIGHTS_IDS**
update product set home_page = 'http://elt.oup.com/teachersclub/', landing_page = 'http://elt.oup.com/secure/registration_success', erights_id = 12001 
where division_id = (select id from division where division_type='ELT')
and product_name like 'Oxford Teachers%Club';

--update product home pages for ELT (non OTC)
update product set home_page = 'http://oxfordlearn.com' 
where division_id = (select id from division where division_type='ELT')
and product_name not like 'Oxford Teachers%Club'
and (home_page is null) OR (rtrim(home_page) = '');
