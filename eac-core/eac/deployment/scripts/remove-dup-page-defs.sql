update registration_definition set page_definition_id = '13AC20F6-0414-4569-B6F0-D92FC3AE24A0' where page_definition_id in 
('0659605B-6E7B-4B92-AFD7-7B6AE174B609', 
'4d1e9e1c-074c-4017-bb2e-587357190611', 
'724c50c2-6784-4695-b2fb-1970f189639e', 
'73feba2a-f2c6-42b3-85f8-c3c0abcc33ee', 
'a171ed6b-7775-45f1-ace7-7f23949ac263',
'a72edcbd-9ee4-4829-a2c2-14316cf5d92b',
'ba0af376-b1e8-44a4-9b9c-e10a8506c294');

delete from page_component where page_definition_id in 
('0659605B-6E7B-4B92-AFD7-7B6AE174B609', 
'4d1e9e1c-074c-4017-bb2e-587357190611', 
'724c50c2-6784-4695-b2fb-1970f189639e', 
'73feba2a-f2c6-42b3-85f8-c3c0abcc33ee', 
'a171ed6b-7775-45f1-ace7-7f23949ac263',
'a72edcbd-9ee4-4829-a2c2-14316cf5d92b',
'ba0af376-b1e8-44a4-9b9c-e10a8506c294');

delete from page_definition where id in
('0659605B-6E7B-4B92-AFD7-7B6AE174B609', 
'4d1e9e1c-074c-4017-bb2e-587357190611', 
'724c50c2-6784-4695-b2fb-1970f189639e', 
'73feba2a-f2c6-42b3-85f8-c3c0abcc33ee', 
'a171ed6b-7775-45f1-ace7-7f23949ac263',
'a72edcbd-9ee4-4829-a2c2-14316cf5d92b',
'ba0af376-b1e8-44a4-9b9c-e10a8506c294');

update registration_definition set page_definition_id = '207BFAE7-57CD-4570-BA7B-2F10B7BAA337' where page_definition_id in 
('06E89AEB-808B-414E-B9CE-6C9C7CEC6848', 
'60d28576-b5d7-45fb-9f6b-31061ebd7b02', 
'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24', 
'ba6819be-e297-4bba-955f-a3cb8ec30648');

delete from page_component where page_definition_id in 
('06E89AEB-808B-414E-B9CE-6C9C7CEC6848', 
'60d28576-b5d7-45fb-9f6b-31061ebd7b02', 
'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24', 
'ba6819be-e297-4bba-955f-a3cb8ec30648');

delete from page_definition where id in
('06E89AEB-808B-414E-B9CE-6C9C7CEC6848', 
'60d28576-b5d7-45fb-9f6b-31061ebd7b02', 
'AE0B281A-8F9C-43A7-9B6D-84C3E570FE24', 
'ba6819be-e297-4bba-955f-a3cb8ec30648');