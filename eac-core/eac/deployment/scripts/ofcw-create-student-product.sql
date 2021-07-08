CREATE PROC eac_create_ofcw_student_product
  @product_name nvarchar(255),
  @division_id nvarchar(255),
  @erights_id int,
  @lecturer_erights_id int,
  @landing_page nvarchar(1000),
  @lecturer_landing_page nvarchar(1000),
  @service_level_agreement nvarchar(255),
  @home_page nvarchar(1000),
  @email nvarchar(255),
  @lecturer_licence_enddate nvarchar(255),
  @lecturer bit
AS
BEGIN
    
    declare @page_definition_id nvarchar(255);
    declare @component_id nvarchar(255);
    declare @asset_id nvarchar(255);
    declare @registerable_product_id nvarchar(255);
    declare @free_asset_id nvarchar(255);
    
    set @page_definition_id = (select newId());
    set @component_id = (select newId());
    set @asset_id = (select newId());
    set @registerable_product_id = (select newId());
    
	if exists (select * from sys.databases WHERE name = 'eactest') 
	BEGIN    
        set @free_asset_id = (select id from asset where erights_id = 5901);
    END
    
    if exists (select * from sys.databases WHERE name = 'eacuat') 
    BEGIN    
        set @free_asset_id = (select id from asset where erights_id = 4301);
    END

    if exists (select * from sys.databases WHERE name = 'eacprod') 
    BEGIN    
        set @free_asset_id = (select id from asset where erights_id = 7501);
    END
    
    print 'Free asset id: ' + @free_asset_id;
    print 'Page Definition id: ' + @page_definition_id;
    print 'Product id: ' + @registerable_product_id;
    print 'Component id: ' + @component_id;
    print 'Asset id: ' + @asset_id;
    print '';
    
    print 'Adding student product';
    
    -- PAGE
    insert into page_definition (id, obj_version, name, title, page_definition_type, division_id) values (@page_definition_id, 0, '', 'title.productregistration', 'PRODUCT_PAGE_DEFINITION', @division_id);
    
    -- TERMS AND CONDITIONS
    insert into component (id, obj_version, label_key) values (@component_id, 0, 'label.registration.tandc.header');
    
    -- PERSONAL DETAILS
    insert into page_component (id, obj_version, component_id, sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, @page_definition_id);
    -- ADDRESS
    insert into page_component (id, obj_version, component_id, sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, @page_definition_id);
    -- INSTITUTION DETAILS
    insert into page_component (id, obj_version, component_id, sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, @page_definition_id);
    -- MARKETING 
    insert into page_component (id, obj_version, component_id, sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, @page_definition_id);
    -- TERMS AND CONDITIONS
    insert into page_component (id, obj_version, component_id, sequence, page_definition_id) values (newId(), 0, @component_id, 4, @page_definition_id);
    
    --MALAYSIA ACCEPT TERMS AND CONDITIONS
    insert into field (id, obj_version, sequence, element_id, component_id, required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', @component_id, 1);
    
    --MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
    insert into field (id, obj_version, sequence, element_id, component_id, required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', @component_id, 0);
    
    insert into asset(id, obj_version, erights_id, product_name, division_id) values (@asset_id, 0, @erights_id, @product_name, @division_id);
    
    insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE', @registerable_product_id, 0, @asset_id, @landing_page, null, 'SELF_REGISTERABLE', @email, @service_level_agreement, @home_page);
    insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, activation_method, home_page) values ('LINKED', newId(), 0, @free_asset_id, '', @registerable_product_id, null, @email, @service_level_agreement, 'PRE_PARENT', @home_page);
    
    insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, @registerable_product_id, '119452A7-07BB-4B13-A334-BF44D04EAF57', @page_definition_id, '552C200A-7FC4-42E6-91FD-730720D02895');
    insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, @registerable_product_id, 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);
    
    print '';
    print 'Finished adding student product';
    print '';
    
    if @lecturer = 1
    BEGIN
	    
	    print 'Adding lecturer product';
	    print '';
	    
	    declare @lecturer_registerable_product_id nvarchar(255);
	    declare @lecturer_page_definition_id nvarchar(255);
	    declare @lecturer_tandc_component_id nvarchar(255);
	    declare @lecturer_important_info_component_id nvarchar(255);
	    declare @lecturer_course_info_component_id nvarchar(255);
	    declare @lecturer_book_adopt_component_id nvarchar(255);
	    declare @lecturer_asset_id nvarchar(255);
	    declare @lecturer_licence_id nvarchar(255);
	    
	    set @lecturer_registerable_product_id = (select newId());
	    set @lecturer_page_definition_id = (select newId());
	    set @lecturer_tandc_component_id = (select newId());
	    set @lecturer_important_info_component_id = (select newId());
	    set @lecturer_course_info_component_id = (select newId());
	    set @lecturer_book_adopt_component_id = (select newId());
	    set @lecturer_asset_id = (select newId());
	    set @lecturer_licence_id = (select newId());
	    
	    print 'Lecturer Page Definition id: ' + @lecturer_page_definition_id;
	    print 'Lecturer Product id: ' + @lecturer_registerable_product_id;
	    print 'Lecturer T and C Component id: ' + @lecturer_tandc_component_id;
	    print 'Lecturer Important info Component id: ' + @lecturer_important_info_component_id;
	    print 'Lecturer Course into Component id: ' + @lecturer_course_info_component_id;
	    print 'Lecturer Book adoption Component id: ' + @lecturer_book_adopt_component_id;
	    print 'Lecturer asset id: ' + @lecturer_asset_id;
	    
	    -- PAGE
	    insert into page_definition (id, obj_version, name, title, page_definition_type, division_id) values (@lecturer_page_definition_id, 0, '', 'title.productregistration', 'PRODUCT_PAGE_DEFINITION', @division_id);
	   
		-- TERMS AND CONDITIONS LECTURER 
		insert into component (id,obj_version,label_key) values (@lecturer_tandc_component_id, 0, 'label.registration.tandc.header');
		-- IMPORTANT INFO (LECTURER)
		insert into component (id,obj_version,label_key) values (@lecturer_important_info_component_id, 0, 'label.important');
		-- COURSE INFO (LECTURER)
		insert into component (id,obj_version,label_key) values (@lecturer_course_info_component_id, 0, 'label.registration.course.info');
		-- BOOK ADOPTION (LECTURER)
		insert into component (id,obj_version,label_key) values (@lecturer_book_adopt_component_id, 0, 'label.registration.book.adoption');
		
		-- IMPORTANT INFO
		insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, @lecturer_important_info_component_id, 0, @lecturer_page_definition_id);
		-- PERSONAL DETAILS
		insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 1, @lecturer_page_definition_id);
		-- INSTITUION DETAILS
		insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '718256F8-9DA0-4508-BC7E-8819BD53BD89', 2, @lecturer_page_definition_id);
		-- COURSE INFO
		insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, @lecturer_course_info_component_id, 3, @lecturer_page_definition_id);
		-- BOOK ADOPTION
		insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, @lecturer_book_adopt_component_id, 4, @lecturer_page_definition_id);
		-- MARKETING
		insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 5, @lecturer_page_definition_id);
		-- TERMS AND CONDITIONS
		insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, @lecturer_tandc_component_id, 6, @lecturer_page_definition_id);
		
		
		--IMPORTANT INFO
		insert into field (id, obj_version, sequence, element_id, component_id, required) values (newId(), 0, 0, '783297B4-274C-4498-A003-0BB17EB648AA', @lecturer_important_info_component_id, 0);
		--SUBJECTS TAUGHT
		insert into field (id, obj_version, sequence, element_id, component_id, required) values (newId(), 0, 1, '10576FB7-04FB-4375-9A5E-81D5E2C1006D', @lecturer_course_info_component_id, 1);
		--COURSE LEVEL
		insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 0, '2D28B465-E723-4A03-8634-30F810D4E0B1', @lecturer_course_info_component_id, 'foundation', 1);
		--BOOK ADOPTION
		insert into field (id, obj_version, sequence, element_id, component_id, default_value, required) values (newId(), 0, 0, '241F2550-8BEB-439C-942C-CC6C41F3EB1B', @lecturer_book_adopt_component_id, 'considering adopting book', 1);
		--MALAYSIA ACCEPT TERMS AND CONDITIONS
		insert into field (id, obj_version, sequence, element_id, component_id, required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', @lecturer_tandc_component_id, 1);
		--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
		insert into field (id, obj_version, sequence, element_id, component_id, required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', @lecturer_tandc_component_id, 0);
		
		insert into asset(id, obj_version, erights_id, product_name, division_id) values (@lecturer_asset_id, 0, @lecturer_erights_id, @product_name + ' (Lecturer)', @division_id);
		
		insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, home_page) values ('REGISTERABLE',@lecturer_registerable_product_id, 0, @lecturer_asset_id, @lecturer_landing_page, null, 'SELF_REGISTERABLE', @email, @service_level_agreement, @home_page);
		insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, activation_method, home_page) values ('LINKED', newId(), 0, @asset_id, '', @lecturer_registerable_product_id, null, @email, @service_level_agreement, 'POST_PARENT', @home_page);
		insert into product (product_type, id, obj_version, asset_id, landing_page, registerable_product_id, registerable_type, email, service_level_agreement, activation_method, home_page) values ('LINKED', newId(), 0, @free_asset_id, '', @lecturer_registerable_product_id, null, @email, @service_level_agreement, 'PRE_PARENT', @home_page);
	
		insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) values (@lecturer_licence_id, 0, 'CONCURRENT', null, CAST(@lecturer_licence_enddate AS datetime), 1, 1, null, null);
		
        insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, @lecturer_registerable_product_id, '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', @lecturer_page_definition_id, @lecturer_licence_id);
        insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, @lecturer_registerable_product_id, 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);


		print '';
		print 'Finished adding lecturer product';
		print '';
		
    END 

END 
GO