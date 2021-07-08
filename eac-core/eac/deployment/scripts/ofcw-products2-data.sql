--Biology for Matriculation Semester 1 Fourth Edition (HE Student)

-- PRODUCT REG PHASE 2 (HE STUDENT) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('ba0af376-b1e8-44a4-9b9c-e10a8506c294', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');


-- TERMS AND CONDITIONS HE STUDENT 
insert into component (id,obj_version,label_key) values ('e865faea-0f1c-4b62-b090-bd3d4b6907f2', 0, 'label.registration.tandc.header');

-- ***************** HE STUDENT  ***********************

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, 'ba0af376-b1e8-44a4-9b9c-e10a8506c294');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, 'ba0af376-b1e8-44a4-9b9c-e10a8506c294');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, 'ba0af376-b1e8-44a4-9b9c-e10a8506c294');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, 'ba0af376-b1e8-44a4-9b9c-e10a8506c294');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'e865faea-0f1c-4b62-b090-bd3d4b6907f2', 4, 'ba0af376-b1e8-44a4-9b9c-e10a8506c294');


--ELEMENTS


--MALAYSIA ACCEPT TERMS AND CONDITIONS
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', 'e865faea-0f1c-4b62-b090-bd3d4b6907f2', 'hestudent.title3.malaysia.cw.terms.accepted', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 'e865faea-0f1c-4b62-b090-bd3d4b6907f2', '', 0);


--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','adbe407d-e473-4f57-9225-5d7e73b2d868', 0, 131628, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release3/oxfordfajar/he-biology-product.jsp', 'Biology for Matriculation Semester 1 Fourth Edition', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'bf03ee96-ccfe-46f9-8025-29918a672295', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'adbe407d-e473-4f57-9225-5d7e73b2d868', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');

--HE STUDENT PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'adbe407d-e473-4f57-9225-5d7e73b2d868', '119452A7-07BB-4B13-A334-BF44D04EAF57', 'ba0af376-b1e8-44a4-9b9c-e10a8506c294', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, 'adbe407d-e473-4f57-9225-5d7e73b2d868', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);




--Chemistry for Matriculation Semester 1 Fourth Edition (HE Student)

-- PRODUCT REG PHASE 2 (HE STUDENT) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('724c50c2-6784-4695-b2fb-1970f189639e', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');


-- TERMS AND CONDITIONS HE STUDENT 
insert into component (id,obj_version,label_key) values ('a6e07fb5-0c67-401e-86d3-579009bdc026', 0, 'label.registration.tandc.header');

-- ***************** HE STUDENT  ***********************

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, '724c50c2-6784-4695-b2fb-1970f189639e');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, '724c50c2-6784-4695-b2fb-1970f189639e');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, '724c50c2-6784-4695-b2fb-1970f189639e');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, '724c50c2-6784-4695-b2fb-1970f189639e');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'a6e07fb5-0c67-401e-86d3-579009bdc026', 4, '724c50c2-6784-4695-b2fb-1970f189639e');


--ELEMENTS


--MALAYSIA ACCEPT TERMS AND CONDITIONS
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', 'a6e07fb5-0c67-401e-86d3-579009bdc026', 'hestudent.title3.malaysia.cw.terms.accepted', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 'a6e07fb5-0c67-401e-86d3-579009bdc026', '', 0);


--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','0e68001e-4bc4-431f-b327-dddda2bcc8fd', 0, 131675, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release3/oxfordfajar/he-chemistry-product.jsp', 'Chemistry for Matriculation Semester 1 Fourth Edition', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '9126bf7c-5459-42ae-850a-f53457724869', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '0e68001e-4bc4-431f-b327-dddda2bcc8fd', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');

--HE STUDENT PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, '0e68001e-4bc4-431f-b327-dddda2bcc8fd', '119452A7-07BB-4B13-A334-BF44D04EAF57', '724c50c2-6784-4695-b2fb-1970f189639e', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '0e68001e-4bc4-431f-b327-dddda2bcc8fd', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);



--Fundamentals of Finance with Microsoft Excel (HE Student and Lecturer)

