package org.globus.cagrid.RProteomics.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.MessageContext;
import org.apache.axis.utils.XMLUtils;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.globus.wsrf.encoding.ObjectSerializer;
import org.globus.wsrf.encoding.SerializationException;
import org.globus.wsrf.utils.ContextUtils;

import edu.duke.cabig.javar.util.Base64;
import org.globus.cagrid.RProteomics.common.RProteomicsI;
import org.xml.sax.SAXException;

import edu.duke.cabig.rproteomics.domain.serviceinterface.*;
import edu.duke.cabig.rproteomics.services.RProteomicsAnalysisService;
import gov.nih.nci.cagrid.common.Utils;

/** 
 *  RProteomicsI
 *  TODO:DOCUMENT ME
 * 
 * @created by caGrid toolkit version 0.5
 * 
 */
public class RProteomicsImpl 
	implements RProteomicsI
{
	private RProteomicsAnalysisService impl;
	
	public RProteomicsImpl()
	{
		// get the config params for the backend tools
		MessageContext context = MessageContext.getCurrentContext(); 
		
		//point to a temp dir
		String baseDir = (String) ContextUtils.getServiceProperty( 
			context, "rprotBaseDir"
		);
		//point to a shared lib for the OSS
		String ossDll = (String) ContextUtils.getServiceProperty( 
			context, "rprotOssDll"
		);
		//point to the RBridge.r file
		String ossSource = (String) ContextUtils.getServiceProperty( 
			context, "rprotOssSource"
		);
		//point to the R cmd
		String rCmd = (String) ContextUtils.getServiceProperty( 
			context, "rprotRcmd"
		);
		//point to the R direction in the rprotiomics dist....
		String sourceDir = (String) ContextUtils.getServiceProperty( 
			context, "rprotSourceDir"
		);
		//RProteomicsDatabase
		String dbCollection = (String) ContextUtils.getServiceProperty( 
			context, "rprotDBCollection"
		);
		//localhost
		String dbServer = (String) ContextUtils.getServiceProperty( 
			context, "rprotDBServer"
		);
		//point to the binary data dir
		String dataDir = (String) ContextUtils.getServiceProperty( 
			context, "rprotDBDir"
		);
		//point to the binary data dir
		String serviceImplClass = (String) ContextUtils.getServiceProperty( 
			context, "serviceImplClass"
		);

		/*
		//point to a temp dir
		String baseDir = (String) base.getProperty("rprotBaseDir");
		//point to a shared lib for the OSS
		String ossDll = (String) base.getProperty("rprotOssDll");
		//point to the RBridge.r file
		String ossSource = (String) base.getProperty("rprotOssSource");
		//point to the R cmd
		String rCmd = (String) base.getProperty("rprotRcmd");
		//point to the R direction in the rprotiomics dist....
		String sourceDir = (String) base.getProperty("rprotSourceDir");
		//RProteomicsDatabase
		String dbCollection = (String) base.getProperty("rprotDBCollection");
		//localhost
		String dbServer = (String) base.getProperty("rprotDBServer");
		//point to the binary data dir
		String dataDir = (String) base.getProperty("rprotDBDir");
		*/
		
		File dataDirFile = null;
		if (dataDir != null) dataDirFile = new File(dataDir);
		
		//impl = new RProteomicsAnalysisServiceImpl(
		//	new File(baseDir), new File(ossDll), new File(ossSource), new File(rCmd), new File(sourceDir), 
		//	dbCollection, dbServer, dataDirFile
		//);	

		try {
			Class cl = Class.forName(serviceImplClass);
			Constructor c = cl.getConstructor(new Class[] {
				File.class, File.class, File.class, File.class, File.class,
		         String.class, String.class, File.class
			});
			impl = (RProteomicsAnalysisService) c.newInstance(new Object[] {
				new File(baseDir), new File(ossDll), new File(ossSource), new File(rCmd), new File(sourceDir), 
				dbCollection, dbServer, 
				dataDirFile
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
				"invalid RProteomics service implementation class (" + serviceImplClass + ")"
			);
		}
	}
	
	public static byte[] unwrap(JpegImageType image)
	{
		if (image == null) return null;
		return Base64.decode(image.getData());
	}
	
	public static Double unwrap(NoiseType noise)
	{
		if (noise == null) return null;
		return new Double(noise.getMinimum());
	}
	
	public static Double unwrap(LambdaType lambda)
	{
		if (lambda == null) return null;
		return new Double(lambda.getValue());
	}
	
	public static Double unwrap(ValuesNearToCutoffType valuesNearToCutoff)
	{
		if (valuesNearToCutoff == null) return null;
		return new Double(valuesNearToCutoff.getValue());
	}
	
	public static Integer unwrap(PercentileType percentile)
	{
		if (percentile == null) return null;
		return new Integer(percentile.getValue());
	}
	
	public static Integer unwrap(PolynomialDegreeType degree)
	{
		if (degree == null) return null;
		return new Integer(degree.getValue());
	}
	
	public static Double unwrap(SpanType span)
	{
		if (span == null) return null;
		return new Double(span.getValue());
	}
	
	public static Integer unwrap(CoefficientsType coefficients)
	{
		if (coefficients == null) return null;
		return new Integer(coefficients.getNumberToDrop());
	}
	
	public static Double unwrap(ThresholdType thresholdMultiplier)
	{
		if (thresholdMultiplier == null) return null;		
		return thresholdMultiplier.getMultiplier();
	}
	
	public static Integer unwrap(WindowType windowSize)
	{
		if (windowSize == null) return null;
		return new Integer(windowSize.getSize());
	}
	
	public static Integer unwrap(QuantileType quantile)
	{
		if (quantile == null) return null;
		return new Integer(quantile.getValue());
	}
	
	public static Double unwrap(ExponentType exponent)
	{
		if (exponent == null) return null;
		return new Double(exponent.getValue());
	}
	
	public static String[] unwrap(LsidType[] lsids)
	{
		if (lsids == null) return null;
		String[] ret = new String[lsids.length];
		for (int i = 0; i < lsids.length; i++) {
			ret[i] = lsids[i].getValue();
		}
		return ret;
	}

	private String[] unwrap(ScanFeaturesType[] scanFeaturesXml) 
		throws SerializationException, IOException
	{
		System.out.println("unwrap scaFeaturesType=" + scanFeaturesXml.length);
		QName qname = new QName("rproteomics.cabig.duke.edu/1/scanFeatures", "scanFeatures");
		String[] ret = new String[scanFeaturesXml.length];
		for (int i = 0; i < ret.length; i++) {
			StringWriter sw = new StringWriter();
			ObjectSerializer.serialize(sw, scanFeaturesXml[i], qname);
			ret[i] = sw.toString();
			sw.close();
		}
		return ret;
	}
	
	public static JpegImageType wrap(byte[] image)
	{
		if (image == null) return null;
		
		JpegImageType ret = new JpegImageType();
		ret.setData(Base64.encodeBytes(image));
		return ret;
	}
	
	public static LsidType[] wrap(String[] lsids)
	{
		if (lsids == null) return null;
		
		LsidType[] ret = new LsidType[lsids.length];
		for (int i = 0; i < lsids.length; i++) {
			ret[i] = new LsidType();
			ret[i].setValue(lsids[i]);
		}
		return ret;
	}


	private ScanFeaturesType[] wrapScanFeatures(String[] scanFeaturesXml) 
		throws ParserConfigurationException, SAXException, IOException, DeserializationException 
	{
		ScanFeaturesType[] ret = new ScanFeaturesType[scanFeaturesXml.length];
		for (int i = 0; i < ret.length; i++) {
			ByteArrayInputStream is = new ByteArrayInputStream(scanFeaturesXml[i].getBytes());
			org.w3c.dom.Document doc = XMLUtils.newDocument(is);
			//System.out.println(scanFeaturesXml[i]);
			ret[i] = (ScanFeaturesType) ObjectDeserializer.toObject(doc.getDocumentElement(), ScanFeaturesType.class);
		}
		return ret;
	}
	
	public static WindowType wrapWindowSize(Integer windowSize)
	{
		if (windowSize == null) return null;
		WindowType ret = new WindowType();
		ret.setSize(windowSize.intValue());
		return ret;
	}
	
	public static CoefficientsType wrapNumCoeffsToDrop(Integer numCoeffsToDrop)
	{
		if (numCoeffsToDrop == null) return null;
		CoefficientsType ret = new CoefficientsType();
		ret.setNumberToDrop(numCoeffsToDrop.intValue());
		return ret;
	}
	
	public static ExponentType wrapExponent(Double exponent)
	{
		if (exponent == null) return null;
		ExponentType ret = new ExponentType();
		ret.setValue(exponent.doubleValue());
		return ret;
	}
	
	public static QuantileType wrapStartQuantile(Integer startQuantile)
	{
		if (startQuantile == null) return null;
		QuantileType ret = new QuantileType();
		ret.setValue(startQuantile.intValue());
		return ret;
	}
	
	public static PercentileType wrapPercentile(Integer percentile)
	{
		if (percentile == null) return null;
		PercentileType ret = new PercentileType();
		ret.setValue(percentile.intValue());
		return ret;
	}
	
	public static ValuesNearToCutoffType wrapValuesNearCutoff(Double valuesNearCutoff)
	{
		if (valuesNearCutoff == null) return null;
		ValuesNearToCutoffType ret = new ValuesNearToCutoffType();
		ret.setValue(valuesNearCutoff.doubleValue());
		return ret;
	}
	
	public static LambdaType wrapLambda(Double lambda)
	{
		if (lambda == null) return null;
		LambdaType ret = new LambdaType();
		ret.setValue(lambda.doubleValue());
		return ret;
	}
	
	public static NoiseType wrapMinNoise(Double minNoise)
	{
		if (minNoise == null) return null;
		NoiseType ret = new NoiseType();
		ret.setMinimum(minNoise.doubleValue());
		return ret;
	}
	
	public static ThresholdType wrapThresholdMultiplier(Double thresholdMultiplier)
	{
		if (thresholdMultiplier == null) return null;
		ThresholdType ret = new ThresholdType();
		ret.setMultiplier(thresholdMultiplier);
		return ret;
	}
	
	public static SpanType wrapSpan(Double span)
	{
		if (span == null) return null;
		SpanType ret = new SpanType();
		ret.setValue(span.doubleValue());
		return ret;
	}
	
	public static PolynomialDegreeType wrapDegree(Integer degree)
	{
		if (degree == null) return null;
		PolynomialDegreeType ret = new PolynomialDegreeType();
		ret.setValue(degree.intValue());
		return ret;
	}

	public static void throwRemoteException(Exception e)
		throws RemoteException
	{
		RemoteException re = new RemoteException(e.getMessage());
		re.setStackTrace(e.getStackTrace());
		e.printStackTrace();
		throw re;
	}
	
	public LsidType[] normalize_log(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_log(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] normalize_log10(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_log10(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public LsidType[] normalize_log2(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_log2(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] normalize_sqrt(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_sqrt(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] normalize_square(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_square(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public LsidType[] normalize_cubeRoot(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_cubeRoot(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public LsidType[] normalize_power(LsidType[] lsids,ExponentType exponent)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_power(unwrap(lsids), unwrap(exponent)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] normalize_byMax(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_byMax(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] normalize_usingMinAndMax(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_usingMinAndMax(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] normalize_IOC(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_IOC(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] normalize_quantile(LsidType[] lsids,QuantileType startQuantile)
		throws RemoteException
	{
		try {
			return wrap(impl.normalize_quantile(unwrap(lsids), unwrap(startQuantile)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public LsidType[] removeBackground_minus(LsidType[] lsids1,LsidType[] lsids2)
		throws RemoteException
	{
		try {
			return wrap(impl.removeBackground_minus(unwrap(lsids1), unwrap(lsids2)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public LsidType[] denoise_waveletUDWT(LsidType[] lsids,WindowType windowSize,ThresholdType thresholdMultiplier)
		throws RemoteException
	{
		try {
			return wrap(impl.denoise_waveletUDWT(unwrap(lsids), unwrap(windowSize), unwrap(thresholdMultiplier)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] denoise_waveletUDWTW(LsidType[] lsids,WindowType windowSize,ThresholdType thresholdMultiplier)
		throws RemoteException
	{
		try {
			return wrap(impl.denoise_waveletUDWTW(unwrap(lsids), unwrap(windowSize), unwrap(thresholdMultiplier)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] denoise_highPass(LsidType[] lsids,WindowType windowSize,CoefficientsType numCoeffsToDrop)
		throws RemoteException
	{
		try {
			return wrap(impl.denoise_highPass(unwrap(lsids), unwrap(windowSize), unwrap(numCoeffsToDrop)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] denoise_highPassW(LsidType[] lsids,WindowType windowSize,CoefficientsType numCoeffsToDrop)
		throws RemoteException
	{
		try {
			return wrap(impl.denoise_highPassW(unwrap(lsids), unwrap(windowSize), unwrap(numCoeffsToDrop)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] denoise_PCAFilter(LsidType[] lsids,WindowType windowSize,CoefficientsType numCoeffsToDrop)
		throws RemoteException
	{
		try {
			return wrap(impl.denoise_PCAFilter(unwrap(lsids), unwrap(windowSize), unwrap(numCoeffsToDrop)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] denoise_q5_PCAFilter(LsidType[] lsids,WindowType windowSize,CoefficientsType numCoeffsToDrop)
		throws RemoteException
	{
		try {
			return wrap(impl.denoise_q5_PCAFilter(unwrap(lsids), unwrap(windowSize), unwrap(numCoeffsToDrop)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] denoise_loess(LsidType[] lsids,WindowType windowSize,SpanType span,PolynomialDegreeType degree)
		throws RemoteException
	{
		try {
			return wrap(impl.denoise_loess(unwrap(lsids), unwrap(windowSize), unwrap(span), unwrap(degree)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public LsidType[] removeBackground_linearFit(LsidType[] lsids,WindowType windowSize)
		throws RemoteException
	{
		try {
			return wrap(impl.removeBackground_linearFit(unwrap(lsids), unwrap(windowSize)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] removeBackground_exponentialFit(LsidType[] lsids,WindowType windowSize)
		throws RemoteException
	{
		try {
			return wrap(impl.removeBackground_exponentialFit(unwrap(lsids), unwrap(windowSize)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] removeBackground_logistic(LsidType[] lsids,WindowType windowSize)
		throws RemoteException
	{
		try {
			return wrap(impl.removeBackground_logistic(unwrap(lsids), unwrap(windowSize)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] removeBackground_quadraticFit(LsidType[] lsids,WindowType windowSize)
		throws RemoteException
	{
		try {
			return wrap(impl.removeBackground_quadraticFit(unwrap(lsids), unwrap(windowSize)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] denoise_MAD(LsidType[] lsids,WindowType windowSize,PercentileType percentile,ValuesNearToCutoffType valuesNearCutoff,LambdaType lambda)
		throws RemoteException
	{
		try {
			return wrap(impl.denoise_MAD(unwrap(lsids), unwrap(windowSize), unwrap(percentile), unwrap(valuesNearCutoff), unwrap(lambda)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] denoise_MADNormalize(LsidType[] lsids,WindowType windowSize,PercentileType percentile,ValuesNearToCutoffType valuesNearCutoff,LambdaType lambda,NoiseType minNoise)
		throws RemoteException
	{
		try {
			return wrap(impl.denoise_MADNormalize(unwrap(lsids), unwrap(windowSize), unwrap(percentile), unwrap(valuesNearCutoff), unwrap(lambda), unwrap(minNoise)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public LsidType[] removeBackground_runningQuantile(LsidType[] lsids,WindowType windowSize,PercentileType percentile)
		throws RemoteException
	{
		try {
			return wrap(impl.removeBackground_runningQuantile(unwrap(lsids), unwrap(windowSize), unwrap(percentile)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public JpegImageType plot_2DStacked(LsidType[] lsidsTop,LsidType[] lsidsBottom)
		throws RemoteException
	{
		try {
			return wrap(impl.plot_2DStacked(unwrap(lsidsTop), unwrap(lsidsBottom)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}
	
	public JpegImageType plot_2D(LsidType[] lsids)
		throws RemoteException
	{
		try {
			return wrap(impl.plot_2D(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public LsidType[] echo(LsidType[] lsids) throws RemoteException
	{
		return lsids;
	}

	     public void throwException() throws RemoteException {
		throw new RemoteException("this is just a test exception and is expected");
	}
	
	public LsidType[] general_interpolate(LsidType[] lsids) throws RemoteException {
		try {
			return wrap(impl.general_interpolate(unwrap(lsids)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
		
    }
     public LsidType[] align_alignx(LsidType[] lsids) 
     	throws RemoteException {
 		try {
			return wrap(impl.align_alignx(unwrap(lsids), null, null, null));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
     }

	public ScanFeaturesType[] normalize_quantileByValue(ScanFeaturesType[] scanFeaturesXml, QuantileType startQuantile) throws RemoteException
	{
 		try {
			return wrapScanFeatures(impl.normalize_quantileByValue(unwrap(scanFeaturesXml), unwrap(startQuantile)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public ScanFeaturesType[] denoise_waveletUDWTWByValue(ScanFeaturesType[] scanFeaturesXml, WindowType windowSize, ThresholdType thresholdMultiplier) throws RemoteException
	{
 		try {
			return wrapScanFeatures(impl.denoise_waveletUDWTWByValue(unwrap(scanFeaturesXml), unwrap(windowSize), unwrap(thresholdMultiplier)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public ScanFeaturesType[] removeBackground_runningQuantileByValue(ScanFeaturesType[] scanFeaturesXml, WindowType windowSize, PercentileType percentile) throws RemoteException
	{
 		try {
			return wrapScanFeatures(impl.removeBackground_runningQuantileByValue(unwrap(scanFeaturesXml), unwrap(windowSize), unwrap(percentile)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public LsidType[] removeBackground_loess(LsidType[] lsids, WindowType windowSize, SpanType span) throws RemoteException
	{
 		try {
			return wrap(impl.removeBackground_loess(unwrap(lsids), unwrap(windowSize), unwrap(span)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public ScanFeaturesType[] removeBackground_minusByValue(ScanFeaturesType[] scanFeaturesXml1, ScanFeaturesType[] scanFeaturesXml2) throws RemoteException
	{
 		try {
			return wrapScanFeatures(impl.removeBackground_minusByValue(unwrap(scanFeaturesXml1), unwrap(scanFeaturesXml2)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public JpegImageType plot_2DStackedByValue(ScanFeaturesType[] scanFeaturesXmlTop, ScanFeaturesType[] scanFeaturesXmlBottom) throws RemoteException
	{
 		try {
			return wrap(impl.plot_2DStackedByValue(unwrap(scanFeaturesXmlTop), unwrap(scanFeaturesXmlBottom)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public JpegImageType plot_2DByValue(ScanFeaturesType[] scanFeaturesXml) throws RemoteException
	{
 		try {
			return wrap(impl.plot_2DByValue(unwrap(scanFeaturesXml)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public ScanFeaturesType[] general_interpolateByValue(ScanFeaturesType[] scanFeaturesXml) throws RemoteException
	{
 		try {
			return wrapScanFeatures(impl.general_interpolateByValue(unwrap(scanFeaturesXml)));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}
	}

	public ScanFeaturesType[] align_alignxByValue(ScanFeaturesType[] scanFeaturesXml) throws RemoteException
	{
 		try {
			return wrapScanFeatures(impl.align_alignxByValue(unwrap(scanFeaturesXml), null, null, null));
		} catch (Exception e) {
			throwRemoteException(e);
			return null;
		}	}

	public ScanFeaturesType[] echoByValue(ScanFeaturesType[] scanFeatures) throws RemoteException
	{
		return scanFeatures;
	}
}
