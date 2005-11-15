# =========================================================================
#
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Utility.tcl,v $
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


proc ManglePathName { path } {
  if { [regexp cygtclsh [info nameofexecutable]] == 1 && $path != "" } {
    set path [exec cygpath -w $path]
    set path [eval file join [file split $path]]
  }
  return $path
}


#
# combines a command and a stdout/stderr redirect to a file in a safe manner
#
proc MakeCommand {cmd outDir } {
   set cmd2 "exec $cmd >& \"$outDir\""
   return $cmd2
}

proc LoadConfigurationFile { arrayname filename } {
  # Open the file, read each of the options in this form
  # option: value
  upvar $arrayname Options
  if { ![file exists $filename] } { return }
  set f [open $filename r]
  set d [read $f]
  close $f
  # Replace backslash line continuation
  regsub -all "\\\\\n" $d "" replaced
  set d [split $replaced "\n"]
  foreach line $d {
    set line [string trim $line]
    # Ignore comments, empty lines, and mis-formed lines
    if { [string index $line 0] == "\#"
     || $line == ""
     || [string first ":" $line] == -1 } {
      continue
    }
    set index [string first ":" $line]
    set option [string range $line 0 [expr $index - 1]]
    set value [string range $line [expr $index + 1] end]
    # Don't override an existing value with a blank
    if { ( [info exists Options($option)] && $value != "" )
         || ![info exists Options($option)] } {
      set Options($option) [string trim $value]
    }
  }
  if { [info exists Options(MangledPathNames) ] } {
    foreach var $Options(MangledPathNames) {
      set Options($var) [ManglePathName $Options($var)]
    }
  }
}

proc MakeDateTimeStamp {} \
{
  return [clock format [clock seconds] -format %Y%m%d-%H%M -gmt 1]
}

proc NeedToRemake { File } \
{
  set XML [file join XML $File.xml]
  if { ![file exists $File.html] && ![file exists $File.htmlz]} \
  {
    # puts "Remake 1"
    return 1
  }

  # If both uncompressed and compressed versions of the HTML file exist, use
  # the date of the newest
  set htmlFileMTime 0
  set compressedHTMLFileMTime 0
  set latestMTime 0

  if { [file exists $File.html] } {
     set htmlFileMTime [file mtime $File.html]
  }

  if { [file exists $File.htmlz] } {
     set compressedHTMLFileMTime [file mtime $File.htmlz]
  }

  if { [expr $htmlFileMTime > $compressedHTMLFileMTime] } {
      set latestMTime $htmlFileMTime
  } else {
      set latestMTime $compressedHTMLFileMTime
  }

  # Is the XML file in XML directory?  i.e. something submitted by a client
  if { [file exists $XML] } \
  {
    return [expr $latestMTime <= [file mtime $XML]]
  }

  # Is the file in the current directory? i.e. something produced on server
  # while processing other XML files.
  if { [file exists $File.xml] } \
  {
    return [expr $latestMTime <= [file mtime $File.xml]]
  }
  return 1
}

proc IsBuildStampInDay { NightlyDateTimeStamp BuildStamp } \
{
  global Dart
  # Convert the BuildStamp into something recognizable
  if { [catch { set NTS [ConvertStampToSeconds $NightlyDateTimeStamp] } r ] } \
  {
    puts $r
    return 0;
  }

  # NTS is in seconds, so we want the range to be from NTS to NTS + 24 hours
  set Start $NTS
  set End [expr $Start + 24 * 60 * 60]

  if { [catch { set BSTS [ConvertStampToSeconds $BuildStamp] } r ] } \
  {
    puts $r
    return 0;
  }

  if { $BSTS >= $Start && $BSTS < $End } \
  {
    return 1
  } \
  else \
  {
    return 0
  }
}

