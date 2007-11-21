/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.dataservice;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public enum QueryInstanceState {
	UNSCHEDULED, SCHEDULED, RUNNING, COMPLETE, CANCELLED, ERROR, TIMEDOUT;
}
