IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_DELETE_ASSET_FOR_GOOD') 
)
  DROP PROCEDURE [dbo].eac_delete_asset_for_good
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_DELETE_REGISTERABLE_PRODUCT_FOR_GOOD') 
)
  DROP PROCEDURE [dbo].eac_delete_registerable_product_for_good
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'EAC_DELETE_LINKED_PRODUCT_FOR_GOOD') 
)
  DROP PROCEDURE [dbo].eac_delete_linked_product_for_good
GO

--
-- This stored procedure deletes a linked product and it's associated data from the EAC database for good.
-- 
-- ** NO GOING BACK. ONCE IT'S GONE, IT'S GONE. **
--
CREATE PROCEDURE EAC_DELETE_LINKED_PRODUCT_FOR_GOOD @productId nvarchar(255)
AS
BEGIN
	--check product's asset has been removed first
	declare @count int  = 0;
	
	set @count = ( select count(*) from asset A
		join product P on P.asset_id = A.id
	    where P.id = @productId
	    and A.state = 'REMOVED' );
	    
		
	IF @count = 0
	BEGIN
		RETURN
	END;
	
	print 'delete from linked_registration where linkedProductId = ' + cast(@productId as nvarchar);
	delete LR from linked_registration LR
	join product LP on LR.linked_product_id = LP.id		
	where LP.id = @productId
	and LP.product_type='LINKED';
		
	print 'delete from linked product where productId = ' + cast(@productId as nvarchar);
	delete LP from product LP		
	where LP.id = @productId
	and LP.product_type='LINKED';

END
GO

--
-- This stored procedure deletes a registerable product and it's associated data from the EAC database for good.
-- 
-- ** NO GOING BACK. ONCE IT'S GONE, IT'S GONE. **
--
CREATE PROCEDURE EAC_DELETE_REGISTERABLE_PRODUCT_FOR_GOOD @productId nvarchar(255)
AS
BEGIN
	declare @activationCodeBatchId nvarchar(255);

	--check product's asset has been removed first
	declare @count int  = 0;
	
	set @count = ( select count(*) from asset A
		join product P on P.asset_id = A.id
	    where P.id = @productId
	    and A.state = 'REMOVED' );
	    
	IF @count = 0
	BEGIN
	   RETURN
	END;
	    
	print 'deleting registerable product with id ' + cast(@productId as nvarchar);
	
	DECLARE C1 CURSOR FOR  
	SELECT ACB.id from activation_code_batch ACB
	join registration_definition RD on RD.id = ACB.activation_code_registration_definition_id
	where RD.product_id = @productId;

	IF @count > 0
	BEGIN		

		--1
		print '1 delete from answer where productId = ' + cast(@productId as nvarchar);
		delete ANS from answer ANS where ANS.registerable_product_id=@productId

		--2
		print '2 delete from linked registration where registerable productId = ' + cast(@productId as nvarchar);
		delete LR 
		from product RP
		left join product LP on LP.registerable_product_id = RP.id
		left join linked_registration LR on LR.linked_product_id = LP.id
		where RP.id=@productId;

		--3
		print '3 delete from linked product where registerable productId = ' + cast(@productId as nvarchar);
		delete LP 
		from product RP
		left join product LP on LP.registerable_product_id = RP.id
		left join linked_registration LR on LR.linked_product_id = LP.id
		where RP.id=@productId;

		--4
		print '4 delete from registration where productId = ' + cast(@productId as nvarchar);
		delete R from registration R 
		join registration_definition RD on RD.id = R.registration_definition_id
		where RD.product_id = @productId;
		
		--5 ARCHIVE ANY RELATED ACTIVATION_CODE_BATCHES
		OPEN C1;
		FETCH NEXT FROM C1 INTO @activationCodeBatchId;

		WHILE @@FETCH_STATUS = 0   
		BEGIN        
        	print '5 calling EAC_ARCHIVE_BATCH with id = ' + cast(@activationCodeBatchId as nvarchar);
        	exec EAC_ARCHIVE_BATCH @batchDbId = @activationCodeBatchId;  --must check that this is working, I have my doubts.
			FETCH NEXT FROM C1 INTO @activationCodeBatchId;
		END;
		CLOSE C1;
		DEALLOCATE C1;
		
		--6
		print '6 delete from registration_definition where product_id = ' + cast(@productId as nvarchar);
		delete from registration_definition where product_id = @productId;
	
		--7		
		print '7 delete from external_identifier where product_id = ' + cast(@productId as nvarchar);
		delete from external_identifier where product_id = @productId;

		--8
		print '8 delete from product where product_id = ' + cast(@productId as nvarchar);
		delete from product where id = @productId;
		
 	END
END
GO

--
-- This stored procedure deletes an ASSET and it's associated data from the EAC database for good.
-- 
-- ** NO GOING BACK. ONCE ITS GONE, ITS GONE. **
--
CREATE PROCEDURE EAC_DELETE_ASSET_FOR_GOOD @assetId nvarchar(255)
AS
BEGIN
	declare @result int = -1;

	--check product is removed first
	declare @count int  = 0;
	
	declare @prodId nvarchar(255);
	
	set @count = ( select count(*) from asset A 
	    where A.id = @assetId
	    and A.state = 'REMOVED' );
	    
	IF @count > 0
	BEGIN		
		set @result = -2;
		BEGIN TRANSACTION
	
	  	DECLARE C3 CURSOR FOR  		
		SELECT P.id from product P 		
		where P.asset_id = @assetId
		and P.product_type='LINKED';

	  	DECLARE C4 CURSOR FOR  		
		SELECT P.id from product P 		
		where P.asset_id = @assetId
		and P.product_type='REGISTERABLE';

	  	DECLARE C5 CURSOR FOR
		SELECT P.id from product P 		
		where P.asset_id = @assetId;	

		OPEN C3;
		FETCH NEXT FROM C3 INTO @prodId;

		WHILE @@FETCH_STATUS = 0   
		BEGIN
			print 'calling EAC_DELETE_LINKED_PRODUCT_FOR_GOOD @productid = ' + cast(@prodId as nvarchar);
        	exec EAC_DELETE_LINKED_PRODUCT_FOR_GOOD @productId = @prodId;
			FETCH NEXT FROM C3 INTO @prodId;
		END
		CLOSE C3  
		DEALLOCATE C3

		
		OPEN C4;
		FETCH NEXT FROM C4 INTO @prodId;

		WHILE @@FETCH_STATUS = 0
		BEGIN
       		print 'calling EAC_DELETE_REGISTERABLE_PRODUCT_FOR_GOOD @productid = ' + cast(@prodId as nvarchar);
			exec EAC_DELETE_REGISTERABLE_PRODUCT_FOR_GOOD @productId = @prodId;
			FETCH NEXT FROM C4 INTO @prodId;
		END
		CLOSE C4  
		DEALLOCATE C4		

		OPEN C5
		FETCH NEXT FROM C5 INTO @prodId;

		WHILE @@FETCH_STATUS = 0   
		BEGIN
  			print 'ERROR : Have not deleted product with id ' + cast(@prodId as nvarchar);
			FETCH NEXT FROM C5 INTO @prodId;
		END
		CLOSE C5  
		DEALLOCATE C5
		
		print 'delete from asset where id = ' + cast(@assetId as nvarchar);
		delete from asset where id = @assetId;
   
	  set @result = 1;
	  COMMIT TRANSACTION
	END
	ELSE
	BEGIN
		print 'Cannot delete asset.';
		set @result=-3;
	END 
	return @result;
END
GO

