-- This script allows for deletion of an activation code batch provided
-- none of the activation codes have been used.
--
-- Change batchId to be id of the batch to be deleted
DECLARE @numCodesInUse int;
DECLARE @batchExists int;
DECLARE @batchId varchar(255) = '';

SELECT @batchExists = COUNT(*) from activation_code_batch where id = @batchId;

if @batchExists = 0 goto NOBATCH;

SELECT @numCodesInUse = COUNT(*) from activation_code where activation_code_batch_id = @batchId and actual_usage > 0;

if @numCodesInUse = 0
	BEGIN
		BEGIN TRANSACTION
			delete from activation_code where activation_code_batch_id = @batchId;    
			delete from activation_code_batch where id = @batchId;
			print 'Successfully deleted activation code batch with id ' + @batchId + ' and associated codes';
		commit;
	END
ELSE
	BEGIN
		print 'Could not delete activation code batch because some of the codes in this batch have already been used';
	END;
GOTO FINISH;
NOBATCH: PRINT 'Batch ' + @batchId + ' does not exist!';
FINISH: PRINT 'Finished!';