proc GetNightlySeconds {} \
{
    global Dart

    # Convert the nightly start time to seconds. Since we are
    # providing only a time and a timezone, the current date of the
    # local machine is assumed. Consequently, nightlySeconds is the
    # time at which the nightly dashboard was opened or will be opened
    # on the date of the current client machine.  As such, this time
    # may be in the past or in the future.
    set nightlySeconds [clock scan $Dart(NightlyStartTime)]

    # If nightlySeconds is in the past, this is the current open
    # dashboard, then return nightlySeconds.  If nightlySeconds is in
    # the future, this is the next dashboard to be opened, so subtract 24
    # hours to get the time of the current open dashboard
    if { $nightlySeconds <= [clock seconds] } {
        return $nightlySeconds
    } else {
        return [expr $nightlySeconds - 24 * 60 * 60]
    }

}

proc MakeNightlyDateTimeStamp {} \
{
  set t [GetNightlySeconds]
  return [clock format $t -format %Y%m%d-%H%M -gmt 1]
}

proc ConvertStampToSeconds { Stamp } \
{
  if { [string range $Stamp 8 8] != "-" } \
  {
    # Stamp is from a submission using old-style encoding for Experimental.
    set Year [string range $Stamp 0 3]
    set Month [string range $Stamp 4 5]
    set Day [string range $Stamp 6 7]
    set Hour [string range $Stamp 8 9]
    set Minute [string range $Stamp 10 11]
    set Seconds [clock scan "$Month/$Day/$Year $Hour:$Minute" -gmt 1]
    return $Seconds
  }

  if { [string range $Stamp 8 12] == "-Nigh" } \
  {
    # Stamp is from a submission using old-style encoding for Nightly.
    set Year [string range $Stamp 0 3]
    set Month [string range $Stamp 4 5]
    set Day [string range $Stamp 6 7]
    # Use nightly start time as build's time since encoding doesn't provide it.
    set NDTS [MakeNightlyDateTimeStamp]
    set Hour [string range $NDTS 9 10]
    set Minute [string range $NDTS 11 12]
    set Seconds [clock scan "$Month/$Day/$Year $Hour:$Minute" -gmt 1]
    return $Seconds
  }

  set Year [string range $Stamp 0 3]
  set Month [string range $Stamp 4 5]
  set Day [string range $Stamp 6 7]
  set Hour [string range $Stamp 9 10]
  set Minute [string range $Stamp 11 12]
  set Seconds [clock scan "$Month/$Day/$Year $Hour:$Minute" -gmt 1]
  return $Seconds
}

# Test should be an optional function that takes the name of a directory
# of file and returns false if it should be ignored.

proc FileMap { Queue PatternList Map {Test {}}} \
{

  while { [llength $Queue] != 0 } \
  {
    set Filename [lindex $Queue 0]
    set Queue [lrange $Queue 1 end]

    if { [file isdirectory $Filename] } \
    {
      if { [file tail $Filename] == "CVS" } \
      {
        continue
      }
      if {$Test != {}} {
         if {[$Test $Filename]} {
            set Status [catch { set Queue [concat $Queue [glob -nocomplain -- [file join $Filename *]]] } Result]
         }
      } else {
         set Status [catch { set Queue [concat $Queue [glob -nocomplain -- [file join $Filename *]]] } Result]
      }
      if { $Status } \
      {
        puts "Caught $Result"
      }
      continue;
    }
    foreach Pattern $PatternList \
    {
      if { [string match $Pattern [file tail $Filename]] } \
      {
        if {$Test == {} || [$Test $Filename]} {
          catch { eval $Map $Filename }
        }
      }
    }
  }
}

proc GetYesterdayDateTimeStamp { NightlyDateTimeStamp } \
{
  if { $NightlyDateTimeStamp == "" } {
    set ts [clock seconds]
  } else {
    set ts [ConvertStampToSeconds $NightlyDateTimeStamp]
  }
  return [clock format [expr $ts - 24 * 60 * 60] -format %Y%m%d-%H%M -gmt 1]
}

proc GetTomorrowDateTimeStamp { NightlyDateTimeStamp } \
{
  if { $NightlyDateTimeStamp == "" } {
    set ts [clock seconds]
  } else {
    set ts [ConvertStampToSeconds $NightlyDateTimeStamp]
  }
  return [clock format [expr $ts + 24 * 60 * 60] -format %Y%m%d-%H%M -gmt 1]
}

