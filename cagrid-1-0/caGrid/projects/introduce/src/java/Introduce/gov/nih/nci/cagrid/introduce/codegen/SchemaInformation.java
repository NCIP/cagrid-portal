package gov.nih.nci.cagrid.introduce.codegen;

public class SchemaInformation {

	private String namespace;
	private String prefix;
	private String location;
	
	public SchemaInformation(String namespace,String prefix, String location){
		this.namespace = namespace;
		this.location = location;
		this.prefix = prefix;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
