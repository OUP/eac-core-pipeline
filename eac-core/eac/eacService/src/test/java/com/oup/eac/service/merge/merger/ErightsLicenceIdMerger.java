package com.oup.eac.service.merge.merger;

import static org.apache.log4j.Level.ERROR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LinkedProduct;
//import com.oup.eac.domain.LinkedProduct.ActivationMethod;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.service.merge.RegistrationLicenceMergeInfoDto;

public class ErightsLicenceIdMerger {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ErightsLicenceIdMerger.class);

    private final MergeContext ctx;

    /**
     * Constructor.
     * 
     * @param licenceService
     * @param registrationDao
     */
    public ErightsLicenceIdMerger(CustomerRegistrationsDto customerRegDto) {
        List<LicenceDto> licences = customerRegDto.getLicences();
        List<Registration<? extends ProductRegistrationDefinition>> registrations = customerRegDto.getRegistrations();
        this.ctx = new MergeContext(customerRegDto.getUser(), licences, registrations);
    }

    /**
     * This method merges the registration information for a customer with the licence dtos for the customer
     * 
     * @param customer
     *            the customer
     * @return the list of RegistrationLicenceMergeInfoDto structures - one per registration
     */
    public MergeResult performMerge() {

        // map is eRightsProductId -> List<LicenceDto>
        Map<Integer, List<LicenceDto>> licMap = ctx.getLicencesMap();
        
        // map is eRightsProductId -> List<RegistrationLicenceMergeInfoDto>
        Map<Integer, List<RegistrationLicenceMergeInfoDto>> regMap = ctx.getRegistrationsMap();

        
        // process each eRightsProductId in turn
        for (Map.Entry<Integer, List<LicenceDto>> entry : licMap.entrySet()) {
        	
            Integer eRightsProdId = entry.getKey();
            List<LicenceDto> dtos = entry.getValue();
            if (dtos.size() == 1) {
                // the eRightsProductId is only on 1 of the customer's licences
                LicenceDto dto = dtos.get(0);
                processSimple(eRightsProdId, dto, regMap.get(eRightsProdId),true);
            } else {                
                // the eRightsProductId is on several of the customer's licences
                processComplex(eRightsProdId, dtos, regMap.get(eRightsProdId));                
            }
        }
        return ctx;
    }

    /**
     * Process complex.
     *
     * @param eRightsProductId the e rights product id
     * @param licDtos the lic dtos
     * @param regDtos the reg dtos
     */
    private void processComplex(final Integer eRightsProductId, final List<LicenceDto> licDtos, final List<RegistrationLicenceMergeInfoDto> regDtos) {
        Map<LicenceTemplateInfo, List<LicenceDto>> licMap = new HashMap<LicenceTemplateInfo, List<LicenceDto>>();
        for (LicenceDto dto : licDtos) {
            LicenceTemplateInfo info = new LicenceTemplateInfo(dto);
            MergeUtils.addMapListEntry(licMap, info, dto);
        }
        Map<LicenceTemplateInfo, List<RegistrationLicenceMergeInfoDto>> regMap = new HashMap<LicenceTemplateInfo, List<RegistrationLicenceMergeInfoDto>>();
        for (RegistrationLicenceMergeInfoDto dto : regDtos) {
            LicenceTemplate lt = dto.getLicenceTemplate();
            LicenceTemplateInfo info = new LicenceTemplateInfo(lt);
            MergeUtils.addMapListEntry(regMap, info, dto);
        }

        Map<LicenceTemplateInfo, List<RegistrationLicenceMergeInfoDto>> filteredRegMap = new HashMap<LicenceTemplateInfo, List<RegistrationLicenceMergeInfoDto>>();
		for (Map.Entry<LicenceTemplateInfo, List<RegistrationLicenceMergeInfoDto>> entry : regMap.entrySet()) {
			List<RegistrationLicenceMergeInfoDto> filtered = filterRegistrations(entry.getValue(), eRightsProductId);
			filteredRegMap.put(entry.getKey(), filtered);
		}
		
		associateMultiplePairsOnLicenceTemplateInfo(eRightsProductId, licMap, filteredRegMap);

        LeftOvers leftovers = new LeftOvers(eRightsProductId, licMap, filteredRegMap);
		leftovers.process();		
		
		if(leftovers.isEmpty() == false){
			int regSize = leftovers.getAllUnmatchedRegs().size();
		
			// we can now do warnings for the unmatched licences and registrations
			for (LicenceDto dto : leftovers.getAllUnmatchedLics()){
				LicenceTemplateInfo temp = new LicenceTemplateInfo(dto);
				ctx.log(Level.WARN, eRightsProductId,"Cannot match licence template [%s] #registrations[%d]", temp.getDescription(), regSize);
			}
		}

    }

    private class LeftOvers{

    	private final Integer eRightsProductId;
        private final List<LicenceDto> allUnmatchedLics;
        private final List<RegistrationLicenceMergeInfoDto> allUnmatchedRegs;

        public LeftOvers(
        			Integer eRightsProductId,
                     Map<LicenceTemplateInfo, List<LicenceDto>> licMap,
                     Map<LicenceTemplateInfo, List<RegistrationLicenceMergeInfoDto>> filteredRegMap) {
               this.allUnmatchedLics = getAllValues(licMap);
               this.allUnmatchedRegs = getAllValues(filteredRegMap);
               this.eRightsProductId = eRightsProductId;
        }

        public boolean isEmpty() {
               return allUnmatchedLics.isEmpty() && allUnmatchedRegs.isEmpty();
        }

        public List<LicenceDto> getAllUnmatchedLics() {
               return allUnmatchedLics;
        }

        public List<RegistrationLicenceMergeInfoDto> getAllUnmatchedRegs() {
               return allUnmatchedRegs;
        }
        
     private <K,V> List<V> getAllValues(Map<K,List<V>> dataMap){
        List<V> result = new ArrayList<V>();
        for(List<V> values : dataMap.values()){
               result.addAll(values);            
        }
        return result;
     }
     
     public void process(){
		ArrayList<LicenceDto> lics = new ArrayList<LicenceDto>(this.allUnmatchedLics);
		for(LicenceDto lic : lics){
			RegistrationLicenceMergeInfoDto matchedReg = processSimple(eRightsProductId, lic, this.allUnmatchedRegs, false);
			if(matchedReg != null){				
				this.allUnmatchedLics.remove(lic);				
		    	this.allUnmatchedRegs.remove(matchedReg);		
			}
		}		
     }

}

    
    



	/**
	 * Process simple.
	 * 
	 * @param eRightsProductId
	 *            the e rights product id
	 * @param licDto
	 *            the lic dto
	 * @param regLicInfoDtos
	 *            the reg lic info dtos
	 */
	private RegistrationLicenceMergeInfoDto processSimple(Integer eRightsProductId, LicenceDto licDto,
			List<RegistrationLicenceMergeInfoDto> regLicInfoDtos, boolean isStrict) {
		if (regLicInfoDtos == null || regLicInfoDtos.size() == 0) {
			ctx.log(ERROR, eRightsProductId,"There are NO registrations for eRightsLicenceId %d",licDto.getLicenseId());
		} else {
			if (regLicInfoDtos.size() == 1) {
				RegistrationLicenceMergeInfoDto regLicInfoDto = regLicInfoDtos.get(0);
				associate(AssociationType.SINGLE_LICENCE, eRightsProductId,licDto, regLicInfoDto);
				return regLicInfoDto;
			} else {
				// see if there's a single activated licence
				List<RegistrationLicenceMergeInfoDto> activated = new ArrayList<RegistrationLicenceMergeInfoDto>();
				for (RegistrationLicenceMergeInfoDto regLicInfoDto : regLicInfoDtos) {
					if (regLicInfoDto.getRegistration().isActivated()) {
						activated.add(regLicInfoDto);
					}
				}
				if (activated.size() == 0) {
					// see if there's a single awaiting validation licence
					List<RegistrationLicenceMergeInfoDto> awaitValidation = new ArrayList<RegistrationLicenceMergeInfoDto>();
					for (RegistrationLicenceMergeInfoDto regLicInfoDto : regLicInfoDtos) {
						if (regLicInfoDto.getRegistration().isAwaitingValidation()) {
							awaitValidation.add(regLicInfoDto);
						}
					}
					if (awaitValidation.size() == 1) {
						RegistrationLicenceMergeInfoDto regLicInfoDto = awaitValidation.get(0);
						associate(AssociationType.SINGLE_LICENCE, eRightsProductId, licDto, regLicInfoDto);
						return regLicInfoDto;
					} else {
						if(isStrict){
							ctx.log(ERROR,eRightsProductId,"There is a single eRightsLicenceId %d but 0 activated registrations but not 1 awaiting validation : %d",licDto.getLicenseId(), awaitValidation.size());
						}else{
							RegistrationLicenceMergeInfoDto reg = awaitValidation.get(0);
							associate(AssociationType.SINGLE_LICENCE, eRightsProductId, licDto, reg);
							return reg;
						}
					}
				} else if (activated.size() == 1) {
					RegistrationLicenceMergeInfoDto regLicInfoDto = activated.get(0);
					associate(AssociationType.SINGLE_LICENCE, eRightsProductId, licDto, regLicInfoDto);
					return regLicInfoDto;
				} else {
					if(isStrict){
						ctx.log(ERROR,eRightsProductId,"There is a single eRightsLicenceId %d but more than 1 activated registration : %d",licDto.getLicenseId(), regLicInfoDtos.size());
					}else{
						RegistrationLicenceMergeInfoDto reg = activated.get(0);
						associate(AssociationType.SINGLE_LICENCE, eRightsProductId, licDto, reg);
						return reg;
					}
				}
			}
		}
		return null;
	}

    /**
     * Associates a licence with a registration. Works on the assumption that each licence only refers to a single eRightsProductId.
     *
     * @param assocType the assoc type
     * @param eRightsProductId the e rights product id
     * @param licDto the licence dto
     * @param regLicInfoDto the wrapped registration
     */
    private void associate(AssociationType assocType,
                           Integer eRightsProductId,
                           LicenceDto licDto,
                           RegistrationLicenceMergeInfoDto regLicInfoDto) {
                           String eRightsLicenceId = licDto.getLicenseId();

        Assert.assertEquals(eRightsProductId, licDto.getProductIds().get(0));
        Registration<? extends ProductRegistrationDefinition> registration = regLicInfoDto.getRegistration();
        
        Product product = getProductInRegistration(registration, eRightsProductId);
            
        if (product instanceof RegisterableProduct) {
           // regLicInfoDto.seteRightsLicenceId(eRightsLicenceId);
            
            ctx.addAssociation(assocType,eRightsProductId,registration,product,licDto);
            
        } else if (product instanceof LinkedProduct) {
            LinkedProduct lp = (LinkedProduct) product;
            Map<Integer, LinkedProduct> linkedRegMap = regLicInfoDto.getLinkedRegistrations();
            //linkedRegMap.put(eRightsLicenceId, lp);
            
            ctx.addAssociation(assocType,eRightsProductId,registration,product,licDto);

        } else {
            if(product == null){
                ctx.log(ERROR, eRightsProductId, "The Product associated with registration id [%s] cannot be found", regLicInfoDto.getRegistration().getId());
            }else{
                ctx.log(ERROR, eRightsProductId, "The Product associated with registration id [%s] is not a RegisterableProduct or LinkedProduct", regLicInfoDto.getRegistration().getId());
            }
        }

    }

    /**
     * Assumes that the products in a registration all have different eRightsProductIds.
     * 
     * @param registration
     * @param eRightsProductId
     * @return
     */
    private Product getProductInRegistration(Registration<? extends ProductRegistrationDefinition> registration, Integer eRightsProductId) {
        Product result = null;
        /*RegisterableProduct rp = registration.getRegistrationDefinition().getProduct();
        if (rp.getErightsId().equals(eRightsProductId)) {
            result = rp;
        } else {
            Set<LinkedProduct> links = rp.getLinkedProducts();
            if (links != null) {
                Iterator<LinkedProduct> iter = links.iterator();
                while (result == null && iter.hasNext()) {
                    LinkedProduct link = iter.next();
                    if (link.getErightsId().equals(eRightsProductId)) {
                        result = link;
                    }
                }
            }
        }*/
        return result;
    }
    
	/**
	 * Associate multiple pairs on licence template info.
	 * 
	 * @param eRightsProductId
	 *            the e rights product id
	 * @param licMap
	 *            the lic map
	 * @param regMap
	 *            the reg map
	 */
	private void associateMultiplePairsOnLicenceTemplateInfo(
			Integer eRightsProductId,
			Map<LicenceTemplateInfo, List<LicenceDto>> licMap,
			Map<LicenceTemplateInfo, List<RegistrationLicenceMergeInfoDto>> filteredRegMap) {


		Set<LicenceTemplateInfo> infos = new HashSet<LicenceTemplateInfo>(licMap.keySet());
		// for each licenceTemplateInfo
		for (LicenceTemplateInfo info : infos) {
			List<LicenceDto> lics = licMap.get(info);
			if (lics != null) {
				List<RegistrationLicenceMergeInfoDto> fRegs = filteredRegMap.get(info);
				TemplateMatchedData matched = associateMultiplePairs(eRightsProductId,lics, fRegs, info);				
				processTemplateMatched(info, info, licMap, filteredRegMap, matched);
			}
		}

	}

	/**
	 * Associate multiple pairs on licence template info.
	 * 
	 * @param eRightsProductId
	 *            the e rights product id
	 * @param info
	 *            the info
	 * @param lics
	 *            the lics
	 * @param regs
	 *            the regs
	 */
	private TemplateMatchedData associateMultiplePairs(Integer eRightsProductId,
			List<LicenceDto> lics, List<RegistrationLicenceMergeInfoDto> regs,
			Object info) {

		TemplateMatchedData result = new TemplateMatchedData();

		if (lics == null) {
			lics = MergeUtils.EMPTY_LICS;
		}
		if (regs == null) {
			regs = MergeUtils.EMPTY_REGS;
		}

		Collections.sort(regs, MergeUtils.REG_DTO_COMP);
		Collections.sort(lics, MergeUtils.LIC_DTO_COMP);

		// pair them off 'in order' (as much as possible)
		int minSize = Math.min(regs.size(), lics.size());

		AssociationType assocType = minSize == 1 ? AssociationType.LICENCE_INFO : AssociationType.PAIRED_LICENCE;

		for (int i = 0; i < minSize; i++) {
			RegistrationLicenceMergeInfoDto reg = regs.get(i);
			LicenceDto lic = lics.get(i);
			associate(assocType, eRightsProductId, lic, reg);
			result.add(lic, reg);
		}

		return result;
	}

	private List<RegistrationLicenceMergeInfoDto> filterRegistrations(
			List<RegistrationLicenceMergeInfoDto> regs, Integer eRightsProductId) {
		List<RegistrationLicenceMergeInfoDto> result = new ArrayList<RegistrationLicenceMergeInfoDto>();
		/*if (regs != null) {

			for (RegistrationLicenceMergeInfoDto reg : regs) {
				boolean addReg = false;
				if (reg.getRegistration().isActivated()) {
					addReg = true;
				} else if (reg.getRegistration().isAwaitingValidation()) {
					Product prod = getProductInRegistration(
							reg.getRegistration(), eRightsProductId);
					if (prod instanceof RegisterableProduct) {
						addReg = true;
					} else if (prod instanceof LinkedProduct) {
						LinkedProduct lp = (LinkedProduct) prod;
						if (lp.getActivationMethod() == ActivationMethod.PRE_PARENT) {
							addReg = true;
						}
					}
				}
				if (addReg) {
					result.add(reg);
				}
			}			
		}*/
		return result;
	}

	private static void processTemplateMatched(
			LicenceTemplateInfo licInfo,
			LicenceTemplateInfo regInfo,
			Map<LicenceTemplateInfo, List<LicenceDto>> licMap,
			Map<LicenceTemplateInfo, List<RegistrationLicenceMergeInfoDto>> regMap,
			TemplateMatchedData md) {
		if (md.isEmpty()) {
			return;
		}
		
		List<LicenceDto> licences = licMap.get(licInfo);
		licences.removeAll(md.getLicences());
		if (licences.size() == 0) {
			licMap.remove(licInfo);
		}

		List<RegistrationLicenceMergeInfoDto> registrations = regMap.get(regInfo);
		registrations.removeAll(md.getRegs());
		if (registrations.size() == 0) {
			regMap.remove(regInfo);
		}
	}

	private class TemplateMatchedData {
		List<LicenceDto> licences = new ArrayList<LicenceDto>();
		List<RegistrationLicenceMergeInfoDto> regs = new ArrayList<RegistrationLicenceMergeInfoDto>();

		public List<LicenceDto> getLicences() {
			return licences;
		}

		public List<RegistrationLicenceMergeInfoDto> getRegs() {
			return regs;
		}

		public void add(LicenceDto lic, RegistrationLicenceMergeInfoDto reg) {
			licences.add(lic);
			regs.add(reg);
		}

		public boolean isEmpty() {
			return this.regs.isEmpty();
		}

		public int size() {
			return regs.size();
		}
	}

}
