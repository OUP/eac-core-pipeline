package com.oup.eac.data.impl.hibernate;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.oup.eac.data.QuestionDao;
import com.oup.eac.domain.Question;

/**
 * The Element DAO hibernate implementation.
 * 
 * @author harlandd
 * @author Ian Packard
 */
@Repository(value="questionDao")
public class QuestionHibernateDao extends HibernateBaseDao<Question, String> implements QuestionDao {

    /**
     * Constructor.
     * 
     * @param sf
     *            the session factory
     */
	@Autowired
    public QuestionHibernateDao(final SessionFactory sf) {
        super(sf);
    }

	@Override
	public boolean isExportNameInUse(final String exportName, final String... ignoreIds) {
		String[] ignoreList = {""};
		if (ignoreIds.length > 0) {
			ignoreList = ignoreIds;
		}
		Query query = getSession().createQuery("select count(*) from ExportName en where en.name = :name and en.id not in (:ignoreIds)");
		query.setParameter("name", exportName);
		query.setParameterList("ignoreIds", ignoreList);
		Long count = (Long) query.uniqueResult();
		return count > 0;
	}

	
}
