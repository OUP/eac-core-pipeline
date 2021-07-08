package com.oup.eac.web.tags;

import javax.servlet.jsp.PageContext;

public class TagIdGenerator {

	/**
	 * The prefix for all {@link PageContext} attributes created by this tag.
	 */
	private static final String PAGE_CONTEXT_ATTRIBUTE_PREFIX = TagIdGenerator.class.getName() + ".";

	/**
	 * Get the next unique ID (within the given {@link PageContext}) for the supplied name.
	 */
	public static String nextId(String name, PageContext pageContext) {
		String attributeName = PAGE_CONTEXT_ATTRIBUTE_PREFIX + name;
		Integer currentCount = (Integer) pageContext.getAttribute(attributeName);
		currentCount = (currentCount != null ? currentCount + 1 : 1);
		pageContext.setAttribute(attributeName, currentCount);
		return (name + currentCount);
	}

}
