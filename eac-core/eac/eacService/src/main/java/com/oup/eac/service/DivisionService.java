package com.oup.eac.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.integration.facade.exceptions.DivisionNotFoundException;
import com.oup.eac.integration.facade.exceptions.ErightsException;

/**
 * In release 11.0, Organisation Unit became a synonym for Division.
 * 
 */
public interface DivisionService {

	/**
	 * Gets a {@link Division} by its id.
	 * 
	 * @param id
	 *            The id of the Division.
	 * @return The Division or null if no Division was found with the specified id.
	 */
	Division getDivisionById(final String id);
	
	/**
	 * Returns a {@link List} of all {@link Division}s in the system.
	 * 
	 * @return The List of Divisions.
	 */
	List<Division> getAllDivisions() throws ErightsException, DivisionNotFoundException, AccessDeniedException;

	/**
	 * Returns a {@link List} of {@link Division}s that the supplied {@link AdminUser} has access to. The list of
	 * Divisions are in alphabetical order of {@link String}.
	 * 
	 * @param adminUser
	 *            The AdminUser to get the list of Divisions for.
	 * @return The list of Divisions or an empty list if the supplied AdminUser does not have access to any Divisions.
	 * @throws AccessDeniedException 
	 * @throws DivisionNotFoundException 
	 * @throws ErightsException 
	 */
	List<Division> getDivisionsByAdminUser(final AdminUser adminUser) throws ErightsException, DivisionNotFoundException, AccessDeniedException;

    /**
     * Checks if  Division(Org unit) is used.
     *
     * @param divisionId the database id of the division ( org unit ) 
     * @return the boolean
     * @throws ServiceLayerException 
     */
    Boolean isDivisionUsed(String divisionId) throws ServiceLayerException;
    
    /**
     * Update divisions.
     *
     * @param toDelete the orgUnits to delete
     * @param toUpdate the orgUnits to update
     * @param toAdd the orgUnits to add
     * @return 
     * @throws ServiceLayerException the service layer exception
     * @throws ErightsException 
     */
    boolean updateDivisions(List<Division> toDelete, List<Division> toUpdate, List<Division> toAdd) throws ServiceLayerException, ErightsException;

	Set<Integer> getDivisionsUsedInProduct();

	void setDivisionsUsedInProduct(Set<Integer> divisionsUsedInProduct);
}
