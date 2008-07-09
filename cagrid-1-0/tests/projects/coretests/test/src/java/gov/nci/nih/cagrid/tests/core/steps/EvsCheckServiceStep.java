/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.evs.service.DescLogicConceptVocabularyName;
import gov.nih.nci.cagrid.evs.service.EVSDescLogicConceptSearchParams;
import gov.nih.nci.cagrid.evs.service.EVSHistoryRecordsSearchParams;
import gov.nih.nci.cagrid.evs.service.EVSMetaThesaurusSearchParams;
import gov.nih.nci.cagrid.evs.service.EVSSourceSearchParams;
import gov.nih.nci.cagrid.evsgridservice.client.EVSGridServiceClient;
import gov.nih.nci.cagrid.testing.system.haste.Step;
import gov.nih.nci.evs.domain.Association;
import gov.nih.nci.evs.domain.Atom;
import gov.nih.nci.evs.domain.Definition;
import gov.nih.nci.evs.domain.DescLogicConcept;
import gov.nih.nci.evs.domain.EdgeProperties;
import gov.nih.nci.evs.domain.HistoryRecord;
import gov.nih.nci.evs.domain.MetaThesaurusConcept;
import gov.nih.nci.evs.domain.Property;
import gov.nih.nci.evs.domain.Qualifier;
import gov.nih.nci.evs.domain.Role;
import gov.nih.nci.evs.domain.SemanticType;
import gov.nih.nci.evs.domain.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.axis.message.addressing.EndpointReferenceType;


/**
 * This step checks the EVS grid service for proper functioning by calling into
 * the client
 * 
 * @author Avinash Shanbhag
 */
public class EvsCheckServiceStep extends Step {
    public static final String SERVICE_DEPLOYMENT_PATH = "cagrid/EVSGridService";
    private EndpointReferenceType endpoint;


    public EvsCheckServiceStep(EndpointReferenceType endpoint) {
        super();
        this.endpoint = endpoint;
    }


    @Override
    public void runStep() throws Exception {

        // There are two main parts for system testing. One is to test the
        // Metathesaurus and other is to
        // test the NCI Thesaurus.
        testMetaThesaurus();

        testSearchMetaThesaurusConcept();

        // test obtaining history record
        // NOTE: this no longer works because of EVS backend!!!
        // testGetHistoryRecords();

        // test searching meta thesaurus using code
        testSearchSourceByCode();

        // Test Searching for DescLogicConcept
        testGetDescriptionLogicConcept();

        testGetVocabularyNames();

        testSearchDescLogicConcept();

    }


    /**
     * This method will use some common search terms that are relevant for
     * cancer and try to navigate the metathesaurus and in the process test the
     * relevant APIs.
     * 
     * @throws Exception
     */

    public void testMetaThesaurus() throws Exception {
        // Search term = Breast
        String searchTerms[] = new String[]{"Breast"};
        EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);

        // First, get a list of valid metasources
        Source[] sources = client.getMetaSources();
        assertNotNull(sources);

        // Get the list
        List sourceList = Arrays.asList(sources);

        EVSMetaThesaurusSearchParams evsMetaThesaurusSearchParam = new EVSMetaThesaurusSearchParams();

        evsMetaThesaurusSearchParam.setLimit(100);
        evsMetaThesaurusSearchParam.setSource("*");
        evsMetaThesaurusSearchParam.setCui(false);
        evsMetaThesaurusSearchParam.setShortResponse(false);
        evsMetaThesaurusSearchParam.setScore(false);

