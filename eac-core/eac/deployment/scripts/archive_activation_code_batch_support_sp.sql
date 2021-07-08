IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_ARCHIVE_BATCH_CREATE_REGN_DEFN_DESC') 
)
  DROP PROCEDURE [dbo].EAC_ARCHIVE_BATCH_CREATE_REGN_DEFN_DESC
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_POPULATE_ARC_REGN_DEFN_DESC') 
)
  DROP PROCEDURE [dbo].EAC_POPULATE_ARC_REGN_DEFN_DESC
GO


CREATE PROCEDURE EAC_ARCHIVE_BATCH_CREATE_REGN_DEFN_DESC @regDefId nvarchar(255), @result nvarchar(255) OUTPUT
AS
BEGIN
	declare @count int = 0;
	set @result = '-1';
	
	select @count = count(*) from registration_definition where id = @regDefId;
	
	IF @count = 1
	BEGIN
		declare @id nvarchar(255);
		select @id = newId();
		insert into arc_registration_definition_desc
		(
			ID,
			REG_DEF_ID,
			REG_DEF_TYPE,
			ASSET_ID,
			ASSET_PRODUCT_NAME,
			ASSET_ERIGHTS_ID,
			PAGE_DEF_ID,
			LICENCE_TEMPLATE_ID,
			ACTIVATION_TYPE,
			PRODUCT_TYPE,
			PRODUCT_LANDING_PAGE,
			PRODUCT_HOME_PAGE,
			PRODUCT_REGISTERABLE_TYPE,
			CREATED_DATE
		) 
		select
			@id,
			RD.id,
			RD.registration_definition_type,
			A.id,
			A.product_name,
			A.erights_id,
			RD.page_definition_id,
			RD.licence_template_id,
			RA.activation_type,
			P.product_type,
			P.landing_page,
			P.home_page,
			P.registerable_type,
			getdate()
		from registration_definition RD		
		join product P on RD.product_id = P.id
		left join registration_activation RA on RD.registration_activation_id = RA.id
		join asset A on P.asset_id = A.id
		where RD.id = @regDefId;
		
		set @result = @id;

	END
	ELSE
	BEGIN
		print 'ERROR : there is no RegistrationDefinition with id [' + @regDefId + ']';		
	END 
END
GO

CREATE PROCEDURE EAC_POPULATE_ARC_REGN_DEFN_DESC
AS
BEGIN
	declare @arcActivationCodeBatchId nvarchar(255);
	declare @idRegDef nvarchar(255);
	
	DECLARE C1 CURSOR FOR  
	SELECT id, activation_code_registration_definition_id from arc_activation_code_batch where arc_registration_defn_desc_id is null;
		
	OPEN C1;
	FETCH NEXT FROM C1 INTO @arcActivationCodeBatchId,@idRegDef;
	
	WHILE @@FETCH_STATUS = 0   
	BEGIN
		declare @regDefDescId nvarchar(255)
		
       	exec EAC_ARCHIVE_BATCH_CREATE_REGN_DEFN_DESC @regDefId = @idRegDef, @result = @regDefDescId output;
       	
       	if @regDefDescId != '-1'
       	BEGIN
       		update arc_activation_code_batch set arc_registration_defn_desc_id = @regDefDescId where id=@arcActivationCodeBatchId;
       	END
       	
		FETCH NEXT FROM C1 INTO @arcActivationCodeBatchId,@idRegDef;
	END;
	
	CLOSE C1;
	DEALLOCATE C1;

END