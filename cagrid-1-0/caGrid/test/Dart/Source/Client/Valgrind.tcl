# =========================================================================
# 
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Valgrind.tcl,v $
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
proc CheckFile { Filename } {
  return [expr [file exists $Filename] && [file executable $Filename]]
}

proc ReportError { Status Result } {
  global errorInfo
  if { $Status } {
    puts $Result
    puts $errorInfo
  }
}

proc RemoveFile { file } \
{
  file delete -force -- $file
}

proc StripValgrind { Data } {
  set out ""
  foreach line [split $Data "\n"] {
    # puts "$line"
    if { [regexp {==([0-9])+==} $line] } {
      append out "$line\n"
    }
  }
  return $out
}

proc ProcessValgrindLog { Test VGLog } \
{
  global Valgrind
  if { ![file exists $VGLog] } \
  {
    set Valgrind([lindex $Test 0],Status) failed
    return
  }
  set f [open $VGLog r]

  set Valgrind([lindex $Test 0],ValgrindLog) ""
  foreach Message $Valgrind(Messages) \
  {
    set Valgrind([lindex $Test 0],$Message) 0
  }
  while { ![eof $f] } \
  {
    set line [gets $f]
    append Valgrind([lindex $Test 0],ValgrindLog) [XMLSafeString "$line\n"]
    set Message ""
    if { [regexp "==\[0-9\]+==.*blocks are still reachable in loss" $line] } \
    { set Message "Memory Leak" }
    if { [regexp "==\[0-9\]+==.*blocks are definitely lost in loss" $line] } \
    {
      set Message "Memory Leak"
    }
    if { [regexp "==\[0-9\]+==.*Invalid read of size" $line] } \
    {
      set Message "Invalid mem access"
    }
    if { [regexp "==\[0-9\]+==.*Address 0x.+ is on thread .+s stack" $line] } \
    {
      set Message "Invalid mem access"
    }
    if { [regexp "==\[0-9\]+==.*Conditional jump or move depends on uninitialised value" $line] } \
    {
      set Message "Invalid mem access"
    }
    if { [regexp "==\[0-9\]+==.*contains  .* unaddressable byte" $line] } \
    {
      set Message "Invalid mem access"
    }
    if { [regexp "==\[0-9\]+==.*Use of uninitialised value of size" $line] } \
    {
      set Message "Invalid mem access"
    }
    if { [regexp "==\[0-9\]+==.*Invalid write of size" $line] } \
    {
      set Message "Invalid mem access"
    }
    if { [regexp "==\[0-9\]+==.*Address .* is not stack'd, malloc'd or free'd" $line] } \
    {
      set Message "Invalid mem access"
    }
    if { [regexp "==\[0-9\]+==.*Jump to the invalid address" $line] } \
    {
      set Message "Invalid jump"
    }
    if { [regexp "==\[0-9\]+==.*Mismatched free.*delete.*delete" $line] } \
    {
      set Message "Mismatched deallocation"
    }
    if { [regexp "==\[0-9\]+==.*Source and destination overlap in" $line] } \
    {
      set Message "Invalid syscall param"
    }
    if { $Message != "" } \
    {
      # puts "$Message : [lindex $Test 0] $line"
      incr Valgrind([lindex $Test 0],$Message)
      incr Valgrind($Message,Count)
    }
  }
}


