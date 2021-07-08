package com.oup.eac.service;

import javax.mail.MessagingException;

import com.oup.eac.domain.MailCriteria;

/**
 * Email service providing business email services.
 * 
 * @author harlandd 
 */
public interface EmailService {

    /**
     * @param mailCriteria
     *            mail criteria
     * @throws MessagingException
     *             messaging exception
     */
    void sendMail(MailCriteria mailCriteria);
}
