# Source all of the files here that are .tcl, and not DashboardManager.tcl

set DoNotSource [list DashboardManager.tcl Index.tcl Cat.tcl]
set dir [file dir [info script]]

foreach file [glob -nocomplain [file join $dir *.tcl]] {
  
  if { [lsearch $DoNotSource [file tail $file]] == -1 } {
    source $file
  }
}
