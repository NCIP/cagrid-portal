package gov.nih.nci.cagrid.portal.dao;


import gov.nih.nci.cagrid.portal.domain.IndexService;

/**
 * @version 1.0
 * @created 22-Jun-2006 6:56:32 PM
 */
public interface IndexDAO {

    /**
     * @param epr
     */
    public void deleteByEPR(String epr);


    public IndexService getObjectByPK(Integer pk);

    /**
     * Return ID for a EPR string
     *
     * @param epr
     * @return
     */
    public int getID4EPR(String epr);


}