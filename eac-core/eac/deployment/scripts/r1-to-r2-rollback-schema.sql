-- rollback product table
alter table activation_code_batch drop constraint activation_code_batch_batch_id_unique;
alter table product alter column landing_page nvarchar(100) null;
alter table product drop column home_page;

alter table QRTZ_TRIGGERS drop constraint FK_QRTZ_TRIGGERS_QRTZ_JOB_DETAILS;

alter table QRTZ_TRIGGER_LISTENERS drop constraint FK_QRTZ_TRIGGER_LISTENERS_QRTZ_TRIGGERS;

alter table QRTZ_SIMPLE_TRIGGERS drop constraint FK_QRTZ_SIMPLE_TRIGGERS_QRTZ_TRIGGERS;

alter table QRTZ_JOB_LISTENERS drop constraint FK_QRTZ_JOB_LISTENERS_QRTZ_JOB_DETAILS;

alter table QRTZ_CRON_TRIGGERS drop constraint FK_QRTZ_CRON_TRIGGERS_QRTZ_TRIGGERS;

alter table QRTZ_TRIGGERS drop constraint PK_QRTZ_TRIGGERS;

alter table QRTZ_TRIGGER_LISTENERS drop constraint PK_QRTZ_TRIGGER_LISTENERS;

alter table QRTZ_JOB_LISTENERS drop constraint PK_QRTZ_JOB_LISTENERS;

alter table QRTZ_JOB_DETAILS drop constraint PK_QRTZ_JOB_DETAILS;

alter table QRTZ_LOCKS drop constraint PK_QRTZ_LOCKS;

alter table QRTZ_SCHEDULER_STATE drop constraint PK_QRTZ_SCHEDULER_STATE;

alter table QRTZ_PAUSED_TRIGGER_GRPS drop constraint PK_QRTZ_PAUSED_TRIGGER_GRPS;

alter table QRTZ_FIRED_TRIGGERS drop constraint PK_QRTZ_FIRED_TRIGGERS;

alter table QRTZ_CRON_TRIGGERS drop constraint PK_QRTZ_CRON_TRIGGERS;

alter table QRTZ_CALENDARS drop constraint PK_QRTZ_CALENDARS;




drop table QRTZ_TRIGGERS;

drop table QRTZ_TRIGGER_LISTENERS;

drop table QRTZ_BLOB_TRIGGERS;

drop table QRTZ_SIMPLE_TRIGGERS;

drop table QRTZ_JOB_LISTENERS;

drop table QRTZ_JOB_DETAILS;

drop table QRTZ_LOCKS;

drop table QRTZ_SCHEDULER_STATE;

drop table QRTZ_PAUSED_TRIGGER_GRPS;

drop table QRTZ_FIRED_TRIGGERS;

drop table QRTZ_CRON_TRIGGERS;

drop table QRTZ_CALENDARS;

drop table quartz_log_entry;

drop table url_skin;

drop table message;



    