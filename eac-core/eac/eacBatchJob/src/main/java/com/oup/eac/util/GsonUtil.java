package com.oup.eac.util;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class CollectionAdapter implements JsonSerializer<Collection<?>> {
	public JsonElement serialize(Collection<?> src, Type typeOfSrc,
			JsonSerializationContext context) {
		if (src == null || src.isEmpty()) {
			return null;
		}

		JsonArray array = new JsonArray();

		for (Object child : src) {
			JsonElement element = context.serialize(child);
			array.add(element);
		}

		return array;
	}
}

public class GsonUtil {
	static Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
			Collection.class, new CollectionAdapter()).create();

	// static Gson gsonBuilder = new GsonBuilder().create();
	/** 
	 * jsonToString 
	 * @param jsonInString
	 * @param fieldName
	 * @return * String 
	 * @author Developed by TCS 
	 */
	public static String jsonToString(String jsonInString, String fieldName) {
		JsonObject jsonObject = gson.fromJson(jsonInString, JsonObject.class);
		return jsonObject.get(fieldName).toString();
	}

	/** 
	 * jsonToInteger 
	 * @param jsonInString
	 * @param fieldName
	 * @return * Integer 
	 * @author Developed by TCS 
	 */
	public static Integer jsonToInteger(String jsonInString, String fieldName) {
		JsonObject jsonObject = gson.fromJson(jsonInString, JsonObject.class);
		return jsonObject.get(fieldName).getAsInt();
	}

	/** 
	 * jsonToObject 
	 * @param jsonInString
	 * @param obj
	 * @return * Object 
	 * @author Developed by TCS 
	 */
	public static Object jsonToObject(String jsonInString, Object obj) {
		Object retObj = gson.fromJson(jsonInString, obj.getClass());
		return retObj;
	}

	/** 
	 * objectToJson 
	 * @param obj
	 * @return * String 
	 * @author Developed by TCS 
	 */
	public static String objectToJson(Object obj) {
		String json = gson.toJson(obj);
		return json;
	}

	/** 
	 * stringToJsonObject 
	 * @param stringToParse
	 * @return * String 
	 * @author Developed by TCS 
	 */
	public static String stringToJsonObject(String stringToParse) {
		JsonParser parser = new JsonParser();
		JsonObject json = (JsonObject) parser.parse(stringToParse);
		return json.getAsString();
	}

	/** 
	 * prettyJsonString 
	 * @param jsonString
	 * @return * String 
	 * @author Developed by TCS 
	 */
	public static String prettyJsonString(String jsonString) {
		if (jsonString == null || jsonString.trim().isEmpty()) {
			return "";
		}
		
		if(jsonString.contains("password")) {
		
			int indexStart = jsonString.indexOf("password") + 11;
			int indexEnd = jsonString.indexOf(",", indexStart)-1;
			String subString = jsonString.substring(indexStart, indexEnd);
			String tempPass = "**********";
		
			jsonString = jsonString.replace(subString, tempPass);
		 }
		return new GsonBuilder().setPrettyPrinting().create()
				.toJson(new JsonParser().parse(jsonString));
	}

}
