package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExtensionsLoader {

	private static ExtensionsLoader loader = null;

	public static final String EXTENSIONS_DIRECTORY = "." + File.separator
			+ "extensions";

	public static final String DISCOVERY_EXTENSION = "DISCOVERY";

	public static final String SERVICE_EXTENSION = "SERVICE";

	private List serviceExtensionDescriptors;

	private List discoveryExtensionDescriptors;

	private File extensionsDir;

	private ExtensionsLoader() {
		this.extensionsDir = new File(EXTENSIONS_DIRECTORY);
		serviceExtensionDescriptors = new ArrayList();
		discoveryExtensionDescriptors = new ArrayList();
		try {
			this.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ExtensionsLoader getInstance() {
		if (loader == null) {
			loader = new ExtensionsLoader();
		}
		return loader;
	}

	private void load() throws Exception {

		if (extensionsDir.isDirectory()) {
			final File[] dirs = extensionsDir.listFiles();
			for (int i = 0; i < dirs.length; i++) {
				final int count = i;
				if (dirs[i].isDirectory()) {
					if (new File(dirs[i].getAbsolutePath() + File.separator
							+ "extension.xml").exists()) {

						System.out.println("Loading extension: "
								+ dirs[count].getAbsolutePath()
								+ File.separator + "extension.xml");
						ExtensionDescription extDesc = null;

						try {
							extDesc = (ExtensionDescription) Utils
									.deserializeDocument(new File(dirs[count]
											.getAbsolutePath()
											+ File.separator + "extension.xml")
											.getAbsolutePath(),
											ExtensionDescription.class);

							if (extDesc.getExtensionType().equals(
									DISCOVERY_EXTENSION)) {
								discoveryExtensionDescriptors.add(extDesc
										.getDiscoveryExtensionDescription());
							} else if (extDesc.getExtensionType().equals(
									SERVICE_EXTENSION)) {
								serviceExtensionDescriptors.add(extDesc
										.getServiceExtensionDescription());
							} else {
								System.out
										.println("Unsupported Extension Type: "
												+ extDesc.getExtensionType());
							}// TODO Auto-generated method stub
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
			}
		}

	}

	public List getServiceExtensions() {
		return this.serviceExtensionDescriptors;
	}

	public ServiceExtensionDescriptionType getServiceExtension(String name) {
		for (int i = 0; i < serviceExtensionDescriptors.size(); i++) {
			ServiceExtensionDescriptionType ex = (ServiceExtensionDescriptionType) serviceExtensionDescriptors
					.get(i);
			if (ex.getName().equals(name)) {
				return ex;
			}
		}
		return null;
	}

	public ServiceExtensionDescriptionType getServiceExtensionByDisplayName(
			String displayName) {
		for (int i = 0; i < serviceExtensionDescriptors.size(); i++) {
			ServiceExtensionDescriptionType ex = (ServiceExtensionDescriptionType) serviceExtensionDescriptors
					.get(i);
			if (ex.getDisplayName().equals(displayName)) {
				return ex;
			}
		}
		return null;
	}

	public DiscoveryExtensionDescriptionType getDiscoveryExtension(String name) {
		for (int i = 0; i < discoveryExtensionDescriptors.size(); i++) {
			DiscoveryExtensionDescriptionType ex = (DiscoveryExtensionDescriptionType) discoveryExtensionDescriptors
					.get(i);
			if (ex.getName().equals(name)) {
				return ex;
			}
		}
		return null;
	}

	public DiscoveryExtensionDescriptionType getDiscoveryExtensionByDisplayName(
			String displayName) {
		for (int i = 0; i < serviceExtensionDescriptors.size(); i++) {
			DiscoveryExtensionDescriptionType ex = (DiscoveryExtensionDescriptionType) discoveryExtensionDescriptors
					.get(i);
			if (ex.getDisplayName().equals(displayName)) {
				return ex;
			}
		}
		return null;
	}

	public List getDiscoveryExtensions() {
		return this.discoveryExtensionDescriptors;
	}

	public File getExtensionsDir() {
		return extensionsDir;
	}
}
