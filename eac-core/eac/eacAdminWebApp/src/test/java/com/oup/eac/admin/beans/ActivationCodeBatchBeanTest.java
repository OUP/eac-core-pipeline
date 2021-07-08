package com.oup.eac.admin.beans;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.RegisterableProduct;

public class ActivationCodeBatchBeanTest {

	@Test
	public void shouldReturnExternalIdsFormattedForDisplay() {
		ExternalProductId externalId1 = createExternalId("systemId1", "system1", "extId1");
		ExternalProductId externalId2 = createExternalId("systemId2", "system2", "extId2");
		RegisterableProduct product = new RegisterableProduct();
		product.setExternalIds(new HashSet<ExternalProductId>(Arrays.asList(externalId1, externalId2)));
		ActivationCodeRegistrationDefinition registrationDefinition = new ActivationCodeRegistrationDefinition();
		registrationDefinition.setProduct(product);
		ActivationCodeBatchBean bean = new ActivationCodeBatchBean(new ActivationCodeBatch());
		bean.setActivationCodeRegistrationDefinition(registrationDefinition);

		List<String> externalIdsForDisplay = bean.getExternalIdsForDisplay();
		
		assertThat(externalIdsForDisplay.size(), equalTo(2));
		assertThat(externalIdsForDisplay.get(0), equalTo("system1/systemid1: extId1"));
		assertThat(externalIdsForDisplay.get(1), equalTo("system2/systemid2: extId2"));
	}

	private ExternalProductId createExternalId(final String externalSystemIdTypeName, final String externalSystemName, final String externalIdName) {
		ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType();
		externalSystemIdType.setName(externalSystemIdTypeName);
		ExternalSystem externalSystem = new ExternalSystem();
		externalSystem.setName(externalSystemName);
		externalSystemIdType.setExternalSystem(externalSystem);
		ExternalProductId externalId = new ExternalProductId();
		externalId.setExternalId(externalIdName);
		externalId.setExternalSystemIdType(externalSystemIdType);
		return externalId;
	}
	
}
