-- ORCS

create table orcsproduct_data (
    id nvarchar(255) not null,
    obj_version numeric(19,0) not null,
    product_name nvarchar(500) not null,
    erights_id int null,
    registerable_type nvarchar(500) not null,
    asset_path nvarchar(1000) not null,
    home_page nvarchar(1000) null,
    landing_page nvarchar(1000) null,
    parent_child_type nvarchar(500) null,
    child_erights_id int null,
    external_id nvarchar(255) null,
    primary key (id),
    unique (product_name, parent_child_type)
);
GO

create table orcsproduct_migration (
    id nvarchar(255) not null,
    obj_version numeric(19,0) not null,
    orcsproduct_data_id nvarchar(255) not null,
    product_migration_state nvarchar(255) not null,
    student_orcsproduct_migration_id nvarchar(255) null,
    erights_id int null,
    asset_id nvarchar(255) null,
    product_id nvarchar(255) null,
    account_registration_definition_id nvarchar(255) null,
    product_registration_definition_id nvarchar(255) null,
    external_product_id nvarchar(255) null,
    primary key (id)
);
GO

alter table orcsproduct_migration 
    add constraint fk_orcsproduct_data
    foreign key (orcsproduct_data_id) 
    references orcsproduct_data;
GO

alter table orcsproduct_data add asset_path_fds nvarchar(1000) null;
GO