--MIGRATE PRODCUT WITH ERIGHTS PRODUCT

ALTER TABLE [DIVISION] ADD [ERIGHTS_ID] [INT] DEFAULT NULL

ALTER TABLE PRODUCT ADD STATE NVARCHAR(25), PRODUCT_NAME NVARCHAR(255), ERIGHTS_ID INT NULL,PRODUCT_ID NVARCHAR(255)

ALTER TABLE [PRODUCT] ADD [division_id] [nvarchar](255) DEFAULT NULL

CREATE INDEX PRODUCT_ERIGHTS_ID_IDX
	ON DBO.PRODUCT (ERIGHTS_ID)


--
--Above Query all ready run on test and dev env
--Above Query all ready run on test and dev env
--Above Query all ready run on test and dev env
--

/*
 * Script for US112-TA359 : division duplication ddl scripts 
 */
--US112/TA335 : Migrate division erights id in ersmd_product_info_data table
ALTER TABLE product ADD division_erights_id INT;


--US112/TA336 : Migrate division for Admin User
ALTER TABLE division_admin_user ADD division_erights_id INT NULL;

--US112/TA337 : Migrate division for Page Definnition
ALTER TABLE page_definition ADD division_erights_id INT NULL;


-- white list urls 
CREATE TABLE white_list_urls (
    id nvarchar(255) NOT NULL PRIMARY KEY,
	obj_version numeric,
    url nvarchar(2000) NOT NULL,
    date_created datetime,
    date_updated datetime,
    date_deleted datetime
	
);

--Batch Job
CREATE TABLE BATCH_JOB_TIME
(
BATCH_JOB_ID INT PRIMARY KEY,
BATCH_JOB_NAME VARCHAR(25),
LAST_JOB_EXECUTE_DATE	DATETIME,
LAST_SUCCESS_DATE DATETIME,
LAST_JOB_STATUS VARCHAR(10),
LAST_JOB_MESSAGE VARCHAR(100)
);