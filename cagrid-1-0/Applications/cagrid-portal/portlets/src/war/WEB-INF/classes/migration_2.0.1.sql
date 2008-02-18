
alter table uml_attr add publicID bigint;
update grid_services set metadataHash='dummy' where char_length(metadataHash) > 1;
delete from models_schemas;
update params set schema_id = null;
update ctx_prop set schema_id = null;
delete from xmlschemas;
