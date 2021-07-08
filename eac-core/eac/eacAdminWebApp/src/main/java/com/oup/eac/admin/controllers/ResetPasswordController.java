package com.oup.eac.admin.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.ResetAdminPasswordBean;
import com.oup.eac.admin.validators.ResetAdminPasswordBeanValidator;
import com.oup.eac.common.utils.token.TokenConverter;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Password;
import com.oup.eac.dto.AdminChangePasswordTokenDto;
import com.oup.eac.service.AdminService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/password")
public class ResetPasswordController {
	
	private static final Logger LOGGER = Logger.getLogger(ResetPasswordController.class);

	private static final String RESET_PASSWORD_VIEW = "resetPassword";
	private static final String CHANGE_PASSWORD_VIEW = "changePassword";
	private static final String CHANGE_PASSWORD_LOGGEDIN_VIEW = "changePasswordLoggedIn";
	private static final String LOGIN_VIEW = "login";
	private static final String RESET_PASSWORD_MAPPING = "/reset.htm";
	private static final String CHANGE_PASSWORD_MAPPING = "/change.htm";
	private static final String MODEL = "resetAdminPasswordBean";
	private static final String SUCCESS_MESSAGE = "status.password.change.success";
	
	private final AdminService adminService;
	private final ResetAdminPasswordBeanValidator validator;

	@Autowired
	public ResetPasswordController(final AdminService adminService, final ResetAdminPasswordBeanValidator validator) {
		this.adminService = adminService;
		this.validator = validator;
	}

	@RequestMapping(value=RESET_PASSWORD_MAPPING, method=RequestMethod.GET)
	public ModelAndView showResetPasswordForm() {
		return new ModelAndView(RESET_PASSWORD_VIEW);
	}
	
	@RequestMapping(value=RESET_PASSWORD_MAPPING, params="username", method=RequestMethod.POST)
	public ModelAndView processResetPassword(@RequestParam("username") final String username) throws Exception {
		adminService.resetPassword(username);
		ModelAndView modelAndView = new ModelAndView(RESET_PASSWORD_VIEW);
		modelAndView.addObject("processing", Boolean.TRUE);
		return modelAndView;
	}
	
	@RequestMapping(value=CHANGE_PASSWORD_MAPPING, params = "token", method=RequestMethod.GET)
	public ModelAndView showChangePasswordFormNotLoggedIn(@RequestParam("token") final String token, final HttpSession session) {
		if (isLoggedIn()) {
			return new ModelAndView(CHANGE_PASSWORD_LOGGEDIN_VIEW);
		}
		
		ModelAndView modelAndView = new ModelAndView(CHANGE_PASSWORD_VIEW);
		
		try {
			AdminChangePasswordTokenDto tokenDto = (AdminChangePasswordTokenDto) TokenConverter.decrypt(token, new AdminChangePasswordTokenDto());
			if (tokenDto.isExpired()) {
				LOGGER.warn("Attempt to use expired change password token for admin user '" + tokenDto.getUsername() + "'");
				modelAndView.addObject("invalidToken", Boolean.TRUE);
			} else {
				session.setAttribute("tokenDto", tokenDto);
			}
		} catch (final Exception e) {
			LOGGER.warn("Error getting token from change password link: " + e, e);
			modelAndView.addObject("invalidToken", Boolean.TRUE);
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value=CHANGE_PASSWORD_MAPPING, method=RequestMethod.GET)
	public ModelAndView showChangePasswordFormLoggedIn() {
		if (!isLoggedIn()) {
			return new ModelAndView("redirect:/" + LOGIN_VIEW);
		}
		
		return new ModelAndView(CHANGE_PASSWORD_LOGGEDIN_VIEW);
	}
	
	@RequestMapping(value=CHANGE_PASSWORD_MAPPING, params = {"password", "confirmPassword", "loggedIn"}, method=RequestMethod.POST)
	public ModelAndView processChangePasswordLoggedIn(
			@Valid @ModelAttribute(MODEL) final ResetAdminPasswordBean resetAdminPasswordBean, 
			final BindingResult bindingResult) throws ServiceLayerException {
		
		if (!isLoggedIn()) {
			return new ModelAndView("redirect:/" + LOGIN_VIEW);
		}
		
		// Reload the AdminUser as the one from the SecurityContext is from a different Hibernate session
		AdminUser adminUser = adminService.getAdminUserById(((AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
		
		if (bindingResult.hasErrors()) {
			return new ModelAndView(CHANGE_PASSWORD_LOGGEDIN_VIEW);
		}
		
		adminUser.setPassword(new Password(resetAdminPasswordBean.getPassword(), false));
		
		adminService.saveAdminUser(adminUser);
		
		return new ModelAndView("redirect:/home?statusMessageKey=" + SUCCESS_MESSAGE);
	}
	
	@RequestMapping(value=CHANGE_PASSWORD_MAPPING, params = {"password", "confirmPassword"}, method=RequestMethod.POST)
	public ModelAndView processChangePasswordNotLoggedIn(
			@Valid @ModelAttribute(MODEL) final ResetAdminPasswordBean resetAdminPasswordBean, 
			final BindingResult bindingResult,
			final HttpSession session) throws ServiceLayerException {
		
		AdminChangePasswordTokenDto tokenDto = (AdminChangePasswordTokenDto) session.getAttribute("tokenDto");
		
		if (tokenDto == null || tokenDto.isExpired()) {
			ModelAndView modelAndView = new ModelAndView(CHANGE_PASSWORD_VIEW);
			modelAndView.addObject("invalidToken", Boolean.TRUE);
			return modelAndView;
		}
		
		AdminUser adminUser = adminService.getAdminUserByUsername(tokenDto.getUsername());
		
		if (bindingResult.hasErrors()) {
			return new ModelAndView(CHANGE_PASSWORD_VIEW);
		}
		
		adminUser.setPassword(new Password(resetAdminPasswordBean.getPassword(), false));
		
		adminService.saveAdminUser(adminUser);
		
		return new ModelAndView("redirect:/login?statusMessageKey=" + SUCCESS_MESSAGE);
	}
	
	@ModelAttribute(MODEL)
	public ResetAdminPasswordBean createModel(
			@RequestParam(value = "password", required = false) final String password,
			@RequestParam(value = "confirmPassword", required = false) final String confirmPassword) {

		ResetAdminPasswordBean bean = new ResetAdminPasswordBean();
		bean.setPassword(password);
		bean.setConfirmPassword(confirmPassword);
		return bean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
	private boolean isLoggedIn() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof AdminUser;
	}
	
}
