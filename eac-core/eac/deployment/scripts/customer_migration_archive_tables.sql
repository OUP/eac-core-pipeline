use eactest;
--
-- Author David Hay.
--
-- Only for testing. NOT PRODUCTION.
-- Used to archive a copy the data from the 3 customer_migration tables.
-- This allows the same books_erights_ids to be migrated multiple times ( if they are given different usernames each time ).
    
    IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='arch_customer_migration_warning') DROP TABLE arch_customer_migration_warning;

    IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='arch_customer_migration') DROP TABLE arch_customer_migration;

    IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='arch_customer_migration_data') DROP TABLE arch_customer_migration_data;

    create table arch_customer_migration_warning (
        archive_number int not null,
        cmd_id nvarchar(255) not null,
        obj_version bigint not null,
        
        message nvarchar(512) not null,
        bad_value nvarchar(255) null,
        created_date datetime2 not null default getdate(),
        primary key (archive_number,cmd_id)
    );

    create table arch_customer_migration (
        archive_number int not null,
        id nvarchar(255) not null,
        obj_version bigint not null,
        
        migration_state nvarchar(255) not null,
        eac_customer_id nvarchar(255),
        eac_registration_id nvarchar(255),
        created_date datetime2 not null default getdate(),
        updated_date datetime2 null,
        primary key (archive_number,id)
    );

    create table arch_customer_migration_data (
        archive_number   int not null,
        id nvarchar(255) not null,
        
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
        ko_num_students nvarchar(255),
        ko_inst_type nvarchar(255),
        ko_mobile_num nvarchar(255),
        ko_locale nvarchar(255),
        primary key (archive_number,id)
    );
    
    alter table arch_customer_migration 
        add   
        foreign key (archive_number,id) 
        references arch_customer_migration_data;

    ALTER TABLE arch_customer_migration_warning
        ADD 
        FOREIGN KEY  (archive_number,cmd_id)
        REFERENCES arch_customer_migration_data;

        