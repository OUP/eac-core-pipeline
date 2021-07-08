package com.oup.eac.service.migrationtool.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.hibernate.proxy.HibernateProxy;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.oup.eac.data.CustomerDao;
import com.oup.eac.data.ExternalIdDao;
import com.oup.eac.data.migrationtool.CustomerMigrationDao;
import com.oup.eac.data.migrationtool.CustomerMigrationDataDao;
import com.oup.eac.data.migrationtool.RegistrationMigrationDao;
import com.oup.eac.data.migrationtool.RegistrationMigrationDataDao;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.Password;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.User;
import com.oup.eac.domain.migrationtool.CustomerMigration;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;
import com.oup.eac.domain.migrationtool.CustomerMigrationState;
import com.oup.eac.domain.migrationtool.RegistrationMigration;
import com.oup.eac.domain.migrationtool.RegistrationMigrationData;
import com.oup.eac.domain.migrationtool.RegistrationMigrationState;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.LicenceDtoDateTime;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.migrationtool.CustomerMigrationService;

/**
 * 
 * @author Chirag Joshi
 *
 */
@Service("customerMigrationService")
public class CustomerMigrationServiceImpl implements CustomerMigrationService {

    private static final Logger LOG = Logger.getLogger(CustomerMigrationServiceImpl.class);    
    
    private final CustomerMigrationDataDao customerMigrationDataDao;
    private final CustomerMigrationDao customerMigrationDao;
    
    private  RegistrationMigrationDataDao registrationMigrationDataDao;
    private  RegistrationMigrationDao registrationMigrationDao;    

    
    private final RegistrationService registrationService;
    private final ConcurrentMap<String,Semaphore> customerLockMap;
    
    private final CustomerDao customerDao;
    private final ErightsFacade eRightsFacade;
    private final ErightsFacade erightsFacade;
    
    private ExternalIdService externalIdService;
    private ExternalIdDao externalIdDao;
    
    @Value("${externalsystem.name}")
    private String externalSystemName;
    
    @Value("${externalsystem.type.id}")
    private String externalSystemTypeName;
    
    @Value("${migration.externalsystem.mapping}")
    private boolean externalSystemMapping;
    
    
    @Autowired
    public CustomerMigrationServiceImpl(
            final CustomerMigrationDao customerMigrationDao,
            final CustomerDao customerDao,
            final CustomerMigrationDataDao customerMigrationDataDao,            
            @Qualifier("erightsFacade") final ErightsFacade eRightsFacade,
            final ExternalIdService externalIdService,
            final ExternalIdDao externalIdDao,
            final RegistrationMigrationDataDao registrationMigrationDataDao,
            final RegistrationMigrationDao registrationMigrationDao,
            final RegistrationService registrationService
            ) {
        super();
        this.customerMigrationDao = customerMigrationDao;        
        this.customerDao = customerDao;
        this.eRightsFacade = eRightsFacade;
        this.erightsFacade = eRightsFacade;
        this.customerMigrationDataDao = customerMigrationDataDao;
        this.externalIdService = externalIdService;
        this.externalIdDao = externalIdDao;
        this.registrationMigrationDataDao=registrationMigrationDataDao;
        this.registrationMigrationDao=registrationMigrationDao;
        this.registrationService = registrationService;
        this.customerLockMap = new ConcurrentHashMap<String, Semaphore>();        
    }
    

    @Override
    public CustomerMigration getCustomerMigrationToProcess(CustomerMigrationState state, int fetchSize) {
        List<CustomerMigration> migrations = customerMigrationDao.getInState(state, fetchSize);
        CustomerMigration result = getRandom(migrations);
        if (result != null) {
            CustomerMigrationState nextState = result.getState().next();
            result.setState(nextState);
        }
        return result;
    }
    
    @Override
    public void saveCustomerMigrationState(String customerMigrationId, CustomerMigrationState state) {
        CustomerMigration cm = customerMigrationDao.getById(customerMigrationId, false);
        cm.setState(state);
    }


