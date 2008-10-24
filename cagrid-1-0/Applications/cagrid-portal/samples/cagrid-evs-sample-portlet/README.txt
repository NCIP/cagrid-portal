
---------------------------------------------------------------------
Overview
---------------------------------------------------------------------
This is a simple Portlet that is intended to serve as a sample for
caGrid/caBIG Portlet developers. It shows the following functionality

1. How to write a portlet for Liferay (caGrid Portal).
2. How to leverage caGrid API
3. How to use caGrid Ivy repository
4. How to use Spring Portlet MVC API. More details on
Spring Portlet support can be found here
http://static.springframework.org/spring/docs/2.0.x/reference/portlet.html

The Portlet implements the following use case.

-It takes in a keyword and then searches the NCI Metathesaurus(EVS)
 using the EVS grid service for the corresponding concept code.



---------------------------------------------------------------------
Build Instructions
---------------------------------------------------------------------

This project can be built with Apache Ant. Make sure you have
the ant executable included in your PATH. To build the Portlet
simply run the following command -

    ant war

This will build the sample Portlet and create an archive
build/evs-sample-portlet.war



---------------------------------------------------------------------
Deployment
----------------------------------------------------------------

The build process of this Portlet uses the caGrid ivy repository.
Details on how to leverage the caGrid  Ivy respository can be found
here

http://www.cagrid.org/wiki/CaGrid:How-To:DependOnCaGridLibraries



---------------------------------------------------------------------
Deployment
----------------------------------------------------------------

This portlet is intended to be deployed as a community Portlet
in the caGrid Portal. More information on the caGrid Portal
can be found here

http://www.cagrid.org/wiki/Portal:Portal

To deploy this locally, you will first need to install a local
instance of the caGrid Portal and then deploy the sample Portlet
into the Portal. Instructions are available on the following
wiki page

http://www.cagrid.org/wiki/Portal:2.1:CommunityPortlets



---------------------------------------------------------------------
Next Steps
----------------------------------------------------------------

Once developers have deployed this sample in the caGrid Portal.
They can then enhance the sample (or use it as a reference)
to suit their development needs.

Please feel free to communicate with the caGrid Portal team to discuss
ideas and issues in Portlet development. The team can be contacted
through the Users list at CAGRID_USERS-L@LIST.NIH.GOV

We will also be able to assist you in deploying your Portlet in the
training caGrid Portal at

http://portal.training.cagrid.org/web/guest/home