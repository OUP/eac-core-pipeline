-- New HE Title 3 Effective Communication for Nurses

-- License template definition
insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) 
values ('898F9AC7-DE84-46EE-9217-DD6BD7EDB8A9', 0, 'CONCURRENT', null, CAST('2021-12-31T23:59:59.997' AS datetime), 1, 1, null, null);

-- Product reg page definition
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) 
values ('AE0B281A-8F9C-43A7-9B6D-84C3E570FE24', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');

-- Components Specific to title
-- Course info
insert into component (id,obj_version,label_key) values ('4602D490-45B3-41BA-9911-BCC18C2154ED', 0, 'label.registration.course.info');
-- subjects
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) 
-- level
values (newId(), 0, 1, '10576FB7-04FB-4375-9A5E-81D5E2C1006D', '4602D490-45B3-41BA-9911-BCC18C2154ED', 'lecturer.title3.subjects.taught', 1);
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) 
values (newId(), 0, 0, '2D28B465-E723-4A03-8634-30F810D4E0B1', '4602D490-45B3-41BA-9911-BCC18C2154ED', 'foundation', 'lecturer.title3.course.level', 1);

-- Book adoption
insert into component (id,obj_version,label_key) values ('C7A42A2C-9E6C-4339-89CC-4EA92D850E26', 0, 'label.registration.book.adoption');
--radio options
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) 
values (newId(), 0, 0, '241F2550-8BEB-439C-942C-CC6C41F3EB1B', 'C7A42A2C-9E6C-4339-89CC-4EA92D850E26', 'considering adopting book', 'lecturer.title3.book.adoption', 1);

-- Terms and conditions
insert into component (id,obj_version,label_key) values ('777074E8-ED56-4B98-AE27-7AA33D8CCB7B', 0, 'label.registration.tandc.header');
--Accept check box
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) 
values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '777074E8-ED56-4B98-AE27-7AA33D8CCB7B', 'lecturer.title3.malaysia.cw.terms.accepted', 1);
-- Link
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) 
values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '777074E8-ED56-4B98-AE27-7AA33D8CCB7B', '', 0);


-- Page components
-- IMPORTANT INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '5EBFE7BE-2865-4EAE-B2AE-C9B9163BA562', 0, 'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24');
-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 1, 'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24');
-- INSTITUION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '718256F8-9DA0-4508-BC7E-8819BD53BD89', 2, 'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24');
-- COURSE INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '4602D490-45B3-41BA-9911-BCC18C2154ED', 3, 'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24');
-- BOOK ADOPTION
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'C7A42A2C-9E6C-4339-89CC-4EA92D850E26', 4, 'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24');
-- MARKETING
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 5, 'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '777074E8-ED56-4B98-AE27-7AA33D8CCB7B', 6, 'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24');


-- Product
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) 
values ('REGISTERABLE','02A00C43-140D-4A63-B0E5-62CBBF5626BB', 0, 42777, '', 'Effective Communication in Nursing (Lecturer Account)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes');
--Linked free content
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) 
values ('LINKED', '0D72D220-74AD-4065-B86E-D331477DB95B', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '02A00C43-140D-4A63-B0E5-62CBBF5626BB', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');

-- Registration activation as per other HE titles

-- Registration definition
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('PRODUCT_REGISTRATION', newId(), 0, '02A00C43-140D-4A63-B0E5-62CBBF5626BB', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24', '898F9AC7-DE84-46EE-9217-DD6BD7EDB8A9');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) 
values ('ACCOUNT_REGISTRATION', newId(), 0, '02A00C43-140D-4A63-B0E5-62CBBF5626BB', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);


-- Updates to all forms to include Position, Other and Level, Undergraduate

--POSITION - STUDENT/TEACHER-LECTURER
-- Other
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.education.position.other', 3, 'Other', 'B0AE87E6-40E4-4ED0-9B73-FC29894AE3BA');

-- COURSE LEVEL
-- Undergraduate Degree
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.courselvl.undergraduate', 5, 'undergraduate', '5797B49C-622F-4E76-8CE5-FB9BA4AEA1E4');

-- MESSAGES
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.position.other','Other');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.courselvl.undergraduate','Undergraduate/Degree');

