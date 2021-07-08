package com.oup.eac.ws.v2.service.impl;

import org.exolab.castor.types.Date;
import org.joda.time.DateTime;

import com.oup.eac.ws.v2.binding.common.ConcurrencyLicenceDetails;
import com.oup.eac.ws.v2.binding.common.ExtendedLicenceDetails;
import com.oup.eac.ws.v2.binding.common.GuestProductEntitlement;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.LicenceDetails;
import com.oup.eac.ws.v2.binding.common.LicenceInfo;
import com.oup.eac.ws.v2.binding.common.ProductDetails;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;
import com.oup.eac.ws.v2.binding.common.RollingLicenceDetail;
import com.oup.eac.ws.v2.binding.common.RollingLicenceInfo;
import com.oup.eac.ws.v2.binding.common.UsageLicenceDetails;
import com.oup.eac.ws.v2.binding.common.UsageLicenceInfo;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.BeginEnum;
import com.oup.eac.ws.v2.binding.common.types.UnitEnum;
import com.oup.eac.ws.v2.service.util.TestIdUtils;

public abstract class BaseDummyAdapter {
    
    protected String getId(WsUserId wsUserId) {
        if (wsUserId.getUserName() != null) {
            return wsUserId.getUserName();
        } else if (wsUserId.getSessionToken() != null) {
            return wsUserId.getSessionToken();
        } else if (wsUserId.getUserId().getInternalId() != null) {
            return wsUserId.getUserId().getInternalId().getId();
        } else if (wsUserId.getUserId().getExternalId() != null) {
            return wsUserId.getUserId().getExternalId().getId();
        } else {
            return "";
        }
    }
    
    protected ProductEntitlement[] getEntitlements(){
        ProductEntitlement[] ents = new ProductEntitlement[2];
        ents[0] = getProductEntitlemResult();
        ents[1] = getProductEntitlement2();
        return ents;
    }
    
    protected ProductEntitlement[] getLinkedEntitlements(){
        ProductEntitlement[] ents = new ProductEntitlement[2];
        ents[0] = getProductEntitlement3();
        ents[1] = getProductEntitlement4();
        return ents;
    }

    public ProductEntitlement getProductEntitlemResult(){
        ProductEntitlement result = new ProductEntitlement();
        result.setActivationCode("AC-123-ABC");
        
        ProductDetails[] prods = new ProductDetails[3];
        
        ProductDetails prod1 = new ProductDetails();
        ProductDetails prod2 = new ProductDetails();
        ProductDetails prod3 = new ProductDetails();
        prods[0] = prod1;
        prods[1] = prod2;
        prods[2] = prod3;
        
        Identifiers ids1 = TestIdUtils.getIds("indProdId1", "extProdId1","system1","ISBN");
        prod1.setProductIds(ids1);
        prod1.setProductName("PRODUCT NUMBER ONE");
        
        Identifiers ids= TestIdUtils.getIds("indProdId2", "extProdId2","system1","ISBN");
        prod2.setProductIds(ids);
        prod2.setProductName("LINKED PROD1");

        Identifiers ids3 = TestIdUtils.getIds("indProdId3", "extProdId3","system1","ISBN");
        prod3.setProductIds(ids3);
        prod3.setProductName("LINKED PROD2");
        
        result.setProduct(prods);
        //ACTIVATION CODE
        result.setActivationCode("ACTIVATION_CODE_123");
        addRollingLicence(result);
        return result;
    }
    
    public ProductEntitlement getProductEntitlement2(){
         ProductEntitlement result = getProductEntitlement(2);
         addUsageLicence(result);
         return result;
    }
    
    public ProductEntitlement getProductEntitlement3(){
        ProductEntitlement result = getProductEntitlement(3);
        addStandardLicence(result);
        return result;
    }
    
    public ProductEntitlement getProductEntitlement4(){
        ProductEntitlement result = getProductEntitlement(4);
        addConcurrentLicence(result);
        return result;
    }

    public void addUsageLicence(ProductEntitlement pe){
        LicenceDetails licDetails = new LicenceDetails();
        licDetails.setEnabled(true);
        Date endDate = new Date(new DateTime(2012,2,2,0,0,0,0).toDate());
        Date startDate = new Date(new DateTime(2011,2,2,0,0,0,0).toDate());
        licDetails.setEndDate(endDate);
        licDetails.setStartDate(startDate);
        UsageLicenceDetails usage = new UsageLicenceDetails();
        usage.setAllowedUsages(12);
        
        UsageLicenceInfo info = new UsageLicenceInfo();
        info.setActive(true);
        info.setExpiryDateAndTime(new DateTime(2011,11,15,0,0,0,0).toDate());
        info.setExpired(false);
        info.setUsagesRemaining(11);
        
        pe.setInfo(info);
        ExtendedLicenceDetails ext = new ExtendedLicenceDetails();
        ext.setUsageLicence(usage);
        licDetails.setExtendedDetails(ext);
        pe.setLicence(licDetails);
    }
    
