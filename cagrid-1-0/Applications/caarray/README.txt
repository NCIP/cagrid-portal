This project uses a patched version of Castor 1.0.4. 
The patch is described here: http://jira.codehaus.org/browse/CASTOR-1660

Also, this project uses a custom implementation of 
the Castor Marshaller, the class is located at 
src/org/exolab/castor/xml/Marshaller2.java. 
Basically, this implementation keeps track of the XPath location
in the document being generated. Before invoking any FieldHandler,
it checks if the XPath expression for that axis matches a supplied
regular expression.

