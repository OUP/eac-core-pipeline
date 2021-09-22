package com.oup.eac.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import com.oup.eac.common.utils.io.DeepCopy;
import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.DivisionAdminUser;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.InstantRegistrationActivation;
import com.oup.eac.domain.OptionsTag;
import com.oup.eac.domain.PageComponent;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Select;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.TagOption;
import com.oup.eac.data.util.SampleDataFactory;

/**
 * @author harlandd PageDefinition hibernate test
 */

public class PageDefinitionHibernateDaoIntegrationTest /* extends AbstractDBTest */ {
	/*
	 * 
	 * private static final Logger LOG =
	 * Logger.getLogger(PageDefinitionHibernateDaoIntegrationTest.class);
	 * 
	 * @Autowired private PageDefinitionDao<ProductPageDefinition>
	 * productPageDefinitionDao;
	 * 
	 * @Autowired private PageDefinitionDao<AccountPageDefinition>
	 * accountPageDefinitionDao;
	 * 
	 * private Division malaysiaDivision; private Division anzDivision; private
	 * Division eltDivision;
	 * 
	 * private ProductPageDefinition productPageDefinition; private
	 * ProductPageDefinition productPageDefinition2; private ProductPageDefinition
	 * productPageDefinition3;
	 * 
	 * private AccountPageDefinition accountPageDefinition; private
	 * AccountPageDefinition accountPageDefinition2; private AccountPageDefinition
	 * accountPageDefinition3;
	 * 
	 * private Field field1, field2, field3, field4;
	 * 
	 * private AdminUser adminUser1; private AdminUser adminUser2;
	 * 
	 * 
	 * 
	 * 
	 *//**
		 * @throws Exception Sets up data ready for test.
		 */
	/*
	 * @Before public final void setUp() throws Exception { StandardLicenceTemplate
	 * standardLicenceTemplate =
	 * getSampleDataCreator().createStandardLicenceTemplate(); malaysiaDivision =
	 * getSampleDataCreator().createDivision("MALAYSIA"); anzDivision =
	 * getSampleDataCreator().createDivision("ANZ"); eltDivision =
	 * getSampleDataCreator().createDivision("ELT"); adminUser1 =
	 * getSampleDataCreator().createAdminUser(); adminUser2 =
	 * getSampleDataCreator().createAdminUser(); DivisionAdminUser divAdmin1 =
	 * getSampleDataCreator().createDivisionAdminUser(malaysiaDivision, adminUser1);
	 * DivisionAdminUser divAdmin2 =
	 * getSampleDataCreator().createDivisionAdminUser(anzDivision, adminUser2);
	 * DivisionAdminUser divAdmin3 =
	 * getSampleDataCreator().createDivisionAdminUser(eltDivision, adminUser2);
	 * Set<DivisionAdminUser> divList1 = new HashSet<DivisionAdminUser>() ;
	 * Set<DivisionAdminUser> divList2 = new HashSet<DivisionAdminUser>() ;
	 * divList1.add(divAdmin1);
	 * 
	 * divList2.add(divAdmin2); divList2.add(divAdmin3);
	 * adminUser1.setDivisionAdminUsers(divList1);
	 * adminUser2.setDivisionAdminUsers(divList2); RegisterableProduct product =
	 * getSampleDataCreator().createRegisterableProduct(Integer.valueOf(1),
	 * "malaysia 1", RegisterableType.SELF_REGISTERABLE);
	 * InstantRegistrationActivation registrationActivation =
	 * getSampleDataCreator().createInstantRegistrationActivation();
	 * accountPageDefinition = SampleDataFactory.createAccountPageDefinition();
	 * accountPageDefinition.setName("first account page");
	 * accountPageDefinition.setDivisionErightsId(divAdmin1.getDivisionErightsId());
	 * getSampleDataCreator().loadAccountPageDefinition(accountPageDefinition);
	 * accountPageDefinition2 = SampleDataFactory.createAccountPageDefinition();
	 * accountPageDefinition2.setName("second account page");
	 * getSampleDataCreator().loadAccountPageDefinition(accountPageDefinition2);
	 * accountPageDefinition3 = SampleDataFactory.createAccountPageDefinition();
	 * accountPageDefinition3.setName("third account page");
	 * getSampleDataCreator().loadAccountPageDefinition(accountPageDefinition3);
	 * productPageDefinition = SampleDataFactory.createProductPageDefinition();
	 * productPageDefinition.setName("first product page");
	 * getSampleDataCreator().loadProductPageDefinition(productPageDefinition);
	 * productPageDefinition2 = SampleDataFactory.createProductPageDefinition();
	 * productPageDefinition2.setName("second product page");
	 * getSampleDataCreator().loadProductPageDefinition(productPageDefinition2);
	 * productPageDefinition3 = SampleDataFactory.createProductPageDefinition();
	 * productPageDefinition3.setName("third product page");
	 * getSampleDataCreator().loadProductPageDefinition(productPageDefinition3);
	 * 
	 * getSampleDataCreator().createProductRegistrationDefinition(product,
	 * standardLicenceTemplate, registrationActivation, productPageDefinition);
	 * getSampleDataCreator().createAccountRegistrationDefinition(product,
	 * registrationActivation, accountPageDefinition); Component component =
	 * getSampleDataCreator().createComponent("label.marketinginformation");
	 * getSampleDataCreator().createPageComponent(productPageDefinition, component,
	 * 1); Component accountComponent =
	 * getSampleDataCreator().createComponent("label.marketinginformation");
	 * getSampleDataCreator().createPageComponent(accountPageDefinition,
	 * accountComponent, 1); Question question =
	 * getSampleDataCreator().createQuestion(); Element element1 =
	 * getSampleDataCreator().createElement(question); field1 =
	 * getSampleDataCreator().createField(component, element1, 1); Select select =
	 * getSampleDataCreator().createSelect(element1);
	 * getSampleDataCreator().createTagOption(select);
	 * getSampleDataCreator().createTagOption(select);
	 * 
	 * Question question2 = getSampleDataCreator().createQuestion(); Element
	 * element2 = getSampleDataCreator().createElement(question2); Select select2 =
	 * getSampleDataCreator().createSelect(element2);
	 * getSampleDataCreator().createTagOption(select2);
	 * getSampleDataCreator().createTagOption(select2); field2 =
	 * getSampleDataCreator().createField(component, element2, 2); field3 =
	 * getSampleDataCreator().createField(accountComponent, element1, 1); field4 =
	 * getSampleDataCreator().createField(accountComponent, element2, 2);
	 * loadAllDataSets(); }
	 * 
	 *//**
		 * Tests getting a page definition.
		 */
	/*
	 * @Test public final void testGetProductPageDefinition() { final StopWatch
	 * timer = new StopWatch(); timer.start("Load product page definition");
	 * ProductPageDefinition regPage =
	 * productPageDefinitionDao.getPageDefinitionById(productPageDefinition.getId())
	 * ; ((StopWatch)timer).stop(); LOG.info(timer.prettyPrint());
	 * 
	 * assertNotNull(regPage); for (PageComponent pageComponent :
	 * regPage.getPageComponents()) { assertEquals(2,
	 * pageComponent.getComponent().getFields().size());
	 * 
	 * Field f1 = getField(pageComponent.getComponent().getFields(), field1); Field
	 * f2 = getField(pageComponent.getComponent().getFields(), field2);
	 * 
	 * assertTrue(compareQuestion(f1, field1)); assertTrue(compareQuestion(f2,
	 * field2));
	 * 
	 * for (Field field : pageComponent.getComponent().getFields()) {
	 * assertNotNull(field.getElement().getQuestion().getDescription()); } }
	 * assertEquals(productPageDefinition.getId(), regPage.getId()); }
	 * 
	 * @Test public final void testGetAccountPageDefinition() {
	 * AccountPageDefinition regPage =
	 * accountPageDefinitionDao.getPageDefinitionById(accountPageDefinition.getId())
	 * ; assertNotNull(regPage); for (PageComponent pageComponent :
	 * regPage.getPageComponents()) { assertEquals(2,
	 * pageComponent.getComponent().getFields().size());
	 * 
	 * Field f3 = getField(pageComponent.getComponent().getFields(), field3); Field
	 * f4 = getField(pageComponent.getComponent().getFields(), field4);
	 * 
	 * assertTrue(compareQuestion(f3, field3)); assertTrue(compareQuestion(f4,
	 * field4));
	 * 
	 * for (Field field : pageComponent.getComponent().getFields()) {
	 * assertNotNull(field.getElement().getQuestion()); } }
	 * assertEquals(accountPageDefinition.getId(), regPage.getId()); }
	 * 
	 * @Test
	 * 
	 * @Ignore public void testDeepCopy() { StopWatch stopWatch = new StopWatch();
	 * stopWatch.start(); ProductPageDefinition prodCopy1 =
	 * UnoptimizedDeepCopy(productPageDefinition); stopWatch.stop();
	 * LOG.info(stopWatch.prettyPrint()); long unoptimizedTime =
	 * stopWatch.getLastTaskTimeMillis();
	 * 
	 * stopWatch.start(); ProductPageDefinition prodCopy2 =
	 * DeepCopy.copy(productPageDefinition); stopWatch.stop();
	 * LOG.info(stopWatch.prettyPrint());
	 * 
	 * assertTrue("Optimzed deepcopy should be faster.", unoptimizedTime >
	 * stopWatch.getLastTaskTimeMillis());
	 * 
	 * assertEquals(productPageDefinition.getName(), prodCopy1.getName());
	 * assertEquals(productPageDefinition.getPageComponents().size(),
	 * prodCopy1.getPageComponents().size());
	 * assertEquals(productPageDefinition.getPageDefinitionType(),
	 * prodCopy1.getPageDefinitionType());
	 * 
	 * PageComponent pageComponent =
	 * productPageDefinition.getPageComponents().iterator().next(); PageComponent
	 * copyPageComponent = prodCopy1.getPageComponents().iterator().next();
	 * 
	 * assertEquals(pageComponent.getId(), copyPageComponent.getId());
	 * 
	 * Component component = pageComponent.getComponent(); Component copyComponent =
	 * copyPageComponent.getComponent();
	 * 
	 * assertEquals(component.getLabelKey(), copyComponent.getLabelKey());
	 * assertEquals(component.getFields().size(), copyComponent.getFields().size());
	 * 
	 * Field f1 = getField(copyComponent.getFields(), field1); Field f2 =
	 * getField(copyComponent.getFields(), field2);
	 * 
	 * assertTrue(compareQuestion(f1, field1)); assertTrue(compareQuestion(f2,
	 * field2));
	 * 
	 * for(Field field : component.getFields()) { Field copyField =
	 * getField(copyComponent.getFields(), field); assertNotNull(copyField);
	 * assertEquals(field.getId(), copyField.getId());
	 * 
	 * Element element = field.getElement(); Element copyElement =
	 * copyField.getElement();
	 * 
	 * assertNotNull(copyElement); assertEquals(element.getId(),
	 * copyElement.getId()); assertEquals(element.getTitleText(),
	 * copyElement.getTitleText());
	 * 
	 * assertTrue(element.getTags().size() > 0);
	 * assertEquals(element.getTags().size(), copyElement.getTags().size());
	 * 
	 * OptionsTag tag = (OptionsTag)element.getTag(); OptionsTag copyTag =
	 * (OptionsTag)copyElement.getTag();
	 * 
	 * assertEquals(tag.getTagType(), copyTag.getTagType());
	 * assertTrue(tag.getOptions().size() > 0);
	 * assertEquals(tag.getOptions().size(), copyTag.getOptions().size());
	 * 
	 * for(TagOption tagOption : tag.getOptions()) { TagOption copyTagOption =
	 * getTagOption(copyTag.getOptions(), tagOption); assertNotNull(copyTagOption);
	 * assertEquals(tagOption.getId(), copyTagOption.getId());
	 * assertEquals(tagOption.getLabel(), copyTagOption.getLabel());
	 * assertEquals(tagOption.getValue(), copyTagOption.getValue()); } }
	 * 
	 * copyComponent.getFields().clear();
	 * 
	 * assertEquals(0, copyComponent.getFields().size());
	 * assertTrue(copyComponent.getFields().size() != component.getFields().size());
	 * 
	 * assertEquals(productPageDefinition.getName(), prodCopy2.getName());
	 * assertEquals(productPageDefinition.getPageComponents().size(),
	 * prodCopy2.getPageComponents().size());
	 * assertEquals(productPageDefinition.getPageDefinitionType(),
	 * prodCopy2.getPageDefinitionType());
	 * 
	 * PageComponent copyPageComponent2 =
	 * prodCopy2.getPageComponents().iterator().next();
	 * 
	 * assertEquals(pageComponent.getId(), copyPageComponent2.getId());
	 * 
	 * Component copyComponent2 = copyPageComponent2.getComponent();
	 * 
	 * assertEquals(component.getLabelKey(), copyComponent2.getLabelKey());
	 * assertEquals(component.getFields().size(),
	 * copyComponent2.getFields().size());
	 * 
	 * Field copyf1 = getField(copyComponent2.getFields(), field1); Field copyf2 =
	 * getField(copyComponent2.getFields(), field2);
	 * 
	 * assertTrue(compareQuestion(copyf1, field1));
	 * assertTrue(compareQuestion(copyf2, field2));
	 * 
	 * for(Field field : component.getFields()) { Field copyField =
	 * getField(copyComponent2.getFields(), field); assertNotNull(copyField);
	 * assertEquals(field.getId(), copyField.getId());
	 * 
	 * Element element = field.getElement(); Element copyElement =
	 * copyField.getElement();
	 * 
	 * assertNotNull(copyElement); assertEquals(element.getId(),
	 * copyElement.getId()); assertEquals(element.getTitleText(),
	 * copyElement.getTitleText());
	 * 
	 * assertTrue(element.getTags().size() > 0);
	 * assertEquals(element.getTags().size(), copyElement.getTags().size());
	 * 
	 * OptionsTag tag = (OptionsTag)element.getTag(); OptionsTag copyTag =
	 * (OptionsTag)copyElement.getTag();
	 * 
	 * assertEquals(tag.getTagType(), copyTag.getTagType());
	 * assertTrue(tag.getOptions().size() > 0);
	 * assertEquals(tag.getOptions().size(), copyTag.getOptions().size());
	 * 
	 * for(TagOption tagOption : tag.getOptions()) { TagOption copyTagOption =
	 * getTagOption(copyTag.getOptions(), tagOption); assertNotNull(copyTagOption);
	 * assertEquals(tagOption.getId(), copyTagOption.getId());
	 * assertEquals(tagOption.getLabel(), copyTagOption.getLabel());
	 * assertEquals(tagOption.getValue(), copyTagOption.getValue()); } }
	 * 
	 * copyComponent2.getFields().clear();
	 * 
	 * assertEquals(0, copyComponent2.getFields().size());
	 * assertTrue(copyComponent2.getFields().size() !=
	 * component.getFields().size());
	 * 
	 * }
	 * 
	 * private TagOption getTagOption(final Set<TagOption> tagOptions, final
	 * TagOption tagOption) { for(TagOption t : tagOptions) {
	 * if(tagOption.getId().equals(t.getId())) { return t; } } return null; }
	 * 
	 * private Field getField(final Set<Field> fields, final Field field) {
	 * for(Field f : fields) { if(field.getId().equals(f.getId())) { return f; } }
	 * return null; }
	 * 
	 * private boolean compareQuestion(final Field f1, final Field f2) {
	 * if(f1.getId().equals(f2.getId())) { Question question1 =
	 * f1.getElement().getQuestion(); Question question2 =
	 * f2.getElement().getQuestion();
	 * 
	 * boolean chk1 = question1.getElementText().equals(question2.getElementText());
	 * boolean chk2 = question1.getDescription().equals(question2.getDescription());
	 * 
	 * return chk1 && chk2; } return false; }
	 * 
	 * public static <T> T UnoptimizedDeepCopy(T orig) { T obj = null; try { //
	 * Write the object out to a byte array ByteArrayOutputStream bos = new
	 * ByteArrayOutputStream(); ObjectOutputStream out = new
	 * ObjectOutputStream(bos); out.writeObject(orig); out.flush(); out.close();
	 * 
	 * // Make an input stream from the byte array and read // a copy of the object
	 * back in. ObjectInputStream in = new ObjectInputStream(new
	 * ByteArrayInputStream(bos.toByteArray())); obj = (T)in.readObject(); }
	 * catch(IOException e) { e.printStackTrace(); } catch(ClassNotFoundException
	 * cnfe) { cnfe.printStackTrace(); } return obj; }
	 * 
	 * @Ignore
	 * 
	 * @Test public final void testShouldReturnTwoAvailableAccountPageDefinitions()
	 * { List<AccountPageDefinition> pageDefs =
	 * accountPageDefinitionDao.getAvailablePageDefinitions(adminUser1);
	 * assertThat(pageDefs.size(), equalTo(3));
	 * assertThat(pageDefs.get(0).getName(), equalTo("first account page"));
	 * assertThat(pageDefs.get(1).getName(), equalTo("second account page")); }
	 * 
	 * @Ignore
	 * 
	 * @Test public final void testShouldReturnOneAvailableAccountPageDefinitions()
	 * { List<AccountPageDefinition> pageDefs =
	 * accountPageDefinitionDao.getAvailablePageDefinitions(adminUser1);
	 * assertThat(pageDefs.size(), equalTo(3));
	 * assertThat(pageDefs.get(0).getName(), equalTo("first account page")); }
	 * 
	 * @Ignore
	 * 
	 * @Test public final void testShouldReturnTwoAvailableProductPageDefinitions()
	 * { List<ProductPageDefinition> pageDefs =
	 * productPageDefinitionDao.getAvailablePageDefinitions(adminUser1);
	 * assertThat(pageDefs.size(), equalTo(3));
	 * assertThat(pageDefs.get(0).getName(), equalTo("first product page"));
	 * assertThat(pageDefs.get(1).getName(), equalTo("second product page")); }
	 * 
	 * @Ignore
	 * 
	 * @Test public final void testShouldReturnOneAvailableProductPageDefinitions()
	 * { List<ProductPageDefinition> pageDefs =
	 * productPageDefinitionDao.getAvailablePageDefinitions(adminUser1);
	 * assertThat(pageDefs.size(), equalTo(3));
	 * assertThat(pageDefs.get(0).getName(), equalTo("first product page")); }
	 * 
	 */}
