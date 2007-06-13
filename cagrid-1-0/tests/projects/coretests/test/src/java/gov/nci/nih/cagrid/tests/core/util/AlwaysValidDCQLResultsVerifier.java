package gov.nci.nih.cagrid.tests.core.util;

import gov.nih.nci.cagrid.dcqlresult.DCQLQueryResultsCollection;


/**
 * AlwaysValidDCQLResultsVerifier TODO:DOCUMENT ME
 * 
 * @author oster
 * @created Jun 12, 2007 8:06:11 PM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
public class AlwaysValidDCQLResultsVerifier implements DCQLResultsVerifier {

    public boolean areResultsValid(DCQLQueryResultsCollection results) {
        return true;
    }

}