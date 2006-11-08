package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.ClassToQname;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;
import gov.nih.nci.cagrid.data.utilities.ResultsCreationException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.projectmobius.bookstore.Book;
import org.projectmobius.bookstore.BookStore;

import javax.xml.namespace.QName;

/** 
 *  TestingCQLQueryProcessor
 *  CQL Query Processor for testing purposes only
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> 
 * @created Nov 7, 2006 
 * @version $Id: TestingCQLQueryProcessor.java,v 1.1 2006-11-08 18:09:38 dervin Exp $ 
 */
public class TestingCQLQueryProcessor extends LazyCQLQueryProcessor {
	public static int BOOK_COUNT = 4;
	
	public static String[] BOOK_TITLES = {
		"Eclipse", "XML Schema", "Hibernate In Action", "Hibernate Quickly"
	};
	public static String[] BOOK_AUTHORS = {
		"Jim D'Anjou, et al", "Eric van der Vlist", 
		"Christian Bauer, Gavin King", "Patrick Peak, Nick, Heudecker"
	};

	public Iterator processQueryLazy(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
		List results = getResultsList(cqlQuery);
		return results.iterator();
	}


	public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
		List results = getResultsList(cqlQuery);
		Mappings mapping = new Mappings();
		ClassToQname bookMap = new ClassToQname();
		bookMap.setClassName(Book.class.getName());
		bookMap.setQname(new QName("gme://projectmobius.org/1/BookStore", "Book").toString());
		ClassToQname storeMap = new ClassToQname();
		storeMap.setClassName(BookStore.class.getName());
		storeMap.setQname(new QName("gme://projectmobius.org/1/BookStore", "Book").toString());
		mapping.setMapping(new ClassToQname[] {bookMap, storeMap});
		try {
			CQLQueryResults queryResults = CQLResultsCreationUtil.createObjectResults(results, cqlQuery.getTarget().getName(), mapping);
			return queryResults;
		} catch (ResultsCreationException ex) {
			throw new QueryProcessingException("Error creating result set: " + ex.getMessage(), ex);
		}
	}
	
	
	private List getResultsList(CQLQuery query) throws QueryProcessingException {
		List results = new LinkedList();
		if (query.getTarget().getName().equals(Book.class.getName())) {
			for (int i = 0; i < BOOK_COUNT; i++) {			
				Book book = new Book();
				book.setTitle(BOOK_TITLES[i]);
				book.setAuthor(BOOK_AUTHORS[i]);
				results.add(book);
			}
		} else if (query.getTarget().getName().equals(BookStore.class.getName())) {
				BookStore store = new BookStore();
				results.add(store);
		} else {
			throw new QueryProcessingException("Target " + query.getTarget().getName() + " is not valid!");
		}
		return results;
	}
}
