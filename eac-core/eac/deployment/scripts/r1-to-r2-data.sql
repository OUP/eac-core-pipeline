-- License template definition

insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,time_period,allowed_usages) values ('6A0386AC-69AB-4E51-BBB6-8670F303F09C', 0, 'CONCURRENT', null, CAST('2015-10-31T23:59:59.997' AS datetime), 1, 1, null, null);

-- PRODUCT REG PHASE 2 (TEACHER)
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('BB530863-D4F8-4C80-BC9C-911D10F480CC', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');
-- PRODUCT REG PHASE 2 (HE STUDENT) TITLE 1
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('C816B7E7-7138-49A6-84AF-555BEA53B825', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');
-- PRODUCT REG PHASE 2 (HE STUDENT) TITLE 2
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('13AC20F6-0414-4569-B6F0-D92FC3AE24A0', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');
-- PRODUCT REG PHASE 2 (LECTURER) TITLE 1
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('207BFAE7-57CD-4570-BA7B-2F10B7BAA337', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');
-- PRODUCT REG PHASE 2 (LECTURER) TITLE 2
insert into page_definition (id,obj_version,name,title,page_definition_type,division_id) values ('EE67C91C-A753-4C1B-B5CB-F54430C289AA', 0, '','title.productregistration', 'PRODUCT_PAGE_DEFINITION', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd');


-- INSTITUTION DETAILS
insert into component (id,obj_version,label_key) values ('718256F8-9DA0-4508-BC7E-8819BD53BD89', 0, 'label.registration.institution.details');
-- TERMS AND CONDITIONS TEACHER
insert into component (id,obj_version,label_key) values ('DC08E959-FE41-4272-A507-479B45C59D8F', 0, 'label.registration.tandc.header');
-- TERMS AND CONDITIONS HE STUDENT TITLE 1
insert into component (id,obj_version,label_key) values ('850359C6-5970-4156-9A86-A2833ABC8E2A', 0, 'label.registration.tandc.header');
-- TERMS AND CONDITIONS HE STUDENT TITLE 2
insert into component (id,obj_version,label_key) values ('F7F40991-366B-408B-A36B-3D11AA624F03', 0, 'label.registration.tandc.header');
-- TERMS AND CONDITIONS LECTURER TITLE 1
insert into component (id,obj_version,label_key) values ('D0A14C49-11A3-4259-801E-5AC4F151BB31', 0, 'label.registration.tandc.header');
-- TERMS AND CONDITIONS LECTURER TITLE 2
insert into component (id,obj_version,label_key) values ('3E3088D5-3859-4034-B540-8AFD64CC81F1', 0, 'label.registration.tandc.header');
-- SUBJECTS TAUGHT (TEACHER)
insert into component (id,obj_version,label_key) values ('1852260F-52A5-4D9D-B6BC-6A086735ED67', 0, 'label.registration.subjects.taught');
-- IMPORTANT INFO (TEACHER)
insert into component (id,obj_version,label_key) values ('52C5021C-5334-41BD-873D-63F64236CFDF', 0, 'label.important');
-- IMPORTANT INFO (LECTURER TITLE 1)
insert into component (id,obj_version,label_key) values ('5EBFE7BE-2865-4EAE-B2AE-C9B9163BA562', 0, 'label.important');
-- IMPORTANT INFO (LECTURER TITLE 2)
insert into component (id,obj_version,label_key) values ('4164AF51-0D15-4B11-8B41-4B83E77675E8', 0, 'label.important');
-- COURSE INFO (LECTURER) 
-- TITLE 1
insert into component (id,obj_version,label_key) values ('49CEF46A-58AA-4EE5-8F7B-BC26BF80243F', 0, 'label.registration.course.info');
-- TITLE 2
insert into component (id,obj_version,label_key) values ('D38BE20C-9882-4651-8012-B4FD75E3A260', 0, 'label.registration.course.info');
-- BOOK ADOPTION (LECTURER) 
-- TITLE 1
insert into component (id,obj_version,label_key) values ('A988F159-4220-4EDE-95CA-D9DA31CCB6A9', 0, 'label.registration.book.adoption');
-- TITLE 2
insert into component (id,obj_version,label_key) values ('0480D590-5985-41B5-B669-DFB95B30583E', 0, 'label.registration.book.adoption');


-- ***************** PHASE 2 HE STUDENT TITLE 1 ***********************

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, 'C816B7E7-7138-49A6-84AF-555BEA53B825');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, 'C816B7E7-7138-49A6-84AF-555BEA53B825');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, 'C816B7E7-7138-49A6-84AF-555BEA53B825');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, 'C816B7E7-7138-49A6-84AF-555BEA53B825');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '850359C6-5970-4156-9A86-A2833ABC8E2A', 4, 'C816B7E7-7138-49A6-84AF-555BEA53B825');

-- ***************** PHASE 2 HE STUDENT TITLE 2 ***********************

-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 0, '13AC20F6-0414-4569-B6F0-D92FC3AE24A0');
-- ADDRESS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '21054566-EF39-4E7A-ACA0-F544EA860B6C', 1, '13AC20F6-0414-4569-B6F0-D92FC3AE24A0');
-- INSTITUTION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '9921178D-2B92-4696-8215-CB8F3BAD2E97', 2, '13AC20F6-0414-4569-B6F0-D92FC3AE24A0');
-- MARKETING 
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 3, '13AC20F6-0414-4569-B6F0-D92FC3AE24A0');
-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'F7F40991-366B-408B-A36B-3D11AA624F03', 4, '13AC20F6-0414-4569-B6F0-D92FC3AE24A0');


