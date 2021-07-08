
IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='eac_groups') DROP TABLE eac_groups;
GO

IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='product_group_mapping') DROP TABLE product_group_mapping;
GO

CREATE TABLE eac_groups
	(
	id                       NVARCHAR (255) NOT NULL,
	group_name               NVARCHAR (255) NOT NULL,
	obj_version              NUMERIC (19) NOT NULL,
	created_date             DATETIME2 NOT NULL,
	updated_date             DATETIME2,
	created_by_admin_user_id NVARCHAR (255) NOT NULL,
	updated_by_admin_user_id NVARCHAR (255),
	is_editable              BIT NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT FK_EAC_GROUP_TO_ADMIN_USER_CREATED FOREIGN KEY (created_by_admin_user_id) REFERENCES dbo.customer (id),
	CONSTRAINT FK_EAC_GROUP_TO_ADMIN_USER_UPDATED FOREIGN KEY (updated_by_admin_user_id) REFERENCES dbo.customer (id)
	)
GO

CREATE TABLE product_group_mapping
	(
	group_id   NVARCHAR (255) NOT NULL,
	product_id NVARCHAR (255),
	UNIQUE (group_id, product_id),
	CONSTRAINT FK_PRODUCT_GROUP_MAPPING_TO_GROUP FOREIGN KEY (group_id) REFERENCES dbo.eac_groups (id),
	CONSTRAINT FK_PRODUCT_GROUP_MAPPING_TO_PRODUCT FOREIGN KEY (product_id) REFERENCES dbo.product (id)
	)
GO

ALTER TABLE dbo.registration_definition
	ADD group_id NVARCHAR (255)
	CONSTRAINT FK_RD_TO_EAC_GROUP FOREIGN KEY (group_id) REFERENCES dbo.eac_groups (id)
GO


Declare @Cons_Name NVARCHAR(100)
Declare @Str NVARCHAR(500)

SELECT @Cons_Name=name
FROM sys.objects
WHERE type='UQ' AND OBJECT_NAME(parent_object_id) = N'registration_definition';

---- Delete the unique constraint.
SET @Str='ALTER TABLE registration_definition DROP ' + @Cons_Name;
Exec (@Str)
GO


ALTER TABLE dbo.registration_definition
	ADD UNIQUE (product_id, registration_definition_type,group_id)
GO

ALTER TABLE dbo.eac_groups
	ADD UNIQUE (group_name)
GO
