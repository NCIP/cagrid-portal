/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.credmgr;

import java.util.Date;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class CredentialBean {
	private String identity;
	private IdPBean idpBean;
	private boolean defaultCredential;
	private Date validUntil;
	
	public CredentialBean(String identity, Date validUntil, IdPBean idpBean) {
		this.identity = identity;
		this.validUntil = validUntil;
		this.idpBean = idpBean;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public IdPBean getIdpBean() {
		return idpBean;
	}
	public void setIdpBean(IdPBean idpBean) {
		this.idpBean = idpBean;
	}
	public boolean isDefaultCredential() {
		return defaultCredential;
	}
	public void setDefaultCredential(boolean defaultCredential) {
		this.defaultCredential = defaultCredential;
	}
	public Date getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}
	
}
