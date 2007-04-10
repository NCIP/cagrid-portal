package gov.nih.nci.cagrid.wsenum;

import gov.nih.nci.cagrid.wsenum.utils.PersistantSDKObjectIterator;

/** 
 *  PersistantEnumIterTestCase
 *  Test case to test the persistent (complicated) enumeration iterator
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Nov 3, 2006 
 * @version $Id$ 
 */
public class PersistantEnumIterTestCase extends CompleteEnumIteratorBaseTest {
	
	public PersistantEnumIterTestCase() {
		super(PersistantSDKObjectIterator.class.getName());
	}
}
