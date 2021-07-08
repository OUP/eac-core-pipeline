package com.oup.eac.domain.utils.audit;

import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.oup.eac.common.utils.security.SecurityContextUtils;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
public class AuditLoggerTest {

	private AdminUser admin1;
	private AdminUser admin2;
	private Customer user1;

	private TestingAppender appender;

	@Before
	public void setup() {
		EacApp.setType(EacAppType.UNDEF);
		admin1 = new AdminUser();
		admin1.setUsername("AdminOne");
		admin2 = new AdminUser();
		admin2.setUsername("AdminTwo");
		user1 = new Customer();
		user1.setUsername("user1");
		appender = new TestingAppender();
		AuditLogger.LOG.addAppender(appender);
		SecurityContextUtils.LOG.addAppender(appender);
	}

	private void setupAdmin(final Object adminUser) {
		PowerMock.mockStatic(SecurityContextHolder.class);
		final SecurityContext sc = new SecurityContext() {

			@Override
			public void setAuthentication(Authentication authentication) {
				// TODO Auto-generated method stub

			}

			@Override
			public Authentication getAuthentication() {
				return new Authentication() {

					@Override
					public String getName() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean isAuthenticated() {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public Object getPrincipal() {
						Object result = adminUser;
						return result;
					}

					@Override
					public Object getDetails() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Object getCredentials() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Collection<? extends GrantedAuthority> getAuthorities() {
						// TODO Auto-generated method stub
						return null;
					}
				};
			}
		};
		EasyMock.expect(SecurityContextHolder.getContext()).andReturn(sc);

	}

	@Test
	public void testAdminEmptyMessagesA() {
		checkNonSystem(admin1, null, null, "UNDEF\tAdmin[adminone]");
	}

	@Test
	public void testAdminEmptyMessagesB() {
		checkNonSystem((AdminUser) null, (Customer) null, (String[]) null, "UNDEF");
	}

	@Test
	public void testAdminEmptyMessagesC() {
		EacApp.setType(EacAppType.ADMIN);
		checkNonSystem((AdminUser) null, (Customer) user1, (String[]) null, "ADMIN\tUser[user1]");
	}

	@Test
	public void testAdminEmptyMessagesD() {
		EacApp.setType(EacAppType.ADMIN);
		checkNonSystem((AdminUser) admin1, (Customer) user1, (String[]) null, "ADMIN\tAdmin[adminone]\tUser[user1]");
	}

	@Test
	public void testAdminMessagesA() {
		
		checkNonSystem((AdminUser) admin1, (Customer) user1, new String[] { "MSG1" },
				"UNDEF\tAdmin[adminone]\tUser[user1]\tMSG1");
	}

	@Test
	public void testAdminMessagesB() {
		checkNonSystem((AdminUser) admin1, (Customer) user1, new String[] { "MSG1", "MSG2" },
				"UNDEF\tAdmin[adminone]\tUser[user1]\tMSG1\tMSG2");
	}

	@Test
	public void testBadAdminMessagesA() {
		checkNonSystem("BAD_ADMIN", (Customer) user1, new String[] { "MSG1", "MSG2" },
				"UNDEF\tUser[user1]\tMSG1\tMSG2");
	}

	@Test
	public void testSystemEventNoCustomer() {
		checkSystem(null, new String[] { "MSG1", "MSG2", "MSG3" }, "UNDEF\tSYSTEM_EVENT\tMSG1\tMSG2\tMSG3");
	}

	@Test
	public void testSystemEventWithCustomer() {
		checkSystem(user1, new String[] { "MSG1", "MSG2", "MSG3" }, "UNDEF\tSYSTEM_EVENT\tUser[user1]\tMSG1\tMSG2\tMSG3");
	}

	@Test
	public void testSystemEventNoMessages() {
		checkSystem(user1, (String[])null, "UNDEF\tSYSTEM_EVENT\tUser[user1]");
	}

	
	@Test
	public void testWebEvent(){
		EacApp.setType(EacAppType.WEB);
		checkNonSystem(null, null, new String[] {"ONE","TWO","THREE"}, "WEB\tONE\tTWO\tTHREE");
		
	}
	
	@Test
	public void testAdminEventA(){
		EacApp.setType(EacAppType.ADMIN);
		checkNonSystem(null, null, new String[] {"ONE","TWO","THREE"}, "ADMIN\tONE\tTWO\tTHREE");
	}
	
	@Test
	public void testAdminEventB(){
		EacApp.setType(EacAppType.ADMIN);
		checkNonSystem(admin1, null, new String[] {"ONE","TWO","THREE"}, "ADMIN\tAdmin[adminone]\tONE\tTWO\tTHREE");
	}
	
	private void checkNonSystem(Object adminUser, Customer user, String[] msgs, String... expected) {		
		setupAdmin(adminUser);
		PowerMock.replay(SecurityContextHolder.class);
		if(user == null){
			AuditLogger.logEvent(msgs);
		}else{
			AuditLogger.logEvent(user, msgs);	
		}
		PowerMock.verify(SecurityContextHolder.class);		
		if(adminUser != null && (adminUser instanceof AdminUser == false )){
			String[] expected2 = new String[expected.length+1];
			expected2[0] = "logged in user should be of type AdminUser but is : " + adminUser.getClass().getName();
			System.arraycopy(expected, 0, expected2, 1, expected.length);
			checkLogEvent(expected2);
		}else{
			checkLogEvent(expected);
		}
	}

	private void checkSystem(Customer user, String[] msgs, String expected) {
		if(user == null){
			AuditLogger.logSystemEvent(msgs);
		}else{
			AuditLogger.logSystemEvent(user, msgs);
		}
		PowerMock.verify(SecurityContextHolder.class);
		checkLogEvent(expected);
	}



	private void checkLogEvent(String... expectedValues) {		
		for (int i = 0; i < expectedValues.length; i++) {
			Assert.assertEquals(expectedValues[i], this.appender.getMessages().get(i).getMessage());
		}
	}
	


}