-- ***************** PHASE 2 TEACHER ***********************
-- IMPORTANT INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '52C5021C-5334-41BD-873D-63F64236CFDF', 0, 'BB530863-D4F8-4C80-BC9C-911D10F480CC');
-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 1, 'BB530863-D4F8-4C80-BC9C-911D10F480CC');
-- SCHOOL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 2, 'BB530863-D4F8-4C80-BC9C-911D10F480CC');

-- SUBJECTS TAUGHT
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '1852260F-52A5-4D9D-B6BC-6A086735ED67', 3, 'BB530863-D4F8-4C80-BC9C-911D10F480CC');

-- MARKETING
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 4, 'BB530863-D4F8-4C80-BC9C-911D10F480CC');

-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'DC08E959-FE41-4272-A507-479B45C59D8F', 5, 'BB530863-D4F8-4C80-BC9C-911D10F480CC');

-- ***************** PHASE 2 LECTURER TITLE 1 ***********************
-- IMPORTANT INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '5EBFE7BE-2865-4EAE-B2AE-C9B9163BA562', 0, '207BFAE7-57CD-4570-BA7B-2F10B7BAA337');
-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 1, '207BFAE7-57CD-4570-BA7B-2F10B7BAA337');
-- INSTITUION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '718256F8-9DA0-4508-BC7E-8819BD53BD89', 2, '207BFAE7-57CD-4570-BA7B-2F10B7BAA337');

-- COURSE INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '49CEF46A-58AA-4EE5-8F7B-BC26BF80243F', 3, '207BFAE7-57CD-4570-BA7B-2F10B7BAA337');

-- BOOK ADOPTION
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'A988F159-4220-4EDE-95CA-D9DA31CCB6A9', 4, '207BFAE7-57CD-4570-BA7B-2F10B7BAA337');

-- MARKETING
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 5, '207BFAE7-57CD-4570-BA7B-2F10B7BAA337');

-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'D0A14C49-11A3-4259-801E-5AC4F151BB31', 6, '207BFAE7-57CD-4570-BA7B-2F10B7BAA337');

-- ***************** PHASE 2 LECTURER TITLE 2 ***********************
-- IMPORTANT INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '4164AF51-0D15-4B11-8B41-4B83E77675E8', 0, 'EE67C91C-A753-4C1B-B5CB-F54430C289AA');
-- PERSONAL DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0241481B-66A8-4591-BFF3-5873F6CB4BC5', 1, 'EE67C91C-A753-4C1B-B5CB-F54430C289AA');
-- INSTITUION DETAILS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '718256F8-9DA0-4508-BC7E-8819BD53BD89', 2, 'EE67C91C-A753-4C1B-B5CB-F54430C289AA');

-- COURSE INFO
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, 'D38BE20C-9882-4651-8012-B4FD75E3A260', 3, 'EE67C91C-A753-4C1B-B5CB-F54430C289AA');

-- BOOK ADOPTION
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '0480D590-5985-41B5-B669-DFB95B30583E', 4, 'EE67C91C-A753-4C1B-B5CB-F54430C289AA');

-- MARKETING
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '87782783-21DE-4301-B208-43DF5F62CAAA', 5, 'EE67C91C-A753-4C1B-B5CB-F54430C289AA');

-- TERMS AND CONDITIONS
insert into page_component (id,obj_version,component_id,sequence, page_definition_id) values (newId(), 0, '3E3088D5-3859-4034-B540-8AFD64CC81F1', 6, 'EE67C91C-A753-4C1B-B5CB-F54430C289AA');



--ELEMENTS

--IMPORTANT INFO
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('783297B4-274C-4498-A003-0BB17EB648AA', 0, 'label.lecturer.important.info', 'title.productinfo', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'LABEL', '', 0, '783297B4-274C-4498-A003-0BB17EB648AA', '');
-- PHASE 2 LECTURER TITLE 1
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, '783297B4-274C-4498-A003-0BB17EB648AA', '5EBFE7BE-2865-4EAE-B2AE-C9B9163BA562', '', 0);
-- PHASE 2 LECTURER TITLE 2
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, '783297B4-274C-4498-A003-0BB17EB648AA', '4164AF51-0D15-4B11-8B41-4B83E77675E8', '', 0);

--IMPORTANT INFO
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('E2BD1B7A-05C5-420B-BAB7-AB350D508B45', 0, 'label.productinfo', 'title.productinfo', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'LABEL', '', 0, 'E2BD1B7A-05C5-420B-BAB7-AB350D508B45', '');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'E2BD1B7A-05C5-420B-BAB7-AB350D508B45', '52C5021C-5334-41BD-873D-63F64236CFDF', '', 0);

--INSTITUTION ADDRESS LINE 1
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('5C7C0430-FD96-48BA-BE43-2D19E2463AE0', 0, 'label.institution.registration.address.line1', 'title.institution.registration.address.line1', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '5C7C0430-FD96-48BA-BE43-2D19E2463AE0', '');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 3, '5C7C0430-FD96-48BA-BE43-2D19E2463AE0', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'teacher.address.line1', 0);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 4, '5C7C0430-FD96-48BA-BE43-2D19E2463AE0', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'lecturer.address.line1', 0);

--INSTITUTION ADDRESS LINE 2
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('3AC3EA31-73D4-4AC7-8206-9EF83BAC0EA6', 0, 'label.institution.registration.address.line2', 'title.institution.registration.address.line2', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '3AC3EA31-73D4-4AC7-8206-9EF83BAC0EA6', '');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 4, '3AC3EA31-73D4-4AC7-8206-9EF83BAC0EA6', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'teacher.address.line2', 0);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 5, '3AC3EA31-73D4-4AC7-8206-9EF83BAC0EA6', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'lecturer.address.line2', 0);

