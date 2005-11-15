# =========================================================================
# 
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Update.tcl,v $
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

proc ParseLog { LogFilename } {
  global UpdateLog
  set f [open $LogFilename r]
  set CurrentFile ""
  set WorkingFileString "Working file: "
  set stringIndex [string length $WorkingFileString]
  set EndString "============================================================================="
  set Buffer ""
  
  while { ![eof $f] } {
    # Find from "Working file:" to "==..."
    set Line [gets $f]
    if { $CurrentFile == "" } {
      if { [string first $WorkingFileString $Line] == -1 } { continue }
      # Parse the filename
      set CurrentFile [string trim [string range $Line $stringIndex end]]
      lappend Buffer $Line
    } else {
      # We're working on a file, so add the contents to the buffer
      # until we hit a "====..."
      lappend Buffer $Line
      if { $Line == $EndString } {
        set UpdateLog($CurrentFile) $Buffer
        set Buffer ""
        set CurrentFile ""
      }
    }
  }
  close $f
}
    

proc SplitLog { Log } \
{
  set Result ""
  set Buffer ""
  foreach Line [split $Log "\n"] \
  {
    switch -exact -- $Line \
    {
      "----------------------------" - \
      "=============================================================================" \
      {
        lappend Result $Buffer
        set Buffer ""
      }
      default \
      {
        append Buffer "$Line\n"
      }
    }
  }
  return [lrange $Result 1 end]
}

proc LoadCVSInformation { File } \
{
  global FileStatus UseDates Yesterday Today cvs YesterdayTS Model Users UpdateLog

  #  puts stderr $YesterdayTS


#   for { set i 0 } { $i < 10 } { incr i } {
#     set UpdateStatus [catch { set Log [exec $Dart(CVSCommand) log -N $File] }]
#     if { $UpdateStatus == 0 } {
#       # All is well
#       break
#     } else {
#       # Sleep for 10 seconds
#       after [expr 10 * 1000]
#     }
#   }
#   if { $UpdateStatus != 0 } {
#     return 0
#   }
  # puts stderr "Log of $File: $Log"
  set Log [join $UpdateLog($File) "\n"]

  regexp "head: (\[0-9.\]+)" $Log dummy FileStatus($File,Head)
  regexp "total revisions: (\[0-9\]+);\tselected revisions: (\[0-9\])" $Log dummy FileStatus($File,TotalRevisions) FileStatus($File,SelectedRevisions)

  set FileStatus($File,SelectedRevisions) 0
  
  set FileStatus($File,LastReportedRevision) $FileStatus($File,Head)
  set Logs [SplitLog $Log]
  set i 0
  set LastReported $FileStatus($File,Head)
  set HaveOne 0
  
  foreach SubLog $Logs \
  {
    set SplitLog [split $SubLog "\n"]
    set FileStatus($File,RevisionLog,$i,Date) "foo"
    set FileStatus($File,RevisionLog,$i,Author) "foo"
    regexp "date: (\[0-9\]+)/(\[0-9\]+)/(\[0-9\]+) (\[^;\]+);" [lindex $SplitLog 1] Date Year Month Day Time
    
    regexp "revision (\[0-9.\]+)" [lindex $SplitLog 0] dummy LastReported

    if { $i == 1 } \
    {
      set FileStatus($File,LastReportedRevision) $LastReported
    }
    
    set FileStatus($File,RevisionLog,$i,PreviousRevision) $LastReported
    set FileStatus($File,RevisionLog,$i,Revision) 1.1
    
    if { [regexp "revision (\[0-9.\]+)" [lindex $SplitLog 0] dummy FileStatus($File,RevisionLog,$i,Revision)] != 1 } {
      puts stderr "Did not find revision in:\n[lindex $SplitLog 0]"
    }

    if { $i != 0 } \
    {
      set FileStatus($File,RevisionLog,[expr $i - 1],PreviousRevision) $FileStatus($File,RevisionLog,$i,Revision)
    }

    #
    # Check to see if the date is still today
    # Always capture at least one revision...
    #     if { [clock scan "$Time $Month/$Day/$Year"] < $YesterdayTS && $HaveOne} \
    #     {
    #       break
    #     }
    set HaveOne 1

    if { 0 == [regexp "date: (\[^;\]+);  author: (\[^;\]+);" [lindex $SplitLog 1] dummy FileStatus($File,RevisionLog,$i,Date) FileStatus($File,RevisionLog,$i,Author)] } \
    {
      puts stderr "Failed to parse file!"
      puts stderr "Text was:\n[lindex $SplitLog 1]"
      set FileStatus($File,RevisionLog,$i,Date) "foo"
      set FileStatus($File,RevisionLog,$i,Author) "foo"
    }
    
    
    if { [info exists Users($FileStatus($File,RevisionLog,$i,Author))] } \
    {
      set FileStatus($File,RevisionLog,$i,Email) $Users($FileStatus($File,RevisionLog,$i,Author))
    } \
    else \
    {
      set FileStatus($File,RevisionLog,$i,Email) ""
    }
    

    foreach l [lrange $SplitLog 2 end] \
    {
      if { $l != {} } \
      {
        append FileStatus($File,RevisionLog,$i,Comment) "$l\n"
      }
    }
    incr i
    incr FileStatus($File,SelectedRevisions)
    if { $Model == "Experimental" && $i > 1 } \
    {
      break
    }
    if { $Model == "Continuous" && $i > 1 } \
    {
      break
    }
    # Break out if not today or yesterday...
    if { $Model == "Nightly" && $i > 1 } \
    {
      if { ![string match "$Today*" $Date] && ![string match "$Yesterday*" $Date] } \
      {
        break;
      }
    }
    
  }
  return 1
}

