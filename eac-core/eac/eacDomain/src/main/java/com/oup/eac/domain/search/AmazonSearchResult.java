package com.oup.eac.domain.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmazonSearchResult {
	
	public String id;
	public Map<String, String> fields = new HashMap<String, String>();
	
	public String getField(String field) {
		return fields.get(field);
	}
	
	public List<String> getAllFieldNames() {
		return new ArrayList<String>(fields.keySet());
	}
	
	@Override
	public String toString(){
		return "Result:[ id=" +id +"\nFields=" +fields.entrySet() +"]";
	}



}
