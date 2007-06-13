package gov.nci.nih.cagrid.tests.core.util;

import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;


/**
 * DCQLResultsVerifier
 * 
 * @author oster
 * @created Jun 12, 2007 7:48:20 PM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
public interface DCQLResultsVerifier {
    public boolean areResultsValid(DCQLQueryResultsCollection results);

}
