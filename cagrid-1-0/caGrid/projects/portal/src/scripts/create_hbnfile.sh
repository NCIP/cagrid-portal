// This file contains a BeanShell script that
// works together with the Ant project file.
// This script searches for hibernate mapping
// files and writes the filenames out to a
// hibernate configuration file.

// Read properties configured in the Ant
// project file.
//
// This property tells us where to look for
// hibernate mapping files.
String basedirName = project.getProperty("generated.dir");
print("Search for hibernate files in " + basedirName);

// This property the filename to use when writing
// out the hibernate file list.
String outFilename = project.getProperty("hibernate.list.file");
print("Output to " + outFilename);

/**********************************************/

/*
 * Write a line to the configuration file.
 */
void dumpFilename(String s) {
  out.println("<mapping resource=\"" + s + "\"/>");
}

/**********************************************/
/*
 * Combine the relative paths together to
 * form a complete path.  If either is empty,
 * then there is no separator since they are
 * not really being combined.
 */
String relativeName(String base, String name) {
  if (base.length() == 0 || name.length() == 0)
    return base + name;
  else
    return base + "/" + name;
}

/**********************************************/
/*
 * This method reads the directory specified
 * and looks for file names to output to the
 * configuration file.  It invokes itself
 * recursively to find all files.
 */
void readDir(String relativePath) {
  // Combine relativePath with basedirName
  // to get get full path
  String fullName = relativeName(basedirName, relativePath);
  File dir = new File(fullName);

  // Get a listing of all directories
  // and all files ending in .hbm.xml
  //
  String[] list = dir.list(new FilenameFilter() {
    boolean accept(File dir, String name) {
      File f = new File(dir, name);
      if (f.isDirectory())
        return true;
      else if (f.isFile() && name.endsWith(".hbm.xml"))
    return true;
      else
        return false;
    }
  });

  count = 0;
  for (i=0; i<list.length; i++) {
    f = new File(fullName + "/" + list[i]);
    if (f.isFile()) {
      dumpFilename(relativeName(relativePath, f.getName()));
      count++;
    } else if (f.isDirectory()) {
      readDir(relativeName(relativePath, f.getName()));
    }
  }

  // Print out summary of what we found
  if (count > 0) {
    print("readDir: " + relativePath);
    print("Found Files: " + count);
  }
}

/**********************************************/
//                  MAIN

// Keep a backup copy of the current file.
mv("NoSuchFile", "hereNow");
mv(outFilename, outFilename+".bak");

File configFile = new File(outFilename);
PrintWriter out = new PrintWriter(new FileWriter(configFile));

// Kick things off by passing in an empty String.
//
readDir("");

// Close the config file we just created.
out.close();