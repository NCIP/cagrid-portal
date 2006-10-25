if (GBrowserIsCompatible()) {
    var baseIcon = new GIcon();
    baseIcon.image = "http://labs.google.com/ridefinder/images/mm_20_red.png";
    baseIcon.shadow = "http://labs.google.com/ridefinder/images/mm_20_shadow.png";
    baseIcon.iconSize = new GSize(20, 34);
    baseIcon.shadowSize = new GSize(37, 34);
    baseIcon.iconAnchor = new GPoint(9, 34);

    var rcIcon = new GIcon(baseIcon);
    rcIcon.image = "http://www.google.com/mapfiles/markerR.png";


    var asIcon = new GIcon(baseIcon);
    asIcon.image = "http://www.google.com/mapfiles/markerA.png";

    var dsIcon = new GIcon(baseIcon);
    dsIcon.image = "http://www.google.com/mapfiles/markerD.png";
}

function createMarker(point, line1, line2) {
    var marker = new GMarker(point);
    GEvent.addListener(marker, "click", function() {
        marker.openInfoWindowHtml("<b>" + line1 + "</b><br>" + line2);
    });
    return marker;
}
