package com.oup.eac.cloudSearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.cloudsearchv2.AmazonCloudSearchClient;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.oup.eac.domain.search.AmazonSearchResult;


public class AmazonCloudSearchRequest extends AmazonCloudSearchClient {

	private static final Logger logger = Logger.getLogger(AmazonCloudSearchRequest.class);

	private String searchEndpoint;

	private String documentEndpoint;

	/**
	 * Constructs a new client to invoke service methods on
	 * AmazonCloudSearchv2 using the specified AWS account credentials.
	 *
	 * <p>
	 * All service calls made using this new client object are blocking, and will not
	 * return until the service call completes.
	 *
	 * @param awsCredentials The AWS credentials (access key ID and secret key) to use
	 *                       when authenticating with AWS services.
	 */
	public AmazonCloudSearchRequest(AWSCredentials awsCredentials) {

		super(awsCredentials, new ClientConfiguration());
	}

	public String getSearchEndpoint() {
		return searchEndpoint;
	}

	public void setSearchEndpoint(String searchEndpoint) {
		this.searchEndpoint = searchEndpoint;
	}

	public String getDocumentEndpoint() {
		return documentEndpoint;
	}

	public void setDocumentEndpoint(String documentEndpoint) {
		this.documentEndpoint = documentEndpoint;
	}

	public AmazonCloudSearchResponse searchCloud(AmazonCloudSearchQuery query) {

		if(logger.isDebugEnabled())	logger.debug("Inside AmazonCloudSearchResponse");
		AmazonCloudSearchResponse result = null;
		String responseBody = null;
		Response response;
		try 
		{
			response = Request.Get("https://" + getSearchEndpoint() + "/2013-01-01/search?" + query.build())
					.useExpectContinue()
					.version(HttpVersion.HTTP_1_1)
					.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
					.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType())
					.execute();

			HttpResponse resp = response.returnResponse();
			responseBody = inputStreamToString(resp.getEntity().getContent());
			JSONObject json = new JSONObject(responseBody);		// convert it to JSON object
			responseBody = json.toString(4); 					// format the json response

			int statusCode = 0;
			if(resp != null)
			{
				statusCode = resp.getStatusLine().getStatusCode();
			}

			if(statusCode >= 400 && statusCode < 500) {
				logger.error("Amazon cloud search request exception" +responseBody);
				throw new AmazonServiceException(responseBody);
			} else if(statusCode >= 500 && statusCode < 600){
				logger.error("Internal Server Error. Please try again as this might be a transient error condition.");
				throw new AmazonServiceException("Internal Server Error. Please try again as this might be a transient error condition.");
			}
			result = convertFromJSON(responseBody);
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException in searchCloud" + e);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException in searchCloud" + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException in searchCloud" + e);
			e.printStackTrace();
		} catch (JSONException e) {
			logger.error("JSONException in searchCloud" + e);
			e.printStackTrace();
		}  catch (AmazonServiceException eAWS) {
			logger.error("AmazonServiceException in searchCloud" +eAWS);
		}
		if(logger.isDebugEnabled())	logger.debug("Returning from AmazonCloudSearchResponse with AmazonCloudSearchResponse" +result);
		return result;
	}


	// Convert inputstream to string 
	private String inputStreamToString(InputStream in) throws IOException {

		if(logger.isDebugEnabled())	logger.debug("Inside inputStreamToString");
		StringWriter output = new StringWriter();
		InputStreamReader input = new InputStreamReader(in);
		char[] buffer = new char[1024 * 4];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toString();
	}


	//#Read Amazon cloud search json response and populate pojos
	private AmazonCloudSearchResponse convertFromJSON(String responseBody) throws JSONException {

		if(logger.isDebugEnabled())	logger.debug("Inside convertFromJSON");

		AmazonCloudSearchResponse CloudSearchResponse = new AmazonCloudSearchResponse();

		JSONObject root = new JSONObject(responseBody);
		JSONObject status = root.getJSONObject("status");
		if(status != null) {
			CloudSearchResponse.setTime(status.getLong("time-ms"));
		}

		JSONObject results = root.getJSONObject("hits");
		if(results != null) {

			int resultFound = results.getInt("found");
			CloudSearchResponse.setFound(resultFound);
			CloudSearchResponse.setStart(results.getInt("start"));

			if(resultFound > 0) {
				JSONArray resultArray = results.getJSONArray("hit");
				if(resultArray != null) {
					for(int i = 0; i < resultArray.length(); i++) {

						JSONObject row = resultArray.getJSONObject(i);
						AmazonSearchResult result = new AmazonSearchResult();

						JSONObject fields = row.getJSONObject("fields");
						String[] names = JSONObject.getNames(fields);

						HashMap<String, String> retfields = null;
						for(String name : names) {

							if(retfields == null) {
								retfields = new HashMap<String, String>();
							}

							result.fields.put(name, fields.getString(name));
						}
						if(CloudSearchResponse.results == null) {
							CloudSearchResponse.results = new ArrayList<AmazonSearchResult>();
						}
						CloudSearchResponse.results.add(result);
					}
				}
			}
		}
		return CloudSearchResponse;
	}
}