-- LICENCE TEMPLATE FOR LECTURER
insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) values ('bf05b59c-a350-4270-bb23-dd6e20deec32', 0, 'CONCURRENT', null, CAST('2016-05-31T23:59:59.997' AS datetime), 1, 1, null, null);

-- PRODUCT REG PHASE 2 (HE STUDENT) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('73feba2a-f2c6-42b3-85f8-c3c0abcc33ee', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');
-- PRODUCT REG PHASE 2 (LECTURER) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('ba6819be-e297-4bba-955f-a3cb8ec30648', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');



-- TERMS AND CONDITIONS HE STUDENT 
insert into component (id,obj_version,label_key) values ('13a93dc3-df58-4399-9990-559497cbfe56', 0, 'label.registration.tandc.header');
-- TERMS AND CONDITIONS LECTURER 
insert into component (id,obj_version,label_key) values ('24ef475b-bafd-4a1c-913f-e2340b45186e', 0, 'label.registration.tandc.header');
-- IMPORTANT INFO (LECTURER)
insert into component (id,obj_version,label_key) values ('0727c1a8-d880-4f5e-a103-e54fc514758b', 0, 'label.important');
-- COURSE INFO (LECTURER)
insert into component (id,obj_version,label_key) values ('a768b94a-72b0-4898-a681-228ea73e61c3', 0, 'label.registration.course.info');
-- BOOK ADOPTION (LECTURER)
insert into component (id,obj_version,label_key) values ('0acab3f0-0036-495f-968c-42f5d5fa45af', 0, 'label.registration.book.adoption');

-- ***************** HE STUDENT  ***********************

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, '73feba2a-f2c6-42b3-85f8-c3c0abcc33ee');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, '73feba2a-f2c6-42b3-85f8-c3c0abcc33ee');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, '73feba2a-f2c6-42b3-85f8-c3c0abcc33ee');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, '73feba2a-f2c6-42b3-85f8-c3c0abcc33ee');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '13a93dc3-df58-4399-9990-559497cbfe56', 4, '73feba2a-f2c6-42b3-85f8-c3c0abcc33ee');



-- ***************** LECTURER ***********************
-- IMPORTANT INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0727c1a8-d880-4f5e-a103-e54fc514758b', 0, 'ba6819be-e297-4bba-955f-a3cb8ec30648');
-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 1, 'ba6819be-e297-4bba-955f-a3cb8ec30648');
-- INSTITUION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '718256F8-9DA0-4508-BC7E-8819BD53BD89', 2, 'ba6819be-e297-4bba-955f-a3cb8ec30648');
-- COURSE INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'a768b94a-72b0-4898-a681-228ea73e61c3', 3, 'ba6819be-e297-4bba-955f-a3cb8ec30648');
-- BOOK ADOPTION
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0acab3f0-0036-495f-968c-42f5d5fa45af', 4, 'ba6819be-e297-4bba-955f-a3cb8ec30648');
-- MARKETING
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 5, 'ba6819be-e297-4bba-955f-a3cb8ec30648');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '24ef475b-bafd-4a1c-913f-e2340b45186e', 6, 'ba6819be-e297-4bba-955f-a3cb8ec30648');


--ELEMENTS

--IMPORTANT INFO
-- LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, '783297B4-274C-4498-A003-0BB17EB648AA', '0727c1a8-d880-4f5e-a103-e54fc514758b', '', 0);
--SUBJECTS TAUGHT
-- LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '10576FB7-04FB-4375-9A5E-81D5E2C1006D', 'a768b94a-72b0-4898-a681-228ea73e61c3', 'lecturer.title4.subjects.taught', 1);
--COURSE LEVEL
-- LECTURER 
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '2D28B465-E723-4A03-8634-30F810D4E0B1', 'a768b94a-72b0-4898-a681-228ea73e61c3', 'foundation', 'lecturer.title4.course.level', 1);
--BOOK ADOPTION
-- LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '241F2550-8BEB-439C-942C-CC6C41F3EB1B', '0acab3f0-0036-495f-968c-42f5d5fa45af', 'considering adopting book', 'lecturer.title4.book.adoption', 1);


