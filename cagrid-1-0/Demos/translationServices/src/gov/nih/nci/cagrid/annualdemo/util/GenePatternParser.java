/*
 * Created on Feb 2, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import edu.duke.cabig.javar.io.StatMLSerializationException;
import gridextensions.Cluster;
import gridextensions.Data;

import java.io.IOException;
import java.util.Hashtable;

public class GenePatternParser
{
	private MicroarrayData microarrayData = new MicroarrayData();
	private ClusterData clusterData = new ClusterData();

	public GenePatternParser()
	{
		super();
	}
	
	public Data convertToStatml() 
		throws Exception
	{
		String[] rowNames = microarrayData.geneNames.toArray(new String[0]);
		String[] rowDescriptions = rowNames;
		String[] columnNames = microarrayData.arrayNames.toArray(new String[0]);;
		double[][] values = new double[microarrayData.data.size()][];
		for (int i = 0; i < values.length; i++) {
			values[i] = microarrayData.data.get(i);
		}
		
		StatmlHelper helper = new StatmlHelper();
		return helper.createStatmlData(rowNames, rowDescriptions, columnNames, values);
	}
	
	public void parseMicroarray(Data data) 
		throws IOException, StatMLSerializationException
	{
		microarrayData = StatmlHelper.extractArrayInfo(data);
	}
	
	public void parseCluster(Cluster[] gpclusters)
	{
		Hashtable<String, ClusterData.Cluster> clusterTable = new Hashtable<String, ClusterData.Cluster>();
		
		// create clusters
		for (Cluster gpcluster : gpclusters) {
			String name = gpcluster.getName();
			
			ClusterData.Cluster cluster = clusterTable.get(name);
			if (cluster == null) {
				clusterTable.put(name, cluster = new ClusterData.Cluster(null));
			}
			cluster.name = name;
			
			String[] childNames = gpcluster.getItemName();
			if (childNames == null) childNames = new String[0];
			for (String childName : childNames) {
				ClusterData.Cluster child = clusterTable.get(childName);
				if (child == null) {
					clusterTable.put(childName, child = new ClusterData.Cluster(null));
				}
				child.setParent(cluster);
			}
		}
		
		// find root
		ClusterData.Cluster root = findRoot(clusterTable.values().iterator().next());
		
		// set leaves
		setLeaves(root);
		
		// set data
		clusterData.geneCluster = root;
		for (Cluster gpcluster : gpclusters) {
			clusterData.genes.add(gpcluster.getName());
		}
	}

	private ClusterData.Cluster findRoot(ClusterData.Cluster cluster)
	{
		if (cluster.parent == null) return cluster;
		return findRoot(cluster.parent);
	}

	private void setLeaves(ClusterData.Cluster cluster)
	{
		if (cluster.cluster1 == null && cluster.cluster2 == null) {
			cluster.isLeaf = true;
			return;
		}
		
		cluster.isLeaf = false;
		cluster.name = null;

		if (cluster.cluster1 != null) setLeaves(cluster.cluster1);
		if (cluster.cluster2 != null) setLeaves(cluster.cluster2);
	}
	
	public ClusterData getClusterData()
	{
		return clusterData;
	}

	public MicroarrayData getMicroarrayData()
	{
		return microarrayData;
	}

	public void setMicroarrayData(MicroarrayData microarrayData)
	{
		this.microarrayData = microarrayData;
	}
}
