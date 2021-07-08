alter table registration drop column updated_date;
alter table customer drop column created_date;
alter table customer drop column updated_date;

create table customer_activation_code (
    id nvarchar(255) not null,
    obj_version numeric(19,0) not null,
    activated_date datetime2 null,
    activation_code_id nvarchar(255) not null,
    customer_id nvarchar(255) not null,
    primary key (id)
);
    
alter table customer_activation_code 
    add constraint FKFC657D15D1DB3079 
    foreign key (activation_code_id) 
    references activation_code;

alter table customer_activation_code 
    add constraint FKFC657D159770384C 
    foreign key (customer_id) 
    references customer;
    