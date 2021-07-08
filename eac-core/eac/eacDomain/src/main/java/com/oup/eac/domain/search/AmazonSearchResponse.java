package com.oup.eac.domain.search;

import java.util.List;
import java.util.Map;

public class AmazonSearchResponse {

	
	//private String status;
		private Long responseTime;
		private int startAt;
		private int resultsFound;
		
		public List<Map<String, String>> resultFields;
		private String errorMessage;
		
		public AmazonSearchResponse(){
			this.errorMessage = "";
		}
		
		/*public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}*/
		
		public Long getResponseTime() {
			return responseTime;
		}
		public void setResponseTime(Long responseTime) {
			this.responseTime = responseTime;
		}
		public int getStartAt() {
			return startAt;
		}
		public void setStartAt(int startAt) {
			this.startAt = startAt;
		}
		public int getResultsFound() {
			return resultsFound;
		}
		public void setResultsFound(int resultsFound) {
			this.resultsFound = resultsFound;
		}
		public List<Map<String, String>> getResultFields() {
			return resultFields;
		}
		public void setResultFields(List<Map<String, String>> resultFields) {
			this.resultFields = resultFields;
		}
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		@Override
		public String toString(){
			return "SearchRestResponse:[" /*+"status="+status*/ +"responseTime=" +responseTime +"startAt=" +startAt +"resultsFound=" +resultsFound
					+"errorMessage=" +errorMessage +"]";
		}

}