    @Override
    public Customer createCustomer(String customerMigrationId) throws ServiceLayerException {
        CustomerMigration customerMigration = getCustomerMigration(customerMigrationId);

        try {
            LOG.debug("Creating Customer with customerMigrationId : " + customerMigrationId);
            Customer existing = customerMigration.getCustomer();
            
            LOG.debug("Customer Migration state : " + customerMigration.getState());
            Assert.isTrue(customerMigration.getState() == CustomerMigrationState.CREATING_CUSTOMER);
            
            LOG.debug("Existing Customer : " + existing);
            Assert.isTrue(existing == null);
            
            Customer customer = new Customer();
            CustomerMigrationData data = customerMigration.getData();
            
            customer.setUsername(data.getUsername());
            
            // password handling
            String password = data.getPassword();            
            customer.setPassword(new Password(password, false));
            
            customer.setFirstName(data.getFirstName());
            customer.setFamilyName(data.getLastName());
            customer.setEmailAddress(data.getEmailAddress());
            customer.setResetPassword(data.isResetPassword());
            customer.setFailedAttempts(data.getFailedAttempts());
            customer.setLocked(data.isLocked());
            if (data.getCustomerType().equalsIgnoreCase("SELF_SERVICE")){                
                
                customer.setCustomerType(Customer.CustomerType.SELF_SERVICE);
                
            }else if (data.getCustomerType().equalsIgnoreCase("SHARED")){
                customer.setCustomerType(Customer.CustomerType.SHARED);
                
            }else if (data.getCustomerType().equalsIgnoreCase("SPECIFIC_CONCURRENCY")){
                customer.setCustomerType(Customer.CustomerType.SPECIFIC_CONCURRENCY);
                
            } else {
                customer.setCustomerType(Customer.CustomerType.SELF_SERVICE);
            }
            
            customer.setEnabled(data.isEnabled());
            customer.setLocale(data.getLocale());
            customer.setTimeZone("Europe/London"); // set time zone to Europe/London
            customer.setLastLoginDateTime(data.getLastLogin());
            
            if (data.getEmailVerificationState().equalsIgnoreCase("UNKNOWN")){    
                customer.setEmailVerificationState(User.EmailVerificationState.UNKNOWN);
                
            }else if (data.getEmailVerificationState().equalsIgnoreCase("EMAIL_SENT")){
                customer.setEmailVerificationState(User.EmailVerificationState.EMAIL_SENT);
                
            } else if (data.getEmailVerificationState().equalsIgnoreCase("VERIFIED")){
                customer.setEmailVerificationState(User.EmailVerificationState.VERIFIED);
            } else{
                customer.setEmailVerificationState(User.EmailVerificationState.UNKNOWN);
            }
            
            CustomerDto customerDto = new CustomerDto(customer);

            CustomerDto result = this.eRightsFacade.createUserAccount(customerDto);
            
            String eRightsCustomerId = result.getUserId();
            LOG.debug("Customer created in Atypon, Id : " + eRightsCustomerId);
            customer.setId(eRightsCustomerId);
            customerDao.save(customer);            
            LOG.debug("Customer Saved successfully...");
            
            if(externalSystemMapping){
                
                // set external system            
                ExternalSystem externalSystem = externalIdService.getExternalSystemByName(externalSystemName);

                // set external system type            
                ExternalSystemIdType externalSystemtype = null ;//externalIdService.getExternalSystemIdType(externalSystem, externalSystemTypeName);
                
                
                ExternalCustomerId externalCustomerIdentifier = new ExternalCustomerId();
                externalCustomerIdentifier.setId(UUID.randomUUID().toString());
                externalCustomerIdentifier.setVersion(0);
                externalCustomerIdentifier.setExternalId(String.valueOf(data.getExternalId()));            
                externalCustomerIdentifier.setExternalSystemIdType(externalSystemtype);
                externalCustomerIdentifier.setCustomer(customer);

                externalIdDao.save(externalCustomerIdentifier);
                LOG.debug("External id saved for Customer: " + customer.getId());
                
            } else {
                LOG.debug("Mapping of External Id is not required...");
            }
            
            customerMigration.setCustomer(customer);
            customerMigration.setState(CustomerMigrationState.CUSTOMER_CREATED);
            
            LOG.debug("Customer Migration state changed to : " + CustomerMigrationState.CUSTOMER_CREATED);
        
            return customer;            
           
        } catch (Exception ex) {
            throw new ServiceLayerException("Problem Creating eRights User Account for username "+ customerMigration.getData().getUsername(), ex);
        }
    }

    
    public static int random(int min, int max) {
        int result = min + (int) (Math.random() * ((max - min) + 1));
        return result;
    }
    
