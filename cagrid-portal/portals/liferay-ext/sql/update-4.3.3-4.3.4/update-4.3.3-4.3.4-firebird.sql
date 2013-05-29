/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
============================================================================
*/
alter table BlogsEntry add urlTitle varchar(150);

create table BlogsStatsUser (
	statsUserId int64 not null primary key,
	groupId int64,
	companyId int64,
	userId int64,
	entryCount integer,
	lastPostDate timestamp,
	ratingsTotalEntries integer,
	ratingsTotalScore double precision,
	ratingsAverageScore double precision
);

alter table BlogsStatsUser add ratingsTotalEntries integer;
alter table BlogsStatsUser add ratingsTotalScore double precision;
alter table BlogsStatsUser add ratingsAverageScore double precision;

delete from MBStatsUser where groupId = 0;
