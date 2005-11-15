# =========================================================================
# 
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Build.tcl,v $
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


# Put in this directory
# Year Month Day - TimeZone - Hour:Minute
# e.g. 20000926-EDT-16:02
# or Year Month Day Hour Minute
# clock format [clock seconds] -format %Y%m%d%H%M

# Must be run from root directory of Insight build, only by make

proc Build { Model BuildStampDir } {
  global Dart

  set UtilityDir [file join $Dart(DartRoot) Source Client]
  source [file join $UtilityDir Utility.tcl]

  set HTMLDir [file join Testing HTML]
  set BuildTempDir [file join Testing Temporary]
  set SiteDir [file join $HTMLDir TestingResults Sites $Dart(Site)]
  set BuildNameDir [file join $SiteDir $Dart(BuildName)]

  set BuildStamp [file tail $BuildStampDir]
  set XMLDir [file join $BuildStampDir XML]


  # We can assume that Testing/HTML/TestingResults/@SITE@ is built
  file mkdir $HTMLDir $SiteDir $BuildNameDir $BuildStampDir $BuildTempDir $XMLDir

  set BuildLogFilename [file join $BuildTempDir Build.log.[pid]]
  set BuildLogCompressedFilename [file join $BuildTempDir Build.log.compressed.[pid]]
  set BuildLogTempFilename [file join $BuildTempDir Build.log.temp.[pid]]

  # Begin the XML output
  set Out [open [file join $XMLDir Build.xml] w]

  puts $Out $Dart(XMLHeader)
  puts $Out "<Site BuildName=\"$Dart(BuildName)\" BuildStamp=\"$BuildStamp\" Name=\"$Dart(Site)\">"
  puts $Out {<Build>}
  puts $Out "\t<StartDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</StartDateTime>"

  # Do the build
  puts "\tStarting Build"
  set myMakeCommands {}
  if { [regexp "^(.*msdev.*).*(ALL_BUILD).*" $Dart(MakeCommand)] } {
    # If building using Microsoft Visual Studio, change the MakeCommand to 
    # a list of projects
    
    # find the workspace
    set dsw [lindex $Dart(MakeCommand) 1]

    # find all the dsps
    set dsps [FindAllBuildDSPDepends $dsw]

    # if a build log already exists, remove it
    if {[file exists $BuildLogFilename]} {
      file delete -force $BuildLogFilename
    }

    # find the configuration
    set makeConfiguration [lindex $Dart(MakeCommand) 3]
    # build each project separately
    set myMakeCommand $Dart(MakeCommand)
    foreach dsp $dsps {
      # Replace the ALL_BUILD project with the name of this project
      regsub "ALL_BUILD" $makeConfiguration $dsp dspConfiguration
      
      # Add this project to the list, msdev builds project from right to 
      # left, so we substitute in a particular order
      set myMakeCommand [linsert $myMakeCommand 4 $dspConfiguration]
    }

    # remove the original ALL_BUILD project from the list
    set myMakeCommand [lremove $myMakeCommand $makeConfiguration]

    set maximumLineLength 1024
    if { [string length $myMakeCommand] > $maximumLineLength } {
	# break the make command into several commands
	set makeCommandPrefix [lrange $myMakeCommand 0 2]
	
	set currentCommand $makeCommandPrefix
	set currentIndex 2
	foreach dsp $dsps {
	    incr currentIndex
	    # predict the new string length (add 2 for a space and eol)
	    if {[expr [string length [lindex $myMakeCommand $currentIndex]] + [string length $currentCommand] + 2] > $maximumLineLength} {
		# put current command on the make command list
		# since msdev builds the projects right to left, 
		# always insert the current command at the front
		# of the list so the last few projects listed in the 
		# original list will be the first make command
		set myMakeCommands [linsert $myMakeCommands 0 $currentCommand]

		# create a new current command
		set currentCommand $makeCommandPrefix
	    }
	    # tack this project onto the current command
	    lappend currentCommand [lindex $myMakeCommand $currentIndex]
	}
	# put the last command on the make command list
	set myMakeCommands [linsert $myMakeCommands 0 $currentCommand]
    } else {
	set myMakeCommands [linsert $myMakeCommands 0 $myMakeCommand]
    }
  } else {
    # Use the standard make command
    set myMakeCommand $Dart(MakeCommand)

    set myMakeCommands [linsert $myMakeCommands 0 $myMakeCommand]
  }

  if { [llength $myMakeCommands] > 1 } {
      puts "\tSplitting build into [llength $myMakeCommands] subcommands."
  }
  
  # build the code
  puts "\tTemp file is: [file join [pwd] $BuildLogFilename]"
  flush stdout
  set outFile [open $BuildLogFilename "w"]
  if { [string tolower $Dart(VerboseBuild)] == "on" } {
      set Verbose 1
  } else {
      set Verbose 0
  }
  set buildNumber 0
  foreach mkCommand $myMakeCommands {
      incr buildNumber
      puts "\n\tBuild command \#$buildNumber"
      set processFile [open "| $mkCommand |& [list $Dart(TclshCommand)] \"[file join $Dart(DartRoot) Source Client Cat.tcl]\""]
      ReportProgress $processFile $outFile $Verbose

      puts $Out "<BuildCommand>[XMLSafeString $mkCommand]</BuildCommand>"
      set BuildStatus [catch {close $processFile} Result]
      
      if {$BuildStatus != 0} { puts "\tBuild Result: $Result" }
      puts "\tBuild \#$buildNumber finished with status: $BuildStatus"
      puts ""
  }
  close $outFile

  if { [glob -nocomplain $BuildLogFilename] == "" } {
    error "$Dart(MakeCommand) produced no output at all, most likely failed to exec."
  }
  puts "\tStarting XML generation"
  set Build [open $BuildLogFilename r]
  set BuildLog [open $BuildLogTempFilename w]
  puts $BuildLog "<html><head><title>Build log: $Dart(Site) - $Dart(BuildName) - $BuildStamp</title></head><body bgcolor=\"\#ffffff\">"
  
  set Cache ""
  catch { unset ErrorList }
  set ErrorList(Counter) 0
  set ErrorList(Append) ""
  set ErrorList(Buffer) ""
  set PostContextCount 5
  set BufferLength 5
  set Report(Error) 0
  set Report(Warning) 0
  set Report(FileMatched) 0
  set RepeatCache("") 1
  if { [info exists Dart(DetectRepeatedErrors)] && [string toupper $Dart(DetectRepeatedErrors)] == "ON" } \
  {
    set DetectRepeats 1
  } else \
  {
    set DetectRepeats 0
  }

  # Regular expressions used to determine if there is an error or warning
  # on a build log line
  set ErrorMatches [list \
		    {^[Bb]us [Ee]rror} \
		    {^[Ss]egmentation [Vv]iolation} \
		    {^[Ss]egmentation [Ff]ault} \
		    {([^ :]+):([0-9]+): ([^ \t])} \
		    {([^:]+): error[ \t]*[0-9]+[ \t]*:} \
		    {^Error ([0-9]+):} \
		    {^Fatal } \
		    {^Error: } \
		    {^Error } \
                    {^\"[^\"]+\", line [0-9]+: [^Ww]} \
		    {^cc[^C]*CC: ERROR File = ([^,]+), Line = ([0-9]+)} \
		    {^ld([^:])*:([ \t])*ERROR([^:])*:} \
		    {^ild:([ \t])*\(undefined symbol\)} \
		    {([^ :]+) : (error|fatal error|catastrophic error)} \
		    {([^:]+): (Error:|error|undefined reference|multiply defined)} \
		    {([^:]+)\(([^\)]+)\) : (error|fatal error|catastrophic error)} \
		    {^fatal error C[0-9]+:} \
                    {: syntax error } \
                    {^collect2: ld returned 1 exit status}\
		    {Unsatisfied symbols:} \
                    {Undefined symbols:} \
                    {^Undefined[ \t]+first referenced} \
		    {^CMake Error:} \
		    {:[ \t]cannot find} \
		    {:[ \t]Can\'t find} \
                    {: \*\*\* No rule to make target \`.*\'.  Stop} \
                    {: Invalid loader fixup for symbol} \
                    {: internal link edit command failed} \
                    {^Fatal: Error detected} \
                    {: Unrecognized option \`.*\'}
		   ]
  set ErrorExceptions [list "instantiated from " "candidates are:" ": warning" "makefile:" "Makefile:" {:[ \t]+Where:}]

  set WarningMatches [list \
		      {([^ :]+):([0-9]+): warning:} \
		      {^cc[^C]*CC: WARNING File = ([^,]+), Line = ([0-9]+)} \
		      {^ld([^:])*:([ \t])*WARNING([^:])*:} \
		      {([^:]+): warning ([0-9]+):} \
		      {^\"[^\"]+\", line [0-9]+: [Ww]arning} \
  		      {([^:]+): warning[ \t]*[0-9]+[ \t]*:} \
  		      {([^:]+): remark \#[0-9]+:} \
		      {^Warning ([0-9]+):} \
		      {^Warning } \
		      {([^ :]+) : warning} \
		      {([^:]+): warning} \
		      {^This workspace file is corrupted} \
                      {: Warnung:}
		     ]

  # Need to ignore some makefile exceptions
  set WarningExceptions [list \
        {/usr/openwin/include/X11/Xlib\.h:[0-9]+: warning: ANSI C\+\+ forbids declaration} \
        {/usr/openwin/include/X11/Xutil\.h:[0-9]+: warning: ANSI C\+\+ forbids declaration} \
        {/usr/openwin/include/X11/XResource\.h:[0-9]+: warning: ANSI C\+\+ forbids declaration} \
        {WARNING 84 :} \
        {WARNING 47 :} \
        {makefile:} \
        {Makefile:} \
        {warning:  Clock skew detected.  Your build may be incomplete.} \
        {/usr/openwin/include/GL/[^:]+:} \
        {bind_at_load} \
        {XrmQGetResource} \
        {IceFlush} \
        {WARNING File = /usr/include/CC} \
        {WARNING File = /usr/include/internal} \
        {WARNING File = .*/vxl/vnl/algo/vnl_powell.cxx, Line = 140} \
        {WARNING File = .*/vxl/vnl/algo/vnl_lbfgs.cxx, Line = 76} \
        {warning LNK4089: all references to [^ \t]+ discarded by /OPT:REF} \
        {ld32: WARNING 85: definition of dataKey in} \
        {cc: warning 422: Unknown option \"\+b\.\" ignored} 
     ]

  puts "\tParsing Build Log"
  set Line 1
  while { ![eof $Build] } \
  {
    # Get a line and write it out
    set LogLine [gets $Build]
    puts $BuildLog "<a name=\"$Line\">$Line:</a><pre>$LogLine</pre>"
    flush $BuildLog
    set hasError [Match $ErrorMatches $LogLine $ErrorExceptions]
    set hasWarning [Match $WarningMatches $LogLine $WarningExceptions]

    if { $hasWarning } \
    {
      set Type Warning
      incr Report(Warning)
    }

    if { $hasError } \
    {
      set Type Error
      incr Report(Error)
    }
    
    if { $hasError || $hasWarning } \
    {
      set isRepeat 0
      if { $DetectRepeats } \
      {
	# puts "LOG LINE $logline"

	# Do this in pure Tcl
        if { [info exists RepeatCache($LogLine)] } {
          set j $RepeatCache($LogLine)
	  set isRepeat 1
	  incr ErrorList($j,RepeatCount)
        }
      }

      if { ! $isRepeat } \
      {
        set i $ErrorList(Counter)
        incr ErrorList(Counter)
        if { $DetectRepeats } {
          set RepeatCache($LogLine) $i
        }
        set ErrorList($i,Type) $Type
        set ErrorList($i,Text) $LogLine
        set ErrorList($i,Line) $Line
        set ErrorList($i,PreContext) $ErrorList(Buffer)
        set ErrorList($i,PostContext) ""
        set ErrorList($i,ContextCounter) 0
        set ErrorList($i,SourceFile) ""
        set ErrorList($i,SourceLineNumber) ""
        set ErrorList($i,RepeatCount) 0
      
        # Take a stab at getting a file name out of the line
        #

        # patch some paths to escape characters that are part of the regular
        # expression syntax
        regsub -all {\+} $Dart(SourceDirectory) {\+} sourceDirectory
        regsub -all {\*} $sourceDirectory {\*} sourceDirectory
        regsub -all {\^} $sourceDirectory {\^} sourceDirectory
        regsub -all {\$} $sourceDirectory {\$} sourceDirectory
        regsub -all {\[} $sourceDirectory {\[} sourceDirectory
        regsub -all {\]} $sourceDirectory {\]} sourceDirectory
        regsub -all {\(} $sourceDirectory {\)} sourceDirectory
        regsub -all {\(} $sourceDirectory {\)} sourceDirectory

        regsub -all {\+} $Dart(SourceDirectoryBase) {\+} sourceDirectoryBase
        regsub -all {\*} $sourceDirectoryBase {\*} sourceDirectoryBase
        regsub -all {\^} $sourceDirectoryBase {\^} sourceDirectoryBase
        regsub -all {\$} $sourceDirectoryBase {\$} sourceDirectoryBase
        regsub -all {\[} $sourceDirectoryBase {\[} sourceDirectoryBase
        regsub -all {\]} $sourceDirectoryBase {\]} sourceDirectoryBase
        regsub -all {\(} $sourceDirectoryBase {\)} sourceDirectoryBase
        regsub -all {\(} $sourceDirectoryBase {\)} sourceDirectoryBase

        # For GCC, we get /path/to/source.cxx:##
        # or ../../../Insight/Code/Common
        set Found 0
        if { !$Found && [regexp "$sourceDirectory/(\[^:\]*):(\[0-9\]+)" $LogLine dummy SourceFile LineNumber] } \
        {
	  set ErrorList($i,SourceFile) $SourceFile
	  set ErrorList($i,SourceLineNumber) $LineNumber
	  set Found 1
        }
        if { !$Found && [regexp "$sourceDirectoryBase/(\[^:\]*):(\[0-9\]+)" $LogLine dummy SourceFile LineNumber] } \
        {
	  set ErrorList($i,SourceFile) $SourceFile
	  set ErrorList($i,SourceLineNumber) $LineNumber
	  set Found 1
        }
      
        # Sun CC
        if { !$Found && [regexp "\"+$sourceDirectory/(\[^:\]*)\"+:(\[0-9\]+)" $LogLine dummy SourceFile LineNumber] } \
        {
	  set ErrorList($i,SourceFile) $SourceFile
	  set ErrorList($i,SourceLineNumber) $LineNumber
	  set Found 1
        }
      
        # MS VC++ 6.0
        set match "${sourceDirectory}(\\\\|/)(\[^\(\]*)\\((\[0-9\]+)\\) : (error|fatal error|catastrophic error|warning)"
        # Replace all the backslashes in the log line with forward slashes
        regsub -all {\\} $LogLine {/} foo
        if { !$Found && [regexp $match $foo dummy dummy2 SourceFile LineNumber] } \
        {
	  # Replace all backslashes with forward slashes
	  regsub -all {\\} $SourceFile {/} dummy
	  set SourceFile $dummy
	  set ErrorList($i,SourceFile) $SourceFile
	  set ErrorList($i,SourceLineNumber) $LineNumber
	  set Found 1
        }
      
        # Irix compiler
        # cc-1552 CC: WARNING File = /home/blezek/src/Insight/Code/Common/itkAffineMutualInformationVW.txx, Line = 250
        if { !$Found && [regexp "$sourceDirectory/(\[^,\]*), Line = (\[0-9\]+)" $LogLine dummy SourceFile LineNumber] } \
        {
	  set ErrorList($i,SourceFile) $SourceFile
	  set ErrorList($i,SourceLineNumber) $LineNumber
	  set Found 1
        }

	# Try the BCC compiler
	# Warning W8004 C:\itkQuality\Borland\Insight\Utilities\zlib\inftrees.c 137: 'p' is assigned a value that is never used in function huft_build
	set match "${sourceDirectory}/(\[^ \]*) (\[0-9\]+):"
        # Replace all the backslashes in the log line with forward slashes
        regsub -all {\\} $LogLine {/} foo
        if { !$Found && [regexp $match $foo dummy SourceFile LineNumber] } {
	  # Replace all backslashes with forward slashes
	  set ErrorList($i,SourceFile) $SourceFile
	  set ErrorList($i,SourceLineNumber) $LineNumber
	  set Found 1
	}
	
        # CMake errors
        # CMake Error: foo garf
        if { !$Found && [regexp "^CMake Error:" $LogLine dummy] } \
        {
	  # No line numbers or source files are indicated by a CMake error
	  set Found 1
        }
      
        incr Report(FileMatched) $Found
        lappend ErrorList(Append) $i
      }
    }
    # Add to the PreContextBuffer
    set l [llength $ErrorList(Buffer)]
    if { $l > $BufferLength } \
    {
      set ErrorList(Buffer) [lrange $ErrorList(Buffer) 1 [expr $l - 2]]
    }
    if { [string trim $LogLine] != "" } \
    {
      lappend ErrorList(Buffer) $LogLine
    }
    
    # Go through all in the ErrorList to append to, and do it
    set AppendList $ErrorList(Append)
    foreach i $AppendList \
    {
      if { $ErrorList($i,Line) != $Line } \
      {
	if { [string trim $LogLine] != "" } \
	{
	  lappend ErrorList($i,PostContext) $LogLine
	  incr ErrorList($i,ContextCounter)
	}
	
	# Remove this error for the append list
	if { $ErrorList($i,ContextCounter) > $PostContextCount } \
	{
	  set ErrorList(Append) [lremove $ErrorList(Append) $i]
	}
      }
    }
    incr Line
  }
  
  
  # Write all the errors out to the Out file
  if { $Report(Error) > $Dart(BuildErrorReportLimit) && $Dart(BuildErrorReportLimit) >= 0} {
    set Suppressed "Not reporting [expr $Report(Error) - $Dart(BuildErrorReportLimit)]"
  } else { set Suppressed "" }
  puts "\t$Report(Error) Compiler Errors $Suppressed"
  if { $Report(Warning) > $Dart(BuildWarningReportLimit) && $Dart(BuildWarningReportLimit) >= 0 } {
    set Suppressed "Not reporting [expr $Report(Warning) - $Dart(BuildWarningReportLimit)]"
  } else { set Suppressed "" }
  puts "\t$Report(Warning) Compiler Warnings $Suppressed"
  set total [expr $Report(Error) + $Report(Warning)]
  if { $total != 0 } \
  {
    set percent [format "%.2f" [expr 100.0 * $Report(FileMatched) / double($Report(Error) + $Report(Warning))]]
    puts "\tParsed $Report(FileMatched) filenames from [expr $Report(Error) + $Report(Warning)] opportunities for $percent%"
  }
  puts "\tWriting XML"
  set ReportedErrors 0
  set ReportedWarnings 0
  for { set i 0 } { $i < $ErrorList(Counter) } { incr i } \
  {
    switch $ErrorList($i,Type) {
      Error {
        if { $ReportedErrors > $Dart(BuildErrorReportLimit) && $Dart(BuildErrorReportLimit) >= 0 } {
          continue
        }
        incr ReportedErrors
      }
      Warning {
        if { $ReportedWarnings > $Dart(BuildWarningReportLimit) && $Dart(BuildErrorReportLimit) >= 0 } {
          continue
        }
        incr ReportedWarnings
      }
    }
    puts $Out "\t<$ErrorList($i,Type)>"
    puts $Out "\t\t<BuildLogLine>[XMLSafeString $ErrorList($i,Line)]</BuildLogLine>"
    puts $Out "\t\t<Text>[XMLSafeString $ErrorList($i,Text)]"
    puts $Out "</Text>"
    if { $ErrorList($i,SourceFile) != "" } \
    {
      puts $Out "\t\t<SourceFile>[XMLSafeString $ErrorList($i,SourceFile)]</SourceFile>"
      puts $Out "\t\t<SourceFileTail>[XMLSafeString [file tail $ErrorList($i,SourceFile)]]</SourceFileTail>"
      puts $Out "\t\t<SourceLineNumber>$ErrorList($i,SourceLineNumber)</SourceLineNumber>"
    }
    puts -nonewline $Out "\t\t<PreContext>"
    foreach l $ErrorList($i,PreContext) \
    {
      puts $Out [XMLSafeString $l]
    }
    puts $Out "</PreContext>"
    puts -nonewline $Out "\t\t<PostContext>"
    foreach l $ErrorList($i,PostContext) \
    {
      puts $Out [XMLSafeString $l]
    }
    puts $Out "</PostContext>"
    puts $Out "\t\t<RepeatCount>[XMLSafeString $ErrorList($i,RepeatCount)]</RepeatCount>"
    
    puts $Out "</$ErrorList($i,Type)>"
    puts $Out "\n"
  }
  
  puts $BuildLog "</body></html>"
  close $BuildLog
  
  #   set LogCompressionStatus [ catch { CompressFile $BuildLogTempFilename $BuildLogCompressedFilename } CompressionResult ]
  #   if { $LogCompressionStatus } \
  #   {
  #     error $CompressionResult
  #   }
  
  #   set BuildLog [open $BuildLogCompressedFilename r]
  #   fconfigure $BuildLog -translation binary
  #   close $BuildLog
  
  puts $Out "\t<Log Encoding=\"base64\" Compression=\"$Dart(CompressionType)\">"
  puts $Out "\t</Log>"
  puts $Out "\t<EndDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</EndDateTime>"
  
  puts $Out "</Build>"
  puts $Out "</Site>"
  
  close $Build
  close $Out

  # Leave a copy of the original build log in the Testing/Temporary directory
  # so that we can look at it later if we need to.  We only the last build log.
  file copy -force $BuildLogFilename [file join $BuildTempDir LastBuild.log]

  file delete $BuildLogTempFilename
  # file delete $BuildLogCompressedFilename
  file delete $BuildLogFilename
  
  puts "\tFinished Build"
  # Clean up any da files from previous coverage
  # FileMap [list .] [list *.da] "file delete -force -- "
  return
}

