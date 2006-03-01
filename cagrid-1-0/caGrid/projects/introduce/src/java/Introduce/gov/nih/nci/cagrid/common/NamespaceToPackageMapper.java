package gov.nih.nci.cagrid.common;

public interface NamespaceToPackageMapper {

	public String getPackageName(String namespace) throws UnsupportedNamespaceFormatException;
}
