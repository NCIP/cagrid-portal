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
	statsUserId int8 not null primary key,
	groupId int8,
	companyId int8,
	userId int8,
	entryCount int,
	lastPostDate datetime YEAR TO FRACTION,
	ratingsTotalEntries int,
	ratingsTotalScore float,
	ratingsAverageScore float
)
extent size 16 next size 16
lock mode row;

alter table BlogsStatsUser add ratingsTotalEntries int;
alter table BlogsStatsUser add ratingsTotalScore float;
alter table BlogsStatsUser add ratingsAverageScore float;

delete from MBStatsUser where groupId = 0;
