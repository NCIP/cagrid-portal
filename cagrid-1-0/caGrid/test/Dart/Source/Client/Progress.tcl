set Progress(Size) 0
set Progress(BS) ""
set Progress(Width) 20

for { set i 0 } { $i < $Progress(Width) } { incr i } {
  append Progress(BS) "\b"
}

set Spinner(\\) "|"
set Spinner(|) "/"
set Spinner(/) "-"
set Spinner(-) "\\"

set Spinner(Symbol) "\\"

set Progress(Time) [clock clicks]

proc Status { {last 0} } {
  global Progress Spinner
  if { $last } {
    puts [format "  Size: %5dK" [expr int($Progress(Size) / 1024.0)]]
    return
  }
  if { $Progress(Count) == 0 } {
    puts "\t. == 1024 bytes of log file"
    puts -nonewline "\t"
    incr Progress(Count)
    return
  }
  puts -nonewline "."
  if { ( $Progress(Count) % 50 ) == 0
     && $Progress(Count) != 0 } {
    puts -nonewline [format "  Size: %5dK\n\t" [expr int($Progress(Size) / 1024.0)]]
  }
  incr Progress(Count)
}


proc ReportProgress { inFile outFile {Verbose 0} } {
  global Progress
  flush stdout
  set Progress(Size) 0
  set Progress(Count) 0
  set dsize 0.0
  if { !$Verbose } {
    Status
  }
  while { ![eof $inFile] } {
    set line [read $inFile 256]
    if { $Verbose } { puts -nonewline stdout $line }
    puts -nonewline $outFile $line
  
    set dsize [expr [string length $line] + $dsize]
    incr Progress(Size) [string length $line]
    set t2 [clock clicks]
    # puts "Time: $time T2: $t2"
    if { !$Verbose
         && ( $Progress(Size) % 1024 ) == 0 } { Status }
  }
  if { !$Verbose } {
    Status 1
  }
}


