/**
 *
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.table.QueryResultCell;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultRow;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import javax.persistence.NonUniqueResultException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class QueryResultTableDao extends AbstractDao<QueryResultTable> {

    @Override
    public Class domainClass() {
        return QueryResultTable.class;
    }

    public List<QueryResultRow> getRows(final Integer tableId,
                                        final int offset, final int numRows) {
        List<QueryResultRow> rows = (List<QueryResultRow>) getHibernateTemplate()
                .execute(new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        return session.createCriteria(QueryResultRow.class)
                                .setFirstResult(offset).setMaxResults(numRows)
                                .createCriteria("table").add(
                                        Restrictions.eq("id", tableId)).list();
                    }
                });
        return rows;
    }

    public List<QueryResultRow> getSortedRows(final Integer tableId,
                                              final String columnName, final String direction, final int offset,
                                              final int numRows) {

        List<QueryResultRow> rows = new ArrayList<QueryResultRow>();

        List<QueryResultCell> cells = (List<QueryResultCell>) getHibernateTemplate()
                .execute(new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        Order order = null;
                        if ("asc".equals(direction)) {
                            order = Order.asc("value");
                        } else {
                            order = Order.desc("value");
                        }
                        return session.createCriteria(QueryResultCell.class)
                                .addOrder(order).setFirstResult(offset)
                                .setMaxResults(numRows)
                                .createCriteria("column").add(
                                        Restrictions.eq("name", columnName))
                                .createCriteria("table").add(
                                        Restrictions.eq("id", tableId)).list();
                    }
                });
        for (QueryResultCell cell : cells) {
            rows.add(cell.getRow());
        }

        return rows;
    }

    public List<QueryResultRow> getSortedRowsByDataServiceUrl(
            final Integer tableId, final String direction, final int offset,
            final int numRows) {
        List<QueryResultRow> rows = (List<QueryResultRow>) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        Order order = null;
                        if ("asc".equals(direction)) {
                            order = Order.asc("serviceUrl");
                        } else {
                            order = Order.desc("serviceUrl");
                        }
                        return session.createCriteria(QueryResultRow.class)
                                .addOrder(order).setFirstResult(offset)
                                .setMaxResults(numRows)
                                .createCriteria("table").add(
                                        Restrictions.eq("id", tableId)).list();
                    }
                });
        return rows;
    }

    public QueryResultTable getByQueryInstanceId(Integer instanceId) {
        List<QueryResultTable> l = getHibernateTemplate().find(
                "from QueryResultTable t where t.queryInstance.id = ?",
                instanceId);
        QueryResultTable t = null;
        if (l.size() > 1) {
            throw new NonUniqueResultException(
                    "More than one query result table found for query instance "
                            + instanceId);
        }
        if (l.size() == 1) {
            t = l.iterator().next();
        }
        return t;
    }

    public Integer getRowCount(final Integer tableId) {
        Integer count = (Integer) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        return session.createCriteria(QueryResultRow.class)
                                .setProjection(Projections.rowCount())
                                .createCriteria("table").add(
                                        Restrictions.eq("id", tableId)).list()
                                .get(0);
                    }
                });
        return count;
    }


    public Integer getRowCountForColumn(final Integer tableId, final String col) {
        Integer count = (Integer) getHibernateTemplate().execute(
                new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {
                        return session.createCriteria(QueryResultCell.class)
                                .createCriteria("column").add(
                                        Restrictions.eq("name", col))
                                .createCriteria("table").add(
                                        Restrictions.eq("id", tableId))
                                .setProjection(Projections.rowCount())
                                .list()
                                .get(0);
                    }
                });
        return count;
    }
}
