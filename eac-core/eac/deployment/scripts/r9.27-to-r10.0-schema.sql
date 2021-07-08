alter table division add created_date datetime2 not null default getdate();
GO

alter table asset add constraint asset_product_name_unq unique (product_name);
GO

