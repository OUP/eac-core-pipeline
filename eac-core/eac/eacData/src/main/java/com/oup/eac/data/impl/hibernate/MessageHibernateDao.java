package com.oup.eac.data.impl.hibernate;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.MessageDao;
import com.oup.eac.domain.Message;

@Repository("messageDao")
public class MessageHibernateDao extends HibernateBaseDao<Message, String> implements MessageDao {

	@Autowired
    public MessageHibernateDao(final SessionFactory sf) {
        super(sf);
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getMessagesByLocaleOrderedByKey(final Locale locale) {
		Query query = createQueryForGetMessagesByLocale(locale);
		setParametersForGetMessagesByLocale(locale, query);
		return query.list();
	}

	private Query createQueryForGetMessagesByLocale(final Locale locale) {
		StringBuilder builder = new StringBuilder("select m from Message m where 1=1 ");
		
		if (StringUtils.isNotBlank(locale.getLanguage())) {
			builder.append("and m.language = :language ");
		} else {
			builder.append("and m.language is null ");
		}
		
		if (StringUtils.isNotBlank(locale.getCountry())) {
			builder.append("and m.country = :country ");
		} else {
			builder.append("and m.country is null ");
		}
		
		if (StringUtils.isNotBlank(locale.getVariant())) {
			builder.append("and m.variant = :variant ");
		} else {
			builder.append("and m.variant is null ");
		}
		
		builder.append("order by m.key");
		
		return getSession().createQuery(builder.toString());
	}
	
	private void setParametersForGetMessagesByLocale(final Locale locale, final Query query) {
		if (StringUtils.isNotBlank(locale.getLanguage())) {
			query.setParameter("language", locale.getLanguage());
		}
		
		if (StringUtils.isNotBlank(locale.getCountry())) {
			query.setParameter("country", locale.getCountry());
		}
		
		if (StringUtils.isNotBlank(locale.getVariant())) {
			query.setParameter("variant", locale.getVariant());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> getDefaultMessagesOrderedByKey() {
		return getSession().createQuery("select m from Message m " +
				"where m.language is null " +
				"and m.country is null " +
				"and m.variant is null " +
				"order by m.key")
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> findMessagesWithKeyOrTextContaining(final String text, final int maxLimit) {
		return getSession().createQuery("select m from Message m " +
				"where (lower(m.key) like :text " +
				"or lower(m.message) like :text) " + 
				"and m.language is null " +
				"and m.country is null " +
				"and m.variant is null " +
				"order by m.key")
				.setParameter("text", "%" + text.toLowerCase() + "%")
				.setMaxResults(maxLimit)
				.list();
	}

}
