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
package gov.nih.nci.cagrid.portal.portlet.terms;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class TerminologyBean {

	private String uri;
	private String label;
	private String comment;

	/**
	 * 
	 */
	public TerminologyBean(String uri, String label, String comment) {
		if (uri == null) {
			throw new IllegalArgumentException("uri must not be null");
		}
		if (label == null) {
			throw new IllegalArgumentException("label must not be null");
		}
		if (comment == null) {
			throw new IllegalArgumentException("comment must not be null");
		}
		this.uri = uri;
		this.label = label;
		this.comment = comment;
	}

	public String getUri() {
		return uri;
	}

	public String getLabel() {
		return label;
	}

	public String getComment() {
		return comment;
	}

	public boolean equals(TerminologyBean t) {
		boolean eq = false;
		if (t != null) {
			eq = getUri().equals(t.getUri());
		}
		return eq;
	}

	public int hashCode() {
		return getUri().hashCode();
	}

	public String toString() {
		return "['" + getUri() + "', '" + getLabel() + "', '" + getComment()
				+ "']";
	}

}
