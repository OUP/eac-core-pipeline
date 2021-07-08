package com.oup.eac.data;

import java.sql.SQLException;
import java.util.List;

import org.joda.time.DateTime;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;

/**
 * @author harlandd Answer dao interface
 */
public interface DivisionDao extends BaseDao<Division, String> {
	
    List<Division> getDivisions();
    
    List<Division> getDivisionsByAdminUser(final AdminUser adminUser);
    
    Division getDivisionByDivisionType(final String divisionType);

    Boolean isDivisionUsed(String divisionId);
    
    Boolean isDivisionUsed(String divisionId, AdminUser administrator);

    List<String[]> getOrgUnitUsage(DateTime fromTime, DateTime toTime) throws SQLException;
}
