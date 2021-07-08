package com.oup.eac.admin.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.SkinBean;
import com.oup.eac.admin.validators.SkinBeanValidator;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.UrlSkinService;

@Controller
@RequestMapping("/mvc/skin/manage.htm")
public class ManageSkinController {
	
	private static final String MANAGE_SKIN_VIEW = "manageSkin";
	private static final String MODEL = "skinBean";
	
	private final UrlSkinService urlSkinService;
	private final SkinBeanValidator validator;
	
	@Autowired
	public ManageSkinController(
			final UrlSkinService urlSkinService, 
			final SkinBeanValidator validator) {
		this.urlSkinService = urlSkinService;
		this.validator = validator;
	}

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView(MANAGE_SKIN_VIEW);
	}
	
	@RequestMapping(method=RequestMethod.GET, params="id")
	public void getSkinDetails(@ModelAttribute(MODEL) final SkinBean skinBean, final HttpServletResponse response) throws IOException {
		UrlSkin selectedSkin = skinBean.getSelectedSkin();
		
		StringBuilder jsonResp = new StringBuilder();
		jsonResp.append("{\"siteName\": \"");
		jsonResp.append(StringUtils.defaultString(selectedSkin.getSiteName()));
		jsonResp.append("\", \"siteUrl\": \"");
		jsonResp.append(StringUtils.defaultString(selectedSkin.getUrl()));
		jsonResp.append("\", \"contactUrl\": \"");
		jsonResp.append(StringUtils.defaultString(selectedSkin.getContactPath()));
		jsonResp.append("\", \"skinUrl\": \"");
		jsonResp.append(StringUtils.defaultString(selectedSkin.getSkinPath()));
		jsonResp.append("\", \"customiser\": \"");
		jsonResp.append(StringUtils.defaultString(selectedSkin.getUrlCustomiserBean()));
		jsonResp.append("\", \"primarySite\": \"");
		jsonResp.append(selectedSkin.isPrimarySite());
		jsonResp.append("\"}");
		
		response.setContentType("application/json");
		OutputStream out = response.getOutputStream();
		IOUtils.write(jsonResp, out);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView saveSkin(@Valid @ModelAttribute(MODEL) final SkinBean skinBean, final BindingResult bindingResult) {
		
		ModelAndView modelAndView = new ModelAndView(MANAGE_SKIN_VIEW);
		modelAndView.addObject("editing", Boolean.TRUE);
		
		if (!bindingResult.hasErrors()) {
			UrlSkin urlSkin = skinBean.getUpdatedSkin();
			urlSkinService.saveUrlSkin(urlSkin);
			//flushCache();
			skinBean.setSkins(urlSkinService.getAllUrlSkinsOrderedBySiteName());
			modelAndView.addObject("statusMessageKey", "status.skin.save.success");
		}
		
		return modelAndView;
	}
	
	/*private void flushCache() {
		Cache urlMapCache = cacheManager.getCache("urlMapCache");
		urlMapCache.removeAll();
	}*/
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deleteSkin(@ModelAttribute(MODEL) final SkinBean skinBean) {
		urlSkinService.deleteUrlSkin(skinBean.getSelectedSkin());
		skinBean.setSkins(urlSkinService.getAllUrlSkinsOrderedBySiteName());
		skinBean.clearSelectedSkin();
		ModelAndView modelAndView = new ModelAndView(MANAGE_SKIN_VIEW);
		modelAndView.addObject("statusMessageKey", "status.skin.delete.success");
		return modelAndView;
	}
	
	@ModelAttribute(MODEL)
	public SkinBean createModel(@RequestParam(value = "id", required = false) final String id) {
		List<UrlSkin> urlSkins = urlSkinService.getAllUrlSkinsOrderedBySiteName();
		SkinBean skinBean = new SkinBean(urlSkins);

		if (StringUtils.isNotBlank(id)) {
			skinBean.setSelectedSkin(urlSkinService.getUrlSkinById(id));
		}
		
		return skinBean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(validator);
	}
}
