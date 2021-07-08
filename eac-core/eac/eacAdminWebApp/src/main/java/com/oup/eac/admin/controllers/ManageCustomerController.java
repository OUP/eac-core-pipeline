package com.oup.eac.admin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping(value = { "/mvc/customer" })
public class ManageCustomerController {

    private static final String CUSTOMER_DELETE_SUCCESS = "status.customer.delete.success";
    private static final String CUSTOMER_DELETE_FAILURE = "status.customer.delete.failure";

    private final CustomerService customerService;

    @Autowired
    public ManageCustomerController(final CustomerService customerService) {        
        this.customerService = customerService;
    }


    @RequestMapping(value = { "/delete.htm" }, method = RequestMethod.GET)
    public ModelAndView deleteCustomer(@RequestParam(value = "id", required = true) final String customerId) {
        String baseUrl = "redirect:/customer/search.htm?";
        String params;

        try{
            customerService.deleteCustomer(customerId);
            params = "statusMessageKey=" + CUSTOMER_DELETE_SUCCESS;
        }catch(ServiceLayerException sle){
            params = "errorMessageKey=" + CUSTOMER_DELETE_FAILURE;
        }
        ModelAndView result = new ModelAndView(baseUrl + params);
        return result;
    }
    
}
