package com.oup.eac.integration.erights;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.xml.XMLUtils;

/**
 * Simple SOAPHandler checks the performance of webservice calls and logs the results.
 * 
 * @author packardi
 * 
 */
public class PerformanceSOAPHandler implements SOAPHandler<SOAPMessageContext> {

    /**
     * The logger used for general messages.
     */
    private static final Logger LOG = Logger.getLogger(PerformanceSOAPHandler.class);

    /**
     * The logger used for performance specific data.
     */
    private static final Logger TIMING_LOG = Logger.getLogger("ERIGHTS_PERFORMANCE");
    private static final String TIMER_KEY = "StopWatch";
    private static final String TIMING_RECORD_SEPARATOR = ",";

    @Override
    public final Set<QName> getHeaders() {
        return null;
    }

    @Override
    public void close(final MessageContext context) {
    }

    @Override
    public final boolean handleFault(final SOAPMessageContext context) {
        
        final Object timer = context.remove(TIMER_KEY);
        if (timer != null) {
            ((StopWatch)timer).stop();
            logTimings((StopWatch)timer, context.getMessage());
        }
        return true;
    }

    @Override
    public final boolean handleMessage(final SOAPMessageContext context) {

        final Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            final StopWatch timer = new StopWatch();
            timer.start(getMethodName(context));
            context.put(TIMER_KEY, timer);
        } else {
            final Object timer = context.remove(TIMER_KEY);
            if (timer != null) {            
                ((StopWatch)timer).stop();
                logTimings((StopWatch)timer, context.getMessage());
            }
        }

        return true;
    }

    private String getMethodName(final SOAPMessageContext context) {
        try {
            return context.getMessage().getSOAPBody().getFirstChild().getLocalName();
        } catch (SOAPException e) {
            LOG.error("Unable to determine method: " + e, e);
            return "Erights web service call (method unknown)";
        }
    }

    /**
     * Pretty print timings and log error if SLA exceeded 
     * @param stopWatch
     */
    private void logTimings(final StopWatch stopWatch, final SOAPMessage soapMessage) {
        TIMING_LOG.debug(createTimingRecord(stopWatch));
        LOG.debug(stopWatch.prettyPrint());
        if (stopWatch.getTotalTimeMillis() > EACSettings.getIntProperty(EACSettings.ERIGHTS_WS_SLA_MILLIS)) {
        	StringBuffer errorMessage = new StringBuffer("Erights Webservice call exceeded SLA (")
							        	.append(stopWatch.getTotalTimeMillis())
							        	.append("mS)");
        	String message = XMLUtils.prettyPrint(soapMessage);
        	if(message != null) errorMessage.append("\n").append(message);
            LOG.error(errorMessage.toString());
        }        
    }

    /**
     * Creates a single comma-separated record for appending to a log.
     * 
     * @param stopWatch
     *            The {@link StopWatch} containing the timing.
     * @return The timing record.
     */
    private String createTimingRecord(final StopWatch stopWatch) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.UK);
        StringBuilder stringBuilder = new StringBuilder(dateFormat.format(new Date()));
        stringBuilder.append(TIMING_RECORD_SEPARATOR);
        stringBuilder.append(stopWatch.getLastTaskName());
        stringBuilder.append(TIMING_RECORD_SEPARATOR);
        stringBuilder.append(stopWatch.getTotalTimeMillis());
        return stringBuilder.toString();
    }
}
