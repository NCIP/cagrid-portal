package gov.nih.nci.cagrid.portal.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a caBIG workspace
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 16, 2006
 * Time: 4:48:28 PM
 * To change this template use File | Settings | File Templates.
 *
 * @hibernate.class table="CABIG_WORKSPACES"
 */
public class CaBIGWorkspace implements DomainObject {

    private Integer pk;
    private String shortName;
    private String longName;
    private String description;
    private Set participants = new HashSet();


    public CaBIGWorkspace() {
    }

    /**
     * @hibernate.id generator-class="increment"
     * column="ID_KEY"
     */
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }


    /**
     * @hibernate.property column="SHORT_NAME"
     */
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * @hibernate.property column="LONG_NAME"
     */
    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * @hibernate.property column="DESCRIPTION"
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @hibernate.set name="participants"
     * table="CABIG_WORKSPACES_PARTICIPANTS_JOIN"
     * cascade="all"
     * lazy="false"
     * @hibernate.collection-key column="CABIG_WORKSPACES_ID_KEY"
     * @hibernate.collection-many-to-many column="CABIG_PARTICIPANTS_ID_KEY"
     * class="gov.nih.nci.cagrid.portal.domain.CaBIGParticipant"
     */
    public Set getParticipants() {
        return participants;
    }

    private void setParticipants(Set participants) {
        this.participants = participants;
    }


    /**
     * adds a Participant to the workspace
     *
     * @param participant
     */
    public void addParticipant(CaBIGParticipant participant) {
        this.participants.add(participant);
        participant.getWorkspaceCollection().add(this);
    }

    /**
     * removes a given participant from the workspace
     *
     * @param participant
     */
    public void removeParticipant(CaBIGParticipant participant) {
        this.participants.remove(participant);
        participant.getWorkspaceCollection().remove(this);
    }

    /**
     * convinience method for removing
     * all associated participants
     */
    public void removeAllParticipants() {
        this.participants.clear();
    }
}

