package com.oup.eac.web.controllers.helpers;

import javax.servlet.http.HttpServletResponse;

public final class ResponseHelper {

    /**
     * Private default constructor.
     */
    private ResponseHelper() {

    }

    public static void setNoCaching(final HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "No-cache");        
    }

}
