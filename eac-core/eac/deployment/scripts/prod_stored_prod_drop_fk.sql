use eacprod;

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'eac_drop_fk') --AND type in (N'SP')
)
  DROP  procedure [dbo].eac_drop_fk
GO

CREATE PROC eac_drop_fk
  @from nvarchar(255),
  @to   nvarchar(255)
AS
BEGIN 

declare @fkName nvarchar(255);
declare @dropFkSQL nvarchar(255);
declare @getFkNameSql nvarchar(2000);

set @getFkNameSql=
'SELECT @fkName = f.name
   --,OBJECT_NAME(f.parent_object_id) AS TableName
   --,COL_NAME(fc.parent_object_id, fc.parent_column_id) AS ColumnName
   --,OBJECT_NAME (f.referenced_object_id) AS ReferenceTableName
   --,COL_NAME(fc.referenced_object_id, fc.referenced_column_id) AS ReferenceColumnName
FROM sys.foreign_keys AS f
INNER JOIN sys.foreign_key_columns AS fc
   ON f.OBJECT_ID = fc.constraint_object_id
where OBJECT_NAME(f.parent_object_id) ='''+@from+'''
and   OBJECT_NAME (f.referenced_object_id)='''+@to+'''';

--print 'sql is ' + @getFkNameSql;

--print 'BEFORE';

exec sp_executesql @getFkNameSql,N'@fkName nvarchar(255) output', @fkName out
--print @fkName
--print 'AFTER';
--print 'FK from'+@from+'to'+@to+'is'+@fkName;
--print 'FK from '+@from+' to '+@to+' is ' + @fkName;
--print 'AFTER1';

IF @fkName is not null AND @fkName != ''
BEGIN
  set @dropFkSQL =  'alter table ' + @from + ' drop ' + @fkName;

  print 'DROP FK SQL : ' + @dropFkSQL;

  exec (@dropFkSQL)
END
ELSE
BEGIN
  print 'NO FOREIGN KEY FOUND between ' + @from + ' and ' + @to;
END

END;
