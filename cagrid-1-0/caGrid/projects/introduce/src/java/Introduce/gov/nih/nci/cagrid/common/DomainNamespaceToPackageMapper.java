package gov.nih.nci.cagrid.common;

import java.util.StringTokenizer;

import org.projectmobius.common.MalformedNamespaceException;
import org.projectmobius.common.Namespace;


public class DomainNamespaceToPackageMapper implements NamespaceToPackageMapper {

	public String getPackageName(String namespace) throws UnsupportedNamespaceFormatException {
		Namespace ns;
		try {
			ns = new Namespace(namespace);
		} catch (MalformedNamespaceException e) {
			throw new UnsupportedNamespaceFormatException(e.getMessage(), e);
		}
		String domain = ns.getDomain();
		domain = domain.replace('/', '.');
		StringTokenizer tokenizer = new StringTokenizer(domain, ".", true);
		StringBuffer packageNameBuf = new StringBuffer();
		while (tokenizer.hasMoreElements()) {
			String nextToken = tokenizer.nextToken();
			if (nextToken.length() > 0) {
				char startingChar = nextToken.charAt(0);
				if (startingChar >= 48 && startingChar <= 57) {
					nextToken = "_" + nextToken;
				}
				packageNameBuf.insert(0, nextToken);
			}
		}

		String packageName = packageNameBuf.toString();
		return packageName.toLowerCase() ;
	}
}
