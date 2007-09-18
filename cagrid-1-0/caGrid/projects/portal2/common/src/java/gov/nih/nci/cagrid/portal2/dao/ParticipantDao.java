/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.Participant;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ParticipantDao extends AbstractDao<Participant> {

	@Override
	public Class domainClass() {
		return Participant.class;
	}

	public List<Participant> getByWorkspaceAbbreviation(
			final String abbreviation) {
		List<Participant> participants = (List<Participant>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						return session.createCriteria(Participant.class)
								.createCriteria("workspaces").add(
										Restrictions.eq("abbreviation",
												abbreviation)).list();
					}
				});
		logger.debug("Retreived " + participants.size()
				+ " Participant objects for workspace '" + abbreviation + "'.");

		return participants;
	}

}
