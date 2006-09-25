
This package contains library files from the NCICB caCORE 
standard technology stack.
The standard technology stack is listed in this document 
http://gforge.nci.nih.gov/docman/view.php/58/2782/caCORE31TechnologyUsage.xls
*cglib jar was updated from the technology stack.

All jars here are the official caCORE 3.1 released jars except client-noehcache.jar,
which is the client.jar with the ehcache.xml file removed (to avoid conflicts).

Having both jars in your classpath will not cause problems as the overlap (everything but ehcache.xml)
is byte-identical.

