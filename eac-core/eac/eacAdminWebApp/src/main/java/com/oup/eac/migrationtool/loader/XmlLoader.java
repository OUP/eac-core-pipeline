package com.oup.eac.migrationtool.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.oup.eac.common.RuntimeContext;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;
import com.oup.eac.domain.migrationtool.beans.AdminCustomer;
import com.oup.eac.domain.migrationtool.beans.AdminCustomerXML;
import com.oup.eac.migrationtool.util.LoaderUtil;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.migrationtool.CustomerMigrationService;

/**
 * @author Chirag Joshi
 */
@Component
public class XmlLoader{
   
    private static final Logger LOG = Logger.getLogger(XmlLoader.class);
    
    
    private String importFile;
    private String schemaFile;
    private boolean SAVE_TO_DB;
    private boolean ANONYMISE; 
    private String ANONYMISE_PREFIX;
    private String ANONYMISE_MAIL_SUFFIX;
    
    private AdminCustomerXML adminCustomerXML;
    
    @Autowired
    private CustomerMigrationService customerMigrationService;
    
    private LoaderUtil loaderUtil;

    
    public CustomerMigrationService getCustomerMigrationService() {
        return customerMigrationService;
    }

    private void setUp(RuntimeContext runtimeContext){
        
        
        importFile=runtimeContext.getProperty("migrationtool.importfile.name");
        schemaFile=runtimeContext.getProperty("migrationtool.importfile.schema");
        SAVE_TO_DB=runtimeContext.getBoolProperty("migrationtool.saveToDB");
        ANONYMISE=runtimeContext.getBoolProperty("migrationtool.anonymise");
        ANONYMISE_PREFIX=runtimeContext.getProperty("migrationtool.anonymise.prefix");
        ANONYMISE_MAIL_SUFFIX=runtimeContext.getProperty("migrationtool.anonymise.mail.suffix");
      
        initLoaderUtil();      
        
    }
    
    public void loadXml(RuntimeContext runtimeContext) {
        
      
        setUp(runtimeContext);
        
        LOG.debug("XML loading started...");        
        
        File xmlFile = new File(importFile);
        
        if(isValidXML(xmlFile)){
            
            xmlToJava(xmlFile);
                        
            List<AdminCustomer> adminCustomerList = adminCustomerXML.getcustomerList();
            
            try{
                //Save customer data
                saveCustomerMigrationDataList(adminCustomerList);
                
                LOG.debug("Loading Completed...");
                
            }catch(Exception e){
                e.printStackTrace();
                LOG.error(e.getMessage());
            }                      
            
        }        
       
    }
    
    
    public boolean isValidXML(File xmlFile){
        
        boolean isValid = true;
        /*
        Source src = new StreamSource(xmlFile);        
        
        try {
            
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            
            Schema schema = schemaFactory.newSchema(new File(this.schemaFile));
            
            Validator validator = schema.newValidator();
            
            validator.validate(src);
            LOG.debug(src.getSystemId() + " is valid");
            
            isValid = true;
            
        } catch (SAXException e) {
            
          
          LOG.error(src.getSystemId() + " is NOT valid");
          LOG.error("Reason: " + e.getLocalizedMessage());
        
        } catch (Exception e) {
          e.printStackTrace();
        }               
        */
        return isValid;
        
    }
    
    public void xmlToJava(File xmlFile){
        
        try {
            
            JAXBContext jaxbContext = JAXBContext.newInstance(AdminCustomerXML.class);
            
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            adminCustomerXML = (AdminCustomerXML) jaxbUnmarshaller.unmarshal(xmlFile);            
           
           
        }catch (JAXBException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }        
        
    }
    
    
    private void saveCustomerMigrationDataList(List<AdminCustomer> adminCustomerList) throws ServiceLayerException {
        
        try {
            CustomerMigrationData cmd = null;
            List<CustomerMigrationData> cmdList = new ArrayList<CustomerMigrationData>();;
            
            LOG.debug("SAVE_TO_DB Flag is: " + SAVE_TO_DB);
            if (SAVE_TO_DB) {
                // Save to DB if flag is true
                for (AdminCustomer adminCustData : adminCustomerList) {
                    cmd = sourceToDestinationMapping(adminCustData);
                    
                    // Anonymise data
                    if(ANONYMISE){
                        cmd = loaderUtil.anonymise(cmd);
                    }
                    cmdList.add(cmd);
                }
                LOG.debug("Saving data into database.");
                //Call service to save data
                customerMigrationService.saveCustomerMigrationData(cmdList);
                
            } else {
                
                LOG.debug("SAVE_TO_DB flag is false, so data will not be saved into database.");
            }
            
        } catch (DataIntegrityViolationException dve) {
            
            LOG.error(dve.getMessage());
            
            CustomerMigrationData cmd = null;
            
            for (AdminCustomer adminCustData : adminCustomerList) {
                try {
                    // try to save individual data if list is throwing exception.
                    cmd = sourceToDestinationMapping(adminCustData);
                    customerMigrationService.saveCustomerMigrationData(cmd);
                    
                } catch (DataIntegrityViolationException dve2) {
                    
                    LOG.error("PROBLEM WITH " + adminCustData);                    
                    throw dve;
                }
            }
        }
    }
    
    private CustomerMigrationData sourceToDestinationMapping(AdminCustomer adminCustomer){
        
        // Mapping fields from adminCustomer to CustomerMigrationData
        CustomerMigrationData cmd = null;
        
        if (adminCustomer != null){
            
            cmd = new CustomerMigrationData();
            
            cmd.setUsername(adminCustomer.getEmailAddr());
            cmd.setPassword(adminCustomer.getPassword());
            cmd.setFirstName(adminCustomer.getFirstName());
            cmd.setLastName(adminCustomer.getLastName());
            cmd.setEmailAddress(adminCustomer.getEmailAddr());
            cmd.setResetPassword(true); // password reset is required.
            cmd.setFailedAttempts(0); // reset failed attemts
            cmd.setLocked(false); // reset lock
            cmd.setCustomerType("SELF_SERVICE"); // set customer type as SELF_SERVICE.
            cmd.setEnabled(adminCustomer.isEnabled());
            cmd.setLocale(new Locale(adminCustomer.getLocale()));
            cmd.setTimeZone(null); // No time zone available.
            cmd.setLastLogin(new DateTime(adminCustomer.getLastLogon().toGregorianCalendar().getTime()));
            cmd.setEmailVerificationState("UNKNOWN"); // set email verification state as unknown.
            cmd.setExternalId(String.valueOf(adminCustomer.getId()));
            cmd.setColumn1(String.valueOf(adminCustomer.getGroupId())); // set group id into column1
            cmd.setColumn2(adminCustomer.getNewsletter()); // set newsletter into column2
            cmd.setColumn3(String.valueOf(adminCustomer.getNumberOfLogons())); // set number of logons into column3
            cmd.setCreatedDate(new DateTime());
        }
        
        return cmd;
    }
    
    private void initLoaderUtil(){
        
        this.loaderUtil = new LoaderUtil();
        loaderUtil.setAnonymisePrifix(ANONYMISE_PREFIX);
        loaderUtil.setAnonymiseMailSuffix(ANONYMISE_MAIL_SUFFIX);
        
    }
    
    
    
    public static void main(String args[]){
        
        System.out.println("XML loading started....");
        File xmlFile = new File("C:\\DEV\\Migration\\kk.xml");
       
        /*if(isValidXML(xmlFile)){
            System.out.println(" XML file is valid");            
        } else{
            System.out.println(" XML file is not valid");
        }*/
       
        
    }
    

}
