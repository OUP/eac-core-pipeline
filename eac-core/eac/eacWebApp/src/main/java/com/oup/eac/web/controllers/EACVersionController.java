package com.oup.eac.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.oup.eac.common.utils.EACSettings;

public class EACVersionController implements Controller {

    @Override
    public final ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return new ModelAndView("version", "version", EACSettings.getProperty(EACSettings.VERSION));
    }

}
