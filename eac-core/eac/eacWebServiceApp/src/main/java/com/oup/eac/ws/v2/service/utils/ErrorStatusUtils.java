package com.oup.eac.ws.v2.service.utils;

import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;

public abstract class ErrorStatusUtils {

    public static ErrorStatus getServerErrorStatus(String reason){
        return getErrorStatus(StatusCode.SERVER_ERROR, reason);
    }

    public static ErrorStatus getClientErrorStatus(String reason){
        return getErrorStatus(StatusCode.CLIENT_ERROR, reason);
    }
    
    public static ErrorStatus getErrorStatus(StatusCode sc, String reason){
        ErrorStatus status = new ErrorStatus();
        status.setStatusCode(sc);
        status.setStatusReason(reason);
        return status;
    }
}
