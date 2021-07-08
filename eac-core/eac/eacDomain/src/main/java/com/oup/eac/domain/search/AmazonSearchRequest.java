package com.oup.eac.domain.search;

import java.util.HashMap;
import java.util.List;

public class AmazonSearchRequest {
	private int resultsPerPage;
	private int startAt;
	private List<AmazonSearchFields> searchFieldsList;
	private List<String> searchResultFieldsList;
	private HashMap<String, String> sortFields;
	
	
	public Integer getResultsPerPage() {
		return resultsPerPage;
	}
	public void setResultsPerPage(int resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}
	public int getStartAt() {
		return startAt;
	}
	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}
	public List<String> getSearchResultFieldsList() {
		return searchResultFieldsList;
	}
	public void setSearchResultFieldsList(List<String> searchResultFieldsList) {
		this.searchResultFieldsList = searchResultFieldsList;
	}
	public List<AmazonSearchFields> getSearchFieldsList() {
		return searchFieldsList;
	}
	public void setSearchFieldsList(List<AmazonSearchFields> searchFieldsList) {
		this.searchFieldsList = searchFieldsList;
	}
	public HashMap<String, String> getSortFields() {
		return sortFields;
	}
	public void setSortFields(HashMap<String, String> sortFields) {
		this.sortFields = sortFields;
	}
	
	@Override
	public String toString(){
		
		return "Search Request Object:-\n" +"Results/page =" +resultsPerPage +", Start at =" +startAt 
				+"\nList of Search fields =" +searchFieldsList 
				+"\nList of Result Fields =" +searchResultFieldsList  
				+"\nList of Sort Fields = [" +sortFields.keySet() +"]";
	}


}
