package com.oup.eac.data;

import java.util.List;
import java.util.Locale;

import com.oup.eac.domain.Message;

public interface MessageDao extends BaseDao<Message, String> {
	
	List<Message> getDefaultMessagesOrderedByKey();

	List<Message> getMessagesByLocaleOrderedByKey(final Locale locale);

	List<Message> findMessagesWithKeyOrTextContaining(final String text, final int maxLimit);

	
}
