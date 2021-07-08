package com.oup.eac.service.merge.merger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.service.merge.RegistrationLicenceMergeInfoDto;

public class MergeContext implements MergeResult {

    public static final String NEWLINE = System.getProperty("line.separator");
    
    private static final Logger LOG = Logger.getLogger(MergeContext.class);
    
    public static class Message{
        
        String message;
        Level level;
        
        public Message(Level level, String message){
            this.level = level;
            this.message  = message;
        }
        
        public void log(){
            LOG.log(level, message);
        }
        
        @Override
        public String toString(){
            String msg = String.format("%s %s",level.toString(),message);
            return msg;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Level getLevel() {
            return level;
        }

        public void setLevel(Level level) {
            this.level = level;
        }
        
    }
    
    public class Association{
        private final AssociationType assocType;        
        private final Integer eRightsProductId;        
        private final LicenceDto licenceDto;       
        private final Registration<? extends ProductRegistrationDefinition> registration;
        private final Product product;//
        
        public Association(AssociationType assocType, Integer eRightsProductId, Registration<? extends ProductRegistrationDefinition> registration, Product product, LicenceDto licenceDto){
            this.assocType = assocType;
            this.eRightsProductId = eRightsProductId;
            this.registration = registration;
            this.product = product;
            this.licenceDto = licenceDto;
        }

        public AssociationType getAssocType() {
            return assocType;
        }

        public Integer geteRightsProductId() {
            return eRightsProductId;
        }

        public LicenceDto getLicenceDto() {
            return licenceDto;
        }

        public Registration<? extends ProductRegistrationDefinition> getRegistration() {
            return registration;
        }

        public Product getProduct() {
            return product;
        }
        
        public boolean isLinkedRegistration(){
        	return product instanceof LinkedProduct;
        }
        
        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("assoc[");
            sb.append(assocType);
            sb.append("]");
            
            sb.append(" eR-prodId[");
            sb.append(eRightsProductId);
            sb.append("]");
            
            sb.append(" eR-licId[");
            sb.append(licenceDto.getLicenseId());
            sb.append("]");
            
            sb.append(" lic[");
            sb.append(new LicenceTemplateInfo(this.licenceDto).getDescription());
            sb.append("]");

            sb.append(" regId[");
            sb.append(registration.getId());
            sb.append("]");
            
            sb.append(" pId[");
            sb.append(product.getId());
            sb.append("]");

            sb.append(" pType[");
            sb.append(product.getProductType().name().substring(0,4));
            sb.append("]");
            
            sb.append(" pName[");
            sb.append(product.getProductName());
            sb.append("]");
            


            return sb.toString();
        }
    }
    
    private final List<Message> messages = new ArrayList<Message>();
    
    private final List<RegistrationLicenceMergeInfoDto> regLicInfoDtos;
    
    private final List<Association> associations = new ArrayList<Association>();
    
    private boolean error = false;

    private final Customer customer;

    // the contents of these 3 collections can't change after initialisation
    private final List<LicenceDto> licences;// contents can change but list can't
    private final List<Registration<? extends ProductRegistrationDefinition>> registrations;
    private final Map<Integer, List<RegistrationLicenceMergeInfoDto>> eRightsProductIdToRegistrationsMap;
    private final Map<Integer, List<LicenceDto>> eRightsProductIdToLicencesMap;

    public MergeContext(Customer customer, List<LicenceDto> licences, List<Registration<? extends ProductRegistrationDefinition>> registrations) {
        this.customer = customer;
        this.registrations = Collections.unmodifiableList(registrations);
        this.licences = Collections.unmodifiableList(licences);
        this.regLicInfoDtos = new ArrayList<RegistrationLicenceMergeInfoDto>();

        // initialise the maps
        Map<Integer, List<RegistrationLicenceMergeInfoDto>> regMap = initRegistrations();
        Map<Integer, List<LicenceDto>> licMap = initLicences();
        this.eRightsProductIdToRegistrationsMap = Collections.unmodifiableMap(regMap);
        this.eRightsProductIdToLicencesMap = Collections.unmodifiableMap(licMap);
    }

    private Map<Integer, List<RegistrationLicenceMergeInfoDto>> initRegistrations() {
        HashMap<Integer, List<RegistrationLicenceMergeInfoDto>> result = new HashMap<Integer, List<RegistrationLicenceMergeInfoDto>>();
        if (this.registrations == null || registrations.isEmpty()) {
            return result;
        }
        for (Registration<? extends ProductRegistrationDefinition> registration : this.registrations) {
            initRegistration(result, registration);
        }
        return result;
    }

    private Map<Integer, List<LicenceDto>> initLicences() {
        Map<Integer, List<LicenceDto>> result = new HashMap<Integer, List<LicenceDto>>();
        if (this.licences == null || licences.isEmpty()) {
            return result;
        }
        for (LicenceDto dto : this.licences) {
            initLicence(result, dto);
        }
        return result;
    }

    private void initRegistration(Map<Integer, List<RegistrationLicenceMergeInfoDto>> regMap, Registration<? extends ProductRegistrationDefinition> registration) {/*
        RegistrationLicenceMergeInfoDto regLicInfo = new RegistrationLicenceMergeInfoDto(this.customer, registration);

        this.regLicInfoDtos.add(regLicInfo);

       // RegisterableProduct rp = registration.getRegistrationDefinition().getProduct();

        // associate the eRightsProductid [for the registerable product] with this registration
        Integer eRightsProductId = rp.getErightsId();
        MergeUtils.addMapListEntry(regMap, eRightsProductId, regLicInfo);

       // Set<LinkedProduct> linked = rp.getLinkedProducts();

        if (linked != null) {
            for (LinkedProduct link : linked) {
                eRightsProductId = link.getErightsId();
                MergeUtils.addMapListEntry(regMap, eRightsProductId, regLicInfo);
            }
        }
    */}

