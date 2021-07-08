package com.oup.eac.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;

import com.oup.eac.util.BatchJobProperties;

public class MailCriteria {


    private String subject;

    private String text;

    private InternetAddress from;

    private InternetAddress replyTo;

    private List<InternetAddress> toAddresses = new ArrayList<InternetAddress>();

    private List<InternetAddress> ccAddresses = new ArrayList<InternetAddress>();

    private List<InternetAddress> bccAddresses = new ArrayList<InternetAddress>();

    private byte[] attachment;

    private String attachmentName;
    
    private final Date sent;
    
    public MailCriteria(Date sent) {
        this.sent = sent;
    }

    public MailCriteria() {
        this(new Date());
    }

    /**
     * @return is the message a multipart message.
     */
	public boolean isMultipartMessage() {
        return isHasAttachment();
    }

    /**
     * @return has the message got an attachment.
     */
	public boolean isHasAttachment() {
        return attachment != null;
    }

    /**
     * @return the attachment
     */
	public byte[] getAttachment() {
        return attachment.clone();
    }

    /**
     * @param attachment
     *            the attachment to set
     */
	public void setAttachment(final byte[] attachment) {
        this.attachment = attachment.clone();
    }

    /**
     * @return the attachmentName
     */
	public String getAttachmentName() {
        return attachmentName;
    }

    /**
     * @param attachmentName
     *            the attachmentName to set
     */
	public void setAttachmentName(final String attachmentName) {
        this.attachmentName = attachmentName;
    }

    /**
     * @return the subject
     */
	public String getSubject() {
        return subject;
    }

    /**
     * @param subject
     *            the subject to set
     */
	public void setSubject(final String subject) {
        this.subject = subject;
    }

    /**
     * @return the text
     */
	public String getText() {
        return text;
    }

    /**
     * @param text
     *            the text to set
     */
	public void setText(final String text) {
        this.text = text;
    }

    /**
     * @return the from
     */
	public InternetAddress getFrom() {
        return from;
    }

    /**
     * @param from
     *            the from to set
     */
	public void setFrom(final InternetAddress from) {
        this.from = from;
    }

    /**
     * @return the replyTo
     */
	public InternetAddress getReplyTo() {
        return replyTo;
    }

    /**
     * @param replyTo
     *            the replyTo to set
     */
	public void setReplyTo(final InternetAddress replyTo) {
        this.replyTo = replyTo;
    }

    /**
     * @return the toAddresses
     */
	public List<InternetAddress> getToAddresses() {
        return toAddresses;
    }

    /**
     * @param toAddresses
     *            the toAddresses to set
     */
	public void setToAddresses(final List<InternetAddress> toAddresses) {
        this.toAddresses = toAddresses;
    }

    /**
     * @return the ccAddresses
     */
	public List<InternetAddress> getCcAddresses() {
        return ccAddresses;
    }

    /**
     * @param ccAddresses
     *            the ccAddresses to set
     */
	public void setCcAddresses(final List<InternetAddress> ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    /**
     * @return the bccAddresses
     */
	public List<InternetAddress> getBccAddresses() {
        return bccAddresses;
    }

    /**
     * @param bccAddresses
     *            the bccAddresses to set
     */
	public void setBccAddresses(final List<InternetAddress> bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    /**
     * 
     * @param internetAddress
     *            internet address
     */
	public void addToAddress(final InternetAddress internetAddress) {
        toAddresses.add(internetAddress);
    }

    /**
     * 
     * @param internetAddress
     *            internet address
     */
	public void addCcAddress(final InternetAddress internetAddress) {
        ccAddresses.add(internetAddress);
    }

    /**
     * 
     * @param internetAddress
     *            internet address
     */
	public void addBccAddress(final InternetAddress internetAddress) {
        bccAddresses.add(internetAddress);
    }

    /**
     * 
     * @return is there are cc addresses.
     */
	public boolean isHasCcAddresses() {
        return ccAddresses.size() > 0;
    }

    /**
     * 
     * @return is there are bcc addresses.
     */
	public boolean isHasBccAddresses() {
        return bccAddresses.size() > 0;
    }

    /**
     * 
     * @return is there are to addresses.
     */
	public boolean isHasToAddresses() {
        return toAddresses.size() > 0;
    }

    /**
     * 
     * @return get the to addresses as an array.
     */
	public InternetAddress[] getToAddressesAsArray() {
        if (getToAddresses().size() == 0) {
            return null;
        }
        return getToAddresses().toArray(new InternetAddress[getToAddresses().size()]);
    }

    /**
     * 
     * @return get the cc addresses as an array.
     */
	public InternetAddress[] getCcAddressesAsArray() {
        if (getCcAddresses().size() == 0) {
            return null;
        }
        return getCcAddresses().toArray(new InternetAddress[getCcAddresses().size()]);
    }

    /**
     * 
     * @return get the bcc addresses as an array.
     */
	public InternetAddress[] getBccAddressesAsArray() {
        if (getBccAddresses().size() == 0) {
            return null;
        }
        return getBccAddresses().toArray(new InternetAddress[getBccAddresses().size()]);
    }

    /**
     * Output all of the message details.
     * 
     * @return the email message
     */
	public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\nFrom: " + getFrom());
        if (getReplyTo() != null) {
            stringBuilder.append("\nReply To: " + getReplyTo());
        }
        SimpleDateFormat sdf = new SimpleDateFormat(BatchJobProperties.EMAIL_SENT_DEBUG_FORMAT);
        stringBuilder.append("\nSent: ").append(sdf.format(sent));
        
        stringBuilder.append("\nTo Recipients: ").append(outputRecipients(getToAddresses()));
        if(isHasCcAddresses()) stringBuilder.append("\nCc Recipients: ").append(outputRecipients(getCcAddresses()));
        if(isHasBccAddresses())stringBuilder.append("\nBcc Recipients: ").append(outputRecipients(getBccAddresses()));
        stringBuilder.append("\nSubject: ").append(getSubject());
        stringBuilder.append("\nMessage: \n\n").append(getText());
        if (isHasAttachment()) {
            stringBuilder.append("\nAttachment: ").append(getAttachmentName());
            stringBuilder.append("\nAttachment exists: ").append(Boolean.toString(getAttachment() != null));
        }
        return stringBuilder.toString();
    }

    /**
     * 
     * @param recipients
     *            the recipients
     * @return output all of the recipients as a string
     */
    private String outputRecipients(final List<InternetAddress> recipients) {
        StringBuilder stringBuilder = new StringBuilder();
        for (InternetAddress email : recipients) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(email);
        }
        return stringBuilder.toString();
    }

    /**
     * Gets the sent.
     *
     * @return the sent
     */
    public Date getSent() {
        return sent;
    }

}
