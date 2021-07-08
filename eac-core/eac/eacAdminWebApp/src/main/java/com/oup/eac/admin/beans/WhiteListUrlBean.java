package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.BaseDomainObject;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.WhiteListUrl;

public class WhiteListUrlBean implements Serializable{

	private List<WhiteListUrl> urlList;
	
	private List<WhiteListUrl> newUrl = new ArrayList<WhiteListUrl>();

	private List<String> urlIndexesToRemove = new ArrayList<String>();
	
	 public WhiteListUrlBean(List<WhiteListUrl> urlList) {
	        super();
	        this.urlList = urlList;               
	    }

	public final List<WhiteListUrl> getNewUrl() {
		return newUrl;
	}

	public final void setNewUrl(List<WhiteListUrl> newUrl) {
		this.newUrl = newUrl;
	}

	public final List<String> getUrlIndexesToRemove() {
		return urlIndexesToRemove;
	}

	public final void setUrlIndexesToRemove(List<String> urlIndexesToRemove) {
		this.urlIndexesToRemove = urlIndexesToRemove;
	}

	public final List<WhiteListUrl> getUrlList() {
		return urlList = this.urlList;
	}
	
	public Set<Integer> getIndexesToDelete(){
        Set<Integer> result = new HashSet<Integer>();
        for(String index : this.urlIndexesToRemove){
            try{
                int idx = Integer.valueOf(index);
                result.add(idx);
            }catch(Exception ex){
            	ex.printStackTrace();
            }
        }
        return result;
    }
}
