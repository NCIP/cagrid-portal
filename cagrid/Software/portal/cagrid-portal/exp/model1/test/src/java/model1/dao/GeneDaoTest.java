/**
 * 
 */
package model1.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model1.DBTestBase;
import model1.domain.Gene;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class GeneDaoTest extends DBTestBase<GeneDao> {
	
	public void testGeneByTerm(){
		
		Set<String> ofNode22 = new HashSet<String>();
		ofNode22.add("gene3_name");
		
		Set<String> ofNode2 = new HashSet<String>();
		ofNode2.add("gene2_name");
		ofNode2.addAll(ofNode22);
		
		Set<String> ofNode1 = new HashSet<String>();
		ofNode1.add("gene1_name");
		
		Set<String> ofRoot = new HashSet<String>();
		ofRoot.addAll(ofNode2);
		ofRoot.addAll(ofNode1);
		
		List<String> genes = names(getDao().getGenesByTerm("node2-2"));
		assertTrue("node22 doesn't match: " + vs(genes, ofNode22), ofNode22.containsAll(genes));
		genes = names(getDao().getGenesByTerm("node2"));
		assertTrue("node2 doesn't match: " + vs(genes, ofNode2), ofNode2.containsAll(genes));
		genes = names(getDao().getGenesByTerm("node1"));
		assertTrue("node1 doesn't match: " + vs(genes, ofNode1), ofNode1.containsAll(genes));
		genes = names(getDao().getGenesByTerm("node2-2"));
		assertTrue("root doesn't match: " + vs(genes, ofRoot), ofRoot.containsAll(genes));
	}
	
	private List<String> names(List<Gene> genes){
		List<String> names = new ArrayList<String>();
		for(Gene gene : genes){
			names.add(gene.getName());
		}
		return names;
	}

	private String vs(List<String> geneNames, Set<String> givenNames) {
		return geneNames + " vs. " + givenNames;
	}
	

}
