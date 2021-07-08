package com.oup.eac.domain.utils.audit;

import org.apache.log4j.Logger;

public final class EacApp {

    private static final Logger LOG = Logger.getLogger(EacApp.class);

    private static EacAppType type = EacAppType.UNDEF;

    private EacApp() {
        // private constructor
    }

    public static EacAppType getType() {
        if (LOG.isDebugEnabled()) {
            String cloader = EacApp.class.getClassLoader().toString();
            String msg = String.format("getType Called on [%s] : result is [%s]", cloader, type);
            LOG.debug(msg);
        }
        return type;
    }

    public static EacAppType setType(EacAppType type) {
        EacApp.type = type;
        return getType();
    }

    public static EacAppType setType(EacAppType type, String source) {
        LOG.info("setType called from " + source);
        return setType(type);
    }

    public static boolean isAdmin() {
        return getType() == EacAppType.ADMIN;
    }

    public static boolean isWebServices() {
        boolean result = getType() == EacAppType.WEB_SERVICES;
        return result;
    }

}
