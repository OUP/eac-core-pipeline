package com.oup.eac.data.migrationtool.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.impl.hibernate.HibernateBaseDao;
import com.oup.eac.data.migrationtool.RegistrationMigrationDataDao;
import com.oup.eac.data.migrationtool.util.ProductMigrationDtls;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;
import com.oup.eac.domain.migrationtool.CustomerMigration;
import com.oup.eac.domain.migrationtool.RegistrationMigration;
import com.oup.eac.domain.migrationtool.RegistrationMigrationData;


@Repository(value="registrationMigrationDataDao")
public class RegistrationMigrationDataDaoImpl extends HibernateBaseDao<RegistrationMigrationData, String> implements RegistrationMigrationDataDao {

	
	@Autowired
	public RegistrationMigrationDataDaoImpl(SessionFactory sf) {
		super(sf);
	}

	
	public Map getProductIds(List<String> productName){
		Session session=getSession();
		Map<String,String> mapOfProdDtls=new HashMap<String, String>();
		String queryStrJoin="select  rd from RegistrationDefinition rd	" +
				"join rd.product p 	" +
				"	where p.productName in (:prodName)" +
				"and (rd.class = :reDef1 or rd.class = :reDef2)";
				Query query=session.createQuery(queryStrJoin);
		query.setParameterList("prodName", productName);	
		query.setParameter("reDef1", RegistrationDefinitionType.PRODUCT_REGISTRATION+" ");
		query.setParameter("reDef2", RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION+" ");
		List<RegistrationDefinition> lstOfDef=query.list();
		for (Iterator iterator = lstOfDef.iterator(); iterator.hasNext();) {
			RegistrationDefinition registrationDefinition = (RegistrationDefinition) iterator
					.next();
			System.out.println("Name of the pro  "+registrationDefinition.getProduct().getProductName());
			System.out.println("id of the pro   "+registrationDefinition.getProduct().getId());
			System.out.println("id of the definittion  "+registrationDefinition.getId());
			mapOfProdDtls.put(registrationDefinition.getProduct().getProductName(), registrationDefinition.getProduct().getId().concat(";").concat(registrationDefinition.getId()));
		}
		return mapOfProdDtls;
	}
	
	
	/*public void createRegirstionMigrationRecords_old(){
		Session session=getSession();
		String strQuery="select cm from CustomerMigration cm where cm.state='CUSTOMER_CREATED'";
		
		Query queryCust=session.createQuery(strQuery);
		List lstOfCust=queryCust.list();
		RegistrationMigration registrationMigration=null;
//		RegistrationMigrationData
		Set<RegistrationMigrationData> migrationDataSet=null;
		for (Iterator iterator = lstOfCust.iterator(); iterator.hasNext();) {
			System.out.println("Loop  1");
			CustomerMigration customerMigration = (CustomerMigration) iterator.next();
			migrationDataSet=customerMigration.getData().getRegistrationMigrationData();
			
			for (Iterator iterator2 = migrationDataSet.iterator(); iterator2
					.hasNext();) {
				System.out.println("Loop  2");
				RegistrationMigrationData registrationMigrationData = (RegistrationMigrationData) iterator2
						.next();
				registrationMigration=new RegistrationMigration();
				registrationMigration.setCustomerMigration(customerMigration);
				System.out.println("registrationMigrationData.getId() "+registrationMigrationData.getId());
				registrationMigration.setData(registrationMigrationData);
				registrationMigration.setState(RegistrationMigrationState.INITIAL);
				session.save(registrationMigration);
				session.flush();
				System.out.println("registrationMigration.getId() "+registrationMigration.getId());
			}
			
		}
	}
	*/

	@Override
    public int createRegirstionMigrationRecords(String customerMigrationId){
        
        Session session=getSession();
        
        @SuppressWarnings("unchecked")
        List<Object[]> data = (List<Object[]>)getSession()
        .createQuery(                
                " select cm, rmd" +
                " from CustomerMigration cm " +
                " join cm.data.registrationMigrationData rmd" + 
                " where cm.id = :cmId" 
                )
        .setParameter("cmId", customerMigrationId)
        .list();
        for(Object[] item : data){
            CustomerMigration cm = (CustomerMigration) item[0];
            RegistrationMigrationData rmd = (RegistrationMigrationData) item[1];
            RegistrationMigration rm  = new RegistrationMigration(cm, rmd);
            session.saveOrUpdate(rm);
        }
        return data.size();
    }
	
