/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import static org.junit.Assert.fail;
import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.Image;
import gov.nih.nci.cagrid.portal.domain.catalog.ImageFormat;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.junit.Test;

/**
 * @author joshua
 *
 */
public class ImageDaoTest extends DaoTestBase<ImageDao> {

	@Test
	public void save(){
		try{
			String inFile = "test/data/person_placeholder_180px.png";
			String outFile = "test/build/person_placeholder_180px.png";
			
			FileInputStream in = new FileInputStream(inFile);
			BufferedImage bImage = ImageIO.read(in);
			in.close();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bImage, ImageFormat.PNG.toString(), bos);
			Image image = new Image();
			image.setData(bos.toByteArray());
			image.setHeight(bImage.getHeight());
			image.setWidth(bImage.getWidth());
			image.setType(bImage.getType());
			image.setFormat(ImageFormat.PNG);
			getDao().save(image);

			FileOutputStream out = new FileOutputStream(outFile);
			Image image2 = getDao().getById(image.getId());
			BufferedImage bImage2 = ImageIO.read(new ByteArrayInputStream(image2.getData()));
			ImageIO.write(bImage2, ImageFormat.PNG.toString(), out);
			out.flush();
			out.close();
			
		}catch(Exception ex){
			fail("Error saving image: " + ex.getMessage());
		}
	}
}
