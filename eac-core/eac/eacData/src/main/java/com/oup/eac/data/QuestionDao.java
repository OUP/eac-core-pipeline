package com.oup.eac.data;

import com.oup.eac.domain.Question;

/**
 * Element DAO interface
 */
public interface QuestionDao extends BaseDao<Question, String> {

	public boolean isExportNameInUse(final String exportName, final String... ignoreIds);
}
