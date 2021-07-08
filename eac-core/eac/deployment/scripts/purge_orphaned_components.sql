delete from field where component_id in (select c.id from component c left outer join page_component pc on c.id = pc.component_id where pc.component_id is null);

delete from component where id in (select c.id from component c left outer join page_component pc on c.id = pc.component_id where pc.component_id is null);

GO
