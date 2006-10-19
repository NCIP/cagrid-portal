/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.evsgridservice.client.EVSGridServiceClient;
import gov.nih.nci.cagrid.evs.service.*;
import gov.nih.nci.evs.domain.Source;
import gov.nih.nci.evs.domain.MetaThesaurusConcept;
import gov.nih.nci.evs.domain.DescLogicConcept;
import gov.nih.nci.evs.domain.HistoryRecord;

import java.io.File;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

/**
 * This step checks the EVS grid service for proper functioning by calling into the client
 * @author Avinash Shanbhag
 */
public class EvsCheckServiceStep
	extends Step
{
	private EndpointReferenceType endpoint;
	private File extractFile;
	
	public EvsCheckServiceStep(int port) 
		throws MalformedURIException
	{
		this(new EndpointReferenceType(new Address("http://localhost:" + port + "/wsrf/services/cagrid/EVSGridService")));
	}

	public EvsCheckServiceStep(EndpointReferenceType endpoint)
	{
		super();
		
		this.endpoint = endpoint;
	}
	
	public void runStep() 
		throws Exception
	{
        // Test getMetaSources
        testGetMetaSources();

        // Test getVocabularyNames
        testGetVocabularyNames();

        testSearchMetaThesaurusConcept();

        testGetHistoryRecords();

        testSearchSourceByCode();


    }

    /**
     * Test the method getMetaSources
     *
     */

    public void testGetMetaSources()
    throws Exception
    {
        System.out.println("Testing method:getMetaSources");

        EVSGridServiceClient client = new EVSGridServiceClient(endpoint);
        Source[] sources = client.getMetaSources();
        assertNotNull(sources);

        // Get the number of sources (if they remain constant) and assert
        assertTrue( sources.length > 0);

        if ( sources != null && sources.length > 0)
        {
            for (int i=0; i < sources.length; i++)
            {
                Source source = sources[i];
                assertNotNull("Source object is null", source);
                assertNotNull("Attribute:Abbreviation is null", source.getAbbreviation());
                assertNotNull("Attribute;Description is null", source.getDescription());
            }
        }
   }

    /**
     * Test getVocabularyNames
     */

    public void testGetVocabularyNames()
    throws Exception
    {
        System.out.println("Testing method:getVocabularyNames");

        EVSGridServiceClient client = new EVSGridServiceClient(endpoint);
        DescLogicConceptVocabularyName[] desclogicConceptVocabularyNames = client.getVocabularyNames();
        assertNotNull(desclogicConceptVocabularyNames);

        assertTrue(desclogicConceptVocabularyNames.length > 0);

        if ( desclogicConceptVocabularyNames != null && desclogicConceptVocabularyNames.length >0 )
        {
            for (int i=0; i < desclogicConceptVocabularyNames.length;i++)
            {
                DescLogicConceptVocabularyName vocab = desclogicConceptVocabularyNames[i];
                assertNotNull("Vocabulary Object is null", vocab);
                assertNotNull("Attribute:VocabularyName is null", vocab.getVocabularyName());
            }
        }

    }

    /**
     *
     */
    public void testSearchMetaThesaurusConcept()
    throws Exception
    {
        System.out.println("Testing:searchMetaThesaurusConcept!");

        EVSMetaThesaurusSearchParams evsMetaThesaurusSearchParam = new EVSMetaThesaurusSearchParams();
        evsMetaThesaurusSearchParam.setSearchTerm("lung");
        evsMetaThesaurusSearchParam.setLimit(2);
        evsMetaThesaurusSearchParam.setSource("*");
        evsMetaThesaurusSearchParam.setCui(false);
        evsMetaThesaurusSearchParam.setShortResponse(false);
        evsMetaThesaurusSearchParam.setScore(false);


        EVSGridServiceClient client = new EVSGridServiceClient(endpoint);
        MetaThesaurusConcept[] metaConcept = client.searchMetaThesaurus(evsMetaThesaurusSearchParam);

        assertNotNull("searchMetaThesaurus returned Null", metaConcept);
        assertTrue( metaConcept.length > 0);

        if (metaConcept != null && metaConcept.length > 0)
        {
            for (int i=0; i < metaConcept.length; i++)
            {
                MetaThesaurusConcept meta = metaConcept[i];
                assertNotNull("MetaThesaurusConcept object is Null", meta);
            }
        }
    }

    /**
     * test
     */

    public void testGetDescriptionLogicConcept()
    throws Exception
    {
        System.out.println("testing:searchDescLogicConcept!");


        EVSDescLogicConceptSearchParams  evsSearchParams = new EVSDescLogicConceptSearchParams();
        evsSearchParams.setVocabularyName("NCI_Thesaurus");
        evsSearchParams.setSearchTerm("blood*");
        evsSearchParams.setLimit(10);

        EVSGridServiceClient client = new EVSGridServiceClient(endpoint);
        DescLogicConcept[] descLogicConcepts = client.searchDescLogicConcept(evsSearchParams);

        assertNotNull("searchDescLogicConcept returned Null", descLogicConcepts);
        assertTrue( descLogicConcepts.length > 0);

        if (descLogicConcepts != null && descLogicConcepts.length > 0)
        {
                for (int i=0; i < descLogicConcepts.length; i++)
                {
                    DescLogicConcept descConcept = descLogicConcepts[i];
                    assertNotNull("DescLogicConcept object is Null", descConcept);
                }
        }
    }

    /**
     * test
     */
    public void testGetHistoryRecords()
    throws Exception
    {
        System.out.println("testing:getHistoryRecords");

        EVSHistoryRecordsSearchParams  evsHistoryParams = new EVSHistoryRecordsSearchParams();
        evsHistoryParams.setVocabularyName("NCI_Thesaurus");
        evsHistoryParams.setConceptCode("C16612");

        EVSGridServiceClient client = new EVSGridServiceClient(endpoint);
        HistoryRecord[] historys = client.getHistoryRecords(evsHistoryParams);

        assertNotNull("getHistoryRecords returned Null", historys);
        assertTrue( historys.length > 0);

        if ( historys.length > 0)
        {
            for (int i=0; i < historys.length; i++)
            {
                HistoryRecord history = historys[i];
                assertNotNull("HistoryRecord object is Null", history);
            }
        }

    }

    /**
     * Test
     */
    public void testSearchSourceByCode()
    throws Exception
    {

        System.out.println("testing:searchSourceByCode!");              


        EVSSourceSearchParams evsSourceParam = new EVSSourceSearchParams();

        // Atom: code = 10834
        // Source Abbreviation: LNC213

        evsSourceParam.setCode("0000001800");
        evsSourceParam.setSourceAbbreviation("AOD2000");

        EVSGridServiceClient client = new EVSGridServiceClient(endpoint);
        MetaThesaurusConcept[] metaConcept2 = client.searchSourceByCode(evsSourceParam);

        assertNotNull("searchSourceByCode returned Null", metaConcept2);
        assertTrue( metaConcept2.length > 0);


        if (metaConcept2 != null && metaConcept2.length > 0)
        {
            for (int i=0; i < metaConcept2.length; i++)
            {
                MetaThesaurusConcept meta = metaConcept2[i];
                assertNotNull("MetaThesaurusConcept object is Null", meta);
            }
        }
    }

}
