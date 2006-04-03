package gov.nih.nci.cagrid.introduce.portal;

import java.util.List;
import java.util.Properties;

import org.jdom.Element;
import org.projectmobius.common.AbstractMobiusConfiguration;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.MobiusResourceManager;


/**
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IntroducePortalConf implements AbstractMobiusConfiguration {

	public static String RESOURCE = "IntroducePortalConf";

	private static final String NAMESPACE_DISCOVERY_COMPONENT = "namespaceTypeDiscoveryComponent";
	public static final String GME_URL = "GME_URL";
	private static final String PROPERTIES = "properties";
	private static final String PROPERTY = "property";
	
	private Properties props;
	private String namespaceTypeDiscoveryClassname = null;

	public IntroducePortalConf() {
		props = new Properties();
	}
	
	public String getNamepaceTypeDiscoveryClassname(){
		return this.namespaceTypeDiscoveryClassname;
	}
	
	public String getProperty(String key){
		return props.getProperty(key);
	}
	

	public void parse(MobiusResourceManager resourceManager, Element config) throws MobiusException {
		Element ntdcEl = config.getChild(NAMESPACE_DISCOVERY_COMPONENT, config.getNamespace());
		if (ntdcEl != null) {
			this.namespaceTypeDiscoveryClassname= ntdcEl.getText();
		}
		Element propertiesEl = config.getChild(PROPERTIES, config.getNamespace());
		if (propertiesEl != null) {
			List propertyElArr = propertiesEl.getChildren(PROPERTY,config.getNamespace());
			for(int i =0; i < propertyElArr.size(); i++){
				Element propEl = (Element)propertyElArr.get(i);
				String key = propEl.getAttributeValue("key");
				String value = propEl.getAttributeValue("value");
				this.props.put(key, value);
			}
		}

	}

}