    public void addRollingLicence(ProductEntitlement pe){
        LicenceDetails licDetails = new LicenceDetails();
        licDetails.setEnabled(true);
        Date endDate = new Date(new DateTime(2012,3,3,0,0,0,0).toDate());
        Date startDate = new Date(new DateTime(2011,3,3,0,0,0,0).toDate());
        licDetails.setEndDate(endDate);
        licDetails.setStartDate(startDate);
        RollingLicenceDetail rolling = new RollingLicenceDetail();
        rolling.setBeginOn(BeginEnum.FIRST_USE);
        rolling.setPeriodUnit(UnitEnum.YEAR);
        rolling.setPeriodValue(2);
        
        RollingLicenceInfo info = new RollingLicenceInfo();
        info.setActive(true);
        info.setExpiryDateAndTime(new DateTime(2011,11,15,0,0,0,0).toDate());
        info.setExpired(false);
        info.setFirstUsedDate(new DateTime(2011,11,15,0,0,0,0).toDate());
        
        pe.setInfo(info);
        ExtendedLicenceDetails ext = new ExtendedLicenceDetails();
        ext.setRollingLicence(rolling);
        licDetails.setExtendedDetails(ext);
        pe.setLicence(licDetails);
    }
    
    public void addConcurrentLicence(ProductEntitlement pe){
        LicenceDetails licDetails = new LicenceDetails();
        licDetails.setEnabled(true);
        Date endDate = new Date(new DateTime(2012,4,4,0,0,0,0).toDate());
        Date startDate = new Date(new DateTime(2011,4,4,0,0,0,0).toDate());
        licDetails.setEndDate(endDate);
        licDetails.setStartDate(startDate);
        ConcurrencyLicenceDetails con = new ConcurrencyLicenceDetails();
        con.setConcurrency(3);
        
        LicenceInfo info = new LicenceInfo();
        info.setActive(true);
        info.setExpiryDateAndTime(new DateTime(2011,11,15,0,0,0,0).toDate());
        info.setExpired(false);
        
        pe.setInfo(info);
        ExtendedLicenceDetails ext = new ExtendedLicenceDetails();
        ext.setConcurrentLicence(con);
        licDetails.setExtendedDetails(ext);
        pe.setLicence(licDetails);
    }
    
    public void addStandardLicence(ProductEntitlement pe){
        LicenceDetails licDetails = new LicenceDetails();
        licDetails.setEnabled(true);
        Date endDate = new Date(new DateTime(2012,5,5,0,0,0,0).toDate());
        Date startDate = new Date(new DateTime(2011,5,5,0,0,0,0).toDate());
        licDetails.setEndDate(endDate);
        licDetails.setStartDate(startDate);
        
        LicenceInfo info = new LicenceInfo();
        info.setActive(true);
        info.setExpiryDateAndTime(new DateTime(2011,11,15,0,0,0,0).toDate());
        info.setExpired(false);
        
        pe.setInfo(info);
        
        licDetails.setExtendedDetails(null);
        pe.setLicence(licDetails);
    }

    private ProductEntitlement getProductEntitlement(int id){
        ProductEntitlement result = new ProductEntitlement();
        
        ProductDetails[] prods = new ProductDetails[1];
        
        ProductDetails prod1 = new ProductDetails();
        
        prods[0] = prod1;
        
        Identifiers ids1 = TestIdUtils.getIds("indProdId" + id,null,null,null);
        prod1.setProductIds(ids1);
        prod1.setProductName("PRODUCT " + id);
        result.setProduct(prods);
        return result;
    }
    
    
    protected GuestProductEntitlement[] getGuestEntitlements(){
        GuestProductEntitlement[] ents = new GuestProductEntitlement[2];
        ents[0] = getGuestProductEntitlemResult();
        ents[1] = getGuestProductEntitlement2();
        return ents;
    }
    
    public GuestProductEntitlement getGuestProductEntitlemResult(){
        GuestProductEntitlement result = new GuestProductEntitlement();
        result.setActivationCode("AC-123-ABC");
        
        ProductDetails[] prods = new ProductDetails[3];
        
        ProductDetails prod1 = new ProductDetails();
        ProductDetails prod2 = new ProductDetails();
        ProductDetails prod3 = new ProductDetails();
        prods[0] = prod1;
        prods[1] = prod2;
        prods[2] = prod3;
        
        Identifiers ids1 = TestIdUtils.getIds("indProdId1", "extProdId1","system1","ISBN");
        prod1.setProductIds(ids1);
        prod1.setProductName("PRODUCT NUMBER ONE");
        
        Identifiers ids= TestIdUtils.getIds("indProdId2", "extProdId2","system1","ISBN");
        prod2.setProductIds(ids);
        prod2.setProductName("LINKED PROD1");

        Identifiers ids3 = TestIdUtils.getIds("indProdId3", "extProdId3","system1","ISBN");
        prod3.setProductIds(ids3);
        prod3.setProductName("LINKED PROD2");
        
        result.setProduct(prods);
        //ACTIVATION CODE
        result.setActivationCode("ACTIVATION_CODE_123");
        return result;
    }
    
    public GuestProductEntitlement getGuestProductEntitlement2(){
        GuestProductEntitlement result = getGuestProductEntitlement(2);
        return result;
    }
    
    private GuestProductEntitlement getGuestProductEntitlement(int id){
        GuestProductEntitlement result = new GuestProductEntitlement();
        
        ProductDetails[] prods = new ProductDetails[1];
        
        ProductDetails prod1 = new ProductDetails();
        
        prods[0] = prod1;
        
        Identifiers ids1 = TestIdUtils.getIds("indProdId" + id,null,null,null);
        prod1.setProductIds(ids1);
        prod1.setProductName("PRODUCT " + id);
        result.setProduct(prods);
        return result;
    }
    
}
