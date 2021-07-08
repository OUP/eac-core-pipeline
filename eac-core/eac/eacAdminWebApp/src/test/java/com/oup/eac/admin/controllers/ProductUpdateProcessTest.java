package com.oup.eac.admin.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.hamcrest.core.IsInstanceOf;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.CountryMatchRegistrationActivation;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.beans.ProductBean;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.process.ProductUpdateProcess;

public class ProductUpdateProcessTest {
	
    private static final String EXTERNAL_SYS_NAME = "dlp";
    
	private final ProductService mockProductService = EasyMock.createMock(ProductService.class);
	private final ExternalIdService mockExternalIdService = EasyMock.createMock(ExternalIdService.class);
	private ProductBean productBean;
	private RegisterableProduct product;
	private AccountRegistrationDefinition accountRegistrationDefinition;
	private ProductRegistrationDefinition productRegistrationDefinition;
	
	private final ExternalSystemIdType isbn = new ExternalSystemIdType();
	private final ExternalSystemIdType eacProdId = new ExternalSystemIdType();
	
	private final ProductUpdateProcess productUpdateProcess = new ProductUpdateProcess(mockProductService);
	
	@Before
	public void setUp() throws ServiceLayerException {
		product = new RegisterableProduct();
		product.setId("8a00889a3a97955d013a9795f2af0000");
		accountRegistrationDefinition = new AccountRegistrationDefinition();
		accountRegistrationDefinition.setProduct(product);
		accountRegistrationDefinition.setValidationRequired(Boolean.FALSE);
		productRegistrationDefinition = new ProductRegistrationDefinition();
		productRegistrationDefinition.setRegistrationActivation(new SelfRegistrationActivation());
		productRegistrationDefinition.setConfirmationEmailEnabled(Boolean.TRUE);
		productRegistrationDefinition.setLicenceTemplate(new ConcurrentLicenceTemplate());
		productRegistrationDefinition.setProduct(product);
		
		Map<ExternalSystem, List<ExternalSystemIdType>> externalSystemMap = new HashMap<ExternalSystem, List<ExternalSystemIdType>>();
		ExternalSystem extsys1 = new ExternalSystem();
		extsys1.setName(EXTERNAL_SYS_NAME);
		extsys1.setId("12505F07-2FA5-4011-9DAD-DF9D3E5B708B");
		isbn.setName("isbn");
		isbn.setId("17DC2E27-801C-42D2-B7B2-85ECAD7DD188");
		isbn.setExternalSystem(extsys1);
		externalSystemMap.put(extsys1, Arrays.asList(isbn));
		
		ExternalSystem otc = new ExternalSystem();
		otc.setName("otc");
		otc.setId("1F0D7133-5D06-418F-AEC5-966C387D0CCB");
		eacProdId.setName("eacprodid");
		eacProdId.setId("251B3C47-AC92-463D-A33D-CC7054912AF1");
		eacProdId.setExternalSystem(otc);
		externalSystemMap.put(otc, Arrays.asList(eacProdId));
		
		ExternalProductId externalProductId1 = new ExternalProductId();
		externalProductId1.setId("6F9C4166-F1FD-4B0F-B098-C77787675B5A");
		externalProductId1.setExternalId("foobar");
		externalProductId1.setExternalSystemIdType(isbn);
		
		ExternalProductId externalProductId2 = new ExternalProductId();
		externalProductId2.setId("76620B77-C4EB-4372-B8BA-AA71E4916DA3");
		externalProductId2.setExternalId("hello");
		externalProductId2.setExternalSystemIdType(eacProdId);

		product.setExternalIds(new HashSet<ExternalProductId>(Arrays.asList(externalProductId1, externalProductId2)));
		EnforceableProductDto enfoProductDto = new EnforceableProductDto(); 
		productBean = new ProductBean(product, accountRegistrationDefinition, productRegistrationDefinition, enfoProductDto, null, null);
		productBean.setProduct(product);
		productBean.setEmail("somebody@somewhere.com");
		productBean.setLicenceType(LicenceType.CONCURRENT);
		productBean.setAccountPageDefinitions(new ArrayList<AccountPageDefinition>());
		productBean.setProductPageDefinitions(new ArrayList<ProductPageDefinition>());
		productBean.setRegistrationActivations(new ArrayList<RegistrationActivation>());
		productBean.setExternalSystemMap(externalSystemMap);
		productBean.setRegistrationType("SELF_REGISTERABLE");
		
		EasyMock.expect(mockProductService.getProductById("8a00889a3a97955d013a9795f2af0000")).andReturn(product);
		
	}
	
	@Test
	public void shouldUpdateBasicProductProperties() throws UnsupportedEncodingException, ServiceLayerException {
		String newProductId = "8a00889a3a97955d013a9795f2af0000";
		productBean.setRegistrationType("ADMIN_REGISTERABLE");
		productBean.setHomePage("homePage");
		productBean.setLandingPage("landingPage");
		productBean.setEmail("someoneelse@somewhere.com");
		productBean.setSla("sla");
		
		EasyMock.expect(mockProductService.getProductById(newProductId)).andReturn(product);
		EasyMock.replay(mockProductService, mockExternalIdService);
		
		productUpdateProcess.process(productBean);
		
		assertThat(product.getId(), equalTo(newProductId));
		//assertThat(product.getRegisterableType(), equalTo(RegisterableType.ADMIN_REGISTERABLE));
		//assertThat(product.getHomePage(), equalTo("homePage"));
		//assertThat(product.getLandingPage(), equalTo("landingPage"));
		//assertThat(product.getEmail(), equalTo("someoneelse@somewhere.com"));
		//assertThat(product.getServiceLevelAgreement(), equalTo("sla"));
		assertThat(accountRegistrationDefinition.getPageDefinition(), nullValue());
		assertThat(productRegistrationDefinition.getPageDefinition(), nullValue());
		//assertThat(productRegistrationDefinition.getRegistrationActivation() instanceof SelfRegistrationActivation, equalTo(true));
	}
	
	@Test
	public void shouldUpdateAccountRegistrationDefinitionProperties() throws UnsupportedEncodingException, ServiceLayerException {
		String pageDefinitionId = "CA60C86E-3E25-459F-9123-AFB4297A0566";
		List<AccountPageDefinition> pageDefinitions = new ArrayList<AccountPageDefinition>();
		AccountPageDefinition pageDef = new AccountPageDefinition();
		pageDef.setId(pageDefinitionId);
		pageDefinitions.add(pageDef);
		productBean.setAccountPageDefinitionId(pageDefinitionId);
		productBean.setAccountPageDefinitions(pageDefinitions);
		productBean.setEmailValidation(true);
		
		productUpdateProcess.process(productBean);
		
		assertThat(accountRegistrationDefinition.getPageDefinition(), notNullValue());
		assertThat(accountRegistrationDefinition.isValidationRequired(), equalTo(true));
	}
	
	@Test
	public void shouldUpdateProductRegistrationDefinitionProperties() throws UnsupportedEncodingException, ServiceLayerException {
		String pageDefinitionId = "D4A2D733-E9DB-4C1D-968B-DC055A865DDC";
		List<ProductPageDefinition> pageDefinitions = new ArrayList<ProductPageDefinition>();
		ProductPageDefinition pageDef = new ProductPageDefinition();
		pageDef.setId(pageDefinitionId);
		pageDefinitions.add(pageDef);
		productBean.setProductPageDefinitionId(pageDefinitionId);
		productBean.setProductPageDefinitions(pageDefinitions);
		
		productUpdateProcess.process(productBean);
		
		assertThat(productRegistrationDefinition.getPageDefinition(), notNullValue());
	}


}