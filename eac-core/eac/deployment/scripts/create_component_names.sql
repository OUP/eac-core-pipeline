alter table component add name nvarchar(255) not null default '';
GO

update component set name = 'ORCS Please note' where id = '9619c39c-d9fa-4e51-b19f-cdf4a00c81d2';
update component set name = 'OXED Please note' where id = 'B073ECBD-BA50-49DA-961F-D3F2F15D2567';
update component set name = 'OFCW Admin Important Information' where id = '19D05445-7667-434D-97DC-5AB260ECF5D2';
update component set name = 'Important Information' where id = '52C5021C-5334-41BD-873D-63F64236CFDF';
update component set name = 'Lecturer Course Details' where id = '3477403f-8768-4340-be96-cabc3938de85';
update component set name = 'Student Course Details' where id = 'b40d6642-31ec-486c-875d-17a811c1eb07';
update component set name = 'Student Your Personal Details' where id = '06a055f6-25ab-41a5-bb18-8ee1374e6b15';
update component set name = 'Lecturer Your Personal Details' where id = '79e3c04b-8317-4d4a-b1a5-213ecc4973f6';
update component set name = 'OFCW Student Address' where id = '21054566-EF39-4E7A-ACA0-F544EA860B6C';
update component set name = 'OFCW Personal Details' where id = '0241481B-66A8-4591-BFF3-5873F6CB4BC5';
update component set name = 'OTC Personal Details' where id = 'CD0E27F2-3C08-471A-959F-929913FC0F0B';
update component set name = 'OFCW Institution Details' where id = '9921178D-2B92-4696-8215-CB8F3BAD2E97';
update component set name = 'Contact Information' where id = '69323313-B3B4-46C5-A4A2-B501B9583829';
update component set name = 'OFCW Contact Information' where id = '87782783-21DE-4301-B208-43DF5F62CAAA';
update component set name = 'OUP Terms and Conditions' where id = '328D4DCF-E39B-4BC8-9900-92EEBB82D30F';
update component set name = 'Terms and Conditions' where id = '70755604-3a35-4a90-acff-127bb68767cc';
update component set name = 'ORCS Terms and Conditions' where id = '904e00c1-fc30-4891-9d32-399740b2e28e';
update component set name = 'OXED Terms and Conditions' where id = 'C767C62F-BC09-4868-B9F4-FD5B9B2306D4';
update component set name = 'OTC Terms and Conditions' where id = 'CDB6784D-AB31-49AF-873A-487F01468BF0';
update component set name = 'ORCS Employee Details' where id = 'dc97f5ea-f962-4ea1-ad56-ff60ee2d6876';
update component set name = 'ORCS Institution Details' where id = 'b5931730-97e9-4416-9e09-f88d8e8ff785';
update component set name = 'OFCW Terms and Conditions' where id = '046A7718-0D2C-4827-894A-03CBEBF00A19';
update component set name = 'Terms and Conditions 3' where id = '3804D5C4-3AAB-4BD8-9E00-15CFB6A3B53F';
update component set name = 'Terms and Conditions 4' where id = 'D0A14C49-11A3-4259-801E-5AC4F151BB31';
update component set name = 'Terms and Conditions 5' where id = 'DC08E959-FE41-4272-A507-479B45C59D8F';
update component set name = 'Terms and Conditions 6' where id = 'F7F40991-366B-408B-A36B-3D11AA624F03';
update component set name = 'Lecturer Important Information' where id = '5EBFE7BE-2865-4EAE-B2AE-C9B9163BA562';
update component set name = 'Book Adoption' where id = 'A988F159-4220-4EDE-95CA-D9DA31CCB6A9';
GO

