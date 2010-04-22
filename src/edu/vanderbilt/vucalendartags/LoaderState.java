/**
 * 
 */
package edu.vanderbilt.vucalendartags;

/**
 * @author Hamilton Turner
 * 
 */
public class LoaderState {

	enum State {
		Started, Downloaded, Parsed, Fixed, Working, Error
	};

	public State state_;
	public int current_;
	public int total_;

	public LoaderState(State s, int current, int total) {
		state_ = s;
		current_ = current;
		total_ = total;
	}
	
	public String getMessage() {
		switch (state_) {
		case Started:
			return "Downloading ICS File";
		case Downloaded:
			return "Attempting to correct for common ICS file errors";
		case Fixed:
			return "Parsing ICS File";
		case Parsed:
			return "Creating Tag Clouds";
		case Working: {
			StringBuffer sb = new StringBuffer("Creating Tag Cloud - Element ");
			sb.append(current_);
			sb.append(" of ");
			sb.append(total_);
			return sb.toString();
		}
		case Error:
			return "Some error occurred in loading the iCal file";
		default:
			return "";

		}
	}
}