proc Valgrind { Model BuildStampDir } {
  global Dart tcl_platform Valgrind errorInfo TestList
  set HTMLDir [file join Testing HTML]
  set TempDir [file join Testing Temporary]
  set TempFile [file join $Dart(BuildDirectory) $TempDir ValgrindLog.txt]
  set UtilitiesDirectory [file join $Dart(DartRoot) Source Client]

  set SiteDir [file join $HTMLDir TestingResults Sites $Dart(Site)]
  set BuildNameDir [file join $SiteDir $Dart(BuildName)]

  set BuildStamp [file tail $BuildStampDir]
  set XMLDir [file join $BuildStampDir XML]

  set Out [open [file join $XMLDir DynamicAnalysis.xml] w]

  puts $Out $Dart(XMLHeader)
  puts $Out "<Site BuildName=\"$Dart(BuildName)\" BuildStamp=\"$BuildStamp\" Name=\"$Dart(Site)\">"
  puts $Out "<DynamicAnalysis Checker=\"Valgrind\">"
  puts $Out "\t<StartDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</StartDateTime>"

  # Find the list of canidate tests
  puts "\tFinding Tests to run Valgrind on"
  set TestList ""
  set OldDir [pwd]
  cd $Dart(BuildDirectory)
  set TestList [FindTests .]
  cd $OldDir
  puts "\tFound [llength $TestList] tests for Valgrind"

  # Delete any old Valgrind files
  puts "\tRemoving old Valgrind logs"
  set Status [catch { FileMap [glob -nocomplain *] [list *.vglog] RemoveFile } Result]
  ReportError $Status $Result

  # Write the list
  puts $Out "\t<TestList>"
  foreach Test $TestList \
  {
    puts $Out "\t\t<Test>[XMLSafeString [lindex $Test 1]/[lindex $Test 0]]</Test>"
  }
  puts $Out "\t</TestList>"

  puts $Out "\t<DefectList>"
  set Valgrind(Messages) [list "Memory Leak" "Invalid mem access" "Invalid jump" "Mismatched deallocation" "Invalid syscall param"]
  foreach Message $Valgrind(Messages) \
  {
    puts $Out "\t\t<Defect Type=\"[XMLSafeString $Message]\"/>"
    set Valgrind($Message,Count) 0
  }
  puts $Out "\t</DefectList>"

  # if we are using msdev, then determine the configuration to test
  set alternateDirectories ""
  if { [regexp "^(.*msdev.*).*" $Dart(MakeCommand)] } {
    # extract the configuration
    set makeConfiguration [lindex [lindex $Dart(MakeCommand) 3] 2]

    # if makeConfiguration is "ALL", then use "Debug" as the test
    # configuration, otherwise use the specified configuration
    if { $makeConfiguration == "ALL" } {
      set alternateDirectories "Debug"
    } else {
      set alternateDirectories $makeConfiguration
    }

    puts "\tWill search for executables in configuration subdirectory $alternateDirectories"
  }

  # For each test, cd to the directory, and run it.
  puts "\tRunning Valgrind Tests"
  set ReportPassed 0
  set ReportFailed 0
  set ReportNotRun 0
  foreach Test $TestList \
  {
    # Each $Test = {TestIdentifier PathFromBuildDirToTest Executable [args]}
    cd $Dart(BuildDirectory)
    cd [lindex $Test 1]

    set Filename [lindex $Test 2]

    # first check to see if Executable exists
    set candidates ""
    # extract prepath form exe name
    set fname [file tail $Filename]
    set pdir [file dirname $Filename]
    foreach path [list "." "" $alternateDirectories] {
      lappend candidates [file join $pdir $path $fname]
      lappend candidates [file join $pdir $path $fname.exe]
    }

    set NewFilename ""
    foreach candidate $candidates {
      if { [CheckFile $candidate] } {
        set NewFilename $candidate
        break
      }
    }

    set Valgrind([lindex $Test 0],Status) notrun
    set Result ""
    set VGLog ""
    if { $NewFilename != "" } \
    {
      puts "\tRunning $NewFilename [lrange $Test 3 end]"
      set VGLog "[lindex $Test 0].vglog"
      set OldDir [pwd]
      cd [file dirname $NewFilename]
      set VGLog [file join $Dart(BuildDirectory) $TempDir [lindex $Test 0].vglog]
      # puts "Running: $Dart(ValgrindCommand) $Dart(ValgrindCommandOptions) $NewFilename [lrange $Test 3 end]"
      set Status [catch { RunTimedCommand "$Dart(ValgrindCommand) $Dart(ValgrindCommandOptions) $NewFilename [lrange $Test 3 end]" $Dart(TimeOut) $VGLog } Result]
      if { $Status } {
        set Valgrind([lindex $Test 0],Status) failed
      } else {
        set Valgrind([lindex $Test 0],Status) passed
      }
      ProcessValgrindLog $Test $VGLog
      # file delete $VGLog
      cd $OldDir
    }

    puts $Out "\t<Test Status=\"$Valgrind([lindex $Test 0],Status)\">"
    puts $Out "\t\t<Name>[XMLSafeString [lindex $Test 0]]</Name>"
    puts $Out "\t\t<Path>[XMLSafeString [lindex $Test 1]]</Path>"
    puts $Out "\t\t<FullName>[XMLSafeString [file join [lindex $Test 1] [lindex $Test 0]]]</FullName>"
    puts $Out "\t\t<FullCommandLine>[XMLSafeString [lrange $Test 2 end]]</FullCommandLine>"
    puts $Out "\t\t<Results>"
    if { $Valgrind([lindex $Test 0],Status) != "notrun" } \
    {
      foreach Message $Valgrind(Messages) \
      {
        puts $Out "\t\t\t<Defect type=\"$Message\">$Valgrind([lindex $Test 0],$Message)</Defect>"
      }
    } \
    else \
    {
      foreach Message $Valgrind(Messages) \
      {
        puts $Out "\t\t\t<Defect type=\"$Message\">0</Defect>"
      }
    }
    puts $Out "\t\t</Results>"

    if { [file exists $VGLog] } \
    {
      set ValgrindLog [open $VGLog r]

      puts $Out "\t<Log>"
      # For the moment, Don't include the build log
      # puts $Out [Base64Encode [read $BuildLog ]]
      # Need to process the log to strip out only ==foo== lines
      puts $Out [XMLSafeString [StripValgrind [read $ValgrindLog]]]
      close $ValgrindLog
      puts $Out "\t</Log>"
    }
    puts $Out "\t</Test>"
    cd $OldDir
  }

  puts $Out "\t<EndDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</EndDateTime>"
  puts $Out "</DynamicAnalysis>"
  puts $Out "</Site>"
  set total [expr double($ReportPassed + $ReportFailed + $ReportNotRun)]
  puts "\tValgrind completed"
  foreach Message $Valgrind(Messages) \
  {
    puts "\t\t$Message - $Valgrind($Message,Count)"
  }
  file delete $TempFile
  close $Out
}
