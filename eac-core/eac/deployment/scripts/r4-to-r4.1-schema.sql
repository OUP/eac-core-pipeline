exec sp_rename 'FK_EXT_SYS_ID_TYPE_TO_EXT_SYS', 'FK1B7A48C295CBED63', 'OBJECT';
exec sp_rename 'FK_EXT_ID_TO_EXT_SYS_TYPE_ID', 'FK641FE19DDDED5379', 'OBJECT';
exec sp_rename 'FK_EXT_ID_TO_CUSTOMER', 'FK641FE19D9770384C', 'OBJECT';
exec sp_rename 'FK_EXT_ID_TO_PRODUCT', 'FK641FE19D2B9EA9C8', 'OBJECT';

exec sp_rename 'FKLINKDREGTOREG', 'FK35031D1F90C8CD6C', 'OBJECT';
exec sp_rename 'FKLINKDREGTOPROD', 'FK35031D1FD66C48D5', 'OBJECT';

alter table customer drop constraint df_customer_enabled;
	
create index tag_option_sequence_idx on tag_option (sequence);

exec sp_rename 'page_component.page_component_sequence_idx', 'page_component_definition_idx', 'INDEX';

create index page_component_sequence_idx on page_component (sequence);

exec sp_rename 'field.field_sequence_idx', 'field_component_element_idx', 'INDEX';

create index field_sequence_idx on field (sequence);

alter table linked_registration add primary key (id);

create index linked_registration_idx on linked_registration (registration_id);

drop index answer.answer_idx;

create index answer_customer_field_idx on answer(customer_id, field_id);

drop table customer_organisation;

drop table organisation;

declare @conName nvarchar(32), @conSql nvarchar(1000)

-- find constraint name
select @conName = O.name 
from sysobjects AS O
left join sysobjects AS T
    on O.parent_obj = T.id
where isnull(objectproperty(O.id,'IsMSShipped'),1) = 0
    and O.name not like '%dtproper%'
    and O.name not like 'dt[_]%'
    and T.name = 'customer'
    and O.name like 'DF__customer__crea%'

-- delete if found
if not @conName is null
begin
    select @conSql = 'ALTER TABLE [customer] DROP CONSTRAINT [' + @conName + ']'
    execute sp_executesql @conSql
end
	

