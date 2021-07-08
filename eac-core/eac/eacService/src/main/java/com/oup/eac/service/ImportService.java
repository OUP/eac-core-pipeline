package com.oup.eac.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.security.core.context.SecurityContext;

import com.oup.eac.domain.AdminUser;

public interface ImportService {
	
	void createProducts(File file, AdminUser adminUser);
	
	String bulkCreateActivationCodeBatch(File file, AdminUser adminUser);
}


