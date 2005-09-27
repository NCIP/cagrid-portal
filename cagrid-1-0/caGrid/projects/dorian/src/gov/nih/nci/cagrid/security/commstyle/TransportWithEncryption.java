package gov.nih.nci.cagrid.security.commstyle;


import org.apache.axis.client.Stub;
import org.globus.axis.util.Util;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GlobusGSSCredentialImpl;
import org.globus.wsrf.security.Constants;
import org.ietf.jgss.GSSCredential;
/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: TransportWithEncryption.java,v 1.1 2005-09-27 20:09:50 langella Exp $
 */
public class TransportWithEncryption implements CommunicationStyle{
	private GlobusCredential credential;
	
	public TransportWithEncryption(){	
	
	}
	
	public TransportWithEncryption(GlobusCredential credential){	
		this.credential = credential;
	}
	
	public void configure(Stub stub) throws CommunicationStyleException{
		try{
			Util.registerTransport();
		stub._setProperty(Constants.GSI_TRANSPORT, Constants.ENCRYPTION);
		if (credential != null) {
			GSSCredential gss = new GlobusGSSCredentialImpl(credential, GSSCredential.INITIATE_AND_ACCEPT);
			stub._setProperty(org.globus.axis.gsi.GSIConstants.GSI_CREDENTIALS, gss);
		}
		}catch(Exception e){
			e.printStackTrace();
			throw new CommunicationStyleException(e.getMessage());
		}
	}

}
