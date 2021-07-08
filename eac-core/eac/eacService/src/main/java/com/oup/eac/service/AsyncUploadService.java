package com.oup.eac.service;

import java.io.File;

import org.springframework.security.core.context.SecurityContext;

import com.oup.eac.domain.AdminUser;

public interface AsyncUploadService {

	void createProductBatch(File file,  AdminUser adminUser);
}
