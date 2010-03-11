/**
 *
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.Participant;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.List;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ParticipantDao extends AbstractDao<Participant> {

    @Override
    public Class domainClass() {
        return Participant.class;
    }

    @Override
    public void save(Participant participant) {
        super.save(participant);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public List<Participant> getByWorkspaceAbbreviation(
            final String abbreviation) {
        List<Participant> participants = (List<Participant>) getHibernateTemplate()
                .execute(new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        return session.createCriteria(Participant.class)
                                .createCriteria("participation")
                                .createCriteria("workspace").add(
                                        Restrictions.eq("abbreviation",
                                                abbreviation)).list();
                    }
                });
        logger.debug("Retreived " + participants.size()
                + " Participant objects for workspace '" + abbreviation + "'.");

        return participants;
    }

}
