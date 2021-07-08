sp_RENAME 'registration_activation.iso_country_list', 'locale_list', 'COLUMN';
GO

update registration_activation set locale_list = 'en_AU,zh_CN,zh_HK,en_NZ,tr_TR' where locale_list = 'AU,CN,HK,NZ,TR';
GO