--INSTITUTION CITY/TOWN
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('BD820E45-60B4-4445-B72C-5DFAE9FD7B49', 0, 'label.registration.address.line3', 'title.registration.address.line3', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, 'BD820E45-60B4-4445-B72C-5DFAE9FD7B49', '');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 5, 'BD820E45-60B4-4445-B72C-5DFAE9FD7B49', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'teacher.address.line3', 1);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 6, 'BD820E45-60B4-4445-B72C-5DFAE9FD7B49', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'lecturer.address.line3', 1);

--INSTITUTION POSTCODE
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('0D9BDD63-AC67-481D-9854-D2AF9AAC99F2', 0, 'label.registration.address.line4', 'title.registration.address.line4', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '0D9BDD63-AC67-481D-9854-D2AF9AAC99F2', '');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 6, '0D9BDD63-AC67-481D-9854-D2AF9AAC99F2', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'teacher.address.line4', 0);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 7, '0D9BDD63-AC67-481D-9854-D2AF9AAC99F2', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'lecturer.address.line4', 0);

--INSTITUTION STATE
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('986C0C21-DB24-4C69-9079-2621244B7BF2', 0, 'label.registration.address.line5', 'title.registration.address.line5', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '986C0C21-DB24-4C69-9079-2621244B7BF2', '');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 7, '986C0C21-DB24-4C69-9079-2621244B7BF2', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'teacher.address.line5', 1);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 8, '986C0C21-DB24-4C69-9079-2621244B7BF2', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'lecturer.address.line5', 1);

--INSTITUTION COUNTRY
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('BFCFF7F2-067B-443F-994B-BFEF4F99D1BD', 0, 'label.registration.address.line6', 'title.registration.address.line6', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('F6293B3F-6197-4804-AA37-C76FD4E9E573', 0, 'SELECT', '', 0, 'BFCFF7F2-067B-443F-994B-BFEF4F99D1BD', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.my', 1, 'my', 'F6293B3F-6197-4804-AA37-C76FD4E9E573');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.sg', 2, 'sg', 'F6293B3F-6197-4804-AA37-C76FD4E9E573');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.id', 3, 'id', 'F6293B3F-6197-4804-AA37-C76FD4E9E573');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.bn', 4, 'bn', 'F6293B3F-6197-4804-AA37-C76FD4E9E573');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.ph', 5, 'ph', 'F6293B3F-6197-4804-AA37-C76FD4E9E573');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.th', 6, 'th', 'F6293B3F-6197-4804-AA37-C76FD4E9E573');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.vn', 7, 'vn', 'F6293B3F-6197-4804-AA37-C76FD4E9E573');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.country.other', 8, 'other', 'F6293B3F-6197-4804-AA37-C76FD4E9E573');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 9, 'BFCFF7F2-067B-443F-994B-BFEF4F99D1BD', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'my', 'teacher.address.line6', 1);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 10, 'BFCFF7F2-067B-443F-994B-BFEF4F99D1BD', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'my', 'lecturer.address.line6', 1);

--INSTITUTION DOMAIN EMAIL
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('6C48F273-0355-49A1-9023-A0FEC68864E3', 0, 'label.institution.domain.email', 'title.institution.domain.email', 'info.institution.domain.email', '^[a-zA-Z0-9!#$%&''*+/=?^_`{|}~-]+(?:\.[a-zA-Z0-9!#$%&''*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '6C48F273-0355-49A1-9023-A0FEC68864E3', '');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 10, '6C48F273-0355-49A1-9023-A0FEC68864E3', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'teacher.institution.domain.email', 0);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 11, '6C48F273-0355-49A1-9023-A0FEC68864E3', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'lecturer.institution.domain.email', 0);

--INSTITUTION CONTACT NUMBER
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('2255A527-1042-4BF1-BE00-0060C228A186', 0, 'label.institution.contact.number', 'title.institution.contact.number', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '2255A527-1042-4BF1-BE00-0060C228A186', '');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 11, '2255A527-1042-4BF1-BE00-0060C228A186', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'teacher.institution.contact.number', 1);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 12, '2255A527-1042-4BF1-BE00-0060C228A186', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'lecturer.institution.contact.number', 1);

--INSTITUTION CONTACT NUMBER TYPE
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('E51422B8-7E7C-454B-A045-26EB259F6C39', 0, 'label.contact.number.type', 'title.contact.number.type', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('A262078C-55CB-4B55-80C0-72BA205F5CD0', 0, 'SELECT', '', 0, 'E51422B8-7E7C-454B-A045-26EB259F6C39', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.contact.type.mobile', 1, 'mobile', 'A262078C-55CB-4B55-80C0-72BA205F5CD0');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.contact.type.institution', 2, 'institution', 'A262078C-55CB-4B55-80C0-72BA205F5CD0');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.contact.type.home', 3, 'home', 'A262078C-55CB-4B55-80C0-72BA205F5CD0');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 12, 'E51422B8-7E7C-454B-A045-26EB259F6C39', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'mobile', 'teacher.institution.contact.number.type', 1);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 13, 'E51422B8-7E7C-454B-A045-26EB259F6C39', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'mobile', 'lecturer.institution.contact.number.type', 1);

--SUBJECTS TAUGHT
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('10576FB7-04FB-4375-9A5E-81D5E2C1006D', 0, 'label.registration.subjects.taught', 'title.registration.subjects.taught', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values (newId(), 0, 'TEXTFIELD', '', 0, '10576FB7-04FB-4375-9A5E-81D5E2C1006D', '');
-- PHASE 2 TEACHER 
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, '10576FB7-04FB-4375-9A5E-81D5E2C1006D', '1852260F-52A5-4D9D-B6BC-6A086735ED67', 'teacher.subjects.taught', 1);
-- PHASE 2 LECTURER TITLE 1 
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '10576FB7-04FB-4375-9A5E-81D5E2C1006D', '49CEF46A-58AA-4EE5-8F7B-BC26BF80243F', 'lecturer.title1.subjects.taught', 1);
-- PHASE 2 LECTURER TITLE 2
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '10576FB7-04FB-4375-9A5E-81D5E2C1006D', 'D38BE20C-9882-4651-8012-B4FD75E3A260', 'lecturer.title2.subjects.taught', 1);

