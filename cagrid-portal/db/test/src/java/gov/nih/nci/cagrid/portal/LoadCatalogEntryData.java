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
package gov.nih.nci.cagrid.portal;

import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipType;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleType;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryWorkflowStatus;
import gov.nih.nci.cagrid.portal.domain.catalog.Citation;
import gov.nih.nci.cagrid.portal.domain.catalog.Comment;
import gov.nih.nci.cagrid.portal.domain.catalog.Commentable;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.DataSetCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.DesktopToolCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.File;
import gov.nih.nci.cagrid.portal.domain.catalog.Hyperlink;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.Rating;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class LoadCatalogEntryData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LoadCatalogEntryData d = new LoadCatalogEntryData();
		d.run();
	}

	public void run() {
		try {
			TestDB.create();

			HibernateTemplate templ = (HibernateTemplate) TestDB
					.getApplicationContext().getBean("hibernateTemplate");

			templ.execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {

					// Create catalog entry for John Doe

					Person person1 = new Person();
					session.saveOrUpdate(person1);

					PortalUser portalUser1 = new PortalUser();
					portalUser1.setPerson(person1);
					session.save(portalUser1);

					PersonCatalogEntry person1ce = new PersonCatalogEntry();
					person1ce.setName("John Doe Catalog Entry");
					person1ce
							.setDescription("Description of catalog entry for John Doe.");
					person1ce.setAuthor(portalUser1);
					person1ce.setCreatedAt(new Date());
					person1ce.setNumViews(0);
					person1ce.setPublished(true);
					person1ce
							.setWorkflowStatus(CatalogEntryWorkflowStatus.APPROVED);

					person1ce.setSalutation("Mr.");
					person1ce.setFirstName("John");
					person1ce.setMiddleName("A.");
					person1ce.setLastName("Doe");

					person1ce.setStreet1("some street1");
					person1ce.setStreet2("some street2");
					person1ce.setLocality("some locality");
					person1ce.setStateProvince("VA");
					person1ce.setPostalCode("12345");
					person1ce.setCountryCode("US");
					person1ce.setEmailAddress("john.doe@seomwhere.com");
					person1ce.setPhoneNumber("301-867-5309");

					person1ce.setAddressPublic(true);
					person1ce.setEmailAddressPublic(true);
					person1ce.setPhoneNumberPublic(true);
//					person1ce.setAbout(portalUser1);

					session.save(person1ce);
					addComment(session, person1ce, person1ce,
							"A comment on my own catalog entry.");

					portalUser1.setCatalog(person1ce);
					portalUser1.getCatalogEntries().add(person1ce);
					session.update(person1ce);

					addCitation(session, person1ce, "some citation", "PM123456");

					Hyperlink hyp1 = addHyperlink(session, person1ce,
							"This is my homepage.",
							"http://some.host.com/myhomepage.html");
					addComment(session, person1ce, hyp1,
							"A comment on my own homepage hyperlink.");

					File file1 = addFile(session, person1ce, "some_file.txt",
							"A comment on my own file.", ".txt", 1000L);
					addComment(session, person1ce, file1,
							"A comment on this file.");

					// Create catalog entry for Some Dataset
					DataSetCatalogEntry dataset1ce = new DataSetCatalogEntry();
					dataset1ce.setCreatedAt(new Date());
					dataset1ce.setName("Some Dataset");
					dataset1ce.setDescription("Some Dataset Description");
					dataset1ce.setNumViews(0);
					dataset1ce.setPublished(true);
					dataset1ce
							.setWorkflowStatus(CatalogEntryWorkflowStatus.APPROVED);
					dataset1ce.setAuthor(portalUser1);
					dataset1ce.setContributor(person1ce);
					session.save(dataset1ce);

					portalUser1.getCatalogEntries().add(dataset1ce);
					session.update(portalUser1);

					person1ce.getContributions().add(dataset1ce);
					session.update(person1ce);

					addCitation(session, dataset1ce,
							"some citation for this data set", "PM452986");
					addComment(session, person1ce, dataset1ce,
							"A comment on this dataset.");

					Hyperlink hyp2 = addHyperlink(session, dataset1ce,
							"Homepage for this dataset.",
							"http://some.host.com/adataset.html");
					addComment(session, person1ce, hyp2,
							"A comment on this dataset hyperlink.");

					File file2 = addFile(session, dataset1ce,
							"some_example_file.xml",
							"An example dataset file.", ".xml", 1000L);
					addComment(session, person1ce, file2,
							"A comment on this dataset file.");

					Rating rating1 = addRating(session, person1ce, dataset1ce,
							5);
					addComment(session, person1ce, rating1,
							"A comment on this rating.");

					// Create relationship types

					CatalogEntryRelationshipType dataSetPersonRelType = createRelationshipType(
							session,
							null,
							"DataSetPerson",
							"A person can be related to a dataset and vice versa.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.DataSetCatalogEntry",
							"DataSetPersonRole",
							"A dataset can be related to a person.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"DataSetPersonOfRole",
							"A person can be related to a dataset.");

					CatalogEntryRelationshipType dataSetUserPersonRelType = createRelationshipType(
							session,
							dataSetPersonRelType,
							"DataUserSetPerson",
							"A person uses a dataset and a dataset is used by a person.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.DataSetCatalogEntry",
							"DataSetUserPersonRole",
							"A dataset is used by a person.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"DataSetUserPersonOfRole",
							"A person uses a dataset.");

					CatalogEntryRelationshipInstance relInst1 = assertRelationship(
							session, person1ce, dataset1ce,
							dataSetUserPersonRelType.getRoleTypeA(),
							"Some Dataset is used by John Doe.",
							dataSetUserPersonRelType.getRoleTypeB(),
							"John Doe uses Some Dataset.");

					addComment(session, person1ce, relInst1,
							"comment on the whole relationship instance");
					addComment(session, person1ce, relInst1.getRoleA(),
							"comment on just the Some Dataset -> John Doe role");
					addComment(session, person1ce, relInst1.getRoleB(),
							"comment on just the John Doe -> Some Dataset role");
					
					

					// Tool to vizualize dataset
					DesktopToolCatalogEntry tool1ce = new DesktopToolCatalogEntry();
					tool1ce.setCreatedAt(new Date());
					tool1ce.setName("Some Dataset Vizualization Tool");
					tool1ce
							.setDescription("Description of this tool (e.g. its feature set).");
					tool1ce.setAuthor(portalUser1);
					tool1ce.setContributor(person1ce);

					session.save(tool1ce);

					portalUser1.getCatalogEntries().add(tool1ce);
					session.update(portalUser1);

					person1ce.getContributions().add(tool1ce);
					session.update(person1ce);

					addCitation(session, tool1ce,
							"some citation for this data tool", "PM8873045");
					addComment(session, person1ce, tool1ce,
							"A comment on this tool.");

					Hyperlink hyp3 = addHyperlink(session, tool1ce,
							"Homepage for this tool.",
							"http://some.host.com/atool.html");
					addComment(session, person1ce, hyp3,
							"A comment on this tool hyperlink.");

					File file3 = addFile(session, tool1ce,
							"some_example_file_for_tool.dat",
							"An example file for tool.", ".dat", 1000L);
					addComment(session, person1ce, file3,
							"A comment on this tool example file.");

					Rating rating2 = addRating(session, person1ce, tool1ce, 3);
					addComment(session, person1ce, rating2,
							"A comment on this rating of the tool.");
					
					

					CatalogEntryRelationshipType toolPersonRelType = createRelationshipType(
							session,
							null,
							"ToolPerson",
							"A person can be related to a tool and vice versa.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.ToolCatalogEntry",
							"ToolPersonRole",
							"A tool can be related to a person.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"ToolPersonOfRole",
							"A person can be related to a tool.");

					CatalogEntryRelationshipType toolPersonCreatorRelType = createRelationshipType(
							session,
							toolPersonRelType,
							"ToolPersonCreator",
							"A person creates a tool and a tool is created by a person.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.ToolCatalogEntry",
							"ToolPersonCreatorRole",
							"A tool is created by a person.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"ToolPersonCreatorOfRole",
							"A person creates a tool.");

					CatalogEntryRelationshipInstance relInst2 = assertRelationship(
							session,
							person1ce,
							tool1ce,
							toolPersonCreatorRelType.getRoleTypeA(),
							"John Doe is the creator of Some Dataset Visualization Tool",
							toolPersonCreatorRelType.getRoleTypeB(),
							"Some Dataset Vizualization Tool is created by John Doe.");
					

					addComment(session, person1ce, relInst2,
							"comment on the whole relationship instance");
					addComment(session, person1ce, relInst2.getRoleA(),
							"comment on just the Some Dataset Vizualization Tool -> John Doe role");
					addComment(session, person1ce, relInst2.getRoleB(),
							"comment on just the John Doe -> Some Dataset Vizualization Tool role");
					
					

					CatalogEntryRelationshipType datasetToolRelType = createRelationshipType(
							session,
							null,
							"DataSetTool",
							"A dataset can be related to a tool and vice versa.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.DataSetCatalogEntry",
							"DataSetToolRole",
							"A dataset can be related to a tool.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.ToolCatalogEntry",
							"DataSetToolOfRole",
							"A tool can be related to a dataset.");

					CatalogEntryRelationshipType datasetVizualizationToolRelType = createRelationshipType(
							session,
							datasetToolRelType,
							"DataSetVizualizationTool",
							"A dataset can be vizualized by a tool and a tool is used to visualize a dataset.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.DataSetCatalogEntry",
							"DataSetVizualizationToolRole",
							"A dataset is visualized by a tool.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.DataSetVisualizationToolCatalogEntry",
							"DataSetVizualizationToolOfRole",
							"A tool vizualizes a dataset.");

					CatalogEntryRelationshipInstance relInst3 = assertRelationship(
							session,
							dataset1ce,
							tool1ce,
							datasetVizualizationToolRelType.getRoleTypeA(),
							"Some Dataset is visualized by Some Dataset Visualization Tool",
							datasetVizualizationToolRelType.getRoleTypeB(),
							"Some Dataset Vizualization Tool vizualizes Some Dataset.");

					Comment c1 = addComment(session, person1ce, relInst3,
							"comment on the whole relationship instance");
					// TODO: Comment should be commentable
					// addComment(session, person1ce, c1, "comment on a
					// comment");

					addComment(session, person1ce, relInst3.getRoleA(),
							"comment on just the Some Dataset Vizualization Tool -> Some Dataset role");
					addComment(session, person1ce, relInst3.getRoleB(),
							"comment on just the Some Dataset -> Some Dataset Vizualization Tool role");

					// Community that uses tool and maintains data set
					CommunityCatalogEntry community1ce = new CommunityCatalogEntry();
					community1ce.setName("Community 1");
					community1ce
							.setDescription("Description of the interests of the community");

					community1ce.setAuthor(portalUser1);
					community1ce.setContributor(person1ce);

					session.save(community1ce);

					portalUser1.getCatalogEntries().add(community1ce);
					session.update(portalUser1);

					person1ce.getContributions().add(community1ce);
					session.update(person1ce);

					addCitation(session, community1ce,
							"some citation for this community", "PM44444");
					addComment(session, person1ce, community1ce,
							"A comment on this community.");

					Hyperlink hyp4 = addHyperlink(session, community1ce,
							"Homepage for this community.",
							"http://some.host.com/acommunity.html");
					addComment(session, person1ce, hyp4,
							"A comment on this community hyperlink.");

					Rating rating3 = addRating(session, person1ce,
							community1ce, 4);
					addComment(session, person1ce, rating3,
							"A comment on this rating of the community.");

					CatalogEntryRelationshipType communityPersonRelType = createRelationshipType(
							session,
							null,
							"CommunityPerson",
							"A community can be related to a person and vice versa.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.CommunityCatalogEntry",
							"CommunityPersonRole",
							"A community can be related to a person.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"CommunityPersonOfRole",
							"A person can be related to a community.");

					CatalogEntryRelationshipType communityPersonMemberRelType = createRelationshipType(
							session,
							communityPersonRelType,
							"CommunityPersonMember",
							"A community has persons as members and persons are members of communities.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.CommunityCatalogEntry",
							"CommunityPersonMemberRole",
							"A community has members persons.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"CommunityPersonMemberOfRole",
							"Persons are members of communities.");

					CatalogEntryRelationshipInstance relInst4 = assertRelationship(
							session, community1ce, person1ce,
							communityPersonMemberRelType.getRoleTypeA(),
							"Community 1 has John Doe as a member.",
							communityPersonMemberRelType.getRoleTypeB(),
							"John Doe is a member of Community 1.");

					addComment(session, person1ce, relInst4,
							"comment on the whole relationship instance");

					addComment(session, person1ce, relInst4.getRoleA(),
							"comment on just the Community 1 -> John Doe role");
					addComment(session, person1ce, relInst4.getRoleB(),
							"comment on just the John Doe -> Community 1 role");
					
					
					//Make John Doe POC for tool, dataset, and community
					CatalogEntryRelationshipType pocRelType = createRelationshipType(
							session,
							null,
							"PointOfContact",
							"A person can be a POC for any catalog entry.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.CatalogEntry",
							"PointOfContactRole",
							"A person is POC for some catalog entry.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"PointOfContactOfRole",
							"A catalog entry has one or more persons as POCs.");

					CatalogEntryRelationshipType pocOpRelType = createRelationshipType(
							session,
							pocRelType,
							"OperationalPointOfContact",
							"A person can be the operational POC for some catalog entry.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.CatalogEntry",
							"OperationalPointOfContactRole",
							"A person is the operation POC for some catalog entry.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"OperationalPontOfContactOfRole",
							"A catalog entry has some person as the operational POC.");
					
					CatalogEntryRelationshipInstance relInst5 = assertRelationship(
							session, person1ce, dataset1ce,
							pocOpRelType.getRoleTypeA(),
							"John Doe is the operational POC for Some Dataset.",
							pocOpRelType.getRoleTypeB(),
							"Some Dataset has John Doe as operational POC.");					
					
					CatalogEntryRelationshipType pocOrgRelType = createRelationshipType(
							session,
							pocRelType,
							"OrganizationalPointOfContact",
							"A person can be the organizational POC for some catalog entry.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.CatalogEntry",
							"OrganizationalPointOfContactRole",
							"A person is the organizational POC for some catalog entry.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"OrganizationalPontOfContactOfRole",
							"A catalog entry has some person as the organizational POC.");
					
					CatalogEntryRelationshipInstance relInst6 = assertRelationship(
							session, person1ce, community1ce,
							pocOrgRelType.getRoleTypeA(),
							"John Doe is the organizational POC for Community 1.",
							pocOrgRelType.getRoleTypeB(),
							"Community 1 has John Doe as organizational POC.");
					
					CatalogEntryRelationshipType pocTechRelType = createRelationshipType(
							session,
							pocRelType,
							"TechnicalPointOfContact",
							"A person can be the technical POC for some catalog entry.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.CatalogEntry",
							"TechnicalPointOfContactRole",
							"A person is the technical POC for some catalog entry.",
							"gov.nih.nci.cagrid.portal.portlet.domain.catalog.PersonCatalogEntry",
							"TechnicalPontOfContactOfRole",
							"A catalog entry has some person as the technical POC.");
					
					CatalogEntryRelationshipInstance relInst7 = assertRelationship(
							session, person1ce, tool1ce,
							pocTechRelType.getRoleTypeA(),
							"John Doe is the technical POC for Some Dataset Vizualization Tool.",
							pocOrgRelType.getRoleTypeB(),
							"Some Dataset Vizualization Tool has John Doe as technical POC.");

					//Create an institution
					
					
					
					addComment(session, person1ce, relInst1,
							"comment on the whole relationship instance");
					addComment(session, person1ce, relInst1.getRoleA(),
							"comment on just the Some Dataset -> John Doe role");
					addComment(session, person1ce, relInst1.getRoleB(),
							"comment on just the John Doe -> Some Dataset role");
					
					

					List entries = session.createQuery("from CatalogEntry")
							.list();
					for (Iterator i = entries.iterator(); i.hasNext();) {
						CatalogEntry entry = (CatalogEntry) i.next();
						System.out.println(entry.getClass().getName() + ":"
								+ entry.getId());
					}

					return null;
				}

			});
		} catch (Exception ex) {
			throw new RuntimeException("Error loading data: " + ex.getMessage());
		}
	}

	public Rating addRating(Session session, PersonCatalogEntry pce,
			CatalogEntry ce, int score) {

		Rating rating = new Rating();
		rating.setRating(score);
		rating.setCreatedAt(new Date());
		rating.setRatingContributor(pce);
		rating.setRatingOf(ce);
		session.save(rating);

		ce.getRatings().add(rating);
		session.update(ce);

		return rating;
	}

	public CatalogEntryRelationshipInstance assertRelationship(Session session,
			CatalogEntry ceA, CatalogEntry ceB, CatalogEntryRoleType roleTypeA,
			String roleTypeADesc, CatalogEntryRoleType roleTypeB,
			String roleTypeBDesc) {

		CatalogEntryRelationshipInstance relInst = new CatalogEntryRelationshipInstance();
		relInst.setCreatedAt(new Date());
		relInst.setSince(new Date());
		relInst.setType(roleTypeA.getRelationshipType());
		session.save(relInst);

		CatalogEntryRoleInstance roleAInst = new CatalogEntryRoleInstance();
		roleAInst.setCatalogEntry(ceA);
		roleAInst.setType(roleTypeA);
		roleAInst.setDescription(roleTypeADesc);
		roleAInst.setRelationship(relInst);
		roleAInst.setCreatedAt(new Date());
		session.save(roleAInst);
		ceA.getRoles().add(roleAInst);
		session.update(ceA);

		CatalogEntryRoleInstance roleBInst = new CatalogEntryRoleInstance();
		roleBInst.setCatalogEntry(ceB);
		roleBInst.setType(roleTypeB);
		roleBInst.setDescription(roleTypeBDesc);
		roleBInst.setRelationship(relInst);
		roleBInst.setCreatedAt(new Date());
		session.save(roleBInst);
		ceB.getRoles().add(roleBInst);
		session.update(ceB);

		relInst.setRoleA(roleAInst);
		relInst.setRoleB(roleBInst);
		session.update(relInst);

		return relInst;

	}

	public CatalogEntryRelationshipType createRelationshipType(Session session,
			CatalogEntryRelationshipType parentRel, String relTypeName,
			String relTypeDesc, String roleAType, String roleAName, String roleADesc,
			String roleBType, String roleBName, String roleBDesc) {

		CatalogEntryRelationshipType ceRelType1 = new CatalogEntryRelationshipType();
		ceRelType1.setName(relTypeName);
		ceRelType1.setDescription(relTypeDesc);
		ceRelType1.setTemporal(true);
		ceRelType1.setParent(parentRel);
		session.save(ceRelType1);

		CatalogEntryRoleType ceRoleType1 = new CatalogEntryRoleType();
		ceRoleType1.setName(roleAType);
		ceRoleType1.setName(roleAName);
		ceRoleType1.setDescription(roleADesc);
		ceRoleType1.setRelationshipType(ceRelType1);
		session.save(ceRoleType1);

		CatalogEntryRoleType ceRoleType2 = new CatalogEntryRoleType();
		ceRoleType2.setName(roleBType);
		ceRoleType2.setName(roleBName);
		ceRoleType2.setDescription(roleBDesc);
		ceRoleType2.setRelationshipType(ceRelType1);
		session.save(ceRoleType2);

		ceRelType1.setRoleTypeA(ceRoleType1);
		ceRelType1.setRoleTypeB(ceRoleType2);

		return ceRelType1;
	}

	public File addFile(Session session, CatalogEntry entry, String name,
			String desc, String fileType, long sizeInBytes) {
		File file1 = new File();
		file1.setName(name);
		file1.setDescription(desc);
		file1.setSizeInBytes(sizeInBytes);
		file1.setCreatedAt(new Date());
		file1.setFileOf(entry);
		session.save(file1);

		entry.getFiles().add(file1);
		session.update(entry);

		return file1;
	}

	public Hyperlink addHyperlink(Session session, CatalogEntry entry,
			String desc, String url) {
		Hyperlink hyp1 = new Hyperlink();
		hyp1.setCreatedAt(new Date());
		hyp1.setDescription(desc);
		try {
			hyp1.setUrl(new URL(url));
		} catch (MalformedURLException ex) {
			throw new RuntimeException("bad URL: " + ex.getMessage(), ex);
		}
		hyp1.setHyperlinkOf(entry);
		session.save(hyp1);

		entry.getHyperlinks().add(hyp1);
		session.update(entry);
		return hyp1;
	}

	public Comment addComment(Session s, PersonCatalogEntry pce, Commentable c,
			String commentText) {
		Comment comment = new Comment();
		comment.setCreatedAt(new Date());
		comment.setCommentText(commentText);
		comment.setCommentor(pce);
		s.save(comment);
		c.getComments().add(comment);
		s.update(c);
		return comment;
	}

	public Citation addCitation(Session session, CatalogEntry entry,
			String citation, String pubMedID) {
		Citation cit1 = new Citation();
		cit1.setCitation(citation);
		cit1.setCreatedAt(new Date());
		cit1.setPubMedID(pubMedID);
		session.save(cit1);

		entry.getCitations().add(cit1);
		session.update(entry);

		return cit1;
	}

}
