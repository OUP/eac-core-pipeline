package com.oup.eac.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.ExportName;
import com.oup.eac.domain.Question;

public class QuestionHibernateDaoIntegrationTest extends AbstractDBTest {
	
	@Autowired
	private QuestionDao questionDao;
	
	private Question question;
	private ExportName exportName;

	@Before
	public void setUp() throws Exception {
		question = getSampleDataCreator().createQuestion();
		exportName = getSampleDataCreator().createExportName(question);
		loadAllDataSets();
	}
	
	@Test
	public void shouldIndicateExportNameInUse() {
		assertThat(questionDao.isExportNameInUse("export_name"), equalTo(true));
	}
	
	@Test
	public void shouldIndicateExportNameNotInUse() {
		assertThat(questionDao.isExportNameInUse("export_name2"), equalTo(false));
	}
	
	
	@Test
	public void shouldIndicateExportNameNotInUseWhenIgnoringId() {
		assertThat(questionDao.isExportNameInUse("export_name", exportName.getId()), equalTo(false));
	}
}
