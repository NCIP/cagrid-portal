package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenterPointOfContact;
import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Utility to create a report (excel)
 * for service statuses
 * <p/>
 * <p/>
 * Usage is
 * new ServiceStatusReportGenerator().createReport()
 * <p/>
 * But can also do
 * <p/>
 * ServiceStatusReportGenerator generator = new ServiceStatusReportGenerator()
 * HSSFWorkbook wb = generator.getWb();
 * <p/>
 * to manipulate the HSSFWorkbook before calling
 * <p/>
 * generator.writeWorkbook();
 * <p/>
 * to print the workbook
 * <p/>
 * <p/>
 * new
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceStatusReportGenerator {
    private HSSFWorkbook wb;
    public final HSSFCellStyle dateStyle;
    public static Log log = LogFactory.getLog(ServiceStatusReportGenerator.class);

    public static final String DATE_FORMAT = "m/d/yy";
    public static final String FILENAME_OPTION = "filename";
    public static final String STATUS_OPTION = "status";
    public static final String EMAIL_OPTION = "email";

    /**
     * Main method
     *
     * @param args If no arguments supplied, will generate an excel report for DORMANT service
     * @throws Exception
     */
    public static void main(String... args) throws Exception {

        Options options = new Options();
        CommandLineParser parser = new GnuParser();
        options.addOption(FILENAME_OPTION, true, "Enter filename to output ther report");
        options.addOption(STATUS_OPTION, true, "(optional)comman seperated list of Status(s) to include in the report");
        options.addOption(EMAIL_OPTION, true, "(optional)Send email to following comma seperated email addresses");

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java ServiceStatusReportGenerator", options);
        }


        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{
                        "classpath:applicationContext-db.xml", "classpath:applicationContext-aggr.xml"});

        String filename = cmd.getOptionValue(FILENAME_OPTION);


        List<ServiceStatus> statuses = new ArrayList<ServiceStatus>();
        if (cmd.hasOption(STATUS_OPTION)) {
            StringTokenizer statusTokens = new StringTokenizer(cmd.getOptionValue(STATUS_OPTION), ",");
            while (statusTokens.hasMoreTokens())
                statuses.add(ServiceStatus.valueOf(statusTokens.nextToken()));
        } else
            statuses.add(ServiceStatus.DORMANT);

        new ServiceStatusReportGenerator().createReport(filename, statuses);

        if (cmd.hasOption(EMAIL_OPTION) && cmd.getOptionValue(EMAIL_OPTION).length() > 1) {

            JavaMailSenderImpl sender = (JavaMailSenderImpl) ctx.getBean("portalMailSender");
            String portalAdminMailAddress = (String) ctx.getBean("portalAdminMailAddress");

            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Portal service status report");
            helper.setText("Automated report generated from the Portal. Attached is the list of " + statuses.toString() + "  services \n\n");
            helper.setFrom(portalAdminMailAddress);
            FileSystemResource file = new FileSystemResource(new File(filename));
            helper.addAttachment(filename, file);


            StringTokenizer emails = new StringTokenizer(cmd.getOptionValue(EMAIL_OPTION), ",");
            if (emails.countTokens() > 0) {
                while (emails.hasMoreTokens()) {
                    helper.addTo(emails.nextToken());
                }
                log.info("Will send email as requested");
                sender.send(message);
            }


        }
    }

    /**
     * Initializes the excel sheet.
     */
    public ServiceStatusReportGenerator() {
        wb = new HSSFWorkbook();
        HSSFDataFormat df = wb.createDataFormat();
        dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(df.getFormat(DATE_FORMAT));
    }

    public void createReport(String filename, List<ServiceStatus> statuses) throws Exception {
        report(statuses);
        String file = filename != null ? filename : "service_list.xls";
        writeWorkbook(file);
    }

    /**
     * Updates the excel sheet
     * with relevant service information
     *
     * @param statuses
     * @throws Exception
     */
    private void report(final List<ServiceStatus> statuses) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext-db.xml"});


        HibernateTemplate templ = (HibernateTemplate) ctx
                .getBean("hibernateTemplate");

        final HSSFSheet sheet = wb.createSheet("Service Status Report");
        addHeaderRow(sheet);


        for (final ServiceStatus status : statuses) {


            templ.execute(new HibernateCallback() {
                public Object doInHibernate(Session session)
                        throws HibernateException, SQLException {
                    int counter = 1;

                    List services = session.createQuery("from GridService").list();
                    for (Iterator i = services.iterator(); i.hasNext();) {
                        GridService svc = (GridService) i.next();

                        if (status.equals(svc.getCurrentStatus())) {
                            HSSFRow row = sheet.createRow((short) counter++);
                            addCell(row, 0, svc.getUrl());
                            ResearchCenter rc = BeanUtils.traverse(svc, "serviceMetadata.hostingResearchCenter",ResearchCenter.class);
                            if (rc != null) {
                                addCell(row, 1, rc.getShortName());

                                for (ResearchCenterPointOfContact poc : rc.getPointOfContactCollection()) {
                                    Person p = poc.getPerson();
                                    addCell(row, 2, (p.getFirstName() + p.getLastName()));
                                    addCell(row, 3, p.getEmailAddress());
                                    addCell(row, 4, p.getPhoneNumber());
                                }
                            }
                            addCell(row, 5, svc.getCurrentStatus().toString());
                            List<StatusChange> history = svc.getStatusHistory();
                            if (history.size() > 1) {

                                HSSFCell dCell = row.createCell(6);
                                dCell.setCellValue(history.get(history.size() - 1).getTime());
                                dCell.setCellStyle(dateStyle);
                            }
                        }

                    }

                    return null;
                }
            });
            sheet.autoSizeColumn((short) 0);
            sheet.autoSizeColumn((short) 1);
            sheet.autoSizeColumn((short) 2);
            sheet.autoSizeColumn((short) 3);
            sheet.autoSizeColumn((short) 4);

        }
    }

    public void writeWorkbook(String filename) throws IOException {
        log.info("Writing workbook to " + filename);

        FileOutputStream fileOut = new FileOutputStream(filename);
        wb.write(fileOut);
        fileOut.close();
        log.info("Wrote workbook to " + filename);

    }

    private void addHeaderRow(HSSFSheet sheet) {
        HSSFCellStyle hStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFFont.COLOR_RED);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hStyle.setFont(font);


        HSSFRow header = sheet.createRow((short) 0);
        addCell(header, "Service URL");
        addCell(header, "Research Center Name");
        addCell(header, "Point of Contact Name");
        addCell(header, "Point of Contact Email");
        addCell(header, "Point of Contact Phone");
        addCell(header, "Status");
        addCell(header, "Status Since");
        header.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
        header.setRowStyle(hStyle);

    }

    private void addCell(HSSFRow row, String text) {
        int col = row.getLastCellNum() > 0 ? row.getLastCellNum() : 0;
        addCell(row, col, text);
    }

    private void addCell(HSSFRow row, int col, String text) {
        row.createCell(col).setCellValue(new HSSFRichTextString(text));
    }

    public HSSFWorkbook getWb() {
        return wb;
    }

    public void setWb(HSSFWorkbook wb) {
        this.wb = wb;
    }
}
