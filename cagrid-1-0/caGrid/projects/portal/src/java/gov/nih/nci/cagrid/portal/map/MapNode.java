package gov.nih.nci.cagrid.portal.map;

import gov.nih.nci.cagrid.portal.common.GeoCodeValues;

import java.util.List;

/**
 * Representa a node on the Map.
 * <p/>
 * If you wish to use the Portal Map in the UI
 * create a java.util.Collection of these MapNode objects
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 23, 2006
 * Time: 2:19:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapNode {

    // Title of the node
    private String title;
    //Geocoding information (will not display if its null)
    private GeoCodeValues geoValues;

    //A List of the description text (comes after title)
    private List displayText;

    public MapNode(String title, GeoCodeValues geoValues, List displayText) throws InvalidMapNodeException {
        /**
         * Check if there is complete geocoding information to render on a Map
         */
        if (geoValues == null || geoValues.getLatitude() == null || geoValues.getLatitude() == null) {
            throw new InvalidMapNodeException("Insufficient of Null Geocoding information in Map Node");
        }

        this.title = title;
        this.geoValues = geoValues;
        this.displayText = displayText;
    }

    public String getTitle() {
        return title;
    }

    public GeoCodeValues getGeoValues() {
        return geoValues;
    }

    public List getDisplayText() {
        return displayText;
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapNode mapNode = (MapNode) o;

        return geoValues.equals(mapNode.geoValues);

    }

    public int hashCode() {
        return geoValues.hashCode();
    }
}
