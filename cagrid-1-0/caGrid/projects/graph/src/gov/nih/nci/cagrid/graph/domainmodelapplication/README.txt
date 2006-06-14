The class you are interested in is the 'DomainModelExplorer' class. 
This class implements a 2 pane browser for viewing DomainModel objects.

The left pane is an outline tree whose nodes can be selected. Based on
the selection, the appropriate tabbed pagewill be  shown on the right pane.  

The DomainModelExplorer by itself is not a standalone GUI component. It needs
to be embedded in some host application.  Controlling this browser is very simple.
From that host, you can issue the following 2 methods:

     - domainModelExplorer.setDomainModel( someDomainModel );
     - domainModelExplorer.clear( );

Again, the explorer has 2 panes.  The left pane can be closed. In order to get it 
back, the host must issue:

     - domainModelExplorer.showOutlinesPane( );

If the outlines are already shown, then this does nothing [you might also want to 
use isOutlinesPaneVisible() ]

Thats it!

 