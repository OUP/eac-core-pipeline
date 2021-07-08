package com.oup.eac.cloudSearch;

import java.util.List;

import com.oup.eac.domain.search.AmazonSearchResult;

public class AmazonCloudSearchResponse {

	public String rid;
	public long time;
	public int found;
	public int start;
	public List<AmazonSearchResult> results;
	
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getFound() {
		return found;
	}
	public void setFound(int found) {
		this.found = found;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public List<AmazonSearchResult> getResults() {
		return results;
	}
	public void setResults(List<AmazonSearchResult> results) {
		this.results = results;
	}
	
	@Override
	public String toString(){
		
		return "AmazonCloudSearchResponse:[" +"rid:"+rid +"time:"+time +"found:"+found +"start:"+start +"results:" +results +"]";
	}

}
