SET IGNORECASE TRUE;

CREATE TABLE arc_activation_code_batch
(
id VARCHAR(255) NOT NULL,
obj_version  NUMERIC(12) NOT NULL,
activation_code_format VARCHAR(255) NULL,
batch_id VARCHAR(255) NOT NULL,
code_prefix VARCHAR(255) NULL,
created_date TIMESTAMP NULL,
start_date TIMESTAMP NULL,
end_date TIMESTAMP NULL,
activation_code_registration_definition_id VARCHAR(255) NULL,
licence_template_id VARCHAR(255) NOT NULL,
updated_date TIMESTAMP NULL,	
archive_date TIMESTAMP NOT NULL
);

CREATE TABLE arc_activation_code
(
id VARCHAR(255) NOT NULL,
obj_version NUMERIC(12) NOT NULL,
actual_usage numeric(7) NULL,
allowed_usage numeric(7) NULL,
code VARCHAR(255) NULL,
activation_code_batch_id VARCHAR(255) NOT NULL,	
archive_date TIMESTAMP NOT NULL
);