update component set name = (select message from message where [key] = 'label.registration.subjects.taught') where id = '1852260F-52A5-4D9D-B6BC-6A086735ED67';
update component set name = (select message from message where [key] = 'label.registration.tandc.header') where id = '2F80BC31-F23C-4F03-89D2-CA237D9A3615';
update component set name = (select message from message where [key] = 'label.registration.tandc.header') where id = '356761EB-CA0D-475F-AB04-31C9D29DA59D';
update component set name = (select message from message where [key] = 'label.registration.tandc.header') where id = '37B694B0-EC9B-4824-BF99-ABDC0BD95363';
update component set name = (select message from message where [key] = 'label.registration.course.info') where id = '49CEF46A-58AA-4EE5-8F7B-BC26BF80243F';
update component set name = (select message from message where [key] = 'label.registration.course.info') where id = '4CE07B42-7409-4EB9-959E-EDBAB5C5299C';
update component set name = (select message from message where [key] = 'label.registration.book.adoption') where id = '5B09470C-99E2-4696-A713-E47C0B9FAAC0';
update component set name = (select message from message where [key] = 'label.registration.employee.info') where id = '5B3DBC40-F988-4163-BC8B-E12D39AF8794';
update component set name = (select message from message where [key] = 'label.registration.institution.details') where id = '718256F8-9DA0-4508-BC7E-8819BD53BD89';
update component set name = (select message from message where [key] = 'label.registration.tandc.header') where id = '79ADF4FB-2057-43C7-AF56-2DD2B07B8604';
update component set name = (select message from message where [key] = 'label.registration.book.adoption') where id = '79DFC90C-909C-479A-899F-1AB773D50F79';
update component set name = (select message from message where [key] = 'label.registration.your.details') where id = 'A3F2C812-E2AC-4DC4-8B60-0FA6066FD07B';
update component set name = (select message from message where [key] = 'label.registration.tandc.header') where id = 'AC42A956-3A8F-4F42-8C90-AFBCB0F3EDC7';
update component set name = (select message from message where [key] = 'label.important') where id = 'ACD010C3-599D-45A5-9861-9EDDEF1D528C';
update component set name = (select message from message where [key] = 'label.registration.tandc.header') where id = 'C3DD0478-3153-49D5-A22C-B4EB16CB20C2';
update component set name = (select message from message where [key] = 'label.preferences') where id = 'CAD9A9DF-9D7A-49E3-8657-4107E3464A09';
update component set name = (select message from message where [key] = 'label.registration.tandc.header') where id = 'D0B9C64B-0CA8-4CF7-A0A8-A8B1CA4CE1C4';
update component set name = (select message from message where [key] = 'label.registration.school.details') where id = 'D49D362F-CA5D-4448-B4EC-00747D9BD2FA';
update component set name = (select message from message where [key] = 'label.registration.course.info') where id = 'DAFC81DD-FE44-4325-AAF3-102614EBE1A0';
update component set name = (select message from message where [key] = 'label.important') where id = 'FB649B6E-EACF-434F-932F-C0C1C0B79EC0';
update component set name = (select message from message where [key] = 'label.registration.tandc.header') where id = 'FFBFCFBD-35CD-4652-996C-607299EA1B8A';
update component set name = '(please set me)' where name = '';
GO

update page_component set component_id = '046A7718-0D2C-4827-894A-03CBEBF00A19' where component_id = '3804D5C4-3AAB-4BD8-9E00-15CFB6A3B53F';
delete from field where component_id = '3804D5C4-3AAB-4BD8-9E00-15CFB6A3B53F';
delete from component where id = '3804D5C4-3AAB-4BD8-9E00-15CFB6A3B53F';

update page_component set component_id = '046A7718-0D2C-4827-894A-03CBEBF00A19' where component_id = 'D0A14C49-11A3-4259-801E-5AC4F151BB31'
delete from field where component_id = 'D0A14C49-11A3-4259-801E-5AC4F151BB31';
delete from component where id = 'D0A14C49-11A3-4259-801E-5AC4F151BB31';

update page_component set component_id = '046A7718-0D2C-4827-894A-03CBEBF00A19' where component_id = 'DC08E959-FE41-4272-A507-479B45C59D8F'
delete from field where component_id = 'DC08E959-FE41-4272-A507-479B45C59D8F';
delete from component where id = 'DC08E959-FE41-4272-A507-479B45C59D8F';

update page_component set component_id = '046A7718-0D2C-4827-894A-03CBEBF00A19' where component_id = 'F7F40991-366B-408B-A36B-3D11AA624F03'
delete from field where component_id = 'F7F40991-366B-408B-A36B-3D11AA624F03';
delete from component where id = 'F7F40991-366B-408B-A36B-3D11AA624F03';

GO