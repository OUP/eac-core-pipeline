package com.oup.eac.data;

import java.util.List;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.DivisionAdminUser;

/**
 * @author harlandd Answer dao interface
 */
public interface DivisionAdminUserDao extends BaseDao<DivisionAdminUser, String> {

	List<DivisionAdminUser> getDivisionAdminUserByDivision(final Division division);
	
	DivisionAdminUser getDivisionAdminUserByDivisionAndAdmin(final Division division, final AdminUser adminUser);
	List<DivisionAdminUser> getDivisionAdminUserByAdmin(final AdminUser adminUser);
}
