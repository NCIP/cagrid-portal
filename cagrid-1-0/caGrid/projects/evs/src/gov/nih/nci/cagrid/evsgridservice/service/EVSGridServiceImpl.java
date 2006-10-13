package gov.nih.nci.cagrid.evsgridservice.service;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.evs.query.EVSQueryImpl;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  This class is the actual implementation of all the operations provided by the EVS grid service.
 *
 * @created By shanbhak
 *
 */
public class EVSGridServiceImpl extends EVSGridServiceImplBase {

    protected static Log LOG = LogFactory.getLog(EVSGridServiceImpl.class.getName());

    public EVSGridServiceImpl() throws RemoteException {
        super();
    }

    /**
     * This method returns all the vocabularies present in the Description Logic in caCORE 3.1 EVS service.
     * @return Array of <code>gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName</code>  or null
     * @throws RemoteException
     */

    public gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName[] getVocabularyNames() throws RemoteException {
        try
        {
            LOG.debug("Inside method:getVocabularyNames. Obtaining connection to caCORE remote instance" +
                    EVSConstants.CACORE_31_URL);


            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            LOG.debug("Obtained connection to caCORE Application Service remote Instance. Building EVSQuery instance");

            // Build the EVSQuery object
            EVSQuery evsSearch = new EVSQueryImpl();
            evsSearch.getVocabularyNames();
            List evsResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            LOG.debug("Calling evsSearch");
            evsResults = (List)appService.evsSearch(evsSearch);

            // Return data
            gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName[] names = null;


           if ( evsResults != null && evsResults.size() > 0)
           {
               LOG.debug("Returning Result count: " + evsResults.size());
               names = new gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName[evsResults.size()];

               for (int i=0; i < evsResults.size(); i++)
               {
                   gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName name = new gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName();
                   name.setVocabularyName((String)evsResults.get(i));
                   names[i] = name;
               }
           }
            else
           {
               LOG.debug("There are no results to return!");
           }

           return names;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            LOG.error("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);

            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);

        }
        catch(Exception e)
        {
            LOG.error("Exception while searching: "+ e.getMessage());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

    /**
     * This method will return an array of <code>gov.nih.nci.evs.domain.Source</code> objects in an
     * array. The Method will query the cacore 3.1 service running at NCICB
     * @return Array of <code>gov.nih.nci.evs.domain.Source</code> objects or null
     * @throws RemoteException
     */

    public gov.nih.nci.evs.domain.Source[] getMetaSources() throws RemoteException {

        try
        {
            LOG.debug("Inside method:getMetaSources. Obtaining connection to caCORE remote instance" +
                    EVSConstants.CACORE_31_URL);

            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            LOG.debug("Obtained connection to caCORE Application Service remote Instance. Building EVSQuery instance");

            // Build the EVSQuery object
            EVSQuery metaSearch = new EVSQueryImpl();
            metaSearch.getMetaSources();
            List metaResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            LOG.debug("Calling evsSearch");
            metaResults = (List)appService.evsSearch(metaSearch);

            // Return data
            gov.nih.nci.evs.domain.Source[] sources = null;

            if (metaResults != null && metaResults.size() > 0)
            {

                LOG.debug("Returning Result count: " + metaResults.size());
                sources = new gov.nih.nci.evs.domain.Source[metaResults.size()];
                System.arraycopy(metaResults.toArray(), 0, sources, 0, metaResults.size());

            }
            else
            {
                LOG.debug("There are no results to return!");
            }

            return sources;

        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            LOG.error("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);

            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);
        }
        catch(Exception e)
        {
            LOG.error("Exception while searching: "+ e.getMessage());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

    /**
     * This method searches for a valid Vocabulary such as NCI Thesaurus and returns
     * Description Logic concepts <code>ov.nih.nci.evs.domain.DescLogicConcept</code> that meet the search criteria.
     * This method will query the caCORE 3.1 EVS service
     *
     * @param eVSDescLogicConceptSearchParams The instance of class <code>gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams</code>
     *                                        that contains the vocabulary name, Search term and maximum number of results
     *
     * @return Array of gov.nih.nci.evs.domain.DescLogicConcept objects or null
     * @throws RemoteException
     */

    public gov.nih.nci.evs.domain.DescLogicConcept[] searchDescLogicConcept(gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams eVSDescLogicConceptSearchParams) throws RemoteException {
        try
        {

            LOG.debug("Inside method:getMetaSources. Obtaining connection to caCORE remote instance" +
                    EVSConstants.CACORE_31_URL);


            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            LOG.debug("Obtained connection to caCORE Application Service remote Instance. " +
                    "Building EVSQuery instance: " +
                    "\nVocabulary Name: " + eVSDescLogicConceptSearchParams.getVocabularyName() +
                    "\nSearch Term: " + eVSDescLogicConceptSearchParams.getSearchTerm() +
                    "\nLimit: " + eVSDescLogicConceptSearchParams.getLimit());

            // Build the EVSQuery object
            EVSQuery evsSearch = new EVSQueryImpl();
            evsSearch.searchDescLogicConcepts( eVSDescLogicConceptSearchParams.getVocabularyName(),
                                           eVSDescLogicConceptSearchParams.getSearchTerm(),
                                           eVSDescLogicConceptSearchParams.getLimit()
                                        );


            List evsResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            LOG.debug("Calling evsSearch");
            evsResults = (List)appService.evsSearch(evsSearch);

            // Return data
            gov.nih.nci.evs.domain.DescLogicConcept[] concepts = null;

            if ( evsResults != null && evsResults.size() > 0)
            {
                LOG.debug("Returning Result count: " + evsResults.size());
                concepts = new gov.nih.nci.evs.domain.DescLogicConcept[evsResults.size()];
                System.arraycopy(evsResults.toArray(), 0, concepts, 0, evsResults.size());
            }
            else
            {
                LOG.debug("There are no results to return!");
            }
            return concepts;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            LOG.error("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);

            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);
        }
        catch(Exception e)
        {
            LOG.error("Exception while searching: "+ e.getMessage());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

    /**
     *  This API searches Meta Thesaurus based on information provided in the input class
     * <code>gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams</code>. This method
     * queries the caCORE 3.1 EVS service.
     * @param eVSMetaThesaurusSearchParams Instance of <code>gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams</code>
     * @return Array of <code> gov.nih.nci.evs.domain.MetaThesaurusConcept>/code> or null
     * @throws RemoteException
     */

    public gov.nih.nci.evs.domain.MetaThesaurusConcept[] searchMetaThesaurus(gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams eVSMetaThesaurusSearchParams) throws RemoteException {
        try
        {
            LOG.debug("Inside method:getMetaSources. Obtaining connection to caCORE remote instance" +
                    EVSConstants.CACORE_31_URL);

            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            LOG.debug("Obtained connection to caCORE Application Service remote Instance. " +
                    "Building EVSQuery instance: " +
                    "\nSearch Term: " + eVSMetaThesaurusSearchParams.getSearchTerm() +
                    "\nLimit: " + eVSMetaThesaurusSearchParams.getLimit() +
                    "\nSource: " + eVSMetaThesaurusSearchParams.getSource() +
                    "\nCui: " + eVSMetaThesaurusSearchParams.isCui() +
                    "\nShort Response: " + eVSMetaThesaurusSearchParams.isShortResponse() +
                    "\nScore: " + eVSMetaThesaurusSearchParams.isScore());

            // Build the EVSQuery object
            EVSQuery evsSearch = new EVSQueryImpl();

            evsSearch.searchMetaThesaurus(eVSMetaThesaurusSearchParams.getSearchTerm(),
                    eVSMetaThesaurusSearchParams.getLimit(),
                    eVSMetaThesaurusSearchParams.getSource(),
                    eVSMetaThesaurusSearchParams.isCui(),
                    eVSMetaThesaurusSearchParams.isShortResponse(),
                    eVSMetaThesaurusSearchParams.isScore());

            List evsResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            LOG.debug("Calling evsSearch");
            evsResults = (List)appService.evsSearch(evsSearch);

            // Return data
            gov.nih.nci.evs.domain.MetaThesaurusConcept[] concepts = null;

            if ( evsResults != null && evsResults.size() > 0)
            {
                LOG.debug("Returning Result count: " + evsResults.size());
                concepts = new gov.nih.nci.evs.domain.MetaThesaurusConcept[evsResults.size()];
                System.arraycopy(evsResults.toArray(), 0, concepts, 0, evsResults.size());

            }
            else
            {
              LOG.debug("There are no results to return!");
            }
            return concepts;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            LOG.error("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);

            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);
        }
        catch(Exception e)
        {
            LOG.error("Exception while searching: "+ e.getMessage());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }

    }

    /**
     * This API searches a valid vocabulary in NCI thesaurus for history information. The input to the API is an instance of
     * <code>gov.nih.nci.cagrid.evs.service.EVSHistoryRecordsSearchParams</code> that allows input of vocabulary name
     * and concept code. This method queries caCORE 3.1 EVS service
     *
     * @param eVSHistoryRecordsSearchParams Instance of <code>gov.nih.nci.cagrid.evs.service.EVSHistoryRecordsSearchParams</code>
     * @return Array of <code>gov.nih.nci.evs.domain.HistoryRecord</code> or null
     * @throws RemoteException
     */

    public gov.nih.nci.evs.domain.HistoryRecord[] getHistoryRecords(gov.nih.nci.cagrid.evs.service.EVSHistoryRecordsSearchParams eVSHistoryRecordsSearchParams) throws RemoteException {
        try
        {
            LOG.debug("Inside method:getMetaSources. Obtaining connection to caCORE remote instance" +
                    EVSConstants.CACORE_31_URL);

            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            LOG.debug("Obtained connection to caCORE Application Service remote Instance. " +
                    "Building EVSQuery instance: " +
                    "\nVocabulary Name: " + eVSHistoryRecordsSearchParams.getVocabularyName() +
                    "\nConcept code: " + eVSHistoryRecordsSearchParams.getConceptCode());

            // Build the EVSQuery object
            EVSQuery evsSearch = new EVSQueryImpl();

            evsSearch.getHistoryRecords(eVSHistoryRecordsSearchParams.getVocabularyName(),
                                        eVSHistoryRecordsSearchParams.getConceptCode());

            List evsResults 	= new ArrayList();
            // Perform query: Assume no data is returned
            LOG.debug("Calling evsSearch");
            evsResults = (List)appService.evsSearch(evsSearch);

            // Return data
            gov.nih.nci.evs.domain.HistoryRecord[] historys = null;

            if ( evsResults != null && evsResults.size() > 0)
            {

                LOG.debug("Returning Result count: " + evsResults.size());
                historys = new gov.nih.nci.evs.domain.HistoryRecord[evsResults.size()];
                System.arraycopy(evsResults.toArray(), 0, historys, 0, evsResults.size());

/*
                for ( int i=0; i < evsResults.size();i++)
                {
                    gov.nih.nci.evs.domain.HistoryRecord history = (gov.nih.nci.evs.domain.HistoryRecord) evsResults.get(i);
                    historys[i] = history;
                }

*/
            }
            else
            {
                LOG.debug("There are no results to return!");
            }
            return historys;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            LOG.error("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);

            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);
        }
        catch(Exception e)
        {
            LOG.error("Exception while searching: "+ e.getMessage());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

    /**
     * This API searches for MetaThesaurus instances in the NCI Meta Thesaurus based on valid source code and abbreviation.
     * The method queries the caCORE 3.1 EVS Service.
     *
     * @param eVSSourceSearchParams  Instance of <code>gov.nih.nci.cagrid.evs.service.EVSSourceSearchParams</code> class
     *                               that contains the Source code and abbreviation
     * @return Array of <code>gov.nih.nci.evs.domain.MetaThesaurusConcept</code> or null
     * @throws RemoteException
     */

    public gov.nih.nci.evs.domain.MetaThesaurusConcept[] searchSourceByCode(gov.nih.nci.cagrid.evs.service.EVSSourceSearchParams eVSSourceSearchParams) throws RemoteException {
        try
        {
            LOG.debug("Inside method:getMetaSources. Obtaining connection to caCORE remote instance" +
                    EVSConstants.CACORE_31_URL);

            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            LOG.debug("Obtained connection to caCORE Application Service remote Instance. " +
                    "Building EVSQuery instance: " +
                    "\nCode: " + eVSSourceSearchParams.getCode() +
                    "\nAbbreviation: " + eVSSourceSearchParams.getSourceAbbreviation());

            // Build the EVSQuery object
            EVSQuery evsSearch = new EVSQueryImpl();

            evsSearch.searchSourceByCode(eVSSourceSearchParams.getCode(), eVSSourceSearchParams.getSourceAbbreviation());

            List evsResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            LOG.debug("Calling evsSearch");
            evsResults = (List)appService.evsSearch(evsSearch);

            // Return data
            gov.nih.nci.evs.domain.MetaThesaurusConcept[] concepts = null;

            if ( evsResults != null && evsResults.size() > 0)
            {
                LOG.debug("Returning Result count: " + evsResults.size());
                concepts = new gov.nih.nci.evs.domain.MetaThesaurusConcept[evsResults.size()];
                System.arraycopy(evsResults.toArray(), 0, concepts, 0, evsResults.size());

            }
            else
            {
                LOG.debug("There are no results to return!");
            }

            return concepts;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            LOG.error("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);

            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL, re);
        }
        catch(Exception e)
        {
            LOG.error("Exception while searching: "+ e.getMessage());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

}