	public void updateProductAndDefinitionId(Map<String,String> mapOfProdDtls){
		Session session=getSession();
		String prodName =null;
		String prodIdRegDefId=null;
		String[] splitProdDtls=null;
		ProductMigrationDtls migrationDtls=null;
		String newProdName=null;
		String [] prodNameSplit=null;
		Query query=null;
		List<ProductMigrationDtls> lstOfProd=new ArrayList<ProductMigrationDtls>();
		Set<String> setOfProduct=mapOfProdDtls.keySet();
		for (Iterator iterator = setOfProduct.iterator(); iterator.hasNext();) {
			migrationDtls=new ProductMigrationDtls();
			 prodName = (String) iterator.next();
			 System.out.println("updateProductAndDefinitionId  ....   "+prodName);
			 prodIdRegDefId=mapOfProdDtls.get(prodName);
			 splitProdDtls= prodIdRegDefId.split(";");
			 System.out.println("The Size of the splitProdDtls is "+splitProdDtls.length);
			 migrationDtls.setProductName(prodName);
			 migrationDtls.setProductId(splitProdDtls[0]);
			 migrationDtls.setRegistrationDefinitionDtls(splitProdDtls[1]);
			 if(prodName.equals("Blackstone's Criminal Practice 2013 ebook")){
				 migrationDtls.setProductCode("bcp");
			 }
			 lstOfProd.add(migrationDtls);
		}
		System.out.println("The Sizwe of the List is "+lstOfProd.size());
		
		String strQuery="select regDataMig from  RegistrationMigrationData regDataMig  where regDataMig.productName like :prodName";
		for (Iterator iterator = lstOfProd.iterator(); iterator.hasNext();) {
			ProductMigrationDtls migrationProdDtls = (ProductMigrationDtls) iterator.next();
			prodName=migrationProdDtls.getProductName();
			 if(prodName.contains("'")){
				 System.out.println("Qoutes found  .....  ");
				 prodName.replaceAll("'", "''");
				/* prodNameSplit=prodName.split("'");
				 prodName=prodNameSplit[0];
				 newProdName=prodName.concat("''");
				 prodName=newProdName.concat(prodNameSplit[1]);*/
				 System.out.println("Product Name "+prodName);
				 migrationProdDtls.setProductName(prodName);
			 }
			query=session.createQuery(strQuery).setString("prodName", migrationProdDtls.getProductName());
//			setParameter("prodName", migrationProdDtls.getProductName());
			List lstOfProdMig=query.list();
			for (Iterator iterator2 = lstOfProdMig.iterator(); iterator2
					.hasNext();) {
				RegistrationMigrationData migrationData = (RegistrationMigrationData) iterator2.next();
				migrationData.setProductId(migrationProdDtls.getProductId());
				migrationData.setProductRegistrationDefinition(getProducRegistrationDefinition(migrationProdDtls.getRegistrationDefinitionDtls()));
			}
			System.out.println("No of record updated "+lstOfProdMig.size());
			session.flush();
		}
	}
	
	private ProductRegistrationDefinition getProducRegistrationDefinition(String id){
		Session session=getSession();
		String strQuery="select regDataMig from  RegistrationDefinition regDataMig  where regDataMig.id= :id and (regDataMig.class= :reDef1 or regDataMig.class= :reDef2)";
		Query query=session.createQuery(strQuery);
		query.setParameter("reDef1", RegistrationDefinitionType.PRODUCT_REGISTRATION+"");
		query.setParameter("reDef2", RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION+"");
		query.setParameter("id",id);
		ProductRegistrationDefinition prodRegistrationDef=(ProductRegistrationDefinition)query.uniqueResult();
		return prodRegistrationDef;
	}
	
}
 