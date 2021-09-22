package com.oup.eac.ws.v2.service.impl.entitlements;

import java.util.Arrays;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.entitlement.ProductDetailsDto;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.LicenceDetails;
import com.oup.eac.ws.v2.binding.common.LicenceInfo;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;
import com.oup.eac.ws.v2.service.entitlements.LicenceData;
import com.oup.eac.ws.v2.service.entitlements.LicenceDtoConverter;

public class EntitlementsAdapterHelperImplTest/* extends AbstractMockTest */ {
	/*
	 * 
	 * private static final String ACTIVATION_CODE = "ACTIVATION_CODE"; private
	 * static final String SYSTEM_ID = "system_id_1"; private static final String
	 * PRODUCT_ONE = "PRODUCT_1"; private static final String PRODUCT_TWO =
	 * "PRODUCT_2"; private static final String PRODUCT_THREE = "PRODUCT_3"; private
	 * static final String PRODUCT_ID_1 = "PROD_ID_1"; private static final String
	 * PRODUCT_ID_2 = "PROD_ID_2"; private static final String PRODUCT_ID_3 =
	 * "PROD_ID_3"; private static final String TYPE_1 = "type_1"; private static
	 * final String TYPE_2 = "type_2"; private static final String TYPE_3 =
	 * "type_3";
	 * 
	 * private static final String EXT_ID_11 = "EXT_ID_11"; private static final
	 * String EXT_ID_21 = "EXT_ID_21"; private static final String EXT_ID_22 =
	 * "EXT_ID_22"; private static final String EXT_ID_23 = "EXT_ID_23";
	 * 
	 * private ExternalSystem extSystem; private ExternalSystemIdType extSysType1;
	 * private ExternalSystemIdType extSysType2; private ExternalSystemIdType
	 * extSysType3;
	 * 
	 * private EntitlementsAdapterHelperImpl sut;
	 * 
	 * private LicenceDtoConverter mLicenceConverter; private Product prod1; private
	 * Product prod2; private Product prod3;
	 * 
	 * private ExternalProductId extId11; private ExternalProductId extId21; private
	 * ExternalProductId extId22; private ExternalProductId extId23;
	 * 
	 * public EntitlementsAdapterHelperImplTest() throws NamingException { super();
	 * }
	 * 
	 * @Before public void setup(){ prod1 = new RegisterableProduct();
	 * prod1.setId(PRODUCT_ID_1); prod1.setProductName(PRODUCT_ONE);
	 * 
	 * prod2 = new RegisterableProduct(); prod2.setId(PRODUCT_ID_2);
	 * prod2.setProductName(PRODUCT_TWO);
	 * 
	 * prod3 = new RegisterableProduct(); prod3.setId(PRODUCT_ID_3);
	 * prod3.setProductName(PRODUCT_THREE);
	 * 
	 * this.mLicenceConverter = EasyMock.createMock(LicenceDtoConverter.class);
	 * this.sut = new EntitlementsAdapterHelperImpl(mLicenceConverter);
	 * this.extSystem= new ExternalSystem(); this.extSystem.setName(SYSTEM_ID);
	 * 
	 * this.extSysType1 = new ExternalSystemIdType();
	 * this.extSysType1.setExternalSystem(this.extSystem);
	 * this.extSysType1.setName(TYPE_1);
	 * 
	 * this.extSysType2 = new ExternalSystemIdType();
	 * this.extSysType2.setExternalSystem(this.extSystem);
	 * this.extSysType2.setName(TYPE_2);
	 * 
	 * this.extSysType3 = new ExternalSystemIdType();
	 * this.extSysType3.setExternalSystem(this.extSystem);
	 * this.extSysType3.setName(TYPE_3);
	 * 
	 * extId11 = new ExternalProductId(); extId11.setProduct(prod1);
	 * extId11.setExternalId(EXT_ID_11);
	 * extId11.setExternalSystemIdType(this.extSysType1);
	 * 
	 * extId21 = new ExternalProductId(); extId21.setProduct(prod1);
	 * extId21.setExternalId(EXT_ID_21);
	 * extId21.setExternalSystemIdType(this.extSysType1);
	 * 
	 * extId22 = new ExternalProductId(); extId22.setProduct(prod2);
	 * extId22.setExternalId(EXT_ID_22);
	 * extId22.setExternalSystemIdType(this.extSysType2);
	 * 
	 * extId23 = new ExternalProductId(); extId23.setProduct(prod3);
	 * extId23.setExternalId(EXT_ID_23);
	 * extId23.setExternalSystemIdType(this.extSysType3);
	 * 
	 * setMocks(this.mLicenceConverter); }
	 * 
	 * @Test public void test() { //IN ProductEntitlementDto dto = new
	 * ProductEntitlementDto(); dto.setActivationCode(ACTIVATION_CODE); LicenceDto
	 * licDto = new LicenceDto(); dto.setLicence(licDto);
	 * 
	 * ProductDetailsDto prodDet1 = new ProductDetailsDto();
	 * prodDet1.setProduct(prod1); prodDet1.setExternalProductIds(null);
	 * 
	 * ProductDetailsDto prodDet2 = new ProductDetailsDto();
	 * prodDet2.setProduct(prod2);
	 * prodDet2.setExternalProductIds(Arrays.asList(extId11));
	 * 
	 * ProductDetailsDto prodDet3 = new ProductDetailsDto();
	 * prodDet3.setProduct(prod3);
	 * prodDet3.setExternalProductIds(Arrays.asList(extId21, extId22, extId23));
	 * 
	 * dto.setProductList(Arrays.asList(prodDet1, prodDet2, prodDet3));
	 * 
	 * //OUT LicenceData licenceData = new LicenceData(); LicenceDetails licDetail =
	 * new LicenceDetails(); licenceData.setDetail(licDetail); LicenceInfo licInfo =
	 * new LicenceInfo(); licenceData.setInfo(licInfo);
	 * 
	 * EasyMock.expect(mLicenceConverter.convertLicenceDto(licDto)).andReturn(
	 * licenceData); EasyMock.replay(getMocks()); ProductEntitlement result =
	 * sut.getProductEntitlement(dto); Assert.assertEquals(ACTIVATION_CODE,
	 * result.getActivationCode());
	 * 
	 * Assert.assertEquals(licInfo, result.getInfo());
	 * Assert.assertEquals(licDetail, result.getLicence());
	 * 
	 * Assert.assertEquals(PRODUCT_ONE, result.getProduct()[0].getProductName());
	 * Assert.assertEquals(PRODUCT_TWO, result.getProduct()[1].getProductName());
	 * Assert.assertEquals(PRODUCT_THREE, result.getProduct()[2].getProductName());
	 * 
	 * Assert.assertEquals(prod1.getId(),
	 * result.getProduct()[0].getProductIds().getId());
	 * 
	 * Assert.assertEquals(prod2.getId(),
	 * result.getProduct()[1].getProductIds().getId());
	 * Assert.assertEquals(prod3.getId(),
	 * result.getProduct()[2].getProductIds().getId());
	 * 
	 * Assert.assertEquals(0,
	 * result.getProduct()[0].getProductIds().getExternal().length);
	 * Assert.assertEquals(1,
	 * result.getProduct()[1].getProductIds().getExternal().length);
	 * Assert.assertEquals(3,
	 * result.getProduct()[2].getProductIds().getExternal().length);
	 * 
	 * 
	 * checkExternalId(SYSTEM_ID, TYPE_1, EXT_ID_11,
	 * result.getProduct()[1].getProductIds().getExternal()[0]);
	 * checkExternalId(SYSTEM_ID, TYPE_1, EXT_ID_21,
	 * result.getProduct()[2].getProductIds().getExternal()[0]);
	 * checkExternalId(SYSTEM_ID, TYPE_2, EXT_ID_22,
	 * result.getProduct()[2].getProductIds().getExternal()[1]);
	 * checkExternalId(SYSTEM_ID, TYPE_3, EXT_ID_23,
	 * result.getProduct()[2].getProductIds().getExternal()[2]);
	 * 
	 * EasyMock.verify(getMocks()); }
	 * 
	 * private void checkExternalId(String system, String type, String id,
	 * ExternalIdentifier wsExtId){ Assert.assertEquals(system,
	 * wsExtId.getSystemId()); Assert.assertEquals(type, wsExtId.getTypeId());
	 * Assert.assertEquals(id, wsExtId.getId()); }
	 * 
	 */}
