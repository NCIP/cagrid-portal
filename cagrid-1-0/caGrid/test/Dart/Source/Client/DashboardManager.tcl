# =========================================================================
#
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: DashboardManager.tcl,v $
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



#
# DashboardManager.tcl <Model> <Command1> [Command2]...
#
# Models:
#
# Experimental  -- Developer driven testing
# Nightly       -- Nightly build in lockstep with all other nightly builds
# Continuous    -- Build against the current version in the repository
#
# Commands for Experimental, Nightly, and Continuous:
#
# Start         -- Creates the proper directory
# Update        -- Perform a cvs update. For Nightly, update to the proper
#                  version of the code. For Continuous and Experimentals,
#                  update to the current repository versions
# Configure     -- Configure the build
# Build         -- Do the build and report in the proper results directory
# Test          -- Do the testing and report in the proper results directory
# Coverage      -- Summerize coverage information
# Purify        -- Summarize dynamic memory usage of the test cases. Search
#                  for memory leaks, array bound reads, etc..
# Valgrind      -- Summarize dynamic memory usage of the test cases. Search
#                  for memory leaks, array bound reads, etc..
# Submit        -- Submit the last set of reports to the Dart server.
#
# Commands for Dashboard building:
#
# DashboardStart [date]  -- Starts a dashboard for the given date
#                         Yesterday is default, only for nightly
# DashboardEnd   [date]  -- Ends a dashboard for the given date
#                         Yesterday is default, only for nightly.
# DashboardArchive AgeInDays -- Compresses HTML and XML reports older than
#                         AgeInDays

# Tell all programs they are being run from DART.
set env(DART_TEST_FROM_DART) 1

# Turn buffering off, because I'm too lazy to add flush commands
fconfigure stdout -buffering none

source [file join [file dir [info script]] Index.tcl]
# Load the defaults from there
LoadConfigurationFile Dart [file join [file dir [info script]] Defaults.conf]


set Usage "usage: $argv0 <config file> <Model> <Command1> \[Command2\]..."
if { $argc < 3 } \
{
  puts stderr $Usage
  exit 1
}
set LocalConfigFile DartLocal.conf

if { [regexp cygtclsh [info nameofexecutable]] == 1} {
  set modifiedArgv0 [exec cygpath -w [lindex $argv 0]]
  set ConfigFile $modifiedArgv0
  set LocalConfigFile [exec cygpath -w $LocalConfigFile]
} else {
  set ConfigFile [lindex $argv 0]
}
LoadConfigurationFile Dart $ConfigFile
if { [file exists $LocalConfigFile] } {
  LoadConfigurationFile Dart $LocalConfigFile
}


#puts "ConfigureCommand: $Dart(ConfigureCommand)"
#puts "CMakeCommand: $Dart(CMakeCommand)"
#puts "MakeCommand: $Dart(MakeCommand)"
#puts "CVSCommand: $Dart(CVSCommand)"
#puts "TclshCommand: $Dart(TclshCommand)"
#puts "JavaCommand: $Dart(JavaCommand)"
#puts "ScpCommand: $Dart(ScpCommand)"
#puts "PurifyCommand: $Dart(PurifyCommand)"
#puts "GunzipCommand: $Dart(GunzipCommand)"
#puts "CompressionCommand: $Dart(CompressionCommand)"
#puts "CompressionType: $Dart(CompressionType)"
#puts "CompressionMode: $Dart(CompressionMode)"


set XMLConfigFile [file join $Dart(BuildDirectory) DashboardConfig.xsl]
set Xalan [file join $Dart(DartRoot) Source Server xalan xalan.jar]
set Xalan [ManglePathName $Xalan]
set Dart(SourceDirectoryBase) [file dir $Dart(SourceDirectory)]

set TestingBaseDir [file join Testing HTML TestingResults]
set TemporaryDir [file join Testing Temporary]

set Model [lindex $argv 1]
set Commands [lrange $argv 2 end]

# Must be run in build directory
set BuildStampBase [file join $TestingBaseDir Sites $Dart(Site) $Dart(BuildName)]
set UtilitiesDir [file join $Dart(DartRoot) Source Client]

set NightlyDateTimeStamp [MakeNightlyDateTimeStamp]
set DateTimeStamp [MakeDateTimeStamp]

set BuildNameNotesRegExp "<BuildNameNotes\[ \t\r\n\]*>(\[^<\]*)(</BuildNameNotes>)"
set NotesRegExp "<Notes\[ \t\r\n\]*>(\[^<\]*)(</Notes>)"

# make sure the Temporary directory exists
file mkdir $TemporaryDir

# Read in the User's list
GetUsersList

set DashboardDir [file join $TestingBaseDir Dashboard ${NightlyDateTimeStamp}-Nightly]

switch -glob -- $Model \
{
  Con* \
  {
    set BuildStampDir [file join $BuildStampBase ${DateTimeStamp}-Continuous]
  }
  Exp* \
  {
    set BuildStampDir [file join $BuildStampBase $DateTimeStamp-Experimental]
    set DashboardDir [file join $TestingBaseDir Dashboard ${DateTimeStamp}-Experimental]
  }
  Nig* \
  {
    set BuildStampDir [file join $BuildStampBase ${NightlyDateTimeStamp}-Nightly]
  }
  default \
  {
    puts stderr "Unknown Model: $Model"
    exit 1
  }
}

if { [lsearch -exact $Commands "Start"] == -1 } {
  set BuildStampDir [GetLastBuildDirectory $Model]
}

set XMLDir [file join $BuildStampDir XML]
set XSLDir [file join $Dart(DartRoot) Source Server XSL]

set RebuildTestOverview OFF
set RebuildBuildOverview OFF

set StartingDirectory [pwd]

