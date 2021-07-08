IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[FK_EXT_ID_TO_EXT_SYS_TYPE_ID]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
BEGIN
   alter table external_identifier
	   drop constraint [FK_EXT_ID_TO_EXT_SYS_TYPE_ID];
END;
	   
	   
IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[FK_EXT_ID_TO_CUSTOMER]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
BEGIN
   alter table external_identifier
	   drop constraint [FK_EXT_ID_TO_CUSTOMER];
END;
    
    
IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[FK_EXT_ID_TO_PRODUCT]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
BEGIN
   alter table external_identifier
	   drop constraint [FK_EXT_ID_TO_PRODUCT];
END;

IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[FK_EXT_SYS_ID_TYPE_TO_EXT_SYS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
BEGIN
   alter table external_system_id_type
	   drop constraint [FK_EXT_SYS_ID_TYPE_TO_EXT_SYS];
END;
       
IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[EXTERNAL_IDENTIFIER]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
BEGIN
   drop table external_identifier;
END;

IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[EXTERNAL_SYSTEM_ID_TYPE]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
BEGIN
   drop table external_system_id_type;
END;
    
IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[EXTERNAL_SYSTEM]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
BEGIN
   drop table external_system;
END;

 
   create table external_identifier (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        erights_id numeric(19,0) null,
        externalid nvarchar(255) null,
        system nvarchar(255) null,
        product_id nvarchar(255) not null,
        primary key (id)
    );
    
    alter table external_identifier 
        add constraint FK641FE19D2B9EA9C8 
        foreign key (product_id) 
        references product
    ;
    
	ALTER TABLE customer drop CONSTRAINT df_customer_enabled;

	alter table customer drop column enabled;
	
	IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[LINKED_REGISTRATION]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
	BEGIN
		drop table linked_registration;
	END;

	alter table registration drop column erights_licence_id;
	
	alter table customer drop column locale;
	
	alter table customer drop column time_zone;