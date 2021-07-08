--Business Management: A Malaysian Perspective (2nd Edition) (HE Student and Lecturer)

-- LICENCE TEMPLATE FOR LECTURER
insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) values ('99AD9070-A05C-4290-841D-AA17DAE5F25E', 0, 'CONCURRENT', null, CAST('2016-05-31T23:59:59.997' AS datetime), 1, 1, null, null);

-- PRODUCT REG PHASE 2 (HE STUDENT) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('0659605B-6E7B-4B92-AFD7-7B6AE174B609', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');
-- PRODUCT REG PHASE 2 (LECTURER) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('06E89AEB-808B-414E-B9CE-6C9C7CEC6848', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');



-- TERMS AND CONDITIONS HE STUDENT 
insert into component (id,obj_version,label_key) values ('B45C4B2C-CE94-4CE9-883E-307E47B24FF8', 0, 'label.registration.tandc.header');
-- TERMS AND CONDITIONS LECTURER 
insert into component (id,obj_version,label_key) values ('AD60E4E9-3498-4F8C-8E47-D461B9713738', 0, 'label.registration.tandc.header');
-- IMPORTANT INFO (LECTURER)
insert into component (id,obj_version,label_key) values ('DB3E4F60-451C-4F96-942A-AF5BA9882882', 0, 'label.important');
-- COURSE INFO (LECTURER)
insert into component (id,obj_version,label_key) values ('1C764010-3FE4-47EC-B286-F5846DB1D6BA', 0, 'label.registration.course.info');
-- BOOK ADOPTION (LECTURER)
insert into component (id,obj_version,label_key) values ('4FA04CB7-6F7A-4A9F-90EA-E2D310819EC3', 0, 'label.registration.book.adoption');

-- ***************** HE STUDENT  ***********************

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, '0659605B-6E7B-4B92-AFD7-7B6AE174B609');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, '0659605B-6E7B-4B92-AFD7-7B6AE174B609');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, '0659605B-6E7B-4B92-AFD7-7B6AE174B609');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, '0659605B-6E7B-4B92-AFD7-7B6AE174B609');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'B45C4B2C-CE94-4CE9-883E-307E47B24FF8', 4, '0659605B-6E7B-4B92-AFD7-7B6AE174B609');



-- ***************** LECTURER ***********************
-- IMPORTANT INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'DB3E4F60-451C-4F96-942A-AF5BA9882882', 0, '06E89AEB-808B-414E-B9CE-6C9C7CEC6848');
-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 1, '06E89AEB-808B-414E-B9CE-6C9C7CEC6848');
-- INSTITUION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '718256F8-9DA0-4508-BC7E-8819BD53BD89', 2, '06E89AEB-808B-414E-B9CE-6C9C7CEC6848');
-- COURSE INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '1C764010-3FE4-47EC-B286-F5846DB1D6BA', 3, '06E89AEB-808B-414E-B9CE-6C9C7CEC6848');
-- BOOK ADOPTION
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '4FA04CB7-6F7A-4A9F-90EA-E2D310819EC3', 4, '06E89AEB-808B-414E-B9CE-6C9C7CEC6848');
-- MARKETING
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 5, '06E89AEB-808B-414E-B9CE-6C9C7CEC6848');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'AD60E4E9-3498-4F8C-8E47-D461B9713738', 6, '06E89AEB-808B-414E-B9CE-6C9C7CEC6848');


--ELEMENTS

--IMPORTANT INFO
-- LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, '783297B4-274C-4498-A003-0BB17EB648AA', 'DB3E4F60-451C-4F96-942A-AF5BA9882882', '', 0);
--SUBJECTS TAUGHT
-- LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '10576FB7-04FB-4375-9A5E-81D5E2C1006D', '1C764010-3FE4-47EC-B286-F5846DB1D6BA', 'lecturer.title4.subjects.taught', 1);
--COURSE LEVEL
-- LECTURER 
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '2D28B465-E723-4A03-8634-30F810D4E0B1', '1C764010-3FE4-47EC-B286-F5846DB1D6BA', 'foundation', 'lecturer.title4.course.level', 1);
--BOOK ADOPTION
-- LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '241F2550-8BEB-439C-942C-CC6C41F3EB1B', '4FA04CB7-6F7A-4A9F-90EA-E2D310819EC3', 'considering adopting book', 'lecturer.title4.book.adoption', 1);


--MALAYSIA ACCEPT TERMS AND CONDITIONS
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', 'B45C4B2C-CE94-4CE9-883E-307E47B24FF8', 'hestudent.title3.malaysia.cw.terms.accepted', 1);
--LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', 'AD60E4E9-3498-4F8C-8E47-D461B9713738', 'lecturer.title4.malaysia.cw.terms.accepted', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 'B45C4B2C-CE94-4CE9-883E-307E47B24FF8', '', 0);
--LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 'AD60E4E9-3498-4F8C-8E47-D461B9713738', '', 0);


--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','1AC6E96F-86AB-4EE2-BE44-9E9268030D7D', 0, 103618, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release3/oxfordfajar/he-malaysian-perspective-product.jsp', 'Business Management: A Malaysian Perspective (2nd Edition)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'F753E37F-AA85-413D-BB30-B8424E598172', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '1AC6E96F-86AB-4EE2-BE44-9E9268030D7D', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');
--LECTURER
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','5478953D-E412-457C-8A00-F2300C897898', 0, 103617, '', 'Business Management: A Malaysian Perspective (2nd Edition) (Lecturer)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes');
--LECTURER
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '69395C02-70A5-4E3F-82E0-1A6AAE6F667F', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '5478953D-E412-457C-8A00-F2300C897898', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'EB3A364A-ADDE-494A-849E-286149CB2714', 0, 103618, '', 'Business Management: A Malaysian Perspective (2nd Edition)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '5478953D-E412-457C-8A00-F2300C897898', 'cw.info@oxfordfajar.com.my', '30 minutes', 'POST_PARENT');


--HE STUDENT PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '1AC6E96F-86AB-4EE2-BE44-9E9268030D7D', '119452A7-07BB-4B13-A334-BF44D04EAF57', '0659605B-6E7B-4B92-AFD7-7B6AE174B609', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '1AC6E96F-86AB-4EE2-BE44-9E9268030D7D', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);
--LECTURER PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '5478953D-E412-457C-8A00-F2300C897898', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', '06E89AEB-808B-414E-B9CE-6C9C7CEC6848', '99AD9070-A05C-4290-841D-AA17DAE5F25E');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '5478953D-E412-457C-8A00-F2300C897898', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);


