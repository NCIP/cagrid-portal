package gov.nih.nci.cagrid.introduce.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ToolTipManager;

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
	private static final String NAMESPACE_DISCOVERY_COMPONENTS = "namespaceTypeDiscoveryComponents";
	private static final String NAMESPACE_TOOLS_COMPONENT = "namespaceTypeToolsComponent";
	private static final String NAMESPACE_TOOLS_COMPONENTS = "namespaceTypeToolsComponents";

	private Map namespaceTypeDiscoveryComponentsMap;
	private List namespaceTypeDiscoveryComponents;
	private Map namespaceTypeToolsComponentsMap;
	private List namespaceTypeToolsComponents;


	public IntroducePortalConf() {
		namespaceTypeDiscoveryComponentsMap = new HashMap();
		namespaceTypeDiscoveryComponents = new ArrayList();
		namespaceTypeToolsComponentsMap = new HashMap();
		namespaceTypeToolsComponents = new ArrayList();
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
	}


	public void parse(MobiusResourceManager resourceManager, Element config) throws MobiusException {
		Element ntdsEl = config.getChild(NAMESPACE_DISCOVERY_COMPONENTS, config.getNamespace());
		if (ntdsEl != null) {
			List ntdEls = ntdsEl.getChildren(NAMESPACE_DISCOVERY_COMPONENT, config.getNamespace());
			if (ntdEls != null) {
				for (int i = 0; i < ntdEls.size(); i++) {
					Element ntdEl = (Element) ntdEls.get(i);
					NamespaceTypeDiscoveryDescriptor desc = new NamespaceTypeDiscoveryDescriptor(ntdEl);
					this.namespaceTypeDiscoveryComponentsMap.put(desc.getType(), desc);
					this.namespaceTypeDiscoveryComponents.add(desc);
				}
			}
		}

		ntdsEl = config.getChild(NAMESPACE_TOOLS_COMPONENTS, config.getNamespace());
		if (ntdsEl != null) {
			List ntdEls = ntdsEl.getChildren(NAMESPACE_TOOLS_COMPONENT, config.getNamespace());
			if (ntdEls != null) {
				for (int i = 0; i < ntdEls.size(); i++) {
					Element ntdEl = (Element) ntdEls.get(i);
					NamespaceTypeToolDescriptor desc = new NamespaceTypeToolDescriptor(ntdEl);
					this.namespaceTypeToolsComponentsMap.put(desc.getType(), desc);
					this.namespaceTypeToolsComponents.add(desc);
				}
			}
		}
	}


	public List getNamespaceTypeDiscoveryComponents() {
		return namespaceTypeDiscoveryComponents;
	}


	public NamespaceTypeDiscoveryDescriptor getNamespaceTypeDiscoveryComponent(String type) {
		return (NamespaceTypeDiscoveryDescriptor) namespaceTypeDiscoveryComponentsMap.get(type);
	}


	public List getNamespaceToolsComponents() {
		return namespaceTypeToolsComponents;
	}


	public NamespaceTypeToolDescriptor getNamespaceTypeToolsComponent(String type) {
		return (NamespaceTypeToolDescriptor) namespaceTypeToolsComponentsMap.get(type);
	}

}