proc Update { Model OutFile BuildStampDir ClientServer } {
  global Dart Users FileStatus Today Yesterday
  global FileStatus UseDates Yesterday Today cvs YesterdayTS Users UpdateLog env
  # Update.tcl is now run from the binary directory, but it cd's to the 
  # source directory to do its work.
  # To avoid boot straping problems, this script
  # writes to stdout


  # stash the current directory
  set originalDir [pwd]
  set UpdateTempDir [file join $originalDir Testing Temporary]
  # Make sure temporary directory exists
  file mkdir $UpdateTempDir

  # Determine the mode
  set DateStamp ""

  switch -glob -- $ClientServer \
  {
    Client \
    {
      set BuildStamp [file tail $BuildStampDir]
    }
    Server \
    {
    }
  }


  # cd to the source directory
  cd $Dart(SourceDirectory)


  
  # Begin the XML output
  set Out [open $OutFile w]

  puts $Out $Dart(XMLHeader)
  # puts $Out [XMLStyleSheet "Update"]
  puts $Out "<Update mode=\"$ClientServer\">"
  if {$ClientServer == "Client"} {
    puts $Out "\t<Site>$Dart(Site)</Site>"
    puts $Out "\t<BuildName>$Dart(BuildName)</BuildName>"
    puts $Out "\t<BuildStamp>$BuildStamp</BuildStamp>"
  }


  
  # Do the update
  # Update to a particular time, but to do later.
  # clock format [clock scan today] -format "%Y-%m-%d 23:00 %Z" -gmt 1

  if { [info exists env(NOUPDATE)] } \
  {
      set UpdateCommand "[list $Dart(CVSCommand)] -n -z3 update $Dart(CVSUpdateOptions)"
  } \
  else \
  {
      set UpdateCommand "[list $Dart(CVSCommand)] -z3 update $Dart(CVSUpdateOptions)"
  }

  set UseDates 0
  if { $Model == "Nightly" } \
  {
    set t [GetNightlySeconds]

    set Date [clock format $t -format "%Y-%m-%d %H:%M GMT" -gmt 1]
    set UpdateCommand "$UpdateCommand -D \"$Date\""

    set Today [clock format $t -format "%Y/%m/%d"]
    set Yesterday [clock format [expr $t - 24 * 60 * 60 ] -format "%Y/%m/%d"]
    
  }
  if { $Model == "Experimental" || $Model == "Continuous"} \
  {
    set t [clock seconds]
    set Today [clock format $t -format "%Y/%m/%d"]
    set Yesterday [clock format [expr $t - 24 * 60 * 60 ] -format "%Y/%m/%d"]
  }

  if { $Model == "Nightly" } {
    puts $Out "\t<StartDateTime>$Date</StartDateTime>"
  } else {
    puts $Out "\t<StartDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</StartDateTime>"
  }

  puts "\tUpdating..."
  for { set i 0 } { $i < 10 } { incr i } {
     set cmd2 [MakeCommand $UpdateCommand [file join $UpdateTempDir update.tmp]]
     set UpdateStatus [catch { eval $cmd2 } result]
     if { $UpdateStatus == 0 } {
        # All is well
        break
     } else {
        # Sleep for 2 minutes
        # after [expr 2 * 60 * 1000]
        
        # Sleep for 10 seconds
        after [expr 10 * 1000]
     }
  }

  
  puts $Out "\t<UpdateCommand>$UpdateCommand</UpdateCommand>"
  puts $Out "\t<UpdateReturnStatus>[XMLSafeString $result]</UpdateReturnStatus>"

  set Update [open [file join $UpdateTempDir update.tmp] r]

  catch { unset AuthorList }
  catch { unset DirectoryList }
  set Files ""
  set Line 0
  while { ![eof $Update] } \
  {
    set Line [gets $Update]
    set F [string range $Line 2 end]
    if { [regexp "^U " $Line] || [regexp "^P " $Line] } \
    {
      set FileStatus($F,Status) Updated
      lappend Files $F
    }
    if { [regexp "^C " $Line] } \
    {
      set FileStatus($F,Status) Conflicting
      lappend Files $F
    }
    if { [regexp "^M " $Line] } \
    {
      set FileStatus($F,Status) Modified
      lappend Files $F
    }
  }
  close $Update
  # catch { file delete -force [file join $UpdateTempDir update.tmp] }

  puts "\tGathering version information..."
  set TempFilename [file join $UpdateTempDir UpdateLog.txt]
  # Get the CVS Log information
  for { set i 0 } { $i < 10 } { incr i } {
    # If we have less than 15 files, do them individually
    file delete $TempFilename
    # Create a blank file
    close [open $TempFilename w]
    if { [llength $Files] < 15 } {
      foreach File $Files {
        set UpdateStatus [catch { set Log [exec $Dart(CVSCommand) log -N $File >>& $TempFilename] }]
      }
    } else {
      set UpdateStatus [catch { set Log [exec $Dart(CVSCommand) log -N >>& $TempFilename] }]
    }
    if { $UpdateStatus == 0 } {
      # All is well
      # Load the info we need
      ParseLog $TempFilename
      # file delete $TempFilename
      break
    } else {
      # Sleep for 10 seconds
      after [expr 10 * 1000]
    }
  }

  # Get a little bit of info for each file
  foreach File $Files \
  {
    if { ![LoadCVSInformation $File] } {
      # Fill in bogus information
      set FileStatus($File,RevisionLog,0,Author) "Unknown"
      set FileStatus($File,RevisionLog,0,Date) "Unknown"
      set FileStatus($File,RevisionLog,0,Email) "Unknown"
      set FileStatus($File,RevisionLog,0,Comment) "Unknown"
      set FileStatus($File,Head) "Unknown"
      set FileStatus($File,LastReportedRevision) "Unknown"
      set FileStatus($File,SelectedRevisions) 0
    }
    # parray FileStatus "*$File*"
    lappend DirectoryList([file dir $File]) $File
    if { [info exists FileStatus($File,RevisionLog,0,Author)] } {
      lappend AuthorList($FileStatus($File,RevisionLog,0,Author)) $File
    }
  }

  foreach Dir [array names DirectoryList] \
  {
    puts $Out "\t<Directory>"
    puts $Out "\t\t<Name>[XMLSafeString $Dir]</Name>"
    foreach File $DirectoryList($Dir) \
    {
      puts $Out "\t<$FileStatus($File,Status)>"
      puts $Out "\t\t<File Directory=\"[XMLSafeString [file dir $File]]\">[XMLSafeString [file tail $File]]</File>"
      puts $Out "\t\t<Directory>[XMLSafeString [file dir $File]]</Directory>"
      puts $Out "\t\t<FullName>[XMLSafeString $File]</FullName>"
      
      puts $Out "\t\t<CheckinDate>[XMLSafeString $FileStatus($File,RevisionLog,0,Date)]</CheckinDate>"
      puts $Out "\t\t<Author>[XMLSafeString $FileStatus($File,RevisionLog,0,Author)]</Author>"
      puts $Out "\t\t<Email>[XMLSafeString $FileStatus($File,RevisionLog,0,Email)]</Email>"
      
      
      puts $Out "\t\t<Log>[XMLSafeString $FileStatus($File,RevisionLog,0,Comment)]</Log>"
      puts $Out "\t\t<Revision>$FileStatus($File,Head)</Revision>"
      puts $Out "\t\t<PriorRevision>$FileStatus($File,LastReportedRevision)</PriorRevision>"

      for { set i 0 } { $i < $FileStatus($File,SelectedRevisions) } { incr i } \
      {
        puts $Out "\t\t<Revisions>"
        foreach Field [list Revision PreviousRevision Author Date Comment Email] \
        {
          puts $Out "\t\t\t<$Field>[XMLSafeString $FileStatus($File,RevisionLog,$i,$Field)]</$Field>"
        }    
        puts $Out "\t\t</Revisions>"
      }
      puts $Out "\t</$FileStatus($File,Status)>"
    }
    puts $Out "\t</Directory>"
  }

  foreach Author [array names AuthorList] \
  {
    puts $Out "\t<Author>"
    puts $Out "\t\t<Name>[XMLSafeString $Author]</Name>"
    foreach File $AuthorList($Author) \
    {
      puts $Out "\t\t<File Directory=\"[XMLSafeString [file dir $File]]\">[file tail $File]</File>"
    }
    puts $Out "\t</Author>"
  }
  

  puts $Out "\t<EndDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</EndDateTime>"

  puts $Out "</Update>"
  close $Out

  # go back to the original
  cd $originalDir

  # if model is Continuous and nothing has changed in the repository,
  # then exit out so that DashboardManager can skip the rest of its commands
  if { $Model == "Continuous" && $Files == ""} {
    return 1
  } else {
    return 0
  }

}
