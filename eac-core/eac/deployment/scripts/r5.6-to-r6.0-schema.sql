
create table asset (
    id nvarchar(255) not null,
    obj_version numeric(19,0) not null,
    erights_id int not null unique,
    product_name nvarchar(255) not null,
    division_id nvarchar(255) not null,
    primary key (id)
);

create index asset_erights_id_idx on asset (erights_id);

alter table product drop constraint FKED8DCCEFBDF051EC;

alter table product add asset_id nvarchar(255) null;

alter table product add registerable_type nvarchar(255) null;

alter table product add constraint FK_PRODUCT_TO_ASSET foreign key (asset_id) references asset;

        