    /**
     * Gets the customer migration.
     * 
     * @param id
     *            the id
     * @return the customer migration
     */
    @Override
    public CustomerMigration getCustomerMigration(final String id) {
        CustomerMigration result = customerMigrationDao.getCustomersMigrationToProcess(id);
        return result;
    }
    
    
    private static final ThreadLocal<Boolean> TL = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public static boolean getFlag() {
        return TL.get();
    }

    public static void resetFlag() {
        TL.set(false);
    }

    public static void setFlag() {
        TL.set(true);
    }

    
    private <T> T getRandom(List<T> data){
        T result;
        if (data == null || data.size() == 0) {
            result = null;
        } else if (data.size() == 1) {
            result = data.get(0);
        } else {
            int randomIndex = random(0, data.size() - 1);
            result = data.get(randomIndex);
        }
        return result;
    }

    
    @Override
    public boolean isMigrationFinished(boolean registrationMigration) {
        long notFinishedCustomer = this.customerMigrationDao.getNotFinishedCount();
        LOG.debug("notFinishedCustomer: " + notFinishedCustomer);
        boolean finished = notFinishedCustomer == 0;
        LOG.debug("flag registrationMigration: " + registrationMigration);
        if (registrationMigration){
            long notFinishedRegistration = this.registrationMigrationDao.getNotFinishedCount();
            LOG.debug("notFinishedRegistration: "+ notFinishedRegistration);
            finished = notFinishedCustomer+notFinishedRegistration == 0;
        }
        
        return finished;
    }


    @Override
    public void saveCustomerMigrationData(CustomerMigrationData data) {
        this.customerMigrationDataDao.save(data);
    }


    @Override
    public void saveCustomerMigrationData(List<CustomerMigrationData> customers) {
        for(CustomerMigrationData customer : customers){
            this.customerMigrationDataDao.save(customer);
        }
        
    }


    @Override
    public List<CustomerMigrationData> findCustomerMigrationData(String username) throws ServiceLayerException {
        return this.customerMigrationDataDao.findByUserId(username);
    }
    
    @Override
    public void saveRegistrationMigrationData(RegistrationMigrationData data) {
        this.registrationMigrationDataDao.save(data);
        this.registrationMigrationDataDao.flush();
        
    }
    
    @Override
    public Map<String, String> getProductDtls(List<String> productNameToMigrate){
        Map<String,String> mapOfProdDtls=null;
        mapOfProdDtls=registrationMigrationDataDao.getProductIds(productNameToMigrate);
        return mapOfProdDtls;
    }
    
    @Override    
    public void updateProductAndDefinitionId(Map<String,String> mapOfprodDtls){
        System.out.println("inside the updateProduc  Service");
        this.registrationMigrationDataDao.updateProductAndDefinitionId(mapOfprodDtls);
    }
    
    
    @Override
    public RegistrationMigration getRegistrationMigrationToProcess(RegistrationMigrationState state, int fetchSize) {
        List<RegistrationMigration> migrations = registrationMigrationDao.getInState(state, fetchSize);
        RegistrationMigration result = getRandom(migrations);
        if (result != null) {
            RegistrationMigrationState nextState = result.getState().next();
            result.setState(nextState);
        }
        return result;
    }
    
    @Override
    public void saveRegistrationMigrationState(String registrationMigrationId, RegistrationMigrationState state) {
        RegistrationMigration rm = registrationMigrationDao.getById(registrationMigrationId, false);
        rm.setState(state);   
    }
    
