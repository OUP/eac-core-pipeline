package com.oup.eac.service.impl;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.EmailService;

/**
 * Email service providing business email services
 * 
 * @author harlandd
 * @author Ian Packard 
 */
@Service(value="emailService")
public class EmailServiceImpl implements EmailService {

    private static final String UTF_8 = "UTF-8";

    private static final Logger LOGGER = Logger.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    /**
     * @param mailSender
     *            the mail sender
     */
    @Autowired
    public EmailServiceImpl(final JavaMailSender mailSender) {
        super();
        Assert.notNull(mailSender);
        this.mailSender = mailSender;
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
        
        if (EACSettings.getBoolProperty(EACSettings.EMAIL_DISABLED)) {
            LOGGER.info("Email is disabled. Details below:");
            LOGGER.info(mailCriteria);
            return;
        }
        if(LOGGER.isDebugEnabled()) {
        	LOGGER.debug("Processing email:\n" + mailCriteria);
        }
        if (mailCriteria.getReplyTo() == null) {
            try {
                mailCriteria.setReplyTo(EmailUtils.getInternetAddressReplyTo());
            } catch (AddressException ex) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("problem setting replyTo on mail criteria", ex);
                }
            }
        }
        try {
	        MimeMessage message = mailSender.createMimeMessage();
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
	        //add this line and comment below code as part of removing HTML content
	        helper.setText(mailCriteria.getText());
	        /*if (mailCriteria.getSubject().equalsIgnoreCase(EACSettings.getProperty(EACSettings.PASSWORD_RESET_EMAIL_SUBJECT_NEW))){
	        	helper.setText(mailCriteria.getText(),true);
	        }else{
	        	helper.setText(mailCriteria.getText());
	        }*/
	        helper.setFrom(mailCriteria.getFrom());
	        helper.setSentDate(mailCriteria.getSent());
	        if (mailCriteria.isHasAttachment()) {
	            InputStreamSource attachmentStream = new ByteArrayResource(mailCriteria.getAttachment());
	            helper.addAttachment(mailCriteria.getAttachmentName(), attachmentStream);
	        }
	
	       mailSender.send(message);
        } catch (MessagingException e) {
        	LOGGER.error("Error sending email:\n " + mailCriteria, e);
		}
		AuditLogger.logSystemEvent("Email", "To:"+mailCriteria.getToAddresses().get(0), "Subject"+mailCriteria.getSubject());
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
                addresses[i] = new InternetAddress(InternationalEmailAddress.convertToAscii(email));
            } catch (IllegalArgumentException e) {
                throw new AddressException("Invalid email address");
            }            
        }
        return addresses;
    }

}