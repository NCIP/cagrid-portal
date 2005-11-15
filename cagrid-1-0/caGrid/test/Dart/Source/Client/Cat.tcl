# Emulate the "cat" unix command

while { ![eof stdin] } {
  puts [gets stdin]
}
