package com.oup.eac.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.data.AbstractDBTest;
import com.oup.eac.service.AdminService;

public class AdminServiceImplIntegrationTest extends AbstractDBTest {
	
	@Autowired
	private AdminService adminService;
	
	@Test
	public void shouldCreateEmailContentForPasswordReset() throws Exception {
		String text = adminService.prepareResetPasswordEmail("admin");
		String startText = IOUtils.toString(getInputStream("/com/oup/eac/service/impl/adminPasswordResetEmailStart.txt"), "UTF-8");
		String endText = IOUtils.toString(getInputStream("/com/oup/eac/service/impl/adminPasswordResetEmailEnd.txt"), "UTF-8");
		assertThat(text.startsWith(startText), equalTo(true));
		assertThat(text.endsWith(endText), equalTo(true));
	}
}
