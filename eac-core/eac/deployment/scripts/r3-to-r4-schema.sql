
	alter table external_identifier 
        drop constraint FK641FE19D2B9EA9C8;
        
	drop table external_identifier;


    create table external_system (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        name nvarchar(255) not null,
        description nvarchar(255) null,
        primary key (id)
    );

    create table external_system_id_type (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        name nvarchar(255) not null,
        description nvarchar(255) null,
        external_system_id nvarchar(255) not null,
        primary key (id)
    );

    alter table external_system_id_type    
        add constraint [FK_EXT_SYS_ID_TYPE_TO_EXT_SYS]
        foreign key (external_system_id) 
        references external_system;

   create table external_identifier (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        external_system_id_type_id nvarchar(255) not null,
        external_id nvarchar(255) not null,
        external_id_type nvarchar(255) not null,
        customer_id nvarchar(255) null,
        product_id nvarchar(255) null,
        primary key (id)
    );
  
    alter table external_identifier   
	    add constraint [FK_EXT_ID_TO_EXT_SYS_TYPE_ID]
        foreign key (external_system_id_type_id)
        references external_system_id_type;
    
    alter table external_identifier
		add constraint [FK_EXT_ID_TO_CUSTOMER]
        foreign key (customer_id)
        references customer;
    
    alter table external_identifier
	    add constraint [FK_EXT_ID_TO_PRODUCT]
        foreign key (product_id)
        references product;    

	alter table customer add enabled bit not null constraint df_customer_enabled default 1;
	
	alter table registration add erights_licence_id int null ;

    create table linked_registration 
    (
		id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
		registration_id nvarchar(255) not null,
		linked_product_id nvarchar(255) not null,
		erights_licence_id int not null,	
    );

    alter table linked_registration 
        add constraint FKLINKDREGTOREG 
        foreign key (registration_id) 
        references registration;
        
    alter table linked_registration 
        add constraint FKLINKDREGTOPROD 
        foreign key (linked_product_id) 
        references product;
        
	alter table customer add locale nvarchar(255) null;

	alter table customer add time_zone nvarchar(255) null;