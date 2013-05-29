/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
============================================================================
*/
alter table BlogsEntry add urlTitle VARCHAR(150) null;

create table BlogsStatsUser (
	statsUserId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	entryCount INTEGER,
	lastPostDate DATE null,
	ratingsTotalEntries INTEGER,
	ratingsTotalScore DOUBLE,
	ratingsAverageScore DOUBLE
);

alter table BlogsStatsUser add ratingsTotalEntries INTEGER;
alter table BlogsStatsUser add ratingsTotalScore DOUBLE;
alter table BlogsStatsUser add ratingsAverageScore DOUBLE;

delete from MBStatsUser where groupId = 0;