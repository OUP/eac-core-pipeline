package com.oup.eac.dto;

import java.io.Serializable;

public class EnforceableProductUrlDto implements Serializable {

	private String protocol;
	
	private String host;
	
	private String path;
	
	private String query;
	
	private String fragment;
	
	private String expression;
	
	public EnforceableProductUrlDto(final EnforceableProductUrlDto dto) {
		this(dto.protocol, dto.host, dto.path, dto.query, dto.fragment, dto.expression);
	}

	public EnforceableProductUrlDto(final String protocol, final String host, final String path,
			final String query, final String fragment, final String expression) {
		super();
		this.protocol = protocol;
		this.host = host;
		this.path = path;
		this.query = query;
		this.fragment = fragment;
		this.expression = expression;
	}
	
	public EnforceableProductUrlDto() {
		super();
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getFragment() {
		return fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
