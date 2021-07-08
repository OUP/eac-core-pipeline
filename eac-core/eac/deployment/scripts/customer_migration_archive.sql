use eactest;
--
-- Author David Hay.
--
-- Only for testing. NOT PRODUCTION.
-- Used to archive a copy the data from the 3 customer_migration tables.
-- This allows the same ids to be migrated multiple times ( if they are given different usernames each time ) -- [was more important with books_erights_id instead of ids holding UUIDs]
 
declare @archive_number int;
declare @check int;

set @archive_number=(select ISNULL(MAX(archive_number), 0)+1 from arch_customer_migration_data);
set @check = (select COUNT(*) from arch_customer_migration_data where archive_number = @archive_number);
if @check > 0
begin
print 'not creating archive ' + cast(@archive_number as nvarchar) + '. it already exists';
end
else
begin

delete from arch_customer_migration_warning where archive_number = @archive_number;
delete from arch_customer_migration where archive_number = @archive_number;
delete from arch_customer_migration_data where archive_number = @archive_number;

print 'inserting into arch_customer_migration_data'
insert into arch_customer_migration_data
        (archive_number,
        id,
        address_line1,
        address_line2,
        address_line3,
        address_line4,
        address_line5,
        address_line6,
        email,
        exam_interests,
        first_name,
        institution_name,
        interests,
        jp_campus,
        jp_clubs,
        jp_dept,
        jp_institution_type,
        jp_no_students,
        jp_public_private,
        last_login,
        last_name,
        marketing,
        password,
        profession,
        profession_other,
        referral,
        teaching_interests,
        username,
        teaching_status,
        created_date,
        ko_num_students,
        ko_inst_type,
        ko_mobile_num,
        ko_locale
        )
SELECT 
        @archive_number,
        id,
        address_line1,
        address_line2,
        address_line3,
        address_line4,
        address_line5,
        address_line6,
        email,
        exam_interests,
        first_name,
        institution_name,
        interests,
        jp_campus,
        jp_clubs,
        jp_dept,
        jp_institution_type,
        jp_no_students,
        jp_public_private,
        last_login,
        last_name,
        marketing,
        [password],
        profession,
        profession_other,
        referral,
        teaching_interests,
        username,
        teaching_status,
        created_date, 
        ko_num_students,
        ko_inst_type,
        ko_mobile_num,
        ko_locale
        from customer_migration_data;
        
print 'inserting into arch_customer_migration'
insert into arch_customer_migration 
        (archive_number,
        id,
        obj_version,
        migration_state,
        eac_customer_id,        
        eac_registration_id,
        created_date,
        updated_date)
select @archive_number,
        id,
        obj_version,
        migration_state,
        eac_customer_id,
        eac_registration_id,
        created_date,
        updated_date from customer_migration;

print 'inserting into arch_customer_migration_warning'
insert into arch_customer_migration_warning
(archive_number,
        cmd_id,
        obj_version,
        message,
        bad_value,
        created_date)
select  @archive_number,
        cmd_id,
        obj_version,        
        message,
        bad_value,
        created_date
        from customer_migration_warning;        
        
print 'deleting from customer_migration_warning';
delete from customer_migration_warning;

print 'deleting from customer_migration';
delete from customer_migration;

print 'deleting from customer_migration_data';
delete from customer_migration_data;

end;
