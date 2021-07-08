package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.date.utils.DateUtils;
import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.common.utils.lang.LocaleUtils;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Platform;
import com.oup.eac.dto.ReportCriteria;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.AsyncReportService;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.PlatformService;
import com.oup.eac.service.ReportService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/reports/registrations")
public class RegistrationsReportController {
    
	private static final Logger LOG = Logger.getLogger(RegistrationsReportController.class);
    private static final String ATTR_MODEL = "reportCriteria";
    
    private static final String ATTR_REG_REPORT = "REG_REPORT";
    
    @Autowired
    private DivisionService divisionService;
    
    @Autowired
	private PlatformService platformService;
    
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private AsyncReportService asyncReportService;
    
    @Autowired
    private ExternalIdService externalIdService;

    @RequestMapping(value = { "/report.htm"}, method = RequestMethod.GET)
    public ModelAndView showForm() {
        return showFormInternal();
    }
    
    @RequestMapping(value = { "/report.htm" }, method = RequestMethod.POST, params = "_eventId=send")  // _eventId maintaining interoperability with WebFlow
    public ModelAndView sendReport(@ModelAttribute(ATTR_MODEL) final ReportCriteria reportCriteria, final HttpServletRequest request) throws ServiceLayerException {
    	LOG.info("registration report send : " + reportCriteria.toString());;
    	ModelAndView modelAndView = showFormInternal();
    	LOG.info("reportCriteria :: " + reportCriteria.toString());
    	try {
	        AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        ReportCriteria lastReport = (ReportCriteria)request.getSession().getAttribute(ATTR_REG_REPORT);
	        
	        if(lastReport.getLastLoginToDate() != null){
	            lastReport.setLastLoginToDate(addDaytoEndDate(lastReport.getLastLoginToDate()));    // make end date as end of the day, so add 23 hrs, 59 min and 59 secs.
	        }
	        
	        if(lastReport.getCustomerCreatedToDate() != null){
	            lastReport.setCustomerCreatedToDate(addDaytoEndDate(lastReport.getCustomerCreatedToDate()));    // make end date as end of the day, so add 23 hrs, 59 min and 59 secs.
	        }
	        
	        if(lastReport.getRegistrationCreatedToDate() != null){
	            lastReport.setRegistrationCreatedToDate(addDaytoEndDate(lastReport.getRegistrationCreatedToDate()));    // make end date as end of the day, so add 23 hrs, 59 min and 59 secs.
	        }
	        
	        if(lastReport.getToDate() != null){
	            lastReport.setToDate(addDaytoEndDate(lastReport.getToDate()));    // make end date as end of the day, so add 23 hrs, 59 min and 59 secs.
	        }
	       
	        lastReport.setStartIndex(reportCriteria.getStartIndex());
	        lastReport.setMaxResults(reportCriteria.getMaxResults());
	        lastReport.setPlatformCode(reportCriteria.getPlatformCode());
	        
	        asyncReportService.createReport(lastReport, adminUser.getEmailAddress());
	
	        modelAndView.addObject("successMsg", "status.report.sent");
        } catch(Exception e){
        	LOG.error(ExceptionUtil.getStackTrace(e));
        }
        return modelAndView;
    }
   
    private ModelAndView showFormInternal() {
        AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Platform> platformList = null;
        ModelAndView modelAndView = new ModelAndView(getViewName());
        try {
			modelAndView.addObject("availableDivisions", divisionService.getDivisionsByAdminUser(adminUser));
		} catch (DivisionNotFoundException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		} catch (AccessDeniedException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		} catch (ErightsException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		}
        try {
        	platformList = platformService.getAllPlatforms() ;
        	modelAndView.addObject("availablePlatforms", platformList);
        } catch (ErightsException e) {
        	LOG.error(ExceptionUtil.getStackTrace(e));
        }
        modelAndView.addObject("locales",LocaleUtils.getOrderedLocales(adminUser.getLocale()));
        modelAndView.addObject("timezones",DateUtils.getOrderedTimeZoneIds());
        modelAndView.addObject("emailVerificationStates", adminUser.getEmailVerificationState().values());
        try {
			modelAndView.addObject("externalSystems",externalIdService.getAllExternalSystemsOrderedByName());
		} catch (ErightsException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		}
        return modelAndView;
    }

