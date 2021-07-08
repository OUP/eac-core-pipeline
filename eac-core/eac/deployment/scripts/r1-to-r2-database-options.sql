if exists (select * from sys.databases WHERE name = 'eactest') 
BEGIN
	print 'Putting database into SINGLE_USER mode!';

	ALTER DATABASE eactest SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
	
	print 'Turning on READ_COMMITED_SNAPHOT on eactest!';
	
	ALTER DATABASE eactest
		SET READ_COMMITTED_SNAPSHOT ON;
    
    print 'Turning on ALLOW_SNAPSHOT_ISOLATION on eactest!';
    
	ALTER DATABASE eactest
		SET ALLOW_SNAPSHOT_ISOLATION ON;
	
	print 'Putting database into MULTI_USER mode!';
		
	ALTER DATABASE eactest SET MULTI_USER WITH ROLLBACK IMMEDIATE;
END
/
if exists (select * from sys.databases WHERE name = 'eacuat') 
BEGIN
	print 'Putting database into SINGLE_USER mode!';

	ALTER DATABASE eacuat SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
	
	print 'Turning on READ_COMMITED_SNAPHOT on eacuat!';
	
	ALTER DATABASE eacuat
		SET READ_COMMITTED_SNAPSHOT ON;
    
    print 'Turning on ALLOW_SNAPSHOT_ISOLATION on eacuat!';
    
	ALTER DATABASE eacuat
		SET ALLOW_SNAPSHOT_ISOLATION ON;
	
	print 'Putting database into MULTI_USER mode!';
		
	ALTER DATABASE eacuat SET MULTI_USER WITH ROLLBACK IMMEDIATE;
END
/
if exists (select * from sys.databases WHERE name = 'eacprod') 
BEGIN
	print 'Putting database into SINGLE_USER mode!';

	ALTER DATABASE eacprod SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
	
	print 'Turning on READ_COMMITED_SNAPHOT on eacprod!';
	
	ALTER DATABASE eacprod
		SET READ_COMMITTED_SNAPSHOT ON;
    
    print 'Turning on ALLOW_SNAPSHOT_ISOLATION on eacprod!';
    
	ALTER DATABASE eacprod
		SET ALLOW_SNAPSHOT_ISOLATION ON;
	
	print 'Putting database into MULTI_USER mode!';
		
	ALTER DATABASE eacprod SET MULTI_USER WITH ROLLBACK IMMEDIATE;
END
/