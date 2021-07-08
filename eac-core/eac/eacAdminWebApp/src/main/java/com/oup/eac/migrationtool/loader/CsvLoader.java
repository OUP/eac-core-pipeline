package com.oup.eac.migrationtool.loader;

import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale; 
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVReader;

import com.oup.eac.common.RuntimeContext;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;
import com.oup.eac.domain.migrationtool.RegistrationMigrationData;
import com.oup.eac.migrationtool.util.LoaderUtil;
import com.oup.eac.service.migrationtool.CustomerMigrationService;


@Component
public class CsvLoader {
	
	 @Autowired
	 private CustomerMigrationService customerMigrationService;
	 
	 
	 private final String pattern = "yyyy-MM-dd HH:mm:ss";
	 
	 
	 private String loadFlag;
	 private String productCreatedFlag;
	 private String productNameToMigrate;	 
	 private String importFile;	 
	 private boolean SAVE_TO_DB;
	 private boolean ANONYMISE;  
	 private String ANONYMISE_PREFIX;
	 private String ANONYMISE_MAIL_SUFFIX;
	 private String migrateCount;
    
	 private LoaderUtil loaderUtil;
    
	public void loadCsv(RuntimeContext runtimeContext) {
		
		try {
			
		    setUp(runtimeContext);
			
			CSVReader csvReader = new CSVReader(new FileReader(importFile),
					'\t');
			String[] row = null;
			int i = 0;
			CustomerMigrationData customerMigrationData = null;
			RegistrationMigrationData registrationMigrationData=null;
			String[] rowSplit=null;
			String qouteReplacedStr1=null;
			String [] arrProductNameToMigrate=productNameToMigrate.split(":::");
            System.out.println("arrProductNameToMigrate   "+arrProductNameToMigrate.length);
            List<String> listOfProdToBeMigrated=Arrays.asList(arrProductNameToMigrate);
			System.out.println("The Load Flag......    "+loadFlag+" productCreatedFlag "+productCreatedFlag+" productNameToMigrate "+productNameToMigrate);
			System.out.println("Boolean.getBoolean(loadFlag)  "+Boolean.valueOf(loadFlag));
			if(Boolean.valueOf(loadFlag)){
			while ((row = csvReader.readNext()) != null) {
				System.out.println("Trunk");
				// rowofStr = csvReader.readAll();
				customerMigrationData = new CustomerMigrationData();
				registrationMigrationData=new RegistrationMigrationData();
				for (int j2 = 0; j2 < row.length; j2++) {
				        rowSplit=row[j2].split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");						                        
						System.out.println("the size is "+rowSplit.length);						
					for (int j = 0; j < rowSplit.length; j++) {
					    qouteReplacedStr1 = removeDoubleQoutes(rowSplit[j]);
						System.out.println("the val is "+j+" value is "+rowSplit[j]);
						if (j == 0) {                         
                            if (!rowSplit[j].equals("null")) {
                                System.out.println("User Id " + rowSplit[j]);                            
                                System.out.println("User Id "+ qouteReplacedStr1);                                
                                customerMigrationData.setExternalId(qouteReplacedStr1);
                            }
                            
                        }
						if (j == 1) {						    
						    if (!rowSplit[j].equals("null")) {
						        System.out.println("Last Login is " + rowSplit[j]);                            
	                            System.out.println("Last Login is "+ qouteReplacedStr1);
	                            DateTime loginDate=getItemDate(qouteReplacedStr1, pattern);
	                            customerMigrationData.setLastLogin(loginDate);
                            } 
						    
                            
                        }
						if (j == 2) {
						    if (!rowSplit[j].equals("null")) {
                                System.out.println("Suspended " + rowSplit[j]);                            
                                System.out.println("Suspended "+ qouteReplacedStr1);
                                if (qouteReplacedStr1.equalsIgnoreCase("suspended")){
                                    customerMigrationData.setEnabled(false);
                                }else {
                                    customerMigrationData.setEnabled(true);
                                }
						    }else{
						        customerMigrationData.setEnabled(true);
						    }
                            
                        }
						if (j == 4) {
							System.out.println("user name is " + rowSplit[j]);
							
							System.out.println("user name is "+ qouteReplacedStr1);
							String trimUsername = qouteReplacedStr1.replaceAll(" ", ""); 
							customerMigrationData.setUsername(trimUsername);
						}
						if (j == 5) {
							System.out.println("password   " + rowSplit[j]);
							
							System.out.println("Password is "+ qouteReplacedStr1);
							customerMigrationData.setPassword(qouteReplacedStr1);
						}
						if (j == 6) {
							System.out.println("First Name " + rowSplit[j]);
							
							System.out.println("First Name "+ qouteReplacedStr1);
							customerMigrationData.setFirstName(qouteReplacedStr1);
						}
						if (j == 7) {
							System.out.println("Last Name  " + rowSplit[j]);
							
							System.out.println("Last Name  "+ qouteReplacedStr1);
							customerMigrationData.setLastName(qouteReplacedStr1);
						}
						if (j == 8) {
							System.out.println("Email  " + rowSplit[j]);
							
							customerMigrationData.setEmailAddress(qouteReplacedStr1);
						}
						if (j == 5) {
							
							System.out.println("Password is "+ qouteReplacedStr1);
							customerMigrationData.setPassword(qouteReplacedStr1);
						}
						if (j == 32) {
							System.out.println("The Product Classification is "+ rowSplit[j]);
						}

						

						if (j == 9) {
							registrationMigrationData.setInstitutionName(qouteReplacedStr1);
						}
						if (j == 10) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setDepartmentName(qouteReplacedStr1);
							} else {
								registrationMigrationData.setDepartmentName("N/A");
							}
						}
						if (j == 11) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setJobTitle(qouteReplacedStr1);
							} else {
								registrationMigrationData.setJobTitle("N/A");
							}
						}
						if (j == 12) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setMarketing(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setMarketing("N/A");
							}
						}
						if (j == 13) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setStreet(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setStreet("N/A");
							}
						}
						if (j == 14) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setAddressLine2(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setAddressLine2("N/A");
							}
						}
						if (j == 15) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setAddressLine3(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setAddressLine3("N/A");
							}
						}
						if (j == 16) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setCity(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setCity("N/A");
							}
						}
						if (j == 17) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setZip(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setZip("N/A");
							}
						}
						if (j == 18) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setCountryCode(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setCountryCode("N/A");
							}
						}
						if (j == 19) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setTelephone(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setTelephone("N/A");
							}
						}
						if (j == 20) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setModuleTitle(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setModuleTitle("N/A");
							}
						}
						if (j == 21) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setEnrolmentYear(qouteReplacedStr1.trim());//Study year columnn in csv
							} else {
								registrationMigrationData.setEnrolmentYear("N/A");
							}
						}
						if (j == 22) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setNumberOfStudents(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setNumberOfStudents("N/A");
							}
						}
						if (j == 23) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setTextBook(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setTextBook("N/A");
							}
						}
						if (j == 24) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setReferral(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setReferral("N/A");
							}
						}
						if (j == 25) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setReferralOther(qouteReplacedStr1.trim());
							} else {
								registrationMigrationData.setReferralOther("N/A");
							}
						}
						if (j == 26) {
							if (!rowSplit[j].equals("null")) {
								registrationMigrationData.setOldOrcsLicenceId(qouteReplacedStr1);
							} else {
								registrationMigrationData.setReferralOther("N/A");
							}
						}
						if (j == 27) {
							if (!rowSplit[j].equals("null") && rowSplit[j].equals("Enabled") ) {
								registrationMigrationData.setLicenceStatus("0");//true
								
							} else {
								registrationMigrationData.setLicenceStatus("1");//false
							}
						}
						
						if (j == 28) {
							if (!rowSplit[j].equals("null")) {
								String licenStartDate=removeDoubleQoutes(rowSplit[j]);
								DateTime licStartDate=getItemDate(licenStartDate, pattern);
								System.out.println("Start Date  "+licStartDate.toLocalDate().toDateTimeAtStartOfDay().toDateTime());
								registrationMigrationData.setLicenceStartDate(licStartDate);
							} else {
								registrationMigrationData.setLicenceStartDate(null);
							}
						}
						if (j == 29) {
							if (!rowSplit[j].equals("null")) {
								String licenEndDate=removeDoubleQoutes(rowSplit[j]);
								DateTime licEndDate=getItemDate(licenEndDate, pattern);
								System.out.println("lic End Date  "+licEndDate.toLocalDate().toDateTimeAtStartOfDay().toDateTime());
								registrationMigrationData.setLicenceEndDate(licEndDate);
							} else {
								registrationMigrationData.setLicenceEndDate(null);
							}
						}
						
						if (j == 31) {
							if (!rowSplit[j].equals("null")) {
								
								registrationMigrationData.setProductName(qouteReplacedStr1);
							} else {
								registrationMigrationData.setProductName("N/A");
							}
						}
						if (j == 32) {
							if (!rowSplit[j].equals("null")) {
								
								registrationMigrationData.setProductClassification(qouteReplacedStr1);
							} else {
								registrationMigrationData.setProductClassification("N/A");
							}
						}
						
						// setting Default CMD
						customerMigrationData.setResetPassword(false); // password reset is not required.
						customerMigrationData.setFailedAttempts(0); // reset failed attemts
						customerMigrationData.setLocked(false); // reset lock
						customerMigrationData.setCustomerType("SELF_SERVICE"); // set customer type as SELF_SERVICE.						
						customerMigrationData.setLocale(new Locale("en_GB")); // Default locale
						customerMigrationData.setTimeZone("Europe/London"); // No time zone available.						
						customerMigrationData.setEmailVerificationState("VERIFIED"); // set email verification state as unknown.
						customerMigrationData.setCreatedDate(new DateTime()); 
					
					}
						
						
						
						
