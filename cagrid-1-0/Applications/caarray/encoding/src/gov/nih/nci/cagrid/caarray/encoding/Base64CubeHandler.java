package gov.nih.nci.cagrid.caarray.encoding;

import gov.nih.nci.mageom.domain.BioAssayData.impl.BioDataCubeImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.util.Base64Decoder;
import org.castor.util.Base64Encoder;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

public class Base64CubeHandler implements FieldHandler {

	private static Log LOG = LogFactory.getLog(Base64CubeHandler.class);

	public void checkValidity(Object arg0) throws ValidityException,
			IllegalStateException {
		// TODO Auto-generated method stub

	}

	public Object getValue(Object obj) throws IllegalStateException {
		Object value = null;

		if (obj != null) {
			if (obj instanceof BioDataCubeImpl) {

				BioDataCubeImpl bdc = (BioDataCubeImpl) obj;
				Object[][][] cube = bdc.getCube();
				if (cube != null) {

					try {
						LOG.debug("Beginning encoding...");
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						BufferedOutputStream bos = new BufferedOutputStream(
								baos);
						ObjectOutputStream oos = new ObjectOutputStream(bos);
						oos.writeObject(cube);
						oos.flush();
						byte[] bytes = baos.toByteArray();

						value = new String(Base64Encoder.encode(bytes));
						LOG.debug("...done encoding.");

					} catch (Exception ex) {
						LOG.error("Error enconding cube: " + ex.getMessage(), ex); 
						throw new RuntimeException("Error encoding cube: "
								+ ex.getMessage(), ex);
					}
				}

			} else {
				throw new IllegalStateException("Object not instanceof: "
						+ BioDataCubeImpl.class.getName()
						+ obj.getClass().getName());
			}
		}

		return value;
	}

	public Object newInstance(Object obj) throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetValue(Object obj) throws IllegalStateException,
			IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	public void setValue(Object obj, Object value)
			throws IllegalStateException, IllegalArgumentException {

		if (obj != null) {

			BioDataCubeImpl bdc = null;
			if (!(obj instanceof BioDataCubeImpl)) {
				throw new IllegalArgumentException("Object not instance of "
						+ BioDataCubeImpl.class.getName() + ": "
						+ obj.getClass().getName());
			}
			bdc = (BioDataCubeImpl) obj;

			if (value == null) {
				bdc.setCube(null);
			} else {

				if (!(value instanceof String)) {
					throw new IllegalArgumentException(
							"Value not instanceof String: "
									+ value.getClass().getName());
				}
				Object[][][] cube = null;
				try {
					byte[] bytes = Base64Decoder.decode((String) value);

					ObjectInputStream ois = new ObjectInputStream(
							new BufferedInputStream(new ByteArrayInputStream(
									bytes)));
					cube = (Object[][][]) ois.readObject();
					ois.close();

					bdc.setCube(cube);

				} catch (Exception ex) {
					throw new RuntimeException("Error decoding cube: "
							+ ex.getMessage(), ex);
				}

			}

		}

	}

}