    @Override
    public Registration<ProductRegistrationDefinition> createRegistration(String registrationMigrationId) throws ServiceLayerException {

        LOG.debug("registrationMigrationId: " + registrationMigrationId);
        RegistrationMigration registrationMigration  = getRegistrationMigration(registrationMigrationId);
        //Assert.isTrue(registrationMigration.getCustomerMigration().getState()==CustomerMigrationState.CUSTOMER_CREATED);
        // Assert.isTrue(registrationMigration.getData().getProductClassification()=="bcp");
        Assert.isTrue(registrationMigration.getState() == RegistrationMigrationState.CREATING_REGISTRATION);
            
        CustomerMigration customerMigration = registrationMigration.getCustomerMigration();
        RegistrationMigrationData rmData = registrationMigration.getData();
            
        DateTime start = rmData.getLicenceStartDate();
        DateTime end = rmData.getLicenceEndDate();
        if(start != null && end != null){
            if(start.isAfter(end)) {
                // make start and end the same
                rmData.setLicenceEndDate(start);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
                String tStart = sdf.format(start.toDate());
                String tEnd = sdf.format(end.toDate());
                String msg = String.format("licenceStartDate [%s] was after end date[%s] (the end date is now same as start date)", tStart ,tEnd);
                //saveWarning(tEnd, msg, rmData);
            }
        }
        
        Customer customer = customerMigration.getCustomer();
        LOG.debug("customer: " + customer);
        //ProductRegistrationDefinition prodRegDef = this.registrationDefinitionService.getProductRegistrationDefinitionWithLicence(rmData.getProductRegistrationDefinition().getId());
        ProductRegistrationDefinition prodRegDef = rmData.getProductRegistrationDefinition();
        
        LOG.debug("prodRegDef: " + prodRegDef);
//        Assert.isTrue(prodRegDef != null);
        //TODO A temp workaround for hibernate problem
       
        LicenceTemplate lt = prodRegDef.getLicenceTemplate();
        LOG.debug("lt: " + lt);
        if(lt instanceof HibernateProxy){
            HibernateProxy proxy = (HibernateProxy)lt;
            org.hibernate.proxy.LazyInitializer init = proxy.getHibernateLazyInitializer();
            Object impl = init.getImplementation();
            prodRegDef.setLicenceTemplate((LicenceTemplate)impl);
        }
        Assert.isTrue(prodRegDef != null);
        String productName = prodRegDef.getProduct().getProductName();
            
        try{
            
            /*
            if (prodRegDef.getPageDefinition() == null) {
                throw new ServiceLayerException("no page definition for productRegistraitonDefinition");
            }
            */

            // create the registration in the db - no erights calls
            Registration<ProductRegistrationDefinition> registration = registrationService.saveProductRegistration(customer, prodRegDef);
            registration.setCompleted(true);
            
            // Update registration depending on whether the licence was enabled or not.
            if(rmData.getLicenceStatus().equals("0")) {
                registration.setActivated(false);
                //AWAITING VALIDATION MEANS 'VALIDATION ACTIONS BY EAC COMPLETE'
                LOG.debug("LICENCE IS DISABLED : SETTING AWAITING VALIDATION TO FALSE");
                registration.setAwaitingValidation(false);
            } else {
                registration.setActivated(true);
                //AWAITING VALIDATION MEANS 'VALIDATION ACTIONS BY EAC COMPLETE'
                LOG.debug("LICENCE IS ENABLED : SETTING AWAITING VALIDATION TO TRUE");
                registration.setAwaitingValidation(true);
            }
            
            boolean shared =false; 
//                  customerMigration.getData().getShared();
            addErightsLicence(customer, registration, prodRegDef, shared);

            registrationMigration.setRegistration(registration);
            registrationMigration.setState(RegistrationMigrationState.REGISTRATION_CREATED);

            
            List<LinkedRegistration> linkedRegistraitons =new ArrayList<LinkedRegistration>(); 
//                  addLinkedProductsforORCS(customer, prodRegDef, registration);
            
            updateLicences(customer, registration, linkedRegistraitons, rmData);
            return registration;

        } catch (Exception ex) {
            /*if (ex instanceof MigrationException) {
                throw (MigrationException) ex;
            }*/
            ex.printStackTrace();
            String msg = String.format("problem creating product registration for username [%s] and product [%s] ", customer.getUsername(), productName);
            throw new ServiceLayerException(msg, ex);
        }
    }
    
