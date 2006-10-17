package gov.nih.nci.cagrid.portal.db;

import gov.nih.nci.cagrid.portal.exception.PortalInitializationException;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;

/**
 * Interfact to be implemented by DB specific classes.
 * This ensurtes Portal DB is created and populated with
 * needed seed data
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 16, 2006
 * Time: 12:06:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PortalDDLExecutor {

    /**
     * Runs portal default DDL
     *
     * @throws PortalInitializationException
     */
    public void executePortalDDL() throws PortalInitializationException;


    /**
     * Populates Geocodes for ZIP codes
     *
     * @throws gov.nih.nci.cagrid.portal.exception.PortalRuntimeException
     *
     */
    public void executePopulateDBWithZipCodes() throws PortalRuntimeException;

    /**
     * Populate DB with caBIG workspace specific data
     *
     * @throws PortalRuntimeException
     */
    public void executePopulateDBWithWorkspaces() throws PortalRuntimeException;

    /**
     * Populate DB with caBIG participants. Subclass can set how to receive/parse Participant
     * data
     *
     * @throws PortalInitializationException
     */
    public void executePopulateDBWithParticipants() throws PortalInitializationException;

}
