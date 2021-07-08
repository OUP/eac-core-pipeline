update permission set name = 'CREATE_REGISTRATION_REPORT' where id = '5b6414f1-dee2-4aa5-a41e-4228c84df7be';
GO

exec dbo.EAC_CLEANUP_ALL_ADMIN_ROLES
GO

-- Fix for incorrect product registration pages - http://mantisoup.idm.fr/view.php?id=15713
update registration_definition set page_definition_id = '13AC20F6-0414-4569-B6F0-D92FC3AE24A0' where id in (
	select rd.id from registration_definition rd, product p, asset ass
	where ass.id = p.asset_id
	and p.id = rd.product_id
	and product_name in ('Biology for Matriculation Semester 2 3e', 
						'Chemistry for Matriculation Semester 2 4e', 
						'Cost Accounting 5e', 'Financial Mgmt 2e', 
						'Maths for Matriculation Semester 2 4e', 
						'Physics for Matriculation Semester 2 4e')
	and rd.registration_definition_type != 'ACCOUNT_REGISTRATION');
GO

