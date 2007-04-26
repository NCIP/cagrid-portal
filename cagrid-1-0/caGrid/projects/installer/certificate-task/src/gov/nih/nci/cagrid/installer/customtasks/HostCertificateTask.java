package gov.nih.nci.cagrid.installer.customtasks;


import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.bouncycastle.jce.PKCS10CertificationRequest;

public class HostCertificateTask extends Task {
	
	private String caKeyLocation; // This string repesent the actual file
	private String caKeyPassword; 
	private String caCertLocation;// This string represent the actual file
	private String hostName;
	private int daysValid;
	private String keyLocation; // Though this should be name of the file but in our case, we will use it as folder
	private String certLocation;//Though this should be name of the file but in our case, we will use it as folder
	
	public void setCaKeyLocation(String kl){
		this.caKeyLocation =kl ;
	}
	public void setCaKeyPassword(String ckpw){
		this.caKeyPassword = ckpw;
	}
	public void setCaCertLocation(String cl){
		this.caCertLocation =cl ;
	}
	public void setHostName(String hostName){
		this.hostName = hostName;
	}
	public void setDaysValid(int dv){
		this.daysValid=dv ;
	}
	public void setkeyLocation(String kl){
		this.keyLocation=kl;
	}
	public void setCertLocation(String cl){
		this.certLocation=cl;
	}
	
	
  
	
	public void generateCertificate() throws Exception{
		
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			
			PrivateKey cakey = null;
			
				try{
					
					cakey = KeyUtil.loadPrivateKey(new File(caKeyLocation),caKeyPassword);
					
				}catch(Exception e){
					throw e;
							
				}
			
			
			X509Certificate cacert = null;
			
			
				try{
					
					cacert = CertUtil.loadCertificate(new File(caCertLocation));
					
				}catch(Exception e){
					throw e;		
				}
			
			String cn = hostName;
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			String rootSub = cacert.getSubjectDN().toString();
			int index = rootSub.lastIndexOf(",");
			String subject = rootSub.substring(0, index) + ",CN=host/"+cn;
			PKCS10CertificationRequest request = CertUtil.generateCertficateRequest(subject, pair);
			
		
			
			GregorianCalendar date = new GregorianCalendar(TimeZone
					.getTimeZone("GMT"));
			/* Allow for a five minute clock skew here. */
			date.add(Calendar.MINUTE, -5);
			Date start = new Date(date.getTimeInMillis());
            Date end = null;
			/* If hours = 0, then cert lifetime is set to user cert */
			if (daysValid <= 0) {
				end = cacert.getNotAfter();
			} else {
				date.add(Calendar.MINUTE, 5);
				date.add(Calendar.DAY_OF_MONTH, daysValid);
				Date d = new Date(date.getTimeInMillis());
				if(cacert.getNotAfter().before(d)){
					throw new GeneralSecurityException("Cannot create a certificate that expires after issuing certificate.");
				}
				end = d;
			}
			X509Certificate userCert = CertUtil.signCertificateRequest(request,start,end,cacert,cakey);

			String keyOut = keyLocation+File.separator+"host_"+hostName+"_key.pem";
			String caOut = certLocation+File.separator+"host_"+hostName+"_cert.pem";
			KeyUtil.writePrivateKey(pair.getPrivate(),new File(keyOut));
			CertUtil.writeCertificate(userCert,new File(caOut));
			System.out.println("Successfully created the user certificate:");
			System.out.println(userCert.getSubjectDN().toString());
			System.out.println("User certificate issued by:");
			System.out.println(cacert.getSubjectDN().toString());
			System.out.println("User Certificate Valid Till:");
			System.out.println(userCert.getNotAfter());
			System.out.println("User Private Key Written to:");
			System.out.println(keyOut);
			System.out.println("User Certificate Written to:");
			System.out.println(caOut);
			
		
	}
	
	public void execute() throws BuildException {
		try{
			this.generateCertificate();
		}catch(Exception ex){
			throw new BuildException(ex.getMessage());
		}
        
    }
	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HostCertificateTask hct = new HostCertificateTask();
		
		hct.setCaCertLocation("c:/temp/certificate/rootCA_cert.pem");
		hct.setCaKeyLocation("c:/temp/certificate/rootCA_key.pem");
		hct.setCaKeyPassword("xvvxcx");
		hct.setHostName("156.40.131.136");
		hct.setDaysValid(90);
		hct.setCertLocation("c:/temp/certificate");
		hct.setkeyLocation("c:/temp/certificate");
		
		
		try {
			hct.generateCertificate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
