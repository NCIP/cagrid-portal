# =========================================================================
# 
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Coverage.tcl,v $
#   Language:  Tcl
#   Date:      $Date: 2005-11-15 14:51:27 $
#   Version:   $Revision: 1.1 $
# 

# Copyright (c) 2001 Insight Consortium
# All rights reserved.

# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:

#  * Redistributions of source code must retain the above copyright notice,
#    this list of conditions and the following disclaimer.

#  * Redistributions in binary form must reproduce the above copyright notice,
#    this list of conditions and the following disclaimer in the documentation
#    and/or other materials provided with the distribution.

#  * The name of the Insight Consortium, nor the names of any consortium members,
#    nor of any contributors, may be used to endorse or promote products derived
#    from this software without specific prior written permission.

#   * Modified source versions must be plainly marked as such, and must not be
#     misrepresented as being the original software.

# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS ``AS IS''
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS OR CONTRIBUTORS BE LIABLE FOR
# ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
# DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
# SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
# CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
# OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
# OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  proc OpenLog { filename BuildStamp } {
    global Dart Coverage
    set Coverage(GCovOut) [open $filename w]
    
    puts $Coverage(GCovOut) $Dart(XMLHeader)
    # puts $Coverage(GCovOut) [XMLStyleSheet "CoverageLog"]
    puts $Coverage(GCovOut) "<Site BuildName=\"$Dart(BuildName)\" BuildStamp=\"$BuildStamp\" Site=\"$Dart(Site)\">"
    puts $Coverage(GCovOut) "<CoverageLog>"
    puts $Coverage(GCovOut) "\t<StartDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</StartDateTime>"
    return Coverage(GCovOut)
  }


proc CloseLog { } {
  global Coverage
  puts $Coverage(GCovOut) "\t<EndDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</EndDateTime>"
  puts $Coverage(GCovOut) "</CoverageLog>"
  puts $Coverage(GCovOut) "</Site>"
  close $Coverage(GCovOut)
}

proc GCov { file } \
{
  global Coverage
  # puts "Running gcov on $file"
  set dir [pwd]
  cd [file dir $file]
  catch { exec gcov -l [file tail $file] } Result
  # puts $Result
  cd $dir
}

proc AddFile { file } \
{
  global Coverage GCovFiles
  set GCovFiles($file) $file
}

proc AddSourceFile { file } \
{
  global Coverage
  lappend Coverage(Source) $file
}


proc PercentCoverage { t ut } \
{
  if { ( $ut + $t ) != 0 } \
  {
    return [format "%.2f" [expr 100.0 * $t / double ( $ut + $t )]]
  }
  return 0.00;
}

proc CoverageMetric { t ut } \
{
  if { ( $ut + $t ) != 0 } \
  {
    return [format "%.2f" [expr ($t + 10.0) / double ( $ut + $t  + 10.0)]]
  }
  return 0.00;
}

proc LOC { file } {
  global Coverage GCovFiles
  
  set Untested 0
  set Tested 0
  set Covered false
  
  set Files [concat [array names GCovFiles $file.gcov] [array names GCovFiles *[file tail $file].gcov]]
  set LineCounter 0
    
  foreach name $Files {
    set Covered true
    # puts "\tAdding up $GCovFiles($name)"
    set f [open $GCovFiles($name) r]
    set LineCounter 0
    while { ![eof $f] } {
      if { ![info exists Line($LineCounter)] } \
      {
        set Line($LineCounter) "-1"
      }
      set l [gets $f]
      if { [regexp "\#\#\#\#\#" $l] } \
      {
        if { $Line($LineCounter) == "-1" } \
        {
          set Line($LineCounter) 0
        }
      }
      if { [regexp {^[ ]*[0-9][0-9]*} $l n] } \
      {
        
        if { $Line($LineCounter) == "-1" } \
        {
          set Line($LineCounter) 0
        }
        incr Line($LineCounter) [string trimleft $n "0"]
      }
      if { ![info exists LineOut($LineCounter)] } \
      {
        if { [string first "\t\t" $l] == 0 } \
        {
          set LineOut($LineCounter) [string range $l 2 end]
        } \
        else \
        {
          set LineOut($LineCounter) [string range $l 15 end]
        }
      }
      incr LineCounter
    }
    close $f
  }

  set Coverage($file,IsCovered) $Covered
  set Coverage($file,UnCovered) 0
  set Coverage($file,Covered) 0
  foreach i [array names Line] \
  {
    if { $Line($i) == 0 } \
    {
      incr Coverage($file,UnCovered)
    }
    if { $Line($i) >= 1 } \
    {
      incr Coverage($file,Covered)
    }
  }
  incr Coverage(UnCovered) $Coverage($file,UnCovered)
  incr Coverage(Covered) $Coverage($file,Covered)


  # Fill in the Coverage Log
  puts $Coverage(GCovOut) "\t<File Name=\"[XMLSafeString [file tail $file]]\" FullPath=\"[XMLSafeString $file]\">"
  puts $Coverage(GCovOut) "\t\t<Report>"
  for { set i 0 } { $i < $LineCounter } { incr i } \
  {
    puts $Coverage(GCovOut) "\t\t<Line Number=\"$i\" Count=\"$Line($i)\">[XMLSafeString $LineOut($i)]</Line>"
  }
  puts $Coverage(GCovOut) "\t\t</Report>"
  puts $Coverage(GCovOut) "\t</File>"
  
}