        for (int i = 0; i < searchTerms.length; i++) {
            // Check for valid Metathesarus concept information for the search
            // term
            evsMetaThesaurusSearchParam.setSearchTerm(searchTerms[i]);
            MetaThesaurusConcept metaConcept = testSearchMetaThesaurusConcept(evsMetaThesaurusSearchParam);
            assertNotNull("Meta Thesaurus concept for " + searchTerms[i] + " is null!", metaConcept);

            // Check that the CUI and name is not null
            assertNotNull("CUI is null", metaConcept.getCui());

            // Check that the name is not null
            assertNotNull("Name is not null", metaConcept.getName());

            // Test that the meta thesaurus concept based on CUI returns same
            // result!
            testMetaConceptBasedOnCui(metaConcept);

            // Get the source Object and make sure that the source corresponds
            // to the one in the list
            testMetaThesaurusSourceInfo(metaConcept, sourceList);

            // Get Atom Collection and then use the Atom:code and
            // Source:abbreviation to determine if the Meta Thesaurus concept
            // is valid
            testAtomInfo(metaConcept, sourceList);

            // Test the synonymcollection attribute
            ArrayList synonymCollection = metaConcept.getSynonymCollection();
            assertNotNull("Synonym collection is null", synonymCollection);

            for (int l = 0; l < synonymCollection.size(); l++) {
                String synonym = (String) synonymCollection.get(l);
                assertNotNull("Synonym is  null", synonym);
            }

            // Test the definition collection
            ArrayList definitionCollection = metaConcept.getDefinitionCollection();
            assertNotNull("Definition collection is null", definitionCollection);

            // Definition collection could be empty (i.e. 0 records). So, cannot
            // test for assertion on the size of the
            // records

            for (int k = 0; k < definitionCollection.size(); k++) {
                Definition def = (Definition) definitionCollection.get(k);
                assertNotNull("Definition is null", def);
            }

            // SemanticTypeCollection
            ArrayList semanticTypeCollection = metaConcept.getSemanticTypeCollection();
            assertNotNull("Semantic Type collection is null", semanticTypeCollection);

            for (int m = 0; m < semanticTypeCollection.size(); m++) {
                SemanticType semType = (SemanticType) semanticTypeCollection.get(m);
                assertNotNull("SemanticType is null", semType);
            }

        }
    }


    private void testAtomInfo(MetaThesaurusConcept metaConcept, List sourceList) throws Exception {
        ArrayList atomArray = metaConcept.getAtomCollection();
        assertNotNull("MetaThesaurus atom collection is null", atomArray);

        EVSSourceSearchParams evsSourceParam = new EVSSourceSearchParams();
        for (int j = 0; j < atomArray.size(); j++) {
            Atom atom = (Atom) atomArray.get(j);

            assertNotNull("atom is null!", atom);
            assertNotNull("atom:code is null!", atom.getCode());
            assertNotNull("atom:lui is null!", atom.getLui());
            assertNotNull("atom:Name is null!", atom.getName());
            assertNotNull("atom:Origin is null!", atom.getOrigin());
            assertNotNull("atom:Source is null!", atom.getSource());

            // Get the source name and check if it is valid
            Source source = atom.getSource();
            // Check and make sure that the Source object is part of the Source
            // collection
            assertTrue("Source (Abbr): " + source.getAbbreviation() + " is not present in metathesaurus!", sourceList
                .contains(source));

            // Atom:code can be empty which is defined by string:NOCODE. In that
            // case, the API will not get the
            // valid MetaThesaurus concept
            evsSourceParam.setCode(atom.getCode());
            evsSourceParam.setSourceAbbreviation(source.getAbbreviation());

            // Get Meta Thesaurus concept based on the EVSourceParam
            MetaThesaurusConcept metaConcept2 = testSearchSourceByCode(evsSourceParam);

            if (atom.getCode() != null && atom.getCode().length() > 0 && !atom.getCode().equalsIgnoreCase("NOCODE")) {
                assertNotNull("MetaThesaurusConcept for the give EVS Source abbreviation: " + source.getAbbreviation()
                    + " and atom code: " + atom.getCode() + " is null", metaConcept2);
            } else {
                // Assert that the value returned is null
                assertNull("Meta Thesaurus concept is not null", metaConcept2);
            }
        }
    }


    /**
     * Test that the Source for the Meta Thesaurus Concept is art of the Source
     * collection for EVS Meta Thesaurus
     * 
     * @param metaConcept
     * @param sourceList
     */

    private void testMetaThesaurusSourceInfo(MetaThesaurusConcept metaConcept, List sourceList) {
        ArrayList sourceArray = metaConcept.getSourceCollection();
        assertNotNull("MetaThesaurus Source collection is null", sourceArray);
        for (int j = 0; j < sourceArray.size(); j++) {
            Source source = (Source) sourceArray.get(j);
            // Check and make sure that the Source object is part of the Source
            // collection
            assertTrue("Source (Abbr): " + source.getAbbreviation() + " is not present in metathesaurus!", sourceList
                .contains(source));
        }
    }


    /**
     * Uses the CUI as the input to obtain EVS Meta Thesaurus concept and then
     * compares it with the object obtained using the Search term to make sure
     * that they are identical.
     * 
     * @param metaConcept
     *            The <code>MetaThesaurusConcept</code> instance used to
     *            compare
     * @throws Exception
     */
    private void testMetaConceptBasedOnCui(MetaThesaurusConcept metaConcept) throws Exception {
        // Test if the same metathesaurus concept is available via API call
        // using the concept code
        EVSMetaThesaurusSearchParams evsMetaThesaurusSearchParams2 = new EVSMetaThesaurusSearchParams();

        evsMetaThesaurusSearchParams2.setCui(true);
        evsMetaThesaurusSearchParams2.setShortResponse(false);
        evsMetaThesaurusSearchParams2.setScore(false);
        evsMetaThesaurusSearchParams2.setLimit(100);
        evsMetaThesaurusSearchParams2.setSource("*");

        evsMetaThesaurusSearchParams2.setSearchTerm(metaConcept.getCui());
        MetaThesaurusConcept metaConceptFromCUI = testSearchMetaThesaurusConcept(evsMetaThesaurusSearchParams2);

        assertEquals("EVS metadata APIs using CUI and Search name return different results", metaConcept.getName(),
            metaConceptFromCUI.getName());
    }


    /**
     * Test the method getMetaSources
     */

    public void testGetMetaSources() throws Exception {
        System.out.println("Testing method:getMetaSources");

        EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);
        Source[] sources = client.getMetaSources();
        assertNotNull(sources);

        // Get the number of sources (if they remain constant) and assert
        assertTrue(sources.length > 0);

        if (sources != null && sources.length > 0) {
            for (int i = 0; i < sources.length; i++) {
                Source source = sources[i];
                System.out.println("Source[" + i + "]=Abbr:" + source.getAbbreviation() + "desc: "
                    + source.getDescription());

                assertNotNull("Source object is null", source);
                assertNotNull("Attribute:Abbreviation is null", source.getAbbreviation());
                assertNotNull("Attribute;Description is null", source.getDescription());
            }
        }
    }


    /**
     * Test getVocabularyNames
     */

    public void testGetVocabularyNames() throws Exception {
        System.out.println("Testing method:getVocabularyNames");

        EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);
        DescLogicConceptVocabularyName[] desclogicConceptVocabularyNames = client.getVocabularyNames();
        assertNotNull(desclogicConceptVocabularyNames);

        assertTrue(desclogicConceptVocabularyNames.length > 0);

        if (desclogicConceptVocabularyNames != null && desclogicConceptVocabularyNames.length > 0) {
            for (int i = 0; i < desclogicConceptVocabularyNames.length; i++) {
                DescLogicConceptVocabularyName vocab = desclogicConceptVocabularyNames[i];
                assertNotNull("Vocabulary Object is null", vocab);
                assertNotNull("Attribute:VocabularyName is null", vocab.getVocabularyName());
            }
        }

    }


    /**
     * Search Meta thesaurus based on Search param provided by
     * class:EVSMetaThesaurusSearchParams
     * 
     * @param metaParams
     * @throws Exception
     */
    public MetaThesaurusConcept testSearchMetaThesaurusConcept(EVSMetaThesaurusSearchParams metaParams)
        throws Exception {
        System.out.println("Testing:searchMetaThesaurusConcep based on valid EVSMetaThesaurusSearchParams !");

        EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);
        MetaThesaurusConcept[] metaConcept = client.searchMetaThesaurus(metaParams);

        assertNotNull("searchMetaThesaurus returned Null", metaConcept);

        assertTrue("Result is over the limit specified by the EVSMetaThesaurusSearchParams "
            + "searc limit attribute: " + metaParams.getLimit(),
            (metaConcept.length > 0 && metaConcept.length <= metaParams.getLimit()));

        if (metaConcept != null && metaConcept.length > 0) {
            for (int i = 0; i < metaConcept.length; i++) {
                MetaThesaurusConcept meta = metaConcept[i];
                assertNotNull("MetaThesaurusConcept object is Null", meta);
            }
        }
        return ((metaConcept != null) ? metaConcept[0] : null);
    }


    /**
     *
     */
    public void testSearchMetaThesaurusConcept() throws Exception {
        System.out.println("Testing:searchMetaThesaurusConcept!");

        // Test passing incorrect inputs.
        EVSMetaThesaurusSearchParams evsMetaThesaurusSearchParam = new EVSMetaThesaurusSearchParams();
        evsMetaThesaurusSearchParam.setSearchTerm("");
        evsMetaThesaurusSearchParam.setLimit(100);
        evsMetaThesaurusSearchParam.setSource("*");
        evsMetaThesaurusSearchParam.setCui(true);
        evsMetaThesaurusSearchParam.setShortResponse(false);
        evsMetaThesaurusSearchParam.setScore(false);

        boolean bInvalid = false;

        EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);
        try {
            MetaThesaurusConcept[] metaConcept = client.searchMetaThesaurus(evsMetaThesaurusSearchParam);
        } catch (gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType e) {
            // gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType
            bInvalid = true;
            System.out.println("Error message: " + e.getFaultString());
        }

        assertTrue("Invalid input did not "
            + "throw <gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType> exception", bInvalid);

    }


    /**
     * test <code>searchDescLogicConcept</code> api to obtain Description
     * logic Concept object
     */

    public void testGetDescriptionLogicConcept() throws Exception {

        String searchTerms[] = new String[]{"Blood*", "C43723"};

        for (int count = 0; count < searchTerms.length; count++) {
            System.out.println("testing:searchDescLogicConcept!");
            EVSDescLogicConceptSearchParams evsSearchParams = new EVSDescLogicConceptSearchParams();
            evsSearchParams.setVocabularyName("NCI_Thesaurus");
            evsSearchParams.setSearchTerm(searchTerms[count]);
            evsSearchParams.setLimit(100);

            EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);
            DescLogicConcept[] descLogicConcepts = client.searchDescLogicConcept(evsSearchParams);

            assertNotNull("searchDescLogicConcept returned Null", descLogicConcepts);
            assertTrue(descLogicConcepts.length > 0);

            if (descLogicConcepts != null && descLogicConcepts.length > 0) {
                for (int i = 0; i < descLogicConcepts.length; i++) {
                    DescLogicConcept descConcept = descLogicConcepts[i];
                    assertNotNull("DescLogicConcept object is Null", descConcept);

                    // name
                    assertNotNull("Name is null", descConcept.getName());
                    // namespaceid
                    assertNotNull("NamespaceID is null", descConcept.getNamespaceId());
                    // code
                    assertNotNull("Code is null", descConcept.getCode());

                    // check attributes - semanticTypeVector
                    Vector semanticType = descConcept.getSemanticTypeVector();

                    // Check what the vector elements are?
                    assertNotNull("SemanticTypeVecor is null", semanticType);

                    // Check properties
                    checkProperties(descConcept);

                    // Check roles
                    checkRoles(descConcept);

                    // Check inverse roles
                    checkInverseRoles(descConcept);

                    // Check Edge Properties - Edge Properties are not populated
                    // by EVS grid service
                    // checkEdgeProperties(descConcept);

                    // Check associated collection
                    checkAssociations(descConcept);

                    // Check inverse assoociation collection
                    checkInverseAssociations(descConcept);

                }
            }
        }
    }


    private void checkInverseAssociations(DescLogicConcept descConcept) {
        Collection inverseAssocCollection = descConcept.getInverseAssociationCollection();

        for (Iterator it = inverseAssocCollection.iterator(); it.hasNext();) {
            System.out.println("Inverse Association Collection: " + it.next().getClass().toString());
        }
    }


    private void checkAssociations(DescLogicConcept descConcept) {
        Collection associatedCollection = descConcept.getAssociationCollection();
        for (Iterator it = associatedCollection.iterator(); it.hasNext();) {
            Association association = (Association) it.next();

            if (association != null) {
                assertNotNull("Association name is null", association.getName());
                assertNotNull("Association value is null", association.getValue());
                // Qualifier will be always null for caCORE 3.1
            }

        }
    }


    /**
     * The <code>EdgeProperties</code> are NOT populated by the API. The class
     * is only used for navingating the tree and is not needed for by the EVS
     * Grid service
     * 
     * @param descConcept
     */
    private void checkEdgeProperties(DescLogicConcept descConcept) {
        EdgeProperties edge = descConcept.getEdgeProperties();
        if (edge != null) {
            // assertNotNull("Name is null!", edge.getName());
            if (edge.getLinks() != null) {
                for (Iterator it = edge.getLinks().iterator(); it.hasNext();) {
                    System.out.println("Link Class : " + it.next().getClass().toString());
                }
            }
        }
    }


    private void checkRoles(DescLogicConcept descConcept) {
        Collection roleCollection = descConcept.getRoleCollection();

        if (roleCollection != null) {
            for (Iterator it = roleCollection.iterator(); it.hasNext();) {
                Role role = (Role) it.next();
                assertNotNull("Role is null", role);
                assertNotNull("Role Name is null", role.getName());
                assertNotNull("Role Value is null", role.getValue());
            }
        }
    }


    private void checkInverseRoles(DescLogicConcept descConcept) {
        Collection roleCollection = descConcept.getInverseRoleCollection();

        if (roleCollection != null) {
            for (Iterator it = roleCollection.iterator(); it.hasNext();) {
                Role role = (Role) it.next();
                assertNotNull("Role is null", role);
                assertNotNull("Role Name is null", role.getName());
                assertNotNull("Role Value is null", role.getValue());
            }
        }
    }


    private void checkProperties(DescLogicConcept descConcept) {
        Vector propCollections = descConcept.getPropertyCollection();
        for (int j = 0; j < propCollections.size(); j++) {
            // Make sure that they are not null
            Property prop = (Property) propCollections.get(j);
            assertNotNull("Property value is null", prop);

            Vector qualCollection = prop.getQualifierCollection();
            for (int k = 0; k < qualCollection.size(); k++) {
                Qualifier qual = (Qualifier) qualCollection.get(k);
                assertNotNull("Qualifier is null", qual);
            }

        }
    }


    /**
     * test
     */
    public void testGetHistoryRecords() throws Exception {
        System.out.println("testing:getHistoryRecords");

        EVSHistoryRecordsSearchParams evsHistoryParams = new EVSHistoryRecordsSearchParams();
        evsHistoryParams.setVocabularyName("NCI_Thesaurus");
        evsHistoryParams.setConceptCode("C16612");

        EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);
        HistoryRecord[] historys = client.getHistoryRecords(evsHistoryParams);

        assertNotNull("getHistoryRecords returned Null", historys);
        assertTrue(historys.length > 0);

        if (historys != null && historys.length > 0) {
            for (int i = 0; i < historys.length; i++) {
                HistoryRecord history = historys[i];
                assertNotNull("HistoryRecord object is Null", history);
            }
        }

        // Test incorrect values

        boolean bInvalid = false;

        try {
            evsHistoryParams.setVocabularyName("nci_Thesaurus");
            evsHistoryParams.setConceptCode("C16612");
            historys = client.getHistoryRecords(evsHistoryParams);
        } catch (gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType e) {
            bInvalid = true;

            System.out.println("Error message: " + e.getFaultString());

        }

        assertTrue("Invalid input did not "
            + "throw <gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType> exception", bInvalid);

    }


    /**
     * Test
     */
    public void testSearchSourceByCode() throws Exception {

        System.out.println("testing:searchSourceByCode!");

        EVSSourceSearchParams evsSourceParam = new EVSSourceSearchParams();

        // Atom: code = 10834
        // Source Abbreviation:AOD2000
        evsSourceParam.setCode("0000001800");
        evsSourceParam.setSourceAbbreviation("AOD2000");

        EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);
        MetaThesaurusConcept[] metaConcept2 = client.searchSourceByCode(evsSourceParam);

        assertNotNull("searchSourceByCode returned Null", metaConcept2);
        assertTrue(metaConcept2.length > 0);

        if (metaConcept2 != null && metaConcept2.length > 0) {
            for (int i = 0; i < metaConcept2.length; i++) {
                MetaThesaurusConcept meta = metaConcept2[i];
                assertNotNull("MetaThesaurusConcept object is Null", meta);
            }
        }

        // Test invalid inputs
        // Case 1: ALL sources flag (*) is not allowed
        boolean bInvalid = false;
        try {

            evsSourceParam.setSourceAbbreviation("*");
            metaConcept2 = client.searchSourceByCode(evsSourceParam);
            assertNull("meta thesaurus concepts are not null!", metaConcept2);

        } catch (gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType e) {
            bInvalid = true;
            System.out.println("Error message: " + e.getFaultString());
        }

        assertTrue("Invalid input did not "
            + "throw <gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType> exception", bInvalid);

        // Case 2: "NOCODE" is not allowed
        bInvalid = false;
        try {

            evsSourceParam.setCode("NOCODE");
            evsSourceParam.setSourceAbbreviation("AOD2000");
            metaConcept2 = client.searchSourceByCode(evsSourceParam);

        } catch (gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType e) {
            bInvalid = true;
            System.out.println("Error message: " + e.getFaultString());
        }

        assertTrue("Invalid input did not "
            + "throw <gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType> exception", bInvalid);

        // Case3 : Source abbreviation is case sensitive
        bInvalid = false;
        try {

            evsSourceParam.setCode("0000001800");
            evsSourceParam.setSourceAbbreviation("aod2000");
            metaConcept2 = client.searchSourceByCode(evsSourceParam);

        } catch (gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType e) {
            bInvalid = true;
            System.out.println("Error message: " + e.getFaultString());
        }

        assertTrue("Invalid input did not "
            + "throw <gov.nih.nci.cagrid.evsgridservice.stubs.types.InvalidInputExceptionType> exception", bInvalid);

    }


    /**
     * @throws Exception
     */
    public MetaThesaurusConcept testSearchSourceByCode(EVSSourceSearchParams evsSourceParams) throws Exception {
        System.out.println("testing:searchSourceByCode!");

        EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);
        MetaThesaurusConcept[] metaConcept = null;

        try {
            metaConcept = client.searchSourceByCode(evsSourceParams);

            if (metaConcept != null && metaConcept.length > 0) {
                for (int i = 0; i < metaConcept.length; i++) {
                    MetaThesaurusConcept meta = metaConcept[i];
                    assertNotNull("MetaThesaurusConcept object is Null", meta);
                }
            }

        } catch (Exception e) {
            System.out.println("Is exception valid: " + e.getMessage());
        }
        return ((metaConcept != null) ? metaConcept[0] : null);

    }


    /**
     *
     */

    public void testSearchDescLogicConcept() {
        // Test passing different values to the API that retrieves
        // DescLogicConcept objects

        try {

            EVSGridServiceClient client = new EVSGridServiceClient(this.endpoint);

            // Case 1: Use Search: Anatomic* to get DesLogicConcept Objects
            String searchTerm = "Anatomic*";
            System.out.println("testing:searchDescLogicConcep by Search Term: " + searchTerm);

            EVSDescLogicConceptSearchParams evsSearchParams = new EVSDescLogicConceptSearchParams();
            evsSearchParams.setVocabularyName("NCI_Thesaurus");
            evsSearchParams.setSearchTerm(searchTerm);
            evsSearchParams.setLimit(100);

            DescLogicConcept[] descLogicConcepts = client.searchDescLogicConcept(evsSearchParams);
            DescLogicConcept descLogicConcept1 = descLogicConcepts[0];
            assertNotNull("Description Logic Concept list is not populated ", descLogicConcept1);

            // Do a search with the same concept code and test that the
            // Description Logic Concept objects are
            // indeed equal.

            evsSearchParams.setSearchTerm(descLogicConcept1.getCode());
            DescLogicConcept[] descLogicConcepts2 = client.searchDescLogicConcept(evsSearchParams);
            DescLogicConcept descLogicConcept2 = descLogicConcepts2[0];

            // The list should contain 1 element
            assertTrue("Desc Logic Concept query should return exactly one result", (descLogicConcepts2.length == 1));

            // Check equality
            assertEquals("DescLogicConcepts are not Equal", descLogicConcept1, descLogicConcept2);

            // Pass incorrect Concept codes and see what we get!

            // (a): space
            String invalidConceptCode1 = "C1229 ";
            evsSearchParams.setSearchTerm(invalidConceptCode1);
            DescLogicConcept[] descLogicConcepts3 = client.searchDescLogicConcept(evsSearchParams);
            assertNull("Desc Logic Concept query should return null", descLogicConcepts3);

            // (b): letter
            evsSearchParams.setSearchTerm("C1229asj");
            descLogicConcepts3 = client.searchDescLogicConcept(evsSearchParams);
            assertNull("Desc Logic Concept query should return null", descLogicConcepts3);

            // (c): Just a wrong concept code
            evsSearchParams.setSearchTerm("C12299999559");
            descLogicConcepts3 = client.searchDescLogicConcept(evsSearchParams);
            assertNull("Desc Logic Concept query should return null", descLogicConcepts3);

        } catch (Exception e) {
            System.out.println("Is exception valid: " + e.getMessage());
        }

    }

}