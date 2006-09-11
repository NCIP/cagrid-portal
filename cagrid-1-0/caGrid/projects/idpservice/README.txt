Prerequisites:
=======================================
Globus 4.0 installed and GLOBUS_LOCATION env defined
Tomcat > 4.0 installed and "CATALINA_HOME" env defined
MySQL installed

To Build:
=======================================
"ant all" will build 
"ant deployTomcat" will deploy to "CATALINA_HOME"

TODO:
=======================================
1. IDPServiceImpl should use Spring to instantiate/configure correct implementation of IdentityProvider.
2. IdentityProviderImpl should allow AuthenticationManager and SAMLAsserter to be pluggable.
3. SAMLAsserterImpl should look for private key password, private key, and certificate in ${user.home}/.cagrid directory.
4. Document requirement to enable read of user.home property by code running in tomcat container.
5. Document requirement to replace XML libaries in $CATALINA_HOME/common/endorsed to support OpenSAML.
