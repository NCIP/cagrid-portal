/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ScrollCommand {

	private ScrollOperation scrollOp;
	
	/**
	 * 
	 */
	public ScrollCommand() {

	}

	public ScrollOperation getScrollOp() {
		return scrollOp;
	}

	public void setScrollOp(ScrollOperation scrollOp) {
		this.scrollOp = scrollOp;
	}

}
