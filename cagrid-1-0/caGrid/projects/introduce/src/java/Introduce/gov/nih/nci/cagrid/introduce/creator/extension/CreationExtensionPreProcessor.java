package gov.nih.nci.cagrid.introduce.creator.extension;

import java.util.Properties;

/**
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created 
 */
public abstract class CreationExtensionPreProcessor extends CreationExtensionProcessor {

	public CreationExtensionPreProcessor(Properties serviceProperties) {
		super(serviceProperties);
		// TODO Auto-generated constructor stub
	}

	public abstract void preCreate() throws CreationExtensionException;
	
}
