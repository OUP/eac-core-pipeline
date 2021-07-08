package com.oup.eac.dto.license;

import com.oup.eac.dto.LicenceDto;

public enum LicenceStatus {
       EXPIRED("licence.status.expired"),
       INACTIVE("licence.status.inactive"),
       DISABLED("licence.status.disabled"),
       ACTIVE("licence.status.active"),
       NO_LICENCE("licence.status.no.licence");
       
       private final String messageCode;
 
       LicenceStatus(String messageCode) {
               this.messageCode = messageCode;
       }
       
       public static LicenceStatus getLicenceStatus(LicenceDto licenceDto){
           if ( licenceDto == null) {
               return NO_LICENCE;
           }
           boolean active = licenceDto.isActive();
           boolean enabled = licenceDto.isEnabled();
           boolean expired = licenceDto.isExpired();
           
           if(expired){
               return EXPIRED;
           }else if(!enabled){
               return DISABLED;
           }else if(!active){
               return INACTIVE;           
           }else {
               return ACTIVE;
           }
       }

    public String getMessageCode() {
        return messageCode;
    }
       
       
}
