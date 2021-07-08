package com.oup.eac.admin.components;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import com.oup.eac.admin.components.BreadCrumbHistory.Page;

public class BreadCrumbHistoryTest {

	@Test
	public void shouldRemoveEntriesAfterSpecificUrl() {
		BreadCrumbHistory history = new BreadCrumbHistory(Arrays.asList(
				new Page("first page", "key1"),
				new Page("second page", "key2"),
				new Page("third page", "key3"),
				new Page("fourth page", "key4"),
				new Page("fifth page", "key5"),
				new Page("sixth page", "key6")
				));
		history.add("first page", "x=y");
		history.add("second page", "x=y");
		history.add("third page", "x=y");
		history.add("fourth page", "x=y");
		history.add("fifth page", "x=y");
		history.add("sixth page", "x=y");
		
		history.removeUpTo("third page");
		
		assertThat(history.size(), equalTo(3));
		Iterator<Page> iter = history.getEntries();
		Page entry = iter.next();
		assertThat(entry.getUrl(), equalTo("first page?x=y"));
		assertThat(entry.getKey(), equalTo("key1"));
		assertThat(iter.next().getUrl(), equalTo("second page?x=y"));
		assertThat(iter.next().getUrl(), equalTo("third page?x=y"));
	}
	
	@Test
	public void shouldRemoveEntriesAfterUrlPartialMatch() {
		BreadCrumbHistory history = new BreadCrumbHistory(Arrays.asList(
				new Page("first page", "key1"),
				new Page("second page", "key2"),
				new Page("third page", "key3"),
				new Page("fourth page", "key4"),
				new Page("fifth page", "key5"),
				new Page("sixth page", "key6")
				));
		history.add("first page", "x=y");
		history.add("second page", "x=y");
		history.add("third page", "x=y");
		history.add("fourth page", "x=y");
		history.add("fifth page", "x=y");
		history.add("sixth page", "x=y");
		
		history.removeUpTo("third");
		
		assertThat(history.size(), equalTo(3));
		Iterator<Page> iter = history.getEntries();
		Page entry = iter.next();
		assertThat(entry.getUrl(), equalTo("first page?x=y"));
		assertThat(entry.getKey(), equalTo("key1"));
		assertThat(iter.next().getUrl(), equalTo("second page?x=y"));
		assertThat(iter.next().getUrl(), equalTo("third page?x=y"));
	}
}
