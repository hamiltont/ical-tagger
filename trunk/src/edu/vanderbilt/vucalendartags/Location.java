package edu.vanderbilt.vucalendartags;

public class Location {

	private int locationid_;
	private String name_;
	private double lat_;
	private double lon_;

	public Location() {

	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Location) {
			Location otherLoc = (Location) o;
			if ((otherLoc.getLat() != getLat())
					|| (otherLoc.getLon() != getLon())
					|| (otherLoc.getName().equalsIgnoreCase(getName()) == false)
					|| (otherLoc.getLocationid() != getLocationid()))
				return false;
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int)(getLat() * getLon()) + locationid_;
	}

	public Location(double lat, double lon) {
		lat_ = lat;
		lon_ = lon;
	}

	public Location(String name, double lat, double lon) {
		name_ = name;
		lat_ = lat;
		lon_ = lon;
	}

	public Location(int locationid, String name, double lat, double lon) {
		locationid_ = locationid;
		name_ = name;
		lat_ = lat;
		lon_ = lon;
	}

	public int getLocationid() {
		return locationid_;
	}

	public String getName() {
		return name_;
	}
	
	@Override
	public String toString() {
		if ((lat_ == 0) || (lon_ == 0))
			return "";
		
		StringBuffer sb = new StringBuffer();
		sb.append(lat_);
		sb.append(" ");
		sb.append(lon_);
		return sb.toString();
	}

	public double getLat() {
		return lat_;
	}

	public double getLon() {
		return lon_;
	}

	public void setName(String name) {
		name_ = name;
	}

	public void setLat(double lat) {
		lat_ = lat;
	}

	public void setLon(double lon) {
		lon_ = lon;
	}

}
