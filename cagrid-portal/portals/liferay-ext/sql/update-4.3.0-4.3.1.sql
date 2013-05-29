/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
============================================================================
*/
alter table BookmarksEntry add priority INTEGER;

alter table Layout add description STRING null;
alter table Layout add dlFolderId LONG;

alter table Organization_ add location BOOLEAN;

COMMIT_TRANSACTION;

update Organization_ set location = FALSE;