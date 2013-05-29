/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
============================================================================
*/
alter table BlogsEntry add urlTitle varchar(150) null;

create table BlogsStatsUser (
	statsUserId bigint not null primary key,
	groupId bigint,
	companyId bigint,
	userId bigint,
	entryCount integer,
	lastPostDate date null,
	ratingsTotalEntries integer,
	ratingsTotalScore double,
	ratingsAverageScore double
);

alter table BlogsStatsUser add ratingsTotalEntries integer;
alter table BlogsStatsUser add ratingsTotalScore double;
alter table BlogsStatsUser add ratingsAverageScore double;

delete from MBStatsUser where groupId = 0;
