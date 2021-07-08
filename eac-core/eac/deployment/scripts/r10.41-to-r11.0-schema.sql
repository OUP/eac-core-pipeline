--remove teachers club migration tables

IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration_warning') DROP TABLE customer_migration_warning;
GO

IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration') DROP TABLE customer_migration;
GO

IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration_data') DROP TABLE customer_migration_data;
GO

--remove ORCS customer migration tables
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

--remove ORCS product migration tables
IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='orcsproduct_migration') DROP TABLE orcsproduct_migration;
GO
    
IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='orcsproduct_data') DROP TABLE orcsproduct_data;
GO

-- to allow asset/product lifecycle states
alter table asset add [state] nvarchar(25) not null default 'ACTIVE';

alter table url_skin add primary_site bit not null DEFAULT(0);
GO

alter table message add obj_version numeric(19,0) not null DEFAULT(0);
GO

alter table registration_definition add confirmation_email_enabled bit null;
GO


-- Modifications for COUNTRY_MATCH registration activation - JIRA EAC-263
alter table registration_activation add matched_activation nvarchar(255);
GO

alter table registration_activation add unmatched_activation nvarchar(255);
GO

alter table registration_activation add iso_country_list nvarchar(255);
GO

alter table registration_activation add description nvarchar(100);
GO

create table export_name (
    id nvarchar(255) not null,
    obj_version numeric(19,0) not null,
    created_date datetime2 not null,
    question_id nvarchar(255) not null,
    export_type nvarchar(255) not null,
    name nvarchar(255) null unique,
    primary key (id)
);
GO

EXEC sp_rename 'question.export_name', 'description', 'COLUMN';
GO

alter table division add updated_date datetime2 null;
GO

-- Modifications for Manage Element - JIRA-265
alter table tag alter column empty_option bit null;
GO
alter table tag add constraint def_empty_option default(0) for empty_option
GO