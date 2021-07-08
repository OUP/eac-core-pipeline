--Update OFCW Admin and linked products with erights id for PRODUCTION
--Admin Product
update product set erights_id=10801 where id='9AE8CE9A-2495-48EA-898C-4BD8CAA8E291';
--Linked Product
update product set erights_id=10803 where id='7D8DEFE7-04AC-469F-99F7-86BB2EF7958F';

--remove dodgy translations
delete from message where language is not null;