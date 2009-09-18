/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.terms;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TermBean {
	
	private TerminologyBean terminology;
	private String uri;
	private String label;
	private String comment;

	/**
	 * 
	 */
	public TermBean(TerminologyBean terminology, String uri, String label, String comment) {
		if(terminology == null){
			throw new IllegalArgumentException("terminology must not be null");
		}
		if(uri == null){
			throw new IllegalArgumentException("uri must not be null");
		}
		if(label == null){
			throw new IllegalArgumentException("label must not be null for " + uri);
		}
		if(comment == null){
			throw new IllegalArgumentException("comment must not be null for " + uri);
		}
		this.terminology = terminology;
		this.uri = uri;
		this.label = label;
		this.comment = comment;
	}
	
	public TerminologyBean getTerminology(){
		return terminology;
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

	
	public boolean equals(TermBean t){
		boolean eq = false;
		if(t != null){
			eq = getUri().equals(t.getUri());
		}
		return eq;
	}
	
	public int hashCode(){
		return getUri().hashCode();
	}
	
	public String toString(){
		return "['" + getUri() + "', '" + getLabel() + "', '" + getComment() + "']"; 
	}

}
