-- Insert external system for Konakart 
INSERT INTO external_system
           ([id], obj_version, name, description)
     VALUES ('23C9F2EC-F568-4795-A96B-47ECAA725CCB', 0, 'Konakart', 'Konakart system');
GO

------------------------------------------
-- Insert external system id type for Konakart
INSERT INTO external_system_id_type
           ([id], obj_version, name, description, external_system_id)
     VALUES(NEWID(), 0, 'kkid', 'Konakart Id', '23C9F2EC-F568-4795-A96B-47ECAA725CCB');
GO
