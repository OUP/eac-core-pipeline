update product set erights_id = 4301 where id in ('E6CC13C4-1D79-4FC6-B0AC-19C2A767B2CB', '52ACA31D-7DD7-4618-B652-AB2636653E5D', '93678573-8EA6-469E-A1B8-2B9905B3D097', 'CA60C86E-3E25-459F-9123-AFB4297A0566', 'ED04D668-427B-4467-B67A-EF25EA52FDEC', '4D54E3E1-203D-407E-B1D7-276A6F0B7AB8');
update product set erights_id = 11207 where id in ('948B5C2B-0D04-4F2F-BA7A-0BFDB3D0D77A');
update product set erights_id = 11205 where id in ('8DBAF754-0238-4EAB-8D0F-26A7A0D8997B', '06AC88B5-B8BC-478D-A72D-372EC7C98123');
update product set erights_id = 11203 where id in ('6F9C4166-F1FD-4B0F-B098-C77787675B5A', '62D5573E-EC48-429B-B36D-251496A3A3B4');
update product set erights_id = 11206 where id in ('210806C4-3FEB-4599-ACF0-5B2D7B776859');
update product set erights_id = 11204 where id in ('1F0D7133-5D06-418F-AEC5-966C387D0CCB');
update product set email = 'eacsystemadmin@oup.com';

--TODO Release 2 UAT landing pages
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/principlesofmanagement/' where id = '8DBAF754-0238-4EAB-8D0F-26A7A0D8997B';
update product set landing_page = 'http://uatcw.oxfordfajar.asia/activate/businessstatistics/' where id = '6F9C4166-F1FD-4B0F-B098-C77787675B5A';

delete from url_skin;
insert into url_skin (id,obj_version,url,skin_path) values (newId(),0,'http://uatcw.oxfordfajar.asia','skin/oxfordfajar/cw/css/eac-override.css');
insert into url_skin (id,obj_version,url,skin_path) values (newId(),0,'http://uatcw.oxfordfajar.net','skin/oxfordfajar/cw/css/eac-override.css');
insert into url_skin (id,obj_version,url,skin_path) values (newId(),0,'http://www.oupeacproducts.co.cc','skin/oxfordfajar/cw/css/eac-override.css');