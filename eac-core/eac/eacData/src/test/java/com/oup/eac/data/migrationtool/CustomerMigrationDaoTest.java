package com.oup.eac.data.migrationtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.migrationtool.CustomerMigration;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;
import com.oup.eac.domain.migrationtool.CustomerMigrationState;

/**
 * 
 * @author Chirag Joshi
 *
 */
public class CustomerMigrationDaoTest /*extends BaseEacMigrationDaoTest*/ {
	/*
	 * 
	 * private CustomerMigration cmInitial1; private CustomerMigration cmInitial2;
	 * private CustomerMigration cmInitial3; private CustomerMigration
	 * cmCreateCustomer4; private CustomerMigration cmCreateCustomer5; private
	 * CustomerMigration cmCreateCustomer6; private CustomerMigration
	 * cmCreationError7; private CustomerMigration cmCreationError8; private
	 * CustomerMigration cmCreationError9; private CustomerMigration
	 * cmCustomerCreated10; private CustomerMigration cmCustomerCreated11; private
	 * CustomerMigration cmCustomerCreated12;
	 * 
	 * 
	 * @Before public final void setUp() throws Exception { this.cmInitial1 =
	 * createCM(1, CustomerMigrationState.INITIAL); this.cmInitial2 = createCM(2,
	 * CustomerMigrationState.INITIAL); this.cmInitial3 = createCM(3,
	 * CustomerMigrationState.INITIAL);
	 * 
	 * this.cmCreateCustomer4 = createCM(4,
	 * CustomerMigrationState.CREATING_CUSTOMER); this.cmCreateCustomer5 =
	 * createCM(5, CustomerMigrationState.CREATING_CUSTOMER); this.cmCreateCustomer6
	 * = createCM(6, CustomerMigrationState.CREATING_CUSTOMER);
	 * 
	 * this.cmCreationError7 = createCM(7,
	 * CustomerMigrationState.ERROR_CREATING_CUSTOMER); this.cmCreationError8 =
	 * createCM(8, CustomerMigrationState.ERROR_CREATING_CUSTOMER);
	 * this.cmCreationError9 = createCM(9,
	 * CustomerMigrationState.ERROR_CREATING_CUSTOMER);
	 * 
	 * this.cmCustomerCreated10 = createCM(10,
	 * CustomerMigrationState.CUSTOMER_CREATED); this.cmCustomerCreated11 =
	 * createCM(11, CustomerMigrationState.CUSTOMER_CREATED);
	 * this.cmCustomerCreated12 = createCM(12,
	 * CustomerMigrationState.CUSTOMER_CREATED);
	 * 
	 * loadAllDataSets();
	 * 
	 * }
	 * 
	 * @Test public void testGetById() { CustomerMigration cm =
	 * this.customerMigrationDao.getById(cmInitial1.getId(), false);
	 * checkSame(cmInitial1, cm); }
	 * 
	 * @Test public void testFetchByStateInitial() {
	 * checkFetchByState(CustomerMigrationState.INITIAL, Arrays.asList(cmInitial1,
	 * cmInitial2, cmInitial3)); System.out.println("oops"); }
	 * 
	 * @Test public void testFetchByStateCreateCustomer() {
	 * checkFetchByState(CustomerMigrationState.CREATING_CUSTOMER,
	 * Arrays.asList(cmCreateCustomer4, cmCreateCustomer5, cmCreateCustomer6)); }
	 * 
	 * @Test public void testFetchByStateCreationError() {
	 * checkFetchByState(CustomerMigrationState.ERROR_CREATING_CUSTOMER,
	 * Arrays.asList(cmCreationError7, cmCreationError8, cmCreationError9)); }
	 * 
	 * @Test public void testFetchByStateCustomerCreated() {
	 * checkFetchByState(CustomerMigrationState.CUSTOMER_CREATED,
	 * Arrays.asList(cmCustomerCreated10, cmCustomerCreated11,
	 * cmCustomerCreated12)); }
	 * 
	 * private void checkFetchByState(CustomerMigrationState state,
	 * List<CustomerMigration> data) { List<String> possibleIds = new
	 * ArrayList<String>(); for (CustomerMigration cm : data) {
	 * possibleIds.add(cm.getId()); } for (int i = 0; i < 5; i++) {
	 * List<CustomerMigration> actuals = this.customerMigrationDao.getInState(state,
	 * i); Assert.assertEquals(Math.min(i, possibleIds.size()), actuals.size()); for
	 * (CustomerMigration actual : actuals) { String actualId = actual.getId();
	 * Assert.assertTrue(possibleIds.contains(actualId)); } } }
	 * 
	 * @Test public void testGetCount() { Assert.assertEquals(12,
	 * this.customerMigrationDao.getCount()); Assert.assertEquals(3,
	 * this.customerMigrationDao.getCount(CustomerMigrationState.INITIAL));
	 * Assert.assertEquals(3,
	 * this.customerMigrationDao.getCount(CustomerMigrationState.CREATING_CUSTOMER))
	 * ; Assert.assertEquals(3,
	 * this.customerMigrationDao.getCount(CustomerMigrationState.
	 * ERROR_CREATING_CUSTOMER)); Assert.assertEquals(3,
	 * this.customerMigrationDao.getCount(CustomerMigrationState.CUSTOMER_CREATED));
	 * }
	 * 
	 * @Test public void testGetIdsInState() {
	 * checkEquals(CustomerMigrationState.INITIAL, this.cmInitial1.getId(),
	 * this.cmInitial2.getId(), this.cmInitial3.getId());
	 * checkEquals(CustomerMigrationState.CREATING_CUSTOMER,
	 * this.cmCreateCustomer4.getId(), this.cmCreateCustomer5.getId(),
	 * this.cmCreateCustomer6.getId());
	 * checkEquals(CustomerMigrationState.ERROR_CREATING_CUSTOMER,
	 * this.cmCreationError7.getId(), this.cmCreationError8.getId(),
	 * this.cmCreationError9.getId());
	 * checkEquals(CustomerMigrationState.CUSTOMER_CREATED,
	 * this.cmCustomerCreated10.getId(), this.cmCustomerCreated11.getId(),
	 * this.cmCustomerCreated12.getId()); }
	 * 
	 * @Test public void testCustomerMigrationToProcess() {
	 * checkGetCustomersMigrationToProcess(this.cmInitial1);
	 * checkGetCustomersMigrationToProcess(this.cmInitial2);
	 * checkGetCustomersMigrationToProcess(this.cmInitial3);
	 * checkGetCustomersMigrationToProcess(this.cmCreateCustomer4);
	 * checkGetCustomersMigrationToProcess(this.cmCreateCustomer5);
	 * checkGetCustomersMigrationToProcess(this.cmCreateCustomer6);
	 * checkGetCustomersMigrationToProcess(this.cmCreationError7);
	 * checkGetCustomersMigrationToProcess(this.cmCreationError8);
	 * checkGetCustomersMigrationToProcess(this.cmCreationError9);
	 * checkGetCustomersMigrationToProcess(this.cmCustomerCreated10);
	 * checkGetCustomersMigrationToProcess(this.cmCustomerCreated11);
	 * checkGetCustomersMigrationToProcess(this.cmCustomerCreated12); }
	 * 
	 * private void checkGetCustomersMigrationToProcess(CustomerMigration migration)
	 * { CustomerMigration res1 =
	 * customerMigrationDao.getCustomersMigrationToProcess(migration.getId());
	 * checkSame(migration, res1); }
	 * 
	 * private void checkEquals(CustomerMigrationState state, String... ids) {
	 * List<String> expStates = Arrays.asList(ids); Set<String> expected = new
	 * HashSet<String>(); expected.addAll(expStates);
	 * 
	 * Set<String> actual = new HashSet<String>();
	 * actual.addAll(this.customerMigrationDao.getIdsInState(state, 1000));
	 * 
	 * Assert.assertEquals(expected, actual); }
	 * 
	 * 
	 * @Test public void testIsNotComplete() { long count =
	 * this.customerMigrationDao.getNotFinishedCount(); Assert.assertEquals(6,
	 * count); }
	 * 
	 * @Test public void testImmutable() { List<CustomerMigration> results =
	 * this.customerMigrationDao.getInState(CustomerMigrationState.INITIAL, 100);
	 * Assert.assertEquals(3, results.size()); CustomerMigration cm =
	 * results.get(0); CustomerMigrationData data = cm.getData();
	 * Assert.assertNotNull(data);
	 * 
	 * System.out.println("CMD ID " + data.getId()); System.out.flush();
	 * 
	 * cm.setState(CustomerMigrationState.ERROR_CREATING_CUSTOMER);
	 * 
	 * String newLastName = UUID.randomUUID().toString();
	 * data.setLastName(newLastName);
	 * 
	 * sessionFactory.getCurrentSession().flush();//send update to database
	 * 
	 * // Check that we can update CUSTOMER_MIGRATION but not
	 * CUSTOMER_MIGRATION_DATA
	 * 
	 * String sql1 =
	 * "select count(*) from customer_migration      where id ='"+data.getId()
	 * +"' and migration_state='ERROR_CREATING_CUSTOMER'"; String sql2 =
	 * "select count(*) from customer_migration_data where id ='"+data.getId()+"'";
	 * String sql3 =
	 * "select count(*) from customer_migration_data where id ='"+data.getId()
	 * +"' and last_name='"+newLastName+"'"; int res1 =
	 * simpleJdbcTemplate.queryForInt(sql1); int res2 =
	 * simpleJdbcTemplate.queryForInt(sql2); int res3 =
	 * simpleJdbcTemplate.queryForInt(sql3); Assert.assertEquals(1, res1);
	 * Assert.assertEquals(1, res2); Assert.assertEquals(0, res3);
	 * 
	 * }
	 * 
	 * 
	 */}
