package gov.nih.nci.cagrid.evsgridservice.service;

import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.evs.query.EVSQueryImpl;
import gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType;

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

	public gov.nih.nci.evs.domain.DescLogicConcept[] searchDescLogicConcept(gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams eVSDescLogicConceptSearchParams) throws RemoteException, gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType {
        try
        {

            LOG.debug("Inside method:searchDescLogicConcept:Testing input");

            // throws exception if inputs are not valid
            validateEVSDescLogicConceptSearchParams(eVSDescLogicConceptSearchParams);

            // Check if the user specified search term is really a concept code. This is quite tricky becasuse there
            // isn't really any specific structure. However, the user should be able to use the same API and get
            // DescLogicConcept objects either by passing search term or concept code.
            boolean bIsSearchTermConceptCode = false;
            bIsSearchTermConceptCode = isSearchTermConceptCode(eVSDescLogicConceptSearchParams.getSearchTerm());

            LOG.debug("Inside method:searchDescLogicConcept. Obtaining connection to caCORE remote instance" +
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
            List evsResults 	= new ArrayList();

            if (bIsSearchTermConceptCode == true)
            {

                // Set the DTSRPC API to query by Concept code
                evsSearch.getDescLogicConceptNameByCode(eVSDescLogicConceptSearchParams.getVocabularyName(),
                                                        eVSDescLogicConceptSearchParams.getSearchTerm());

                LOG.debug("calling evsSearch: getDescLogicConceptNameByCode");

                // The return should be 1 record which is a string, corresponding to the name.
                List nameList = (List) appService.evsSearch(evsSearch);
                if ( nameList != null && nameList.size() > 0)
                {
                    String searchTerm = (String) nameList.get(0);

                    // Actually, the result should be 1; so go ahead and simply pick the first one in the list
                    LOG.debug("Calling evsSearch: searchDescLogicConcepts for search term <" + searchTerm + ">" );

                    EVSQuery evsSearch2 = new EVSQueryImpl();
                    evsSearch2.searchDescLogicConcepts( eVSDescLogicConceptSearchParams.getVocabularyName(),
                                               searchTerm,
                                               eVSDescLogicConceptSearchParams.getLimit()
                                            );

                    // Perform query: Assume no data is returned
                    evsResults = (List)appService.evsSearch(evsSearch2);
                }
            }
            else
            {

                evsSearch.searchDescLogicConcepts( eVSDescLogicConceptSearchParams.getVocabularyName(),
                                           eVSDescLogicConceptSearchParams.getSearchTerm(),
                                           eVSDescLogicConceptSearchParams.getLimit()
                                        );

                LOG.debug("Calling evsSearch: searchDescLogicConcepts");

                // Perform query: Assume no data is returned
                evsResults = (List)appService.evsSearch(evsSearch);
            }

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
        catch (gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType ie)
        {
            // simply rethrow
            throw ie;
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

	public gov.nih.nci.evs.domain.MetaThesaurusConcept[] searchMetaThesaurus(gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams eVSMetaThesaurusSearchParams) throws RemoteException, gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType {
        try
        {

            LOG.debug("Inside method:searchMetaThesaurus:Testing input");

            // This will throw Exception if invalid
            validateEVSMetaThesaurusSearchParams(eVSMetaThesaurusSearchParams);

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
        catch(gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType ie)
        {
            // Just throw the exception
            throw ie;

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

	public gov.nih.nci.evs.domain.HistoryRecord[] getHistoryRecords(gov.nih.nci.cagrid.evs.service.EVSHistoryRecordsSearchParams eVSHistoryRecordsSearchParams) throws RemoteException, gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType {
        try
        {

            LOG.debug("Inside method:getHistoryRecords:Testing input");

            validateEVSHistoryRecordsSearchParams(eVSHistoryRecordsSearchParams);

            LOG.debug("Inside method:getHistoryRecords. Obtaining connection to caCORE remote instance" +
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
        catch (gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType ie)
        {
            // simply re-throw
            throw ie;
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

	public gov.nih.nci.evs.domain.MetaThesaurusConcept[] searchSourceByCode(gov.nih.nci.cagrid.evs.service.EVSSourceSearchParams eVSSourceSearchParams) throws RemoteException, gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType {
        try
        {

            LOG.debug("Inside method:searchSourceByCode:Testing input");

            // Throws exception if invalid
            validateEVSSourceSearchParams(eVSSourceSearchParams);

            LOG.debug("Inside method:searchSourceByCode. Obtaining connection to caCORE remote instance" +
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
        catch (gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType ie)
        {
            // simply rethrow
            throw ie;
        }
        catch(Exception e)
        {
            LOG.error("Exception while searching: "+ e.getMessage());
            throw new RemoteException("Catch all exception" + e.getMessage());
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////
//  helper methods
//
////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Test that the source is supported by Meta Thesaurus
     */

    private boolean isMetaSourceValid(String sourceAbbreviation)
    {
        boolean bRet = false;

        // All Sources abbreviation
        if ( EVSConstants.META_ALL_SOURCES.equals(sourceAbbreviation))
                return true;

        // Get list of Meta Sources
        try
        {
            gov.nih.nci.evs.domain.Source[] sources = getMetaSources();

            if (sources != null && sources.length >0 )
            {
                // If the source Abbrviation matches the one in the list, return True
                for (int i=0; i < sources.length; i++)
                {
                    if ( sources[i].getAbbreviation().equals(sourceAbbreviation))
                        return true;
                }
            }
        }
        catch(Exception e)
        {
            LOG.error("Exception while searching: "+ e.getMessage());
            return  bRet;
        }

        return (bRet);

    }

    /**
     * Test that the vocabulary requested by the user is supported by EVS
     */
    private boolean isVocabularyValid(String vocabularyName)
    {
        boolean bRet = false;
        // Get list of vocabulary names
        try
        {
            gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName[] vocabs = getVocabularyNames();

            if (vocabs != null && vocabs.length >0 )
            {
                // If the source Abbrviation matches the one in the list, return True
                for (int i=0; i < vocabs.length; i++)
                {
                    // Check case sensitivity
                    if ( vocabs[i].getVocabularyName().equals(vocabularyName))
                        return true;
                }
            }
        }
        catch(Exception e)
        {
            LOG.error("Exception while searching: "+ e.getMessage());
            return  bRet;
        }

        return (bRet);

    }

    /**
     * Check if the Search Term passed to <code>gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams</code>
     * refers to concept code or not
     *
     *
     */

    private boolean isSearchTermConceptCode(String searchTerm)
    {
        boolean bIsConceptCode = false;

        // Concept code starts with letter "C" and is followed by digits.
         if (searchTerm.startsWith(EVSConstants.META_CUI_PREFIX))
         {

             // The search term starts with the correct prefix (C); so now we have to check if it is really a concept code
             // or not.

            // Since strings are passed by reference, build a new string to do the parssing.
            String newSearchTerm = new String(searchTerm);
            String postFix = newSearchTerm.substring(1);
            LOG.debug("The remaining String (after removing prefix): " + postFix);

             if ( postFix != null && postFix.length() > 0)
             {
                 // Check if all the characters are digits.
                 for (int i=0; i < postFix.length(); i++)
                 {
                    char ch = postFix.charAt(i);
                    if (Character.isDigit(ch))
                    {
                        // continue
                    }
                    else
                    {
                        LOG.debug("The invalid digit in the Search term is: <" + ch + ">");
                        // Break from the for loop; it is not a concept code!
                        break;
                    }
                 }

                 // IF all the characters are digits, then it is a concept code!
                 bIsConceptCode = true;
             }
         }
        return (bIsConceptCode);
    }

    /**
     * Test that <code>gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams</code> input used to
     * search for EVS Meta Thesaurus concepts <code></code> from EVS is valid
     * @param eVSMetaThesaurusSearchParams instance of <code>gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams</code> class
     */

    private void validateEVSMetaThesaurusSearchParams(gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams eVSMetaThesaurusSearchParams)
            throws gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType
    {

        // Check input validity
        if (eVSMetaThesaurusSearchParams == null )
        {
            // Throw appropriate exception; i.e. invalid inputs.
            LOG.warn("Invalid inputs: EVSMetaThesaurusSearchParams object cannot be NULL");
            InvalidInputExceptionType fault = new InvalidInputExceptionType();
            fault.setFaultString("Invalid inputs: EVSMetaThesaurusSearchParams object cannot be NULL");
            throw fault;
        }

        // Search Term: cannot be empty

        if ( eVSMetaThesaurusSearchParams.getSearchTerm() == null ||
             eVSMetaThesaurusSearchParams.getSearchTerm().length() == 0)
        {
            // Throw  appropriate exception; invalid attribute: search term should be specified
            LOG.warn("Invalid inputs: EVSMetaThesaurusSearchParams attribute <searchTerm> has to be specified");
            InvalidInputExceptionType fault = new InvalidInputExceptionType();
            fault.setFaultString("Invalid inputs: EVSMetaThesaurusSearchParams attribute <searchTerm> has to be specified");
            throw fault;
        }
        else
        {
            // Check that the Search term is valid (for CUI only)
            if (eVSMetaThesaurusSearchParams.isCui())
            {
                // Test that the search term is constrained by the rules!
                if (eVSMetaThesaurusSearchParams.getSearchTerm().length() != EVSConstants.META_CUI_MAX_LENGTH ||
                    !eVSMetaThesaurusSearchParams.getSearchTerm().startsWith(EVSConstants.META_CUI_PREFIX))
                {
                    // Throw appropriate exception: Search Term has to follow rules when it is specified to be CUI.
                    // Max Length:8
                    // Starts with:C
                    LOG.warn("Invalid inputs<searchTerm>: EVSMetaThesaurusSearchParams attribute " +
                            "<" + eVSMetaThesaurusSearchParams.getSearchTerm() + ">" + " does not follow EVS" +
                            "rules (either starting with C or maximum length of  " + EVSConstants.META_CUI_MAX_LENGTH );

                    InvalidInputExceptionType fault = new InvalidInputExceptionType();
                    fault.setFaultString("Invalid inputs<searchTerm>: EVSMetaThesaurusSearchParams attribute " +
                            "<" + eVSMetaThesaurusSearchParams.getSearchTerm() + ">" + " does not follow EVS" +
                            "rules (either starting with C or maximum length of  " + EVSConstants.META_CUI_MAX_LENGTH);
                    throw fault;
                }
            }
        }

        // Limit: Should be a positive integer
        if (eVSMetaThesaurusSearchParams.getLimit() <= 0)
        {
            // Throw appropriate exception; i.e. invalid input: limit has to be greater than zero
            LOG.warn("Invalid inputs: EVSMetaThesaurusSearchParams attribute <limit> has to be " +
                    "greater than zero");

            InvalidInputExceptionType fault = new InvalidInputExceptionType();
            fault.setFaultString("Invalid inputs: EVSMetaThesaurusSearchParams attribute <limit> has to be " +
                    "greater than zero");

            throw fault;

        }

        //  Check that the  source is correct!
        if (eVSMetaThesaurusSearchParams.getSource() == null ||
            !isMetaSourceValid(eVSMetaThesaurusSearchParams.getSource()))
        {
            //Throw appropriate exception; i.e. invalid Source Abbreviation used
            LOG.warn("Invalid inputs<source>: EVSMetaThesaurusSearchParams  attribute " +
                    "<" + eVSMetaThesaurusSearchParams.getSource() + ">" + " is not supported " +
                    "by EVS API");

            InvalidInputExceptionType fault = new InvalidInputExceptionType();
            fault.setFaultString("Invalid inputs<source>: EVSMetaThesaurusSearchParams  attribute " +
                    "<" + eVSMetaThesaurusSearchParams.getSource() + ">" + " is not supported " +
                    "by EVS API");

            throw fault;
        }

        return;
    }

    /**
     *  This method tests the validity of the inputs to the API <code>searchSourceByCode</code>
     *
     */
    private void validateEVSSourceSearchParams(gov.nih.nci.cagrid.evs.service.EVSSourceSearchParams eVSSourceSearchParams)
            throws gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType
    {

        // Check input validity
        if (eVSSourceSearchParams == null )
        {
            // Throw appropriate exception; i.e. invalid inputs.
            LOG.warn("Invalid inputs: EVSSourceSearchParams object cannot be NULL");
            InvalidInputExceptionType fault = new InvalidInputExceptionType();
            fault.setFaultString("Invalid inputs: EVSSourceSearchParams object cannot be NULL");
            throw fault;
        }

        // The source abbreviation has to be valid Source abbreviation. The "*" All sources is not valid!
        if (eVSSourceSearchParams.getSourceAbbreviation() == null ||
            EVSConstants.META_ALL_SOURCES.equals(eVSSourceSearchParams.getSourceAbbreviation()) ||
            !isMetaSourceValid(eVSSourceSearchParams.getSourceAbbreviation()))
        {
            //Throw appropriate exception; i.e. invalid Source Abbreviation used
            LOG.warn("Invalid inputs<sourceAbbreviation>: EVSSourceSearchParams  attribute " +
                    "<" + eVSSourceSearchParams.getSourceAbbreviation() + ">" + " is not supported " +
                    "by EVS API");

            InvalidInputExceptionType fault = new InvalidInputExceptionType();

            fault.setFaultString("Invalid inputs<sourceAbbreviation>: EVSSourceSearchParams  attribute " +
                    "<" + eVSSourceSearchParams.getSourceAbbreviation() + ">" + " is not supported " +
                    "by EVS API");

            throw fault;

        }

        // The Atom code cannot be null or "NOCODE"
        if (eVSSourceSearchParams.getCode() == null ||
            eVSSourceSearchParams.getCode().length() == 0 ||
            EVSConstants.ATOM_NOCODE_IDENTIFIER.equals(eVSSourceSearchParams.getCode()))
        {

            // Throw appropriate exception; i.e. invalid code
            LOG.warn("Invalid inputs<code>: EVSSourceSearchParams  attribute " +
                    "<" + eVSSourceSearchParams.getCode() + ">" + " is not valid ");

            InvalidInputExceptionType fault = new InvalidInputExceptionType();

            fault.setFaultString("Invalid inputs<code>: EVSSourceSearchParams  attribute " +
                    "<" + eVSSourceSearchParams.getCode() + ">" + " is not valid ");

            throw fault;

        }
        return;

    }

    /**
     *
     */
    private void validateEVSDescLogicConceptSearchParams(gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams eVSDescLogicConceptSearchParams)
            throws gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType
    {

        // Check input validity
        if (eVSDescLogicConceptSearchParams == null )
        {
            // Throw appropriate exception; i.e. invalid inputs.
            LOG.warn("Invalid inputs: EVSDescLogicConceptSearchParams object cannot be NULL");
            InvalidInputExceptionType fault = new InvalidInputExceptionType();
            fault.setFaultString("Invalid inputs: EVSDescLogicConceptSearchParams object cannot be NULL");
            throw fault;

        }

        // The limit has to be greater than zero
        if (eVSDescLogicConceptSearchParams.getLimit() <= 0)
        {
            // Throw appropriate exception; i.e. invalid input: limit has to be greater than zero
            LOG.warn("Invalid inputs: eVSDescLogicConceptSearchParams attribute <limit> has to be " +
                    "greater than zero");

            InvalidInputExceptionType fault = new InvalidInputExceptionType();
            fault.setFaultString("Invalid inputs: eVSDescLogicConceptSearchParams attribute <limit> has to be " +
                    "greater than zero");
            throw fault;

        }

        // The vocabulary name has to be valid
        if ( eVSDescLogicConceptSearchParams.getVocabularyName() == null ||
             eVSDescLogicConceptSearchParams.getVocabularyName().length() == 0 ||
             !isVocabularyValid(eVSDescLogicConceptSearchParams.getVocabularyName()))
        {
            //Throw appropriate exception; i.e. invalid Source Abbreviation used
            LOG.warn("Invalid inputs<vocabularyName>: EVSDescLogicConceptSearchParams  attribute " +
                    "<" + eVSDescLogicConceptSearchParams.getVocabularyName() + ">" + " is not supported " +
                    "by EVS API");
            InvalidInputExceptionType fault = new InvalidInputExceptionType();

            fault.setFaultString("Invalid inputs<vocabularyName>: EVSDescLogicConceptSearchParams  attribute " +
                    "<" + eVSDescLogicConceptSearchParams.getVocabularyName() + ">" + " is not supported " +
                    "by EVS API");

            throw fault;

        }

        return;

    }

    /**
     *  This method tests that the inputs  <code>bov.nih.nci.cagrid.evs.service.EVSHistoryRecordsSearchParams</code>
     * to the API <code>getHistoryRecords</code> are valid
     *
     */

    private void validateEVSHistoryRecordsSearchParams(gov.nih.nci.cagrid.evs.service.EVSHistoryRecordsSearchParams eVSHistoryRecordsSearchParams)
            throws gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType
    {

        // Check input validity
        if (eVSHistoryRecordsSearchParams == null )
        {
            // Throw appropriate exception; i.e. invalid inputs.
            LOG.warn("Invalid inputs: EVSHistoryRecordsSearchParams object cannot be NULL");
            InvalidInputExceptionType fault = new InvalidInputExceptionType();
            fault.setFaultString("Invalid inputs: EVSHistoryRecordsSearchParams object cannot be NULL");
            throw fault;
        }

        // Check that the concept code is valid
        if (eVSHistoryRecordsSearchParams.getConceptCode() == null||
            eVSHistoryRecordsSearchParams.getConceptCode().length() == 0 ||
            !eVSHistoryRecordsSearchParams.getConceptCode().startsWith(EVSConstants.META_CUI_PREFIX))
        {
            LOG.warn("Invalid inputs<conceptCode>: EVSHistoryRecordsSearchParams attribute " +
                    "<" + eVSHistoryRecordsSearchParams.getConceptCode() + ">" + " does not follow EVS" +
                    "rule of starting with C");

            InvalidInputExceptionType fault = new InvalidInputExceptionType();
            fault.setFaultString("Invalid inputs<conceptCode>: EVSHistoryRecordsSearchParams attribute " +
                    "<" + eVSHistoryRecordsSearchParams.getConceptCode() + ">" + " does not follow EVS" +
                    "rule of starting with C");
            throw fault;

        }

        // Check that the vocabulay Name is valid
        if ( eVSHistoryRecordsSearchParams.getVocabularyName() == null ||
             eVSHistoryRecordsSearchParams.getVocabularyName().length() == 0 ||
             !isVocabularyValid(eVSHistoryRecordsSearchParams.getVocabularyName()))
        {
            //Throw appropriate exception; i.e. invalid Source Abbreviation used
            LOG.warn("Invalid inputs<vocabularyName>: EVSHistoryRecordsSearchParams  attribute " +
                    "<" + eVSHistoryRecordsSearchParams.getVocabularyName() + ">" + " is not supported " +
                    "by EVS API");

            InvalidInputExceptionType fault = new InvalidInputExceptionType();

            fault.setFaultString("Invalid inputs<vocabularyName>: EVSHistoryRecordsSearchParams  attribute " +
                    "<" + eVSHistoryRecordsSearchParams.getVocabularyName() + ">" + " is not supported " +
                    "by EVS API");

            throw fault;

        }

        return;

    }

}

