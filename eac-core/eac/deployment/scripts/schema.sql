
    alter table activation_code 
        drop constraint FK4CB22376E32A652E;

    alter table activation_code_batch 
        drop constraint FK31A6D31916BD02B;

    alter table activation_code_batch 
        drop constraint FK31A6D31E6EE206B;

    alter table answer 
        drop constraint FKABCA3FBEAC33168D;

    alter table answer 
        drop constraint FKABCA3FBE9770384C;

    alter table answer 
        drop constraint FKABCA3FBED0EA5B4C;

    alter table customer_roles 
        drop constraint FK8704931C8894C385;

    alter table customer_roles 
        drop constraint FK8704931CA63D1FFB;

    alter table division_admin_user 
        drop constraint FKCE14418DF3E32BC7;

    alter table division_admin_user 
        drop constraint FKCE14418DBDF051EC;

    alter table element 
        drop constraint FK9CE31EFCD0EA5B4C;

    alter table element_country_restriction 
        drop constraint FKAA3B6EE094CB52A8;

    alter table external_identifier 
        drop constraint FK641FE19D2B9EA9C8;

    alter table external_identifier 
        drop constraint FK641FE19D9770384C;

    alter table external_identifier 
        drop constraint FK641FE19DDDED5379;

    alter table external_system_id_type 
        drop constraint FK1B7A48C295CBED63;

    alter table field 
        drop constraint FK5CEA0FA4FA82388;

    alter table field 
        drop constraint FK5CEA0FA94CB52A8;

    alter table linked_registration 
        drop constraint FK35031D1FD66C48D5;

    alter table linked_registration 
        drop constraint FK35031D1F90C8CD6C;

    alter table page_component 
        drop constraint FK5B31D34D4FA82388;

    alter table page_component 
        drop constraint FK5B31D34D8866A8EB;

    alter table page_definition 
        drop constraint FKE93D4903BDF051EC;

    alter table product 
        drop constraint FKED8DCCEFBDF051EC;

    alter table product 
        drop constraint FKED8DCCEFAC33168D;

    alter table registration 
        drop constraint FKAF83E8B93D11792A;

    alter table registration 
        drop constraint FKAF83E8B963ED35C2;

    alter table registration 
        drop constraint FKAF83E8B9D1DB3079;

    alter table registration 
        drop constraint FKAF83E8B99770384C;

    alter table registration_definition 
        drop constraint FK224324991E74A5F;

    alter table registration_definition 
        drop constraint FK22432499547DB7D6;

    alter table registration_definition 
        drop constraint FK2243249984987094;

    alter table registration_definition 
        drop constraint FK22432499548AFB8B;

    alter table registration_definition 
        drop constraint FK22432499E6EE206B;

    alter table role_permissions 
        drop constraint FKEAD9D23BD42BCA4C;

    alter table role_permissions 
        drop constraint FKEAD9D23BE61A1A2C;

    alter table tag 
        drop constraint FK1BF9A94CB52A8;

    alter table tag_option 
        drop constraint FK1008E4BA77B2D6CE;

    drop table activation_code;

    drop table activation_code_batch;

    drop table answer;

    drop table component;

    drop table customer;

    drop table customer_roles;

    drop table division;

    drop table division_admin_user;

    drop table element;

    drop table element_country_restriction;

    drop table external_identifier;

    drop table external_system;

    drop table external_system_id_type;

    drop table field;

    drop table licence_template;

    drop table linked_registration;

    drop table page_component;

    drop table page_definition;

    drop table permission;

    drop table product;

    drop table quartz_log_entry;

    drop table question;

    drop table registration;

    drop table registration_activation;

    drop table registration_definition;

    drop table role;

    drop table role_permissions;

    drop table tag;

    drop table tag_option;

    drop table url_skin;
    
    drop table message;
    
    if object_id('message') is not null
	begin
	  drop table message
	end	

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[FK_QRTZ_JOB_LISTENERS_QRTZ_JOB_DETAILS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
    ALTER TABLE [QRTZ_JOB_LISTENERS] DROP CONSTRAINT FK_QRTZ_JOB_LISTENERS_QRTZ_JOB_DETAILS;

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
    ALTER TABLE [QRTZ_TRIGGERS] DROP CONSTRAINT FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS;
    
    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
    ALTER TABLE [QRTZ_CRON_TRIGGERS] DROP CONSTRAINT FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS;

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
    ALTER TABLE [QRTZ_SIMPLE_TRIGGERS] DROP CONSTRAINT FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS;

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[FK_QRTZ_TRIGGER_LISTENERS_QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISFOREIGNKEY') = 1)
    ALTER TABLE [QRTZ_TRIGGER_LISTENERS] DROP CONSTRAINT FK_QRTZ_TRIGGER_LISTENERS_QRTZ_TRIGGERS;

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_CALENDARS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_CALENDARS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_CRON_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_CRON_TRIGGERS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_BLOB_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_BLOB_TRIGGERS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_FIRED_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_FIRED_TRIGGERS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_PAUSED_TRIGGER_GRPS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_PAUSED_TRIGGER_GRPS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_SCHEDULER_STATE]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_SCHEDULER_STATE];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_LOCKS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_LOCKS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_JOB_DETAILS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_JOB_DETAILS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_JOB_LISTENERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_JOB_LISTENERS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_SIMPLE_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_SIMPLE_TRIGGERS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_TRIGGER_LISTENERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_TRIGGER_LISTENERS];

    IF EXISTS (SELECT * FROM sysobjects WHERE id = OBJECT_ID(N'[QRTZ_TRIGGERS]') AND OBJECTPROPERTY(id, N'ISUSERTABLE') = 1)
    DROP TABLE [QRTZ_TRIGGERS];

    create table activation_code (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        actual_usage int null,
        allowed_usage int null,
        code nvarchar(255) null unique,
        activation_code_batch_id nvarchar(255) not null,
        primary key (id)
    );

    create table activation_code_batch (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        created_date datetime2 not null,
        activation_code_format nvarchar(255) null,
        batch_id nvarchar(255) not null unique,
        code_prefix nvarchar(255) null,
        end_date datetime2 null,
        start_date datetime2 null,
        activation_code_registration_definition_id nvarchar(255) null,
        licence_template_id nvarchar(255) null,
        primary key (id)
    );

    create table answer (
        answer_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        created_date datetime2 not null,
        answer_text nvarchar(255) null,
        customer_id nvarchar(255) not null,
        question_id nvarchar(255) not null,
        registerable_product_id nvarchar(255) null,
        primary key (id)
    );

    create table component (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        label_key nvarchar(255) null,
        primary key (id)
    );

    create table customer (
        user_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        created_date datetime2 not null,
        updated_date datetime2 null,
        email_address nvarchar(255) null,
        email_verified bit not null,
        enabled bit not null,
        failed_attempts int not null,
        family_name nvarchar(255) null,
        first_name nvarchar(255) null,
        locale nvarchar(255) null,
        locked bit not null,
        password nvarchar(255) null,
        reset_password bit not null,
        time_zone nvarchar(255) null,
        username nvarchar(255) not null unique,
        customer_type nvarchar(255) null,
        erights_id int null,
        primary key (id)
    );

    create table customer_roles (
        customer_id nvarchar(255) not null,
        role_id nvarchar(255) not null,
        primary key (customer_id, role_id)
    );

    create table division (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        division_type nvarchar(255) null unique,
        primary key (id)
    );

    create table division_admin_user (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        admin_user_id nvarchar(255) null,
        division_id nvarchar(255) null,
        primary key (id)
    );

    create table element (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        help_text nvarchar(255) null,
        regular_expression nvarchar(255) null,
        title_text nvarchar(255) null,
        question_id nvarchar(255) null,
        primary key (id)
    );

    create table element_country_restriction (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        locale nvarchar(255) null,
        element_id nvarchar(255) not null,
        primary key (id)
    );

    create table external_identifier (
        external_id_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        external_id nvarchar(255) not null,
        external_system_id_type_id nvarchar(255) null,
        customer_id nvarchar(255) null,
        product_id nvarchar(255) null,
        primary key (id)
    );

    create table external_system (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        description nvarchar(255) null,
        name nvarchar(255) null,
        primary key (id)
    );

    create table external_system_id_type (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        description nvarchar(255) null,
        name nvarchar(255) null,
        external_system_id nvarchar(255) not null,
        primary key (id)
    );

    create table field (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        default_value nvarchar(255) null,
        required bit not null,
        sequence int not null,
        component_id nvarchar(255) not null,
        element_id nvarchar(255) not null,
        primary key (id),
        unique (component_id, element_id)
    );

    create table licence_template (
        licence_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        end_date date null,
        start_date date null,
        total_concurrency int null,
        user_concurrency int null,
        begin_on nvarchar(255) null,
        time_period int null,
        unit_type nvarchar(255) null,
        allowed_usages int null,
        primary key (id)
    );

    create table linked_registration (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        erights_licence_id int not null,
        linked_product_id nvarchar(255) not null,
        registration_id nvarchar(255) not null,
        primary key (id)
    );

    create table page_component (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        sequence int not null,
        component_id nvarchar(255) not null,
        page_definition_id nvarchar(255) not null,
        primary key (id),
        unique (page_definition_id, component_id)
    );

    create table page_definition (
        page_definition_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        name nvarchar(255) null,
        title nvarchar(255) null,
        division_id nvarchar(255) not null,
        primary key (id)
    );

    create table permission (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        name nvarchar(255) null unique,
        primary key (id)
    );

    create table product (
        product_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        email nvarchar(255) null,
        erights_id int null,
        home_page nvarchar(1000) null,
        landing_page nvarchar(1000) null,
        product_name nvarchar(255) null,
        service_level_agreement nvarchar(255) null,
        activation_method nvarchar(255) null,
        division_id nvarchar(255) not null,
        registerable_product_id nvarchar(255) null,
        primary key (id)
    );

    CREATE TABLE [QRTZ_CALENDARS] (
        [CALENDAR_NAME] [VARCHAR] (200)  NOT NULL ,
        [CALENDAR] [IMAGE] NOT NULL
    );

    CREATE TABLE [QRTZ_CRON_TRIGGERS] (
        [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
        [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
        [CRON_EXPRESSION] [VARCHAR] (120)  NOT NULL ,
        [TIME_ZONE_ID] [VARCHAR] (80) 
    );

    CREATE TABLE [QRTZ_FIRED_TRIGGERS] (
        [ENTRY_ID] [VARCHAR] (95)  NOT NULL ,
        [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
        [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
        [IS_VOLATILE] [VARCHAR] (1)  NOT NULL ,
        [INSTANCE_NAME] [VARCHAR] (200)  NOT NULL ,
        [FIRED_TIME] [BIGINT] NOT NULL ,
        [PRIORITY] [INTEGER] NOT NULL ,
        [STATE] [VARCHAR] (16)  NOT NULL,
        [JOB_NAME] [VARCHAR] (200)  NULL ,
        [JOB_GROUP] [VARCHAR] (200)  NULL ,
        [IS_STATEFUL] [VARCHAR] (1)  NULL ,
        [REQUESTS_RECOVERY] [VARCHAR] (1)  NULL 
    );

    CREATE TABLE [QRTZ_PAUSED_TRIGGER_GRPS] (
        [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL 
    );

    CREATE TABLE [QRTZ_SCHEDULER_STATE] (
        [INSTANCE_NAME] [VARCHAR] (200)  NOT NULL ,
        [LAST_CHECKIN_TIME] [BIGINT] NOT NULL ,
        [CHECKIN_INTERVAL] [BIGINT] NOT NULL
    );

    CREATE TABLE [QRTZ_LOCKS] (
        [LOCK_NAME] [VARCHAR] (40)  NOT NULL 
    );

    CREATE TABLE [QRTZ_JOB_DETAILS] (
        [JOB_NAME] [VARCHAR] (200)  NOT NULL ,
        [JOB_GROUP] [VARCHAR] (200)  NOT NULL ,
        [DESCRIPTION] [VARCHAR] (250) NULL ,
        [JOB_CLASS_NAME] [VARCHAR] (250)  NOT NULL ,
        [IS_DURABLE] [VARCHAR] (1)  NOT NULL ,
        [IS_VOLATILE] [VARCHAR] (1)  NOT NULL ,
        [IS_STATEFUL] [VARCHAR] (1)  NOT NULL ,
        [REQUESTS_RECOVERY] [VARCHAR] (1)  NOT NULL ,
        [JOB_DATA] [IMAGE] NULL
    );

    CREATE TABLE [QRTZ_JOB_LISTENERS] (
        [JOB_NAME] [VARCHAR] (200)  NOT NULL ,
        [JOB_GROUP] [VARCHAR] (200)  NOT NULL ,
        [JOB_LISTENER] [VARCHAR] (200)  NOT NULL
    );

    CREATE TABLE [QRTZ_SIMPLE_TRIGGERS] (
        [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
        [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
        [REPEAT_COUNT] [BIGINT] NOT NULL ,
        [REPEAT_INTERVAL] [BIGINT] NOT NULL ,
        [TIMES_TRIGGERED] [BIGINT] NOT NULL
    );

    CREATE TABLE [QRTZ_BLOB_TRIGGERS] (
        [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
        [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
        [BLOB_DATA] [IMAGE] NULL
    );

    CREATE TABLE [QRTZ_TRIGGER_LISTENERS] (
        [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
        [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
        [TRIGGER_LISTENER] [VARCHAR] (200)  NOT NULL
    );

    CREATE TABLE [QRTZ_TRIGGERS] (
        [TRIGGER_NAME] [VARCHAR] (200)  NOT NULL ,
        [TRIGGER_GROUP] [VARCHAR] (200)  NOT NULL ,
        [JOB_NAME] [VARCHAR] (200)  NOT NULL ,
        [JOB_GROUP] [VARCHAR] (200)  NOT NULL ,
        [IS_VOLATILE] [VARCHAR] (1)  NOT NULL ,
        [DESCRIPTION] [VARCHAR] (250) NULL ,
        [NEXT_FIRE_TIME] [BIGINT] NULL ,
        [PREV_FIRE_TIME] [BIGINT] NULL ,
        [PRIORITY] [INTEGER] NULL ,
        [TRIGGER_STATE] [VARCHAR] (16)  NOT NULL ,
        [TRIGGER_TYPE] [VARCHAR] (8)  NOT NULL ,
        [START_TIME] [BIGINT] NOT NULL ,
        [END_TIME] [BIGINT] NULL ,
        [CALENDAR_NAME] [VARCHAR] (200)  NULL ,
        [MISFIRE_INSTR] [SMALLINT] NULL ,
        [JOB_DATA] [IMAGE] NULL
    );

    create table quartz_log_entry (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        act_fire_time datetime2 null,
        host_address nvarchar(255) null,
        host_name nvarchar(255) null,
        job_class_name nvarchar(255) null,
        job_group nvarchar(255) null,
        job_name nvarchar(255) null,
        job_run_time numeric(19,0) null,
        nxt_fire_time datetime2 null,
        refire_count int null,
        sch_fire_time datetime2 null,
        trg_group nvarchar(255) null,
        trg_instr_code int null,
        trg_name nvarchar(255) null,
        primary key (id)
    );

    create table question (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        element_text nvarchar(255) null,
        export_name nvarchar(255) null,
        product_specific bit not null,
        primary key (id)
    );

    create table registration (
        registration_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        created_date datetime2 not null,
        updated_date datetime2 null,
        activated bit not null,
        awaiting_validation bit not null,
        completed bit not null,
        denied bit not null,
        erights_licence_id int null,
        customer_id nvarchar(255) not null,
        activation_code_id nvarchar(255) null,
        registration_definition_id nvarchar(255) not null,
        primary key (id)
    );

    create table registration_activation (
        activation_type nvarchar(50) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        validator_email nvarchar(255) null,
        primary key (id)
    );

    create table registration_definition (
        registration_definition_type nvarchar(50) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        product_id nvarchar(255) null,
        registration_activation_id nvarchar(255) null,
        page_definition_id nvarchar(255) null,
        licence_template_id nvarchar(255) null,
        primary key (id),
        unique (product_id, registration_definition_type)
    );

    create table role (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        name nvarchar(255) null unique,
        primary key (id)
    );

    create table role_permissions (
        role_id nvarchar(255) not null,
        permission_id nvarchar(255) not null,
        primary key (role_id, permission_id)
    );

    create table tag (
        tag_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        value bit null,
        empty_option bit not null,
        new_window bit null,
        url nvarchar(255) null,
        element_id nvarchar(255) not null,
        primary key (id)
    );

    create table tag_option (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        label nvarchar(255) null,
        sequence int not null,
        value nvarchar(255) null,
        tag_id nvarchar(255) not null,
        primary key (id)
    );

    create table url_skin (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        contact_path nvarchar(255) null,
        site_name nvarchar(255) null,
        skin_path nvarchar(255) not null,
        url nvarchar(255) not null unique,
        primary key (id)
    );
    
    create table message (
        id nvarchar(255) not null,
        basename nvarchar(31) not null ,
        language nvarchar(7) null,
        country nvarchar(7) null,
        variant nvarchar(7) null,
        [key] nvarchar(255) null,
        message TEXT,
        primary key (id),
        unique (basename,language,country,variant,[key])
     );

    create index message_idx on message (basename,language,country,variant,[key]);
	
    create index activation_code_idx on activation_code (code);

    alter table activation_code 
        add constraint FK4CB22376E32A652E 
        foreign key (activation_code_batch_id) 
        references activation_code_batch;

    alter table activation_code_batch 
        add constraint FK31A6D31916BD02B 
        foreign key (activation_code_registration_definition_id) 
        references registration_definition;

    alter table activation_code_batch 
        add constraint FK31A6D31E6EE206B 
        foreign key (licence_template_id) 
        references licence_template;

    create index answer_customer_question_idx on answer (customer_id, question_id);

    create index answer_question_idx on answer (question_id);

    alter table answer 
        add constraint FKABCA3FBEAC33168D 
        foreign key (registerable_product_id) 
        references product;

    alter table answer 
        add constraint FKABCA3FBE9770384C 
        foreign key (customer_id) 
        references customer;

    alter table answer 
        add constraint FKABCA3FBED0EA5B4C 
        foreign key (question_id) 
        references question;

    create index customer_username_idx on customer (username);

    create index customer_email_idx on customer (email_address);

    create index customer_erights_id_idx on customer (erights_id);

    alter table customer_roles 
        add constraint FK8704931C8894C385 
        foreign key (roles) 
        references role;

    alter table customer_roles 
        add constraint FK8704931CA63D1FFB 
        foreign key (customer) 
        references customer;

    alter table division_admin_user 
        add constraint FKCE14418DF3E32BC7 
        foreign key (admin_user_id) 
        references customer;

    alter table division_admin_user 
        add constraint FKCE14418DBDF051EC 
        foreign key (division_id) 
        references division;

    alter table element 
        add constraint FK9CE31EFCD0EA5B4C 
        foreign key (question_id) 
        references question;

    alter table element_country_restriction 
        add constraint FKAA3B6EE094CB52A8 
        foreign key (element_id) 
        references element;

    alter table external_identifier 
        add constraint FK641FE19D2B9EA9C8 
        foreign key (product_id) 
        references product;

    alter table external_identifier 
        add constraint FK641FE19D9770384C 
        foreign key (customer_id) 
        references customer;

    alter table external_identifier 
        add constraint FK641FE19DDDED5379 
        foreign key (external_system_id_type_id) 
        references external_system_id_type;

    alter table external_system_id_type 
        add constraint FK1B7A48C295CBED63 
        foreign key (external_system_id) 
        references external_system;

    create index field_component_element_idx on field (component_id, element_id);

    create index field_sequence_idx on field (sequence);

    alter table field 
        add constraint FK5CEA0FA4FA82388 
        foreign key (component_id) 
        references component;

    alter table field 
        add constraint FK5CEA0FA94CB52A8 
        foreign key (element_id) 
        references element;

    create index linked_registration_idx on linked_registration (registration_id);

    alter table linked_registration 
        add constraint FK35031D1FD66C48D5 
        foreign key (linked_product_id) 
        references product;

    alter table linked_registration 
        add constraint FK35031D1F90C8CD6C 
        foreign key (registration_id) 
        references registration;

    create index page_component_sequence_idx on page_component (sequence);

    create index page_component_definition_idx on page_component (component_id, page_definition_id);

    alter table page_component 
        add constraint FK5B31D34D4FA82388 
        foreign key (component_id) 
        references component;

    alter table page_component 
        add constraint FK5B31D34D8866A8EB 
        foreign key (page_definition_id) 
        references page_definition;

    create index page_definition_idx on page_definition (division_id, page_definition_type);

    alter table page_definition 
        add constraint FKE93D4903BDF051EC 
        foreign key (division_id) 
        references division;

    create index erights_id_idx on product (erights_id);

    alter table product 
        add constraint FKED8DCCEFBDF051EC 
        foreign key (division_id) 
        references division;

    alter table product 
        add constraint FKED8DCCEFAC33168D 
        foreign key (registerable_product_id) 
        references product;

    ALTER TABLE [QRTZ_CALENDARS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_CALENDARS] PRIMARY KEY  CLUSTERED
    (
        [CALENDAR_NAME]
    );

    ALTER TABLE [QRTZ_CRON_TRIGGERS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_CRON_TRIGGERS] PRIMARY KEY  CLUSTERED
    (
        [TRIGGER_NAME],
        [TRIGGER_GROUP]
    );

    ALTER TABLE [QRTZ_FIRED_TRIGGERS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_FIRED_TRIGGERS] PRIMARY KEY  CLUSTERED
    (
        [ENTRY_ID]
    );

    ALTER TABLE [QRTZ_PAUSED_TRIGGER_GRPS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_PAUSED_TRIGGER_GRPS] PRIMARY KEY  CLUSTERED
    (
        [TRIGGER_GROUP]
    );

    ALTER TABLE [QRTZ_SCHEDULER_STATE] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_SCHEDULER_STATE] PRIMARY KEY  CLUSTERED
    (
        [INSTANCE_NAME]
    );

    ALTER TABLE [QRTZ_LOCKS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_LOCKS] PRIMARY KEY  CLUSTERED
    (
        [LOCK_NAME]
    );

    ALTER TABLE [QRTZ_JOB_DETAILS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_JOB_DETAILS] PRIMARY KEY  CLUSTERED
    (
        [JOB_NAME],
        [JOB_GROUP]
    );

    ALTER TABLE [QRTZ_JOB_LISTENERS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_JOB_LISTENERS] PRIMARY KEY  CLUSTERED
    (
        [JOB_NAME],
        [JOB_GROUP],
        [JOB_LISTENER]
    );

    ALTER TABLE [QRTZ_SIMPLE_TRIGGERS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_SIMPLE_TRIGGERS] PRIMARY KEY  CLUSTERED
    (
        [TRIGGER_NAME],
        [TRIGGER_GROUP]
    );

    ALTER TABLE [QRTZ_TRIGGER_LISTENERS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_TRIGGER_LISTENERS] PRIMARY KEY  CLUSTERED
    (
        [TRIGGER_NAME],
        [TRIGGER_GROUP],
        [TRIGGER_LISTENER]
    );

    ALTER TABLE [QRTZ_TRIGGERS] WITH NOCHECK ADD
        CONSTRAINT [PK_QRTZ_TRIGGERS] PRIMARY KEY  CLUSTERED
    (
        [TRIGGER_NAME],
        [TRIGGER_GROUP]
    );

    ALTER TABLE [QRTZ_CRON_TRIGGERS] ADD
        CONSTRAINT [FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS] FOREIGN KEY
    (
        [TRIGGER_NAME],
        [TRIGGER_GROUP]
    ) REFERENCES [QRTZ_TRIGGERS] (
        [TRIGGER_NAME],
        [TRIGGER_GROUP]
    ) ON DELETE CASCADE;

    ALTER TABLE [QRTZ_JOB_LISTENERS] ADD
        CONSTRAINT [FK_QRTZ_JOB_LISTENERS_QRTZ_JOB_DETAILS] FOREIGN KEY
    (
        [JOB_NAME],
        [JOB_GROUP]
    ) REFERENCES [QRTZ_JOB_DETAILS] (
        [JOB_NAME],
        [JOB_GROUP]
    ) ON DELETE CASCADE;

    ALTER TABLE [QRTZ_SIMPLE_TRIGGERS] ADD
        CONSTRAINT [FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS] FOREIGN KEY
    (
        [TRIGGER_NAME],
        [TRIGGER_GROUP]
    ) REFERENCES [QRTZ_TRIGGERS] (
        [TRIGGER_NAME],
        [TRIGGER_GROUP]
    ) ON DELETE CASCADE;

    ALTER TABLE [QRTZ_TRIGGER_LISTENERS] ADD
        CONSTRAINT [FK_QRTZ_TRIGGER_LISTENERS_QRTZ_TRIGGERS] FOREIGN KEY
    (
        [TRIGGER_NAME],
        [TRIGGER_GROUP]
    ) REFERENCES [QRTZ_TRIGGERS] (
        [TRIGGER_NAME],
        [TRIGGER_GROUP]
    ) ON DELETE CASCADE;

    ALTER TABLE [QRTZ_TRIGGERS] ADD
        CONSTRAINT [FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS] FOREIGN KEY
    (
        [JOB_NAME],
        [JOB_GROUP]
    ) REFERENCES [QRTZ_JOB_DETAILS] (
        [JOB_NAME],
        [JOB_GROUP]
    );

    create index registration_idx on registration (customer_id, registration_definition_id);
    
    create index reg_by_date_and_owner_idx on registration (created_date, customer_id);

    alter table registration 
        add constraint FKAF83E8B93D11792A 
        foreign key (registration_definition_id) 
        references registration_definition;

    alter table registration 
        add constraint FKAF83E8B963ED35C2 
        foreign key (registration_definition_id) 
        references registration_definition;

    alter table registration 
        add constraint FKAF83E8B9D1DB3079 
        foreign key (activation_code_id) 
        references activation_code;

    alter table registration 
        add constraint FKAF83E8B99770384C 
        foreign key (customer_id) 
        references customer;

    alter table registration_definition 
        add constraint FK224324991E74A5F 
        foreign key (registration_activation_id) 
        references registration_activation;

    alter table registration_definition 
        add constraint FK22432499547DB7D6 
        foreign key (page_definition_id) 
        references page_definition;

    alter table registration_definition 
        add constraint FK2243249984987094 
        foreign key (page_definition_id) 
        references page_definition;

    alter table registration_definition 
        add constraint FK22432499548AFB8B 
        foreign key (product_id) 
        references product;

    alter table registration_definition 
        add constraint FK22432499E6EE206B 
        foreign key (licence_template_id) 
        references licence_template;

    alter table role_permissions 
        add constraint FKEAD9D23B824FB53E 
        foreign key (role) 
        references role;

    alter table role_permissions 
        add constraint FKEAD9D23B7F056925 
        foreign key (permissions) 
        references permission;

    create index tag_element_idx on tag (element_id);

    alter table tag 
        add constraint FK1BF9A94CB52A8 
        foreign key (element_id) 
        references element;

    create index tag_option_sequence_idx on tag_option (sequence);

    create index tag_option_options_tag_idx on tag_option (tag_id);

    alter table tag_option 
        add constraint FK1008E4BA77B2D6CE 
        foreign key (tag_id) 
        references tag;

    create index url_skin_idx on url_skin (url);
