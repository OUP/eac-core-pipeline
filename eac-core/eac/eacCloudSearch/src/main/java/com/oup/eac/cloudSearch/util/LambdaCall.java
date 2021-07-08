package com.oup.eac.cloudSearch.util;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.invoke.LambdaFunction;



public interface LambdaCall {

    /**createOupMapping.
     * @param input
     * @return Map<String, String>
     */
    @LambdaFunction(functionName = "CREATE_OUP_ID_ERIGHTS_ID_MAPPING")
    Map<String, String> createOupMapping(JSONObject input);
    
    /**getErightsIdFromOupId.
     * @param input
     * @return JSONObject
     */
    @LambdaFunction(functionName = "GET_ERIGHTS_ID_FROM_OUP_ID")
    JSONObject getErightsIdFromOupId(JSONObject input);
    
    /**getoupIdFromErightsId.
     * @param input
     * @return Map<String, String>
     */
    @LambdaFunction(functionName = "GET_OUP_ID_FROM_ERIGHTS_ID")
    Map<String, String> getoupIdFromErightsId(JSONObject input);

    
    /**getoupIds.
     * @param input
     * @return List<String>
     */
    @LambdaFunction(functionName = "GENERATE_OUP_ID")
    List<String> getoupIds(JSONObject input);
    
    /**removeoupIds.
     * @param input
     * @return String
     */
    @LambdaFunction(functionName = "REMOVE_OUP_ID")
    String removeoupIds(JSONObject input);
    
    /**lambdaCallForEmailIntegration.
     * @param input
     * @return String
     */
    @LambdaFunction(functionName = "Email_Integration")
    String lambdaCallForEmailIntegration(JSONObject input);
}
