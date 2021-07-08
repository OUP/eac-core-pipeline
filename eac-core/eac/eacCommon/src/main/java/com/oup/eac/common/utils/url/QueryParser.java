package com.oup.eac.common.utils.url;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

import com.Ostermiller.util.CGIParser;

public class QueryParser {
	
	private static final String UTF_8 = "UTF-8";
    private static final char QUESTION_MARK = '?';
	private final String url;
	private CGIParser cgiParser;

	public QueryParser(final String url) {
		this.url = url;
		parse();
	}

	public final String getUrl() {
		try {
			if(hasParams()) {
				if(url.indexOf(QUESTION_MARK)> -1) {
					return url.substring(0, url.indexOf(QUESTION_MARK) + 1) + cgiParser.toString();
				}
				return url + QUESTION_MARK + cgiParser.toString(UTF_8); 
			}
			return url;
		} catch(UnsupportedEncodingException e) {
			throw new IllegalStateException("Encoding can not be illegal. Encoding is: " + UTF_8, e);
		}
	}
	
    public final String getParamValue(String name) {
        return cgiParser.getParameter(name);
    }
    
    public final void removeParam(String name){
        cgiParser.setParameter(name, (String) null);
    }
    
	public final boolean hasParams() {
		return cgiParser != null && cgiParser.getParameterNameList().length > 0;
	}
	
	public final void replaceParameter(final String param, final String value) {
		cgiParser.setParameter(param, value);
	}

	private final void parse() {
		try {
			if(StringUtils.isBlank(url)) {
				throw new IllegalArgumentException("The url can not be blank.");
			}
			if(url.indexOf(QUESTION_MARK) != -1) {
				this.cgiParser = new CGIParser(url.substring(url.indexOf(QUESTION_MARK) + 1), UTF_8);
			} else {
				this.cgiParser = new CGIParser("", UTF_8);
			}
		} catch(UnsupportedEncodingException e) {
			throw new IllegalStateException("Encoding can not be illegal. Encoding is: " + UTF_8, e);
		}
	}
	
	public String getBaseUrl() { 
		int idx =  url.indexOf(QUESTION_MARK);
		if (idx > -1) {
			return url.substring(0, idx);
		} else {
			return url;
		}
	}

}
