# =========================================================================
# 
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Test.tcl,v $
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

# This code handles fileevents and idle callbacks
proc ReadFile { p } {
  global done Output
  if { [eof $p] } {
    set done "Completed"
  } else {
    if { [gets $p line] != -1 } {
      append Output "$line\n"
    }
  }
}

proc IdleTimeout {} {
  global done
  set done "Timeout"
}


proc CheckFile { Filename } \
{
  return [expr [file exists $Filename] && [file executable $Filename]]
}

proc Test { Model BuildStampDir } {
  global Dart Output done tcl_platform TestList

  set TempDir [file join [pwd] Testing Temporary]

  set HTMLDir [file join Testing HTML]
  set TempDir [file join [pwd] Testing Temporary]

  set SiteDir [file join $HTMLDir TestingResults Sites $Dart(Site)]
  set BuildNameDir [file join $SiteDir $Dart(BuildName)]
  set TempFile [file join $TempDir Temp.txt]

  set BuildStamp [file tail $BuildStampDir]
  set XMLDir [file join $BuildStampDir XML]

  set Out [open [file join $XMLDir Test.xml] w]

  puts $Out $Dart(XMLHeader)
  puts $Out "<Site BuildName=\"$Dart(BuildName)\" BuildStamp=\"$BuildStamp\" Name=\"$Dart(Site)\">"

  puts $Out {<Testing>}
  puts $Out "\t<StartDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</StartDateTime>"

  # Find the list of candidate tests
  puts "\tFinding Tests"
  set TestList ""
  set OldDir [pwd]
  cd $Dart(BuildDirectory)
  set TestList [FindTests .]
  cd $OldDir
  puts "\tFound [llength $TestList] tests"

  # Delete any old coverage files
  catch { FileMap [glob -nocomplain *] [list *.da *.gcov] RemoveFile } Result

  # Write the list
  puts $Out "\t<TestList>"
  foreach Test $TestList \
  {
    puts $Out "\t\t<Test>[XMLSafeString [lindex $Test 1]/[lindex $Test 0]]</Test>"
  }
  puts $Out "\t</TestList>"
  

  # DartMeasurement regular expressions
  set dartMeasurementRegExp(twoattributes) "<DartMeasurement\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*>(\[^<\]*)(</DartMeasurement>)"
  set dartMeasurementRegExp(threeattributes) "<DartMeasurement\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*>(\[^<\]*)(</DartMeasurement>)"
  set dartMeasurementRegExp(fourattributes) "<DartMeasurement\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*>(\[^<\]*)(</DartMeasurement>)"
  set dartMeasurementRegExp(measurementfile) "<DartMeasurementFile\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*(name|type|encoding|compression)=\"(\[^\"]*)\"\[ \t\r\n\]*>(\[^<\]*)(</DartMeasurementFile>)"
  
  # if we are using msdev, then determine the configuration to test
  set alternateDirectories ""
  if { [regexp "^(.*msdev.*).*" $Dart(MakeCommand)] } {
    # extract the configuration
    set makeConfiguration [lindex [lindex $Dart(MakeCommand) 3] 2]

    # if makeConfiguration is "ALL", then use "Release" as the test 
    # configuration, otherwise use the specified configuration 
    if { $makeConfiguration == "ALL" } {
      set alternateDirectories "Release"
    } else {
      set alternateDirectories $makeConfiguration
    }
      
    puts "\tWill search for executables in configuration subdirectory $alternateDirectories"
  }
  if { [regexp "^(.*devenv.*).*" $Dart(MakeCommand)] } {
    # extract the configuration
    set makeConfiguration [lindex $Dart(MakeCommand) 3]

    # if makeConfiguration is "ALL", then use "Release" as the test 
    # configuration, otherwise use the specified configuration 
    if { $makeConfiguration == "ALL" } {
      set alternateDirectories "Release"
    } else {
      set alternateDirectories $makeConfiguration
    }
      
    puts "\tWill search for executables in configuration subdirectory $alternateDirectories"
  }

  # For each test, cd to the directory, and run it.
  puts "\tRunning Tests, one icon per test, 50 icons per line"
  puts "\t. == Passed\t- == Failed\t* == NotRun"
  set ReportPassed 0
  set ReportFailed 0
  set ReportNotRun 0
  set TestCount 0

  foreach Test $TestList \
  {
    # Each $Test = {TestIdentifier PathFromBuildDirToTest Executable [args]}
    cd [lindex $Test 1]

    set Filename [lindex $Test 2]
    if { [regexp cygtclsh [info nameofexecutable]] == 1} {
      set Filename [exec cygpath -w $Filename]
      set Filename [eval file join [file split $Filename]]
    }

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
    
    set Status notrun
    set Result ""
    if { $NewFilename != "" } \
    {
      set done 0
      set Output ""
      set command [list $NewFilename]
      foreach arg [lrange $Test 3 end] {
	lappend command $arg
      }
      # Setup a timer for the test.
      set StartTimeSeconds [clock seconds]
      set Execution [time {
         # The funny open pipe does not like spaces in $TempFile
         # So we quote it.
         if {[catch { set p [open "| $command 2> [list $TempFile]"] }] } {
            set done "BadCommand"
            set Status 1
         } else { 
            # If the gets operation blocks, then the tcl event loop
            # will never get a chance to see the timeout.
            fconfigure $p -blocking 0

            fileevent $p readable [list ReadFile $p]
            set Callback [after [expr int($Dart(TimeOut) * 1000)] IdleTimeout]
            # Wait for a timeout, or the command to complete
            vwait done
         }  }]
      set EndTimeSeconds [clock seconds]
      # Cancel the after callback, and the fileevent.
      if { $done != "BadCommand" } {
         after cancel $Callback
         fileevent $p readable ""
         # Assume we passed, until proven otherwise.
         set Status 0
      }

      # If we timed out, kill the process, if we can.
      if { $done == "Timeout" } {
        set Status 1
        foreach PID [pid $p] {
          if { $tcl_platform(platform) == "windows" } {
            # this is a windows system, use the kill command distributed w/Dart
            catch { exec [file join $Dart(DartRoot) Source Utilities win32 bin kill.exe] $PID }
          } else {
            # this is a unix system, use standard kill command
            catch { exec kill $PID }
          }
        }
      }
      
      # Start creating the result
      set Result ""
      
      # Configure the command pipeline to be blocking before closing,
      # to ensure that close with a useful exit status.
      fconfigure $p -blocking 1
      if { [catch { close $p } r] } {
        if { $r != "" && $done == "Completed" } {
          append Result "<DartMeasurement type=\"text/string\" name=\"Exit Code\">[XMLSafeString [lindex $::errorCode 0]]</DartMeasurement>\n"
          append Result "<DartMeasurement type=\"text/string\" name=\"Exit Value\">[XMLSafeString [lindex $::errorCode 2]]</DartMeasurement>\n"
          if { [lindex $::errorCode 3] != "" } {
            append Result "<DartMeasurement type=\"text/string\" name=\"Exit Explanation\">[XMLSafeString [lindex $::errorCode 3]]</DartMeasurement>\n"
          }
          set Status 1
          set ::errorCode ""
        }
      }

      # XMLSafeString corrupts the uuencoded binary images, by changing the tags
      # need to use PrintableString
      set f [open $TempFile r]
      append Result "Standard Output:\n[PrintableString $Output]\nStandard Error:\n[PrintableString [read $f]]"
      close $f
      
      # If the test runs more than 30 minutes, report the time in seconds
      # Turns out that 35.8 minutes is where the "time" command wraps
      # as it collects in microseconds
      set DeltaTime [expr $EndTimeSeconds - $StartTimeSeconds]
      if { $DeltaTime > 1800 } {
        set ExecutionTime $DeltaTime
      } else {
        set ExecutionTime [expr [lindex $Execution 0] / 1000000.0]
      }
      
      append Result "<DartMeasurement type=\"numeric/double\" name=\"Execution Time\">$ExecutionTime</DartMeasurement>\n"
      append Result "<DartMeasurement type=\"text/string\" name=\"Completion Status\">$done</DartMeasurement>\n"
      # set Status [catch { eval exec $NewFilename [lrange $Test 3 end]} Result]
    }
    if { $Status == 0 } \
    {
      set Status passed
    }
    if { $Status == 1 } \
    {
      set Status failed
    }
    switch $Status \
    {
      notrun { incr ReportNotRun; set icon "*" }
      passed { incr ReportPassed; set icon "." }
      failed { incr ReportFailed; set icon "-" }
    }
    
    if { $TestCount == 0 } {
      puts -nonewline "\t"
      incr TestCount
    }
    puts -nonewline $icon
    if { ( $TestCount % 50 ) == 0 } {
      puts -nonewline [format "  T: %4d\n\t" $TestCount]
    }
    incr TestCount

    # output the header for the test
    puts $Out "\t<Test Status=\"$Status\">"
    puts $Out "\t\t<Name>[XMLSafeString [lindex $Test 0]]</Name>"
    puts $Out "\t\t<Path>[XMLSafeString [lindex $Test 1]]</Path>"
    puts $Out "\t\t<FullName>[XMLSafeString [file join [lindex $Test 1] [lindex $Test 0]]]</FullName>"
    puts $Out "\t\t<FullCommandLine>[XMLSafeString [lrange $Test 2 end]]</FullCommandLine>"
    puts $Out "\t\t<Results>"

    # parse the output of the test to look for Dart XML tags
    set DartResults ""
    set parseDone 0
    while { $parseDone == 0 } {
      set parseDone 1
      if { [regexp $dartMeasurementRegExp(twoattributes) $Result d t1 v1 t2 v2 measurement endtag] } {
        # found a two attribute call to DartMeasurement
        if { $t1 == "name" || $t2 == "name"} {
          if { $t1 == "type" || $t2 == "type"} {
            puts $Out "\t\t\t<NamedMeasurement $t1=\"$v1\" $t2=\"$v2\"><Value>$measurement</Value></NamedMeasurement>"
          }
        }

        # remove pattern from the output
        regsub $dartMeasurementRegExp(twoattributes) $Result "" Result

        set parseDone 0
      } elseif { [regexp $dartMeasurementRegExp(threeattributes) $Result d t1 v1 t2 v2 t3 v3 measurement endtag] } {
        # found a three attribute call to DartMeasurement
        if { $t1 == "name" || $t2 == "name" || $t3 == "name"} {
          if { $t1 == "type" || $t2 == "type" || $t3 == "type"} {
            puts $Out "\t\t\t<NamedMeasurement $t1=\"$v1\" $t2=\"$v2\" $t3=\"$v3\"><Value>$measurement</Value></NamedMeasurement>"
          }
        }
        
        # remove pattern from the output
        regsub $dartMeasurementRegExp(threeattributes) $Result "" Result

        set parseDone 0
      } elseif { [regexp $dartMeasurementRegExp(fourattributes) $Result d t1 v1 t2 v2 t3 v3 t4 v4 measurement endtag] } {
        # found a four attribute call to DartMeasurement
        if { $t1 == "name" || $t2 == "name" || $t3 == "name" || $t4 == "name"} {
          if { $t1 == "type" || $t2 == "type" || $t3 == "type" || $t4 == "type"} {
            puts $Out "\t\t\t<NamedMeasurement $t1=\"$v1\" $t2=\"$v2\" $t3=\"$v3\" $t4=\"$v4\"><Value>$measurement</Value></NamedMeasurement>"
          }
        }
        
        # remove pattern from the output
        regsub $dartMeasurementRegExp(fourattributes) $Result "" Result

        set parseDone 0
      } elseif { [regexp $dartMeasurementRegExp(measurementfile) $Result d t1 v1 t2 v2 measurement endtag] } {
        # found a two attribute call to DartMeasurementFile
        if { $t1 == "name" || $t2 == "name"} {
          if { $t1 == "type" || $t2 == "type"} {
            if { [info tclversion] < 8.1 } {
              puts $Out "\t\t\t<NamedMeasurement name=\"$v1\" type=\"text/string\"><Value>Cannot encode base64 files with this version of tcl: [info tclversion] less than 8.1</Value></NamedMeasurement>"
            } else {

              if { [catch {set mFile [open [string trim $measurement] r]}] == 0} {
                fconfigure $mFile -translation binary
                puts $Out "\t\t\t<NamedMeasurement $t1=\"$v1\" $t2=\"$v2\" encoding=\"base64\">"
                puts $Out "\t\t\t\t<Value>[base64::encode [read $mFile]]</Value>"
                puts $Out "\t\t\t</NamedMeasurement>"
                close $mFile
              } else {
                puts -nonewline $Out "\t\t\t<NamedMeasurement "
                if { $t1 == "name" } {
                  puts $Out "name=\"$v1\" type=\"text/string\"><Value>File $measurement not found.</Value></NamedMeasurement>"
                } elseif { $t2 == "name" } {
                  puts $Out "name=\"$v2\" type=\"text/string\"><Value>File $measurement not found.</Value></NamedMeasurement>"
                }
              }
            }
          }
        }
        
        # remove pattern from the output
        regsub $dartMeasurementRegExp(measurementfile) $Result "" Result

        set parseDone 0
      }
    }


    # output the results of this test
    puts $Out "\t\t\t<Measurement>"
    puts $Out "\t\t\t\t<Value>[XMLSafeString $Result]</Value>"
    puts $Out "\t\t\t</Measurement>"
    puts $Out "\t\t</Results>"
    puts $Out "\t</Test>"

    cd $OldDir

  }

  puts $Out "\t<EndDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</EndDateTime>"

  puts $Out "</Testing>"
  puts $Out "</Site>"
  set total [expr double($ReportPassed + $ReportFailed + $ReportNotRun)]

  # Cap off the dots
  incr TestCount -1
  puts [format "  T: %4d" $TestCount]

  puts "\tTesting completed"
  if { $total != 0.0 } \
  {
    set Percent [format "%.2f" [expr 100.0 * $ReportNotRun / $total]]
    puts "\t$ReportNotRun Tests Not Run -- $Percent%"
    set Percent [format "%.2f" [expr 100.0 * $ReportFailed / $total]]
    puts "\t$ReportFailed Tests Failed -- $Percent%"
    set Percent [format "%.2f" [expr 100.0 * $ReportPassed / $total]]
    puts "\t$ReportPassed Tests Passed -- $Percent%"
  }

  close $Out
  return;
}
