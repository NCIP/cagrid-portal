The extensions directory is to be laid out in the following order:

->extensions
	-><extensionName>
		-extension.xml (xml file containing an {gme://gov.nih.nci.cagrid.introduce/1/Extension}ExtensionDescriptor element)
		->lib (containg the jars which are required to run this extension)
