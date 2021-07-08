alter table registration add updated_date datetime2 null;
alter table customer add created_date datetime2 not null default getdate();
alter table customer add updated_date datetime2 null;
	 
    alter table customer_activation_code 
        drop constraint FKFC657D15D1DB3079;

    alter table customer_activation_code 
        drop constraint FKFC657D159770384C;
      
	drop table customer_activation_code;

