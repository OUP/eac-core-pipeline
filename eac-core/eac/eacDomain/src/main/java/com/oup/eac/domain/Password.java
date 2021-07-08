package com.oup.eac.domain;

import java.io.Serializable;

public class Password implements Serializable {

    private static final long serialVersionUID = -6785302244619578107L;
    private final String value;
    private final boolean hashed;

    public Password(String value, boolean hashed) {
        this.value = value;
        this.hashed = hashed;
    }

    public boolean isHashed() {
        return hashed;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (hashed ? 1231 : 1237);
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Password other = (Password) obj;
        if (hashed != other.hashed)
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
}