foreach Command $Commands \
{
  cd $StartingDirectory
  # note the switch uses "--" to end switches in case someone does a
  # "DashboardArchive -1" to compress all HTML and XML documents.
  switch -- $Command \
  {
    Start \
    {
      puts "Starting $Model Build [file tail $BuildStampDir]"
      set XMLDir [file join $BuildStampDir XML]
      file mkdir $BuildStampDir $XMLDir
    }
    Build - \
    Configure - \
    Coverage - \
    Purify - \
    Valgrind - \
    Submit - \
    Test {
      set CurrentDirectory [pwd]
      if { $BuildStampDir == "" } {
        error "Could not find build stamp, please use the Start command"
      }
      puts "$Command $Model"
      set Status [catch {$Command $Model $BuildStampDir} r]
      if { $Status } {
        puts $r
        puts $errorInfo
      }
      cd $CurrentDirectory
    }
    Update \
    {
      # If we don't have an existing update, make one
      if { ![file exists [file join $BuildStampDir Update.xml]] } \
      {
    # Actually do the update
    # If the Model is Experimental or Continuous, will do an update
        # against the latest sources
    # If the Model is Nightly, will update to the previous day's nightly snapshot.
        puts "\tBuilding Update.xml"
        set Status [catch { Update $Model [file join $Dart(BuildDirectory) $BuildStampDir XML Update.xml] $BuildStampDir Client } updateStatus]
        if { $Status } {
          puts $updateStatus
          puts $errorInfo
        }

        # If model is Continuous and no code changes are detected, then skip
        # the rest of the commands
        if { $Model == "Continuous" } {
          if {$updateStatus == 1} {
            puts "\tNo files changed, skipping remaining commands."
            exit 1
          }
        }
      }
    }
    DashboardArchive \
    {
      if {$argc == 4} {
        set AgeInDays [lindex $argv 3]
        puts "Archiving (compressing) reports older than $AgeInDays days."

        # move to the directory that contains Dashboards and Sites
        cd [file join $StartingDirectory $TestingBaseDir]

        # use the calculated NightlyDateTimeStamp
        # puts "Current nightly DateTimeStamp: $NightlyDateTimeStamp"
        set DashboardTimeStamp [ConvertStampToSeconds $NightlyDateTimeStamp]

        # calculate the mtime to use to determine what files to compress
        set AgeMTime [expr $DashboardTimeStamp - ($AgeInDays * 24 * 60 * 60)]

        # Compress old Dashboards
        FileMap [glob -nocomplain Dashboard/2*] [list *.xml] CompressOldFile
        FileMap [glob -nocomplain Dashboard/2*] [list *.html] CompressOldFile
        FileMap [glob -nocomplain Dashboard/2*/TestDetail] [list *.html] CompressOldFile
        # Compress old Site submissions. Go down into the BuildStamp
        # directories to avoid compressing the BuildNameNotes.xml.
        # (BuildNameNotes.xml is kept at the BuildName level of the tree
        # and hardly ever changes but it is read every day.)
        FileMap [glob -nocomplain Sites/*/*/2*/XML] [list *.xml] CompressOldFile
        FileMap [glob -nocomplain Sites/*/*/2*] [list *.xml] CompressOldFile
        FileMap [glob -nocomplain Sites/*/*/2*] [list *.html] CompressOldFile
        FileMap [glob -nocomplain Sites/*/*/2*/Results] [list *.html] CompressOldFile
        FileMap [glob -nocomplain Sites/*/*/2*/Purify] [list *.html] CompressOldFile
        FileMap [glob -nocomplain Sites/*/*/2*/DynamicAnalysis] [list *.html] CompressOldFile
        # note that coverage information is one directory above the rest.
        # if the coverage information is old, then compress it
        FileMap [glob -nocomplain Sites/*/*/Coverage] [list *.html] CompressOldFile

      } else {
          puts stderr "Dashboard archiving needs an \"age in days\" specified"
      }
    }
    DashboardStart \
    {
      set FakeUpdate 0
      if { $Model == "Nightly" && $argc == 4 } \
      {
        # If we are starting on "old" Nightly dashboard,
        # fake the Update.xml file
        if { [lindex $argv 3] != $NightlyDateTimeStamp } \
        {
          set FakeUpdate 1
        }
        set NightlyDateTimeStamp [lindex $argv 3]
        set DashboardDir [file join $TestingBaseDir Dashboard ${NightlyDateTimeStamp}-Nightly]
      }

      puts "Making $DashboardDir"
      file mkdir $DashboardDir
      set Dir [pwd]

      #cd $SourceDirectory   # Update.tcl now cd's to source directory

      # If we don't have an existing update, make one
      if { ![file exists [file join $Dir $DashboardDir Update.xml]] } \
      {
        if { $FakeUpdate } \
        {
          puts "Faking update"
          set Update [open [file join $Dir $DashboardDir Update.xml] w]
          puts $Update $Dart(XMLHeader)
          # puts $Update [XMLStyleSheet "Update"]
          puts $Update "<Update></Update>"
          close $Update
        } \
        else \
        {
          # Actually do the update
          # If the Model is Experimental, will do an update against the
          # latest sources
          # If the Model is Nightly, will update to the previous day's
          # 23:00 repository snapshot
          puts "\tBuilding Update.xml"
          set Status [catch {Update $Model [file join $Dir $DashboardDir Update.xml] $BuildStampDir Server} updateStatus]
          if { $Status } {
            puts $updateStatus
            puts $errorInfo
          }
        }
      }

      if { ![file exists [file join $DashboardDir Doxygen.xml]] } \
      {
        if { [string toupper $Dart(BuildDoxygen)] == "ON" && [file exists $Dart(DoxygenConfig)] } \
        {
        # Create the Doxygen.xml file...
              puts "\tBuilding Doxygen.xml"
              catch { Doxygen $Model [file join $Dir $DashboardDir Doxygen.xml] }
        }
      }


      puts "\tCompleted"
      #cd $Dir  # Update.tcl now puts us back in the current directory
    }
    DashboardEnd \
    {
      # Collect all the latest BuildStamps, and any from today's Nightlies

      # Make the images directory and copy things over
      # Icons need to go in BuildDir/Testing/HTML/TestingResults/Icons
      # From Dart/Source/Server/www/Icons
      set DashboardIconsDir [file join $TestingBaseDir Icons]
      set IconsSourceDir [file join $Dart(DartRoot) Source Server www Icons]

      # Silently fails if the directory exists
      file mkdir [file join $DashboardIconsDir]
      set IconList [glob -nocomplain [file join $IconsSourceDir *]]
      foreach Icon $IconList \
      {
        if { [file isdir $Icon] } \
        {
          continue
        }
        set TargetIcon [file join $DashboardIconsDir [file tail $Icon]]
        if { ![file exists $TargetIcon] || [expr [file mtime $Icon] > [file mtime $TargetIcon]] } \
        {
          puts "Copy $Icon to $DashboardIconsDir"
          # Copy it over.
          file copy -force $Icon $DashboardIconsDir
        }
      }

      # Make the javascript directory and copy things over
      # Javascript goes in BuildDir/Testing/HTML/TestingResults/Javascript
      # From Dart/Source/Server/www/Javascript
      set DashboardJavascriptDir [file join $TestingBaseDir Javascript]
      set JavascriptSourceDir [file join $Dart(DartRoot) Source Server www Javascript]

      # Silently fails if the directory exists
      file mkdir [file join $DashboardJavascriptDir]
      set JavascriptList [glob -nocomplain [file join $JavascriptSourceDir *]]
      foreach Javascript $JavascriptList \
      {
        if { [file isdir $Javascript] } \
        {
          continue
        }
        set TargetJavascript [file join $DashboardJavascriptDir [file tail $Javascript]]
        if { ![file exists $TargetJavascript] || [expr [file mtime $Javascript] > [file mtime $TargetJavascript]] } \
        {
          puts "Copy $Javascript to $DashboardJavascriptDir"
          # Copy it over.
          file copy -force $Javascript $DashboardJavascriptDir
        }
      }

      if { $Model == "Nightly" && $argc == 4 } \
      {
        # If we are starting on "old" Nightly dashboard
        set NightlyDateTimeStamp [lindex $argv 3]
        set DashboardDir [file join $TestingBaseDir Dashboard ${NightlyDateTimeStamp}-Nightly]
      } \
      else \
      {
        set DashboardDir [GetLastDashboardDirectory $Model]
        set NightlyDateTimeStamp [string range [file tail $DashboardDir] 0 12]
      }

      puts "NightlyDateTimeStamp: $NightlyDateTimeStamp"
      set YesterdayDateTimeStamp [GetYesterdayDateTimeStamp $NightlyDateTimeStamp]
      set TomorrowDateTimeStamp [GetTomorrowDateTimeStamp $NightlyDateTimeStamp]
      set DashboardTimeStamp [ConvertStampToSeconds $NightlyDateTimeStamp]
      set DashboardDate [clock format $DashboardTimeStamp -format "%A, %B %d %Y" -gmt 1]

      if { $DashboardDir == "" } \
      {
        puts stderr "Did not find a dashboard to end"
        exit 1
      }

      # Get the nightly builds relevant to this Dashboard
      set NightlyBuilds ""

      switch -glob $Model \
      {
        Nig* \
        {
          # Get both the nightlies and the experimentals...
          foreach f [glob -nocomplain -- [file join $TestingBaseDir Sites * * 2*]] \
          {
            # Ignore the coverage special directory...
            if { [file tail $f] == "Coverage" } { continue }
            if { [IsBuildStampInDay $NightlyDateTimeStamp [file tail $f]] } \
            {
              lappend NightlyBuilds $f
            }
          }
        }
        Exp* \
        {
          # Just get the non-nightlies
          foreach f [glob -nocomplain -- [file join $TestingBaseDir Sites * * *-Experimental]] \
          {
            if { [IsBuildStampInDay $NightlyDateTimeStamp [file tail $f]] } \
            {
              lappend NightlyBuilds $f
            }
          }
        }
        Cont* \
        {
          # Just get the non-nightlies
          foreach f [glob -nocomplain -- [file join $TestingBaseDir Sites * * *-Continuous]] \
          {
            if { [IsBuildStampInDay $NightlyDateTimeStamp [file tail $f]] } \
            {
              lappend NightlyBuilds $f
            }
          }
        }
      }

      # What times were dashboards rolled up on the server.
      # This glob finds old Dart Dashboards of the form 20011021-Nightly
      # as well as new Dart Dashboards of the form 20011021-0300-Nightly
      puts "Building Dashboard history"
      set previousDashboards [glob -nocomplain [file join $TestingBaseDir Dashboard "*-Nightly"]]
      # strip the paths off the Dashboards
      regsub -all "[file join $TestingBaseDir Dashboard]/" $previousDashboards {} previousDashboards
      # build a map indicating what start times were used for which dates
      foreach pDashboard $previousDashboards {
          if { [regexp {(^[0-9]*)-([0-9]*)-Nightly} $pDashboard dummy d sTime] } {
              lappend startTimeMap($sTime) $d
          } elseif { [regexp {(^[0-9]*)-Nightly} $pDashboard dummy d] } {
              lappend startTimeMap(None) $d
          }
      }
      # write the map to a Javascript file that represents the dashboards
      # on this server
      set dMap [open [file join $DashboardJavascriptDir DashboardMap.js.[pid]] "w"]
      puts $dMap "// Generated file listing the start times for all Nightly dashoards on this "
      puts $dMap "// Dart server."
      puts $dMap "//"
      puts $dMap "// Last generated on [AbbreviateTimeZone [clock format [clock seconds]]]"
      puts $dMap ""
      puts $dMap "var dashboardStartTimes = new Array();"
      puts $dMap "dst = dashboardStartTimes; // Reference to array, shortens length of map file"
      foreach sTime [array names startTimeMap] {
          foreach dStamp $startTimeMap($sTime) {
              puts $dMap "dst\[\"$dStamp\"\] = \"$sTime\";"
          }
      }
      close $dMap
      # rename the map file
      file rename -force [file join $DashboardJavascriptDir DashboardMap.js.[pid]] [file join $DashboardJavascriptDir DashboardMap.js]


      set Dir [pwd]
      cd $DashboardDir
      set DashboardStamp [file tail $DashboardDir]

      puts "Dashboard [file tail $DashboardDir]"
      if { [file exists Update.xml] && [NeedToRemake Update] } \
      {
        puts -nonewline "\tBuilding Update.html "

        puts [catch { exec $Dart(JavaCommand) -jar $Xalan -Q -IN Update.xml -OUT Update.html -XSL file:///[file join $XSLDir Update.xsl]  -PARAM TestDocDir [pwd] -PARAM DashboardDate $DashboardDate -PARAM DashboardStamp $DashboardStamp -PARAM CVSWebURL $Dart(CVSWebURL) -PARAM CVSROOT $Dart(CVSROOT) -PARAM BuildDoxygen $Dart(BuildDoxygen) -PARAM UseDoxygen [string toupper $Dart(UseDoxygen)] -PARAM DoxygenURL $Dart(DoxygenURL) -PARAM UseGnats [string toupper $Dart(UseGnats)] -PARAM GnatsWebURL $Dart(GnatsWebURL) -PARAM ProjectURL $Dart(ProjectURL) -PARAM RollupURL $Dart(RollupURL)} Result]
        set Result [string trim $Result]
        if { $Result != "" } { puts "\t$Result" }
      }
      if { [NeedToRemake Doxygen] } \
      {
        if { [string toupper $Dart(BuildDoxygen)] == "ON" } \
        {
          if { [file exists Doxygen.xml] } \
          {
            puts -nonewline "\tBuilding Doxygen.html "
            puts [catch { exec $Dart(JavaCommand) -jar $Xalan -Q -IN Doxygen.xml -OUT Doxygen.html -XSL file:///[file join $XSLDir Doxygen.xsl]  -PARAM DashboardDate $DashboardDate -PARAM TestDocDir [pwd] -PARAM DashboardStamp $DashboardStamp -PARAM CVSWebURL $Dart(CVSWebURL) -PARAM CVSROOT $Dart(CVSROOT) -PARAM BuildDoxygen $Dart(BuildDoxygen) -PARAM UseDoxygen [string toupper $Dart(UseDoxygen)] -PARAM DoxygenURL $Dart(DoxygenURL) -PARAM UseGnats [string toupper $Dart(UseGnats)] -PARAM GnatsWebURL $Dart(GnatsWebURL) -PARAM ProjectURL $Dart(ProjectURL) -PARAM RollupURL $Dart(RollupURL)} Result]
            set Result [string trim $Result]
            if { $Result != "" } { puts "\t$Result" }
              } else {
            puts "\tNot Building Doxygen.html (missing Doxygen.xml)"
          }
        }
      }


      set DashboardTargets [open DashboardTargets.txt w]
      set DashboardFile [open Dashboard.xml w]

      puts $DashboardFile $Dart(XMLHeader)
      # puts $DashboardFile [XMLStyleSheet "Dashboard"]
      puts $DashboardFile "<Dashboard>"

      # Add some information about the dashboard
      puts $DashboardFile "<Information>"
      puts $DashboardFile "\t<DashboardStamp>$DashboardStamp</DashboardStamp>"
      set Time [ConvertStampToSeconds [file tail $DashboardDir]]
      puts $DashboardFile "\t<Model>$Model</Model>"
      puts $DashboardFile "\t<GMT></GMT>"
      puts $DashboardFile "\t<LocalTime>[AbbreviateTimeZone [clock format [clock seconds]]]</LocalTime>"
      puts $DashboardFile "\t<Yesterday>$YesterdayDateTimeStamp-Nightly</Yesterday>"
      puts $DashboardFile "\t<Tomorrow>$TomorrowDateTimeStamp-Nightly</Tomorrow>"
      puts $DashboardFile "</Information>"

      XMLConcat $DashboardFile UpdateSummary.xml
      XMLConcat $DashboardFile DoxygenSummary.xml

      set ExpectedBuilds $Dart(ExpectedBuilds)
      if { [llength $ExpectedBuilds] > 0 } {
          set UsingExpectedBuilds 1
      } else {
          set UsingExpectedBuilds 0
      }

      foreach NightlyBuild $NightlyBuilds \
      {
        puts "\tNightlyBuild $NightlyBuild"
        puts $DashboardTargets $NightlyBuild
        set CurrentDir [pwd]
        cd [file join $Dir $NightlyBuild]

        set NightlyBuildSplit [file split $NightlyBuild]
        set Length [llength $NightlyBuildSplit]

        # Must do this in tcl8.0, in higher versions end-2 is valid
        set e2 [expr $Length - 2 - 1]
        set e1 [expr $Length - 1 - 1]
        set CurrentSite [lindex $NightlyBuildSplit $e2]
        set CurrentBuildName [lindex $NightlyBuildSplit $e1]
        set CurrentBuildStamp [lindex $NightlyBuildSplit end]

        # If a Nightly, look up this build in the expected build list
        set wasExpected 0
        set isNightly 0
        if { [string first "Nightly" $CurrentBuildStamp] != -1 } {
           set isNightly 1
           set index [lsearch $ExpectedBuilds [list $CurrentSite $CurrentBuildName]]
           if { $index != -1 } {
             # Remove it from the list
             set ExpectedBuilds [lreplace $ExpectedBuilds $index $index]
             set wasExpected 1
           }
        }

        if { ![file exists [file join XML Build.xml]] && ![file exists [file join XML Test.xml]] && ![file exists [file join XML Update.xml]]} \
        {
          puts "\t\tCould not find file [file join [pwd] XML Build.xml], [file join [pwd] XML Test.xml], or [file join [pwd] XML Update.xml]"
          cd $CurrentDir
          continue
        }

        # Output the BuildStamp tag, mark the build as expected as
        # necessary
        if { $UsingExpectedBuilds == 1 } {
           if { $isNightly == 1} {
              # this is a Nightly build, so mark it as expected or not
              if { $wasExpected == 1 } {
                 puts $DashboardFile "<BuildStamp expected=\"yes\">"
              } else {
                 puts $DashboardFile "<BuildStamp expected=\"no\">"
              }
           } else {
             # not a Nightly build, so do not mark it expected at all
             puts $DashboardFile "<BuildStamp>"
           }
        } else {
           # not using expected builds, don't mark anything as expected
           puts $DashboardFile "<BuildStamp>"
        }

        # Tack any dashboard notes onto the dashboard XML file
        XMLConcat $DashboardFile [file join XML DashboardNotes.xml]

        # Join the BuildName notes with the BuildStamp notes
        #
        set BuildNameNotesExist 0
        set NotesExist 0
        set RebuildNotes 0
        if { [file exists [file join ".." BuildNameNotes.xml]] } {
          set BuildNameNotesExist 1
          set BuildNameNotesMTime [file mtime [file join ".." BuildNameNotes.xml]]
        }
        if { [file exists [file join "XML" Notes.xml]] } {
          set NotesExist 1
          set NotesMTime [file mtime [file join "XML" Notes.xml]] 
        }
        # Do we already have a NotesCollection file? This file is
        # created (down below) by joining the BuildNameNotes.xml file
        # and the Notes.xml together. If we already have one and it is
        # newer than the constituents, then we do no need to rebuild
        # the notes.
        if { [file exists [file join "XML" NoteCollection.xml]] } {
          set NoteCollectionMTime [file mtime [file join "XML" NoteCollection.xml]] 
          # if the NoteCollection file newer than the separate notes files?
          if { $BuildNameNotesExist } {
            if { [expr $BuildNameNotesMTime > $NoteCollectionMTime] } {
               set RebuildNotes 1
            }
          }
          if { $NotesExist } {
            if { [expr $NotesMTime > $NoteCollectionMTime] } {
              set RebuildNotes 1
            }
          }
        } else {
            # no NotesCollection file, so set RebuildNotes to 1 if there are
            # any notes
            if { $BuildNameNotesExist || $NotesExist } {
              set RebuildNotes 1
            }
        }

        if { $RebuildNotes } {
          set NotesFile [open [file join XML NoteCollection.xml] w]
          puts $NotesFile $Dart(XMLHeader)
          # puts $NotesFile [XMLStyleSheet "Notes"]
          # puts $NotesFile "<NoteCollection BuildName=\"$CurrentBuildName\" BuildStamp=\"$CurrentBuildStamp\" Name=\"$CurrentSite\">"
          puts $NotesFile "<NoteCollection>"
          if { $BuildNameNotesExist } {
            XMLConcat $NotesFile [file join ".." BuildNameNotes.xml]
          }
          if { $NotesExist } {
            XMLConcat $NotesFile [file join "XML" Notes.xml]
          }
          puts $NotesFile "</NoteCollection>"
          close $NotesFile

          puts -nonewline "\t\tBuilding Notes.html "
          puts [catch { exec $Dart(JavaCommand) -Xmx256m -jar $Xalan -Q -IN [file join XML NoteCollection.xml] -OUT Notes.html -XSL file:///[file join $XSLDir Notes.xsl]  -PARAM TestDocDir [file join $Dir $NightlyBuild/]   -PARAM DashboardStamp $DashboardStamp -PARAM DashboardDate $DashboardDate -PARAM CVSWebURL $Dart(CVSWebURL) -PARAM CVSROOT $Dart(CVSROOT) -PARAM BuildDoxygen $Dart(BuildDoxygen) -PARAM UseDoxygen [string toupper $Dart(UseDoxygen)] -PARAM DoxygenURL $Dart(DoxygenURL) -PARAM UseGnats [string toupper $Dart(UseGnats)] -PARAM GnatsWebURL $Dart(GnatsWebURL) -PARAM ProjectURL $Dart(ProjectURL) -PARAM RollupURL $Dart(RollupURL)} Result]
          set Result [string trim $Result]
          if { $Result != "" } { puts "\t$Result" }
        }

        # always tack on the submission date for the NotesCollection file
        if { [file exists [file join "XML" NoteCollection.xml]] } {
          puts $DashboardFile "\t<NotesSubmissionDateTime>[AbbreviateTimeZone [clock format [file mtime [file join XML NoteCollection.xml]]]]</NotesSubmissionDateTime>"
        }

        # Convert the standard XML submissions to HTML
        #
        foreach XML [list Update Build Configure Test Coverage Purify DynamicAnalysis] \
        {
          if { ![file exists [file join XML $XML.xml]] } \
          {
            continue
          }
          puts $DashboardFile "\t<${XML}SubmissionDateTime>[AbbreviateTimeZone [clock format [file mtime [file join XML $XML.xml]]]]</${XML}SubmissionDateTime>"

          if { [NeedToRemake $XML] } \
          {
            if { $XML == "Test" } {
              # New test results have been submitted, so we will need to
              # regenerate TestOverview information
              set RebuildTestOverview ON
            }
            if { $XML == "Build" } {
              # New build results have been submitted, so we will need to
              # regenerate BuildOverview information
              set RebuildBuildOverview ON
            }

            puts -nonewline "\t\tBuilding $XML.html "
            puts [catch { exec $Dart(JavaCommand) -Xmx256m -jar $Xalan -Q -IN [file join XML $XML.xml] -OUT $XML.html -XSL file:///[file join $XSLDir $XML.xsl]  -PARAM TestDocDir [file join $Dir $NightlyBuild/]   -PARAM DashboardStamp $DashboardStamp -PARAM DashboardDate $DashboardDate -PARAM CVSWebURL $Dart(CVSWebURL) -PARAM CVSROOT $Dart(CVSROOT) -PARAM BuildDoxygen $Dart(BuildDoxygen) -PARAM UseDoxygen [string toupper $Dart(UseDoxygen)] -PARAM DoxygenURL $Dart(DoxygenURL) -PARAM UseGnats [string toupper $Dart(UseGnats)] -PARAM GnatsWebURL $Dart(GnatsWebURL) -PARAM ProjectURL $Dart(ProjectURL) -PARAM RollupURL $Dart(RollupURL)} Result]
            set Result [string trim $Result]
            if { $Result != "" } { puts "\t$Result" }
          }
        }

        # Convert any base64 files to unencoded form
        if { [info tclversion] < 8.1 } {
            puts "\t\tCannot decode base64 files with this version of tcl: [info tclversion] < 8.1"
            foreach file [glob -nocomplain [file join $Dir $NightlyBuild Results *.base64]] {
                 file delete $file
          }
        } else {
          foreach file [glob -nocomplain [file join $Dir $NightlyBuild Results *.base64]] {
            puts "\t\tDecoding [file tail $file]"
            set f [open $file r]
            set fout [open [file root $file] w]
            fconfigure $fout -translation binary
            puts -nonewline $fout [base64::decode [read $f]]
            close $f
            close $fout
            file delete $file
          }
        }

        # Check to see if this is a Continuous build, and send some email if Errors are != 0
        # and we have some authors
        # This ContinuousEmail is deprecated in favour of BrokenBuildEmail
        if { [info exists Dart(DeliverContinuousEmail)] && [string toupper $Dart(DeliverContinuousEmail)] == "ON" } {
          if { [string first "Continuous" $CurrentBuildStamp] != -1 } {
            # Check to see if we can send mail, or have already sent it
            if { [file exists UpdateSummary.xml]
                 && [file exists BuildSummary.xml]
                 && ![file exists MailSent.txt] } {
              set AuthorList [GetAuthorsFromUpdateSummary UpdateSummary.xml]
              set foo [GetErrorsWarningsFromBuildSummary BuildSummary.xml]
              set ErrorCount [lindex $foo 0]
              set WarningCount [lindex $foo 1]
              if { $ErrorCount != 0 && $AuthorList != "" } {
                puts "\t\tBroken continuous build, sending mail to $AuthorList"
                # Send the email
                ezsmtp::config -from $Dart(ContinuousFrom) -mailhost $Dart(SMTPMailhost)
                # Compose the body
                set body "This is the body of the Continuous email"
                set subject "Broken continuous build for $Dart(ContinuousProject) $CurrentSite/$CurrentBuildName"
                set body "A continuous build has been broken for $Dart(ContinuousProject) and you have been identified\n"
                append body "as one of the authors who have checked in changes that are part of this build.\n\n"
                append body "Errors for this build are here: $Dart(ContinuousBaseURL)/Sites/$CurrentSite/$CurrentBuildName/$CurrentBuildStamp/BuildError.html\n\n"
                append body "The changes for this build are here: $Dart(ContinuousBaseURL)/Sites/$CurrentSite/$CurrentBuildName/$CurrentBuildStamp/Update.html\n\n"
                append body "The dashboard for the day is here:  $Dart(ContinuousBaseURL)/Dashboard/$DashboardStamp/Dashboard.html\n\n"
                append body "If you have any questions about this email, please contact the $Dart(ContinuousProject) Continuous build monitors: $Dart(ContinuousMonitorList)"
                set mailStatus [catch {ezsmtp::send -tolist $AuthorList -cclist $Dart(ContinuousMonitorList) -subject $subject -body $body} mailResult]
                if { $mailStatus != 0 } {
                    puts "Error sending Continuous build notifications: $mailResult"
                }
              }
              set f [open MailSent.txt w]
              puts $f [clock format [clock seconds]]
              close $f
            }
          }
        }

        if { [info exists Dart(DeliverBrokenBuildEmail)] } {
          set CurrentBuildModel [string range $CurrentBuildStamp [expr [string last "-" $CurrentBuildStamp] + 1] end]
          if { [lsearch $Dart(DeliverBrokenBuildEmail) $CurrentBuildModel] != -1 } {
            # Check to see if we can send mail, or have already sent it
            if { [file exists UpdateSummary.xml]
                 && ( ($Dart(DeliverBrokenBuildEmailWithBuildErrors) && [file exists BuildSummary.xml])
                    ||($Dart(DeliverBrokenBuildEmailWithBuildWarnings) && [file exists BuildSummary.xml])
                    ||($Dart(DeliverBrokenBuildEmailWithConfigureFailures) && [file exists ConfigureSummary.xml])
                    ||($Dart(DeliverBrokenBuildEmailWithTestNotRuns) && [file exists TestSummary.xml])
                    ||($Dart(DeliverBrokenBuildEmailWithTestFailures) && [file exists TestSummary.xml]) )
                 && ![file exists MailSent.txt] } {
              set MonitorList {}
              foreach {maint} $Dart(BuildMonitors) {
                if { [regexp [lindex $maint 0] $CurrentSite] && [regexp [lindex $maint 1] $CurrentBuildName] } {
                  set MonitorList [concat $MonitorList [lindex $maint 2]]
                }
              }
              set AuthorList [GetAuthorsFromUpdateSummary UpdateSummary.xml]


              set foo [GetErrorsWarningsFromBuildSummary BuildSummary.xml]
              set ErrorCount [lindex $foo 0]
              set WarningCount [lindex $foo 1]
              set ConfigureStatus [GetStatusFromConfigureSummary ConfigureSummary.xml]
              set foo [GetCountsFromTestSummary TestSummary.xml]
              set TestNotRunCount [lindex $foo 0]
              set TestFailureCount [lindex $foo 1]
              if { ($AuthorList != "")
                && ( ($Dart(DeliverBrokenBuildEmailWithBuildErrors) && $ErrorCount != 0)
                  || ($Dart(DeliverBrokenBuildEmailWithBuildWarnings) && $WarningCount != 0)
                  || ($Dart(DeliverBrokenBuildEmailWithConfigureFailures) && $ConfigureStatus != 0)
                  || ($Dart(DeliverBrokenBuildEmailWithTestNotRuns) && $TestNotRunCount != 0)
                  || ($Dart(DeliverBrokenBuildEmailWithTestFailures) && $TestFailureCount != 0)) } {
                puts "\t\tProblem with $CurrentBuildModel build, sending mail to $AuthorList"
                # Send the email
                ezsmtp::config -from $Dart(EmailFrom) -mailhost $Dart(SMTPMailhost)
                # Compose the body
                set subject "Problem with $CurrentBuildModel build for $Dart(EmailProjectName) $CurrentSite/$CurrentBuildName"
                set body "A problem has arisen with $CurrentBuildModel build for $Dart(EmailProjectName) and you have been identified\n"
                append body "as one of the authors who have checked in changes that are part of this build.\n\n"
                if { $Dart(DeliverBrokenBuildEmailWithConfigureFailures) && $ConfigureStatus != 0 } {
                  append body "There are configure failures: $Dart(DartboardBaseURL)/Sites/$CurrentSite/$CurrentBuildName/$CurrentBuildStamp/Configure.html\n\n" }
                if { $Dart(DeliverBrokenBuildEmailWithBuildErrors) && $ErrorCount != 0 } {
                  append body "There are build errors: $Dart(DartboardBaseURL)/Sites/$CurrentSite/$CurrentBuildName/$CurrentBuildStamp/BuildError.html\n\n" }
                if { $Dart(DeliverBrokenBuildEmailWithBuildWarnings) && $WarningCount != 0 } {
                  append body "There are build warnings: $Dart(DartboardBaseURL)/Sites/$CurrentSite/$CurrentBuildName/$CurrentBuildStamp/BuildWarning.html\n\n" }
                if { ($Dart(DeliverBrokenBuildEmailWithTestNotRuns) && $TestNotRunCount != 0)
                  || ($Dart(DeliverBrokenBuildEmailWithTestFailures) && $TestFailureCount != 0)} {
                  append body "There are test problems: $Dart(DartboardBaseURL)/Sites/$CurrentSite/$CurrentBuildName/$CurrentBuildStamp/Test.html\n\n" }
                append body "The changes for this build are here: $Dart(DartboardBaseURL)/Sites/$CurrentSite/$CurrentBuildName/$CurrentBuildStamp/Update.html\n\n"
                append body "The dashboard for the day is here:  $Dart(DartboardBaseURL)/Dashboard/$DashboardStamp/Dashboard.html\n\n"
                if { $MonitorList != "" } {
                  append body "If you have any questions about this email, please contact the $Dart(EmailProjectName)\n"
                  append body "monitors for this build: $MonitorList"
                }
                set mailStatus [catch {ezsmtp::send -tolist $AuthorList -cclist $MonitorList -subject $subject -body $body} mailResult]
                if { $mailStatus != 0 } {
                  puts "Error sending Continuous build notifications: $mailResult"
                }
              }
              set f [open MailSent.txt w]
              puts $f [clock format [clock seconds]]
              puts $f "To: $AuthorList"
              puts $f "Cc: $MonitorList"
              close $f
            }
          }
        }


        # Do CoverageLog files
        foreach XML [glob -nocomplain [file join XML CoverageLog*.xml]] {
          puts $DashboardFile "\t<[file tail ${XML}]SubmissionDateTime>[AbbreviateTimeZone [clock format [file mtime $XML]]]]</[file tail ${XML}]SubmissionDateTime>"
          set XML [file root [file tail $XML]]
          if { [NeedToRemake $XML] } \
          {
            puts -nonewline "\t\tBuilding $XML.html "
            puts [catch { exec $Dart(JavaCommand) -Xmx256m -jar $Xalan -Q -IN [file join XML $XML.xml] -OUT $XML.html -XSL file:///[file join $XSLDir CoverageLog.xsl]  -PARAM TestDocDir [file join $Dir $NightlyBuild/] -PARAM DashboardStamp $DashboardStamp -PARAM DashboardDate $DashboardDate -PARAM CVSWebURL $Dart(CVSWebURL) -PARAM CVSROOT $Dart(CVSROOT) -PARAM BuildDoxygen $Dart(BuildDoxygen) -PARAM UseDoxygen [string toupper $Dart(UseDoxygen)] -PARAM DoxygenURL $Dart(DoxygenURL) -PARAM UseGnats [string toupper $Dart(UseGnats)] -PARAM GnatsWebURL $Dart(GnatsWebURL) -PARAM ProjectURL $Dart(ProjectURL) -PARAM RollupURL $Dart(RollupURL)} Result]
            set Result [string trim $Result]
            if { $Result != "" } { puts "\t$Result" }
          }
        }
        foreach f [glob -nocomplain [file join XML CoverageLog*.xml]] {
          file delete $f
        }


        # If HTML files are to be compressed, do the compression for this build
        # Note: test results and purify results are in a subdirectory
        # of the  BuildStamp whereas there is coverage results are stored
        # in a subdirectory of the BuildName (since coverage information is
        # large we only keep the most recent coverage results for a BuildName).
        if { $Dart(CompressionMode) == "ALL" && $Dart(CompressionType) == "gzip" } {

          foreach f [glob -nocomplain *.html Results/*.html DynamicAnalysis/*.html Purify/*.html ../Coverage/*.html] {
          if { ![file exists ${f}z] || ([file exists ${f}z] && [expr [file mtime $f] > [file mtime ${f}z]]) } {
              puts "\t\tCompressing $f"
              CompressFile $f ${f}z
              # if the compression worked, remove the original
              set compressedSize 0
              catch {set compressedSize [file size ${f}z]}
              if { [expr $compressedSize > 0] } {
                  RemoveFile $f
              }
            }
          }
        }

        # Finish this build up by tacking the build summary information
          # onto the Dashboard XML file
        cd $CurrentDir

        foreach Summary [list UpdateSummary ConfigureSummary BuildSummary TestSummary CoverageSummary PurifySummary DynamicAnalysisSummary NotesSummary] \
        {
          XMLConcat $DashboardFile [file join $Dir $NightlyBuild $Summary.xml]
        }
        puts $DashboardFile "</BuildStamp>"

      }

      # Add any missing builds
      foreach MissingBuild $ExpectedBuilds {
        puts $DashboardFile "<BuildStamp expected=\"yes\">"
        puts $DashboardFile "<MissingBuild>"
        puts $DashboardFile "<SiteName>[XMLSafeString [lindex $MissingBuild 0]]</SiteName>"
        puts $DashboardFile "<BuildName>[XMLSafeString [lindex $MissingBuild 1]]</BuildName>"
        puts $DashboardFile "</MissingBuild>"
        puts $DashboardFile "</BuildStamp>"
      }

      puts $DashboardFile "</Dashboard>"

      close $DashboardFile
      close $DashboardTargets

      puts "Dashboard $DashboardStamp -- Started [AbbreviateTimeZone [clock format [clock seconds]]]"

      # Only build TestOverview if new test results have been submitted
      # or if TestOverview has never been generated. This flag gets passed
      # to Dashboard.xsl which builds TestOverview.xml as a side-effect.
      # So when RebuildTestOverview is "OFF", Dashboard.xsl will not try
      # to build TestOverview.xml and consequently TestOverview.html and
      # the TestDetail paged will not need to be regenerated.
      if { $RebuildTestOverview != "ON" && [NeedToRemake TestOverview] } {
          set RebuildTestOverview ON
      }
      # Same for BuildOverview
      if { $RebuildBuildOverview != "ON" && [NeedToRemake BuildOverview] } {
          set RebuildBuildOverview ON
      }

      # Now run xalan to build the dashboard
      foreach XML [list Dashboard TestOverview BuildOverview] \
      {
        if { [NeedToRemake $XML] } \
        {
          set CreationDate [AbbreviateTimeZone [clock format [clock seconds]]]
          puts -nonewline "\tBuilding $XML.html "
          flush stdout
          puts [catch { exec $Dart(JavaCommand) -Xmx256m -jar $Xalan -Q -IN $XML.xml -OUT $XML.html.[pid] -XSL file:///[file join $XSLDir $XML.xsl]  -PARAM CreationDate $CreationDate -PARAM DashboardDate $DashboardDate -PARAM DashboardStamp $DashboardStamp -PARAM DashboardPath [pwd] -PARAM CVSWebURL $Dart(CVSWebURL) -PARAM CVSROOT $Dart(CVSROOT) -PARAM BuildDoxygen $Dart(BuildDoxygen) -PARAM UseDoxygen [string toupper $Dart(UseDoxygen)] -PARAM DoxygenURL $Dart(DoxygenURL) -PARAM UseGnats [string toupper $Dart(UseGnats)] -PARAM GnatsWebURL $Dart(GnatsWebURL) -PARAM ProjectURL $Dart(ProjectURL) -PARAM RollupURL $Dart(RollupURL) -PARAM BuildTestOverview $RebuildTestOverview -PARAM BuildBuildOverview $RebuildBuildOverview} Result]
          catch { file rename -force -- $XML.html.[pid] $XML.html }
          set Result [string trim $Result]
          if { $Result != "" } { puts "\t$Result" }
        } \
        else \
        {
          puts "\tNo need to rebuild $XML.html"
        }

      }

      # Compress any HTML files for the Dashboard
      if { $Dart(CompressionMode) == "ALL" && $Dart(CompressionType) == "gzip" } {
          foreach f [glob -nocomplain *.html TestDetail/*.html] {
              if { ![file exists ${f}z] || ([file exists ${f}z] && [expr [file mtime $f] > [file mtime ${f}z]]) } {
                  puts "\tCompressing $f"
                  CompressFile $f ${f}z
                  # if the compression worked, remove the original
                  set compressedSize 0
                  catch {set compressedSize [file size ${f}z]}
                  if { [expr $compressedSize > 0] } {
                      RemoveFile $f
                  }
              }
          }
      }


      # Add the HTML redirections from MostRecentResults to latest.
      cd ..
      puts [pwd]
      file delete -force MostRecentResults-Nightly
      file mkdir MostRecentResults-Nightly
      cd MostRecentResults-Nightly
      foreach name {Dashboard TestOverview TestOverviewByCount TestOverviewByTest Update BuildOverview} {
          if [catch {open $name.html w} fileid] {
              puts stderr "Cannot open $name.html: $fileid"
          } else {
              set url [file join ".." [file tail $DashboardDir] $name.html]
              puts $fileid "
              <HTML>
              <BODY>
              <SCRIPT language='JavaScript'>
              <!--
              if (document.images)
                  location.replace('$url');
              else
                  location.href='$url';
              // -->
              </SCRIPT>
              <META HTTP-EQUIV='Refresh' CONTENT='0; URL=$url'>
              </BODY>
              </HTML>"
              close $fileid
          }
      }
      puts "Dashboard $DashboardStamp -- Finished [AbbreviateTimeZone [clock format [clock seconds]]]"
    }
  }
}
