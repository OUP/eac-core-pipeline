package com.oup.eac.admin.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oup.eac.common.RuntimeContext;
import com.oup.eac.common.utils.spring.ApplicationContextSupport;
import com.oup.eac.migrationtool.loader.CsvLoader;
import com.oup.eac.migrationtool.loader.XmlLoader;


@Controller
@RequestMapping("/loadData.htm") 
public class MigrationToolController {

    private static final Logger LOG = Logger.getLogger(MigrationToolController.class);
    
    @Autowired
    XmlLoader xmlLoader;
    
    @Autowired
    CsvLoader csvLoader;
    
    private String fileType; 
	
    private static RuntimeContext runtimeContext;
	static{
	
	}
	
	public MigrationToolController() {	     
		 runtimeContext = (RuntimeContext) ApplicationContextSupport.getBean("runtimeContext");		 
	}

	private RuntimeContext getRuntimeContext() {
	        return runtimeContext;
	    }

	@RequestMapping(method = RequestMethod.GET)
	public void startMigrationTool(){
	    
	    fileType=runtimeContext.getProperty("migrationtool.importfile.type");	
	    if (fileType.equalsIgnoreCase("xml")){
	        LOG.info("Loading XML file...");
	        xmlLoader.loadXml(runtimeContext); 
	        
	        
	    } else if (fileType.equalsIgnoreCase("csv")){
	        LOG.info("Loading CSV file...");
	        csvLoader.loadCsv(runtimeContext);
	        
	        
	    } else {
	        LOG.error("File type not matching... ");
	    }
	    
	}
}
