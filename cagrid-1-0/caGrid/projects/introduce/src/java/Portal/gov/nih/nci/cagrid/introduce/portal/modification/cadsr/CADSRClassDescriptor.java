package gov.nih.nci.cagrid.introduce.portal.modification.cadsr;

public class CADSRClassDescriptor {
	
	private String context;
	private String classificationScheme;
	private String version;
	private String schemeItem;

	public CADSRClassDescriptor(String context, String classificationScheme, String version, String schemeItem) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;
		this.classificationScheme = classificationScheme;
		this.version = version;
		this.schemeItem = schemeItem;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getClassificationScheme() {
		return classificationScheme;
	}

	public void setClassificationScheme(String classificationScheme) {
		this.classificationScheme = classificationScheme;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getSchemeItem() {
		return schemeItem;
	}

	public void setSchemeItem(String schemeItem) {
		this.schemeItem = schemeItem;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
