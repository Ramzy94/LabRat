package com.itrw324.mofokeng.labrat.NonActivityClasses;

/**
 * Created by Mofokeng on 08-Nov-16.
 */

public class Venue {
    private String venueID;
    private String venueName;

    public Venue(String venueID, String venueName) {
        this.venueID = venueID;
        this.venueName = venueName;
    }

    public String getVenueID() {
        return venueID;
    }

    public String getVenueName() {
        return venueName;
    }

}
