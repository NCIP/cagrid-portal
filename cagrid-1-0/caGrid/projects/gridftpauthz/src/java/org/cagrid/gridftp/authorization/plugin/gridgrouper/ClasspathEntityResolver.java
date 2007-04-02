package org.cagrid.gridftp.authorization.plugin.gridgrouper;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ClasspathEntityResolver implements EntityResolver {

	public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
		InputSource source = null;
		if (arg1.contains("gridgrouper-config.xsd")) {
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(
				GridGrouperAuthCallout.SCHEMA_LOCATION);
			source = new InputSource(stream);
			
		} else
		if (arg1.contains("gridgrouper.xsd")) {
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(
				GridGrouperAuthCallout.PARENT_SCHEMA);
			source = new InputSource(stream);
		}
		return source;
	}

}
