create table arc_registration_definition_desc
(
	ID						nvarchar(255) NOT NULL PRIMARY KEY,
	REG_DEF_ID				nvarchar(255) NOT NULL,
	REG_DEF_TYPE			nvarchar(255) NOT NULL,
	ASSET_ID				nvarchar(255) NOT NULL,
	ASSET_PRODUCT_NAME		nvarchar(255) NOT NULL,
	ASSET_ERIGHTS_ID		nvarchar(255) NOT NULL,
	PAGE_DEF_ID				nvarchar(255),
	LICENCE_TEMPLATE_ID		nvarchar(255),
	ACTIVATION_TYPE			nvarchar(255),
	PRODUCT_TYPE			nvarchar(255) NOT NULL,
	PRODUCT_LANDING_PAGE	nvarchar(255) NULL,
	PRODUCT_HOME_PAGE		nvarchar(255) NULL,
	PRODUCT_REGISTERABLE_TYPE nvarchar(255) NOT NULL,
	CREATED_DATE			datetime2(7) NOT NULL default getdate()
);
GO

ALTER TABLE arc_activation_code_batch ADD arc_registration_defn_desc_id nvarchar(255) NULL;
GO

declare @fkName nvarchar(255)

SELECT  
    @fkName = fk.name
FROM 
    sys.foreign_keys fk
INNER JOIN 
    sys.foreign_key_columns fkc ON fkc.constraint_object_id = fk.object_id
INNER JOIN
    sys.columns c1 ON fkc.parent_column_id = c1.column_id AND fkc.parent_object_id = c1.object_id
INNER JOIN
    sys.columns c2 ON fkc.referenced_column_id = c2.column_id AND fkc.referenced_object_id = c2.object_id
	where object_name(fk.parent_object_id) = 'ARC_ACTIVATION_CODE_BATCH'
	and   c1.name = 'ACTIVATION_CODE_REGISTRATION_DEFINITION_ID'
	and   object_name(fk.referenced_object_id) = 'REGISTRATION_DEFINITION'
	and   c2.name = 'ID'

IF NOT(@fkName is null)
BEGIN
	print 'fkName is ' + @fkName;

	declare @sql nvarchar(1000);
	set @sql = 'IF EXISTS (SELECT * ' +
	' FROM sys.foreign_keys ' +
	' WHERE object_id = OBJECT_ID(''' + @fkName + ''')' +
	' AND parent_object_id = OBJECT_ID(N''ARC_ACTIVATION_CODE_BATCH'') '+
	' ) '+
	' ALTER TABLE ARC_ACTIVATION_CODE_BATCH DROP CONSTRAINT ' + @fkName ;

	print 'sql is ' + @sql;
	exec (@sql);

END
GO

