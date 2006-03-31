package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescriptionType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExtensionsLoader {
	private List extensionDescriptors;
	private File extensionsDir;
	
	public ExtensionsLoader(){
		this.extensionsDir = new File("." + File.separator + "extensions");
		extensionDescriptors = new ArrayList();
		try {
			this.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void load() throws Exception{
		if(extensionsDir.isDirectory()){
			File[] dirs = extensionsDir.listFiles();
			for(int i = 0; i < dirs.length; i ++){
				if(dirs[i].isDirectory()){
					if(new File(dirs[i].getAbsolutePath() + File.separator + "extension.xml").exists()){
						System.out.println("Loading extensions: " + dirs[i].getAbsolutePath() + File.separator + "extension.xml");
						extensionDescriptors.add((ExtensionDescriptionType)Utils.deserializeDocument(new File(dirs[i].getAbsolutePath() + File.separator + "extension.xml").getAbsolutePath(),ExtensionDescriptionType.class));
					}
				}
			}
		}
	}
	
	public List getExtensions(){
		return this.extensionDescriptors;
	}
	
	public ExtensionDescriptionType getExtension(String name){
		for (int i = 0; i < extensionDescriptors.size(); i++) {
			ExtensionDescriptionType ex = (ExtensionDescriptionType) extensionDescriptors.get(i);
			if(ex.getName().equals(name)){
				return ex;
			}
		}
		return null;
	}

	public File getExtensionsDir() {
		return extensionsDir;
	}
}
