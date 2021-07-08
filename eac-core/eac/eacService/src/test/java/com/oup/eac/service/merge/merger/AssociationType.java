package com.oup.eac.service.merge.merger;

public enum AssociationType {
    
    SINGLE_LICENCE("SIN_LIC"),
    LICENCE_INFO("LIC_INF"),
    PAIRED_LICENCE("PAI_LIC");
    
    private final String name;

    private AssociationType(String name){
        this.name=name;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