--LEVEL
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('5CDCA662-E4E9-4244-8645-09585602640B', 0, 'label.registration.level', 'title.registration.level', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('650249B2-E331-4EF3-9B4C-03EFC7B1AB1E', 0, 'SELECT', '', 0, '5CDCA662-E4E9-4244-8645-09585602640B', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.lvl.lower.primary', 1, 'lower primary', '650249B2-E331-4EF3-9B4C-03EFC7B1AB1E');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.lvl.upper.primary', 2, 'upper primary', '650249B2-E331-4EF3-9B4C-03EFC7B1AB1E');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.lvl.lower.secondary', 3, 'lower secondary', '650249B2-E331-4EF3-9B4C-03EFC7B1AB1E');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.lvl.upper.secondary', 4, 'upper secondary', '650249B2-E331-4EF3-9B4C-03EFC7B1AB1E');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.lvl.form.six', 5, 'form six', '650249B2-E331-4EF3-9B4C-03EFC7B1AB1E');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.lvl.other', 6, 'other', '650249B2-E331-4EF3-9B4C-03EFC7B1AB1E');
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 1, '5CDCA662-E4E9-4244-8645-09585602640B', '1852260F-52A5-4D9D-B6BC-6A086735ED67', 'lower primary', 'teacher.level', 1);

-- COURSE LEVEL
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('2D28B465-E723-4A03-8634-30F810D4E0B1', 0, 'label.registration.level', 'title.registration.level', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('5797B49C-622F-4E76-8CE5-FB9BA4AEA1E4', 0, 'SELECT', '', 0, '2D28B465-E723-4A03-8634-30F810D4E0B1', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.courselvl.foundation', 1, 'foundation', '5797B49C-622F-4E76-8CE5-FB9BA4AEA1E4');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.courselvl.certification', 2, 'certification', '5797B49C-622F-4E76-8CE5-FB9BA4AEA1E4');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.courselvl.diploma', 3, 'diploma', '5797B49C-622F-4E76-8CE5-FB9BA4AEA1E4');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.courselvl.professional', 4, 'professional postgraduate', '5797B49C-622F-4E76-8CE5-FB9BA4AEA1E4');
-- PHASE 2 LECTURER TITLE 1 
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '2D28B465-E723-4A03-8634-30F810D4E0B1', '49CEF46A-58AA-4EE5-8F7B-BC26BF80243F', 'foundation', 'lecturer.title1.course.level', 1);
-- PHASE 2 LECTURER TITLE 2
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '2D28B465-E723-4A03-8634-30F810D4E0B1', 'D38BE20C-9882-4651-8012-B4FD75E3A260', 'foundation', 'lecturer.title2.course.level', 1);

-- BOOK ADOPTION
insert into element (id,obj_version,element_text,title_text,help_text,regular_expression) values ('241F2550-8BEB-439C-942C-CC6C41F3EB1B', 0, 'label.registration.adopted.status', 'title.registration.adopted.status', '', '');
insert into tag (id, obj_version, tag_type, value, empty_option, element_id, url) values ('FA97727F-3084-446F-9CBD-BDB4A4932D1A', 0, 'RADIO', '', 0, '241F2550-8BEB-439C-942C-CC6C41F3EB1B', '');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.adopted.book', 1, 'adopted book', 'FA97727F-3084-446F-9CBD-BDB4A4932D1A');
insert into tag_option (id, obj_version, label, sequence, value, tag_id) values (newId(), 0, 'label.registration.adopted.consider', 2, 'considering adopting book', 'FA97727F-3084-446F-9CBD-BDB4A4932D1A');
-- PHASE 2 LECTURER TITLE 1 
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '241F2550-8BEB-439C-942C-CC6C41F3EB1B', 'A988F159-4220-4EDE-95CA-D9DA31CCB6A9', 'considering adopting book', 'lecturer.title1.book.adoption', 1);
-- PHASE 2 LECTURER TITLE 2 
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '241F2550-8BEB-439C-942C-CC6C41F3EB1B', '0480D590-5985-41B5-B669-DFB95B30583E', 'considering adopting book', 'lecturer.title2.book.adoption', 1);


--POSITION - STUDENT/TEACHER-LECTURER
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '8EFCF8EE-36B9-4B45-820A-2F61ADAAC225', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'Teacher', 'teacher.position', 1);
-- PHASE 2 LECTURER  
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 0, '8EFCF8EE-36B9-4B45-820A-2F61ADAAC225', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'Teacher', 'lecturer.position', 1);

