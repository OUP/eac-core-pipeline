package com.oup.eac.admin.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class BreadCrumbHistory implements Serializable {
	
	private static final PageList PAGE_LIST = new PageList(Arrays.asList(
			new Page("/mvc/page/manage.htm", 		"link.managePages"),
			new Page("/mvc/component/manage.htm", 	"link.manageComponents"),
			new Page("/mvc/element/manage.htm", 	"link.manageElements"),
			new Page("/mvc/question/manage.htm", 	"link.manageQuestions")));
	
	private PageList pageList = PAGE_LIST;
	private final List<Page> history = new ArrayList<Page>();
	
	public BreadCrumbHistory() {
	}
	
	public BreadCrumbHistory(final List<Page> pageList) {
		this.pageList = new PageList(pageList);
	}

	public void add(final String path, final String queryString) {
		if (history.size() > 0) {
			Page lastEntry = history.get(history.size() - 1);
			if (lastEntry.isIdentifiedBy(path)) {
				history.remove(history.size() - 1);
			}
		}
		
		String url = path;
		if (StringUtils.isNotBlank(queryString)) {
			url = url + "?" + queryString;
		}
		history.add(new Page(url, pageList.getKey(path)));
	}
	
	public Page getNextLevelDown() {
		Page nextLevel = null;
		if (history.size() > 0) {
			Page lastEntry = history.get(history.size() - 1);
			for (Iterator<Page> iter = pageList.iterator(); iter.hasNext();) {
				Page page = iter.next();
				if (lastEntry.getUrl().contains(page.url)) {
					if (iter.hasNext()) {
						nextLevel = iter.next();
						break;
					}
				}
			}
		}
		return nextLevel;
	}

	public void removeUpTo(final String path) {
		List<Page> reversedHistory = new ArrayList<Page>(history);
		Collections.reverse(reversedHistory);
		for (Page page : reversedHistory) {
			if (page.isIdentifiedBy(path)) {
				break;
			}
			history.remove(page);
		}
	}
	
	public boolean contains(final String path) {
		boolean contains = false;
		for (Page page : history) {
			if (page.isIdentifiedBy(path)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public Iterator<Page> getEntries() {
		return history.iterator();
	}

	public int size() {
		return history.size();
	}

	@Override
	public String toString() {
		return "BreadCrumbHistory [history=" + history + "]";
	}
	
	private static class PageList implements Iterable<Page>, Serializable {
		
		private final List<Page> pageList;
		
		public PageList(final List<Page> pageList) {
			this.pageList = pageList;
		}
		
		public String getKey(final String path) {
			String key = null;
			for (Page page : pageList) {
				if (path.contains(page.url)) {
					key = page.key;
					break;
				}
			}
			return key;
		}

		@Override
		public Iterator<Page> iterator() {
			return pageList.iterator();
		}
		
	}
	
	public static class Page implements Serializable {
		
		private final String url;
		private final String key;
		
		public Page(final String url, final String key) {
			this.url = url;
			this.key = key;
		}

		public String getUrl() {
			return url;
		}

		public String getKey() {
			return key;
		}
		
		public boolean isIdentifiedBy(final String path) {
			return url != null && url.contains(path);
		}

		@Override
		public String toString() {
			return "Page [url=" + url + ", key=" + key + "]";
		}
		
	}
}