--MALAYSIA ACCEPT TERMS AND CONDITIONS
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '13a93dc3-df58-4399-9990-559497cbfe56', 'hestudent.title3.malaysia.cw.terms.accepted', 1);
--LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '24ef475b-bafd-4a1c-913f-e2340b45186e', 'lecturer.title4.malaysia.cw.terms.accepted', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '13a93dc3-df58-4399-9990-559497cbfe56', '', 0);
--LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '24ef475b-bafd-4a1c-913f-e2340b45186e', '', 0);


--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','df91f511-5af9-4b70-8f67-ec10f160b0fc', 0, 131645, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release3/oxfordfajar/he-finance-product.jsp', 'Fundamentals of Finance with Microsoft Excel', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'e26d243c-f32f-4a5e-a06d-b859724f1dbf', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'df91f511-5af9-4b70-8f67-ec10f160b0fc', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');
--LECTURER
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','47f88630-ea5f-4561-8dd0-42dcc221f5ed', 0, 131646, '', 'Fundamentals of Finance with Microsoft Excel (Lecturer)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes');
--LECTURER
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'b5a18200-c22c-4216-add6-28d702e23f8b', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '47f88630-ea5f-4561-8dd0-42dcc221f5ed', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '406c34b9-ecde-4686-b44e-a1094c11ba63', 0, 131645, '', 'Fundamentals of Finance with Microsoft Excel', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '47f88630-ea5f-4561-8dd0-42dcc221f5ed', 'cw.info@oxfordfajar.com.my', '30 minutes', 'POST_PARENT');


--HE STUDENT PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'df91f511-5af9-4b70-8f67-ec10f160b0fc', '119452A7-07BB-4B13-A334-BF44D04EAF57', '73feba2a-f2c6-42b3-85f8-c3c0abcc33ee', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, 'df91f511-5af9-4b70-8f67-ec10f160b0fc', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);
--LECTURER PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '47f88630-ea5f-4561-8dd0-42dcc221f5ed', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'ba6819be-e297-4bba-955f-a3cb8ec30648', 'bf05b59c-a350-4270-bb23-dd6e20deec32');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '47f88630-ea5f-4561-8dd0-42dcc221f5ed', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);




--Mathematics for Matriculation Semester 1 Fourth Edition (HE Student)

-- PRODUCT REG PHASE 2 (HE STUDENT) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('a171ed6b-7775-45f1-ace7-7f23949ac263', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');


-- TERMS AND CONDITIONS HE STUDENT 
insert into component (id,obj_version,label_key) values ('79c0b905-46ad-40a2-92ae-3aa1caf05b46', 0, 'label.registration.tandc.header');

-- ***************** HE STUDENT  ***********************

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, 'a171ed6b-7775-45f1-ace7-7f23949ac263');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, 'a171ed6b-7775-45f1-ace7-7f23949ac263');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, 'a171ed6b-7775-45f1-ace7-7f23949ac263');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, 'a171ed6b-7775-45f1-ace7-7f23949ac263');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '79c0b905-46ad-40a2-92ae-3aa1caf05b46', 4, 'a171ed6b-7775-45f1-ace7-7f23949ac263');


--ELEMENTS


--MALAYSIA ACCEPT TERMS AND CONDITIONS
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '79c0b905-46ad-40a2-92ae-3aa1caf05b46', 'hestudent.title3.malaysia.cw.terms.accepted', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '79c0b905-46ad-40a2-92ae-3aa1caf05b46', '', 0);


--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','e007b973-95ee-4cb6-b869-13cba989710f', 0, 131647, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release3/oxfordfajar/he-mathematics-product.jsp', 'Mathematics for Matriculation Semester 1 Fourth Edition', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'c80217c8-b315-469a-b2d0-3e963d9d4d29', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'e007b973-95ee-4cb6-b869-13cba989710f', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');

--HE STUDENT PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'e007b973-95ee-4cb6-b869-13cba989710f', '119452A7-07BB-4B13-A334-BF44D04EAF57', 'a171ed6b-7775-45f1-ace7-7f23949ac263', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, 'e007b973-95ee-4cb6-b869-13cba989710f', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);



--Physics for Matriculation Semester 1 Fourth Edition (HE Student)

