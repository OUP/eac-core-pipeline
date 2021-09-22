package com.oup.eac.data.migrationtool;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.data.AbstractDbMessageTest;
import com.oup.eac.domain.migrationtool.CustomerMigrationData;

/**
 * 
 * @author Chirag Joshi
 * 
 */
public class CustomerMigrationDataDaoTest/* extends AbstractDbMessageTest */ {
	/*
	 * 
	 * @Autowired private CustomerMigrationDataDao cmdDao;
	 * 
	 * @Autowired private SessionFactory sessionFactory;
	 * 
	 * private CustomerMigrationData cmd;
	 * 
	 * 
	 * 
	 * @Before public void setup() { this.cmd = new CustomerMigrationData();
	 * this.cmd.setEmailAddress("chirag.joshi@oup.com");
	 * this.cmd.setUsername("chiragjoshi"); this.cmd.setPassword("password");
	 * this.cmd.setFirstName("firstname"); this.cmd.setLastName("lastname");
	 * this.cmd.setResetPassword(true); this.cmd.setFailedAttempts(0);
	 * this.cmd.setLocked(false); this.cmd.setCustomerType("SELF_SERVICE");
	 * this.cmd.setEnabled(true); this.cmd.setLocale(new Locale("en/gb"));
	 * this.cmd.setTimeZone("Europe/London"); // set time zone to Europe/London
	 * this.cmd.setEmailVerificationState("UNKNOWN");
	 * this.cmd.setExternalId(UUID.randomUUID().toString());
	 * this.cmd.setCreatedDate(new DateTime()); this.cmd.setUpdatedDate(new
	 * DateTime());
	 * 
	 * }
	 * 
	 * private void flush() { sessionFactory.getCurrentSession().flush(); }
	 * 
	 * @Test public void testSaveCustomerMigrationData() { int before =
	 * countRowsInTable("CUSTOMER_MIGRATION_DATA"); cmdDao.save(cmd); flush(); int
	 * after = countRowsInTable("CUSTOMER_MIGRATION_DATA");
	 * Assert.assertEquals(before + 1, after); }
	 * 
	 * @Test public void testGetCount() { cmdDao.save(cmd); flush();
	 * Assert.assertEquals(1, this.cmdDao.getCount()); }
	 * 
	 * @Test public void testfindByUserId() { cmdDao.save(cmd); flush();
	 * List<CustomerMigrationData> res1 =
	 * this.cmdDao.findByUserId(this.cmd.getUsername());
	 * Assert.assertEquals(this.cmd.getUsername(),res1.get(0).getUsername());
	 * 
	 * }
	 * 
	 * 
	 * 
	 */}