--INSTITUTION TYPE
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 1, '025A165B-CDBD-4B2C-8AAE-5097793FD5DE', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'national primary', 'instituition.type', 1);
-- PHASE 2 LECTURER 
insert into field (id, obj_version, sequence, element_id, component_id, default_value, export_name,required) values (newId(), 0, 1, '025A165B-CDBD-4B2C-8AAE-5097793FD5DE', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'public university', 'instituition.type', 1);

--INSTITUTION NAME
-- PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 2, '9ae814a7-2eeb-11e0-be2b-a4badbe688c6', 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA', 'teacher.institution.name', 1);
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 2, '9ae814a7-2eeb-11e0-be2b-a4badbe688c6', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'lecturer.institution.name', 1);

--FACULTY/DEPARTMENT
-- PHASE 2 LECTURER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 3, 'E14668C5-20B8-442D-AF86-A47A3F8688FE', '718256F8-9DA0-4508-BC7E-8819BD53BD89', 'faculty.department', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS
--PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', 'DC08E959-FE41-4272-A507-479B45C59D8F', 'teacher.malaysia.cw.terms.accepted', 1);
--PHASE 2 HE STUDENT TITLE 1
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '850359C6-5970-4156-9A86-A2833ABC8E2A', 'hestudent.title1.malaysia.cw.terms.accepted', 1);
--PHASE 2 HE STUDENT TITLE 2
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', 'F7F40991-366B-408B-A36B-3D11AA624F03', 'hestudent.title2.malaysia.cw.terms.accepted', 1);
--PHASE 2 LECTURER TITLE 1
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', 'D0A14C49-11A3-4259-801E-5AC4F151BB31', 'lecturer.title1.malaysia.cw.terms.accepted', 1);
--PHASE 2 LECTURER TITLE 2
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 0, 'D61AA669-B769-44DC-9248-859ED232B856', '3E3088D5-3859-4034-B540-8AFD64CC81F1', 'lecturer.title2.malaysia.cw.terms.accepted', 1);

--MALAYSIA ACCEPT TERMS AND CONDITIONS LINK
--PHASE 2 TEACHER
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 'DC08E959-FE41-4272-A507-479B45C59D8F', '', 0);
--PHASE 2 HE STUDENT TITLE 1
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '850359C6-5970-4156-9A86-A2833ABC8E2A', '', 0);
--PHASE 2 HE STUDENT TITLE 2
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 'F7F40991-366B-408B-A36B-3D11AA624F03', '', 0);
--PHASE 2 LECTURER TITLE 1
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', 'D0A14C49-11A3-4259-801E-5AC4F151BB31', '', 0);
--PHASE 2 LECTURER TITLE 2
insert into field (id, obj_version, sequence, element_id, component_id, export_name,required) values (newId(), 0, 1, '9CB527EC-CF4E-442F-9A92-5151DE5B5F6A', '3E3088D5-3859-4034-B540-8AFD64CC81F1', '', 0);






insert into registration_activation(activation_type,id,obj_version,validator_email) values ('VALIDATED','166F6F3C-3C0F-46C8-93C5-797BA8F178DF',0,'eacsystemadmin@oup.com');

--TEACHER
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','948B5C2B-0D04-4F2F-BA7A-0BFDB3D0D77A', 0, 46862, '', 'OFCW Teacher Resources', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes');
--TEACHER LINKED STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '52ACA31D-7DD7-4618-B652-AB2636653E5D', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '948B5C2B-0D04-4F2F-BA7A-0BFDB3D0D77A', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');

--HE STUDENT PRODUCT 1
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','8DBAF754-0238-4EAB-8D0F-26A7A0D8997B', 0, 27502, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release2/oxfordfajar/spm-success-bio.jsp', 'Principles of Management', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
--HE STUDENT PRODUCT 1 LINKED STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '93678573-8EA6-469E-A1B8-2B9905B3D097', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '8DBAF754-0238-4EAB-8D0F-26A7A0D8997B', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');

--HE STUDENT PRODUCT 2
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, home_page) values ('REGISTERABLE','6F9C4166-F1FD-4B0F-B098-C77787675B5A', 0, 27504, 'http://dev.eac.uk.oup.com/eacSampleSite/protected/release2/oxfordfajar/spm-success-english.jsp', 'Business Statistics', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes', 'http://dev.eac.uk.oup.com/eacSampleSite');
--HE STUDENT PRODUCT 2 LINKED STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'CA60C86E-3E25-459F-9123-AFB4297A0566', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '6F9C4166-F1FD-4B0F-B098-C77787675B5A', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');

--HE LECTURER PRODUCT 1
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','210806C4-3FEB-4599-ACF0-5B2D7B776859', 0, 27506, '', 'Principles of Management (Lecturer Account)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes');
--HE LECTURER PRODUCT 1 LINKED STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', 'ED04D668-427B-4467-B67A-EF25EA52FDEC', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '210806C4-3FEB-4599-ACF0-5B2D7B776859', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '06AC88B5-B8BC-478D-A72D-372EC7C98123', 0, 27502, '', 'Principles of Management', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '210806C4-3FEB-4599-ACF0-5B2D7B776859', 'cw.info@oxfordfajar.com.my', '30 minutes', 'POST_PARENT');

--HE LECTURER PRODUCT 2
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement) values ('REGISTERABLE','1F0D7133-5D06-418F-AEC5-966C387D0CCB', 0, 27505, '', 'Business Statistics (Lecturer Account)', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', null, 'cw.info@oxfordfajar.com.my', '30 minutes');
--HE LECTURER PRODUCT 2 LINKED STUDENT
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '4D54E3E1-203D-407E-B1D7-276A6F0B7AB8', 0, 5901, '', 'OFCW Free Content', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '1F0D7133-5D06-418F-AEC5-966C387D0CCB', 'cw.info@oxfordfajar.com.my', '30 minutes', 'PRE_PARENT');
insert into product (product_type,id,obj_version,erights_id,landing_page, product_name, division_id, registerable_product_id, email, service_level_agreement, activation_method) values ('LINKED', '62D5573E-EC48-429B-B36D-251496A3A3B4', 0, 27504, '', 'Business Statistics', 'bfdc5888-c26b-4ed5-a580-6aa363a25ecd', '1F0D7133-5D06-418F-AEC5-966C387D0CCB', 'cw.info@oxfordfajar.com.my', '30 minutes', 'POST_PARENT');