proc RollUpCoverage { id } \
{
  global Coverage
  set DirCoverage 0
  set DirFiles 0
  set Coverage($id,Tested) 0
  set Coverage($id,Untested) 0
  foreach Dir $Coverage($id,DirList) \
  {
    RollUpCoverage $Dir
    set DirCoverage [expr $DirCoverage + $Coverage($Dir,Coverage)]
    incr DirFiles $Coverage($Dir,FileCount)
    incr Coverage($id,Tested) $Coverage($Dir,Tested)
    incr Coverage($id,Untested) $Coverage($Dir,Untested)
  }
  foreach File $Coverage($id,FileList) \
  {
    set t [LOC $File]
    set Tested [lindex $t 0]
    set Untested [lindex $t 1]
    set DirCoverage [expr $DirCoverage + $Coverage($File,Coverage)]
    set Coverage($File,Tested) $Tested
    set Coverage($File,Untested) $Untested
    incr Coverage($id,Tested) $Tested
    incr Coverage($id,Untested) $Untested
    incr DirFiles
  }
  set Coverage($id,Coverage) 0
  if { $Coverage($id,Tested) != 0 && $Coverage($id,Untested) != 0} \
  {
    set Coverage($id,Coverage) [expr $Coverage($id,Tested) / double ( $Coverage($id,Tested) + $Coverage($id,Untested))]
  }
  set Coverage($id,FileCount) $DirFiles
}

proc IsCoverageSuppressed {filename} {
  # look for a .NoDartCoverage file in the directory.
  # If it is empty, suppress the whole directory.
  # If it isn't empty, match each line as a regexp.
#  puts "Testing $filename for coverage suppression"
  if {[file isdirectory $filename] && \
    [file exists [file join $filename .NoDartCoverage]] && \
    [file size [file join $filename .NoDartCoverage]] == 0 } {
      return 0 }
  set nocoverage [file join [file dirname $filename] .NoDartCoverage]
  if {[file exists $nocoverage] &&
    [file size $nocoverage] != 0} \
  {
    set f [open $nocoverage r]
    while {! [eof $f] } {
      set l [gets $f]
#      puts "Testing $nocoverage for [file tail $filename], with $l"
      if { [expr {$l != ""} ] && [regexp $l [file tail $filename] ] } {
#        puts "Suppress"
        return 0 }
    }
  }
  return 1
}

