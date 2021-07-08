/*package com.oup.eac.data;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.AccountValidated;
import com.oup.eac.domain.ActivationState;
import com.oup.eac.domain.ProgressBar;
import com.oup.eac.domain.ProgressBarElement;
import com.oup.eac.domain.ProgressBarElement.ElementType;
import com.oup.eac.domain.ProgressState;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationState;
import com.oup.eac.domain.RegistrationType;
import com.oup.eac.domain.TokenState;
import com.oup.eac.domain.UserState;

public class ProgressBarDaoIntegrationTest extends AbstractDBTest {

	public static final String ACCOUNT_REGISTRATION = "ACCOUNT_REGISTRATION";
	
	@Autowired
    private ProgressBarDao progressBarDao;
	
    *//**
     * Setup test by creating a progress bar
     * 
     * @throws Exception
     *             the exception
     *//*
    @Before
    public final void setUp() throws Exception {
    	ProgressBar progressBar = getSampleDataCreator().createProgressBar(ActivationState.UNKNOWN, ACCOUNT_REGISTRATION, UserState.NEW, RegistrationState.UNKNOWN, ActivationStrategy.SELF, TokenState.NA, RegistrationType.REGULAR, AccountValidated.NON_VALIDATED);
    	getSampleDataCreator().createProgressBarElement(progressBar, ElementType.CURRENT_COMPLETED_STEP, 1);
    	getSampleDataCreator().createProgressBarElement(progressBar, ElementType.INCOMPLETE_STEP, 2);
    	getSampleDataCreator().createProgressBarElement(progressBar, ElementType.INCOMPLETE_STEP, 3);
    	getSampleDataCreator().createProgressBarElement(progressBar, ElementType.INCOMPLETE_STEP, 4);
        loadAllDataSets();

    }

    *//**
     * Get a progress bar by state
     * 
     * @throws Exception
     *             the exception
     *//*
    @Test
    public final void testGetRegiterableProductByErightsId() throws Exception {
        ProgressState progressState = new ProgressState();
        progressState.setActivationState(ActivationState.UNKNOWN);
        progressState.setRegistrationType(RegistrationType.REGULAR);
        progressState.setUserState(UserState.NEW);
        progressState.setActivationStrategy(ActivationStrategy.SELF);
        progressState.setTokenState(TokenState.NA);
        progressState.setRegistrationState(RegistrationState.UNKNOWN);
        progressState.setAccountValidated(AccountValidated.NON_VALIDATED);
        
    	ProgressBar pb = progressBarDao.getProgressBar(progressState, ACCOUNT_REGISTRATION);
    	Assert.assertNotNull(pb);
    	Assert.assertEquals(4, pb.getElements().size());
    	ProgressBarElement pbe = getProgressBarElementBySequence(pb.getElements(), 1);
    	Assert.assertNotNull(pbe);
    	Assert.assertEquals(ElementType.CURRENT_COMPLETED_STEP, pbe.getElementType());
    	pbe = getProgressBarElementBySequence(pb.getElements(), 2);
    	Assert.assertNotNull(pbe);
    	Assert.assertEquals(ElementType.INCOMPLETE_STEP, pbe.getElementType());
    	pbe = getProgressBarElementBySequence(pb.getElements(), 3);
    	Assert.assertNotNull(pbe);
    	Assert.assertEquals(ElementType.INCOMPLETE_STEP, pbe.getElementType());
    	pbe = getProgressBarElementBySequence(pb.getElements(), 4);
    	Assert.assertNotNull(pbe);
    	Assert.assertEquals(ElementType.INCOMPLETE_STEP, pbe.getElementType());
    }
    
    private ProgressBarElement getProgressBarElementBySequence(Set<ProgressBarElement> elements, int seq) {
    	for(ProgressBarElement progressBarElement : elements) {
    		if(progressBarElement.getSequence() == seq) {
    			return progressBarElement;
    		}
    	}
    	return null;
    }

}
*/