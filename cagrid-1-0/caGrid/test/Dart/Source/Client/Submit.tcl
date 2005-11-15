# =========================================================================
#
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Submit.tcl,v $
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

# This script submits testing results to the centralized server.  It
# uses ftp, scp, or cp protocols for transfering the testing
# results. It then uses a http geturl protocol to notify the server of
# that testing results have been dropped into the drop box.  If
# your testing machine is behind a firewall, set two environment variables
# HTTP_PROXY and HTTP_PROXY_PORT and the http trigger will run through
# your filewall.
#

proc BuildRemoteFileName { DropBoxPrefix XMLFile } {
  set RemoteFilename [file join $DropBoxPrefix [file tail $XMLFile]]
  set RemoteFilename [file split $RemoteFilename]
  set RemoteFilename [join $RemoteFilename "___"]

  return $RemoteFilename
}

proc TriggerSubmission { RemoteFilename } {
  global Dart
  global env

  # load http and determine which version of http we have
  set httpVersion [package require http 2.0]
  puts "\tHTTP package version $httpVersion"

  # convert the trigger timeout to milliseconds
  set triggerTimeOut [expr 1000*$Dart(TriggerTimeOut)]

  # convert the filename to it can be used in a url
  #
  regsub -all "\\+" $RemoteFilename "%2B" urlFilename


  if { $httpVersion > 2.0 } {
    # use standard timeout mechanism

    # attempt to submit without using a proxy server
    set token [::http::geturl $Dart(TriggerSite)?xmlfile=$urlFilename \
         -timeout $triggerTimeOut]

    # if the trigger times out, then attempt to use a proxy server
    if {[::http::status $token] == "timeout"} {
      # geturl timed out.  look for a proxy server
      if {[info exist env(HTTP_PROXY)] && [info exists env(HTTP_PROXY_PORT)]} {
        puts "\tReconfiguring submission for a proxy server."

        ::http::config -proxyhost $env(HTTP_PROXY) \
      -proxyport $env(HTTP_PROXY_PORT)

        # reattempt the trigger

        set token [::http::geturl $Dart(TriggerSite)?xmlfile=$urlFilename \
           -timeout $triggerTimeOut]

        puts "\tTrigger status: [::http::status $token] [::http::code $token]"


      } else {
        puts "\tTrigger failed. If your computer is behind a firewall, make sure that"
        puts "\tyou have set the environment variables HTTP_PROXY and HTTP_PROXY_PORT."
      }
    } else {
      puts "\tTrigger status: [::http::status $token] [::http::code $token]"
    }

  } else {

    puts "\tUsing old trigger timeout mechanism"

    # we have an old version of http (like the one that is distributed
    # with cygwin). hack the timeout handling.

    # attempt to submit without using a proxy server
    catch {[::http::geturl $Dart(TriggerSite)?xmlfile=$urlFilename \
                -timeout $triggerTimeOut] } result

    # if a http socket variable is not returned, then attempt to use a
    # proxy server
    if {[regexp "::http::" $result] == 0} {
      # assume geturl timed out.  look for a proxy server
      if {[info exist env(HTTP_PROXY)] && [info exists env(HTTP_PROXY_PORT)]} {
        puts "\tReconfiguring submission for a proxy server."

        ::http::config -proxyhost $env(HTTP_PROXY) \
      -proxyport $env(HTTP_PROXY_PORT)

        # reattempt the trigger
        set token [::http::geturl $Dart(TriggerSite)?xmlfile=$urlFilename \
                                -timeout $triggerTimeOut]

        # status line commented out because they cause an error
        #puts "\tSecond trigger status: [::http::status $token]  [::http::code $token]"

      } else {
        puts "\tTrigger failed. If your computer is behind a firewall, make sure that"
        puts "\tyou have set the environment variables HTTP_PROXY and HTTP_PROXY_PORT."
      }
    } else {
        # status line commented out because they cause an error
        #puts "\tFirst trigger status: [::http::status $result] [::http::code $result]"
    }
  }
}

proc SubmitFileByFTP { conn DropBoxPrefix XMLFile } {

  set RemoteFilename [BuildRemoteFileName $DropBoxPrefix $XMLFile]

  puts "\tPut [file tail $XMLFile]"
  set putStatus [ftp::Put $conn $XMLFile $RemoteFilename]

  if { $putStatus == 0 } {
    puts "\t   Error sending file $XMLFile to drop box. Retrying."
    set putStatus [ftp::Put $conn $XMLFile $RemoteFilename]

    if { $putStatus == 0 } {
      puts "\t   Error on second attempt to send $XMLFile to drop box."
      puts "\tAborting submission."
      return
    }
  }

  TriggerSubmission $RemoteFilename
}