proc Coverage { Model BuildStampDir } {
  global Dart Coverage

  set HTMLDir [file join Testing HTML]
  set TempDir [file join Testing Temporary]

  set SiteDir [file join $HTMLDir TestingResults Sites $Dart(Site)]
  set BuildNameDir [file join $SiteDir $Dart(BuildName)]

  set BuildRoot [pwd]

  # Find the build name directory
  set BuildStampDir [GetLastBuildDirectory $Model]
  if { $BuildStampDir == "" } \
  {
    puts stderr "Could not find any builds in the $BuildNameDir to do testing $Result"
    exit 1
  }

  set BuildStamp [file tail $BuildStampDir]
  set XMLDir [file join $BuildStampDir XML]

  # We can assume that Testing/HTML/TestingResults/@SITE@ is built
  file mkdir $HTMLDir $SiteDir $BuildNameDir $BuildStampDir $TempDir $XMLDir


  set Out [open [file join $XMLDir Coverage.xml] w]

  catch { unset Coverage }

  # Clean out the build directory files
  # puts "Currently in $BuildRoot"

  cd $Dart(SourceDirectory)
  set BuildDirectory $BuildRoot
  set SourceDirectory [pwd]
  set Coverage(BuildDirectory) $BuildRoot
  set Coverage(SourceDirectory) [pwd]


  # puts "Currently in [pwd]"

  set Counter 0


  # First run gcov on each *.da file
  # Next index all *.cxx, *.h, *.txx, *.c
  cd $BuildRoot
  set CoverageDirs [list "."]
  FileMap $CoverageDirs [list *.bb] GCov IsCoverageSuppressed
  FileMap $CoverageDirs [list *.gcov] AddFile IsCoverageSuppressed

  set Coverage(UnCovered) 0
  set Coverage(Covered) 0

  cd $SourceDirectory
  FileMap $CoverageDirs [list *.h *.H *.C *.c++ *.cc *.cpp *.cxx *.c *.txx] AddSourceFile IsCoverageSuppressed


  puts $Out $Dart(XMLHeader)
  # puts $Out [XMLStyleSheet "Coverage"]
  puts $Out "<Site BuildName=\"$Dart(BuildName)\" BuildStamp=\"$BuildStamp\" Name=\"$Dart(Site)\">"
  puts $Out {<Coverage>}
  puts $Out "\t<StartDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</StartDateTime>"


  cd $BuildRoot

  
  set LogCount 0
  set FileCounter 0
  OpenLog [file join $XMLDir CoverageLog-$LogCount.xml] $BuildStamp
  incr LogCount
  foreach file $Coverage(Source) \
  {
    # puts "Looking at $file"
    # Find all of the .gcov files associated, and roll up coverage
    LOC $file
    puts $Out "\t<File Name=\"[file tail $file]\" FullPath=\"$file\" Covered=\"$Coverage($file,IsCovered)\">"
    puts $Out "\t\t<LOCTested>$Coverage($file,Covered)</LOCTested>"
    puts $Out "\t\t<LOCUnTested>$Coverage($file,UnCovered)</LOCUnTested>"
    puts $Out "\t\t<PercentCoverage>[PercentCoverage $Coverage($file,Covered) $Coverage($file,UnCovered)]</PercentCoverage>"
    puts $Out "\t\t<CoverageMetric>[CoverageMetric $Coverage($file,Covered) $Coverage($file,UnCovered)]</CoverageMetric>"
    puts $Out "\t</File>"
    if { $FileCounter == 100 } {
      CloseLog 
      OpenLog [file join $XMLDir CoverageLog-$LogCount.xml] $BuildStamp
      incr LogCount
      set FileCounter 0
    }
    incr FileCounter
  }
  CloseLog

  puts $Out "\t<LOCTested>$Coverage(Covered)</LOCTested>"
  puts $Out "\t<LOCUntested>$Coverage(UnCovered)</LOCUntested>"
  puts $Out "\t<LOC>[expr $Coverage(UnCovered) + $Coverage(Covered)]</LOC>"
  puts $Out "\t<PercentCoverage>[PercentCoverage $Coverage(Covered) $Coverage(UnCovered)]</PercentCoverage>"
  puts $Out "\t<EndDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</EndDateTime>"
  puts $Out "</Coverage>"
  puts $Out "</Site>"

  puts "\tCovered LOC:         $Coverage(Covered)"
  puts "\tNot covered LOC:     $Coverage(UnCovered)"
  puts "\tTotal LOC:           [expr $Coverage(UnCovered) + $Coverage(Covered)]"
  puts "\tPercentage Coverage: [PercentCoverage $Coverage(Covered) $Coverage(UnCovered)]%"

  close $Out

}
















