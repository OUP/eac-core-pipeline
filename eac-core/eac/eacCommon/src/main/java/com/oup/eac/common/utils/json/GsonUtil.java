package com.oup.eac.common.utils.json;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

class CollectionAdapter implements JsonSerializer<Collection<?>> {
	@Override
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
class DateTimeAdapter implements JsonSerializer<DateTime> {
	@Override
	public JsonElement serialize(DateTime src, Type typeOfSrc,
			JsonSerializationContext context) {
		if (src == null) {
			return null;
		}
		JsonPrimitive json = new JsonPrimitive(ISODateTimeFormat.dateTime().print(src));
		
		return json;
	}
}
public class GsonUtil {
	static Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
			Collection.class, new CollectionAdapter()).create();
	
	static Gson gsonDateFormat = new GsonBuilder().registerTypeHierarchyAdapter(
			Collection.class, new CollectionAdapter()).registerTypeAdapter(DateTime.class, new DateTimeAdapter()).create();
	

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
	 * objectToJsonWithDateFormat 
	 * @param obj
	 * @return * String 
	 * @author Developed by Cognizant
	 */
	public static String objectToJsonWithDateFormat(Object obj) {
		String json = gsonDateFormat.toJson(obj);
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

	/** 
	 * cleanJsonString 
	 * @param jsonInString
	 * @param clazz
	 * @return * String 
	 * @author Developed by TCS 
	 */
	public static String cleanJsonString(String jsonInString, Class clazz) {
		return removeEmptyAndNulls(gson.toJson(gson.fromJson(jsonInString,
				clazz)));
	}
	
	/** 
	 * removeEmptyAndNulls 
	 * @param jsonString
	 * @return * String 
	 * @author Developed by TCS 
	 */
	private static String removeEmptyAndNulls(String jsonString) {
		String json = jsonString;
		Type type = new TypeToken<Map<String, Object>>() { } .getType();
		Map<String, Object> data = new Gson().fromJson(json, type);

		for (Iterator<Map.Entry<String, Object>> it = data.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, Object> entry = it.next();
			if (entry.getValue() == null) {
				it.remove();
			} else if (entry.getValue() instanceof List) {
				if (((List<?>) entry.getValue()).isEmpty()) {
					it.remove();
				}
			} else if (entry.getValue() instanceof Map) {
				if (((Map<?, ?>) entry.getValue()).isEmpty()) {
					it.remove();
				}
			}
		}

		json = new GsonBuilder().setPrettyPrinting().create().toJson(data);
		return json;
	}
}
