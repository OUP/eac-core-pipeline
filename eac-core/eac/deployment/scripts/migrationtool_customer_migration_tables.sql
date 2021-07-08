IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='registration_migration') DROP TABLE registration_migration;
GO

IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='registration_migration_data') DROP TABLE registration_migration_data;
GO


IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration') DROP TABLE customer_migration;
GO

IF EXISTS (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='customer_migration_data') DROP TABLE customer_migration_data;
GO


CREATE TABLE customer_migration (
    id nvarchar(255) NOT NULL,
    obj_version bigint NOT NULL,
    migration_state nvarchar(255) NOT NULL,
    eac_customer_id nvarchar(255),
    created_date datetime2 NOT NULL default getdate(),
    updated_date datetime2,
    primary key (id)
);
GO

    
CREATE TABLE customer_migration_data(
    id nvarchar(255) NOT NULL,
    obj_version bigint NOT NULL,
    username nvarchar(255) NOT NULL,
    password nvarchar(255),
    first_name nvarchar(255),
    last_name nvarchar(255),
    email_address nvarchar(255),
    reset_password bit NOT NULL,
    failed_attempts int NOT NULL,
    locked bit NOT NULL,
    customer_type nvarchar(255),
    enabled bit NOT NULL,
    locale nvarchar(255),
    time_zone nvarchar(255),
    last_login datetime2(7),
    email_verification_state nvarchar(255),
    external_id nvarchar(255),
    column1 nvarchar(255),
    column2 nvarchar(255),
    column3 nvarchar(255),
    column4 nvarchar(255),
    column5 nvarchar(255),
    created_date datetime2(7) NOT NULL default getdate(),
    updated_date datetime2(7),
	PRIMARY KEY CLUSTERED 
	(
	    [id] ASC
	)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
	UNIQUE NONCLUSTERED 
	(
	    [username] ASC
	)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
	) ON [PRIMARY];
GO

    
create index idx_cust_mig_lastlogin on customer_migration_data (last_login);
GO
    
create index idx_cust_mig_state on customer_migration (migration_state);
GO

alter table customer_migration 
    add constraint FK3658F0ADB7A26B54 
    foreign key (eac_customer_id) 
    references customer;
GO
    
alter table customer_migration 
    add constraint FK3658F0ADDDFD8E58 
    foreign key (id) 
    references customer_migration_data;
GO



 create table registration_migration (
        id nvarchar(255) not null,
        obj_version bigint not null,
        migration_state nvarchar(255) not null,
        eac_registration_id nvarchar(255),
        registration_migration_data_id nvarchar(255),
        created_date datetime2 not null default getdate(),
        updated_date datetime2 null,
        customer_migration_id nvarchar(255),
        primary key (id)
    ); 

 create table registration_migration_data (
        id nvarchar(255) not null,
        obj_version bigint not null,
        customer_migration_data_id nvarchar(255) not null,--foreign key
        institution_name nvarchar(255),
        department_name nvarchar(255),
        job_title nvarchar(255),
        marketing nvarchar(255),
        street nvarchar(255),
        address_line_2 nvarchar(255),
        address_line_3 nvarchar(255),
        zip nvarchar(255),
        city nvarchar(255),
        Country_code nvarchar(255),
        telephone nvarchar(255),
        module_title nvarchar(255),
        study_year nvarchar(255),
        number_of_students nvarchar(255),
        text_book nvarchar(255),
        referral nvarchar(255),
        referral_other nvarchar(255),
        old_licence_id nvarchar(255),
        licence_status nvarchar(10) not null,
        licence_start_date datetime,
        licence_end_date datetime,
        product_id nvarchar(255) not null,
        product_name nvarchar(255),
        product_classification nvarchar(255),
        product_classification_parameter nvarchar(255),
        created_date datetime2 not null default getdate(),
        enrolment_year nvarchar(255),
        registration_definition_id nvarchar(255),
    status nvarchar(25),
        primary key (id)
    );
GO
  
alter table registration_migration 
        add constraint FK3658F0ADDDFD8E88 
        foreign key (registration_migration_data_id) 
        references registration_migration_data;
GO    


alter table registration_migration_data 
        add constraint FK3658F0ADDDFD8E98 
        foreign key (customer_migration_data_id) 
        references customer_migration_data;
GO


 alter table registration_migration 
        add constraint FK3658F0ADDDFD8E99 
        foreign key (eac_registration_id) 
        references registration;
GO

 alter table registration_migration 
        add constraint FK3658F0ADDDFD8E97 
        foreign key (customer_migration_id) 
        references customer_migration;
GO


