package gov.nih.nci.cagrid.evsgridservice.service;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.evs.query.EVSQueryImpl;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

/**
 *  TODO:DOCUMENT ME
 * 
 * @created by shanbhak
 * 
 */
public class EVSGridServiceImpl extends EVSGridServiceImplBase {

    public EVSGridServiceImpl() throws RemoteException {
        super();
    }

    /**
     *
     * @return
     * @throws RemoteException
     */

	public gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName[] getVocabularyNames() throws RemoteException {
        try
        {
            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            // Build the EVSQuery object
            EVSQuery evsSearch = new EVSQueryImpl();
            evsSearch.getVocabularyNames();
            List evsResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            evsResults = (List)appService.evsSearch(evsSearch);

            // Return data
            gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName[] names = null;
           if ( evsResults != null && evsResults.size() > 0)
           {
               names = new gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName[evsResults.size()];

               for (int i=0; i < evsResults.size(); i++)
               {
                   gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName name = new gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName();
                   name.setVocabularyName((String)evsResults.get(i));
                   names[i] = name;
               }
           }
           return names;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            // TODO: Some logging on the server side
            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL);
        }
        catch(Exception e)
        {
            System.out.println("exception: " + e.getClass().toString());
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
            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            // Build the EVSQuery object
            EVSQuery metaSearch = new EVSQueryImpl();
            metaSearch.getMetaSources();
            List metaResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            metaResults = (List)appService.evsSearch(metaSearch);

            // Return data
            gov.nih.nci.evs.domain.Source[] sources = null;

            if (metaResults != null && metaResults.size() > 0)
            {
                sources = new gov.nih.nci.evs.domain.Source[metaResults.size()];
                for (int i=0; i < metaResults.size(); i++)
                {
                    gov.nih.nci.evs.domain.Source source = (gov.nih.nci.evs.domain.Source) metaResults.get(i);
                    sources[i] = source;
                }
            }
            return sources;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            // TODO: Some logging on the server side
            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL);
        }
        catch(Exception e)
        {
            System.out.println("exception: " + e.getClass().toString());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

    /**
     *
     * @param eVSDescLogicConceptSearchParams
     * @return
     * @throws RemoteException
     */

	public gov.nih.nci.evs.domain.DescLogicConcept[] searchDescLogicConcept(gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams eVSDescLogicConceptSearchParams) throws RemoteException {
        try
        {
            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            // Build the EVSQuery object
            EVSQuery evsSearch = new EVSQueryImpl();
            evsSearch.searchDescLogicConcepts( eVSDescLogicConceptSearchParams.getVocabularyName(),
                                           eVSDescLogicConceptSearchParams.getSearchTerm(),
                                           eVSDescLogicConceptSearchParams.getLimit()
                                        );

            List evsResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            evsResults = (List)appService.evsSearch(evsSearch);
            System.out.println("no. of results: " + evsResults.size() + " className: " + evsResults.get(0).getClass());

            // Return data
            gov.nih.nci.evs.domain.DescLogicConcept[] concepts = null;

            if ( evsResults != null && evsResults.size() > 0)
            {
                concepts = new gov.nih.nci.evs.domain.DescLogicConcept[evsResults.size()];
                for ( int i=0; i < evsResults.size();i++)
                {
                    gov.nih.nci.evs.domain.DescLogicConcept concept = (gov.nih.nci.evs.domain.DescLogicConcept) evsResults.get(i);
                    concepts[i] = concept;
                }
            }
            return concepts;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            // TODO: Some logging on the server side
            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL);
        }
        catch(Exception e)
        {
            System.out.println("exception: " + e.getClass().toString());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

    /**
     *  This API searches Meta Thesaurus based on information provided in the input class <code>gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams</code>
     * @param eVSMetaThesaurusSearchParams Instance of <code>gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams</code>
     * @return
     * @throws RemoteException
     */

	public gov.nih.nci.evs.domain.MetaThesaurusConcept[] searchMetaThesaurus(gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams eVSMetaThesaurusSearchParams) throws RemoteException {
        try
        {
            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

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
            evsResults = (List)appService.evsSearch(evsSearch);

            // Return data
            gov.nih.nci.evs.domain.MetaThesaurusConcept[] concepts = null;
            if ( evsResults != null && evsResults.size() > 0)
            {
                concepts = new gov.nih.nci.evs.domain.MetaThesaurusConcept[evsResults.size()];
                for ( int i=0; i < evsResults.size();i++)
                {
                    gov.nih.nci.evs.domain.MetaThesaurusConcept concept = (gov.nih.nci.evs.domain.MetaThesaurusConcept) evsResults.get(i);
                    concepts[i] = concept;
                }

            }
            return concepts;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            // TODO: Some logging on the server side
            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL);
        }
        catch(Exception e)
        {
            System.out.println("exception: " + e.getClass().toString());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

    /**
     *
     * @param eVSHistoryRecordsSearchParams
     * @return
     * @throws RemoteException
     */

	public gov.nih.nci.evs.domain.HistoryRecord[] getHistoryRecords(gov.nih.nci.cagrid.evs.service.EVSHistoryRecordsSearchParams eVSHistoryRecordsSearchParams) throws RemoteException {
        try
        {
            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            // Build the EVSQuery object
            EVSQuery evsSearch = new EVSQueryImpl();

            evsSearch.getHistoryRecords(eVSHistoryRecordsSearchParams.getVocabularyName(),
                                        eVSHistoryRecordsSearchParams.getConceptCode());

            List evsResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            evsResults = (List)appService.evsSearch(evsSearch);
            System.out.println("no. of results: " + evsResults.size() + " className: " + evsResults.get(0).getClass());

            // Return data
            gov.nih.nci.evs.domain.HistoryRecord[] historys = null;
            historys = new gov.nih.nci.evs.domain.HistoryRecord[evsResults.size()];

            for ( int i=0; i < evsResults.size();i++)
            {
                gov.nih.nci.evs.domain.HistoryRecord history = (gov.nih.nci.evs.domain.HistoryRecord) evsResults.get(i);
//                System.out.println("HistoryRecord[" + i + "] = " + history.getDescLogicConceptCode() );
                historys[i] = history;
            }
            return historys;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            // TODO: Some logging on the server side
            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL);
        }
        catch(Exception e)
        {
            System.out.println("exception: " + e.getClass().toString());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

    /**
     * This method searches for MetaThesaurus instances in the NCI Meta Thesaurus based on valid source code and abbreviation
     * @param eVSSourceSearchParams
     * @return
     * @throws RemoteException
     */

	public gov.nih.nci.evs.domain.MetaThesaurusConcept[] searchSourceByCode(gov.nih.nci.cagrid.evs.service.EVSSourceSearchParams eVSSourceSearchParams) throws RemoteException {
        try
        {
            //Obtain the Application Service
            ApplicationService appService = ApplicationServiceProvider.getRemoteInstance(EVSConstants.CACORE_31_URL);

            // Build the EVSQuery object
            EVSQuery evsSearch = new EVSQueryImpl();

            evsSearch.searchSourceByCode(eVSSourceSearchParams.getCode(), eVSSourceSearchParams.getSourceAbbreviation());

            List evsResults 	= new ArrayList();

            // Perform query: Assume no data is returned
            evsResults = (List)appService.evsSearch(evsSearch);

            // Return data
            gov.nih.nci.evs.domain.MetaThesaurusConcept[] concepts = null;

            if ( evsResults != null && evsResults.size() > 0)
            {
                concepts = new gov.nih.nci.evs.domain.MetaThesaurusConcept[evsResults.size()];
                for ( int i=0; i < evsResults.size();i++)
                {
                    gov.nih.nci.evs.domain.MetaThesaurusConcept concept = (gov.nih.nci.evs.domain.MetaThesaurusConcept) evsResults.get(i);
                    concepts[i] = concept;
                }

            }
            return concepts;
        }
        catch(org.springframework.remoting.RemoteAccessException re)
        {
            // TODO: Some logging on the server side
            throw new ServiceException("Error in connecting to the caCORE Service. Please " +
                    "check that the following URI is working correctly: " + EVSConstants.CACORE_31_URL);
        }
        catch(Exception e)
        {
            System.out.println("exception: " + e.getClass().toString());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

}

