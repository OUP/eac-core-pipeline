package com.oup.eac.service.impl.entitlements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;
import com.oup.eac.service.entitlements.ProductEntitlementGroupSource;

@Component("productEntitlementGroupSource")
public class ProductEntitlementGroupSourceImpl implements ProductEntitlementGroupSource {

	/**
	 * This complex method merges the data from the eac database and atypon web service
	 * to create a list of product entitlement groups. The entitlement group
	 * can hold details about products and licences for a single registration IF
	 * eac holds licence ids for the registerable product and the registerable product's associated linked products
	 * and the entitlement for the registerable product is available. 
	 * 
	 * @param registrations
	 *            the list of customer registration, linked regisrations, linked
	 *            product and external ids from the eac database.
	 * @param infos
	 *            the list of licences (and the associated product ids )
	 *            associated with a customer as returned from the atypon web
	 *            service.
	 * @return
	 */
	@Override
	public List<ProductEntitlementGroupDto> getProductEntitlementGroups(
			final List<Registration<? extends ProductRegistrationDefinition>> registrations,
			final List<ProductEntitlementInfoDto> infos) {
	    
		LinkingData linkingData = new LinkingData(registrations);//this holds details about parent child relationships between the licence ids.
		
		Map<String, EntitlementGroup> groupMap = new HashMap<String, EntitlementGroup>();//keyed by eRightsLicenceId
		List<ProductEntitlementInfoDto> children = new ArrayList<ProductEntitlementInfoDto>();//a list of entitlements not placed into the groupMap.

		if (infos != null) {
		    //for each entitlement place it into the groupMap or child list.
			for (ProductEntitlementInfoDto info : infos) {			    
				String licenceId = info.getLicenceId();
				Assert.notNull(licenceId);
				if(linkingData.isParent(licenceId)){
				    //create a group for each parent entitlement.
				    EntitlementGroup group = new EntitlementGroup(info.getEntitlement(), false);
				    groupMap.put(licenceId, group);
				}else{
				    //keep track of the non-parent entitlements
				    children.add(info);
				}
			}
		}

		// process each non-parent entitlement - try and place in parent group if possible other wise create a group.
		Iterator<ProductEntitlementInfoDto> iter = children.iterator();
		while (iter.hasNext()) {
			ProductEntitlementInfoDto child = iter.next();
			ProductEntitlementDto ent = child.getEntitlement();
			String parentId = linkingData.getParentId(child.getLicenceId());
			//the parentId could be null
			EntitlementGroup group = groupMap.get(parentId);	
			if(group != null){
			    //add child to parent group
			    group.addChild(ent);
			}
		}
		
		// convert each 'EntitlementGroup' into a 'ProductEntitlementGroup'
		List<ProductEntitlementGroupDto> result = new ArrayList<ProductEntitlementGroupDto>();
		for (EntitlementGroup group : groupMap.values()) {
			result.add(group.getGroup());
		}
		return result;
	}

	/**
	 * This is a utility class used to build up the list of
	 * ProductEntitlementGroups
	 * 
	 * @author David Hay
	 */
	private static class EntitlementGroup {
		private final ProductEntitlementDto parent;
		private final List<ProductEntitlementDto> children;
		private final boolean orphan;
		
		public EntitlementGroup(ProductEntitlementDto parent, boolean orphan) {
		    this.orphan = orphan;
			this.parent = parent;
			this.children = new ArrayList<ProductEntitlementDto>();
		}

		public void addChild(ProductEntitlementDto child) {
			this.children.add(child);
		}

		public ProductEntitlementGroupDto getGroup() {
			ProductEntitlementGroupDto result = new ProductEntitlementGroupDto();
			result.setEntitlement(parent);
			result.setLinkedEntitlements(children);
			result.setOrphan(orphan);
			return result;
		}

        @SuppressWarnings("unused")
        public boolean isOrphan() {
            return orphan;
        }
		
	};
	
	private static class LinkingData{
	    private Map<String, String> linkToParentMap = new HashMap<String,String>();
	    private Set<String> parentIds = new HashSet<String>();

        public LinkingData(List<Registration<? extends ProductRegistrationDefinition>> registrations) {
            if(registrations != null){
                for (Registration<? extends ProductRegistrationDefinition> registration : registrations) {
                    processRegistration(registration);
                }
            }
        }
        
        private void processRegistration(final Registration<? extends ProductRegistrationDefinition> registration){
            String eRightsLicenceId = registration.getId();
            if (eRightsLicenceId == null) {
                return;
            }
            parentIds.add(eRightsLicenceId);
            // store the parent->parent relationship
            this.linkToParentMap.put(eRightsLicenceId, eRightsLicenceId);
                
            Set<LinkedRegistration> linkedRegistrations = registration.getLinkedRegistrations();
            if (linkedRegistrations == null) {
                return;
            }
            for (LinkedRegistration linkedRegistration : linkedRegistrations) {
                String linkedErightsLicenceId = linkedRegistration.getId();
                // store the child->parent relationship
                linkToParentMap.put(eRightsLicenceId+linkedErightsLicenceId, eRightsLicenceId);
            }
        }
        
        public boolean isParent(String id){
            return this.parentIds.contains(id);
        }
        public String getParentId(String id){
            return this.linkToParentMap.get(id);
        }
	}
	
}
