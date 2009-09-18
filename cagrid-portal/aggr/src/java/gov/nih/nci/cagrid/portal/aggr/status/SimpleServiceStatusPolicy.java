/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SimpleServiceStatusPolicy implements ServiceStatusPolicy {

	private static final Log logger = LogFactory
			.getLog(SimpleServiceStatusPolicy.class);

	private int maxDowntimeHours;

	/**
	 * 
	 */
	public SimpleServiceStatusPolicy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.aggr.status.ServiceStatusPolicy#shouldBanService(java.util.List)
	 */
	public boolean shouldSetServiceDormant(List<StatusChange> statusHistory) {
		boolean should = false;

		StatusChange lastChange = statusHistory.get(statusHistory.size() - 1);
		ServiceStatus currentStatus = lastChange.getStatus();

		if (!ServiceStatus.ACTIVE.equals(currentStatus)
				&& !ServiceStatus.BANNED.equals(currentStatus)
				&& !ServiceStatus.DORMANT.equals(currentStatus)) {

			int changeToNotActiveIdx = -1;
			for (int i = statusHistory.size() - 1; i >= 0; i--) {
				StatusChange change = statusHistory.get(i);
				if (ServiceStatus.ACTIVE.equals(change.getStatus())) {
					changeToNotActiveIdx = i + 1;
					break;
				}
			}
			if (changeToNotActiveIdx > -1
					&& changeToNotActiveIdx < statusHistory.size()) {
				StatusChange change = statusHistory.get(changeToNotActiveIdx);
				Date now = new Date();
				int diffInHours = (int) ((now
						.getTime() - change.getTime().getTime()) / (1000 * 60 * 60));
				if (diffInHours >= getMaxDowntimeHours()) {
					should = true;
				}
			}
		}
		return should;
	}

	public int getMaxDowntimeHours() {
		return maxDowntimeHours;
	}

	public void setMaxDowntimeHours(int maxDowntimeHours) {
		this.maxDowntimeHours = maxDowntimeHours;
	}

}
