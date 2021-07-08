 
  IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='orcs_registration_migration_warning') DROP TABLE orcs_registration_migration_warning;
  GO
    
  IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='orcs_registration_migration') DROP TABLE orcs_registration_migration;
  GO

  IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='orcs_customer_migration') DROP TABLE orcs_customer_migration;
  GO

  IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='orcs_registration_migration_data') DROP TABLE orcs_registration_migration_data;
  GO

  IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='orcs_customer_migration_data') DROP TABLE orcs_customer_migration_data;
  GO


    create table orcs_registration_migration_warning (
        id nvarchar(255) not null,
        obj_version bigint not null,
        orcs_registration_migration_data_id nvarchar(255) not null,
        message nvarchar(512) not null,
        bad_value nvarchar(255) null,
        created_date datetime2 not null default getdate(),
        primary key (id)
    );
  GO

    create table orcs_customer_migration (
        id nvarchar(255) not null,
        obj_version bigint not null,
        migration_state nvarchar(255) not null,
        eac_customer_id nvarchar(255),
        created_date datetime2 not null default getdate(),
        updated_date datetime2 null,
        primary key (id)
    );
  GO

    create table orcs_customer_migration_data (
        id nvarchar(255) not null,
        obj_version bigint not null,
        username nvarchar(255) not null,--from 
        email nvarchar(255) not null,
        first_name nvarchar(255),
        last_name nvarchar(255),
        password nvarchar(255) not null,
        last_login datetime,
        created_date datetime2 not null default getdate(),
        suspended bit not null,
        suspended_on datetime,        
        user_status nvarchar(255) not null,
        user_id nvarchar(255),
        shared bit not null,
        primary key (id)
    );
  GO
    
    create table orcs_registration_migration (
        id nvarchar(255) not null,
        orcs_registration_migration_data_id nvarchar(255) not null,
        orcs_customer_migration_id nvarchar(255) not null,
        obj_version bigint not null,
        migration_state nvarchar(255) not null,
        eac_registration_id nvarchar(255),
        created_date datetime2 not null default getdate(),
        updated_date datetime2 null,
        primary key (id)
    );
  GO

    create table orcs_registration_migration_data (
        id nvarchar(255) not null,
        obj_version bigint not null,
        orcs_customer_migration_data_id nvarchar(255) not null,--foreign key
        
        orcs_product_id nvarchar(255) not null,
        
        orcs_product_name nvarchar(255),
        institution_name nvarchar(255),
        department_name nvarchar(255),
        job_title nvarchar(255),
        marketing nvarchar(1),
        address_line_1 nvarchar(255),
        address_line_2 nvarchar(255),
        address_line_3 nvarchar(255),
        address_line_4 nvarchar(255),
        address_line_5 nvarchar(255),
        address_line_6 nvarchar(255),
        phone_number nvarchar(255),
        module_title nvarchar(255),
        education_level nvarchar(255),
        number_of_students nvarchar(255),
        referral nvarchar(255),
        previous_text_book nvarchar(255),
        created_date datetime2 not null default getdate(),
        enrolment_year nvarchar(255),
        licence_enabled bit not null,
        licence_start_date datetime,
        licence_end_date datetime,
        old_licence_id nvarchar(255),
        source_file_desc nvarchar(255),
        status nvarchar(255),
        registration_definition_id nvarchar(255),
        primary key (id)
    );
  GO
    


    
  create index idx_orcs_cust_mig_lastlogin on orcs_customer_migration_data (last_login);
  GO
  create index idx_orcs_cust_mig_state on orcs_customer_migration (migration_state);
  GO
  create index idx_orcs_reg_mig_state on orcs_registration_migration (migration_state);
  GO
  
--0
    alter table orcs_registration_migration_data
        add constraint FK_ORMD_TO_PRD
        foreign key (registration_definition_id)
        references registration_definition;
    GO
 
    --1
    alter table orcs_registration_migration_data
        add constraint FK_ORMD_TO_OCMD
        foreign key (orcs_customer_migration_data_id)
        references orcs_customer_migration_data;
 GO

    --2
    alter table orcs_customer_migration
        add constraint FK_OCM_TO_OCMD
        foreign key (id)
        references orcs_customer_migration_data;
 GO

    --3
    alter table orcs_registration_migration
        add constraint FK_ORM_TO_ORMD
        foreign key (orcs_registration_migration_data_id)
        references orcs_registration_migration_data;
 GO

    --4
    alter table orcs_registration_migration
        add constraint FK_ORM_TO_OCM
        foreign key (orcs_customer_migration_id)
        references orcs_customer_migration;
GO

    --5
    alter table orcs_customer_migration
        add constraint FK_OCM_TO_CUSTOMER
        foreign key (eac_customer_id)
        references customer;
GO

    --6
    alter table orcs_registration_migration
        add constraint FK_ORM_TO_REGISTRATION
        foreign key (eac_registration_id)     
        references registration;
GO

    --7
    alter table orcs_registration_migration_warning
        add constraint FK_ORMW_TO_ORMD
        foreign key  (orcs_registration_migration_data_id)
        references orcs_registration_migration_data;
GO

alter table orcs_registration_migration_data add referral_other varchar(255)
GO
