package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.gts.bean.AuthorityGTS;
import gov.nih.nci.cagrid.gts.bean.AuthorityPriorityUpdate;
import gov.nih.nci.cagrid.gts.bean.Lifetime;
import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.Status;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.GridTrustServicePortType;
import gov.nih.nci.cagrid.gts.stubs.IllegalAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidPermissionFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidTrustLevelFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidTrustedAuthorityFault;
import gov.nih.nci.cagrid.gts.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.gts.stubs.service.GridTrustServiceAddressingLocator;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;
import org.globus.wsrf.impl.security.authorization.IdentityAuthorization;
import org.globus.wsrf.impl.security.authorization.NoAuthorization;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: TrustedAuthorityManager.java,v 1.1 2006/03/08 19:48:46 langella
 *          Exp $
 */
public class GTS implements TrustLevelStatus, TrustLevelLookup {

	private GTSConfiguration conf;
	private String gtsURI;
	private TrustedAuthorityManager trust;
	private PermissionManager permissions;
	private TrustLevelManager levels;
	private GTSAuthorityManager authority;
	private Logger logger;


	public GTS(GTSConfiguration conf, String gtsURI) {
		this.conf = conf;
		this.gtsURI = gtsURI;
		Database db = new Database(this.conf.getConnectionManager(), this.conf.getGTSInternalId());
		trust = new TrustedAuthorityManager(this.gtsURI, this, db);
		levels = new TrustLevelManager(this.gtsURI, this, db);
		permissions = new PermissionManager(db);
		authority = new GTSAuthorityManager(db);
		logger = Logger.getLogger(this.getClass().getName());
	}


