the graph project currently holds several disparate sub-projects that should
be moved to their own places.  There are dependencies amongst them however.  

The first project is the .../uml folder. This folder contains the 'UMLDiagram'
class which is an implementation of a UML Diagram viewer component.

The next project is  .../domainmodelapplication folder.  This folder contains 
a browser that can display multiple UML diagrams for packages in a 
DomainModel tabbed out in the same window.  

Finally, the last one is the .../vstheme folder. This folder contains implementations
of MDI panels that have the visual studio theme.  This is used by the .../domainmodelapplication
code.

the .../geometry folder should belong together with the .../uml folder.