	protected ReportCriteria createModelInternal() {
		return new ReportCriteria();
	}
	
	protected String getViewName() {
		return "registrationsReport";
	}

    @ModelAttribute(ATTR_MODEL)
    public ReportCriteria createModel(final HttpServletRequest request) {
        return new ReportCriteria();
    }
    
    @RequestMapping(value = { "/report.htm" }, method = RequestMethod.POST, params = "_eventId=report")  // _eventId maintaining interoperability with WebFlow
    public ModelAndView checkReport(@ModelAttribute(ATTR_MODEL) final ReportCriteria reportCriteria, final HttpServletRequest request) throws ServiceLayerException {
    	LOG.info("registration report : " + reportCriteria.toString());;
    	AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ModelAndView modelAndView = new ModelAndView("reportDetails");
        LOG.info("reportCriteria :: " + reportCriteria.toString());
        try {
	        if(reportCriteria.getLastLoginToDate() != null){
	            reportCriteria.setLastLoginToDate(addDaytoEndDate(reportCriteria.getLastLoginToDate()));    // make end date as end of the day, so add 23 hrs, 59 min and 59 secs.
	        }
	        
	        if(reportCriteria.getCustomerCreatedToDate() != null){
	            reportCriteria.setCustomerCreatedToDate(addDaytoEndDate(reportCriteria.getCustomerCreatedToDate()));    // make end date as end of the day, so add 23 hrs, 59 min and 59 secs.
	        }
	        
	        if(reportCriteria.getRegistrationCreatedToDate() != null){
	            reportCriteria.setRegistrationCreatedToDate(addDaytoEndDate(reportCriteria.getRegistrationCreatedToDate()));    // make end date as end of the day, so add 23 hrs, 59 min and 59 secs.
	        }
	        
	        if(reportCriteria.getToDate() != null){
	            reportCriteria.setToDate(addDaytoEndDate(reportCriteria.getToDate()));    // make end date as end of the day, so add 23 hrs, 59 min and 59 secs.
	        }
	        
	        
	        long reportSize = reportService.getReportSize(reportCriteria);
	        
	        request.getSession().setAttribute(ATTR_REG_REPORT, reportCriteria);
	
	        
	        modelAndView.addObject("reportSize", reportSize);
	        modelAndView.addObject("maxSize", reportCriteria.getMaxResults());
	        modelAndView.addObject("emailAddress", adminUser.getEmailAddress());
        } catch (Exception e){
        	LOG.error(ExceptionUtil.getStackTrace(e));
        	throw new ServiceLayerException();
        }
        return modelAndView;
    }
    
    private DateTime addDaytoEndDate(DateTime toDate){
        
        // Add 23 hrs 59 mins 59 secs to EndDate, so that it becomes end of the day.
        
        return toDate.plusHours(23).plusMinutes(59).plusSeconds(59);
        
    }
    
    @RequestMapping(value = { "/changeExternalSystem.htm" }, method = RequestMethod.GET, params = "_eventId=changeExternalSystem")  // _eventId maintaining interoperability with WebFlow
    public @ResponseBody List<String> changeExternalSystem(@RequestParam("externalSystem") final String externalSystemIdName) throws ServiceLayerException, ErightsException {
    	List<String> extTypeNameList = new ArrayList<String>();
    	ExternalSystem externalSystemId = new ExternalSystem();
    	externalSystemId.setName(externalSystemIdName);
		List<ExternalSystemIdType> extIdTypes =  externalIdService.getExternalSystemIdTypesOrderedByName(externalSystemId);

    	for (ExternalSystemIdType externalSystemIdType : extIdTypes) {
    		extTypeNameList.add(externalSystemIdType.getName());
		}
        return extTypeNameList;
    } 
}
