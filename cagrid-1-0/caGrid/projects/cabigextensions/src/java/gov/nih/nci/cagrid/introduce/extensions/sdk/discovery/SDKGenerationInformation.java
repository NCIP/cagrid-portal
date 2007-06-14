package gov.nih.nci.cagrid.introduce.extensions.sdk.discovery;



/**
 * @author oster
 */
public class SDKGenerationInformation {

    private String caDSRcontext;
    private String caDSRProjectName;
    private String caDSRProjectVersion;

    private String xmiFile;
    private String packageIncludes;
    private String packageExcludes;


    public SDKGenerationInformation() {
        super();
    }


    /**
     * @param context
     * @param name
     * @param version
     * @param excludes
     * @param includes
     * @param file
     */
    public SDKGenerationInformation(String context, String name, String version, String excludes, String includes,
        String file) {
        super();

        this.caDSRcontext = context;
        this.caDSRProjectName = name;
        this.caDSRProjectVersion = version;
        this.packageExcludes = excludes;
        this.packageIncludes = includes;
        this.xmiFile = file;
    }


    public String getCaDSRcontext() {
        return this.caDSRcontext;
    }


    public void setCaDSRcontext(String caDSRcontext) {
        this.caDSRcontext = caDSRcontext;
    }


    public String getCaDSRProjectName() {
        return this.caDSRProjectName;
    }


    public void setCaDSRProjectName(String caDSRProjectName) {
        this.caDSRProjectName = caDSRProjectName;
    }


    public String getCaDSRProjectVersion() {
        return this.caDSRProjectVersion;
    }


    public void setCaDSRProjectVersion(String caDSRProjectVersion) {
        this.caDSRProjectVersion = caDSRProjectVersion;
    }


    public String getPackageExcludes() {
        return this.packageExcludes;
    }


    public void setPackageExcludes(String packageExcludes) {
        this.packageExcludes = packageExcludes;
    }


    public String getPackageIncludes() {
        return this.packageIncludes;
    }


    public void setPackageIncludes(String packageIncludes) {
        this.packageIncludes = packageIncludes;
    }


    public String getXmiFile() {
        return this.xmiFile;
    }


    public void setXmiFile(String xmiFile) {
        this.xmiFile = xmiFile;
    }


    @Override
    public String toString() {
        return "Generation will read file: " + this.xmiFile + "\n\tincluding packages: " + this.packageIncludes
            + "\n\texcluding packages: " + this.packageExcludes + "\nrepresenting Project: " + this.caDSRProjectName
            + "\n\tversion: " + this.caDSRProjectVersion + "\n\tin context:" + this.caDSRcontext;
    }
}
