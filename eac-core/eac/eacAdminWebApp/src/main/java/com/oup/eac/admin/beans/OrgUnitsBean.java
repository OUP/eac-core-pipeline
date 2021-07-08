package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.oup.eac.domain.Division;

public class OrgUnitsBean implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(OrgUnitsBean.class);

    private final List<Division> orgUnits;
    
    private List<Division> newOrgUnits = new ArrayList<Division>();

    private List<String> orgUnitIndexesToRemove = new ArrayList<String>();
    
    public OrgUnitsBean(List<Division> orgUnits) {
        super();
        this.orgUnits = orgUnits;               
    }

    public List<Division> getOrgUnits() {
        return orgUnits;
    }

    public List<String> getOrgUnitIndexesToRemove() {
        return orgUnitIndexesToRemove;
    }

    public void setOrgUnitIndexesToRemove(List<String> orgUnitIndexesToRemove) {
        this.orgUnitIndexesToRemove = orgUnitIndexesToRemove;
    }

    public List<Division> getNewOrgUnits() {
        return newOrgUnits;
    }

    public void setNewOrgUnits(List<Division> newOrgUnits) {
        this.newOrgUnits = newOrgUnits;
    }
    
    public Set<Integer> getIndexesToDelete(){
        Set<Integer> result = new HashSet<Integer>();
        for(String index : this.orgUnitIndexesToRemove){
            try{
                int idx = Integer.valueOf(index);
                result.add(idx);
            }catch(Exception ex){
                LOG.warn("Problem converting " + index + " to Integer");
            }
        }
        return result;
    }
}
