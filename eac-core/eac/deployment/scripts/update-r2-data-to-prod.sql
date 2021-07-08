--UPDATE WITH CORRECT IDS FOR PROD!!
-- OFCW Student Content
update product set erights_id = 7501 where id in ('E6CC13C4-1D79-4FC6-B0AC-19C2A767B2CB', '52ACA31D-7DD7-4618-B652-AB2636653E5D', '93678573-8EA6-469E-A1B8-2B9905B3D097', 'CA60C86E-3E25-459F-9123-AFB4297A0566', 'ED04D668-427B-4467-B67A-EF25EA52FDEC', '4D54E3E1-203D-407E-B1D7-276A6F0B7AB8');
-- OFCW Teacher Resources
update product set erights_id = 8101 where id in ('948B5C2B-0D04-4F2F-BA7A-0BFDB3D0D77A');
-- OFCW Principals of management (student)
update product set erights_id = 8104 where id in ('8DBAF754-0238-4EAB-8D0F-26A7A0D8997B', '06AC88B5-B8BC-478D-A72D-372EC7C98123');
-- OFCW Business Statistics
update product set erights_id = 8102 where id in ('6F9C4166-F1FD-4B0F-B098-C77787675B5A', '62D5573E-EC48-429B-B36D-251496A3A3B4');
-- OFCW Principles of management (lecturer)
update product set erights_id = 8105 where id in ('210806C4-3FEB-4599-ACF0-5B2D7B776859');
-- OFCW Business Statistics (lecturer)
update product set erights_id = 8103 where id in ('1F0D7133-5D06-418F-AEC5-966C387D0CCB'); 

--TODO Release 2 UAT landing pages
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/principlesofmanagement/' where id = '8DBAF754-0238-4EAB-8D0F-26A7A0D8997B';
update product set landing_page = 'http://cw.oxfordfajar.com.my/activate/businessstatistics/' where id = '6F9C4166-F1FD-4B0F-B098-C77787675B5A';
update product set email = 'cw.info@oxfordfajar.com.my';

update registration_activation set validator_email = 'cw.info@oxfordfajar.com.my' where id = '166F6F3C-3C0F-46C8-93C5-797BA8F178DF';

delete from url_skin;
insert into url_skin (id,obj_version,url,skin_path) values (newId(),0,'http://cw.oxfordfajar.com.my','skin/oxfordfajar/cw/css/eac-override.css');
update customer set password = '76e424997c2b5f27f2c50703a4dee2d3' where id = 'bfdc5888-c26b-4ed5-a580-6aa363a25ecc';