//						registrationMigrationData.setRegistrationDefinitionId(registrationDefinitionId)
//						registrationMigrationData.setVersion(version)
					
				}
				i++;	
				if(!migrateCount.equalsIgnoreCase("all")){
				    if(i==Integer.parseInt(migrateCount)){
	                    break;
	                }
				    
				}
				
				System.out.println("i   " + i);
				  if(ANONYMISE){
					  customerMigrationData = loaderUtil.anonymise(customerMigrationData);
                  }
				  System.out.println("productNameArray "+arrProductNameToMigrate.length+"List size is "+listOfProdToBeMigrated.size()+"The product name existss   "+listOfProdToBeMigrated.contains(registrationMigrationData.getProductClassification())+" registrationMigrationData.getProductClassification()  "+registrationMigrationData.getProductClassification());
				
                  if(listOfProdToBeMigrated.contains(registrationMigrationData.getProductName())){
                      if(customerMigrationData.isEnabled()){                          
                      
                          CustomerMigrationData custMigData = saveCustomerFromCSV(customerMigrationData);                      
                          
                          registrationMigrationData.setCustomerMigrationData(custMigData);    
                          
                          registrationMigrationData.setCreatedDate(new DateTime());                      
                          registrationMigrationData.setProductRegistrationDefinition(null);
                          registrationMigrationData.setProductId("NA");
                          saveRegistrationMigrationFromCSV(registrationMigrationData);
                      }
                  }
			}
		}
			if(Boolean.valueOf(productCreatedFlag)){
			    updateProductAndRegistrationDefinitionId(listOfProdToBeMigrated);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void setUp(RuntimeContext runtimeContext){
        
	    
	    loadFlag=runtimeContext.getProperty("migrationtool.load");
	    productCreatedFlag=runtimeContext.getProperty("migrationtool.product.created");
	    productNameToMigrate=runtimeContext.getProperty("migrationtool.productName.toMigration");	    
        importFile=runtimeContext.getProperty("migrationtool.importfile.name");        
        SAVE_TO_DB=runtimeContext.getBoolProperty("migrationtool.saveToDB");
        ANONYMISE=runtimeContext.getBoolProperty("migrationtool.anonymise");
        ANONYMISE_PREFIX=runtimeContext.getProperty("migrationtool.anonymise.prefix");
        ANONYMISE_MAIL_SUFFIX=runtimeContext.getProperty("migrationtool.anonymise.mail.suffix");
        migrateCount=runtimeContext.getProperty("migrationtool.migrate.count");
      
        initLoaderUtil();      
        
    }
    
	
private String removeDoubleQoutes(String strToConvert){
		
		int firstOccurance=-1;
		firstOccurance=strToConvert.indexOf("\"");
		int lastOccurance=-1;
				lastOccurance=strToConvert.lastIndexOf("\"");
		
		String refinedStr=null;
		if(firstOccurance>-1 &&  lastOccurance>-1){
		refinedStr=strToConvert.substring(firstOccurance+1, lastOccurance);
		}else{
			refinedStr=	strToConvert;
		}
		System.out.println("Refined Str==  "+refinedStr);
		return refinedStr;
	}

	
	private CustomerMigrationData saveCustomerFromCSV(CustomerMigrationData customerMigrationData){
	    CustomerMigrationData cmd = null;
	    try{
		    
			System.out.println("saveCustomerMigrationData");
			List existingCustList = customerMigrationService.findCustomerMigrationData(customerMigrationData.getUsername());
			if(!existingCustList.isEmpty()){
			   cmd =  (CustomerMigrationData)existingCustList.get(0); 
			}else {
			   customerMigrationService.saveCustomerMigrationData(customerMigrationData);
			   cmd = customerMigrationData;
			}
		}catch(Exception e){
			e.printStackTrace();			
		}
		return cmd;
	}
	
	private String saveRegistrationMigrationFromCSV(RegistrationMigrationData data){
		customerMigrationService.saveRegistrationMigrationData(data);
		return "success";
	}
	
	private void updateProductAndRegistrationDefinitionId(List productToMigrate){
		System.out.println("Inside the Update Product Registration Definition");
		Map<String,String> mapOfProdDtls=null;
		System.out.println("The product Name "+productNameToMigrate);
		/*String [] productNameArray=productNameToMigrate.split("^^");
		List<String> productToMigrate=new ArrayList<String>();
		for (int i = 0; i < productNameArray.length; i++) {
			productToMigrate.add(productNameArray[i]);
		}*/
		mapOfProdDtls=customerMigrationService.getProductDtls(productToMigrate);
		customerMigrationService.updateProductAndDefinitionId(mapOfProdDtls);
	}
	
	private  DateTime getItemDate(final String date, String pattern)
	{
		 DateTimeParser parser = DateTimeFormat.forPattern(pattern).getParser();
	        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(parser).toFormatter();
	        DateTime parsedDate=formatter.parseDateTime(date);
	        System.out.println("The Parsed Date is "+parsedDate);
	        return parsedDate;
	}

    private void initLoaderUtil(){
        
        this.loaderUtil = new LoaderUtil();
        loaderUtil.setAnonymisePrifix(ANONYMISE_PREFIX);
        loaderUtil.setAnonymiseMailSuffix(ANONYMISE_MAIL_SUFFIX);
        
    }
	
}
