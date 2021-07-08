package com.oup.eac.common.utils.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	/**getStackTrace.
     * @param e
     * @return String
     */
    public static String getStackTrace(final Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString();
        return sStackTrace;
    }
}
