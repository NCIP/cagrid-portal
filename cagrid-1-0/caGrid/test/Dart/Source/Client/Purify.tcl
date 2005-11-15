# =========================================================================
# 
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Purify.tcl,v $
#   Language:  Tcl
#   Date:      $Date: 2005-11-15 14:51:26 $
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
proc ProcessPurify { Test PLog } { return [ProcessLog $Test $PLog] }

proc ProcessLog { Test PLog } \
{
  global Purify
  if { ![file exists $PLog] } \
  {
    set Purify([lindex $Test 0],Status) failed
    return
  }
  set f [open $PLog r]

  set Purify([lindex $Test 0],PurifyLog) ""
  foreach Message $Purify(Messages) \
  {
    set Purify([lindex $Test 0],$Message) 0
  }
  while { ![eof $f] } \
  {
    set line [gets $f]
    append Purify([lindex $Test 0],PurifyLog) [XMLSafeString "$line\n"]
    foreach Message $Purify(Messages) \
    {
      set match "$Message:"
      if { [regexp $match $line] } \
      {
        incr Purify([lindex $Test 0],$Message)
        incr Purify($Message,Count)
      }
    }
  }
}


proc Purify { Model BuildStampDir {MemoryChecker Purify} } {
  global Dart tcl_platform Purify errorInfo TestList
  set HTMLDir [file join Testing HTML]
  set TempDir [file join Testing Temporary]
  set TempFile [file join $Dart(BuildDirectory) $TempDir PurifyLog.txt]
  set UtilitiesDirectory [file join $Dart(DartRoot) Source Client]

  set SiteDir [file join $HTMLDir TestingResults Sites $Dart(Site)]
  set BuildNameDir [file join $SiteDir $Dart(BuildName)]

  set BuildStamp [file tail $BuildStampDir]
  set XMLDir [file join $BuildStampDir XML]

  set Out [open [file join $XMLDir DynamicAnalysis.xml] w]

  puts $Out $Dart(XMLHeader)
  puts $Out "<Site BuildName=\"$Dart(BuildName)\" BuildStamp=\"$BuildStamp\" Name=\"$Dart(Site)\">"
  puts $Out "<DynamicAnalysis Checker=\"Purify\">"
  puts $Out "\t<StartDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</StartDateTime>"

  # Find the list of canidate tests
  puts "\tFinding Tests to run Purify on"
  set TestList ""
  set OldDir [pwd]
  cd $Dart(BuildDirectory)
  set TestList [FindTests .]
  cd $OldDir
  puts "\tFound [llength $TestList] tests for Purify"

  # Delete any old purify files
  puts "\tRemoving old purify logs"
  set Status [catch { FileMap [glob -nocomplain *] [list *.plog] RemoveFile } Result]
  ReportError $Status $Result

  # Write the list
  puts $Out "\t<TestList>"
  foreach Test $TestList \
  {
    puts $Out "\t\t<Test>[XMLSafeString [lindex $Test 1]/[lindex $Test 0]]</Test>"
  }
  puts $Out "\t</TestList>"

  puts $Out "\t<DefectList>"
  set Purify(Messages) [list MLK PLK MPK ABR ABW ABWL IPR NPR ODS COR EXU FMM FUM FMR FMW FFM MAF UMC UMR]
  foreach Message $Purify(Messages) \
  {
    puts $Out "\t\t<Defect Type=\"[XMLSafeString $Message]\"/>"
    set Purify($Message,Count) 0
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
  puts "\tRunning Purify Tests"
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

    set Purify([lindex $Test 0],Status) notrun
    set Result ""
    set PLog ""
    if { $NewFilename != "" } \
    {
      puts "\tRunning $NewFilename [lrange $Test 3 end]"
      set PLog "[lindex $Test 0].plog"
      if { $tcl_platform(platform) != "windows" } {
        set OldDir [pwd]
        cd [file dirname $NewFilename]
        switch $MemoryChecker {
          Purify {
            # running purify on unix
            set PurifyOptions $Dart(PurifyOptions)
            append PurifyOptions "-suppression-file-names=[file join $Dart(SourceDirectory) $UtilitiesDirectory .purify] "
            append PurifyOptions "-log-file=$PLog "
            
            # Save current directory
            set PurifyFilename [file tail $NewFilename]
            set Status [catch { eval exec [list $Dart(PurifyCommand)] $PurifyOptions [list $PurifyFilename] [lrange $Test 3 end] 2> $TempFile } Result]
            ReportError $Status $Result
            set Status [catch { eval exec [list $PurifyFilename.pure] [lrange $Test 3 end] 2> $TempFile } Result]
            ReportError $Status $Result
            set Purify([lindex $Test 0],Status) passed
            ProcessPurify $Test $PLog
            # delete the purified executable to save disk space
            file delete $PurifyFilename.pure
            file delete $PurifyFilename.plog
          }
        }
        cd $OldDir
      } else {
        # running purify on windows
        set PurifyOptions "/SAVETEXTDATA=$PLog "
        set Status [catch { eval exec [list $Dart(PurifyCommand)] $PurifyOptions [list $NewFilename] [lrange $Test 3 end]} Result]
        ReportError $Status $Result

        set Purify([lindex $Test 0],Status) passed
        ProcessPurify $Test $PLog
        # delete the purified executable to save disk space
        # but windows purify stashes these in a cache...
      }
    }

    puts $Out "\t<Test Status=\"$Purify([lindex $Test 0],Status)\">"
    puts $Out "\t\t<Name>[XMLSafeString [lindex $Test 0]]</Name>"
    puts $Out "\t\t<Path>[XMLSafeString [lindex $Test 1]]</Path>"
    puts $Out "\t\t<FullName>[XMLSafeString [file join [lindex $Test 1] [lindex $Test 0]]]</FullName>"
    puts $Out "\t\t<FullCommandLine>[XMLSafeString [lrange $Test 2 end]]</FullCommandLine>"
    puts $Out "\t\t<Results>"
    if { $Purify([lindex $Test 0],Status) != "notrun" } \
    {
      foreach Message $Purify(Messages) \
      {
        puts $Out "\t\t\t<Defect Type=\"$Message\">$Purify([lindex $Test 0],$Message)</Defect>"
      }
    } \
    else \
    {
      foreach Message $Purify(Messages) \
      {
        puts $Out "\t\t\t<Defect type=\"$Message\">0</Defect>"
      }
    }
    puts $Out "\t\t</Results>"

    if { [file exists $PLog] } \
    {
      set PurifyLog [open $PLog r]

      puts $Out "\t<Log>"
      # For the moment, Don't include the build log
      # puts $Out [Base64Encode [read $BuildLog ]]
      switch $MemoryChecker {
        Purify {
          puts $Out [XMLSafeString [read $PurifyLog]]
        }
      }
      close $PurifyLog
      puts $Out "\t</Log>"
    }
    puts $Out "\t</Test>"
    cd $OldDir
  }

  puts $Out "\t<EndDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</EndDateTime>"
  puts $Out "</DynamicAnalysis>"
  puts $Out "</Site>"
  set total [expr double($ReportPassed + $ReportFailed + $ReportNotRun)]
  puts "\tPurify completed"
  foreach Message $Purify(Messages) \
  {
    puts "\t\t$Message - $Purify($Message,Count)"
  }
  file delete $TempFile
  close $Out
}