	public TrustedAuthority addTrustedAuthority(TrustedAuthority ta, String callerGridIdentity)
		throws GTSInternalFault, IllegalTrustedAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		return trust.addTrustedAuthority(ta);
	}


	public TrustedAuthority[] findTrustAuthorities(TrustedAuthorityFilter filter) throws GTSInternalFault {
		return trust.findTrustAuthorities(filter);
	}


	public void updateTrustedAuthority(TrustedAuthority ta, String callerGridIdentity) throws GTSInternalFault,
		IllegalTrustedAuthorityFault, InvalidTrustedAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		trust.updateTrustedAuthority(ta);
	}


	public void removeTrustedAuthority(String name, String callerGridIdentity) throws GTSInternalFault,
		InvalidTrustedAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		trust.removeTrustedAuthority(name);
	}


	public void addTrustLevel(TrustLevel level, String callerGridIdentity) throws GTSInternalFault,
		IllegalTrustLevelFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		levels.addTrustLevel(level);
	}


	public void removeTrustLevel(String name, String callerGridIdentity) throws GTSInternalFault,
		InvalidTrustLevelFault, IllegalTrustLevelFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		levels.removeTrustLevel(name);
	}


	public void updateTrustLevel(TrustLevel level, String callerGridIdentity) throws GTSInternalFault,
		InvalidTrustLevelFault, IllegalTrustLevelFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		levels.updateTrustLevel(level);
	}


	public TrustLevel[] getTrustLevels() throws GTSInternalFault {
		return levels.getTrustLevels();
	}


	public void addPermission(Permission p, String callerGridIdentity) throws GTSInternalFault, IllegalPermissionFault,
		PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		if ((p.getTrustedAuthorityName() != null)
			&& (!p.getTrustedAuthorityName().equals(gov.nih.nci.cagrid.gts.common.Constants.ALL_TRUST_AUTHORITIES))) {
			if (!trust.doesTrustedAuthorityExist(p.getTrustedAuthorityName())) {
				IllegalPermissionFault fault = new IllegalPermissionFault();
				fault.setFaultString("Cannot add permission, the Trusted Authority (" + p.getTrustedAuthorityName()
					+ ") specified does not exist.");
				throw fault;
			}
		}
		permissions.addPermission(p);
	}


	public Permission[] findPermissions(PermissionFilter filter, String callerGridIdentity) throws GTSInternalFault,
		PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		return permissions.findPermissions(filter);
	}


	public void revokePermission(Permission p, String callerGridIdentity) throws GTSInternalFault,
		InvalidPermissionFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		permissions.revokePermission(p);
	}


	public void addAuthority(AuthorityGTS gts, String callerGridIdentity) throws GTSInternalFault,
		IllegalAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		authority.addAuthority(gts);
	}


	public void updateAuthorityPriorities(AuthorityPriorityUpdate update, String callerGridIdentity)
		throws GTSInternalFault, IllegalAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		authority.updateAuthorityPriorities(update);
	}


	public void updateAuthority(AuthorityGTS gts, String callerGridIdentity) throws GTSInternalFault,
		IllegalAuthorityFault, InvalidAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		authority.updateAuthority(gts);
	}


	public AuthorityGTS[] getAuthorities() throws GTSInternalFault {
		return authority.getAuthorities();
	}


	public void removeAuthority(String serviceURI, String callerGridIdentity) throws GTSInternalFault,
		InvalidAuthorityFault, PermissionDeniedFault {
		checkServiceAdministrator(callerGridIdentity);
		try {
			authority.removeAuthority(serviceURI);
			TrustedAuthorityFilter f = new TrustedAuthorityFilter();
			f.setSourceTrustService(serviceURI);
			TrustedAuthority[] ta = this.trust.findTrustAuthorities(f);
			boolean error = false;
			StringBuffer elist = null;
			for (int i = 0; i < ta.length; i++) {
				try {
					trust.removeTrustedAuthority(ta[i].getTrustedAuthorityName());
				} catch (Exception ex) {
					logger.error(ex);
					if (elist == null) {
						error = true;
						elist = new StringBuffer("Unable to remove the trusted authorities:\n");

					}
					elist.append(ta[i].getTrustedAuthorityName() + "\n");

				}
			}
			if (error) {
				throw new Exception(elist.toString());
			}
		} catch (Exception e) {
			logger.error(e);
			GTSInternalFault fault = new GTSInternalFault();
			fault.setFaultString("An following unexpected error occurred removing the authority " + serviceURI + ": "
				+ e.getMessage());
			throw fault;
		}
	}


	private void checkServiceAdministrator(String gridIdentity) throws GTSInternalFault, PermissionDeniedFault {
		if (!permissions.isUserTrustServiceAdmin(gridIdentity)) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("You are not a trust service administrator!!!");
			throw fault;
		}
	}


	public void destroy() throws GTSInternalFault {
		trust.destroy();
		permissions.destroy();
		levels.destroy();
		authority.destroy();
	}


	public boolean isTrustLevelUsed(String name) throws GTSInternalFault {
		TrustedAuthorityFilter f = new TrustedAuthorityFilter();
		f.setTrustLevel(name);
		TrustedAuthority[] ta = this.findTrustAuthorities(f);
		if ((ta == null) || (ta.length == 0)) {
			return false;
		} else {
			return true;
		}
	}


	public boolean doesTrustLevelExist(String name) throws GTSInternalFault {
		if (levels.doesTrustLevelExist(name)) {
			return true;
		} else {
			return false;
		}
	}


	protected void synchronizeTrustedAuthorities(String authorityServiceURI, TrustedAuthority[] trusted) {
		// Synchronize the Trusted Authorities
		if (trusted != null) {
			// We need to get a list of all the Trusted Authorities provided
			// by the source,
			// such that we can remove the ones that are not provided in the
			// new list
			Map toBeDeleted = new HashMap();
			try {
				TrustedAuthorityFilter f = new TrustedAuthorityFilter();
				f.setSourceTrustService(authorityServiceURI);
				TrustedAuthority[] existing = this.trust.findTrustAuthorities(f);
				for (int i = 0; i < existing.length; i++) {
					toBeDeleted.put(existing[i].getTrustedAuthorityName(), Boolean.TRUE);
				}
			} catch (Exception e) {
				this.logger.error("Error synchronizing with the authority " + authorityServiceURI
					+ "the following error occurred obtaining the existing Trusted Authorities: " + e.getMessage(), e);
				return;
			}

			for (int j = 0; j < trusted.length; j++) {
				try {
					toBeDeleted.remove(trusted[j].getTrustedAuthorityName());
					if (this.trust.doesTrustedAuthorityExist(trusted[j].getTrustedAuthorityName())) {
						// Perform Update
						TrustedAuthority ta = this.trust.getTrustedAuthority(trusted[j].getTrustedAuthorityName());
						AuthorityGTS updateAuthority = authority.getAuthority(authorityServiceURI);
						// Determine if we should peform update
						boolean performUpdate = false;
						// Check to see if this service is the authority
						if (!ta.getAuthorityTrustService().equals(gtsURI)) {
							AuthorityGTS currAuthority = authority.getAuthority(ta.getSourceTrustService());

							// Check to see if the authority GTS is the same
							if (currAuthority.getServiceURI().equals(updateAuthority.getServiceURI())) {
								performUpdate = true;
								this.logger.debug("The trusted authority (" + ta.getTrustedAuthorityName()
									+ ") will be updated!!!");
							} else if (currAuthority.getPriority() > updateAuthority.getPriority()) {
								performUpdate = true;
								this.logger.debug("The trusted authority (" + ta.getTrustedAuthorityName()
									+ ") will be updated, the authority (" + updateAuthority.getServiceURI()
									+ ") has a greater priority then the current source authority ("
									+ currAuthority.getServiceURI() + ")!!!");

							} else {
								this.logger.debug("The trusted authority (" + ta.getTrustedAuthorityName()
									+ ") will NOT be updated, the current source authority ("
									+ currAuthority.getServiceURI()
									+ ") has a greater priority then the source authority ("
									+ updateAuthority.getServiceURI() + ")!!!");
								performUpdate = false;
							}
						} else {
							this.logger.debug("The trusted authority (" + ta.getTrustedAuthorityName()
								+ ") will NOT be updated, this GTS is its authority !!!");
							performUpdate = false;
						}
						if (performUpdate) {
							trusted[j].setIsAuthority(Boolean.FALSE);
							trusted[j].setSourceTrustService(authorityServiceURI);
							Calendar c = new GregorianCalendar();
							c.add(Calendar.HOUR, updateAuthority.getTrustedAuthorityTimeToLive().getHours());
							c.add(Calendar.MINUTE, updateAuthority.getTrustedAuthorityTimeToLive().getMinutes());
							c.add(Calendar.SECOND, updateAuthority.getTrustedAuthorityTimeToLive().getSeconds());
							trusted[j].setExpires(c.getTimeInMillis());
							try {
								trust.updateTrustedAuthority(trusted[j], false);
							} catch (Exception e) {
								this.logger.error("Error synchronizing with the authority " + authorityServiceURI
									+ ", the following error occcurred when trying to update the authority, "
									+ trusted[j].getTrustedAuthorityName() + ": " + e.getMessage(), e);
								continue;
							}
						}
					} else {
						AuthorityGTS updateAuthority = authority.getAuthority(authorityServiceURI);
						this.logger.debug("The trusted authority (" + trusted[j].getTrustedAuthorityName()
							+ ") will be added with the authority (" + authorityServiceURI + ") as the source!!!");
						trusted[j].setIsAuthority(Boolean.FALSE);
						trusted[j].setSourceTrustService(authorityServiceURI);
						Calendar c = new GregorianCalendar();
						c.add(Calendar.HOUR, updateAuthority.getTrustedAuthorityTimeToLive().getHours());
						c.add(Calendar.MINUTE, updateAuthority.getTrustedAuthorityTimeToLive().getMinutes());
						c.add(Calendar.SECOND, updateAuthority.getTrustedAuthorityTimeToLive().getSeconds());
						trusted[j].setExpires(c.getTimeInMillis());
						try {
							trust.addTrustedAuthority(trusted[j], false);
						} catch (Exception e) {
							this.logger.error("Error synchronizing with the authority " + authorityServiceURI
								+ ", the following error occcurred when trying to add the authority, "
								+ trusted[j].getTrustedAuthorityName() + ": " + e.getMessage(), e);
							continue;
						}

					}
				} catch (Exception ex) {
					this.logger.error("Error synchronizing with the authority " + authorityServiceURI + ": "
						+ ex.getMessage(), ex);
					continue;
				}
			}
			Iterator itr = toBeDeleted.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String) itr.next();
				try {
					trust.removeTrustedAuthority(name);
					this.logger
						.debug("The trusted authority (" + name
							+ ") was removed because it has been removed from the authority " + authorityServiceURI
							+ "!!!");
				} catch (Exception e) {
					this.logger.error("The trusted authority (" + name
						+ ") should have beeen removed because it has been removed from the authority "
						+ authorityServiceURI + ", however the following error occurred:" + e.getMessage(), e);
				}
			}

		}
	}


	private void synchronizeWithAuthorities() {
		AuthorityGTS[] auths = null;
		try {
			auths = this.getAuthorities();
		} catch (Exception ex) {
			this.logger
				.error("Error synchronizing with the authorities, could not obtain a list of authorities!!!", ex);
			return;
		}

		TrustedAuthorityFilter filter = new TrustedAuthorityFilter();
		filter.setStatus(Status.Trusted);
		filter.setLifetime(Lifetime.Valid);

		for (int i = 0; i < auths.length; i++) {
			TrustedAuthority[] trusted = null;
			try {
				GridTrustServiceAddressingLocator locator = new GridTrustServiceAddressingLocator();
				EndpointReferenceType endpoint = new EndpointReferenceType();
				endpoint.setAddress(new Address(auths[i].getServiceURI()));
				GridTrustServicePortType port = locator.getGridTrustServicePortTypePort(endpoint);
				org.apache.axis.client.Stub stub = (org.apache.axis.client.Stub) port;
				stub._setProperty(org.globus.wsrf.security.Constants.GSI_TRANSPORT,
					org.globus.wsrf.security.Constants.ENCRYPTION);
				stub._setProperty(org.globus.wsrf.security.Constants.GSI_ANONYMOUS, Boolean.TRUE);
				if (auths[i].isPerformAuthorization()) {
					IdentityAuthorization ia = new IdentityAuthorization(auths[i].getServiceIdentity());
					stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, ia);
				} else {
					stub._setProperty(org.globus.wsrf.security.Constants.AUTHORIZATION, NoAuthorization.getInstance());
				}
				gov.nih.nci.cagrid.gts.stubs.FindTrustedAuthorities params = new gov.nih.nci.cagrid.gts.stubs.FindTrustedAuthorities();
				gov.nih.nci.cagrid.gts.stubs.FindTrustedAuthoritiesF fContainer = new gov.nih.nci.cagrid.gts.stubs.FindTrustedAuthoritiesF();
				fContainer.setTrustedAuthorityFilter(filter);
				params.setF(fContainer);
				gov.nih.nci.cagrid.gts.stubs.FindTrustedAuthoritiesResponse boxedResult = port
					.findTrustedAuthorities(params);
				trusted = boxedResult.getTrustedAuthority();

			} catch (Exception ex) {
				this.logger.error("Error synchronizing with the authority " + auths[i].getServiceURI() + ": "
					+ ex.getMessage(), ex);
				continue;
			}

			// Synchronize the Trusted Authorities
			this.synchronizeTrustedAuthorities(auths[i].getServiceURI(), trusted);
		}
	}
}
