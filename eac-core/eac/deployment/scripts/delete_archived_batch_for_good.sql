-- Verify that the stored procedure does not already exist.
IF OBJECT_ID ( 'EAC_DELETE_ARCHIVED_BATCH_ERROR_INFO', 'P' ) IS NOT NULL 
    DROP PROCEDURE EAC_DELETE_ARCHIVED_BATCH_ERROR_INFO;
GO

-- Verify that the stored procedure does not already exist.
IF OBJECT_ID ( 'EAC_DELETE_ARCHIVED_BATCH_FOR_GOOD', 'P' ) IS NOT NULL 
    DROP PROCEDURE EAC_DELETE_ARCHIVED_BATCH_FOR_GOOD;
GO

--- Used to get information about why stored procedure failed.
CREATE PROCEDURE EAC_DELETE_ARCHIVED_BATCH_ERROR_INFO @batchName nvarchar(255)
AS
SELECT
	ERROR_PROCEDURE() AS ErrorProcedure
	,@batchName as ARCHIVED_BATCH_NAME
    ,ERROR_NUMBER() AS ErrorNumber
    ,ERROR_SEVERITY() AS ErrorSeverity
    ,ERROR_STATE() AS ErrorState
    ,ERROR_LINE() AS ErrorLine
    ,ERROR_MESSAGE() AS ErrorMessage;
GO

/*
 ***Best to back up the database before calling this stored procedure.***

Permanently Deletes Archived Batch.
Takes the archived batch name as a parameter AND the archived batch's database id.

*/
CREATE PROCEDURE EAC_DELETE_ARCHIVED_BATCH_FOR_GOOD @batchName nvarchar(255), @batchDbId nvarchar(255)
AS
BEGIN TRY
    -- Generate divide-by-zero error.
    --SELECT 1/0;	
	declare @exists int;
	declare @result int = 0;
	select @exists = count(*) from arc_activation_code_batch where batch_id=@batchName and id=@batchDbId;
	IF @exists = 1 
	BEGIN
		BEGIN TRANSACTION
		
		delete AAC from arc_activation_code AAC
		join arc_activation_code_batch AACB on AAC.activation_code_batch_id = AACB.id
		where AACB.batch_id=@batchName;

		delete from arc_activation_code_batch where batch_id=@batchName;
		set @result = 1;
		COMMIT TRANSACTION
		print 'Committed. Archived batch [' + @batchName + '/' + @batchDbId + '] has now been permanently deleted.'
	END
	ELSE
	BEGIN
		print 'Cannot Delete Archived Batch with BatchName [' + @batchName + '] Id[' + @batchDbId +']. It does not exist.';
		set @result = -1;
	END
	print 'Result is ' + cast(@result as nvarchar(255));
	return @result;
END TRY
BEGIN CATCH
	IF @@TRANCOUNT > 0 
	BEGIN
		ROLLBACK;
		print 'Rolled Back.';
	END;
    -- Execute error retrieval routine.
    EXECUTE EAC_DELETE_ARCHIVED_BATCH_ERROR_INFO @batchName=@batchName
	return @result;
END CATCH; 