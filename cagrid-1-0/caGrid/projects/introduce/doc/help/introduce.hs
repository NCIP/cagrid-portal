<?xml version='1.0' encoding='utf-8'  ?>
<!DOCTYPE helpset
 PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
        "http://java.sun.com/products/javahelp/helpset_1_0.dtd">
<helpset version="1.0">

<!-- title -->
	<title>Introduce</title>
	<!-- maps -->
	<maps>
		<homeID>introduce12helpsystem</homeID>
		<mapref location="Introduce-map.jhm"/>
	</maps>
	<!-- TOC view -->
	<view>
		<name>TOC</name>
		<label>Contents</label>
		<type>javax.help.TOCView</type>
		<data>Introduce-toc.xml</data>
	</view>
	<!-- Index view -->
	<view>
		<name>Index</name>
		<label>Index</label>
		<type>javax.help.IndexView</type>
			<data>Introduce-index.xml</data>
	</view>
	<!-- Search view -->
	<view>
		<name>Search</name>
		<label>Search</label>
		<type>javax.help.SearchView</type>
		<data engine="com.sun.java.help.search.DefaultSearchEngine">
			JavaHelpSearch
		</data>
	</view>
</helpset>
