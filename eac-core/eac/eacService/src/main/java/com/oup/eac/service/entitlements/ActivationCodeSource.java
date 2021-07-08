package com.oup.eac.service.entitlements;

/**
 * Used to get the activation code associated with an atypon licnece id.
 * @author David Hay
 *
 */
public interface ActivationCodeSource {
    
    /**
     * Gets the activation code if there is one associated with the licence.
     *
     * @param eRightsLicenceId the atypon licence id
     * @return the activation code associated with the licenceId or null
     */
    public String getActivationCode(String eRightsLicenceId);// only for top licences associated with registerable products
}