proc GetLastBuildDirectory { {Model Experimental} } \
{
  global Dart
  set HTMLDir [file join Testing HTML]
  set SiteDir [file join $HTMLDir TestingResults Sites $Dart(Site)]
  set BuildNameDir [file join $SiteDir $Dart(BuildName)]

  set GlobString 2*-Experimental
  if { $Model == "Nightly" } \
  {
    set GlobString 2*-Nightly
  }
  if { $Model == "Continuous" } \
  {
    set GlobString 2*-Continuous
  }
  set BuildStampDirs ""
  set BuildStampDirs [glob -nocomplain -- [file join $BuildNameDir $GlobString]]
  return [lindex [lsort $BuildStampDirs] end]
}

proc GetLastDashboardDirectory { {Model Experimental} } \
{
  global Dart

  set GlobString 2*-Experimental
  if { $Model == "Nightly" } \
  {
    set GlobString 2*-Nightly
  }
  if { $Model == "Continuous" } \
  {
    set GlobString 2*-Continuous
  }
  set DashboardDirs ""
  set DashboardDirs [glob -nocomplain -- [file join Testing HTML TestingResults Dashboard $GlobString]]
  return [lindex [lsort $DashboardDirs] end]
}


proc EchoFileName { file } \
{
  puts $file
}


proc RemoveFile { file } \
{
  file delete -force -- $file
}


proc CompressOldFile { file } \
{
  global AgeMTime
  global Dart

  if { $Dart(CompressionType) == "gzip" } {
      if { [expr [file mtime $file] < $AgeMTime] } {
          puts "\tCompressing $file"
          CompressFile $file ${file}z
          # if the compression worked, then remove the original
          set compressedSize 0
          catch {set compressedSize [file size ${file}z]}
          if { [expr $compressedSize > 0] } {
              RemoveFile $file
          }
      } else {
          # puts "$file compression skipped"
      }
  }
}

proc CompressFile { In Out } \
{
  global Dart

  if { $In == $Out } \
  {
    error "CompressFile, can not compress a file to itself $In"
  }
  switch -glob $Dart(CompressionCommand) \
  {
    *gzip* \
    {
      set Dart(CompressionType) gzip
      return [exec $Dart(CompressionCommand) -c $In > $Out]
    }
    *compress* \
    {
      set Dart(CompressionType) compress
      return [exec $Dart(CompressionCommand) -c $In > $Out]
    }
    *zip* \
    {
      set Dart(CompressionType) zip
      return [exec $Dart(CompressionCommand) - $In > $Out]
    }
    default \
    {
      set Dart(CompressionType) none
      return [file copy -force $In $Out]
    }
  }
}

proc XMLConcat { FP filename } \
{
  catch {
    set f [open $filename r]
      while { ![eof $f] } {
    set line [gets $f]
    if { ![string match {<?xml*} $line] } \
     {
         puts $FP $line
     }
      }
    # puts -nonewline $FP [read $f]
    close $f
  }
}


proc XMLSafeString { str } \
{
  # Here is where all substitutions should be done
  # regsub -all "\"" $str {%quot;} str
  regsub -all "&" $str {\&amp;} str
  regsub -all "<" $str {\&lt;} str
  regsub -all ">" $str {\&gt;} str
  # regsub -all "'" $str {%apos;} str

  return [PrintableString $str]
  return $str
}

# Set inside PrintableString
set XMLCharMap ""
proc PrintableString { str } \
{
  global XMLCharMap
  # need to remove nulls, and > 0x80
  if { [info tclversion] > 8.0 } { regsub -all "\0" $str "^BadChar^" str }
  if { [info tclversion] > 8.2 } {
    # remove all problem characters
    while { ![string is ascii -failindex idx $str] } {
      # Remove the bad character
      set str [string replace $str $idx $idx "^BadChar^"]
    }
    if { $XMLCharMap == "" } {
      # Remove < 0x20, aside from \t, \n, \r
      # > 20 hex are OK, == 32 decimal
      for { set t 0 } { $t < 32 } { incr t } {
        if { $t == 9 || $t == 10 || $t == 13 } { continue }
        lappend XMLCharMap [binary format c $t]
        lappend XMLCharMap "^BadChar^"
      }
    }
    set str [string map $XMLCharMap $str]
  }
  return $str
}

