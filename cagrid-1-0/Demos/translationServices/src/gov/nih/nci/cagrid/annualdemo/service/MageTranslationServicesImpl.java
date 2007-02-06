package gov.nih.nci.cagrid.annualdemo.service;

import edu.columbia.geworkbench.cagrid.cluster.hierarchical.TreeViewInput;
import gov.nih.nci.cagrid.annualdemo.util.GeWorkbenchParser;
import gov.nih.nci.cagrid.annualdemo.util.GenePatternParser;
import gov.nih.nci.cagrid.annualdemo.util.MageParser;
import gov.nih.nci.cagrid.annualdemo.util.TreeViewHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.types.URI.MalformedURIException;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class MageTranslationServicesImpl extends MageTranslationServicesImplBase {

	private File treeViewDir = new File("c:\\");
	
	public MageTranslationServicesImpl() throws RemoteException {
		super();
	}
	

	public gridextensions.Data mageToStatML(gov.nih.nci.cagrid.cqlresultset.CQLQueryResults cQLQueryResultCollection) throws RemoteException {
		int count = cQLQueryResultCollection.getObjectResult().length;
		System.out.println("Got " + count + " Results from caArray");
		
		// hack - Dave: fix this
		cQLQueryResultCollection.setTargetClassname("java.lang.Object");
		
		// get mage
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(cQLQueryResultCollection, true);
		String mage = (String) iter.next();
		
		//System.out.println(mage);
		try {
			MageParser mageParser = new MageParser();
			mageParser.parseMicroarray(mage);
			GenePatternParser gpParser = new GenePatternParser();
			gpParser.setMicroarrayData(mageParser.getMicroarrayData());
			return gpParser.convertToStatml();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	public edu.columbia.geworkbench.cagrid.microarray.MicroarraySet statmlToMicroarraySet(gridextensions.Data data) throws RemoteException {
		try {
			GenePatternParser gpParser = new GenePatternParser();
			gpParser.parseMicroarray(data);
			
			GeWorkbenchParser gwParser = new GeWorkbenchParser();
			gwParser.setMicroarrayData(gpParser.getMicroarrayData());
			
			return gwParser.convertToMicroarraySet();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	public edu.columbia.geworkbench.cagrid.microarray.MicroarraySet mageToMicroArraySet(gov.nih.nci.cagrid.cqlresultset.CQLQueryResults cQLQueryResultCollection) throws RemoteException {
		int count = cQLQueryResultCollection.getObjectResult().length;
		System.out.println("Got " + count + " Results from caArray");
		
		// hack - Dave: fix this
		cQLQueryResultCollection.setTargetClassname("java.lang.Object");
		
		// get mage
		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(cQLQueryResultCollection, true);
		String mage = (String) iter.next();
		
		//System.out.println(mage);
		try {
			MageParser mageParser = new MageParser();
			mageParser.parseMicroarray(mage);
			GeWorkbenchParser gwParser = new GeWorkbenchParser();
			gwParser.setMicroarrayData(mageParser.getMicroarrayData());
			return gwParser.convertToMicroarraySet();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

//	public edu.columbia.geworkbench.cagrid.microarray.MicroarraySet mageToMicroArray(gov.nih.nci.cagrid.cqlresultset.CQLQueryResults cQLQueryResultCollection) throws RemoteException {
//		int count = cQLQueryResultCollection.getObjectResult().length;
//		System.out.println("Got " + count + " Results from caArray");
//		
//		// hack - Dave: fix this
//		cQLQueryResultCollection.setTargetClassname("java.lang.Object");
//		
//		// get mage
//		CQLQueryResultsIterator iter = new CQLQueryResultsIterator(cQLQueryResultCollection, true);
//		String mage = (String) iter.next();
//		
//		//System.out.println(mage);
//		try {
//			MageParser mageParser = new MageParser();
//			mageParser.parseMicroarray(mage);
//			GeWorkbenchParser gwParser = new GeWorkbenchParser();
//			gwParser.setMicroarrayData(mageParser.getMicroarrayData());
//			return gwParser.convertToMicroarraySet();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RemoteException(e.getMessage());
//		}
//
//	}

	public edu.columbia.geworkbench.cagrid.cluster.hierarchical.TreeViewInput gwClusterToTreeView(edu.columbia.geworkbench.cagrid.cluster.hierarchical.HierarchicalCluster hierarchicalCluster,edu.columbia.geworkbench.cagrid.microarray.MicroarraySet microarraySet) throws RemoteException {
		GeWorkbenchParser parser = new GeWorkbenchParser();

		try {
			// get XML
			StringWriter microarraySw = new StringWriter();
			Utils.serializeObject(microarraySet, new QName("MicroarraySet"), microarraySw);
	
			StringWriter clusterSw = new StringWriter();
			Utils.serializeObject(hierarchicalCluster, new QName("HierarchicalCluster"), clusterSw);
			
			// parse xml
			parser.parseCluster(microarraySw.toString());
			parser.parseCluster(clusterSw.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
		
		// translate
		TreeViewHelper helper = new TreeViewHelper(parser.getMicroarrayData(), parser.getClusterData());

		StringWriter cdtSw = new StringWriter();
		PrintWriter cdtOut = new PrintWriter(cdtSw);
		StringWriter atrSw = new StringWriter();
		PrintWriter atrOut = new PrintWriter(atrSw);
		StringWriter gtrSw = new StringWriter();
		PrintWriter gtrOut = new PrintWriter(gtrSw);
		
		try {
			helper.writeTreeView(cdtOut, atrOut, gtrOut);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
		
		cdtOut.flush();
		atrOut.flush();
		gtrOut.flush();
		
		String cdt = cdtSw.toString();
		String atr = atrSw.toString();
		String gtr = gtrSw.toString();
		
		// write to file
		try {
			Utils.stringBufferToFile(new StringBuffer(cdt),
				new File(treeViewDir, "workflow.cdt").toString()
			);
			Utils.stringBufferToFile(new StringBuffer(atr),
				new File(treeViewDir, "workflow.atr").toString()
			);
			Utils.stringBufferToFile(new StringBuffer(gtr),
				new File(treeViewDir, "workflow.gtr").toString()
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
		
		// return
		TreeViewInput ret = new TreeViewInput(); 
		ret.setCdt(cdt);
		ret.setAtr(atr);
		ret.setGtr(gtr);
		
		return ret;
	}

//	public edu.columbia.geworkbench.cagrid.cluster.hierarchical.TreeViewInput clusterToTreeView(edu.columbia.geworkbench.cagrid.cluster.hierarchical.HierarchicalCluster hierarchicalCluster,edu.columbia.geworkbench.cagrid.microarray.MicroarraySet microarraySet) throws RemoteException {
//	}

	public edu.columbia.geworkbench.cagrid.cluster.hierarchical.TreeViewInput gpClusterToTreeView(gridextensions.ConsensusClusterResultCollection consensusClusterResultCollection,gridextensions.Data data) throws RemoteException {
		GenePatternParser parser = new GenePatternParser();
		try {
			parser.parseMicroarray(data);
			parser.parseCluster(consensusClusterResultCollection.getCluster());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
			
		// translate
		TreeViewHelper helper = new TreeViewHelper(parser.getMicroarrayData(), parser.getClusterData());

		StringWriter cdtSw = new StringWriter();
		PrintWriter cdtOut = new PrintWriter(cdtSw);
		StringWriter atrSw = new StringWriter();
		PrintWriter atrOut = new PrintWriter(atrSw);
		StringWriter gtrSw = new StringWriter();
		PrintWriter gtrOut = new PrintWriter(gtrSw);
		
		try {
			helper.writeTreeView(cdtOut, atrOut, gtrOut);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
		
		cdtOut.flush();
		atrOut.flush();
		gtrOut.flush();
		
		String cdt = cdtSw.toString();
		String atr = atrSw.toString();
		String gtr = gtrSw.toString();
		
		// write to file
		try {
			Utils.stringBufferToFile(new StringBuffer(cdt),
				new File(treeViewDir, "workflow.cdt").toString()
			);
			Utils.stringBufferToFile(new StringBuffer(atr),
				new File(treeViewDir, "workflow.atr").toString()
			);
			Utils.stringBufferToFile(new StringBuffer(gtr),
				new File(treeViewDir, "workflow.gtr").toString()
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
		
		// return
		TreeViewInput ret = new TreeViewInput(); 
		ret.setCdt(cdt);
		ret.setAtr(atr);
		ret.setGtr(gtr);
		
		return ret;
	}

	public gridextensions.Data microarraySetToStatml(edu.columbia.geworkbench.cagrid.microarray.MicroarraySet microarraySet) throws RemoteException {
		try {
			// get XML
			StringWriter microarraySw = new StringWriter();
			Utils.serializeObject(microarraySet, new QName("MicroarraySet"), microarraySw);

			GeWorkbenchParser gwParser = new GeWorkbenchParser();
			gwParser.parseMicroarray(microarraySw.toString());
			
			GenePatternParser gpParser = new GenePatternParser();
			gpParser.setMicroarrayData(gpParser.getMicroarrayData());
			
			return gpParser.convertToStatml();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	public gridextensions.Data performAnalysis(gridextensions.Data data,gridextensions.PreprocessDatasetParameterSet preprocessDatasetParameterSet) throws RemoteException {
		PreprocessDatasetSTATMLServiceClient client;
		try {
			client = new PreprocessDatasetSTATMLServiceClient("http://node255.broad.mit.edu:6060/wsrf/services/cagrid/PreprocessDatasetSTATMLService");
			return client.performAnalysis(data, preprocessDatasetParameterSet);
		} catch (MalformedURIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public gridextensions.Data bioAssayToStatML(gov.nih.nci.mageom.domain.bioassay.MeasuredBioAssay[] measuredBioAssay) throws RemoteException {
		//TODO: Implement this autogenerated method
		throw new RemoteException("Not yet implemented");
	}

	public gridextensions.Data bioAssayToMicroArraySet(gridextensions.MeasuredBioAssay[] measuredBioAssay) throws RemoteException {
		//TODO: Implement this autogenerated method
		throw new RemoteException("Not yet implemented");
	}

}

