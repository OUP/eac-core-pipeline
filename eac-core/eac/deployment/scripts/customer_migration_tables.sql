--
-- TABLES FOR 'KOREAN - Oxford Teachers' Club' DATA MIGRATION - NOT REQUIRED FOR MAIN EAC APPLICATIONS.
--
-- David Hay
--
    
    IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration_warning') DROP TABLE customer_migration_warning;

    IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration') DROP TABLE customer_migration;

    IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration_data') DROP TABLE customer_migration_data;

    create table customer_migration_warning (
        id nvarchar(255) not null,
        obj_version bigint not null,
        cmd_id nvarchar(255) not null,
        message nvarchar(512) not null,
        bad_value nvarchar(255) null,
        created_date datetime2 not null default getdate(),
        primary key (id)
    );
    

    create table customer_migration (
        id nvarchar(255) not null,
        obj_version bigint not null,
        migration_state nvarchar(255) not null,
        eac_customer_id nvarchar(255),
        
        eac_registration_id nvarchar(255),
        created_date datetime2 not null default getdate(),
        updated_date datetime2 null,
        primary key (id)
    );

    create table customer_migration_data (
        id nvarchar(255) not null,
        obj_version bigint not null,
        address_line1 nvarchar(255),
        address_line2 nvarchar(255),
        address_line3 nvarchar(255),
        address_line4 nvarchar(255),
        address_line5 nvarchar(255),
        address_line6 nvarchar(255),
        email nvarchar(255) not null,
        exam_interests nvarchar(255),
        first_name nvarchar(255),
        institution_name nvarchar(255),
        interests nvarchar(255),
        jp_campus nvarchar(255),
        jp_clubs nvarchar(255),
        jp_dept nvarchar(255),
        jp_institution_type nvarchar(255),
        jp_no_students nvarchar(255),
        jp_public_private nvarchar(255),
        last_login datetime,
        last_name nvarchar(255),
        marketing nvarchar(255),
        password nvarchar(255) not null,
        profession nvarchar(255),
        profession_other nvarchar(255),
        referral nvarchar(255),
        teaching_interests nvarchar(255),
        username nvarchar(255) not null,
        teaching_status nvarchar(255) not null,
        created_date datetime2 not null default getdate(),       
        ko_num_students nvarchar(255) null,
        ko_inst_type nvarchar(255) null,
        ko_mobile_num nvarchar(255) null,      
        ko_locale nvarchar(255) null,
        primary key (id)
    );
    
    create index idx_cust_mig_lastlogin on customer_migration_data (last_login);
    create index idx_cust_mig_state on customer_migration (migration_state);

    alter table customer_migration 
        add constraint FK3658F0ADB7A26B54 
        foreign key (eac_customer_id) 
        references customer;

    alter table customer_migration 
        add constraint FK3658F0ADDDFD8E58 
        foreign key (id) 
        references customer_migration_data;

    alter table customer_migration 
        add constraint FK3658F0AD171A3C74 
        foreign key (eac_registration_id)         
        references registration;
        
    ALTER TABLE customer_migration_warning
        ADD  CONSTRAINT FK_cm_id
        FOREIGN KEY  (cmd_id)
        REFERENCES customer_migration_data;