proc XMLStyleSheet { basename } {
    global Dart
    return "<?xml-stylesheet type=\"text/xsl\" href=\"file:///[file join $Dart(DartRoot) Source Server XSL $basename.xsl]\"?>"
}



proc GetDirectories { dir } \
{
  set result ""
  foreach f [glob -nocomplain -- $dir]
  {
    if { [file isdirectory $f] } \
    {
      lappend result $f
    }
  }
  return $result
}


proc InitBase64 {} \
{
  global Base64

  if { [info exists Base64(0)] } { return }
  set String "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

  set count [string length $String]
  for { set i 0 } { $i < $count } { incr i } \
  {
    set Base64($i) [string index $String $i]
  }
  set Base64(Pad) "="
}

proc _toU8 { num } \
{
  return [expr ( $num + 0x100 ) % 0x100]
}

proc Base64Encode { String } \
{
  global Base64
  InitBase64

  set length [string length $String]
  set result ""
  set index 0
  set count 0
  while { $length > 2 } \
  {
    binary scan [string index $String $index] c current0
    set current0 [_toU8 $current0]
    incr index
    binary scan [string index $String $index] c current1
    set current1 [_toU8 $current1]
    incr index
    binary scan [string index $String $index] c current2
    set current2 [_toU8 $current2]
    incr index

    # puts "$current0 $current1 $current2"

    append result $Base64([expr $current0 >> 2])
    append result $Base64([expr (($current0 & 0x03) << 4) + ($current1 >> 4 )])
    append result $Base64([expr (($current1 & 0x0f ) << 2) + ($current2 >> 6) ])
    append result $Base64([expr $current2 & 0x3f])
    incr length -3
  }


  if { $length != 0 } \
  {
    # puts "length = $length"
    binary scan [string index $String $index] c current0
    set current0 [_toU8 $current0]
    incr index
    append result $Base64([expr $current0 >> 2])
    if { $length > 1 } \
    {
      binary scan [string index $String $index] c current1
      set current1 [_toU8 $current1]
      incr index
      append result $Base64([expr (($current0 & 0x03) << 4) + ($current1 >> 4)])
      append result $Base64([expr ($current1 & 0x0f) << 2])
      append result $Base64(Pad)
    } \
    else \
    {
      append result $Base64([expr ($current0 & 0x03) << 4])
      append result $Base64(Pad)
      append result $Base64(Pad)
    }
  }
  # Break result into 78 character counts
  set length [string length $result]
  set ret ""
  set index 0
  while { $length > 60 } \
  {
    append ret "[string range $result 0 59]\n"
    set result [string range $result 60 end]
    incr length -60
  }
  if { $length != 0 } \
  {
    append ret $result
  }
  return $ret
}

proc Base64EncodeFileToFile { In Out } \
{
  global Base64
  InitBase64
  set length 0
  set result ""
  set index 0
  set count 0

  while { ![eof $In] } \
  {
    append String [read $In 2049]
    incr length [string length $String]
    while { $length > 2 } \
    {
      binary scan [string index $String $index] c current0
      set current0 [_toU8 $current0]
      incr index
      binary scan [string index $String $index] c current1
      set current1 [_toU8 $current1]
      incr index
      binary scan [string index $String $index] c current2
      set current2 [_toU8 $current2]
      incr index

      set result ""
      append result $Base64([expr $current0 >> 2])
      append result $Base64([expr (($current0 & 0x03) << 4) + ($current1 >> 4 )])
      append result $Base64([expr (($current1 & 0x0f ) << 2) + ($current2 >> 6) ])
      append result $Base64([expr $current2 & 0x3f])
      incr length -3
      incr count 4
      puts -nonewline $Out $result
      if { $count >= 78 } \
      {
    puts $Out ""
    set count 0
      }
    }
  }

  set result ""
  if { $length != 0 } \
  {
    # puts "length = $length"
    binary scan [string index $String $index] c current0
    set current0 [_toU8 $current0]
    incr index
    append result $Base64([expr $current0 >> 2])
    if { $length > 1 } \
    {
      binary scan [string index $String $index] c current1
      set current1 [_toU8 $current1]
      incr index
      append result $Base64([expr (($current0 & 0x03) << 4) + ($current1 >> 4)])
      append result $Base64([expr ($current1 & 0x0f) << 2])
      append result $Base64(Pad)
    } \
    else \
    {
      append result $Base64([expr ($current0 & 0x03) << 4])
      append result $Base64(Pad)
      append result $Base64(Pad)
    }
    incr count 3
  }
  puts $Out $result
}

