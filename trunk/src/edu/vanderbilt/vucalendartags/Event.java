/**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package edu.vanderbilt.vucalendartags;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Event {

	private String locationName_;
	private Location loc_;
	private String title_;
	private String user_;
	private long start_;
	private long end_;
	private String sourceUid_;
	private String responseType_;
	private String description_;
	private int dbUserId_;
	private List<String> tags_;
	private String monthYear_;

	private String monthToString(int month) {
		month++; // yea...this is a hack. I didnt want to change the cases...
		switch (month) {
		case 1:
			return "Janurary";
		case 2:
			return "Feburary";
		case 3:
			return "March";
		case 4:
			return "April";
		case 5:
			return "May";
		case 6:
			return "June";
		case 7:
			return "July";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "October";
		case 11:
			return "November";
		case 12:
			return "December";
		default:
			return "";
		}
	}

	public Event() {
	}

	public void setTags(List<String> tags) {
		tags_ = tags;
	}

	public List<String> getTags() {
		if (tags_ == null)
			return new ArrayList<String>();
		return tags_;
	}

	public String getDescription() {
		return description_;
	}

	public long getEndTime() {
		return end_;
	}

	public String getMonthYear() {
		return monthYear_;
	}

	public Location getLocation() {
		if (loc_ == null)
			loc_ = new Location();
		return loc_;
	}

	public String getTitle() {
		return title_;
	}

	public String getResponseType() {
		return responseType_;
	}

	public long getStartTime() {
		return start_;
	}

	public String getUser() {
		return user_;
	}

	public void setDescription(String description) {
		description_ = description;
	}

	public void setEndTime(long end) {
		end_ = end;

	}

	public void setLocation(Location loc) {
		loc_ = loc;
	}

	public void setTitle(String title) {
		title_ = title;
	}

	public void setReponseType(String type) {
		responseType_ = type;
	}

	public void setStartTime(long start) {
		while (Long.toString(start).length() < 13)
			start *= 10;
		while (Long.toString(start).length() > 13)
			start /= 10;
		
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(start);

		monthYear_ = monthToString(c.get(Calendar.MONTH))
				+ c.get(Calendar.YEAR);

		start_ = start;
	}

	/**
	 * @param user
	 *            the user_ to set
	 */
	public void setUser(String user) {
		user_ = user;
	}

	public void setDbUserId(int user) {
		dbUserId_ = user;
	}

	public int getDbUserId() {
		return dbUserId_;
	}

	public void setLocationName(String locationName) {
		locationName_ = locationName;
	}

	public String getLocationName() {
		return locationName_;
	}

	public void setSourceUid(String sourceUid) {
		sourceUid_ = sourceUid;
	}

	public String getSourceUid() {
		return sourceUid_;
	}

}
