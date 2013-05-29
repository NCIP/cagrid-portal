/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.util;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * 
 * HTML filtering utility for protecting against XSS (Cross Site Scripting).
 *
 * This code is licensed under a Creative Commons Attribution-ShareAlike 2.5 License
 * http://creativecommons.org/licenses/by-sa/2.5/
 * 
 * This code is a Java port of the original work in PHP by Cal Hendersen.
 * http://code.iamcal.com/php/lib_filter/
 *
 * The trickiest part of the translation was handling the differences in regex handling
 * between PHP and Java.  These resources were helpful in the process:
 * 
 * http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html
 * http://us2.php.net/manual/en/reference.pcre.pattern.modifiers.php
 * http://www.regular-expressions.info/modifiers.html
 * 
 * A note on naming conventions: instance variables are prefixed with a "v"; global
 * constants are in all caps.
 * 
 * Sample use:
 * String input = ...
 * String clean = new HTMLInputFilter().filter( input );
 * 
 * If you find bugs or have suggestions on improvement (especially regarding 
 * perfomance), please contact me at the email below.  The latest version of this
 * source can be found at
 * 
 * http://josephoconnell.com/java/xss-html-filter/
 * 
 * JAP: Changes
 * <ul>
 *  <li>allowing https protocol</li>
 * </ul>
 *
 * @author Joseph O'Connell <joe.oconnell at gmail dot com>
 * @version 1.0 
 */
public class HTMLInputFilterTest extends TestCase
{  
  protected HTMLInputFilter vFilter;
  
  protected void setUp() 
  { 
    vFilter = new HTMLInputFilter(false);
  }
  
  protected void tearDown()
  {
    vFilter = null;
  }
  
  private void t( String input, String result )
  {
    Assert.assertEquals( result, vFilter.filter(input) );
  }
  
  public void test_basics()
  {
    t( "", "" );
    t( "hello", "hello" );
  }
  
  public void test_balancing_tags()
  {
    t( "<b>hello", "<b>hello</b>" );
    t( "<b>hello", "<b>hello</b>" );
    t( "hello<b>", "hello" );
    t( "hello</b>", "hello" );
    t( "hello<b/>", "hello" );
    t( "<b><b><b>hello", "<b><b><b>hello</b></b></b>" );
    t( "</b><b>", "" );
  }
  
  public void test_end_slashes()
  {
    t("<img>","<img />");
    t("<img/>","<img />");
    t("<b/></b>","");
  }
  
  public void test_balancing_angle_brackets()
  {
    if (HTMLInputFilter.ALWAYS_MAKE_TAGS) {
      t("<img src=\"foo\"","<img src=\"foo\" />");
      t("i>","");
      t("<img src=\"foo\"/","<img src=\"foo\" />");
      t(">","");
      t("foo<b","foo");
      t("b>foo","<b>foo</b>");
      t("><b","");
      t("b><","");
      t("><b>","");
    } else {
      t("<img src=\"foo\"","&lt;img src=\"foo\"");
      t("b>","b&gt;");
      t("<img src=\"foo\"/","&lt;img src=\"foo\"/");
      t(">","&gt;");
      t("foo<b","foo&lt;b");
      t("b>foo","b&gt;foo");
      t("><b","&gt;&lt;b");
      t("b><","b&gt;&lt;");
      t("><b>","&gt;");
    }
  }
  
  public void test_attributes()
  {
    t("<img src=foo>","<img src=\"foo\" />"); 
    t("<img asrc=foo>","<img />");
    t("<img src=test test>","<img src=\"test\" />"); 
  }
  
  public void test_disallow_script_tags()
  {
    t("<script>","");
    if (HTMLInputFilter.ALWAYS_MAKE_TAGS) { t("<script","");  } else { t("<script","&lt;script"); }
    t("<script/>","");
    t("</script>","");
    t("<script woo=yay>","");
    t("<script woo=\"yay\">","");
    t("<script woo=\"yay>","");
    t("<script woo=\"yay<b>","");
    t("<script<script>>","");
    t("<<script>script<script>>","script");
    t("<<script><script>>","");
    t("<<script>script>>","");
    t("<<script<script>>","");
  }
  
  public void test_protocols()
  {
  	t("<a href=\"http://foo\">bar</a>", "<a href=\"http://foo\">bar</a>");
  	t("<a href=\"https://foo\">bar</a>", "<a href=\"https://foo\">bar</a>");
    // we don't allow ftp. t("<a href=\"ftp://foo\">bar</a>", "<a href=\"ftp://foo\">bar</a>");
    t("<a href=\"mailto:foo\">bar</a>", "<a href=\"mailto:foo\">bar</a>");
    t("<a href=\"javascript:foo\">bar</a>", "<a href=\"#foo\">bar</a>");
    t("<a href=\"java script:foo\">bar</a>", "<a href=\"#foo\">bar</a>");
    t("<a href=\"java\tscript:foo\">bar</a>", "<a href=\"#foo\">bar</a>");
    t("<a href=\"java\nscript:foo\">bar</a>", "<a href=\"#foo\">bar</a>");
    t("<a href=\"java" + HTMLInputFilter.chr(1) + "script:foo\">bar</a>", "<a href=\"#foo\">bar</a>");
    t("<a href=\"jscript:foo\">bar</a>", "<a href=\"#foo\">bar</a>");
    t("<a href=\"vbscript:foo\">bar</a>", "<a href=\"#foo\">bar</a>");
    t("<a href=\"view-source:foo\">bar</a>", "<a href=\"#foo\">bar</a>");
  }
  
  public void test_self_closing_tags()
  {
    t("<img src=\"a\">","<img src=\"a\" />");
    t("<img src=\"a\">foo</img>", "<img src=\"a\" />foo");
    t("</img>", "");
  }
  
  public void test_comments()
  {
    if (HTMLInputFilter.STRIP_COMMENTS) {
      t("<!-- a<b --->", "");
    } else {
      t("<!-- a<b --->", "<!-- a&lt;b --->");
    }
  }
  


}