proc lremove { l item } \
{
  # Return a list with all instances of the item removed
  set ret ""
  foreach element $l \
  {
    if { [string compare $element $item] != 0 } \
    {
      lappend ret $element
    }
  }
  return $ret
}


proc Grep { matchlist file { Exceptions {} } } \
{
  set result ""
  foreach line [split $file "\n"] \
  {
    set E 0
    foreach match $matchlist \
    {
      if { [regexp $match $line] } \
      {
    set E 1
    foreach Exception $Exceptions \
    {
      if { [regexp $Exception $line] } \
      {
        set E 0
      }
    }
    if { $E } \
    {
      lappend result $line
      break
    }
      }
    }
  }
  return $result
}

proc Match { match file { Exceptions {} } } \
{
  return [expr [llength [Grep $match $file $Exceptions]] > 0 ]
}

proc FindTests { Path {clear 1}} \
{
  global TestList
  if { ![info exists TestList] } { set TestList "" }
  if { $clear } { set TestList "" }

  set testCommands [GetTestfileTests]
  if { $testCommands != ""} {
    # insert $Path to the list for this test and change how tests are run to a
    # (cwd $Path; exec; cwd $Back)
    foreach test $testCommands {
       set testCommand [list [lindex $test 0] $Path ]
       set testCommand [concat $testCommand [lrange $test 1 end]]
       lappend TestList $testCommand
    }
  }

  foreach SubDir [GetTestfileSubdirs] \
  {
    # Need to return to the previous directory.
    # Using cd .. does not always work.
    set curDir [pwd]
    cd $SubDir
    FindTests [file join $Path $SubDir] 0
    cd $curDir
  }
  return $TestList
}


proc GetCMakeVariable { Variable } \
{
  if { ![file exists CMakeLists.txt] } \
  {
    error "No CMakeLists.txt file"
  }
  set Contents ""
  set f [open CMakeLists.txt r]
  set Contents [read $f]
  close $f
  regsub -all "\n" $Contents " " C1
  regsub -all {\)} $C1 ")\n " C
  set Contents $C

  set variableValue ""
  foreach Line [split $Contents "\n"] \
  {
    if { [string first $Variable $Line] != -1 } \
    {
      set i [expr [string first "(" $Line] + 1]
      set l [string trim [string range $Line $i end]]
      set l [string trim $l "()"]
      eval lappend variableValue $l
    }
  }
  return $variableValue
}

# Pull out all ADD_TEST commands from a Testfile. Each ADD_TEST command
# adds a sublist which is {TestName ExeName [args]}
proc GetTestfileTests {} \
{
  if { ![file exists DartTestfile.txt] } \
  {
    error "No DartTestfile.txt found"
  }
  set Contents ""
  set f [open DartTestfile.txt r]
  set Contents [read $f]
  close $f
  regsub -all "\n" $Contents " " C1
  regsub -all {\)} $C1 ")\n " C
  set Contents $C

  set variableValue ""
  foreach Line [split $Contents "\n"] \
  {
    if { [string first ADD_TEST $Line] != -1 } \
    {
      if { [string first "#" $Line] != -1 && [string first "#" $Line] < [string first ADD_TEST $Line] } {
        continue;
      }
      set i [expr [string first "(" $Line] + 1]
      set l [string trim [string range $Line $i end]]
      set l [string trim $l "()"]
      if {$l != ""} {
        lappend variableValue $l
      }
    }
  }
  return $variableValue
}


