package com.oup.eac.admin.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class BreadCrumbHistoryInterceptor implements HandlerInterceptor {

	private static final String BREAD_CRUMB = "breadCrumb";
	private static final String DEEP_PARAM = "deep";
	
	private List<String> ignoreParams = new ArrayList<String>();

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView)
			throws Exception {

		if (isGETRequest(request)) {
			String servletPath = request.getServletPath();
			HttpSession session = request.getSession();
			BreadCrumbHistory history = (BreadCrumbHistory) session.getAttribute(BREAD_CRUMB);

			if (history == null) {
				history = new BreadCrumbHistory();
				session.setAttribute(BREAD_CRUMB, history);
			}

			if (history.contains(servletPath)) {
				history.removeUpTo(servletPath);
			}
			
			history.add(servletPath, getQueryString(request));
		}
	}

	private boolean isGETRequest(final HttpServletRequest request) {
		String method = request.getMethod();
		return StringUtils.isNotBlank(method) && method.toLowerCase().equals("get");
	}

	private String getQueryString(final HttpServletRequest request) {
		List<RequestParameter> queryString = new ArrayList<RequestParameter>();

		for (Object param : request.getParameterMap().keySet()) {
			String paramVal = request.getParameter((String) param);
			addParamToQueryString((String) param, paramVal, queryString);
		}

		addParamToQueryString(DEEP_PARAM, "1", queryString);

		return StringUtils.join(queryString, "&");
	}

	private void addParamToQueryString(final String param, final String value, final List<RequestParameter> queryString) {
		RequestParameter requestParameter = new RequestParameter(param, value);
		if (!queryString.contains(requestParameter) && !ignoreParams.contains(param)) {
			queryString.add(requestParameter);
		}
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex)
			throws Exception {
	}

	private class RequestParameter {
		private final String name;
		private final String value;

		public RequestParameter(final String name, final String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {
			return name + "=" + value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RequestParameter other = (RequestParameter) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		private BreadCrumbHistoryInterceptor getOuterType() {
			return BreadCrumbHistoryInterceptor.this;
		}

	}

	public void setIgnoreParams(final List<String> ignoreParams) {
		this.ignoreParams = ignoreParams;
	}
	
}
