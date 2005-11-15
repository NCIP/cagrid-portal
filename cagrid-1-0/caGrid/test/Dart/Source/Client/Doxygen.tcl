# =========================================================================
# 
#   Program:   Insight Segmentation & Registration Toolkit
#   Module:    $RCSfile: Doxygen.tcl,v $
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


# Put in this directory
# Year Month Day - TimeZone - Hour:Minute
# e.g. 20000926-EDT-16:02
# or Year Month Day Hour Minute
# clock format [clock seconds] -format %Y%m%d%H%M

# Must be run from root directory of project build, only by make

proc Doxygen { Model OutFile } {
  global Dart DashboardDir
  
  set SourceDirectory [pwd]
  set UtilityDir [file join $Dart(DartRoot) Source Client]

  # If this project/build is not currently setup to use doxygen, then exit.
  # This configured by Utility.tcl
  if { $Dart(BuildDoxygen) != "ON" } {
    return
  }


  # Begin the XML output
  set Out [open $OutFile w]

  puts $Out $Dart(XMLHeader)
  puts $Out {<Doxygen>}
  puts $Out "\t<StartDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</StartDateTime>"


  set Log Doxygen.log

  # Do the build
  set DoxygenStatus [catch { eval exec $Dart(DoxygenCommand) $Dart(DoxygenConfig) 2> $Log } Result ]

  set Doxygen [open $Log r]

  set Cache ""
  catch { unset ErrorList }
  set ErrorList(Counter) 0
  set ErrorList(Append) ""
  set ErrorList(Buffer) ""
  set PostContextCount 5
  set BufferLength 5

  set Exceptions ""
  set Line 1
  while { ![eof $Doxygen] } \
  {
    # Get a line and write it out
    set LogLine [gets $Doxygen]

    set hasError [Match {.*[Ee][Rr][Rr][Oo][Rr]:.*} $LogLine $Exceptions]
    set hasWarning [Match {.*[Ww][Aa][Rr][Nn][Ii][Nn][Gg]:.*} $LogLine $Exceptions]

    if { $hasError } \
    {
      set Type Error
    }
    if { $hasWarning } \
    {
      set Type Warning
    }
    if { $hasError || $hasWarning } \
    {
      set i $ErrorList(Counter)
      incr ErrorList(Counter)
      set ErrorList($i,Type) $Type
      set ErrorList($i,Text) $LogLine
      set ErrorList($i,Line) $Line
      set ErrorList($i,PreContext) $ErrorList(Buffer)
      set ErrorList($i,PostContext) ""
      set ErrorList($i,ContextCounter) 0
      set ErrorList($i,SourceFile) ""
      set ErrorList($i,SourceLineNumber) ""

      # Take a stab at getting a file name out of the line
      # For GCC, we get /path/to/source.cxx:##
      if { [string match "*$SourceDirectory*:*" $LogLine] } \
      {
        # GCC
        if { [regexp "${SourceDirectory}/(\[^:\]*):(\[0-9\]*)" $LogLine dummy SourceFile LineNumber] } \
        {
          set ErrorList($i,SourceFile) $SourceFile
          set ErrorList($i,SourceLineNumber) $LineNumber
        }
      }
      
      lappend ErrorList(Append) $i
    }

    # Add to the PreContextBuffer
    set l [llength $ErrorList(Buffer)]
    if { $l > $BufferLength } \
    {
      set ErrorList(Buffer) [lrange $ErrorList(Buffer) 1 [expr $l - 2]]
    }
    lappend ErrorList(Buffer) $LogLine

    # Go through all in the ErrorList to append to, and do it
    set AppendList $ErrorList(Append)
    foreach i $AppendList \
    {
      if { $ErrorList($i,Line) != $Line } \
      {
        lappend ErrorList($i,PostContext) $LogLine
        incr ErrorList($i,ContextCounter) 

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
  for { set i 0 } { $i < $ErrorList(Counter) } { incr i } \
  {
    puts $Out "\t<$ErrorList($i,Type)>"
    puts $Out "\t\t<LogLine>[XMLSafeString $ErrorList($i,Line)]</LogLine>"
    puts $Out "\t\t<Text>[XMLSafeString $ErrorList($i,Text)]</Text>"
    if { $ErrorList($i,SourceFile) != "" } \
    {
      puts $Out "\t\t<SourceFile>[XMLSafeString $ErrorList($i,SourceFile)]</SourceFile>"
      puts $Out "\t\t<SourceLineNumber>$ErrorList($i,SourceLineNumber)</SourceLineNumber>"
    }
    puts $Out "\t\t<PreContext>"
    foreach l $ErrorList($i,PreContext) \
    {
      puts $Out [XMLSafeString $l]
    }
    puts $Out "\t\t</PreContext>"
    puts $Out "\t\t<PostContext>"
    foreach l $ErrorList($i,PostContext) \
    {
      puts $Out [XMLSafeString $l]
    }
    puts $Out "\t\t</PostContext>"
    puts $Out "</$ErrorList($i,Type)>"
    puts $Out "\n"
  }

  # set LogCompressionStatus [ catch { CompressFile $DoxygenLogTempFilename $DoxygenLogCompressedFilename } CompressionResult ]
  # if { $LogCompressionStatus } \
  # {
  #   error $CompressionResult
  # }
  # set DoxygenLog [open $DoxygenLogCompressedFilename r]
  # fconfigure $DoxygenLog -translation binary

  # puts $Out "\t<Log Encoding=\"base64\" Compression=\"$CompressionType\">"
  # puts $Out [Base64Encode [read $DoxygenLog ]]
  # puts $Out "\t</Log>"

  puts $Out "\t<EndDateTime>[AbbreviateTimeZone [clock format [clock seconds]]]</EndDateTime>"

  puts $Out "</Doxygen>"

  close $Doxygen
  close $Out

  file delete $Log
  return
}
