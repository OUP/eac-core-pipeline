-- ASSET REMOVAL SCRIPT
SP_RENAME 'ARC_REGISTRATION_DEFINITION_DESC.ASSET_ID' , 'PRODUCT_ID', 'COLUMN';
SP_RENAME 'ARC_REGISTRATION_DEFINITION_DESC.ASSET_PRODUCT_NAME' , 'PRODUCT_NAME', 'COLUMN';
SP_RENAME 'ARC_REGISTRATION_DEFINITION_DESC.ASSET_ERIGHTS_ID' , 'PRODUCT_ERIGHTS_ID', 'COLUMN';

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [DBO].[EAC_ARCHIVE_BATCH_CREATE_REGN_DEFN_DESC] @REGDEFID NVARCHAR(255), @RESULT NVARCHAR(255) OUTPUT
AS
BEGIN
	DECLARE @COUNT INT = 0;
	SET @RESULT = '-1';

	SELECT @COUNT = COUNT(*) FROM REGISTRATION_DEFINITION WHERE ID = @REGDEFID;

	IF @COUNT = 1
	BEGIN
		DECLARE @ID NVARCHAR(255);
		SELECT @ID = NEWID();
		INSERT INTO ARC_REGISTRATION_DEFINITION_DESC
		(
			ID,
			REG_DEF_ID,
			REG_DEF_TYPE,
			PRODUCT_ID,
			PRODUCT_NAME,
			PRODUCT_ERIGHTS_ID,
			PAGE_DEF_ID,
			LICENCE_TEMPLATE_ID,
			ACTIVATION_TYPE,
			PRODUCT_TYPE,
			PRODUCT_LANDING_PAGE,
			PRODUCT_HOME_PAGE,
			PRODUCT_REGISTERABLE_TYPE,
			CREATED_DATE
		) 
		SELECT
			@ID,
			RD.ID,
			RD.REGISTRATION_DEFINITION_TYPE,
			P.ID,
			P.PRODUCT_NAME,
			P.ERIGHTS_ID,
			RD.PAGE_DEFINITION_ID,
			RD.LICENCE_TEMPLATE_ID,
			RA.ACTIVATION_TYPE,
			P.PRODUCT_TYPE,
			P.LANDING_PAGE,
			P.HOME_PAGE,
			P.REGISTERABLE_TYPE,
			GETDATE()
		FROM REGISTRATION_DEFINITION RD		
		JOIN PRODUCT P ON RD.PRODUCT_ID = P.ID
		LEFT JOIN REGISTRATION_ACTIVATION RA ON RD.REGISTRATION_ACTIVATION_ID = RA.ID
		WHERE RD.ID = @REGDEFID;

		SET @RESULT = @ID;

	END
	ELSE
	BEGIN
		PRINT 'ERROR : THERE IS NO REGISTRATIONDEFINITION WITH ID [' + @REGDEFID + ']';		
	END 
END


--RUN ALL DML script related to product and division then run this script
ALTER TABLE PRODUCT DROP CONSTRAINT FK_PRODUCT_TO_ASSET;
ALTER TABLE PRODUCT DROP COLUMN ASSET_ID;


-- product De-duplication
alter table registration_definition drop constraint FK224324991E74A5F
 
--Sprint6 Customer de-duplication
alter table customer alter column reset_password bit NULL
alter table customer alter column enabled bit NULL
alter table customer alter column username nvarchar (255) NULL
alter table customer alter column failed_attempts int NULL
alter table customer drop constraint[UQ__customer__F3DBC5728EF5227F]
alter table page_definition drop constraint[FKE93D4903BDF051EC]
alter table page_definition alter column division_id nvarchar(255) NULL


ALTER TABLE CUSTOMER DROP COLUMN locale;
ALTER TABLE CUSTOMER DROP COLUMN time_zone;
ALTER TABLE CUSTOMER DROP COLUMN last_login;
ALTER TABLE CUSTOMER DROP COLUMN email_verification_state;

ALTER TABLE PRODUCT DROP COLUMN landing_page
ALTER TABLE PRODUCT DROP COLUMN service_level_agreement
ALTER TABLE PRODUCT DROP COLUMN home_page
ALTER TABLE PRODUCT DROP COLUMN email
ALTER TABLE PRODUCT DROP COLUMN registerable_type
ALTER TABLE PRODUCT DROP COLUMN STATE
ALTER TABLE PRODUCT DROP COLUMN PRODUCT_NAME
ALTER TABLE PRODUCT DROP COLUMN PRODUCT_ID
ALTER TABLE PRODUCT DROP COLUMN division_erights_id
ALTER TABLE PRODUCT DROP CONSTRAINT [DF__product__divisio__73852659]
ALTER TABLE PRODUCT DROP COLUMN division_id

ALTER TABLE registration_definition drop [FK224324991E74A5F]
ALTER TABLE registration_definition DROP COLUMN registration_activation_id
ALTER TABLE registration_definition drop [FK22432499E6EE206B]
ALTER TABLE registration_definition DROP COLUMN licence_template_id
ALTER TABLE registration_definition DROP COLUMN confirmation_email_enabled
ALTER TABLE registration_definition drop [UQ__registra__D6870BD3EE1F69C6]
ALTER TABLE registration_definition drop [FK_RD_TO_EAC_GROUP]
ALTER TABLE registration_definition DROP COLUMN group_id
ALTER TABLE division_admin_user DROP [FKCE14418DBDF051EC]
ALTER TABLE division_admin_user DROP COLUMN division_id


DROP table product_group_mapping
DROP table eac_groups
DROP table external_identifier
DROP table external_system_id_type
DROP table external_system 
drop table registration_activation
DROP table linked_registration
DROP table registration_migration
DROP table registration_migration_data
DROP table registration
DROP table activation_code
DROP table activation_code_batch
DROP table arc_activation_code
DROP table arc_activation_code_batch
DROP table licence_template
DROP table asset
DROP table division
ALTER TABLE answer drop constraint [FKABCA3FBE9770384C]
ALTER TABLE  registration_definition drop constraint [FK22432499548AFB8B]
DROP table product;