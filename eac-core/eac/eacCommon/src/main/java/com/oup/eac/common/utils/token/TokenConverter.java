package com.oup.eac.common.utils.token;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oup.eac.common.utils.crypto.SimpleCipher;

/**
 * Utility class to convert a bean to and from an encrypted token string.
 * 
 */
public final class TokenConverter {
    private static final String EQUALS = "=";
    private static final String AMPASAND = "&";
    private static final String UTF_8 = "UTF-8";

    private static final Logger LOG = LoggerFactory.getLogger(TokenConverter.class);
    
    /**
     * Hidden construction.
     */
    private TokenConverter() {

    }

    /**
     * Encrypt.
     * 
     * @param fromBean
     *            bean to extract values from
     * @return encrypted string containing bean value
     * @throws Exception
     *             exception encrypting bean values
     */
    public static String encrypt(final Object fromBean) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (fromBean != null) {
            Field[] fromBeanFields = fromBean.getClass().getDeclaredFields();
            AccessibleObject.setAccessible(fromBeanFields, true);
            // loop through all fields in fromBean
            for (Field fromField : fromBeanFields) {
                // cant set transients or statics
                if (fromField != null && !Modifier.isTransient(fromField.getModifiers()) && !Modifier.isStatic(fromField.getModifiers())) {
                    fromField.setAccessible(true);
                    sb.append(AMPASAND);
                    sb.append(fromField.getName());
                    sb.append(EQUALS);
                    Object fieldValue = fromField.get(fromBean);
                    if (null != fieldValue) {
                        if (fieldValue instanceof String) {
                            fieldValue = URLEncoder.encode((String) fieldValue, UTF_8);
                        }
                        sb.append(fieldValue);
                    }
                }
            }
        }
        LOG.debug("Encrypted Token: " + sb.toString());
        return SimpleCipher.encrypt(sb.toString());
    }

    /**
     * Decrypt.
     * 
     * @param encryptedString
     *            encrypted string token
     * @param toBean
     *            bean to populate with values
     * @return populated bean
     * @throws Exception
     *             exception decrypting string
     */
    public static Object decrypt(final String encryptedString, final Object toBean) throws Exception {
        LOG.debug("Decrypt Token: " + encryptedString);
        String plainText = SimpleCipher.decrypt(encryptedString);
        LOG.debug("Decrypted Token: " + plainText);
        Map<String, String> paramMap = getParamMap(plainText);

        Field[] toBeanFields = toBean.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(toBeanFields, true);
        for (Field toField : toBeanFields) {
            // cant set transients or statics
            if (toField != null && !Modifier.isTransient(toField.getModifiers()) && !Modifier.isStatic(toField.getModifiers())) {
                String strValue = paramMap.get(toField.getName());
                Class<?> clazz = toField.getType();
                if (strValue != null && clazz.equals(Boolean.class)) {
                    toField.set(toBean, Boolean.valueOf(strValue));
                } else if (strValue != null && clazz.equals(Character.class)) {
                    toField.set(toBean, Character.valueOf(strValue.charAt(0)));
                } else if (strValue != null && clazz.equals(Byte.class)) {
                    toField.set(toBean, Byte.valueOf(strValue));
                } else if (strValue != null && clazz.equals(Double.class)) {
                    toField.set(toBean, Double.valueOf(strValue));
                } else if (strValue != null && clazz.equals(Float.class)) {
                    toField.set(toBean, Float.valueOf(strValue));
                } else if (strValue != null && clazz.equals(Integer.class)) {
                    toField.set(toBean, Integer.valueOf(strValue));
                } else if (strValue != null && clazz.equals(Long.class)) {
                    toField.set(toBean, Long.valueOf(strValue));
                } else if (strValue != null && clazz.equals(Short.class)) {
                    toField.set(toBean, Short.valueOf(strValue));
                } else if (strValue != null && clazz.equals(int.class)) {
                    toField.set(toBean, Integer.valueOf(strValue));
                } else {
                    toField.set(toBean, strValue);
                }
            }
        }
        return toBean;
    }

    /**
     * Convert query string to NVP map.
     * 
     * @param queryString
     *            string containing name value pairs
     * @return Map of name value pairs
     * @throws UnsupportedEncodingException
     *             problem converting query string to map
     */
    private static Map<String, String> getParamMap(final String queryString) throws UnsupportedEncodingException {
        Map<String, String> paramMap = new HashMap<String, String>();

        StringTokenizer pairTokeniser = new StringTokenizer(queryString, AMPASAND);
        while (pairTokeniser.hasMoreTokens()) {
            String pair = pairTokeniser.nextToken();
            StringTokenizer nvpTokenizer = new StringTokenizer(pair, EQUALS);
            String name = nvpTokenizer.nextToken();

            if (nvpTokenizer.hasMoreTokens()) {
                String token = nvpTokenizer.nextToken();

                String value = URLDecoder.decode(token, UTF_8);
                paramMap.put(name, value);
            }
        }
        return paramMap;
    }

}
