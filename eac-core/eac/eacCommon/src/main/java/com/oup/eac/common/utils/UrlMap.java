package com.oup.eac.common.utils;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Map for URL string and value. Performing exact and partial matching on urls. e.g. http://www.oup.com/elt/teachers/club would match
 * http://www.oup.com/elt/teachers or http://oup.com/elt etc.
 */
public class UrlMap<T> implements Serializable {

    private static final String WWWDOT = "www.";
    private static final String SLASH = "/";
    private static final int WWWDOT_LEN = 4;

    private static final Logger LOG = Logger.getLogger(UrlMap.class);

    private final Map<String, Map<String, T>> map = new HashMap<String, Map<String, T>>();

    /**
     * Constructor.
     */
    public UrlMap() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param map
     *            url and value map
     * @throws MalformedURLException
     *             invalid url
     */
    public UrlMap(final Map<String, T> map) throws MalformedURLException {
        final Set<Entry<String, T>> entries = map.entrySet();
        for (Entry<String, T> entry : entries) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Put url and value mapping.
     * 
     * @param urlString
     *            url
     * @param value
     *            map value
     * @throws MalformedURLException
     *             invalid url
     */
    public final void put(final String urlString, final T value) throws MalformedURLException {
        if(LOG.isDebugEnabled()){
            String msg = String.format("Adding [%s]->[%s]",urlString,value);
            LOG.debug(msg);
        }
        final URL url = new URL(urlString);
        final String host = cleanUrl(url.getHost());
        final String path = cleanUrl(url.getPath());
        Map<String, T> pathMap = map.get(host);
        if (pathMap == null) {
            pathMap = new HashMap<String, T>();
        }
        pathMap.put(path, value);
        map.put(host, pathMap);
    }

    /**
     * Get value for matching url mapping.
     * 
     * @param urlString
     *            url
     * @return value
     * @throws MalformedURLException
     *             invalid url
     */
    public final T get(final String urlString) throws MalformedURLException {
        if(LOG.isDebugEnabled()){
            final String msg = String.format("Getting Skin for [%s]", urlString);
            LOG.debug(msg);
        }
        T value = null;
        final List<T> values = match(urlString, true);
        if (!values.isEmpty()) {
            value = values.get(0);
        }
        if(LOG.isDebugEnabled()){
            final String msg = String.format("The skin for [%s] is [%s]",urlString, value);
            LOG.debug(msg);
        }
        return value;
    }

    /**
     * Get all values matching url mapping.
     * 
     * @param urlString
     *            url
     * @return values
     * @throws MalformedURLException
     *             invalid url
     */
    public final Collection<T> getAll(final String urlString) throws MalformedURLException {
        if(LOG.isDebugEnabled()){
            final String msg = String.format("Getting Skin for [%s]", urlString);
            LOG.debug(msg);
        }
        final Collection<T> values = match(urlString, false);
        if(LOG.isDebugEnabled()){
            final String msg = String.format("The skin for [%s] is [%s]",urlString, values.toString());
            LOG.debug(msg);
        }
        return values;        
    }
    
    /**
     * Get collection of all values in map.
     * 
     * @return values
     */
    public Collection<T> values() {
        final Collection<T> values = new ArrayList<T>();
        final Collection<Map<String, T>> pathMaps = map.values();
        for (Map<String, T> map : pathMaps) {
            values.addAll(map.values());            
        }
        return values;
    }
    
    /**
     * Match as much of the url as possible removing parts until match found.
     * Get all matches or first match only, based on firstMatchOnly value
     * 
     * @param urlString
     * @param firstMatchOnly
     * @return list of values
     * @throws MalformedURLException
     */
    private final List<T> match(final String urlString, final boolean firstMatchOnly) throws MalformedURLException {
        final List<T> value = new ArrayList<T>();
        if (StringUtils.isNotBlank(urlString)) {
            final URL url = new URL(urlString);
            final String host = cleanUrl(url.getHost());
            String path = cleanUrl(url.getPath());

            final Map<String, T> pathMap = map.get(host);
            if (pathMap != null) {
                // match as much of the url as possible
                // removing parts until match found
                final int numParts = getNumberOfParts(path);
                for (int i = 0; i < numParts; i++) {
                    if (pathMap.get(path) != null) {
                        value.add(pathMap.get(path));
                        if (firstMatchOnly) {
                            break;
                        }
                    }
                    path = removePath(path);
                }
            }
        }
        if(LOG.isDebugEnabled()){
            final String msg = String.format("The skin for [%s] is [%s]",urlString,value);
            LOG.debug(msg);
        }
        return value;
        
    }    
    /**
     * Get number of path parts.
     * 
     * @param urlPath
     *            path
     * @return count of path parts
     */
    private int getNumberOfParts(final String urlPath) {
        final String[] parts = urlPath.split(SLASH);
        return parts.length;
    }

    /**
     * Remove last part of url path.
     * 
     * @param urlPath
     *            path
     * @return shortened path
     */
    private String removePath(final String urlPath) {
        String path = "";
        if (urlPath != null) {
            final int pos = urlPath.lastIndexOf(SLASH);
            if (pos > 0) {
                path = urlPath.substring(0, pos);
            }
        }
        return path;
    }

    /**
     * Remove www and trailing slashes.
     * 
     * @param urlString
     *            url
     * @return clean url
     */
    private String cleanUrl(final String urlString) {
        String cleanUrl = urlString;
        if (urlString.endsWith(SLASH)) {
            cleanUrl = urlString.substring(0, urlString.length() - 1);
        }
        if (urlString.startsWith(WWWDOT)) {
            cleanUrl = urlString.substring(WWWDOT_LEN);
        }
        return cleanUrl;
    }
    
    /**
     * Get size.
     * @return
     */
    public int getSize(){
        return this.map.size();
    }
}
