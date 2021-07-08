package com.oup.eac.web.validators;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.springframework.validation.Errors;

import com.oup.eac.common.mock.AbstractMockTest;

public abstract class AbstractValidatorTest extends AbstractMockTest {

	protected Errors errors;
	
	public AbstractValidatorTest() throws NamingException {
		super();
	}
	
	@Before
	public void setUp() {
		errors = EasyMock.createMock(Errors.class);
		addMock(errors);
	}
	
	
    protected Object[] eqLabels(final Object[] labels) {
        IArgumentMatcher matcher = new IArgumentMatcher() {

            @Override
            public boolean matches(Object arg) {
                if (arg instanceof Object[] == false) {
                    return false;
                }
                Object[] args = (Object[])arg;
                for(int i=0;i<args.length;i++) {
                	if(!args[i].equals(labels[i])) {
                		return false;
                	}
                }
                return true;
            }

            @Override
            public void appendTo(StringBuffer out) {
                out.append("eqLabels(");
                out.append(labels.toString());
                out.append(")");
            }
        };
        EasyMock.reportMatcher(matcher);
        return null;
    }

}
