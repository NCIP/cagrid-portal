############
# TO BUILD #
############
From cagrid-1-0/Applications, type "ant build-caarray".

#############
# TO DEPLOY #
#############

 1.) Type: and deployTomcat
 2.) Edit CATALINA_HOME/bin/catalina.sh to set the java.security.policy
     system property like so:
     
     -Djava.security.policy=/path/to/policy.file
     
     That policy file should look like this:
     
     grant {
		permission java.security.AllPermission;
	 };
	 
If you are deploying the caarray grid service to a container that hosts
other caGrid services, be sure to remove:

	CATALINA_HOME/webapps/wsrf/WEB-INF/lib/castor-0.9.9.jar
	
This service uses a patched version of castor (see notes below).

#########
# NOTES #
#########

This project uses a patched version of Castor 1.0.4. 
The patch is described here: http://jira.codehaus.org/browse/CASTOR-1660

Also, this project uses a custom implementation of 
the Castor Marshaller, the class is located at 
src/org/exolab/castor/xml/Marshaller2.java. 
Basically, this implementation keeps track of the XPath location
in the document being generated. Before invoking any FieldHandler,
it checks if the XPath expression for that axis matches a supplied
regular expression.

################
# KNOWN ISSUES #
################

# Marshalling Performance #

The default behavior of the marshaller is to serialize the results
one level deep away from the target object. This results in many
RMI calls being made to populate each object in the result set.
This behavior can be configured using the marshallerXpathRegex
parameter in server-conf.wsdd. This regular expression must evaluate to true 
before traversing any association.

Alternatively, one can just use the "AttributeNames" query modifier to 
avoid serialization all together.

# CQL Support #

1. The MAGEOM API doesn't process nested sub groups correctly. 
2. The MAGEOM API doesn't support IS_NULL and IS_NOT_NULL predicates.