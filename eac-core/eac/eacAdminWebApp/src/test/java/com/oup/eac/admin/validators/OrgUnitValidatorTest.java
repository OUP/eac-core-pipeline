package com.oup.eac.admin.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.oup.eac.admin.beans.OrgUnitsBean;
import com.oup.eac.domain.Division;

public class OrgUnitValidatorTest {

    private OrgUnitValidator validator;

    @Before
    public void setup() {
        validator = new OrgUnitValidator();
    }

    @Test
    public void supportsTest() {
        Assert.assertTrue(validator.supports(OrgUnitsBean.class));
    }

    @Test
    public void regexTest() {
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit("A"));
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit("Z"));
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit("AA"));
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit("ZZ"));
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit("__A__"));
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit("A_Z"));
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit("-"));
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit(")"));
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit("("));
        Assert.assertTrue(OrgUnitValidator.isValidOrgUnit("Aa_01231231231(-Za)"));

        Assert.assertFalse(OrgUnitValidator.isValidOrgUnit("$"));
        Assert.assertFalse(OrgUnitValidator.isValidOrgUnit("£"));
        Assert.assertFalse(OrgUnitValidator.isValidOrgUnit("'"));
        Assert.assertFalse(OrgUnitValidator.isValidOrgUnit("="));
        Assert.assertFalse(OrgUnitValidator.isValidOrgUnit("/"));
        Assert.assertFalse(OrgUnitValidator.isValidOrgUnit("+"));

    }

    @Test
    public void testBlank() {
        Errors errors1 = validateOrgUnits(Arrays.asList(" "),null);
        Assert.assertTrue(errors1.hasErrors());
        
        Errors errors2 = validateOrgUnits(null,Arrays.asList(" "));
        Assert.assertTrue(errors2.hasErrors());
        
        Errors errors3 = validateOrgUnits(Arrays.asList(" "),Arrays.asList(" "));
        Assert.assertTrue(errors3.hasErrors());

    }
    
    

    @Test
    public void testDuplicate() {
        Errors errors1 = validateOrgUnits(Arrays.asList("BOB", "BOB"), null);
        Assert.assertTrue(errors1.hasErrors());
        
        Errors errors2 = validateOrgUnits(null, Arrays.asList("BOB", "BOB"));
        Assert.assertTrue(errors2.hasErrors());
        
        Errors errors3 = validateOrgUnits(Arrays.asList("BOB"), Arrays.asList("BOB"));
        Assert.assertTrue(errors3.hasErrors());

    }
    

    @Test
    public void testInvalid() {
        Errors errors1 = validateOrgUnits(Arrays.asList("bob£"), null);
        Assert.assertTrue(errors1.hasErrors());
        
        Errors errors2 = validateOrgUnits(null, Arrays.asList("bob£"));
        Assert.assertTrue(errors2.hasErrors());
        
        
        Errors errors3 = validateOrgUnits(Arrays.asList("bob£"), Arrays.asList("bob£"));
        Assert.assertTrue(errors3.hasErrors());

    }
    
    @Test
    public void testValid() {
        
        Errors errors1 = validateOrgUnits(Arrays.asList("ONE"), null);
        Assert.assertFalse(errors1.hasErrors());
        
        Errors errors2 = validateOrgUnits(null, Arrays.asList("TWO"));
        Assert.assertFalse(errors2.hasErrors());
        
        
        Errors errors3 = validateOrgUnits(Arrays.asList("THREE"), Arrays.asList("FOUR"));
        Assert.assertFalse(errors3.hasErrors());
        

        Errors errors4 = validateOrgUnits(Arrays.asList("GOODONE","GOODTWO"), Arrays.asList("FOUR"));
        Assert.assertFalse(errors4.hasErrors());
        
        Errors errors5 = validateOrgUnits(Arrays.asList("GOODONE","oops$","GOODTWO"), Arrays.asList("FOUR"));
        Assert.assertTrue(errors5.hasErrors());

        Errors errors6 = validateOrgUnits(Arrays.asList("GOODONE","oops$","GOODTWO"), Arrays.asList("FOUR"), Arrays.asList("1"));
        Assert.assertFalse(errors6.hasErrors());
    }


    private List<Division> getDivisions(List<String> orgUnits) {
        List<Division> divisions = new ArrayList<Division>();
        if (orgUnits != null) {
            for (String orgUnit : orgUnits) {
                Division div = new Division();
                div.setDivisionType(orgUnit);
                divisions.add(div);
            }
        }
        return divisions;
    }
    private Errors validateOrgUnits(List<String> orgUnits, List<String> newOrgUnits){
        return validateOrgUnits(orgUnits, newOrgUnits, new ArrayList<String>());
    }
    
    private Errors validateOrgUnits(List<String> orgUnits, List<String> newOrgUnits, List<String> orgUnitIndexesToDelete) {
        
        List<Division> divisions = getDivisions(orgUnits);
        List<Division> newDivisions = getDivisions(newOrgUnits);        
        OrgUnitsBean orgUnitBean = new OrgUnitsBean(divisions);
        orgUnitBean.setNewOrgUnits(newDivisions);
        orgUnitBean.setOrgUnitIndexesToRemove(orgUnitIndexesToDelete);
        Errors errors = new BindException(orgUnitBean, "orgUnitBean");
        validator.validate(orgUnitBean, errors);
        return errors;
    }
}
