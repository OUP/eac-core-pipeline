

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
        activation_code_format nvarchar(255) null,
        batch_id nvarchar(255) not null,
        code_prefix nvarchar(255) null,
        created_date datetime2 null,
        start_date datetime2 null,
        end_date datetime2 null,
        activation_code_registration_definition_id nvarchar(255) null,
        licence_template_id nvarchar(255) null,
        primary key (id)
    );

    create table answer (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        answer_text nvarchar(255) null,
        created_date datetime2 not null,
        customer_id nvarchar(255) not null,
        field_id nvarchar(255) not null,
        primary key (id),
        unique (field_id, customer_id)
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
        email_address nvarchar(255) null,
        email_verified bit not null,
        failed_attempts int not null,
        family_name nvarchar(255) null,
        first_name nvarchar(255) null,
        locked bit not null,
        password nvarchar(255) null,
        reset_password bit not null,
        username nvarchar(255) not null unique,
        customer_type nvarchar(255) null,
        erights_id int null,
        primary key (id)
    );

    create table customer_activation_code (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        activated_date datetime2 null,
        activation_code_id nvarchar(255) not null,
        customer_id nvarchar(255) not null,
        primary key (id)
    );

    create table customer_organisation (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        customer_id nvarchar(255) not null,
        organisation_id nvarchar(255) not null,
        primary key (id)
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
        element_text nvarchar(255) null,
        help_text nvarchar(255) null,
        regular_expression nvarchar(255) null,
        title_text nvarchar(255) null,
        primary key (id)
    );

    create table external_identifier (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        erights_id numeric(19,0) null,
        externalid nvarchar(255) null,
        system nvarchar(255) null,
        product_id nvarchar(255) not null,
        primary key (id)
    );

    create table field (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        sequence int not null,
        component_id nvarchar(255) not null,
        element_id nvarchar(255) not null,
        default_value nvarchar(255) null,
        export_name nvarchar(255) null,
        required bit not null,
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

    create table organisation (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        parent_id nvarchar(255) null,
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

    create table product (
        product_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        erights_id int null,
        landing_page nvarchar(100) null,
        product_name nvarchar(255) null,
        division_id nvarchar(255) not null,
        registerable_product_id nvarchar(255) null,
        email nvarchar(255) null,
        service_level_agreement nvarchar(255) null,
        activation_method nvarchar(31) null,
        primary key (id)
    );

    create table product_skin (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        css_url nvarchar(255) null,
        product_url nvarchar(255) null,
        primary key (id)
    );

    create table registration (
        registration_type nvarchar(31) not null,
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        activated bit not null,
        awaiting_validation bit not null,
        completed bit not null,
        created_date datetime2 not null,
        denied bit not null,
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

    create index answer_element_idx on answer (field_id);

    create index answer_idx on answer (field_id, customer_id);

    alter table answer 
        add constraint FKABCA3FBE9770384C 
        foreign key (customer_id) 
        references customer;

    alter table answer 
        add constraint FKABCA3FBE8920ED68 
        foreign key (field_id) 
        references field;

    create index customer_username_idx on customer (username);

    create index customer_email_idx on customer (email_address);

    create index customer_erights_id_idx on customer (erights_id);

    alter table customer_activation_code 
        add constraint FKFC657D15D1DB3079 
        foreign key (activation_code_id) 
        references activation_code;

    alter table customer_activation_code 
        add constraint FKFC657D159770384C 
        foreign key (customer_id) 
        references customer;

    alter table customer_organisation 
        add constraint FKFBCC239B7560CDCC 
        foreign key (organisation_id) 
        references organisation;

    alter table customer_organisation 
        add constraint FKFBCC239B9770384C 
        foreign key (customer_id) 
        references customer;

    alter table division_admin_user 
        add constraint FKCE14418DF3E32BC7 
        foreign key (admin_user_id) 
        references customer;

    alter table division_admin_user 
        add constraint FKCE14418DBDF051EC 
        foreign key (division_id) 
        references division;

    alter table external_identifier 
        add constraint FK641FE19D2B9EA9C8 
        foreign key (product_id) 
        references product;

    create index field_sequence_idx on field (component_id, element_id);

    alter table field 
        add constraint FK5CEA0FA4FA82388 
        foreign key (component_id) 
        references component;

    alter table field 
        add constraint FK5CEA0FA94CB52A8 
        foreign key (element_id) 
        references element;

    alter table organisation 
        add constraint FK3A5300DAAF95F3FC 
        foreign key (parent_id) 
        references organisation;

    create index page_component_sequence_idx on page_component (component_id, page_definition_id);

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

    create index tag_element_idx on tag (element_id);

    alter table tag 
        add constraint FK1BF9A94CB52A8 
        foreign key (element_id) 
        references element;

    create index tag_option_options_tag_idx on tag_option (tag_id);

    alter table tag_option 
        add constraint FK1008E4BA77B2D6CE 
        foreign key (tag_id) 
        references tag;
