if db_id('eactest') is not null
begin
    USE master ALTER DATABASE eactest SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    USE master DROP DATABASE eactest;
end
USE master CREATE DATABASE eactest;
USE master ALTER DATABASE eactest SET MULTI_USER WITH ROLLBACK IMMEDIATE;

