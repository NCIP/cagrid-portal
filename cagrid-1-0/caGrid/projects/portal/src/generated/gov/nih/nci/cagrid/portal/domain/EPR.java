package gov.nih.nci.cagrid.portal.domain;

import java.io.Serializable;

/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 */
public class EPR implements Serializable {

    private String eprString;

    public EPR(String eprString) {
        this.eprString = eprString;
    }

    public String getEprString() {
        return eprString;
    }

    public void setEprString(String eprString) {
        this.eprString = eprString;
    }


    public boolean equals(EPR epr) {
        if (epr.getEprString().equals(this.getEprString())) {
            return true;
        }
        return false;
    }

}