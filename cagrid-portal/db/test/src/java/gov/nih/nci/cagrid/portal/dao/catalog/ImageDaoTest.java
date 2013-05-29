/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

/**
 * @author joshua
 * 
 */
public class ImageDaoTest extends DaoTestBase<ImageDao> {

    private String originalImgFolder = "test/data/";
    private String originalImgName = "photo-person-cc-medium"; // creative commons licensed photo from:
                                                        // http://www.flickr.com/photos/daexus/329687468
    private String originalImgType = "jpg";

    private String suffix;

    @Before
    public void init() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        suffix = dateFormat.format(new java.util.Date());
    }

    @Test
    public void save() {
        try {
            // String inFile = "test/data/person_placeholder_180px.png";
            // String outFile = "test/build/person_placeholder_180px.png";
            String inFile = originalImgFolder + originalImgName + "." + originalImgType;
            String outFile = "test/build/" + originalImgName + "_save_" + suffix + "." + originalImgType;

            FileInputStream in = new FileInputStream(inFile);
            BufferedImage bImage = ImageIO.read(in);
            in.close();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage, ImageFormat.JPEG.toString(), bos);
            Image image = new Image();
            image.setData(bos.toByteArray());
            // image.setHeight(bImage.getHeight());
            // image.setWidth(bImage.getWidth());
            image.setType(bImage.getType());
            image.setFormat(ImageFormat.JPEG);
            getDao().save(image);

            FileOutputStream out = new FileOutputStream(outFile);
            Image image2 = getDao().getById(image.getId());
            BufferedImage bImage2 = ImageIO.read(new ByteArrayInputStream(image2.getData()));
            ImageIO.write(bImage2, ImageFormat.JPEG.toString(), out);
            out.flush();
            out.close();

        } catch (Exception ex) {
            fail("Error saving image: " + ex.getMessage());
        }
    }
}
