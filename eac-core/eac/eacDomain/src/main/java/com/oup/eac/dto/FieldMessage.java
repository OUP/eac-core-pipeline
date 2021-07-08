/**
 * 
 */
package com.oup.eac.dto;

/**
 * @author harlandd Message for configuring message keys and args with field names
 */
public class FieldMessage extends Message {

    private final String fieldName;

    /**
     * @param fieldName
     *            the fieldName
     * @param messageKey
     *            the messageKey
     */
    public FieldMessage(final String fieldName, final String messageKey) {
        super(messageKey);
        this.fieldName = fieldName;
    }

    /**
     * @param fieldName
     *            the fieldName
     * @param messageKey
     *            the messageKey
     * @param defaultMessage
     *            the defaultMessage
     */
    public FieldMessage(final String fieldName, final String messageKey, final String defaultMessage) {
        super(messageKey, defaultMessage);
        this.fieldName = fieldName;
    }

    /**
     * @param fieldName
     *            the fieldName
     * @param messageKey
     *            the messageKey
     * @param defaultMessage
     *            the defaultMessage
     * @param args
     *            the args
     */
    public FieldMessage(final String fieldName, final String messageKey, final String defaultMessage, final Object... args) {
        super(messageKey, defaultMessage, args);
        this.fieldName = fieldName;
    }

    /**
     * @return the field name
     */
    public final String getFieldName() {
        return fieldName;
    }

    /**
     * @return the message type
     */
    @Override
    public final MessageType getMessageType() {
        return MessageType.FIELD_MESSAGE;
    }
}
