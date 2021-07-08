package com.oup.eac.ws.rest.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;



@XmlType(name = "PlatformSTATUS")
@XmlEnum
public enum PlatformSTATUS {

    SUCCESS,
    ERROR;

    public String value() {
        return name();
    }

    public static PlatformSTATUS fromValue(String v) {
        return valueOf(v);
    }

}

