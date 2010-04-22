/**
 * 
 */
package edu.vanderbilt.vucalendartags;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;

import org.mcavallo.opencloud.Cloud;

import edu.vanderbilt.vucalendartags.LoaderState.State;

/**
 * @author Hamilton Turner
 * 
 */
public class ICalLoader extends SwingWorker<WorkerReply, LoaderState> {
	private String url_;
	private Main main_;

	public ICalLoader(String url, Main main) {
		url_ = url;
		main_ = main;
	}

	@Override
	protected WorkerReply doInBackground() throws Exception {

		publish(new LoaderState(State.Started, 0, 0));

		// First, read the file in, removing all instances of "US-Central:" and
		// "LAST-MODIFIED;"
		BufferedReader reader = null;
		URL url = null;
		try {
			if (url_ == null || url_.equalsIgnoreCase(""))
				return null;

			url_ = url_.replaceAll(" ", "%20");
			url = (URI.create(url_)).toURL();
			reader = new BufferedReader(new InputStreamReader(url.openStream(),
					"UTF-8"));

			FileOutputStream fout = new FileOutputStream("calendar.ics");
			OutputStreamWriter out = new OutputStreamWriter(fout, "UTF-8");
			String s;
			while ((s = reader.readLine()) != null) {
				out.write(s + "\n");
			}
			out.close();
		} catch (MalformedURLException e1) {
			System.err.println("ICS calendar URL malformed");
			e1.printStackTrace();
			publish(new LoaderState(State.Error, 0, 0));
			return null;
		} catch (IOException e1) {
			System.err.println("Couldn't load ICS calendar");
			e1.printStackTrace();
			publish(new LoaderState(State.Error, 0, 0));
			return null;
		}

		publish(new LoaderState(State.Downloaded, 0, 0));

		// Write it back out to a temp file
		try {
			reader = new BufferedReader(new FileReader("calendar.ics"));

			FileOutputStream fout = new FileOutputStream("calendar-temp.ics");
			OutputStreamWriter out = new OutputStreamWriter(fout, "UTF-8");

			String s;
			while ((s = reader.readLine()) != null) {
				s = s.replaceFirst(";TZID=US-Central", "") + "\n";
				out.write(s);
			}
			out.close();
		} catch (IOException e1) {
			publish(new LoaderState(State.Error, 0, 0));
			e1.printStackTrace();
			return null;
		}

		// Read in the iCal file from the preprocessed temp file
		InputStreamReader fin = null;
		try {
			fin = new InputStreamReader(
					new FileInputStream("calendar-temp.ics"), "UTF-8");
		} catch (FileNotFoundException e) {
			System.err
					.println("Could not read preprocessed input file vu-calendar-temp.ics");
			e.printStackTrace();
			publish(new LoaderState(State.Error, 0, 0));
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			publish(new LoaderState(State.Error, 0, 0));
			return null;
		}

		publish(new LoaderState(State.Fixed, 0, 0));

		// Now build the calendar
		CalendarBuilder builder = new CalendarBuilder();
		Calendar calendar = null;
		try {
			calendar = builder.build(fin);
		} catch (IOException e) {
			publish(new LoaderState(State.Error, 0, 0));
			e.printStackTrace();
			return null;
		} finally {
			try {
				fin.close();
			} catch (IOException e) {
				publish(new LoaderState(State.Error, 0, 0));
				e.printStackTrace();
				return null;
			}
		}

		publish(new LoaderState(State.Parsed, 0, 0));

		WorkerReply wr = new WorkerReply();

		Cloud tags = wr.eventTags_;
		Cloud descriptions = wr.descriptions_;
		Cloud monthYear = wr.monthYear_;
		Cloud title = wr.titles_;
		Cloud locNames = wr.locationName_;
		Cloud submitter = wr.submitter_;
		Cloud geoLocs = wr.geoLocation_;

		// Get all of the events from the calendar
		ComponentList events = calendar.getComponents(Component.VEVENT);
		int i = 0, size = events.size();
		for (Object c : events) {

			Event e = EventBuilder.build((Component) c);

			for (String s : e.getTags())
				tags.addTag(s);

			descriptions.addText(e.getDescription());
			monthYear.addTag(e.getMonthYear());
			title.addText(e.getTitle());
			locNames.addText(e.getLocationName());
			submitter.addText(e.getSourceUid());
			geoLocs.addText(e.getLocation().toString());

			publish(new LoaderState(State.Working, i++, size));
		}

		return wr;
	}

	@Override
	protected void process(List<LoaderState> states) {
		if (states.size() > 0)
			main_.newMessage(states.get(0));
	}

	@Override
	public void done() {
		try {
			WorkerReply answer = get();
			main_.newReply(answer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
