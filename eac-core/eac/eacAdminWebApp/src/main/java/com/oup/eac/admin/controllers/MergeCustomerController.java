package com.oup.eac.admin.controllers;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.beans.MergeCustomerBean;
import com.oup.eac.dto.CustomerSearchCriteria;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;


@Controller
@RequestMapping("/mvc/mergeCustomer")
public class MergeCustomerController {
	
	private static final String MERGE_CUSTOMER_VIEW = "mergeCustomers";
	private static final String MERGE_FORM_CUSTOMER_VIEW = "formMerge";
	private static final String MERGE_CUSTOMERS_SUCCESS = "status.merge.success";
	private static final String MERGE_CUSTOMERS_ERROR = "error.merge";
	
	private static final String SUCCESS_REDIRECT_URL = "redirect:/mvc/mergeCustomer/formMerge.htm?statusMessageKey=";
	private static final String ERROR_REDIRECT_URL = "redirect:/mvc/mergeCustomer/formMerge.htm?errorMessageKey=";
	//private static final String SEARCH_CUSTOMERS_FOR_MERGE="searchResultsMergeCustomer";
	private final CustomerService customerService;
	
	   @Autowired
	    public MergeCustomerController(final CustomerService customerService) {
	        this.customerService = customerService;
	    }
	
	
	 @RequestMapping(value = { "/formMerge.htm" },method = RequestMethod.GET)
	 public ModelAndView showForm() {
		 return showFormInternal();
	 }	
	
	 private ModelAndView showFormInternal() {
	        ModelAndView modelAndView = new ModelAndView(MERGE_CUSTOMER_VIEW);
	        modelAndView.addObject("mergeCustomerForm", new MergeCustomerBean());
	        return modelAndView;
	    }
	 
	 @RequestMapping(value = { "/searchCustomers.htm" }, method = RequestMethod.POST)
	 private ModelAndView searchCustomers(@RequestParam("emailId") String email) {
		 	String emailId=email;
		 	CustomerSearchCriteria customerSearchCriteria=new CustomerSearchCriteria();
		 	List<Customer> customers=null;
		 	
		 	try {
		 		 validateEmail(emailId);
		 		 customerSearchCriteria.setEmail(emailId);
				 customers=customerService.searchCustomersForMerging(customerSearchCriteria);
			} catch (ServiceLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ModelAndView(ERROR_REDIRECT_URL + e.getMessage());
			}
		 	
	        ModelAndView modelAndView = new ModelAndView("mergeCustomersTile");
	        
	        MergeCustomerBean mergeCustomerBean=new MergeCustomerBean();
	        modelAndView.addObject("mergeCustomerForm", mergeCustomerBean);
	        modelAndView.addObject("customers", customers);
	        modelAndView.addObject("count", customers.size());
	        
	        return modelAndView;
	    }
	 
	 @RequestMapping(value = { "/merge.htm" }, method = RequestMethod.POST)
	 private ModelAndView mergeCustomers(@RequestParam("emailId") String email,@RequestParam("customerId") String id) {
		 	
		 	ModelAndView modelAndView=null;
		 	MergeCustomerBean mergeCustomerBean=new MergeCustomerBean();
		 	
		 	try {
		 		String emailId=email;
				 customerService.mergeCustomer(id, emailId);
				 
				 modelAndView=new ModelAndView(SUCCESS_REDIRECT_URL + MERGE_CUSTOMERS_SUCCESS);
				 modelAndView.addObject("mergeCustomerForm", mergeCustomerBean);
			} catch (ServiceLayerException e) {
				// TODO Auto-generated catch block
				modelAndView=new ModelAndView(ERROR_REDIRECT_URL + MERGE_CUSTOMERS_ERROR);
				modelAndView.addObject("mergeCustomerForm", mergeCustomerBean);
				e.printStackTrace();
			}
		 	
	        return modelAndView;
	    }
	 
	 private void validateEmail(final String email) throws ServiceLayerException {
			
			if (StringUtils.isBlank(email) || !InternationalEmailAddress.isValid(email)) {
				throw new ServiceLayerException("error.invalidEmail");
			}
		}

}