proc SubmitFileByScp { DropBoxPrefix XMLFile } {
  global Dart
  set RemoteFilename [BuildRemoteFileName $DropBoxPrefix $XMLFile]

  puts "\tPut [file tail $XMLFile]"
    catch {exec $Dart(ScpCommand) $XMLFile [join [list $Dart(DropSiteUser) "@" $Dart(DropSite) ":" $Dart(DropLocation) "/" $RemoteFilename] ""]}

  TriggerSubmission $RemoteFilename
}

proc SubmitFileByCp { DropBoxPrefix XMLFile } {
  global Dart
  set RemoteFilename [BuildRemoteFileName $DropBoxPrefix $XMLFile]
  puts "\tPut [file tail $XMLFile]"

  file copy -force $XMLFile [file join $Dart(DropLocation) $RemoteFilename]

  TriggerSubmission $RemoteFilename
}

proc Submit { Model BuildStampDir } {
  global Dart

  set HTMLDir [file join Testing HTML]
  set TempDir [file join Testing Temporary]

  set SiteDir [file join $HTMLDir TestingResults Sites $Dart(Site)]
  set BuildNameDir [file join $SiteDir $Dart(BuildName)]

  set BuildStamp [file tail $BuildStampDir]
  set XMLDir [file join $BuildStampDir XML]
  set Dart(DropBoxPrefix) [file join $Dart(Site) $Dart(BuildName) $BuildStamp XML]
  set Dart(SecondDropBoxPrefix) [file join $Dart(Site) $Dart(BuildName)]


  if {$Dart(DropMethod) == "ftp"} {
    puts "\tEstablishing connection to drop box."

    # Ftp the results to Dart server
    if {$Dart(DropSiteMode) == {}} {
      set mode "passive"
    } else {
      set mode $Dart(DropSiteMode)
    }
    puts "\t   FTP set to $mode mode"
    #set ftp::DEBUG 1
    #set ftp::VERBOSE 1
    set conn [ftp::Open $Dart(DropSite) $Dart(DropSiteUser) $Dart(DropSitePassword) -mode $mode]
    if { $conn == -1 } {
      # connection failed
      puts "\t   Cannot establish an ftp connection to $Dart(DropSite). Retrying."
      set conn [ftp::Open $Dart(DropSite) $Dart(DropSiteUser) $Dart(DropSitePassword) -mode $mode]
      if { $conn == -1 } {
        puts "\t   Second attempt to establish ftp connection to $Dart(DropSite) failed."
        puts "\tAborting submission."
        return
      }
    }
    ftp::Type $conn ascii
    ftp::Cd $conn $Dart(DropLocation)
  }

  puts "\tBeginning Submission"

  # Put any xml files at the BuildStamp/XML level
  set XMLFiles [glob -nocomplain $XMLDir/*.xml]
  foreach XMLFile $XMLFiles {
    # post each xml file
    #
    if {$Dart(DropMethod) == "ftp"} {
      SubmitFileByFTP $conn $Dart(DropBoxPrefix) $XMLFile
    } elseif {$Dart(DropMethod) == "scp" || $Dart(DropMethod) == "ssh"}  {
      SubmitFileByScp $Dart(DropBoxPrefix) $XMLFile
    } elseif {$Dart(DropMethod) == "cp"}  {
      SubmitFileByCp $Dart(DropBoxPrefix) $XMLFile
    }
  }

  # Put any xml files at the BuildName level
  set XMLFiles [glob -nocomplain $XMLDir/../../*.xml]
  foreach XMLFile $XMLFiles {
    # post each xml file
    #
    if {$Dart(DropMethod) == "ftp"} {
      SubmitFileByFTP $conn $Dart(SecondDropBoxPrefix) $XMLFile
    } elseif {$Dart(DropMethod) == "scp" || $Dart(DropMethod) == "ssh"}  {
      SubmitFileByScp $Dart(SecondDropBoxPrefix) $XMLFile
    }
  }

  # close down any connections
  if {$Dart(DropMethod) == "ftp" } {
    ftp::Close $conn
  }
}
