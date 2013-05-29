/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.hibernate;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.InitializationException;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.mapping.Mappings;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.sdkquery4.beans.domaininfo.DomainTypesInformation;
import gov.nih.nci.cagrid.sdkquery4.processor.CQL2ParameterizedHQL;
import gov.nih.nci.cagrid.sdkquery4.processor.DomainTypesInformationUtil;
import gov.nih.nci.cagrid.sdkquery4.processor.ParameterizedHqlQuery;
import gov.nih.nci.cagrid.sdkquery4.processor.RoleNameResolver;

import java.io.FileReader;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class HibernateCQLQueryProcessor extends CQLQueryProcessor implements
		InitializingBean, ApplicationContextAware {

	private static final Log logger = LogFactory
			.getLog(HibernateCQLQueryProcessor.class);

	private CQLQueryResultsBuilder cqlQueryResultsBuilder;
	private CQL2ParameterizedHQL cqlTranslator;
	private HibernateTemplate hibernateTemplate;
	private Mappings classToQName;
	private DomainModel domainModel;

	private String domainTypesInfoFileName;
	private String domainModelFileName;
	private String classToQNameFileName;
	private boolean useCaseInsensitiveQueries;
	private ApplicationContext applicationContext;

	/**
	 * 
	 */
	public HibernateCQLQueryProcessor() {

	}

	public CQLQueryResults processQuery(CQLQuery cqlQuery, String[] paths)
			throws MalformedQueryException, QueryProcessingException {
		CQLQueryResults results = null;
		try {
			for (String path : paths) {
				System.out.println("PATH: " + path);
			}

			ParameterizedHqlQuery pQuery = cqlTranslator.convertToHql(cqlQuery);
			List params = pQuery.getParameters();
			String hql = pQuery.getHql();
			if(!hql.startsWith("select")){
				hql = "select __TargetAlias__ " + hql;
			}
			logger.debug("HQL: " + hql);
			List rawResults = hibernateTemplate
					.find(hql, (Object[]) params
							.toArray(new Object[params.size()]));
			if (rawResults.size() > 0) {
				results = cqlQueryResultsBuilder.build(rawResults, cqlQuery,
						classToQName);
			}
		} catch (Exception ex) {
			String msg = "Error processing query: " + ex.getMessage();
			logger.error(msg, ex);
			throw new QueryProcessingException(msg, ex);
		}
		return results;

	}

	/**
	 * Overriden to add initialization of the inheritance manager
	 */
	public void initialize(Properties parameters, InputStream wsdd)
			throws InitializationException {
		try {
			super.initialize(parameters, wsdd);
		} catch (Exception ex) {
			// TODO: Throwing an exception here causes a
			// "java.lang.IllegalArgumentException: object is not an instance of
			// declaring class"
			// when the client connects. So, for now, the best thing to do is
			// print a stack trace.
			ex.printStackTrace();
		}
	}

	public Properties getRequiredParameters() {
		Properties props = new Properties();
		return props;
	}

	public void afterPropertiesSet() throws Exception {

		try {
			Assert.notNull(hibernateTemplate,
					"The hibernateTemplate property is required.");
			Assert.notNull(cqlQueryResultsBuilder,
					"The cqlQueryResultsBuilder property is required.");

			if (applicationContext instanceof WebApplicationContext) {

				WebApplicationContext wac = (WebApplicationContext) applicationContext;
				String basePath = wac.getServletContext().getRealPath("");

				logger.debug("Using WebApplicationContext. Base path is: "
						+ basePath);

				setClassToQNameFileName(basePath + getClassToQNameFileName());
				setDomainModelFileName(basePath + getDomainModelFileName());
				setDomainTypesInfoFileName(basePath
						+ getDomainTypesInfoFileName());
			} else {
				logger.debug("Expecting absolute paths");
			}
			try {
				classToQName = (Mappings) Utils.deserializeDocument(
						getClassToQNameFileName(), Mappings.class);
			} catch (Exception ex) {
				throw new InitializationException("Error getting mapping: "
						+ ex.getMessage(), ex);
			}

			domainModel = (DomainModel) MetadataUtils
					.deserializeDomainModel(new FileReader(
							getDomainModelFileName()));

			DomainTypesInformation typesInfo = null;
			try {
				FileReader reader = new FileReader(getDomainTypesInfoFileName());
				typesInfo = DomainTypesInformationUtil
						.deserializeDomainTypesInformation(reader);
				reader.close();
			} catch (Exception ex) {
				throw new InitializationException(
						"Error deserializing domain types information from "
								+ getDomainTypesInfoFileName() + ": "
								+ ex.getMessage(), ex);
			}

			RoleNameResolver resolver = new RoleNameResolver(domainModel);
			// create the query translator instance
			try {
				cqlTranslator = new CQL2ParameterizedHQL(typesInfo, resolver,
						isUseCaseInsensitiveQueries());
			} catch (Exception ex) {
				throw new InitializationException(
						"Error instantiating CQL to HQL translator: "
								+ ex.getMessage(), ex);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	public String getDomainTypesInfoFileName() {
		return domainTypesInfoFileName;
	}

	public void setDomainTypesInfoFileName(String domainTypesInfoFileName) {
		this.domainTypesInfoFileName = domainTypesInfoFileName;
	}

	public String getDomainModelFileName() {
		return domainModelFileName;
	}

	public void setDomainModelFileName(String domainModelFileName) {
		this.domainModelFileName = domainModelFileName;
	}

	public boolean isUseCaseInsensitiveQueries() {
		return useCaseInsensitiveQueries;
	}

	public void setUseCaseInsensitiveQueries(boolean useCaseInsensitiveQueries) {
		this.useCaseInsensitiveQueries = useCaseInsensitiveQueries;
	}

	public String getClassToQNameFileName() {
		return classToQNameFileName;
	}

	public void setClassToQNameFileName(String classToQNameFileName) {
		this.classToQNameFileName = classToQNameFileName;
	}

	@Override
	public CQLQueryResults processQuery(CQLQuery cqlQuery)
			throws MalformedQueryException, QueryProcessingException {
		return processQuery(cqlQuery, new String[0]);
	}

	public CQLQueryResultsBuilder getCqlQueryResultsBuilder() {
		return cqlQueryResultsBuilder;
	}

	public void setCqlQueryResultsBuilder(
			CQLQueryResultsBuilder cqlQueryResultsBuilder) {
		this.cqlQueryResultsBuilder = cqlQueryResultsBuilder;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
