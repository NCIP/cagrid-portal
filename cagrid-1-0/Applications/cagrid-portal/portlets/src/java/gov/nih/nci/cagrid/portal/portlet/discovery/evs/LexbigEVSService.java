package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;

import java.util.Set;
import java.util.TreeSet;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LexbigEVSService extends BaseEVSService {
    private CodingSchemeVersionOrTag productionTag;
    private ConceptReference cr;
    private boolean caseSensitive;


    public Set<EVSConceptDTO> getConceptsForKeyword(String keyword) throws CaGridPortletApplicationException {

        Set<EVSConceptDTO> resultSet = new TreeSet<EVSConceptDTO>();
        try {
            CodedNodeSet cns = appService.getCodingSchemeConcepts(vocabulary, productionTag);
            cns = cns.restrictToMatchingDesignations(
                    keyword, CodedNodeSet.SearchDesignationOption.ALL,
                    LBConstants.MatchAlgorithms.startsWith.toString(), null);
            SortOptionList sortCriteria =
                    Constructors.createSortOptionList(new String[]{"matchToQuery", "code"});

            // Resolve and analyze the result ...
            ResolvedConceptReferenceList matches =
                    cns.resolveToList(sortCriteria, null, new CodedNodeSet.PropertyType[]{}, searchLimit);
            if (matches == null) {
                logger.warn("WARNING: getCodedEntry ResolvedConceptReferenceList returns matches = 0)");

            } else {
                for (ResolvedConceptReference concept : matches.getResolvedConceptReference()) {
                    //temporary fix for the EVS search startsWith not working
                    if (concept.getEntityDescription().getContent().toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
                        EVSConceptDTO dto = new EVSConceptDTO(concept);
                        resultSet.add(dto);
                    }
                }
            }
        } catch (Exception e) {
            throw new CaGridPortletApplicationException(e);
        }


        return resultSet;//To change body of implemented methods use File | Settings | File Templates.
    }


    public void setProductionTag(CodingSchemeVersionOrTag productionTag) {
        this.productionTag = productionTag;
    }

    public void setCr(ConceptReference cr) {
        this.cr = cr;
    }
}
