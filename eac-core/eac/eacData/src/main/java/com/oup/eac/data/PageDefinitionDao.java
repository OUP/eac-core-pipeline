package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.PageDefinition;
import com.oup.eac.domain.Question;

/**
 * @author harlandd The user dao interface
 */
public interface PageDefinitionDao<T extends PageDefinition> extends BaseDao<T, String> {

    /**
     * 
     * @return PageDefinition the PageDefinition
     */
	T getPageDefinitionById(final String id);
	
	List<T> getAvailablePageDefinitions(final AdminUser adminUser);
	
	List<Question> getPageDefinitionQuestions(final PageDefinition pageDefinition);
}