-- PRODUCT REG PHASE 2 (HE STUDENT) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('4d1e9e1c-074c-4017-bb2e-587357190611', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');


-- TERMS AND CONDITIONS HE STUDENT 
insert into component (id,obj_version,label_key) values ('67b8e165-054f-4987-a28f-bee49a595465', 0, 'label.registration.tandc.header');

-- ***************** HE STUDENT  ***********************

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, '4d1e9e1c-074c-4017-bb2e-587357190611');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, '4d1e9e1c-074c-4017-bb2e-587357190611');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, '4d1e9e1c-074c-4017-bb2e-587357190611');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, '4d1e9e1c-074c-4017-bb2e-587357190611');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '67b8e165-054f-4987-a28f-bee49a595465', 4, '4d1e9e1c-074c-4017-bb2e-587357190611');


--ELEMENTS


--MALAYSIA ACCEPT TERMS AND CONDITIONS
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '67b8e165-054f-4987-a28f-bee49a595465', 'hestudent.title3.malaysia.cw.terms.accepted', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '67b8e165-054f-4987-a28f-bee49a595465', '', 0);


--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','f6b44681-512c-4314-a846-1356663d3935', 0, 131648, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release3/oxfordfajar/he-physics-product.jsp', 'Physics for Matriculation Semester 1 Fourth Edition', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '87d2b368-61a2-4c6c-afa2-8de87bd19dfa', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'f6b44681-512c-4314-a846-1356663d3935', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');

--HE STUDENT PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'f6b44681-512c-4314-a846-1356663d3935', '119452A7-07BB-4B13-A334-BF44D04EAF57', '4d1e9e1c-074c-4017-bb2e-587357190611', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, 'f6b44681-512c-4314-a846-1356663d3935', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);




--Tamadun Islam dan Tamadun Asia (TITAS) Edisi (HE Student and Lecturer)

-- LICENCE TEMPLATE FOR LECTURER
insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) values ('5930d1e0-a641-465d-8f06-179b5f6045a8', 0, 'CONCURRENT', null, CAST('2016-05-31T23:59:59.997' AS datetime), 1, 1, null, null);

-- PRODUCT REG PHASE 2 (HE STUDENT) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('a72edcbd-9ee4-4829-a2c2-14316cf5d92b', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');
-- PRODUCT REG PHASE 2 (LECTURER) 
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('60d28576-b5d7-45fb-9f6b-31061ebd7b02', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');



-- TERMS AND CONDITIONS HE STUDENT 
insert into component (id,obj_version,label_key) values ('c247f0a7-279f-40db-b40c-8d1244a1a4bb', 0, 'label.registration.tandc.header');
-- TERMS AND CONDITIONS LECTURER 
insert into component (id,obj_version,label_key) values ('85e7570a-4ec6-491a-906c-e26e955b4a51', 0, 'label.registration.tandc.header');
-- IMPORTANT INFO (LECTURER)
insert into component (id,obj_version,label_key) values ('06664906-35de-4ab9-b153-e029e99e61a8', 0, 'label.important');
-- COURSE INFO (LECTURER)
insert into component (id,obj_version,label_key) values ('4d4dcee9-cfbf-4d15-8487-9700498f3172', 0, 'label.registration.course.info');
-- BOOK ADOPTION (LECTURER)
insert into component (id,obj_version,label_key) values ('bdc434c7-8f57-4cfe-8a48-482dd45a2cdb', 0, 'label.registration.book.adoption');

-- ***************** HE STUDENT  ***********************

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, 'a72edcbd-9ee4-4829-a2c2-14316cf5d92b');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, 'a72edcbd-9ee4-4829-a2c2-14316cf5d92b');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, 'a72edcbd-9ee4-4829-a2c2-14316cf5d92b');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, 'a72edcbd-9ee4-4829-a2c2-14316cf5d92b');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'c247f0a7-279f-40db-b40c-8d1244a1a4bb', 4, 'a72edcbd-9ee4-4829-a2c2-14316cf5d92b');



