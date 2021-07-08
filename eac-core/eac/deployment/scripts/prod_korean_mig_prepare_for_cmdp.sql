--
--
--Used to alter 'CUSTOMER' and 'REGISTRATION' timestamps so that records are not picked up by CMDP export.
--
--Author David Hay 13th Aug 2012
--
--

use eactest; --UPDATE

declare @splitTarget int = 2;
declare @split int = 0;
declare @hours int = 0;
declare @splitSize int = 300; -- UPDATE
declare @sql nvarchar(2000);  -- UPDATE
declare @createdDate nvarchar(30) = convert(nvarchar,(select max(created_date) from customer_migration),126);


update customer_migration set updated_date = created_date;

while @split < @splitTarget
BEGIN
    set @split = @split + 1;
    set @hours = @split * 24;

    set @sql = 'update CM1 ' +
    ' set CM1.updated_date = DATEADD(HOUR,'+ cast(@hours as nvarchar) +',CM1.created_date) '
    + ' from customer_migration CM1 '
    + ' join customer_migration_data CMD on CMD.id = CM1.id '
    + ' where CM1.id in  '
    + ' (select top ' + cast(@splitSize as nvarchar) + ' id from customer_migration CM2 '
    +   ' where CM2.created_date = CM2.updated_date '
    +   ' and CM2.created_date='''+@createdDate + ''''
    +   ' and CM2.migration_state=''CUSTOMER_REGISTERED'')'
     
    print @sql;
    exec(@sql)

    
END

update C
set C.created_date = CM.updated_date,  C.updated_date = CM.updated_date
from customer_migration CM
join  customer C on CM.eac_customer_id = C.id
join registration R on CM.eac_registration_id = R.id
where CM.migration_state='CUSTOMER_REGISTERED'

update R
set R.created_date = CM.updated_date,  R.updated_date = CM.updated_date
from customer_migration CM
join customer C on CM.eac_customer_id = C.id
join registration R on CM.eac_registration_id = R.id
where CM.migration_state='CUSTOMER_REGISTERED'

select CM.updated_date CM_UPD_DATE,C.created_date C_CRE_DATE, C.updated_date C_UPD_DATE, R.created_date R_CRE_DATE, R.updated_date R_UPD_DATE, count(*)
from customer_migration CM
join customer C on CM.eac_customer_id = C.id
join registration R on CM.eac_registration_id = R.id
where CM.migration_state='CUSTOMER_REGISTERED'
group by CM.updated_date,C.created_date, C.updated_date, R.created_date, R.updated_date;

