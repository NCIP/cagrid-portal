package gov.nih.nci.cagrid.introduce.upgrade.common;

import java.util.ArrayList;
import java.util.List;

    
public class StatusBase {
    public class Issue {
        private String issue;
        private String resolution;
        
        public Issue(String issue, String resolution){
            this.issue = issue;
            this.resolution = resolution;
        }

        public String getIssue() {
            return issue;
        }

        public void setIssue(String issue) {
            this.issue = issue;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }
    }
    public static final String UPGRADE_OK = "Upgrade OK";
    public static final String UPGRADE_FAIL = "Upgrade FAILED";
    public static final String UPGRADE_NOT_AVAILABLE = "Upgrade Not Available";
    
    private String status = null;
    private String description = "";
    private List issues;
    public StatusBase(String status){
        this.status = status;
        this.issues = new ArrayList();
    }
    
    public StatusBase(){
        this.issues = new ArrayList();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void addDescriptionLine(String description) {
        this.description += description + "\n";
    }
    
    public void addIssue(String issue, String resolution){
        this.issues.add(new Issue(issue,resolution));
    }
    
    public List getIssues(){
        return this.issues;
    }
    
    
}
