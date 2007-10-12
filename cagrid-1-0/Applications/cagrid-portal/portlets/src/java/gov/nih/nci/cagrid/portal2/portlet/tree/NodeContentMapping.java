/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tree;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface NodeContentMapping {
	
	boolean matches(Object nodeContent);
	String getServletUrl();
	String getRequestAttributeName();

}
