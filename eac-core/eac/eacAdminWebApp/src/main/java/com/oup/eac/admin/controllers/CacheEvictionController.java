package com.oup.eac.admin.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.CacheEvictionBean;
import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.dto.Message;
import com.oup.eac.service.CacheEvictionService;
import com.oup.eac.service.ServiceLayerException;
@Controller
@RequestMapping("/mvc/support/cacheEviction.htm")
public class CacheEvictionController {
	
	private static final Logger LOGGER = Logger.getLogger(CacheEvictionController.class);
	private static final String GENERATE_SUCCESS = "Successfully evicted {} entries.";
	private static final String GENERATE_ERROR = "Error evicting Cache";
	private static final String INVALID_PATTERN = "Invalid pattern, To flushall use '*'";
	private static final String STATUS_MSG_KEY = "statusMessageKey";
	
	private static final String CACHE_EVICTION_VIEW = "cacheEviction";
	
	private final CacheEvictionService cacheEvictionService;
	
	@Autowired
	public CacheEvictionController(
			final CacheEvictionService cacheEvictionService) {
		this.cacheEvictionService = cacheEvictionService;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView(CACHE_EVICTION_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView evictCache(@Valid @ModelAttribute("cacheEvictionBean") final CacheEvictionBean cacheEvictionBean, final BindingResult bindingResult) throws ServiceLayerException {

		ModelAndView modelAndView = new ModelAndView(CACHE_EVICTION_VIEW);
		
		try {
			if (StringUtils.isBlank(cacheEvictionBean.getPattern())) {
				LOGGER.info("Pattern received : " + cacheEvictionBean.getPattern());
				Object[] errorMessagesArray = new String[1];
				errorMessagesArray[0] = INVALID_PATTERN ;
				bindingResult.rejectValue(null, "", errorMessagesArray, INVALID_PATTERN);
				return modelAndView ;
			}
			if (!bindingResult.hasErrors()) {
				Map<String, String> request = new HashMap<String, String>();
				request.put("regex", cacheEvictionBean.getPattern());
				Map<String, String> response = cacheEvictionService.evictCache(request);
				if (response.get("status").equalsIgnoreCase("SUCCESS")) {
					String count = "0";
					if(response.get("count") != null){
						count = (response.get("count"));
					} 
					String finalMessage = GENERATE_SUCCESS.replace("{}", count);
					modelAndView.addObject(STATUS_MSG_KEY, finalMessage );
				} else {
					modelAndView.addObject(STATUS_MSG_KEY, GENERATE_ERROR );
				}
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtil.getStackTrace(e));
			throw new ServiceLayerException("CacheEviction error.", e, new Message(
                    "", "There was a problem in eviction ")) ;
		}
		return modelAndView;

	}
	
	
	
	@ModelAttribute("cacheEvictionBean")
	public CacheEvictionBean createModel() {
		
		CacheEvictionBean cacheEvictionBean = new CacheEvictionBean();
		
		return cacheEvictionBean;
	}
	
	
}
