package com.oup.eac.data.migrationtool;

import java.util.List;
import java.util.Map;

import com.oup.eac.data.BaseDao;
import com.oup.eac.domain.migrationtool.RegistrationMigrationData;
 
public interface RegistrationMigrationDataDao extends BaseDao<RegistrationMigrationData, String>{
 
	Map getProductIds(List<String> productName);
	void updateProductAndDefinitionId(Map<String,String> mapOfProdDtls);
	
	int createRegirstionMigrationRecords(String customerMigrationId);
}
   