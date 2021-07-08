package com.oup.eac.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public class OupIdUtility {

	public static enum Entity {
		GROUP,
		USER,
		LICENSE,
		CLAIM_DIRECT_LICENSE,
		PRODUCT,
		PRODUCT_GROUP

	}
	private static Logger logger = Logger.getLogger(OupIdUtility.class.getName());
	
	/*OUPID dynamodb tablename*/
	public static final String TABLE_NAME = BatchJobProperties.AWS_DYNAMODB_TABLENAME;
	
	/*OUPID lambda functions*/
	public static final String GET_OUP_ID_FROM_ERIGHTS_ID = BatchJobProperties.GET_OUP_ID_FROM_ERIGHTS_ID;
	public static final String GET_ERIGHTS_ID_FROM_OUP_ID = BatchJobProperties.GET_ERIGHTS_ID_FROM_OUP_ID;

	/**getcredentialsForLambda.
	 * @return AWSLambdaClient
	 */
	public static AWSLambdaClient getAWSCredentials() {
		final String accessKey = BatchJobProperties.AWS_LAMBDA_ACCESSKEY;
		final String secretAccessKey = BatchJobProperties.AWS_LAMBDA_SECRETKEY;
		final Regions region = Regions.valueOf(BatchJobProperties.AWS_REGION);
		AWSCredentials credentials = new BasicAWSCredentials(accessKey,
				secretAccessKey);

		/** Proxy Configuration **/
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setMaxErrorRetry(5);
		clientConfig.setConnectionTimeout(600*1000);
		clientConfig.setSocketTimeout(600*1000);

		AWSLambdaClient awsLambda = 
				new AWSLambdaClient(credentials, clientConfig).withRegion(region);
		return awsLambda;
	}


	/**getOupIdFromErightsId.
	 * @param erightsId
	 * @param entity
	 * @return String
	 * @
	 */
	public static String getOupIdFromErightsId(final String erightsId, final String entity) {
		List<String> erightsIds = new ArrayList<String>();
		erightsIds.add(erightsId);
		String responseObj = null;
		Map<String, String> response = getOupIdsFromErightsIds(erightsIds, entity);
		if (response.containsKey(erightsId)) {
			responseObj = response.get(erightsId);
		}
		return responseObj;
	}

	/**getOupIdFromErightsId.
	 * @param erightsIds
	 * @param entity
	 * @return Map<String, String>
	 * @
	 */

	public static Map<String, String> getOupIdsFromErightsIds(final List<String> erightsIds, final String entity) {
		AWSLambdaClient awsLambda = getAWSCredentials();
		Map<String, String> response = new HashMap<String, String>();

		InvokeRequest invokeRequest = new InvokeRequest();
		invokeRequest.setFunctionName(GET_OUP_ID_FROM_ERIGHTS_ID);
		JSONObject json = new JSONObject();
		json.put("ERIGHTS_IDS", erightsIds);
		json.put("ENTITY", entity);
		json.put("TABLE_NAME", TABLE_NAME);
		invokeRequest.setPayload(json.toString());
		InvokeResult result = awsLambda.invoke(invokeRequest);
		response.putAll((Map<String, String>) GsonUtil.jsonToObject(new String(result.getPayload().array()), response));
		return response;
	}

	/**getErightsIdFromOupIdWithDeletedIds.
	 * gets erightsIds even if deleted.
	 * @param oupId
	 * @return
	 * @
	 */
	public static String getErightsIdFromOupIdWithDeletedId(final String oupId, final String entity) {
		List<String> oupIdList = new ArrayList<String>();
		oupIdList.add(oupId);
		Map<String, String> response = getErightsIdsFromOupIds(oupIdList, true, entity);
		String responseObj = null;
		if (response.containsKey(oupId)) {
			responseObj = response.get(oupId);
		}
		return responseObj;
	}

	/**getErightsIdFromOupId.
	 * gets erightsIds excluding deleted erightsIds
	 * @param oupId
	 * @return String
	 * @
	 */
	public static String getErightsIdFromOupId(final String oupId, final String entity) {
		List<String> oupIdList = new ArrayList<String>();
		oupIdList.add(oupId);
		Map<String, String> response = getErightsIdsFromOupIds(oupIdList, false, entity);
		String responseObj = null;
		if (response.containsKey(oupId)) {
			responseObj = response.get(oupId);
		}
		return responseObj;
	}
	/**getErightsIdFromOupId.
	 * if flag is true gets all erightsIds
	 * else excludes deleted values.
	 * @param oupIdList
	 * @param flag
	 * @return Map<String , String>
	 * @
	 */
	public static Map<String , String> getErightsIdsFromOupIds(final List<String> oupIdList, final Boolean flag, final String entity) {
		Map<String, String> responseMap = new HashMap<String, String>();
		AWSLambdaClient awsLambda = getAWSCredentials();

		InvokeRequest invokeRequest = new InvokeRequest();
		invokeRequest.setFunctionName(GET_ERIGHTS_ID_FROM_OUP_ID);
		
		JSONObject json = new JSONObject();
		json.put("OUP_IDS", oupIdList);
		json.put("FLAG", flag);
		json.put("ENTITY", entity);
		json.put("TABLE_NAME", TABLE_NAME);
		
		invokeRequest.setPayload(json.toString());
		InvokeResult result = awsLambda.invoke(invokeRequest);
		JSONObject jsonRes = new JSONObject();
		jsonRes =  (JSONObject) GsonUtil.jsonToObject(new String(result.getPayload().array()), jsonRes);

		for (int i = 0; i < oupIdList.size(); i++) {
			responseMap.put(oupIdList.get(i), (String) jsonRes.get(oupIdList.get(i)));
		}
		return responseMap;
	}

}
