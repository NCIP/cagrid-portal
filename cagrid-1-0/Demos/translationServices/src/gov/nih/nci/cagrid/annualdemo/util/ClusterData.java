/*
 * Created on Jan 27, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import java.util.ArrayList;

public class ClusterData
{
	public ArrayList<String> arrays = new ArrayList<String>();
	public ArrayList<String> genes = new ArrayList<String>();
	
	public Cluster arrayCluster = null;
	public Cluster geneCluster = null;

	
	public static class Cluster
	{
		public Cluster cluster1;
		public Cluster cluster2;
		public Cluster parent;
		public String name;
		boolean isLeaf = false;
		
		public Cluster(Cluster parent)
		{
			super();
			
			setParent(parent);
		}
		
		public void setParent(Cluster parent)
		{
			this.parent = parent;
			if (parent != null) {
				if (parent.cluster1 == null) parent.cluster1 = this;
				else parent.cluster2 = this;
			}
		}
	}


	public void swapClusters()
	{
		ArrayList<String> tmpList = arrays;
		arrays = genes;
		genes = tmpList;
		
		Cluster tmpCluster = arrayCluster;
		arrayCluster = geneCluster;
		geneCluster = tmpCluster;
	}
}
