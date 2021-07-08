-- put data in the new URL_SKIN columns for the 3 url_skin entries
update url_skin set site_name='OFCW UAT Asia',contact_path ='http://uatcw.oxfordfajar.asia/contactus.aspx' where url='http://uatcw.oxfordfajar.asia';
update url_skin set site_name='OFCW UAT Net' ,contact_path ='http://uatcw.oxfordfajar.asia/contactus.aspx' where url='http://uatcw.oxfordfajar.net';
update url_skin set site_name='OFCW UAT EAC Products',contact_path ='http://uatcw.oxfordfajar.asia/contactus.aspx' where url='http://www.oupeacproducts.co.cc';
update url_skin set url='http://elt.uat.oup.com' where site_name like 'Oxford Teachers%Club';

-- Update OTC erights id
update product set erights_id = '27401' where id = '8FE6FD5B-F136-4A86-A9FB-7AC3724A8298';

--update product home pages for MALAYSIA
update product set home_page = 'http://uatcw.oxfordfajar.asia' 
where division_id = (select id from division where division_type='MALAYSIA');

--update product home pages for ELT (OTC) 
update product set home_page = 'http://elt.uat.oup.com/teachersclub/', landing_page = 'http://elt.uat.oup.com/secure/registration_success' 
where division_id = (select id from division where division_type='ELT')
and product_name like 'Oxford Teachers%Club';

--update product home pages for ELT (non OTC)
update product set home_page = 'http://oxfordlearn.com' 
where division_id = (select id from division where division_type='ELT')
and product_name not like 'Oxford Teachers%Club';