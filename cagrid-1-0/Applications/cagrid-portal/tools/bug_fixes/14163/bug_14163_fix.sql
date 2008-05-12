delete from shared_cql where class_id  not in (select distinct(id) from uml_class);
