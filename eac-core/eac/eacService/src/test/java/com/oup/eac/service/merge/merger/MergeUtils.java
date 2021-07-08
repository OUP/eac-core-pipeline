package com.oup.eac.service.merge.merger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.oup.eac.dto.LicenceDto;
import com.oup.eac.service.merge.RegistrationLicenceMergeInfoDto;

public abstract class MergeUtils {
    
    public static final List<LicenceDto> EMPTY_LICS = Collections.<LicenceDto> emptyList();
    
    public static final List<RegistrationLicenceMergeInfoDto> EMPTY_REGS = Collections.<RegistrationLicenceMergeInfoDto> emptyList();

    public static final Comparator<RegistrationLicenceMergeInfoDto> REG_DTO_COMP = new Comparator<RegistrationLicenceMergeInfoDto>() {
        @Override
        public int compare(RegistrationLicenceMergeInfoDto regDto1, RegistrationLicenceMergeInfoDto regDto2) {
            return regDto1.getRegistration().getCreatedDate().compareTo(regDto2.getRegistration().getCreatedDate());
        }
    };

    public static final Comparator<LicenceDto> LIC_DTO_COMP = new Comparator<LicenceDto>() {
        @Override
        public int compare(LicenceDto licDto1, LicenceDto licDto2) {
            int result = Boolean.valueOf(licDto2.isActive()).compareTo(Boolean.valueOf(licDto1.isActive()));
            if(result == 0){
                result = licDto1.getLicenseId().compareTo(licDto2.getLicenseId());
            }
            return result; 
        }
    };

    /**
     * Handy generic method for adding element into the list associated with the key
     * 
     * @param map
     *            the map
     * @param key
     *            the key for the list
     * @param value
     *            the value to be added to the list
     */
    public static <K, V> void addMapListEntry(Map<K, List<V>> map, K key, V value) {
        List<V> temp = map.get(key);
        if (temp == null) {
            temp = new ArrayList<V>();
            map.put(key, temp);
        }
        temp.add(value);
    }
    
    static class MyLicenceDto extends LicenceDto{
        private String name;
        public void setName(String name){
            this.name = name;
        }
        public String toString(){
            return name;
        }
        
       public MyLicenceDto(final String eRightsId, final DateTime expiryDateTime, final boolean expired, final boolean active) {
           super(eRightsId,expiryDateTime,expired,active,true,true,true);
        }

    }
    public static void main(String args[]){
        MyLicenceDto licDto1 = new MyLicenceDto("111",new DateTime().plusDays(5),false,false);
        licDto1.setName("ONE");
        MyLicenceDto licDto2 = new MyLicenceDto("222",new DateTime().plusDays(10),false,true);//we want the 
        licDto2.setName("TWO");
        List<? extends LicenceDto> lics = Arrays.asList(licDto1,  licDto2);
        Collections.sort(lics,LIC_DTO_COMP);
        System.out.println(lics);
        
        List<Boolean> vals1 = Arrays.asList(false,true);
        Collections.sort(vals1);
        System.out.println(vals1);
        
        List<Boolean> vals2 = Arrays.asList(true,false);
        Collections.sort(vals2);
        System.out.println(vals2);
    }
    
}
