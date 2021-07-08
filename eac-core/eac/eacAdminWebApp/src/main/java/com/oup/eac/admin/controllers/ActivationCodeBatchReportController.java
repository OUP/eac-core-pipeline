package com.oup.eac.admin.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Platform;
import com.oup.eac.dto.ActivationCodeBatchReportCriteria;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ActivationCodeBatchReportService;
import com.oup.eac.service.AsyncReportService;
import com.oup.eac.service.DivisionService;
import com.oup.eac.service.PlatformService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/reports/registrations/activationCodeBatchReport.htm")
public class ActivationCodeBatchReportController {
    
	private static final Logger LOG = Logger.getLogger(ActivationCodeBatchReportController.class);
	
    private static final String ATTR_MODEL = "activationCodeReportCriteria";
    
    private static final String ATTR_ACTIVATION_COD_REG_REPORT = "ACTIVATION_CODE_REG_REPORT";
    
    private static final String SEND_EVENT = "send";
    
    private static final String REPORT_EVENT = "report";
    
    
    @Autowired
    private DivisionService divisionService;
    
    @Autowired
	private PlatformService platformService;
    
    @Autowired
    private ActivationCodeBatchReportService activationCodeBatchReportService;
    
    @Autowired
    private AsyncReportService asyncReportService;
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showForm() {
        return showFormInternal();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "_eventId=send")  // _eventId maintaining interoperability with WebFlow
    public ModelAndView sendReport(@ModelAttribute(ATTR_MODEL) final ActivationCodeBatchReportCriteria reportCriteria, final HttpServletRequest request) 
    		throws ServiceLayerException, ErightsException {
    	LOG.info("ActivationCodeBatch report send: " + reportCriteria.toString());;
        AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ActivationCodeBatchReportCriteria lastReport = (ActivationCodeBatchReportCriteria)request.getSession().getAttribute(ATTR_ACTIVATION_COD_REG_REPORT);
        reportCriteria.setEvent(SEND_EVENT);
        reportCriteria.setAdminEmail(adminUser.getEmailAddress());
        asyncReportService.createReport(lastReport, adminUser.getEmailAddress());

        ModelAndView modelAndView = showFormInternal();
        modelAndView.addObject("successMsg", "status.report.sent");

        return modelAndView;
    }
    
    private ModelAndView showFormInternal() {
        AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ModelAndView modelAndView = new ModelAndView(getViewName());
        List<Platform> platformList = null;
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
        return modelAndView;
    }

	protected ActivationCodeBatchReportCriteria createModelInternal() {
		return new ActivationCodeBatchReportCriteria();
	}
	
	protected String getViewName() {
		return "activationCodeBatchReport";
	}

    @ModelAttribute(ATTR_MODEL)
    public ActivationCodeBatchReportCriteria createModel(final HttpServletRequest request) {
        return new ActivationCodeBatchReportCriteria();
    }
    
    @RequestMapping(method = RequestMethod.POST, params = "_eventId=report")  // _eventId maintaining interoperability with WebFlow
    public ModelAndView checkReport(@ModelAttribute(ATTR_MODEL) final ActivationCodeBatchReportCriteria reportCriteria, final HttpServletRequest request) 
    		throws ServiceLayerException, ErightsException {
    	LOG.info("ActivationCodeBatch report : " + reportCriteria.toString());;
        AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reportCriteria.setEvent(REPORT_EVENT);
        reportCriteria.setAdminEmail(adminUser.getEmailAddress());
        long reportSize = activationCodeBatchReportService.getActivationCodeReportSize(reportCriteria);
        
        request.getSession().setAttribute(ATTR_ACTIVATION_COD_REG_REPORT, reportCriteria);

        ModelAndView modelAndView = new ModelAndView("activationCodeBatchReportDetails");
        modelAndView.addObject("reportSize", reportSize);
        modelAndView.addObject("maxSize", reportCriteria.getMaxResults());
        modelAndView.addObject("emailAddress", adminUser.getEmailAddress());

        return modelAndView;
    }
}
