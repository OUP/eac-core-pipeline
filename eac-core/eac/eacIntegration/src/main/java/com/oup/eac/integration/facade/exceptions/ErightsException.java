/**
 * 
 */
package com.oup.eac.integration.facade.exceptions;

/**
 * @author harlandd
 * 
 */
public class ErightsException extends Exception {

    private final String code;
    
    private final Integer errorCode;

    public ErightsException(final String errorMessage) {
        super(errorMessage);
        code = null;
        errorCode= null;
    }

    public ErightsException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
        code = null;
        errorCode= null;
    }

    /**
     * Create an ErightsException with a message and a code.
     * 
     * @param errorMessage
     *            The error message
     * @param code
     *            The erights error code returned
     */
    public ErightsException(final String errorMessage, final String code) {
        super(errorMessage);
        this.code = code;
        errorCode= null;
    }

    /**
     * Create an ErightsException with a message and a code.
     * 
     * @param errorMessage
     *            The error message
     * @param cause
     *            The original cause
     * @param code
     *            The erights error code returned
     */
    public ErightsException(final String errorMessage, final Throwable cause, final String code) {
        super(errorMessage, cause);
        this.code = code;
        errorCode= null;
    }

    /**
     * The error code this exception represents.
     * 
     * @return The error code
     */
    public final String getCode() {
        return code;
    }
    
    /**
     * The error code this exception represents.
     * 
     * @return The error code
     */
    public final Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public final String getMessage() {
        return new StringBuilder().append((super.getMessage() != null) ? super.getMessage() : "No further detail was provided.").toString();
    }
    
    public ErightsException(final String errorMessage, final String code, final Integer errorCode) {
        super(errorMessage);
        this.code = code;
        this.errorCode= errorCode;
    }
}