# Pull out all SUBDIRS commands from a Testfile and append them into a
# single list.
proc GetTestfileSubdirs {} \
{
  if { ![file exists DartTestfile.txt] } \
  {
    error "No DartTestfile.txt found"
  }
  set Contents ""
  set f [open DartTestfile.txt r]
  set Contents [read $f]
  close $f
  regsub -all "\n" $Contents " " C1
  regsub -all {\)} $C1 ")\n " C
  set Contents $C

  set variableValue ""
  foreach Line [split $Contents "\n"] \
  {
    if { [string first SUBDIRS $Line] != -1 } \
    {
      set i [expr [string first "(" $Line] + 1]
      set l [string trim [string range $Line $i end]]
      set l [string trim $l "()"]
      eval lappend variableValue $l
    }
  }
  return $variableValue
}


# If the specified Microsoft Workspace has an ALL_BUILD project, return all
# projects it (ALL_BUILD) depends on.
#
proc FindAllBuildDSPDepends { Workspace } {
  set dsps ""

  if { [file exists $Workspace] } {
    set f [open $Workspace r]

    while { ![eof $f] } {
      set pname ""

      set Line [gets $f]

      # Extract the project name from
      # 'Project: "PROJECT_NAME"=PROJECT_NAME.dsp - Package Owner <4>'
      regexp "^Project: \"(\[^\"\]*)\"=" $Line dummy pname

      # If project name is ALL_BUILD
      if { $pname == "ALL_BUILD" } {
        # Search for project dependencies listed in DSW
        while { ![eof $f] } {
          set Line [gets $f]

          # if Line is, Project: "PROJECT_NAME"=.... then we are done
          if {[regexp "^Project: \"(\[^\"\]*)\"=" $Line d1 d2] } {
            close $f
            return $dsps
          }

          # if Line is, Project_Dep_Name PROJECT_NAME
          # then add it to dsp list
          if {[regexp "^Project_Dep_Name (.*)" $Line dummy pname]} {
            lappend dsps $pname
          }
        }
      }
    }

    # if we get here, make sure we close the file
    close $f
  }
}

proc AbbreviateTimeZone { dt } {

  if { [regsub "Eastern Standard Time" $dt "EST" newdt] } {
    return $newdt
  } elseif { [regsub "Eastern Daylight Time" $dt "EDT" newdt] } {
    return $newdt
  } else {
    return $dt
  }
}

proc lunique { l } {
  # Return a new list containing only unique members of l
  foreach i $l {
    set a($i) 1
  }
  return [array names a]
}

proc GetAuthorsFromUpdateSummary { filename } {
  set Authors ""
  set expression {<Author>([^<]*)</Author>}
  if { ![file exists $filename] } { return $Authors }
  set f [open $filename r]
  while { ![eof $f] } {
    set line [gets $f]
    if { [regexp $expression $line foo a] } {
      lappend Authors $a
    }
  }
  close $f
  set Email [FormatEmailAddress [lunique $Authors]]
  return $Email
}


proc GetErrorsWarningsFromBuildSummary { filename } {
  set Errors 0
  set Warnings 0
  set eexpression {<ErrorCount>([^<]*)</ErrorCount>}
  set wexpression {<WarningCount>([^<]*)</WarningCount>}
  if { ![file exists $filename] } { return [list $Errors $Warnings] }
  set f [open $filename r]
  while { ![eof $f] } {
    set line [gets $f]
    if { [regexp $eexpression $line foo a] } {
      set Errors $a
    }
    if { [regexp $wexpression $line foo a] } {
      set Warnings $a
    }
  }
  close $f
  return [list $Errors $Warnings]
}

proc GetStatusFromConfigureSummary { filename } {
  set Status 0
  set sexpression {<ConfigureStatus>([^<]*)</ConfigureStatus>}
  if { ![file exists $filename] } { return $Status }
  set f [open $filename r]
  while { ![eof $f] } {
    set line [gets $f]
    if { [regexp $sexpression $line foo a] } {
      set Status $a
    }
  }
  close $f
  return $Status
}