insert into licence_template (id, obj_version,licence_type,start_date,end_date,total_concurrency,user_concurrency,begin_on,time_period,unit_type,allowed_usages) values ('7E2C938F-799C-4A89-964D-3D93140915CD', 0, 'ROLLING', null, null, null, null, 'FIRST_USE', 2, 'YEAR',NULL);

--TEACHER
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '948B5C2B-0D04-4F2F-BA7A-0BFDB3D0D77A', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'BB530863-D4F8-4C80-BC9C-911D10F480CC', '7E2C938F-799C-4A89-964D-3D93140915CD');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '948B5C2B-0D04-4F2F-BA7A-0BFDB3D0D77A', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);

--HE STUDENT PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', '3FDE1289-4B3D-48DC-97CE-C4CCD67D6B7E', 0, '8DBAF754-0238-4EAB-8D0F-26A7A0D8997B', '119452A7-07BB-4B13-A334-BF44D04EAF57', 'C816B7E7-7138-49A6-84AF-555BEA53B825', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '8DBAF754-0238-4EAB-8D0F-26A7A0D8997B', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);

--HE STUDENT PRODUCT 2
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACTIVATION_CODE_REGISTRATION', '8C99E72E-6E55-4695-A7FC-E7E7A80673C3', 0, '6F9C4166-F1FD-4B0F-B098-C77787675B5A', '119452A7-07BB-4B13-A334-BF44D04EAF57', '13AC20F6-0414-4569-B6F0-D92FC3AE24A0', '552C200A-7FC4-42E6-91FD-730720D02895');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '6F9C4166-F1FD-4B0F-B098-C77787675B5A', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);

---HE LECTURER PRODUCT 1
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '210806C4-3FEB-4599-ACF0-5B2D7B776859', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', '207BFAE7-57CD-4570-BA7B-2F10B7BAA337', '6A0386AC-69AB-4E51-BBB6-8670F303F09C');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '210806C4-3FEB-4599-ACF0-5B2D7B776859', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);

---HE LECTURER PRODUCT 2
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('PRODUCT_REGISTRATION', newId(), 0, '1F0D7133-5D06-418F-AEC5-966C387D0CCB', '166F6F3C-3C0F-46C8-93C5-797BA8F178DF', 'EE67C91C-A753-4C1B-B5CB-F54430C289AA', '6A0386AC-69AB-4E51-BBB6-8670F303F09C');
insert into registration_definition (registration_definition_type,id,obj_version,product_id,registration_activation_id,page_definition_id,licence_template_id) values ('ACCOUNT_REGISTRATION', newId(), 0, '1F0D7133-5D06-418F-AEC5-966C387D0CCB', 'D7FEFBDF-94D9-4D15-9579-4EF312E4054E', '1B037300-852D-45F8-B45F-F02BD0C18C15', null);