-- ***************** LECTURER ***********************
-- IMPORTANT INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '06664906-35de-4ab9-b153-e029e99e61a8', 0, '60d28576-b5d7-45fb-9f6b-31061ebd7b02');
-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 1, '60d28576-b5d7-45fb-9f6b-31061ebd7b02');
-- INSTITUION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '718256F8-9DA0-4508-BC7E-8819BD53BD89', 2, '60d28576-b5d7-45fb-9f6b-31061ebd7b02');
-- COURSE INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '4d4dcee9-cfbf-4d15-8487-9700498f3172', 3, '60d28576-b5d7-45fb-9f6b-31061ebd7b02');
-- BOOK ADOPTION
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'bdc434c7-8f57-4cfe-8a48-482dd45a2cdb', 4, '60d28576-b5d7-45fb-9f6b-31061ebd7b02');
-- MARKETING
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 5, '60d28576-b5d7-45fb-9f6b-31061ebd7b02');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '85e7570a-4ec6-491a-906c-e26e955b4a51', 6, '60d28576-b5d7-45fb-9f6b-31061ebd7b02');


--ELEMENTS

--IMPORTANT INFO
-- LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, '783297B4-274C-4498-A003-0BB17EB648AA', '06664906-35de-4ab9-b153-e029e99e61a8', '', 0);
--SUBJECTS TAUGHT
-- LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '10576FB7-04FB-4375-9A5E-81D5E2C1006D', '4d4dcee9-cfbf-4d15-8487-9700498f3172', 'lecturer.title4.subjects.taught', 1);
--COURSE LEVEL
-- LECTURER 
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '2D28B465-E723-4A03-8634-30F810D4E0B1', '4d4dcee9-cfbf-4d15-8487-9700498f3172', 'foundation', 'lecturer.title4.course.level', 1);
--BOOK ADOPTION
-- LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '241F2550-8BEB-439C-942C-CC6C41F3EB1B', 'bdc434c7-8f57-4cfe-8a48-482dd45a2cdb', 'considering adopting book', 'lecturer.title4.book.adoption', 1);


--MALAYSIA ACCEPT TERMS AND CONDITIONS
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', 'c247f0a7-279f-40db-b40c-8d1244a1a4bb', 'hestudent.title3.malaysia.cw.terms.accepted', 1);
--LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '85e7570a-4ec6-491a-906c-e26e955b4a51', 'lecturer.title4.malaysia.cw.terms.accepted', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
--HE STUDENT
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 'c247f0a7-279f-40db-b40c-8d1244a1a4bb', '', 0);
--LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '85e7570a-4ec6-491a-906c-e26e955b4a51', '', 0);


--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','e6477a54-e420-4c4d-b76a-5b1e49f72963', 0, 131649, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release3/oxfordfajar/he-tamadun-product.jsp', 'Tamadun Islam dan Tamadun Asia (TITAS) Edisi Kedua', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
--HE STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '8a3b90a6-7795-45e0-af97-4f6f01a5df6c', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', 'e6477a54-e420-4c4d-b76a-5b1e49f72963', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');
--LECTURER
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','771869e7-fc34-4944-9a92-2d4420ff8bad', 0, 131650, '', 'Tamadun Islam dan Tamadun Asia (TITAS) Edisi Kedua (Lecturer)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes');
--LECTURER
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'f2c87649-7982-4ea1-af19-1d260376df54', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '771869e7-fc34-4944-9a92-2d4420ff8bad', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '2fc6f77e-17dc-48c6-a74a-54a5a9cdf3f9', 0, 131649, '', 'Tamadun Islam dan Tamadun Asia (TITAS) Edisi Kedua', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '771869e7-fc34-4944-9a92-2d4420ff8bad', 'cw.info@oxfordfajar.com.my', '30 minutes', 'POST_PARENT');


--HE STUDENT PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', newId(), 0, 'e6477a54-e420-4c4d-b76a-5b1e49f72963', '119452A7-07BB-4B13-A334-BF44D04EAF57', 'a72edcbd-9ee4-4829-a2c2-14316cf5d92b', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, 'e6477a54-e420-4c4d-b76a-5b1e49f72963', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);
--LECTURER PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '771869e7-fc34-4944-9a92-2d4420ff8bad', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', '60d28576-b5d7-45fb-9f6b-31061ebd7b02', '5930d1e0-a641-465d-8f06-179b5f6045a8');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '771869e7-fc34-4944-9a92-2d4420ff8bad', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);


