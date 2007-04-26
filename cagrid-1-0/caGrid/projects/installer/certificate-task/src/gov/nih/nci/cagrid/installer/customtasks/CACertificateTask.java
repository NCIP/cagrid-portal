package gov.nih.nci.cagrid.installer.customtasks;




import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.bouncycastle.asn1.x509.X509Name;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;




public class CACertificateTask extends Task{
	
	private String organization;
	private String organizationalUnit;
	private String commonName;
	private int daysValid;
	private String keyPassword;
	private String certLocation; // This should be actually be a file but we will take it as a folder
	private String keyLocation; // This should be actually be a file but we will take it as a folder
	
	public void setOrganization(String o){
		this.organization =o ;
	}
	public void setOrganizationalUnit(String ou){
		this.organizationalUnit =ou ;
	}
	public void setCommonName(String cn){
		this.commonName =cn ;
	}
	public void setDaysValid(int dv ){
		this.daysValid =dv ;
	}
	
	public void setKeyPassword(String kpw){
		this.keyPassword = kpw;
	}
	public void setCertLocation(String cl){
		this.certLocation =cl ;
	}
	public void setKeyLocation(String kl){
		this.keyLocation =kl ;
	}
	
	public void generateCertificate()throws Exception{
		
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			StringBuffer sb = new StringBuffer();
			sb.append("O=").append(organization);
			sb.append(",OU=" + organizationalUnit);
			sb.append(",CN=" + commonName);
			KeyPair root = KeyUtil.generateRSAKeyPair1024();
			GregorianCalendar date = new GregorianCalendar(TimeZone
					.getTimeZone("GMT"));
			
			date.add(Calendar.MINUTE, -5);
		
			Date start = new Date(date.getTimeInMillis());
			date.add(Calendar.MINUTE, 5);
			
			
			date.add(Calendar.DAY_OF_MONTH,daysValid);
			Date end = new Date(date.getTimeInMillis());
			
			X509Certificate cert = CertUtil.generateCACertificate(new X509Name(
					sb.toString()), start,end, root);
			
			String password = clean(keyPassword);
			
			String keyFile = keyLocation+File.separator+"rootCA_key.pem";
			String certFile = certLocation+File.separator+"rootCA_cert.pem";
			KeyUtil.writePrivateKey(root.getPrivate(),new File(keyFile),password);
			CertUtil.writeCertificate(cert,new File(certFile));
			System.out.println("Successfully created the CA certificate:");
			System.out.println(sb.toString());
			System.out.println("CA Certificate Valid Till:");
			System.out.println(cert.getNotAfter());
			System.out.println("CA Private Key Written to:");
			System.out.println(keyFile);
			System.out.println("CA Certificate Written to:");
			System.out.println(certFile);
			
		
		
		
	}
	
	public void execute() throws BuildException {
		try{
			this.generateCertificate();
		}catch(Exception ex){
			throw new BuildException(ex.getMessage());
		}
        
    }

	private String clean(String s) {
		if ((s == null) || (s.trim().length() == 0)) {
			return null;
		} else {
			return s;
		}
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CACertificateTask ca  = new CACertificateTask();
		ca.setOrganization("NIH");
		ca.setOrganizationalUnit("NCI");
		ca.setCommonName("GRID");
		ca.setDaysValid(180);
		ca.setKeyPassword("xvvxcx");
		ca.setCertLocation("/Users/joshua");
		ca.setKeyLocation("/Users/joshua");
		try {
			ca.generateCertificate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
