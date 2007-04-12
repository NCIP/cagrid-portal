package gov.nih.nci.cagrid.introduce.upgrade.common;

import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase.Issue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class UpgradeStatus {

    List introduceUpgradesStatus;


    public UpgradeStatus() {
        introduceUpgradesStatus = new ArrayList();
    }


    public void addIntroduceUpgradeStatus(IntroduceUpgradeStatus introduceUpgradeStatus) {
        this.introduceUpgradesStatus.add(introduceUpgradeStatus);
    }


    public List getIntroduceUpgradesStatus() {
        return this.introduceUpgradesStatus;
    }
    
    public String toString(){
        StringBuffer sb = new StringBuffer();
        Iterator iStatusI = introduceUpgradesStatus.iterator();
        while(iStatusI.hasNext()){
            IntroduceUpgradeStatus iStatus = (IntroduceUpgradeStatus)iStatusI.next();
            sb.append("Status: " + iStatus.getStatus() + "\n\n");
            sb.append("Description: \n" + iStatus.getDescription() + "\n");
            sb.append("Issues: \n" + iStatus.getDescription());
            List issues = iStatus.getIssues();
            Iterator issuesI = issues.iterator();
            while(issuesI.hasNext()){
                Issue issue = (Issue)issuesI.next();
                sb.append("\tIssue: " + issue.getIssue() + "\n");
                sb.append("\tResolution: " + issue.getResolution() + "\n\n");
            }
            
            Iterator eStatusI = iStatus.getExtensionUgradesStatus().iterator();
            while(eStatusI.hasNext()){
                ExtensionUpgradeStatus eStatus = (ExtensionUpgradeStatus)eStatusI.next();
                sb.append("Status: " + eStatus.getStatus() + "\n\n");
                sb.append("Description: \n" + eStatus.getDescription() + "\n");
                sb.append("Issues: \n" + eStatus.getDescription());
                issues = eStatus.getIssues();
                issuesI = issues.iterator();
                while(issuesI.hasNext()){
                    Issue issue = (Issue)issuesI.next();
                    sb.append("\tIssue: " + issue.getIssue() + "\n");
                    sb.append("\tResolution: " + issue.getResolution() + "\n\n");
                }
                sb.append("\n\n");
            
            }
        }
        
        
        return sb.toString();
    }

}