-- Generated [7] from  emails_es.properties on 14/Sep/2011 13:48:37
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.dear','Al');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.you','imponerle');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.complete','castigo quer\u00eda');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.after.registration','ejemplarizar con \u00e9l');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.regards','ese');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.team','ejemplarizar con \u00e9l');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages','es',null,null,'label.respond','(imponerle ese castigo quer\u00eda)');
-- Generated [31] from  emails.properties on 14/Sep/2011 13:48:37
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.dear','Dear');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.you','You have registered for');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.hasregistered','has registered for');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.account','Account');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.application','Application');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.allow','To allow access to');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.deny','To deny access to');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.clickhere','click here');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.yousuccess','Thank you for registering for access to');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.unfortunate','We have carefully reviewed your registration details and regret that we cannot approve your access to these resources at this time. We hope you will understand the need to restrict access to these resources to authorized users only.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.yourreg','Thank you for requesting access to');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.regaccepted','We have validated your registration and are pleased to inform you that your request has been accepted.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.wehope','We hope that you enjoy using this website and find it useful.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.ifcomments','If you have any comments or feedback please contact us by email at');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.wewelcome','We welcome your feedback.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.regdenied','has been denied');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.ifrequire','If you feel this to be an error on our part, please do let us know why and we will be happy to look into this for you. You can contact us by email at');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.complete','To complete your registration please click on the following link');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.after.registration','and then log in to access');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.regards','Regards');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.team','The OUP Support Team');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.respond','(Please do not respond to this automatically generated email)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.reset','You have successfully reset your password. Please log in with your user name and the new password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.password.change','You will be required to change your password after you log in.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.yourlicense','Your access starts');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.to','and will end on');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.regprocess','The registration process is now complete and you have access to the resources within the website.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'subject.successfullyactivated','Access to {0} successfully activated.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.duration','and will last for {0} from when you first start using the resources');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.usages','and will last for {0} usage(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'YEAR','year(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'MONTH','month(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'WEEK','week(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'DAY','day(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'HOUR','hour(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'MINUTE','minute(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'SECOND','second(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'MILLISECOND','millisecond(s)');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.now','now');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.on','on');

-- Generated [207] from  messages.properties on 14/Sep/2011 13:48:37
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.activatelicence','Activate Licence');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.changepassword','Change Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.error','Error');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.login','Log in');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.accountregistration','Account Creation');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.productregistration','Product Registration');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registrationsuccess','Registration Success');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.resetpassword','Reset Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.resetpasswordsuccess','Reset Password Success');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.contactus','Contact Us');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.accessibility','Accessibility');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.privacypolicy','Terms, Conditions and Privacy Policy');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.version','EAC Version');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.redeemcode','Redeem Activation Code');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.licencealreadyactive','Licence Already Active');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.validatorlicenceallowed','Licence Request Successful');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.validatorlicencedenied','Licence Request Unsuccessful');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.awaitinglicenceactivation','Awaiting Licence Activation');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'header.logo','Oxford University Press Enterprise Access Control');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'header.title','Enterprise Access Control');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'header.country','Worldwide');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'help.resetpassword','Please enter your username or email address in the box below and press submit. We will email you your password details.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'help.dob','The date of birth must be in the format dd/mm/yyyy e.g. 18/12/1988.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'help.activationcode','Please enter your activation code in the box below and click the Activate button. If you are not already logged in you will be asked to log in or register.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activatelicence','Thank you for completing the registration form. An activation email has been sent to your registered email address. Please check your email and follow the instructions to complete your registration to {0}.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.activatelicenceemail','If your activation email does not arrive within {1} please look in your spam or junk mail folder. If the email is not there please contact {2}.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.confirmpassword','Confirm Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.confirmnewpassword','Confirm New Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.denyreason','Deny Reason');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.personaldetails','Personal Details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.english','English');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.errors','The following errors have occurred:');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.familyname','Surname');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.firstname','First Name');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.language','Language');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.login','Log in');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.marketinginformation','Marketing Information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.mandatory','Field is mandatory');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.newpassword','New Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.password','Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registrationsuccess','You have successfully confirmed your registration.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.productregistration','Product Registration');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registrationAddress','Address');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registrationPosition','Position');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.register','Register');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.product.home','Return to product');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.resetpassword','Reset Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.resetpasswordsuccess','Thank you. If we have your email address in our records, you will be sent an email containing a new password. You will be asked to select a new password the next time you log in. NOTE: You will not receive an email if your email address is not in our records.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.spanish','Spanish');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.username','Username');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.unexpectederror','An unexpected error has occurred.<br/>Please go back and try again.<br/>If this continues to happen please contact the system administrator.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.version','EAC Version');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.redeemcode','Activation Code');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.licencealreadyactive','Licence Already Active');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.validatorlicenceallowed','You have successfully approved');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.awaitinglicenceactivation','Thank you for completing the registration form. We will contact you within a week regarding your access to {0}.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.awaitinglicenceactivationconfirm','As soon as your access has been confirmed you will receive an email notifying you of your access to the site. If your notification email has not arrived within a week, please check for it in your spam or junk mail folder. If the email is not there please contact <a href="mailto:{1}">{1}</a>.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.licenceisdenied','Thank you for requesting access to {0}. Unfortunately your request has been denied. If you require further information please contact eacsystemadmin@oup.com.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.validatorlicencedenied','You have successfully denied');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.touse','to use');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.important','Important Information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.productinfo','Only teachers may register for access to this content. Registration details will be verified before access to Teacher Resources is granted. Please allow one week for verification. Access to Student Resources is immediate upon completing registration.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.lecturer.important.info','Only lecturers may register for access to this content. Registration details will be verified before access is granted. Please allow one week for verification. This Lecturer Account will give access to both Student and Lecturer resources');

insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.customer.header','Personal Details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.customer.sex','Gender');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.sex.male','Male');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.sex.female','Female');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.customer.dob','Date of Birth');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.address.header','Address');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.address.line1','Line 1');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.address.line2','Line 2');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.address.line3','City/Town');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.address.line4','Postcode');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.address.line5','State');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.address.line6','Country');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.my','Malaysia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.sg','Singapore');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.id','Indonesia');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.bn','Brunei');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.ph','Philippines');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.th','Thailand');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.vn','Vietnam');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.country.other','Other');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.registration.address.line1','Institution Address Line 1');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.registration.address.line2','Institution Address Line 2');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.domain.email','Institution Email Address');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.institution.contact.number','Contact Number');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.header','Institution Details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.position','Position');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.position.student','Student');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.position.teacher','Teacher/Lecturer');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution','Institution Type');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.national.primary','National Primary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.national.type.primary','National Type Primary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.private.primary','Private Primary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.national.secondary','National Secondary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.private.secondary','Private Secondary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.form6','Form6/Pre-U');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.matriculation.centre','Matriculation Centre');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.polytechnic','Polytechnic');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.private.college','Private College');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.private.language.school','Private Language School');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.public.university','Public University');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.private.university','Private University');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.other','Other');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.institution.name','Institution Name');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.marketing.header','Marketing Information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.marketing','Oxford University Press Marketing Information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.marketing.optin','I am happy to receive information about products and services from OUP and associated companies');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.marketing.optout','I do not wish to receive any correspondence from OUP and associated companies apart from critical user information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.my.cw.marketing','Oxford Fajar Marketing Information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.my.cw.marketing.optin','I am happy to receive information about related products and services from Oxford Fajar');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.my.cw.marketing.optout','I do not wish to receive any correspondence from Oxford  Fajar apart from critical user information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.lvl.lower.primary','Lower Primary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.lvl.upper.primary','Upper Primary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.lvl.lower.secondary','Lower Secondary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.lvl.upper.secondary','Upper Secondary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.lvl.form.six','Form Six');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.lvl.other','Other');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.courselvl.foundation','Foundation');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.courselvl.certification','Certification');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.courselvl.diploma','Diploma');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.courselvl.professional','Professional/Post Graduate');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.adopted.book','I have adopted this book');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.adopted.consider','I am considering adopting this book');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.adopted.status','Adoption status');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.contact.type.mobile','Mobile');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.contact.type.institution','Institution');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.contact.type.home','Home');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.contact.number.type','Contact Number Type');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.education.faculty.department','Faculty/ Department');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.tandc.header','Terms and Conditions');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.preferred.contact','Preferred Contact Details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.tandc','I have read and agreed to Oxford University Press''''s Terms and Conditions for Account Creation and Privacy Policy');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.tandc.link','Terms and Conditions');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.my.cw.tandc','I have read and agreed to the Terms and Conditions of the Oxford Fajar Companion Website');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.my.cw.tandc.link','Terms and Conditions');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.subjects.taught','Subjects Taught');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.level','Level');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.school.details','School Details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.institution.details','Institution Details');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.course.info','Course Information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'label.registration.book.adoption','Book Adoption');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.password','Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.confirmpassword','Confirm Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.confirmnewpassword','Confirm New Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.newpassword','New Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.username','Username');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.familyname','Last Name');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.firstname','First Name');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.customer.sex','Gender');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.customer.dob','Date of Birth');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.address.line1','Line 1');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.address.line2','Line 2');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.address.line3','City/Town');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.address.line4','Postcode');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.address.line5','State');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.address.line6','Country');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.institution.registration.address.line1','Institution Address Line 1');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.institution.registration.address.line2','Institution Address Line 2');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.institution.domain.email','Institution Email Address');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.institution.contact.number','Contact Number');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.education.position','Position');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.education.institution','Institution Type');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.education.institution.name','Institution Name');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.marketing','Preference on receiving Oxford University Press Marketing Information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.tandc','Terms and Conditions acceptance');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.tandc.link','Terms and Conditions');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.my.cw.tandc','Terms and Conditions acceptance');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.my.cw.tandc.link','Terms and Conditions');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.subjects.taught','Subjects Taught');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.level','Level');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.lvl.lower.primary','Lower Primary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.lvl.upper.primary','Upper Secondary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.lvl.lower.secondary','Lower Secondary');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.lvl.form.six','Form Six');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.contact.number.type','Contact Number Type');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.adopted.status','Adoption status');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.registration.education.faculty.department','Faculty/Department');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'title.productinfo','Important Information');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'info.username','Your username must be a valid email address.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'info.password.strength','Your password must be from 6 to 15 characters long and it must contain at least one lower case character, one upper case character and at least one digit. No other characters are permitted.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'info.redeem.code','Please enter your activation code');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'info.institution.domain.email','If provided, will help to expedite validation process');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'submit.login','Log in');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'submit.accountregistration','Create Account');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'submit.productregistration','Register');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'submit.resetpassword','Reset Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'submit.changepassword','Change Password');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'submit.redeemcode','Activate');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.notthesame','{0} and {1} are not the same');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.regularexpression','{0} is invalid');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.not-specified','{0} is required');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.password.strength','Your password must be from 6 to 15 characters long and it must contain at least one lower case character, one upper case character and at least one digit. No other characters are permitted.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.must.be.valid.email','{0} must be a valid email address.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.must.be.same','{0} and {1} must be the same.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.please.check.email','Please check your email.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.problem.with.login.credentials','There was a problem with your login credentials.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.username.taken','This username is already taken. Please <a href="{0}">login</a> to use your existing account or try another username.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.problem.session.concurrency','Sorry. We could not log you in. Please try again later.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.problem.creating.customer','There was a problem creating your account. Please contact the system administrator.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.problem.resetting.password','There was a problem resetting your password. Please contact the system administrator.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.problem.changing.password','There was a problem changing your password. Please contact the system administrator.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.problem.registering.product','There was a problem registering the product. Please contact the system administrator.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.changing.customer.password','There was a problem changing your password. Please contact the system administrator.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.aquiring.product.information','There was a problem with your product. Please go back to the product page and choose the item again.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.logging.out.customer','There was a problem');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'error.problem.activating.token','There was a problem with your activation code.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'footer.copyright','Copyright &copy; Oxford University Press, {0}. All Rights Reserved.');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'footer.contactus','Contact Us');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'footer.help','Help &amp; Support');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'footer.accessibility','Accessibility');
insert into message (id,basename,language,country,variant,[key],message) values (newId(), 'messages',null,null,null,'footer.privacypolicy','Terms, Conditions and Privacy Policy');

-- These 5 rows are Quartz Reference Data - they are not EAC specific -- David Hay
INSERT INTO [QRTZ_LOCKS] VALUES('TRIGGER_ACCESS');
INSERT INTO [QRTZ_LOCKS] VALUES('JOB_ACCESS');
INSERT INTO [QRTZ_LOCKS] VALUES('CALENDAR_ACCESS');
INSERT INTO [QRTZ_LOCKS] VALUES('STATE_ACCESS');
INSERT INTO [QRTZ_LOCKS] VALUES('MISFIRE_ACCESS');

insert into url_skin (id,obj_version,url,skin_path) values (newId(),0,'http://dev.eac.uk.oup.com','skin/oxfordfajar/cw/css/eac-override.css');
insert into url_skin (id,obj_version,url,skin_path) values (newId(),0,'http://localhost','skin/oxfordfajar/cw/css/eac-override.css');

