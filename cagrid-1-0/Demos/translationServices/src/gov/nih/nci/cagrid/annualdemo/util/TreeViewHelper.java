/*
 * Created on Jan 27, 2007
 */
package gov.nih.nci.cagrid.annualdemo.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

public class TreeViewHelper
{
	private MicroarrayData microarrayData;
	private ClusterData clusterData;
	
	private Hashtable<String, String> geneNameTable = new Hashtable<String, String>();
	private Hashtable<String, String> arrayNameTable = new Hashtable<String, String>();
	
	private Hashtable<ClusterData.Cluster, String> geneTable = new Hashtable<ClusterData.Cluster, String>();
	private Hashtable<ClusterData.Cluster, String> arrayTable = new Hashtable<ClusterData.Cluster, String>();

	private Hashtable<String, Integer> genePosTable = new Hashtable<String, Integer>();
	private Hashtable<String, Integer> arrayPosTable = new Hashtable<String, Integer>();

	private int weightCount = 0;

	public TreeViewHelper(MicroarrayData microarrayData, ClusterData clusterData)
	{
		super();
		
		this.microarrayData = microarrayData;
		this.clusterData = clusterData;
		
		buildGeneTable(clusterData.geneCluster);
		buildArrayTable(clusterData.arrayCluster);
		buildPosTable(microarrayData.geneNames, genePosTable);
		buildPosTable(microarrayData.arrayNames, arrayPosTable);
	}
	
	private void buildPosTable(ArrayList<String> names, Hashtable<String, Integer> posTable)
	{
		for (int i = 0; i < names.size(); i++) {
			posTable.put(names.get(i), new Integer(i));
		}
	}

	private void buildGeneTable(ClusterData.Cluster cluster)
	{
		if (cluster == null) return;
		
		if (cluster.isLeaf) {
			geneNameTable.put(cluster.name, "GENE" + geneNameTable.size() + "X");
		} else {
			geneTable.put(cluster, "NODE" + geneTable.size());
			buildGeneTable(cluster.cluster1);
			buildGeneTable(cluster.cluster2);
		}
	}
	
	private void buildArrayTable(ClusterData.Cluster cluster)
	{
		if (cluster == null) return;
		
		if (cluster.isLeaf) {
			arrayNameTable.put(cluster.name, "ARRY" + arrayNameTable.size() + "X");
		} else {
			arrayTable.put(cluster, "NODE" + arrayTable.size());
			buildArrayTable(cluster.cluster1);
			buildArrayTable(cluster.cluster2);
		}
	}
	
	public void writeTreeView(PrintWriter cdt, PrintWriter atr, PrintWriter gtr) 
		throws IOException
	{
		writeCdt(cdt);
		writeAtr(atr);
		writeGtr(gtr);		
	}
	
	public void writeTreeViewFiles(File cdtFile, File atrFile, File gtrFile) 
		throws IOException
	{
		PrintWriter cdtOut = new PrintWriter(new BufferedWriter(new FileWriter(cdtFile)));
		PrintWriter atrOut = new PrintWriter(new BufferedWriter(new FileWriter(atrFile)));
		PrintWriter gtrOut = new PrintWriter(new BufferedWriter(new FileWriter(gtrFile)));
		try {
			writeCdt(cdtOut);
			writeAtr(atrOut);
			writeGtr(gtrOut);
		} finally {
			cdtOut.flush();
			cdtOut.close();
			atrOut.flush();
			atrOut.close();
			gtrOut.flush();
			gtrOut.close();
		}
	}
	
	public void writeCdt(PrintWriter out)
		throws IOException
	{
		
		// print column headers
		out.print("GID\tNAME\tGWEIGHT");
		for (int i = 0; i < clusterData.arrays.size(); i++) {
			String arrayName = clusterData.arrays.get(i);
			out.print("\t");
			out.print(arrayName);
		}
		out.println();
		
		// print aid
		out.print("AID\t\t");
		for (String name : clusterData.arrays) {
			String aid = arrayNameTable.get(name);
			out.print("\t");
			out.print(aid);
		}
		out.println();
		
		// print data by rows
		for (int i = 0; i < clusterData.genes.size(); i++) {
			String geneName = clusterData.genes.get(i);
			String gid = geneNameTable.get(geneName);
			int geneIndex = genePosTable.get(geneName);
			
			out.print(gid);
			out.print("\t");
			out.print(microarrayData.geneNames.get(geneIndex));
			out.print("\t1");
			
//			for (double[] d : microarrayData.data) {
//				out.print("\t");
//				out.print(d[i]);
//			}
			for (double d : microarrayData.data.get(i)) {
				out.print("\t");
				out.print(d);
			}
			
			out.println();
		}
		
		out.flush(); 
	}
	
	public void writeGtr(PrintWriter out)
		throws IOException
	{
		weightCount = 0;
		writeCluster(clusterData.geneCluster, geneTable, geneNameTable, out, 1.0 / clusterData.genes.size() / 3.0);
		
		out.flush(); 
	}
	
	public void writeAtr(PrintWriter out)
		throws IOException
	{
		weightCount = 0;
		writeCluster(clusterData.arrayCluster, arrayTable, arrayNameTable, out, 1.0 / clusterData.arrays.size() / 3.0);
		
		out.flush(); 
	}
	
	protected void writeCluster(
		ClusterData.Cluster cluster, 
		Hashtable<ClusterData.Cluster, String> nodeTable,
		Hashtable<String, String> nameTable,
		PrintWriter out,
		double weightIncrement
	) {
		if (cluster.isLeaf) return;
		writeCluster(cluster.cluster1, nodeTable, nameTable, out, weightIncrement);
		writeCluster(cluster.cluster2, nodeTable, nameTable, out, weightIncrement);
		
		out.print(nodeTable.get(cluster));
		
		out.print("\t");
		if (cluster.cluster1.isLeaf) out.print(nameTable.get(cluster.cluster1.name));
		else out.print(nodeTable.get(cluster.cluster1));

		out.print("\t");
		if (cluster.cluster2.isLeaf) out.print(nameTable.get(cluster.cluster2.name));
		else out.print(nodeTable.get(cluster.cluster2));

		out.print("\t");
		out.print(1.0 - (weightCount * weightIncrement));
		weightCount++;
		
		out.println();
	}
}
