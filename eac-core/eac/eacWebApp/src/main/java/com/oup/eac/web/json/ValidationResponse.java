package com.oup.eac.web.json;

/**
 * @author David Hay
 * Converted to json by jackson for ajax password validation.
 */
public class ValidationResponse {

    private boolean valid;
            
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
