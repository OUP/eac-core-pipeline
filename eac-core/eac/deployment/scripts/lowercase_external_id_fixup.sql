-- Force lowercase or external_system and external_system_id_type in response to mantis ticket http://mantisoup.idm.fr/view.php?id=14858

declare @count int
select @count = count(*) from external_system where id='9c318f21-c559-4f9b-a2cb-461f7ec94e8f'

IF @count = 1
BEGIN
	update EXTTYP
	set EXTTYP.external_system_id='8af2bf0f395335920139553d31240054'
	from external_system EXTSYS
	join external_system_id_type EXTTYP on EXTTYP.external_system_id = EXTSYS.id
	where EXTTYP.id='a493d026-ddf3-4a9e-b307-c92822ffa5d6'


	delete from external_system where id='9c318f21-c559-4f9b-a2cb-461f7ec94e8f'
END
ELSE
BEGIN
	print 'no external_system to process'
END
GO
IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'TRG_LOWER_NAME_EXT_SYS'))
  DROP  TRIGGER [dbo].TRG_LOWER_NAME_EXT_SYS
GO

CREATE TRIGGER TRG_LOWER_NAME_EXT_SYS
ON external_system
INSTEAD OF INSERT, UPDATE
AS 
BEGIN
	
	IF (NOT EXISTS(SELECT EXT.id from external_system EXT, inserted INS where INS.id = EXT.id))
    BEGIN

        INSERT INTO external_system
		(id,obj_version,name,description)		
            SELECT  INS.id,
                    INS.obj_version,
                    lower(INS.name),
                    INS.description
            FROM    inserted INS                

    END
    ELSE BEGIN

        UPDATE  EXT_SYS		
        SET     id          = INS.id,
                obj_version = INS.obj_version,
				name        = lower(INS.name),
				description = INS.description		
		from external_system EXT_SYS, inserted INS
        WHERE   EXT_SYS.id = INS.id

    END

END	
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'TRG_LOWER_NAME_EXT_SYS_ID_TYP'))
  DROP  TRIGGER [dbo].TRG_LOWER_NAME_EXT_SYS_ID_TYP
GO

CREATE TRIGGER TRG_LOWER_NAME_EXT_SYS_ID_TYP
ON external_system_id_type
INSTEAD OF INSERT, UPDATE
AS 
BEGIN
	
	IF (NOT EXISTS(SELECT EXT.id from external_system_id_type EXT, inserted INS where INS.id = EXT.id))
    BEGIN

        INSERT INTO external_system_id_type
		(id,obj_version,name,description,external_system_id)		
            SELECT  INS.id,
                    INS.obj_version,
                    lower(INS.name),
                    INS.description,
					INS.external_system_id
            FROM    inserted INS                

    END
    ELSE BEGIN

        UPDATE  EXT_SYS_ID_TYPE		
        SET     id          = INS.id,
                obj_version = INS.obj_version,
				name        = lower(INS.name),
				description = INS.description,
				external_system_id = INS.external_system_id
		from external_system_id_type EXT_SYS_ID_TYPE, inserted INS
        WHERE   EXT_SYS_ID_TYPE.id = INS.id

    END

END	
GO

alter table external_system
add constraint UQ_external_system_name unique(name)

alter table external_system_id_type
add constraint UQ_external_system_id_type_name unique(external_system_id,name)

update external_system set name = name;

update external_system_id_type set name = name;


