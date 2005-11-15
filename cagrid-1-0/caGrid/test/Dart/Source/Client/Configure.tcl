# =========================================================================
# 
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Configure.tcl,v $
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

proc Configure { Model BuildStampDir } {
  global Dart
  
  set UtilityDir [file join $Dart(DartRoot) Source Client]

  set HTMLDir [file join Testing HTML]
  set BuildTempDir [file join Testing Temporary]
  set SiteDir [file join $HTMLDir TestingResults Sites $Dart(Site)]
  set BuildNameDir [file join $SiteDir $Dart(BuildName)]

  set BuildStampDir [GetLastBuildDirectory $Model]
  if { $BuildStampDir == "" } \
  {
    puts "\tCould not find a BuildStamp in $BuildNameDir"
    exit 1
  }
  set BuildStamp [file tail $BuildStampDir]
  set XMLDir [file join $BuildStampDir XML]


  # We can assume that Testing/HTML/TestingResults/@SITE@ is built
  file mkdir $HTMLDir $SiteDir $BuildNameDir $BuildStampDir $BuildTempDir $XMLDir

  set ConfigureLogFilename [file join $BuildTempDir Configure.log.[pid]]

  # Begin the XML output
  set Out [open [file join $XMLDir Configure.xml] w]

  puts $Out $Dart(XMLHeader)
  # puts $Out [XMLStyleSheet "Configure"]
  puts $Out "<Site BuildName=\"$Dart(BuildName)\" BuildStamp=\"$BuildStamp\" Name=\"$Dart(Site)\">"
  puts $Out {<Configure>}
  puts $Out "\t<StartDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</StartDateTime>"

  # Configure the build
  puts "\tConfiguring build"
  puts $Out "<ConfigureCommand>[XMLSafeString $Dart(ConfigureCommand)]</ConfigureCommand>"

  set ConfigureStatus [catch {eval exec $Dart(ConfigureCommand) >& $ConfigureLogFilename} ]

  if { [glob -nocomplain $ConfigureLogFilename] == "" } {
    puts "$Dart(ConfigureCommand) produced no output at all, most likely failed to exec."
    puts $Out "<Log>No log generated</Log>"
  } else {
    set ConfigureLog [open $ConfigureLogFilename r]
    puts $Out "<Log>[XMLSafeString [read $ConfigureLog]]</Log>"
    close $ConfigureLog
  }
  puts $Out "\t<ConfigureStatus>$ConfigureStatus</ConfigureStatus>"
  puts "\tConfigure finished with status: $ConfigureStatus"

  puts $Out "\t<EndDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</EndDateTime>"
  puts $Out "</Configure>"
  puts $Out "</Site>"
  close $Out

  # Leave a copy of the original configure log in the Testing/Temporary directory
  # so that we can look at it later if we need to.  We only keep the last log.
  file copy -force $ConfigureLogFilename [file join $BuildTempDir LastConfigure.log]
  file delete -force $ConfigureLogFilename

  puts "\tFinished Configure"

}
