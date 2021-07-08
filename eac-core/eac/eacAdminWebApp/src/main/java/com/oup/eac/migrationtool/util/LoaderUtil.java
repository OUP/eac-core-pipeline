package com.oup.eac.migrationtool.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.migrationtool.CustomerMigrationData;

public class LoaderUtil {
    

    private String anonymisePrefix;
    private String anonymiseMailSuffix;
    
    private Map<String, Integer> anonUsernameMap = new HashMap<String, Integer>();
    
    
    public CustomerMigrationData anonymise(CustomerMigrationData cmd) {
         
        if (StringUtils.isNotBlank(cmd.getFirstName())) {
            cmd.setFirstName(UUID.randomUUID().toString());
        }
        if (StringUtils.isNotBlank(cmd.getLastName())) {
            cmd.setLastName(UUID.randomUUID().toString());
        }
        if (StringUtils.isNotBlank(cmd.getEmailAddress())) {
            int idx = cmd.getEmailAddress().indexOf("@");
            if (idx >= 0) {
                StringBuffer sb = new StringBuffer();
                sb.append(cmd.getEmailAddress().substring(0, idx));
                sb.append(anonymiseMailSuffix);
                String newEmail = sb.toString();
                cmd.setEmailAddress(newEmail);
            }
        }
        
        /*if (StringUtils.isNotBlank(cmd.getUsername())) {
            int idx = cmd.getUsername().indexOf("@");
            if (idx >= 0) {
                StringBuffer sb = new StringBuffer();
                sb.append(cmd.getUsername().substring(0, idx));
                sb.append(anonymiseMailSuffix);
                String newUsername = sb.toString();
                cmd.setUsername(newUsername);
            }
        }*/
        
        // make usernames unique!
        String username = cmd.getUsername();
        Integer unCount = this.anonUsernameMap.get(username);
        if (unCount == null) {
            unCount = 0;
            unCount++;
        }       
        this.anonUsernameMap.put(cmd.getUsername(), unCount);
        username = anonymisePrefix + String.valueOf(unCount) + username;
        cmd.setUsername(username.toLowerCase());
        
        return cmd;
    }
    
    public String getAnonymisePrifix() {
        return anonymisePrefix;
    }


    public void setAnonymisePrifix(String anonymisePrefix) {
        this.anonymisePrefix = anonymisePrefix;
    }


    public String getAnonymiseMailSuffix() {
        return anonymiseMailSuffix;
    }


    public void setAnonymiseMailSuffix(String anonymiseMailSuffix) {
        this.anonymiseMailSuffix = anonymiseMailSuffix;
    }

    public Map<String, Integer> getAnonUsernameMap() {
        return anonUsernameMap;
    }

    public void setAnonUsernameMap(Map<String, Integer> anonUsernameMap) {
        this.anonUsernameMap = anonUsernameMap;
    }



}
