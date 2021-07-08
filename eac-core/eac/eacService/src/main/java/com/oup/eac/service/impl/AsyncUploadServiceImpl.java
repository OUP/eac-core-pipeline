package com.oup.eac.service.impl;

import java.io.File;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.service.AsyncUploadService;
import com.oup.eac.service.ImportService;

@Service("asyncUploadService")
public class AsyncUploadServiceImpl implements AsyncUploadService {
	
	@Autowired
    private ImportService importService;

	@Override
	public void createProductBatch(final File file, final AdminUser adminUser) {
		
		final Authentication a = SecurityContextHolder.getContext().getAuthentication();
		
		 Executor executor = new Executor() {
	            @Override
	            public void execute(Runnable runnable) {
	                new Thread(runnable).start();
	            }
	        };
	        executor.execute(new Runnable() {
	            @Override
	            public void run() {
	            	try {
	            		SecurityContext ctx = SecurityContextHolder.createEmptyContext();
		                ctx.setAuthentication(a);
		                SecurityContextHolder.setContext(ctx);
		            	importService.createProducts(file, adminUser);
	            	} finally{
	            		SecurityContextHolder.clearContext();
	            	}
	            	
	            }
	        });
	}
}
