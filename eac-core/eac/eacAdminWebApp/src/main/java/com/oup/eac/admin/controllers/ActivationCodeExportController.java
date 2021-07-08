package com.oup.eac.admin.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.oup.eac.common.utils.lang.EACStringUtils;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.service.ActivationCodeService;

@Controller
public class ActivationCodeExportController {
	
	@Autowired
	private ActivationCodeService activationCodeService;
	
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public ModelAndView export(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		return exportFile(request, response, false);
	}
	
	@RequestMapping(value="/exportFormatted", method=RequestMethod.GET)
	public ModelAndView exportFormatted(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		return exportFile(request, response, true);
	}
	
	private final ModelAndView exportFile(final HttpServletRequest request, final HttpServletResponse response, final boolean format) throws Exception {
		String batchId = request.getParameter("batchId");
		String actualBatchId= null;
		if(StringUtils.isBlank(batchId)) {
			return null;
		}
		actualBatchId = HtmlUtils.htmlUnescape(batchId) ;
		byte[] in = activationCodeService.getActivationCodeByBatch(actualBatchId, format);
		ActivationCodeBatch activationCodeBatch = activationCodeService.getActivationCodeBatchByBatchId(actualBatchId,true);
		prepareResponse(response, in.length, EACStringUtils.removeNonAlphanumericAndWhitespace(activationCodeBatch.getBatchId()));
		FileCopyUtils.copy(in, response.getOutputStream());
		return null; 		
	}

	private void prepareResponse(final HttpServletResponse response, final int contentLength, final String name) {
		response.setContentType("text/plain");
		response.setContentLength(contentLength);
		response.setHeader("Content-Disposition","attachment; filename=\"" + name + ".txt\"");
	}
}
