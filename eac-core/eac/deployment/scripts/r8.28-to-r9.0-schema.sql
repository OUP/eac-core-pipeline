alter table activation_code_batch add updated_date datetime2 null;
GO

alter table licence_template add updated_date datetime2 null, created_date datetime2 not null default getdate();
GO

ALTER TABLE activation_code_batch ALTER COLUMN licence_template_id nvarchar(255) NOT NULL;
GO
