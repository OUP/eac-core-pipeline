	create table question (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        export_name nvarchar(255) null,
        element_text nvarchar(255) null,
        product_specific bit not null default 1,
        primary key (id)
    );
    
	create table element_country_restriction (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        element_id nvarchar(255) null,
        locale nvarchar(255) null,
        primary key (id)
    );
    
	alter table element add question_id nvarchar(255) null;
    
    alter table element add constraint FK9CE31EFCD0EA5B4C foreign key (question_id) references question;
    
    alter table answer add question_id nvarchar(255) null;
    
    alter table answer add answer_type nvarchar(255) not null constraint df_answer_type default 'ANSWER';
    
    alter table answer add registerable_product_id nvarchar(255) null;
    
    alter table answer drop constraint FKABCA3FBE8920ED68;
    
    declare @conAnsName nvarchar(32);
    declare @conAnsSql nvarchar(1000);
    
	-- find constraint name
	select @conAnsName = O.name from sysobjects AS O 
	left join sysobjects AS T on O.parent_obj = T.id 
	where isnull(objectproperty(O.id,'IsMSShipped'),1) = 0
	and O.name not like '%dtproper%'
	and O.name not like 'dt[_]%'
	and T.name = 'answer'
	and O.name like 'UQ__answer__%'

	-- delete if found
	if not @conAnsName is null
	begin
		select @conAnsSql = 'ALTER TABLE [answer] DROP CONSTRAINT [' + @conAnsName + ']'
		execute sp_executesql @conAnsSql
	end
    
    drop index answer_customer_field_idx on answer;
    
    drop index answer_element_idx on answer;
    
    alter table answer add constraint FKABCA3FBED0EA5B4C foreign key (question_id) references question;
       
    create index answer_customer_question_idx on answer (question_id, customer_id);

    create index answer_question_idx on answer (question_id);
    
    alter table answer drop constraint df_answer_type; 

    ALTER TABLE external_identifier ADD CONSTRAINT EXTERNAL_ID_UNQ
    UNIQUE NONCLUSTERED (
        external_id_type,
        external_id,
        external_system_id_type_id                                    
    );

    alter table url_skin add site_name nvarchar(255) null;
    alter table url_skin add contact_path nvarchar(255) null;

    EXEC sp_rename 'dbo.message.[message]', 'oldmessage', 'COLUMN';

    alter table message add message nvarchar(1000) null;

    begin
        declare @updateMessageSql nvarchar(1000);
        select @updateMessageSql = 'update message set message = oldmessage'
        execute sp_executesql @updateMessageSql
    end

    alter table message drop column oldmessage;
    
    alter table page_definition add preamble nvarchar(255) null;
    
    create table progress_bar (
        id varchar(255) not null,
        obj_version bigint not null,
        activation_state varchar(255),
        activation_strategy varchar(255),
        page varchar(255),
        registration_state varchar(255),
        registration_type varchar(255),
        token_state varchar(255),
        user_state varchar(255),
        primary key (id)
    );

    create table progress_bar_element (
        id varchar(255) not null,
        obj_version bigint not null,
        default_message varchar(255),
        element_type varchar(255),
        label varchar(255),
        progress_bar_id varchar(255),
        sequence int not null,
        primary key (id)
    );
    
    create table role (
        id nvarchar(255) not null,
        obj_version numeric(19,0) not null,
        name nvarchar(255) null unique,
        primary key (id)
    );
    
	create table customer_roles (
        customer_id nvarchar(255) not null,
        role_id nvarchar(255) not null,
        primary key (customer_id, role_id)
    );
    
    create table permission (
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
    
    alter table customer_roles 
        add constraint FK8704931CD42BCA4C 
        foreign key (role_id) 
        references role;

    alter table customer_roles 
        add constraint FK8704931C187C1D39 
        foreign key (customer_id) 
        references customer;
    
    alter table role_permissions 
        add constraint FKEAD9D23BD42BCA4C 
        foreign key (role_id) 
        references role;

    alter table role_permissions 
        add constraint FKEAD9D23BE61A1A2C 
        foreign key (permission_id) 
        references permission;

	alter table customer add last_login datetime2 null;