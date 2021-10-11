package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.admin.beans.ExternalSystemBean;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;

public class ExternalSystemBeanValidatorTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private ExternalSystemBeanValidator validator; private ExternalIdService
	 * externalIdService; private Errors mockErrors;
	 * 
	 * public ExternalSystemBeanValidatorTest() throws NamingException { super(); }
	 * 
	 * @Before public void setup(){ externalIdService =
	 * createAndAddMock(ExternalIdService.class); mockErrors =
	 * createAndAddMock(Errors.class); validator = new
	 * ExternalSystemBeanValidator(externalIdService); }
	 * 
	 * @Test public void shouldRejectWhenSystemIdEmpty() { ExternalSystem
	 * externalSystem = new ExternalSystem(); externalSystem.setId("12345");
	 * externalSystem.setName(""); externalSystem.setDescription("description");
	 * ExternalSystemBean externalSystemBean = new
	 * ExternalSystemBean(Arrays.asList(externalSystem));
	 * externalSystemBean.setSelectedExternalSystemGuid("12345");
	 * 
	 * try {
	 * EasyMock.expect(externalIdService.getAllExternalSystemsOrderedByName()).
	 * andReturn(Arrays.asList(externalSystem)); } catch (ErightsException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * mockErrors.rejectValue("", "error.emptyExternalSystemId");
	 * mockErrors.rejectValue("", "error.externalSystemTypeIdRequired");
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * validator.validate(externalSystemBean, mockErrors);
	 * 
	 * EasyMock.verify(getMocks()); }
	 * 
	 * @Test public void shouldRejectWhenSystemTypeIdEmpty() { ExternalSystem
	 * externalSystem = new ExternalSystem(); externalSystem.setId("12345");
	 * externalSystem.setName("abc"); externalSystem.setDescription("description");
	 * ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType();
	 * externalSystemIdType.setName(null);
	 * externalSystemIdType.setDescription("Desc");
	 * externalSystem.setExternalSystemIdTypes(new
	 * HashSet<ExternalSystemIdType>(Arrays.asList(externalSystemIdType)));
	 * ExternalSystemBean externalSystemBean = new
	 * ExternalSystemBean(Arrays.asList(externalSystem));
	 * externalSystemBean.setSelectedExternalSystemGuid("12345");
	 * 
	 * 
	 * 
	 * try {
	 * EasyMock.expect(externalIdService.getAllExternalSystemsOrderedByName()).
	 * andReturn(Arrays.asList(externalSystem));
	 * //EasyMock.expect(externalIdService.getAllExternalSystemsOrderedByName()).
	 * andReturn(Arrays.asList(externalSystem));
	 * EasyMock.expect(externalIdService.getExternalSystemIdTypesOrderedByName(
	 * externalSystem)).andReturn(Arrays.asList(externalSystemIdType));
	 * 
	 * } catch (ErightsException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * mockErrors.rejectValue("", "error.emptyExternalSystemTypeId");
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * validator.validate(externalSystemBean, mockErrors);
	 * 
	 * EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 * @Test public void shouldRejectWhenNewSystemIdEmpty() { ExternalSystem
	 * externalSystem = new ExternalSystem(); externalSystem.setName("foobar");
	 * externalSystem.setDescription("description"); ExternalSystemBean
	 * externalSystemBean = new ExternalSystemBean(Arrays.asList(externalSystem));
	 * externalSystemBean.setNewExternalSystemName("");
	 * externalSystemBean.setSelectedExternalSystemGuid(ExternalSystemBean.NEW);
	 * 
	 * 
	 * try {
	 * EasyMock.expect(externalIdService.getAllExternalSystemsOrderedByName()).
	 * andReturn(Arrays.asList(externalSystem)); } catch (ErightsException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * mockErrors.rejectValue("", "error.emptyExternalSystemId");
	 * mockErrors.rejectValue("", "error.externalSystemTypeIdRequired");
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * validator.validate(externalSystemBean, mockErrors);
	 * 
	 * EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 * @Test public void shouldRejectWhenNewSystemIdAlreadyInUse() { ExternalSystem
	 * externalSystem = new ExternalSystem(); externalSystem.setName("foobar");
	 * externalSystem.setDescription("description"); ExternalSystemBean
	 * externalSystemBean = new ExternalSystemBean(Arrays.asList(externalSystem));
	 * externalSystemBean.setNewExternalSystemName("foobar");
	 * externalSystemBean.setSelectedExternalSystemGuid(ExternalSystemBean.NEW);
	 * 
	 * 
	 * try {
	 * EasyMock.expect(externalIdService.getAllExternalSystemsOrderedByName()).
	 * andReturn(Arrays.asList(externalSystem)); } catch (ErightsException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * mockErrors.rejectValue("", "error.externalSystemIdInUse");
	 * mockErrors.rejectValue("", "error.externalSystemTypeIdRequired");
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * validator.validate(externalSystemBean, mockErrors);
	 * 
	 * EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 * @Test public void shouldRejectWhenNewSystemTypeIdIsDuplicateOnlyIfModified()
	 * { ExternalSystem externalSystem = new ExternalSystem();
	 * Set<ExternalSystemIdType> externalSystemIdTypes = new
	 * HashSet<ExternalSystemIdType>();
	 * 
	 * externalSystem.setId("12345"); externalSystem.setName("foobar");
	 * externalSystem.setDescription("description");
	 * 
	 * ExternalSystemIdType externalSystemIdType1 = new ExternalSystemIdType();
	 * externalSystemIdType1.setName("a_name");
	 * externalSystemIdTypes.add(externalSystemIdType1);
	 * 
	 * ExternalSystemIdType externalSystemIdType2 = new ExternalSystemIdType();
	 * externalSystemIdType2.setName("b_name");
	 * externalSystemIdType2.setDescription("b_desc");
	 * externalSystemIdTypes.add(externalSystemIdType2);
	 * 
	 * externalSystem.setExternalSystemIdTypes(externalSystemIdTypes);
	 * 
	 * ExternalSystemBean externalSystemBean = new
	 * ExternalSystemBean(Arrays.asList(externalSystem));
	 * externalSystemBean.getNewExternalSystemIdTypes().get(0).setName("a_name");
	 * externalSystemBean.getNewExternalSystemIdTypes().get(1).setName("b_name");
	 * externalSystemBean.setSelectedExternalSystemGuid("12345");
	 * 
	 * try {
	 * EasyMock.expect(externalIdService.getAllExternalSystemsOrderedByName()).
	 * andReturn(Arrays.asList(externalSystem));
	 * EasyMock.expect(externalIdService.getExternalSystemIdTypesOrderedByName(
	 * externalSystem)).andReturn(new
	 * ArrayList<ExternalSystemIdType>(externalSystemIdTypes)); } catch
	 * (ErightsException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * //mockErrors.rejectValue("", "error.externalSystemTypeIdDuplicate");
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * validator.validate(externalSystemBean, mockErrors);
	 * 
	 * EasyMock.verify(getMocks()); }
	 * 
	 * @Test public void shouldRejectWhenNewSystemTypeIdEmpty() {
	 * 
	 * ExternalSystem externalSystem = new ExternalSystem();
	 * 
	 * externalSystem.setId("12345"); externalSystem.setName("foobar");
	 * externalSystem.setDescription("description");
	 * 
	 * ExternalSystemIdType externalSystemIdType = new ExternalSystemIdType();
	 * externalSystemIdType.setName("");
	 * externalSystemIdType.setDescription("desc");
	 * externalSystem.setExternalSystemIdTypes(new
	 * HashSet<ExternalSystemIdType>(Arrays.asList(externalSystemIdType)));
	 * 
	 * 
	 * ExternalSystemBean externalSystemBean = new
	 * ExternalSystemBean(Arrays.asList(externalSystem));
	 * externalSystemBean.getNewExternalSystemIdTypes().get(0).setName("");
	 * externalSystemBean.getNewExternalSystemIdTypes().get(0).
	 * setDescription("a desc");
	 * externalSystemBean.setSelectedExternalSystemGuid("12345");
	 * 
	 * try {
	 * EasyMock.expect(externalIdService.getAllExternalSystemsOrderedByName()).
	 * andReturn(Arrays.asList(externalSystem));
	 * 
	 * EasyMock.expect(externalIdService.getExternalSystemIdTypesOrderedByName(
	 * externalSystem)).andReturn(Arrays.asList(externalSystemIdType)); } catch
	 * (ErightsException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * mockErrors.rejectValue("", "error.emptyExternalSystemTypeId");
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * validator.validate(externalSystemBean, mockErrors);
	 * 
	 * EasyMock.verify(getMocks()); }
	 */}
