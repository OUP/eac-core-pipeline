package com.oup.eac.cloudSearch.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.json.GsonUtil;

public class OupIdMappingUtil {
	public static enum Entity {
		GROUP,
		USER,
		LICENSE,
		CLAIM_DIRECT_LICENSE,
		PRODUCT,
		PRODUCT_GROUP

	}
	
	/*OUPID dynamodb tablename*/
	public static final String TABLE_NAME = EACSettings.getProperty("aws.dynamodb.oupid.tablename");
	
	/*OUPID lambda functions*/
	public static final String GET_OUP_ID_FROM_ERIGHTS_ID = EACSettings.getProperty("aws.lambda.oupid.getOupIdFromErightsId");
	public static final String GET_ERIGHTS_ID_FROM_OUP_ID = EACSettings.getProperty("aws.lambda.oupid.getErightsIdFromOupId");

	/**getcredentialsForLambda.
	 * @return AWSLambdaClient
	 */
	public static AWSLambdaClient getAWSCredentials() {
		final String accessKey = EACSettings.getProperty("aws.lambda.oupid.accesskey");
		final String secretAccessKey = EACSettings.getProperty("aws.lambda.oupid.secretkey");
		final Regions region = Regions.valueOf(EACSettings.getProperty("aws.region"));
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
	/*    
	 *//**getOupIdsUsingLambda.
	 * @param numberOfIds
	 * @param awsLambda
	 * @return List<String>
	 * @
	 *//*
	public static List<String> generateOupIds(final int numberOfIds, AWSLambdaClient awsLambda) {
        List<String> oupIds = new ArrayList<String>();
        if (awsLambda == null) {
        awsLambda = getAWSCredentials();
        }
        final LambdaCall serviceCall = LambdaInvokerFactory.build(LambdaCall.class, awsLambda);
        JSONObject json = new JSONObject();
        json.put("SIZE", numberOfIds);
        oupIds = serviceCall.getoupIds(json);
        for (int i = 0; i < oupIds.size(); i++) {
            LOGGER.info("OUP_ID: " + oupIds.get(i));    
        }
        return oupIds;
    }



	  *//**createOupIdandMapUsingLambda.
	  * @param erightsId
	  * @param entity
	  * @param dateCreated
	  * @param createdBy
	  * @param LOGGER
	  * @return String
	  * @
	  *//*
	public static String generateOupIdAndMapErightsId(final String erightsId, 
		final String entity, final String dateCreated, 
        final String createdBy) {
    List<String> idsList = new ArrayList<String>();
    idsList.add(erightsId);
    Map<String, String> oupidMap = new HashMap<String, String>();
    oupidMap = generateOupIdsAndMapErightsIds(idsList, entity, dateCreated, createdBy);
    String responseObj = null;
    if (oupidMap.containsKey(erightsId)) {
        responseObj = oupidMap.get(erightsId);
    }
    return responseObj;

     }


	   *//**generateOupIdsAndMapErightsIds.
	   * @param erightsIdsList
	   * @param entity
	   * @param dateCreated
	   * @param createdBy
	   * @return Map<String, String>
	   * @
	   *//*
	public static Map<String, String> generateOupIdsAndMapErightsIds(final List<String> erightsIdsList, final String entity,
	        final String dateCreated, final String createdBy) {
	    Map<String, String> oupidsMap = new HashMap<String, String>();

	    //String[] ids = erightsIdsList.toArray(new String[erightsIdsList.size()]);
	    List<String> oupids = generateOupIds(erightsIdsList.size(), null);
	    AWSLambdaClient awsLambda = getAWSCredentials();
	    final LambdaCall serviceCall = LambdaInvokerFactory.build(LambdaCall.class, awsLambda);
	    JSONObject json = new JSONObject();
	    json.put("ERIGHTS_IDS", erightsIdsList);
	    json.put("OUP_IDS", oupids);
	    json.put("ENTITY", entity); 
	    json.put("DATE_CREATED", dateCreated);
	    json.put("CREATED_BY", createdBy);
	    String nullStr = "NULL";
	    json.put("DATE_UPDATED", nullStr);
	    json.put("DATE_DELETED", nullStr); 
	    json.put("UPDATED_BY", nullStr); 
	    json.put("DELETED_BY", nullStr);
	    json.put("TABLE_NAME", tableName);
	    //LOGGER.info("jsonInput :" + json.toString());
	    oupidsMap = serviceCall.createOupMapping(json);

	    return oupidsMap;

     }

	    *//**removeOupIds.
	    * @param oupIds
	    * @param entity
	    * @param datedeleted
	    * @param deletedby
	    * @
	    *//*
    public static void removeOupIds(final List<String> oupIds, final String entity,
        final String datedeleted, final String deletedby) {
    	AWSLambdaClient awsLambda = getAWSCredentials();
        final LambdaCall serviceCall = LambdaInvokerFactory.build(LambdaCall.class, awsLambda);
        //String[] oupidArray = oupIds.toArray(new String[oupIds.size()]);
        JSONObject inputJson = new JSONObject();
        inputJson.put("OUP_IDS", oupIds);
        inputJson.put("DATE_DELETED", datedeleted);
        inputJson.put("DELETED_BY", deletedby);
        inputJson.put("TABLE_NAME", tableName);
        String response = serviceCall.removeoupIds(inputJson);

     }

	     *//**removeOupId.
	     * @param oupId
	     * @param entity
	     * @param datedeleted
	     * @param deletedby
	     *//*
    public static void removeOupId(final String oupId, final String entity,
            final String datedeleted, final String deletedby) {
        List<String> oupIds = new  ArrayList<String>();
        oupIds.add(oupId);
        removeOupIds(oupIds, entity, datedeleted, deletedby);
     }


	      *//**mapErightsIdswithEacIds.
	      * @param erightsIdsList
	      * @param eacidsList
	      * @param entity
	      * @param dateCreated
	      * @param createdBy
	      * @param dateDeleted
	      * @param deletedBy
	      * @return Map<String, String>
	      *//*
    public static Map<String, String> mapErightsIdswithEacIds(final List<String> erightsIdsList,final List<String> eacidsList, final String entity,
            final String dateCreated, final String createdBy, final String dateDeleted, final String deletedBy) {
        Map<String, String> oupidsMap = new HashMap<String, String>();
        AWSLambdaClient awsLambda = getAWSCredentials();
        final LambdaCall serviceCall = LambdaInvokerFactory.build(LambdaCall.class, awsLambda);
        JSONObject json = new JSONObject();
        json.put("ERIGHTS_IDS", erightsIdsList);
        json.put("OUP_IDS", eacidsList);
        json.put("ENTITY", entity); 
        json.put("DATE_CREATED", dateCreated);
        json.put("CREATED_BY", createdBy);
        json.put("DATE_DELETED", dateDeleted); 
        json.put("DELETED_BY", deletedBy); 
        json.put("DATE_UPDATED", "NULL");
        json.put("UPDATED_BY", "NULL"); 
        json.put("TABLE_NAME", tableName);
        //LOGGER.info("jsonInput :" + json.toString());
        oupidsMap = serviceCall.createOupMapping(json);

        return oupidsMap;

     }

	       *//**mapErightsIdwithEacId.
	       * @param erightsId
	       * @param eacid
	       * @param entity
	       * @param dateCreated
	       * @param createdBy
	       * @param dateDeleted
	       * @param deletedBy
	       * @return String
	       *//*
    public static String mapErightsIdwithEacId(final String erightsId,final String eacid, final String entity,
            final String dateCreated, final String createdBy, final String dateDeleted, final String deletedBy) {
        List<String> idsList = new ArrayList<String>();
        idsList.add(erightsId);
        List<String> eacidsList = new ArrayList<String>();
        eacidsList.add(eacid);
        Map<String, String> oupidMap = new HashMap<String, String>();
        oupidMap = mapErightsIdswithEacIds(idsList,eacidsList,entity, dateCreated, createdBy,dateDeleted,deletedBy);
        String responseObj = null;
        if (oupidMap.containsKey(erightsId)) {
            responseObj = oupidMap.get(erightsId);
        }
        return responseObj;

    }*/

}
