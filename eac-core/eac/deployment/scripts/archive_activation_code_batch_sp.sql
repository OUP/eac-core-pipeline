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
		insert into arc_activation_code_batch (id,obj_version,activation_code_format,batch_id,code_prefix,created_date,start_date,end_date,activation_code_registration_definition_id,licence_template_id,updated_date, archive_date)
		select id,obj_version,activation_code_format,batch_id,code_prefix,created_date,start_date,end_date,activation_code_registration_definition_id,licence_template_id,updated_date, @archiveDate from activation_code_batch where id=@batchDbId
	
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

	--check batch archive exists first?
	IF (select count(*) from arc_activation_code_batch where id = @batchDbId) > 0
	BEGIN
	  BEGIN TRANSACTION
		insert into activation_code_batch (id,obj_version,activation_code_format,batch_id,code_prefix,created_date,start_date,end_date,activation_code_registration_definition_id,licence_template_id,updated_date)
		select id,obj_version,activation_code_format,batch_id,code_prefix,created_date,start_date,end_date,activation_code_registration_definition_id,licence_template_id,updated_date from arc_activation_code_batch where id=@batchDbId

		insert into activation_code (id,obj_version,actual_usage,allowed_usage,code,activation_code_batch_id)
		select id,obj_version,actual_usage,allowed_usage,code,activation_code_batch_id from arc_activation_code where activation_code_batch_id=@batchDbId
	
		delete from arc_activation_code where activation_code_batch_id = @batchDbId;
	
		delete from arc_activation_code_batch where id = @batchDbId;

	COMMIT TRANSACTION
	END	
END
GO
