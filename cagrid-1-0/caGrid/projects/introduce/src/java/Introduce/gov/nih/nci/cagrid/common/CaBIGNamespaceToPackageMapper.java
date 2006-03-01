package gov.nih.nci.cagrid.common;

public class CaBIGNamespaceToPackageMapper implements NamespaceToPackageMapper {

	private static final String DOT_SEPARATED_WORDS_REGEX = "([\\w-])+(\\.([\\w-])+)*";


	public String getPackageName(String namespace) throws UnsupportedNamespaceFormatException {
		int i = namespace.lastIndexOf("/");
		if (i <= 0 || i == namespace.length() - 1) {
			throw new UnsupportedNamespaceFormatException(
				"Namespace is expected to have a meaningful package name after the last /");
		}

		String pack = namespace.substring(i+1);
		if (!pack.matches(DOT_SEPARATED_WORDS_REGEX)) {
			throw new UnsupportedNamespaceFormatException(
				"Namespace is expected to have a meaningful package name after the last /, extracted package("+pack+") was not valid");
		}

		return pack.toLowerCase();

	}


	public static void main(String[] args) {
		CaBIGNamespaceToPackageMapper mapper = new CaBIGNamespaceToPackageMapper();
		try {
			System.out.println(mapper.getPackageName("gme://caCORE.cabig/3.0/gov.nih.nci.cadsr.domain"));
		} catch (UnsupportedNamespaceFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
