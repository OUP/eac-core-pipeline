CREATE FUNCTION eac_role_tokenizer (@list nvarchar(MAX))
   RETURNS @tbl TABLE (token nvarchar(255) NOT NULL) AS
BEGIN
   DECLARE @pos        int,
           @nextpos    int,
           @valuelen   int

   SELECT @pos = 0, @nextpos = 1

   WHILE @nextpos > 0
   BEGIN
      SELECT @nextpos = charindex(',', @list, @pos + 1)
      SELECT @valuelen = CASE WHEN @nextpos > 0
                              THEN @nextpos
                              ELSE len(@list) + 1
                         END - @pos - 1
      declare @tokenCode nvarchar(255);
      declare @token nvarchar(255);
      set @tokenCode = substring(@list, @pos + 1, @valuelen);
      
      set @token = (SELECT CASE rtrim(ltrim(@tokenCode))
        WHEN 'SA' THEN 'SYSTEM_ADMIN'
        WHEN 'DA' THEN 'DIVISION_ADMIN'
        WHEN 'PC' THEN 'PRODUCTION_CONTROLLER'
        WHEN 'PO' THEN 'PUBLICATIONS_OFFICER'
        WHEN 'FR' THEN 'FIELD_REP'
        ELSE null
        END
        );
        
      IF @token is not null
      BEGIN  
        declare @tbl_count int;
        set @tbl_count = (select count(*) from @tbl where token = @token);
        if @tbl_count = 0
            INSERT @tbl (token) VALUES (@token)
      END;
      SELECT @pos = @nextpos
   END
   RETURN
END
