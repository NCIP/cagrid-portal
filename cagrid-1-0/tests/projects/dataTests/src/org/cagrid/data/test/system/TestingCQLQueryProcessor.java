package org.cagrid.data.test.system;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.LazyCQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.ClassToQname;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.data.utilities.CQLResultsCreationUtil;
import gov.nih.nci.cagrid.data.utilities.ResultsCreationException;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.projectmobius.bookstore.Book;
import org.projectmobius.bookstore.BookStore;

/** 
 *  TestingCQLQueryProcessor
 *  CQL Query Processor for testing purposes only
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> 
 * @created Nov 7, 2006 
 * @version $Id: TestingCQLQueryProcessor.java,v 1.1 2008-05-16 19:25:25 dervin Exp $ 
 */
public class TestingCQLQueryProcessor extends LazyCQLQueryProcessor {
	public static final String PROPERTY_STARTS_WITH_LOWERCASE = "thisPropertyStartsWithLowercase";
	public static final String PROPERTY_STARTS_WITH_UPPERCASE = "ThisPropertyStartsWithUppercase";
	public static final String LC_DEFAULT_VALUE = "lowercase";
	public static final String UC_DEFAULT_VALUE = "UPPERCASE";
	
	public static int BOOK_COUNT = 4;
	
	public static String[] BOOK_TITLES = {
		"Eclipse", "XML Schema", "Hibernate In Action", "Hibernate Quickly"
	};
	public static String[] BOOK_AUTHORS = {
		"Jim D'Anjou, et al", "Eric van der Vlist", 
		"Christian Bauer, Gavin King", "Patrick Peak, Nick, Heudecker"
	};
	
	
	public void initialize(Properties props, InputStream wsddStream) throws InitializationException {
		// verify the values for each required property are NOT the defaults
		// if the defaults are found, that means the service properties aren't
		// getting correctly transfered into the initialization properties
		String lcPropValue = props.getProperty(PROPERTY_STARTS_WITH_LOWERCASE);
		if (lcPropValue.equals(LC_DEFAULT_VALUE)) {
			throw new InitializationException("Default value found for property " + PROPERTY_STARTS_WITH_LOWERCASE + ", service properties incorrectly transfered");
		}
		String ucPropString = props.getProperty(PROPERTY_STARTS_WITH_UPPERCASE);
		if (ucPropString.equals(UC_DEFAULT_VALUE)) {
			throw new InitializationException("Default value found for property " + PROPERTY_STARTS_WITH_UPPERCASE + ", service properties incorrectly transfered");
		}
		super.initialize(props, wsddStream);
	}
	

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
		List<Object> results = new LinkedList<Object>();
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
	
	
	public Properties getRequiredParameters() {
		Properties props = new Properties();
		props.setProperty(PROPERTY_STARTS_WITH_LOWERCASE, LC_DEFAULT_VALUE);
		props.setProperty(PROPERTY_STARTS_WITH_UPPERCASE, UC_DEFAULT_VALUE);
		return props;
	}
}
