

alter table uml_attr add publicID bigint;

update grid_services set metadataHash='dummy' where char_length(metadataHash) > 1;