    private void initLicence(Map<Integer, List<LicenceDto>> licMap, LicenceDto dto) {/*
        List<String> pids = dto.getProductIds();
        if (pids == null || pids.isEmpty()) {
            log(Level.ERROR, "There is no products on licenceId" + dto.getErightsId());
        } else {
            if (pids.size() > 1) {
                log(Level.ERROR, "There is more than 1 product on licenceId" + dto.getErightsId());
            }
            String eRightsProductId = pids.get(0);
           // MergeUtils.addMapListEntry(licMap, eRightsProductId, dto);
        }
    */}

    @Override
    public List<RegistrationLicenceMergeInfoDto> getRegistrationLicenceInfo() {
        return this.regLicInfoDtos;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    public Map<Integer, List<RegistrationLicenceMergeInfoDto>> getRegistrationsMap() {
        return this.eRightsProductIdToRegistrationsMap;
    }

    public Map<Integer, List<LicenceDto>> getLicencesMap() {
        return this.eRightsProductIdToLicencesMap;
    }


    public void log(Level level, Integer eRightsProductId, String format, Object... args) {
        String msg = String.format(format, args);
        String message = String.format("eRightsProductId[%d] : %s", eRightsProductId, msg);        
        addMessage(level, message);
    }

    private void log(Level level, String format, Object... args) {
        String msg = String.format(format, args);
        addMessage(level, msg);
    }
    
    private void addMessage(Level level, String msg){
        if(level == Level.ERROR){
            this.error = true;
        }
        String message = String.format("Cust. Email[%s]  %s", customer.getEmailAddress(), msg);
        this.messages.add(new Message(level, message));
    }

    @Override
    public boolean isError(){
       return this.error;
    }
    
    @Override
    public List<Message> getMessages(){
        return this.messages;
    }

    public void addAssociation(AssociationType assocType, Integer eRightsProductId, Registration<? extends ProductRegistrationDefinition> registration, Product product, LicenceDto licenceDto) {
        this.associations.add(new Association(assocType,eRightsProductId,registration,product,licenceDto));
    }

    @Override
    public List<Association> getAssociations() {
        return associations;
    }

    @Override
    public boolean isSingleLicenceCustomer(){
        return this.associations.size() == 1 && this.associations.get(0).getAssocType() == AssociationType.SINGLE_LICENCE;
    }

    
    @Override
    public String getMessagesSummary(){
        StringBuilder sb = new StringBuilder();
        for(Message msg : this.messages){
            sb.append("  ");
            sb.append(msg);
            sb.append(NEWLINE);
        }
        return sb.toString();        
    }
    
    @Override
    public String getAssocationsSummary(){
        StringBuilder sb = new StringBuilder();
        for(Association assoc : this.associations){
            sb.append("  ");
            sb.append(assoc);
            sb.append(NEWLINE);
        }
        return sb.toString();
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();        
        sb.append(getSummary());
        sb.append(getMessagesSummary());
        sb.append(getAssocationsSummary());
        return sb.toString();
    }

    @Override
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer email[");
        sb.append(customer.getEmailAddress());
        sb.append("] id[");
        sb.append(customer.getId());
        sb.append("] isError?[");
        sb.append(isError());
        sb.append("] isSingleCustomerLicence?[");
        sb.append(isSingleLicenceCustomer());
        sb.append("]");
        sb.append(String.format("%n  %d licences %d assocations %d messages ", this.licences.size(), this.associations.size(), this.messages.size()));
        return sb.toString();
    }

	@Override
	public int getCustomerLicenceCount() {
		return this.licences.size();
	}

	@Override
	public List<String> getSql(String prefix){
		List<String> result = new ArrayList<String>();
		 
		String sqlComment = String.format("-- Customer : %s : isError?[%b] : username[%s] id[%s]",prefix, isError(), customer.getUsername(), customer.getId());
		result.add(sqlComment);//comment at start
		
		if(this.isError() == false){
			
			String deleteSqlFormat = "delete from linked_registration where id in"
				+ " ("
				+ "    select LR.id from linked_registration LR"
				+ "    join registration R on R.id = LR.registration_id"
				+ "    join customer C on R.customer_id = C.id"
				+ "    where C.id='%s'"
				+ " );";

			String deleteSql = String.format(deleteSqlFormat,customer.getId());
			result.add(deleteSql);
		
			for(Association assoc : this.associations){/*
			
				Integer eRightsLicenceId = assoc.getLicenceDto().getErightsId();
				String regId = assoc.getRegistration().getId();
				Product prod = assoc.getProduct();			
				if(prod instanceof RegisterableProduct){
				
					String sql = String.format("update registration set erights_licence_id = %d where id = '%s';", eRightsLicenceId, regId);
					result.add(sql);
				}
			
				if(prod instanceof LinkedProduct){
					String linkProdId = prod.getId();
					String newId = UUID.randomUUID().toString();
					String sql = String.format("insert into linked_registration (id, obj_version, registration_id, linked_product_id, erights_licence_id) values ('%s',0,'%s','%s','%s');", newId, regId, linkProdId, eRightsLicenceId);
					result.add(sql);
				}
			*/}
		}
		
		result.add("");//blank line
		
		return result;
	}

}