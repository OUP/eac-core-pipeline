package com.oup.eac.data;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.oup.eac.common.utils.activationcode.ActivationCodeGenerator;
import com.oup.eac.common.utils.activationcode.EacNumericActivationCode;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.Permission;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Role;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.ValidatedRegistrationActivation;

public class DomainDataTest extends AbstractDBTest {

	private StandardLicenceTemplate standardLicenceTemplate;
	//private ActivationCodeBatch activationCodeBatch;
	private RegisterableProduct registerableProduct;
	private InstantRegistrationActivation instantRegistrationActivation;
	private SelfRegistrationActivation selfRegistrationActivation;
	private ValidatedRegistrationActivation validatedRegistrationActivation;
	private ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition;
	private ProductRegistrationDefinition productRegistrationDefinition;
	private ProductPageDefinition productPageDefinition;
	private AccountPageDefinition accountPageDefinition;
	private Customer customer;
	private Role role;
	private Permission permission;

	private ActivationCodeGenerator eacNumericActivationCodeGenerator = new EacNumericActivationCode();
    
    /**
     * @throws Exception
     *             Sets up data ready for test.
     */
    @Before
    public final void setUp() throws Exception {
    	standardLicenceTemplate = getSampleDataCreator().createStandardLicenceTemplate();
    	//instantRegistrationActivation = getSampleDataCreator().createInstantRegistrationActivation();
    	//selfRegistrationActivation = getSampleDataCreator().createSelfRegistrationActivation();
    //	validatedRegistrationActivation = getSampleDataCreator().createValidatedRegistrationActivation();
    	
    	registerableProduct = getSampleDataCreator().createRegisterableProduct(1235, "malaysia 1", RegisterableType.SELF_REGISTERABLE);
    	productPageDefinition = getSampleDataCreator().createProductPageDefinition();
    	accountPageDefinition = getSampleDataCreator().createAccountPageDefinition();
    	activationCodeRegistrationDefinition = getSampleDataCreator().createActivationCodeRegistrationDefinition(registerableProduct, standardLicenceTemplate, instantRegistrationActivation, productPageDefinition);
    	//activationCodeBatch = getSampleDataCreator().createActivationCodeBatch(standardLicenceTemplate, ActivationCodeFormat.EAC_NUMERIC, activationCodeRegistrationDefinition, null, null);
    	//ActivationCode activationCode = getSampleDataCreator().createActivationCode(activationCodeBatch, eacNumericActivationCodeGenerator);
    	//getSampleDataCreator().createLinkedProduct(registerableProduct, 12344);
    	//getSampleDataCreator().createLinkedProduct(registerableProduct,12346);
    	//customer = getSampleDataCreator().createCustomer();
    	productRegistrationDefinition = getSampleDataCreator().createProductRegistrationDefinition(registerableProduct, standardLicenceTemplate, selfRegistrationActivation, productPageDefinition);
    	getSampleDataCreator().createAccountRegistrationDefinition(registerableProduct, validatedRegistrationActivation, accountPageDefinition);
    	//getSampleDataCreator().createRegistration(customer, productRegistrationDefinition);
    	//getSampleDataCreator().createActivationCodeRegistration(customer, activationCodeRegistrationDefinition, activationCode);
    	Role role = getSampleDataCreator().createRole("TEST_ROLE");
    	role.getPermissions().add(getSampleDataCreator().createPermission("TEST_PERMISSION1"));
    	role.getPermissions().add(getSampleDataCreator().createPermission("TEST_PERMISSION2"));
    	role.getPermissions().add(getSampleDataCreator().createPermission("TEST_PERMISSION3"));
    	getSampleDataCreator().createRolePermissions(role);
    	loadAllDataSets();
    }
    
    @Test
    public void testDomain() throws Exception {
    	/*assertEquals(1, countRowsInTable("LICENCE_TEMPLATE"));
    	assertEquals(1, countRowsInTable("ACTIVATION_CODE")); 
    	assertEquals(3, countRowsInTable("PRODUCT"));
    	assertEquals(3, countRowsInTable("REGISTRATION_ACTIVATION"));*/
    	assertEquals(3, countRowsInTable("REGISTRATION_DEFINITION"));
    	//assertEquals(2, countRowsInTable("REGISTRATION"));
    	assertEquals(2, countRowsInTable("PAGE_DEFINITION"));
    	assertEquals(1, countRowsInTable("ROLE"));
    	assertEquals(3, countRowsInTable("PERMISSION"));
    	assertEquals(3, countRowsInTable("ROLE_PERMISSIONS"));
    }
}
