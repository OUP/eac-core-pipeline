update page_definition set name = 'Maths for Matriculation Semester 2 4e' where id = (
select pd.id from page_definition pd, registration_definition rd, product p, asset a
where pd.id = rd.page_definition_id
and rd.product_id = p.id
and p.asset_id = a.id
and pd.name = ''
and a.product_name in (
	'Maths for Matriculation Semester 2 4e')
);
GO

update page_definition set name = 'Financial Mgmt 2e' where id = (
select pd.id from page_definition pd, registration_definition rd, product p, asset a
where pd.id = rd.page_definition_id
and rd.product_id = p.id
and p.asset_id = a.id
and pd.name = ''
and a.product_name in (
	'Financial Mgmt 2e')
);
GO

update page_definition set name = 'Chemistry for Matriculation Semester 2 4e' where id = (
select pd.id from page_definition pd, registration_definition rd, product p, asset a
where pd.id = rd.page_definition_id
and rd.product_id = p.id
and p.asset_id = a.id
and pd.name = ''
and a.product_name in (
	'Chemistry for Matriculation Semester 2 4e')
);
GO

update page_definition set name = 'Biology for Matriculation Semester 2 3e' where id = (
select pd.id from page_definition pd, registration_definition rd, product p, asset a
where pd.id = rd.page_definition_id
and rd.product_id = p.id
and p.asset_id = a.id
and pd.name = ''
and a.product_name in (
	'Biology for Matriculation Semester 2 3e')
);
GO

update page_definition set name = 'Cost Accounting 5e' where id = (
select pd.id from page_definition pd, registration_definition rd, product p, asset a
where pd.id = rd.page_definition_id
and rd.product_id = p.id
and p.asset_id = a.id
and pd.name = ''
and a.product_name in (
	'Cost Accounting 5e')
);
GO

update page_definition set name = 'Physics for Matriculation Semester 2 4e' where id = (
select pd.id from page_definition pd, registration_definition rd, product p, asset a
where pd.id = rd.page_definition_id
and rd.product_id = p.id
and p.asset_id = a.id
and pd.name = ''
and a.product_name in (
	'Physics for Matriculation Semester 2 4e')
);
GO

update registration_definition set page_definition_id = '207BFAE7-57CD-4570-BA7B-2F10B7BAA337' where page_definition_id in 
(select pd.id from page_definition pd where name in (	
	'Maths for Matriculation Semester 2 4e',
	'Financial Mgmt 2e',
	'Chemistry for Matriculation Semester 2 4e',
	'Biology for Matriculation Semester 2 3e',
	'Cost Accounting 5e',
	'Physics for Matriculation Semester 2 4e'));
GO

delete from page_component where page_definition_id in 
(select pd.id from page_definition pd where name in (	
	'Maths for Matriculation Semester 2 4e',
	'Financial Mgmt 2e',
	'Chemistry for Matriculation Semester 2 4e',
	'Biology for Matriculation Semester 2 3e',
	'Cost Accounting 5e',
	'Physics for Matriculation Semester 2 4e'));
GO

delete from page_definition where id in
(select pd.id from page_definition pd where name in (	
	'Maths for Matriculation Semester 2 4e',
	'Financial Mgmt 2e',
	'Chemistry for Matriculation Semester 2 4e',
	'Biology for Matriculation Semester 2 3e',
	'Cost Accounting 5e',
	'Physics for Matriculation Semester 2 4e'));
GO

update page_definition set name = 'Cost Accounting 5e (Lecturer)' where id = (
select pd.id from page_definition pd, registration_definition rd, product p, asset a
where pd.id = rd.page_definition_id
and rd.product_id = p.id
and p.asset_id = a.id
and pd.name = ''
and a.product_name in (
	'Cost Accounting 5e (Lecturer)')
);
GO

update page_definition set name = 'Financial Mgmt 2e (Lecturer)' where id = (
select pd.id from page_definition pd, registration_definition rd, product p, asset a
where pd.id = rd.page_definition_id
and rd.product_id = p.id
and p.asset_id = a.id
and pd.name = ''
and a.product_name in (
	'Financial Mgmt 2e (Lecturer)')
);
GO

update registration_definition set page_definition_id = '207BFAE7-57CD-4570-BA7B-2F10B7BAA337' where page_definition_id in 
(select pd.id from page_definition pd where name in ('Cost Accounting 5e (Lecturer)', 'Financial Mgmt 2e (Lecturer)'));
GO

delete from page_component where page_definition_id in 
(select pd.id from page_definition pd where name in ('Cost Accounting 5e (Lecturer)', 'Financial Mgmt 2e (Lecturer)'));
GO

delete from page_definition where id in
(select pd.id from page_definition pd where name in ('Cost Accounting 5e (Lecturer)', 'Financial Mgmt 2e (Lecturer)'));
GO

update page_definition set name = 'OTC Account Registration' where id = '262e8967-2067-4a72-bb8a-1847ac0249f7';
GO
update page_definition set name = 'OTC Product Registration' where id = '8992DD30-D16F-405F-B2C0-7A492491EEC4'
GO

-- Constraints to lock down page_definition.name. These need to go here rather than in
-- the schema script because they depend upon the updates above being executed first.

-- Prevent the name being set to null
alter table page_definition alter column name nvarchar(255) not null;
GO
-- Prevent duplicate names
alter table page_definition add constraint unq_page_def_name unique nonclustered (name);
GO
-- Prevent the name being set to empty string
alter table page_definition with check add constraint chk_page_def_name check ((name <> N''));
GO