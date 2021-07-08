--
-- OXFORD TEACHERS' CLUB MIGRATION 
--
-- This script gets the IDs of the newly migrated customers for the Oxford Teachers' Club.
-- The startTime will have to be amended.
-- The output of this query can be exported as a CSV file from within 'Microsoft SQL Server Management Studio' 
-- Author : David Hay
--
declare @startTime datetime; 
set @startTime = convert(datetime,'2012-04-30 19:00:00',20);

select CM.eac_customer_id EAC_CUSTOMER_ID
	   ,CM.books_erights_id BOOKS_ERIGHTS_ID
--       ,CM.created_date MIGRATION_TS 
       from customer_migration CM
where CM.migration_state = 'CUSTOMER_REGISTERED'
and   CM.eac_customer_id is not null
and   CM.eac_registration_id is not null
and   CM.created_date > @startTime
order by CM.books_erights_id;