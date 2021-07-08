-- Allowing ORCS to search for products/assets - http://mantisoup.idm.fr/view.php?id=15712
delete from role_permissions where role_id = '04B20C68-DF39-41C3-B70E-B4A3F4F27657' and permission_id = 'E4DBC4A1-2B1C-4753-9D6E-5DD341748CDC';
GO
delete from role_permissions where role_id = 'D5F17EEA-BDB0-426D-8328-45FCBF83433E' and permission_id = 'E4DBC4A1-2B1C-4753-9D6E-5DD341748CDC';
GO
delete from role_permissions where role_id = '04B20C68-DF39-41C3-B70E-B4A3F4F27657' and permission_id = 'D7A27D8E-2AA4-4F50-AF47-6FD9F0F7FA2F';
GO
delete from role_permissions where role_id = 'D5F17EEA-BDB0-426D-8328-45FCBF83433E' and permission_id = 'D7A27D8E-2AA4-4F50-AF47-6FD9F0F7FA2F';
GO
delete from role_permissions where role_id = 'D5F17EEA-BDB0-426D-8328-45FCBF83433E' and permission_id = 'D3BB14B7-0A83-424D-9294-23AB76577683';
GO
delete from role_permissions where role_id = '04B20C68-DF39-41C3-B70E-B4A3F4F27657' and permission_id = 'D3BB14B7-0A83-424D-9294-23AB76577683';
GO
insert into role_permissions values ('D5F17EEA-BDB0-426D-8328-45FCBF83433E', 'AA77640E-6D1D-41B0-A23F-05430189C98A');
GO
insert into role_permissions values ('353FB7EC-8F39-4A41-B4A7-28C06B52E410', 'AA77640E-6D1D-41B0-A23F-05430189C98A');
GO
insert into role_permissions values ('04B20C68-DF39-41C3-B70E-B4A3F4F27657', 'AA77640E-6D1D-41B0-A23F-05430189C98A');
GO

-- Granting CREATE_REGISTRATION for ORCS - http://mantisoup.idm.fr/view.php?id=15789
insert into role_permissions values ('7DAB80B7-3CBA-49BE-8F4B-B7DD1A13B18A', '91CB7A26-E805-4C36-9D74-1E887F9B0F0E');
GO
insert into role_permissions values ('04B20C68-DF39-41C3-B70E-B4A3F4F27657', '91CB7A26-E805-4C36-9D74-1E887F9B0F0E');
GO
insert into role_permissions values ('353FB7EC-8F39-4A41-B4A7-28C06B52E410', '91CB7A26-E805-4C36-9D74-1E887F9B0F0E');
GO
insert into role_permissions values ('D5F17EEA-BDB0-426D-8328-45FCBF83433E', '91CB7A26-E805-4C36-9D74-1E887F9B0F0E');
GO