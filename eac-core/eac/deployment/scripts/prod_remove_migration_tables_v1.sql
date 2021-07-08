use eacprod;

exec eac_drop_fk 'customer_migration', 'customer';
exec eac_drop_fk 'customer_migration', 'registration';
exec eac_drop_fk 'customer_migration', 'customer_migration_data';
exec eac_drop_fk 'customer_migration_warning','customer_migration_data';

IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration_warning') 
BEGIN
    TRUNCATE TABLE customer_migration_warning;
    DROP TABLE     customer_migration_warning;
    print 'Dropped table CUSTOMER_MIGRATION_WARNING';
END
ELSE
BEGIN
    print 'The table CUSTOMER_MIGRATION_WARNING does not exist'
END

IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration') 
BEGIN
    TRUNCATE TABLE customer_migration;
    DROP TABLE     customer_migration;
    print 'Dropped table CUSTOMER_MIGRATION';
END
ELSE
BEGIN
    print 'The table CUSTOMER_MIGRATION does not exist'
END

IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration_data') 
BEGIN
    TRUNCATE TABLE customer_migration_data;
    DROP TABLE     customer_migration_data;
    print 'Dropped table CUSTOMER_MIGRATION_DATA';
END
ELSE
BEGIN
    print 'The table CUSTOMER_MIGRATION_DATA does not exist'
END
 
