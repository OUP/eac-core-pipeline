package com.oup.eac.service.merge.merger;

import java.util.List;

import com.oup.eac.domain.Customer;
import com.oup.eac.service.merge.RegistrationLicenceMergeInfoDto;
import com.oup.eac.service.merge.merger.MergeContext.Association;
import com.oup.eac.service.merge.merger.MergeContext.Message;

public interface MergeResult {

    public List<Message> getMessages();
    public List<Association> getAssociations();
    public boolean isError();
    public Customer getCustomer();
    public List<RegistrationLicenceMergeInfoDto> getRegistrationLicenceInfo();
    public boolean isSingleLicenceCustomer();
    
    public String getSummary();
    public String getMessagesSummary();
    public String getAssocationsSummary();
    public int getCustomerLicenceCount();
	public List<String> getSql(String prefix);
}