    @Override
    public RegistrationMigration getRegistrationMigration(final String id) {
        RegistrationMigration result = registrationMigrationDao.getRegistrationMigrationToProcess(id);
        return result;
    }
    
    
    private void addErightsLicence(final Customer customer,
            final Registration<ProductRegistrationDefinition> registration,
            final ProductRegistrationDefinition prodRegDef,
            final boolean shared) throws ErightsException, ServiceLayerException {
        boolean licenceEnabled = true;
        String erightsUserId = customer.getId();
        LicenceTemplate licenceTemplate = prodRegDef.getLicenceTemplate();
        List<String> erightsProductIds = Arrays.asList(prodRegDef.getProduct().getId());
        
        Semaphore newLock = new Semaphore(1);
        Semaphore lock = this.customerLockMap.putIfAbsent(customer.getId(), newLock );
        if(lock == null){
            lock = newLock;
        }
        Assert.notNull(lock);
        try {
            lock.acquire();
            try {
                // For shared users, the concurrent licences are UNLIMITED
                if (licenceTemplate.getLicenceType() == LicenceType.CONCURRENT && shared) {
                    ConcurrentLicenceTemplate conLicTemp = (ConcurrentLicenceTemplate) licenceTemplate;
                    conLicTemp.setTotalConcurrency(-1);
                    conLicTemp.setUserConcurrency(-1);
                }
                String eRightsLicenceId = eRightsFacade.addLicense(erightsUserId, licenceTemplate, erightsProductIds,licenceEnabled);
                //registration.setErightsLicenceId(eRightsLicenceId);
            } finally {
                lock.release();
            }
        } catch (InterruptedException ex) {
            //handleInterruptedException(customer, ex);
        }
    }
    
    
    private void updateLicences(Customer customer, Registration<ProductRegistrationDefinition> registration,
            List<LinkedRegistration> linkedRegistraitons,RegistrationMigrationData rmData) throws ServiceLayerException, ErightsException {
        List<LicenceDto> licences = erightsFacade.getLicensesForUser(customer.getId(), null);
        Map<String, LicenceDto> licenceMap = new HashMap<String, LicenceDto>();
        for (LicenceDto licence : licences ) {
            licenceMap.put(licence.getLicenseId(), licence);
        }
        Assert.isTrue(registration.isCompleted());
        

        
        LicenceDto mainLicence = licenceMap.get(registration.getId());
        updateLicence(customer, rmData, mainLicence, false);
        
        
        for(LinkedRegistration linkedRegistration : linkedRegistraitons){
            LicenceDto linkedLicence = licenceMap.get(linkedRegistration.getErightsLicenceId());
            updateLicence(customer, rmData, linkedLicence, true);
        }
    }
    
    private void updateLicence(Customer customer, RegistrationMigrationData rmData, LicenceDto licenceDto, boolean isLinked) throws ErightsException, ServiceLayerException {
        Semaphore lock = this.customerLockMap.get(customer.getId());
        Assert.isTrue(lock != null);
        try {
            lock.acquire();
            if (licenceDto == null) {
                return;
            }
            DateTime startDate = rmData.getLicenceStartDate();
            DateTime endDate = rmData.getLicenceEndDate();
            
            //We disable top level licences if the migration data has them disabled.
            if(!isLinked){
                if(rmData.getLicenceStatus().equals("1")){
                	System.out.println("TRUE.................................");
                     licenceDto.setEnabled(true);
                }else{
                	System.out.println("FALSE.................................");
                    licenceDto.setEnabled(false);
                }
                 
            }
            LicenceDtoDateTime licenceDto2 = new LicenceDtoDateTime(licenceDto, startDate, endDate);
            erightsFacade.updateLicenceUsingDateTimes(customer.getId(), licenceDto2);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
           // handleInterruptedException(customer, ex);
        } finally {
            lock.release();
        }
    }
    
    @Override
    public void createRegirstionMigrationRecords(String customerMigrationId){
        
        this.registrationMigrationDataDao.createRegirstionMigrationRecords(customerMigrationId);
        
    }


}