proc GetCountsFromTestSummary { filename } {
  set Passed 0
  set NotRun 0
  set Failed 0
  set pexpression {<PassedCount>([^<]*)</PassedCount>}
  set fexpression {<FailedCount>([^<]*)</FailedCount>}
  set nexpression {<NotRunCount>([^<]*)</NotRunCount>}
  if { ![file exists $filename] } { return [list $NotRun $Failed $Passed] }
  set f [open $filename r]
  while { ![eof $f] } {
    set line [gets $f]
    if { [regexp $pexpression $line foo a] } {
      set Passed $a
    }
    if { [regexp $nexpression $line foo a] } {
      set NotRun $a
    }
    if { [regexp $fexpression $line foo a] } {
      set Failed $a
    }
  }
  close $f
  return [list $NotRun $Failed $Passed]
}


proc FormatEmailAddress { Addresses } {
  global Dart Users

  set r ""
  foreach Address $Addresses {
    set Address [string trim $Address]
    if { [info exists Users($Address)] } {
      # We have a lookup!
      set Address $Users($Address)
    }
    if { [string first "@" $Address] != -1 } {
      # Looks like a valid email address
      lappend r $Address
    } else {
      # Try to slap on the DefaultContinuousDomain variable
      if { [info exists Dart(DefaultContinuousDomain)] && $Dart(DefaultContinuousDomain) != "" } {
        lappend r "$Address@$Dart(DefaultContinuousDomain)"
      } elseif { [info exists Dart(CVSIdentToEmail)] && $Dart(CVSIdentToEmail) != "" } {
        set AuthorEmail ""
        foreach Rule $Dart(CVSIdentToEmail) {
          if { [regsub [lindex $Rule 0] $Address [lindex $Rule 1] AuthorEmail] } {
            break
          } else {
            set AuthorEmail {}
          }
        }
        if {$AuthorEmail == ""} {
          puts "\t\tERROR: Can't match $Address in CVSIdentToEmail"
        } elseif {$AuthorEmail != "DO_NOT_EMAIL"} {
          set r [concat $r $AuthorEmail]
        }
      }
    }
  }
  return $r
}

proc GetUsersList {} {
  global Users
  global Dart
  # Read in the user list
  set CheckFiles [list [file join $Dart(SourceDirectory) Documentation UserList.txt] \
                  [file join $Dart(SourceDirectory) CVSROOT users]]

  foreach File $CheckFiles {
    if {[file exists $File]} {
      set f [open $File]
      while { ![eof $f] } \
      {
        set l [split [string trim [gets $f]] ":"]
        switch [llength $l] \
        {
          3 {
            # Insight style
            set Users([lindex $l 0]) [lindex $l 2]
          }
          2 {
            # CVSROOT style
            set Users([lindex $l 0]) [lindex $l 1]
          }
        }
      }
      close $f
    }
  }
}

# A fileevent proc to immediately dump output, and set done when
# finished
proc DumpOutput { p out } {
  global done
  if { [eof $p] } {
    set done "Completed"
  } else {
    puts -nonewline $out [read $p 1]
  }
}

# This code handles fileevents and idle callbacks
proc UtilityReadFile { p } {
  global done Output
  if { [eof $p] } {
    set done "Completed"
  } else {
    if { [gets $p line] != -1 } {
      puts $Output "$line\n"
    }
  }
}

proc UtilityIdleTimeout {} {
  global done
  set done "Timeout"
}

proc RunTimedCommand { command Timeout LogFile } {
  global done
  global Dart
  global Output
  global tcl_platform
  # Setup a timer for the test.
  set Output [open $LogFile w]

  set p [open "| $command |&  [list $Dart(TclshCommand)] [file join $Dart(DartRoot) Source Client Cat.tcl]"]
  # If the gets operation blocks, then the tcl event loop
  # will never get a chance to see the timeout.
  fconfigure $p -blocking 0

  fileevent $p readable [list UtilityReadFile $p]
  set Callback [after [expr int($Timeout * 1000)] UtilityIdleTimeout]
  # Wait for a timeout, or the command to complete
  vwait done
  # Cancel the after callback, and the fileevent.
  after cancel $Callback
  fileevent $p readable ""

  # Assume we passed, until proven otherwise.
  set Status 0
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

  close $Output
  # Configure the command pipeline to be blocking before closing,
  # to ensure that close with a useful exit status.
  fconfigure $p -blocking 1
  return [close $p]
}
