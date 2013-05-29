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
package gov.nih.nci.cagrid.portal.portlet.tree;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TextContent {
	
	private String text;

	/**
	 * 
	 */
	public TextContent() {
		this(null);
	}

	public TextContent(String text) {
		setText(text);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
