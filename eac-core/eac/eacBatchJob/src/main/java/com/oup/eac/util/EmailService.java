package com.oup.eac.util;

import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.Assert;

import com.oup.eac.domain.MailCriteria;



public class EmailService {


    private static final String UTF_8 = "UTF-8";

    private static final Logger LOGGER = Logger.getLogger(EmailService.class);
    private static final String ENCODING = "B";
    private static final String AT_SYMBOL = "@";
    public static final String NEW_LINE_VALUE;
    
    public static final String NEW_LINE_KEY="nl";
    
    static {
        NEW_LINE_VALUE = System.getProperty("line.separator");
    }

    private final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    /**
     * @param mailSender
     *            the mail sender
     */
    public EmailService() {
        super();
      
    }

    /**
     * @param mailCriteria
     *            mail criteria
     *             messaging exception
     */
    public final void sendMail(final MailCriteria mailCriteria)  {
        Assert.isTrue(mailCriteria.getFrom() != null);
        Assert.isTrue(mailCriteria.getSubject() != null);
        Assert.isTrue(mailCriteria.getToAddresses() != null && mailCriteria.getToAddresses().isEmpty() == false);
        Assert.isTrue(mailCriteria.getText() != null);
        Assert.isTrue(mailCriteria.getSent() != null);
        
        if (BatchJobProperties.EMAIL_DISABLED) {
            LOGGER.info("Email is disabled. Details below:");
            LOGGER.info(mailCriteria);
            return;
        }
        if(LOGGER.isDebugEnabled()) {
        	LOGGER.debug("Processing email:\n" + mailCriteria);
        }
        if (mailCriteria.getReplyTo() == null) {
            try {
                mailCriteria.setReplyTo(getInternetAddressReplyTo());
            } catch (AddressException ex) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("problem setting replyTo on mail criteria", ex);
                }
            }
        }
        try {
        	/*Properties props = new Properties();
    		props.put("mail.smtp.auth", "true");
    		props.put("mail.smtp.starttls.enable", "true");
    		props.put("mail.smtp.host", "34.245.196.243");
    		props.put("mail.smtp.port", "25");

    		Session session = Session.getInstance(props,
    		  new javax.mail.Authenticator() {
    			protected PasswordAuthentication getPasswordAuthentication() {
    				return new PasswordAuthentication("do_not_reply@oup.com", "Passw0rd");
    			}
    		  });
    		//Session session = Session.getInstance(props);
*/	        MimeMessage message =  mailSender.createMimeMessage();//new MimeMessage(session);
	        MimeMessageHelper helper = new MimeMessageHelper(message, mailCriteria.isMultipartMessage(), UTF_8);
	        if (mailCriteria.getReplyTo() != null) {
	            helper.setReplyTo(mailCriteria.getReplyTo());
	        }
	        if (mailCriteria.isHasToAddresses()) {
	            helper.setTo(convertIDNs(mailCriteria.getToAddresses()));
	        }
	        if (mailCriteria.isHasCcAddresses()) {
	            helper.setCc(convertIDNs(mailCriteria.getCcAddresses()));
	        }
	        if (mailCriteria.isHasBccAddresses()) {
	            helper.setBcc(convertIDNs(mailCriteria.getBccAddresses()));
	        }
	        helper.setSubject(mailCriteria.getSubject());
	        helper.setText(mailCriteria.getText());
	        helper.setFrom(mailCriteria.getFrom());
	        helper.setSentDate(mailCriteria.getSent());
	        if (mailCriteria.isHasAttachment()) {
	            InputStreamSource attachmentStream = new ByteArrayResource(mailCriteria.getAttachment());
	            helper.addAttachment(mailCriteria.getAttachmentName(), attachmentStream);
	        }
	        final String mailHost = BatchJobProperties.MAIL_SMTP_HOST;
	        final String mailPort = BatchJobProperties.MAIL_SMTP_PORT;
	        final String mailUsername = BatchJobProperties.MAIL_SMTP_USERNAME;
	        final String mailPassword = BatchJobProperties.MAIL_SMTP_PASSWORD;
	        mailSender.setHost(mailHost);
	        mailSender.setPort(Integer.parseInt(mailPort));
	        mailSender.setUsername(mailUsername);
	        mailSender.setPassword(mailPassword);
	        
	        Properties mailProperties = new Properties();
			mailProperties.put("mail.smtp.auth", Boolean.TRUE.toString());
			mailProperties.put("mail.smtp.starttls.enable", Boolean.TRUE.toString());
			mailSender.setJavaMailProperties(mailProperties);
	        //Transport.send(message);
	        mailSender.send(message);
        } catch (MessagingException e) {
        	LOGGER.error("Error sending email:\n " + mailCriteria, e);
		}
		//AuditLogger.logSystemEvent("Email", "To:"+mailCriteria.getToAddresses().get(0), "Subject"+mailCriteria.getSubject());
    }

    /**
     * Convert IDN email addresses to punycode format
     * @param recipients
     * @return
     * @throws AddressException 
     */
    private InternetAddress[] convertIDNs(final List<InternetAddress> recipients) throws AddressException {
        InternetAddress[] addresses = new InternetAddress[recipients.size()];
        for (int i = 0; i < addresses.length; i++) {
            final String email = recipients.get(i).getAddress();
            try {
                addresses[i] = new InternetAddress(convertToAscii(email));
            } catch (IllegalArgumentException e) {
                throw new AddressException("Invalid email address");
            }            
        }
        return addresses;
    }
    
    /**
     * Convert email address to ASCII Compatible Encoding (ACE)
     * @param emailAddress
     * @return
     */
    public static String convertToAscii(final String emailAddress) {
        String email = null;
        if (emailAddress != null) {
            final int atPosition = emailAddress.indexOf(AT_SYMBOL);
            if (atPosition > 0) {
                final String localPart = emailAddress.substring(0, atPosition);
                final String domain = emailAddress.substring(atPosition + 1);
                // handle local part by quoted printable conversion to UTF-8
                // handle IDNs by punycode conversion to ASCII Compatible Encoding (ACE)
                try {
                    final String localPartQP = MimeUtility.encodeText(localPart, UTF_8, ENCODING);
                    final String domainAce = IDN.toASCII(domain);
                    email = localPartQP + AT_SYMBOL + domainAce;
                    if (!emailAddress.equals(email)) {
                        LOGGER.debug("Converted:" + emailAddress + " to:" + email);
                    }
                } catch (Exception e) {                
                	LOGGER.warn("Unable to convert email address", e);
                    throw new IllegalArgumentException("Unable to convert email address", e);
                }
            } else {
            	LOGGER.warn("Invalid email address:" + emailAddress);
                throw new IllegalArgumentException("Invalid email address");
            }
        }
        return email;
    }
    
    public static InternetAddress getInternetAddressReplyTo() throws AddressException { 
        InternetAddress result = new InternetAddress(BatchJobProperties.EAC_EMAIL_REPLY_TO_ADDRESS);
        return result;
    }
    
    public static InternetAddress getInternetAddressFrom() throws UnsupportedEncodingException {
        InternetAddress result = new InternetAddress(BatchJobProperties.EMAIL_FROM_ADDRESS, BatchJobProperties.EMAIL_FROM_TITLE);
        return result;
    }
    
    public static String mergeTemplateIntoString(
            VelocityEngine velocityEngine, String templateLocation, Map<String,Object> model)
            throws VelocityException {        
        addNewLineToMap(model);
        
        String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
        return result;
    }

    private static void addNewLineToMap(Map<String,Object> model){
        model.put(NEW_LINE_KEY, NEW_LINE_VALUE);
    }
    
    public static VelocityContext createVelocityContext() {
        Map<String,Object> model = new HashMap<String,Object>();
        addNewLineToMap(model);
        VelocityContext ctx = new VelocityContext(model);
        return ctx;
    }
}
