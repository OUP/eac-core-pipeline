IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_ARCHIVE_BATCH') 
)
  DROP PROCEDURE [dbo].eac_archive_batch
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_UN_ARCHIVE_BATCH'))
  DROP PROCEDURE [dbo].eac_un_archive_batch
GO

--
-- This stored procedure is called from EAC Admin when an admin 'deletes' an Activation Code Batch
--
CREATE PROCEDURE EAC_ARCHIVE_BATCH @batchDbId nvarchar(255)
AS
BEGIN
	declare @archiveDate datetime2 = getdate();

	--check batch exists first?
	IF (select count(*) from activation_code_batch where id = @batchDbId) > 0
	BEGIN		
	  BEGIN TRANSACTION
	  	
	  	declare @arcRegDefDescId nvarchar(255);
	  	declare @idRegDef nvarchar(255);
	  	
	  	select @idRegDef = activation_code_registration_definition_id from activation_code_batch where id=@batchDbId;
	  	
	  	exec EAC_ARCHIVE_BATCH_CREATE_REGN_DEFN_DESC @regDefId = @idRegDef, @result = @arcRegDefDescId output;
	  
		insert into arc_activation_code_batch (id,obj_version,activation_code_format,batch_id,code_prefix,created_date,start_date,end_date,licence_template_id,updated_date, archive_date, arc_registration_defn_desc_id)
		select id,obj_version,activation_code_format,batch_id,code_prefix,created_date,start_date,end_date,licence_template_id,updated_date, @archiveDate, @arcRegDefDescId from activation_code_batch where id=@batchDbId
	
		insert into arc_activation_code (id,obj_version,actual_usage,allowed_usage,code,activation_code_batch_id,archive_date)
		select id,obj_version,actual_usage,allowed_usage,code,activation_code_batch_id,@archiveDate from activation_code where activation_code_batch_id=@batchDbId
	
		delete from activation_code where activation_code_batch_id = @batchDbId;
	
		delete from activation_code_batch where id = @batchDbId;

	  COMMIT TRANSACTION
	END	
END
GO

--
-- This stored procedure is NOT called by EAC code but is for un-archiving batches manually if someone archives a batch by mistake.
--
CREATE PROCEDURE EAC_UN_ARCHIVE_BATCH @batchDbId nvarchar(255)
AS
BEGIN
	declare @result INT = -1;
	
	--check batch archive exists first?
	IF (
	select count(*) from arc_activation_code_batch ACB
	  join arc_registration_definition_desc REG_DEF_DESC on ACB.arc_registration_defn_desc_id = REG_DEF_DESC.id
	  join registration_definition RD                    on RD.id = REG_DEF_DESC.REG_DEF_ID  -- original reg_def_id still exists! 
	  where ACB.id = @batchDbId
	) > 0
	BEGIN
	  BEGIN TRANSACTION
		insert into activation_code_batch (id,obj_version,activation_code_format,batch_id,code_prefix,created_date,start_date,end_date,activation_code_registration_definition_id,licence_template_id,updated_date)
		select ACB.id, ACB.obj_version, ACB.activation_code_format, ACB.batch_id, ACB.code_prefix, ACB.created_date, ACB.start_date, ACB.end_date,  REG_DEF_DESC.REG_DEF_ID, ACB.licence_template_id, ACB.updated_date 
		from arc_activation_code_batch ACB 
		join arc_registration_definition_desc REG_DEF_DESC on ACB.arc_registration_defn_desc_id = REG_DEF_DESC.id
		where ACB.id=@batchDbId

		insert into activation_code (id,obj_version,actual_usage,allowed_usage,code,activation_code_batch_id)
		select id,obj_version,actual_usage,allowed_usage,code,activation_code_batch_id from arc_activation_code where activation_code_batch_id=@batchDbId
	
		delete from arc_activation_code where activation_code_batch_id = @batchDbId;
	
		delete from arc_activation_code_batch where id = @batchDbId;

		set @result = 1;
	COMMIT TRANSACTION
	END	
	return @result;
END
GO
