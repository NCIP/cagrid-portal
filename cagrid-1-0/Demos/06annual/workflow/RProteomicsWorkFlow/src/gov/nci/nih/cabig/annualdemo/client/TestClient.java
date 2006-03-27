package gov.nci.nih.cabig.annualdemo.client;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

import caBIG.RProteomics.JpegImageType;

import gov.nih.nci.cagrid.common.Utils;
import caBIG.RProteomics.PercentileType;
import caBIG.RProteomics.WindowType;
import gov.nih.nci.cagrid.cql.CQLQueryType;
import gov.nih.nci.cagrid.rproteomics.stubs.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import org.apache.axis.encoding.Base64;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TestClient {
	protected static final String DEFAULT_URL_BASE = "http://localhost:8080/active-bpel/services/";

	private static final String RPROT_DATA_NAMESPACE = "http://rproteomics.cagrid.nci.nih.gov/RPData";

	protected static String urlString = DEFAULT_URL_BASE
			+ "WorkFlowClientService";

	protected static String imageFileName = "plot.jpg";

	WorkFlowInputType workFlowInput = null;

	public TestClient(String urlString, WorkFlowInputType workFlowInput,
			String imageFileName) {
		this.urlString = urlString;
		this.workFlowInput = workFlowInput;
		this.imageFileName = imageFileName;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Options options = getOptions();
			
			CommandLine cmd = null;
			try {
				cmd = new BasicParser().parse(options, args);
			} catch (ParseException e) {
				e.printStackTrace();
				System.out
						.println("Error parsing arguments: " + e.getMessage());
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("TestClient", options);
				System.exit(-1);
				return;
			}
			WorkFlowInputType input = createInput(cmd);

			TestClient testClient = new TestClient(cmd.getOptionValue("gsh"),
					input, cmd.getOptionValue("imageFileName"));
			WorkFlowOutputType output = testClient.callService();
			System.out.println("Submitted the workflow, waiting for finish");
			if (output != null) {
				JpegImageType image = output.getJpegImage();
				byte[] imageBytes = Base64.decode(image.getData());
				BufferedOutputStream os = new BufferedOutputStream(
						new FileOutputStream(imageFileName));
				os.write(imageBytes);
				os.flush();
				os.close();
				System.out.println("Wrote the plot to : " + imageFileName);
			} else {
				System.out.println("No Output!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.toString());
		}
	}

	private static WorkFlowInputType createInput(CommandLine cmd) throws Exception {
		CQLQueryType queryType = (CQLQueryType) Utils.deserializeDocument(cmd
				.getOptionValue("query"), CQLQueryType.class);
		Query queryElement = new Query(queryType);
		WorkFlowInputType input = new WorkFlowInputType();
		int windowSize = 1023;
		WindowType windowType = new WindowType(windowSize);
		PercentileType percentileType = new PercentileType(75);
		input.setQuery(queryElement);
		input.setWindowType(windowType);
		input.setPercentileType(percentileType);
		System.out.println(input.toString());
		return input;
	}

	public String getURL() {
		return urlString;
	}

	public void setURL(String u) {
		urlString = u;
	}

	public static Options getOptions() {
		Option gsh = OptionBuilder.hasArg().isRequired(false).withDescription(
				"grid service handle").create("gsh");

		Option query = OptionBuilder.withArgName("query").hasArg().isRequired(
				false).withDescription("query file").create("query");

		Option imageFileName = OptionBuilder.withArgName("imageFileName")
				.hasArg().isRequired(false).withDescription(
						"path to image generated").create("imageFileName");

		// add options
		Options options = new Options();

		options.addOption(gsh);
		options.addOption(query);
		options.addOption(imageFileName);

		return options;
	}

	public WorkFlowOutputType callService() throws Exception {
		WorkFlowOutputType output = null;
		try {
			Call call = createCall();
			if (this.workFlowInput.getQuery().getQuery() != null) {
				System.out.println("Query Name: " + 
						this.workFlowInput.getQuery().getQuery().getName());
				QName workFlowInputQName = new QName(RPROT_DATA_NAMESPACE,
				"WorkFlowInputType");
				Utils.serializeDocument("sample.xml", this.workFlowInput, workFlowInputQName);
			output = (WorkFlowOutputType) call
					.invoke(new Object[] { this.workFlowInput });
			} else {
				System.out.println("Query is null");
			}
			return output;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Creates and returns an RPC SOAP call.
	 * 
	 * @return an RPC SOAP call
	 */
	protected Call createCall() throws Exception {
		QName workFlowInputQName = new QName(RPROT_DATA_NAMESPACE,
				"WorkFlowInputType");
		QName workFlowOuputQName = new QName(RPROT_DATA_NAMESPACE,
				"WorkFlowOutputType");

		Service service = new Service();
		Call call = (Call) service.createCall();
		System.out.println("Calling : " + urlString);
		call.setTargetEndpointAddress(new java.net.URL(urlString));

		call.setOperationName("startWorkFlow");

		call.addParameter("parameters", workFlowInputQName, ParameterMode.IN);
		call.setReturnType(workFlowOuputQName, WorkFlowOutputType.class);

		register(call, WorkFlowInputType.class, workFlowInputQName);
		register(call, WorkFlowOutputType.class, workFlowOuputQName);
		return call;
	}

	protected void register(Call call, Class klass, QName qname) {
		call.registerTypeMapping(klass, qname, BeanSerializerFactory.class,
				BeanDeserializerFactory.class);
	}

}
