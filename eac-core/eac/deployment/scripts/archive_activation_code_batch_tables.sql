IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'arc_activation_code') 
)
  DROP TABLE [dbo].arc_activation_code
GO

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'arc_activation_code_batch') 
)
  DROP TABLE [dbo].arc_activation_code_batch
GO

CREATE TABLE arc_activation_code_batch (
	[id] [nvarchar](255) NOT NULL,
	[obj_version] [numeric](19, 0) NOT NULL,
	[activation_code_format] [nvarchar](255) NULL,
	[batch_id] [nvarchar](255) NOT NULL,
	[code_prefix] [nvarchar](255) NULL,
	[created_date] [datetime2](7) NULL,
	[start_date] [datetime2](7) NULL,
	[end_date] [datetime2](7) NULL,
	[activation_code_registration_definition_id] [nvarchar](255) NULL,  --foreign key?
	[licence_template_id] [nvarchar](255) NOT NULL,                     --foreign key?
	[updated_date] [datetime2](7) NULL,
	
	[archive_date] [datetime2](7) NOT NULL
	)
GO
	
ALTER TABLE arc_activation_code_batch
ADD PRIMARY KEY (id)

ALTER TABLE arc_activation_code_batch
add CONSTRAINT arc_acb_batch_id UNIQUE (batch_id)

ALTER TABLE arc_activation_code_batch
ADD CONSTRAINT fk_aacb_to_reg_def
FOREIGN KEY (activation_code_registration_definition_id)
REFERENCES registration_definition(id)

ALTER TABLE arc_activation_code_batch
ADD CONSTRAINT fk_aacb_to_lic_temp
FOREIGN KEY (licence_template_id)
REFERENCES licence_template(id)


CREATE TABLE [dbo].[arc_activation_code](
	[id] [nvarchar](255) NOT NULL,
	[obj_version] [numeric](19, 0) NOT NULL,
	[actual_usage] [int] NULL,
	[allowed_usage] [int] NULL,
	[code] [nvarchar](255) NULL,
	[activation_code_batch_id] [nvarchar](255) NOT NULL,                 --foreign key?
	
	[archive_date] [datetime2](7) NOT NULL
)
GO
ALTER TABLE arc_activation_code
ADD PRIMARY KEY (id)

ALTER TABLE arc_activation_code
add CONSTRAINT arc_ac_code UNIQUE (code)

ALTER TABLE arc_activation_code
ADD CONSTRAINT fk_aac_to_aacb
FOREIGN KEY (activation_code_batch_id)
REFERENCES arc_activation_code_batch(id)

create index arc_activation_code_idx on activation_code (code);
