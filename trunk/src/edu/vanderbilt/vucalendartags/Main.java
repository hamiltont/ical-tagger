package edu.vanderbilt.vucalendartags;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

public class Main {

	private String iCalFeed = "http://calendar.vanderbilt.edu/calendar/ics/set/500/vu-calendar.ics";
	private ICalLoader loader;
	public JPanel filter;
	public JPanel monthYear;
	public List<Event> events_;
	private JPanel description;
	private JPanel submitter;
	private JPanel title;
	private JPanel location;
	private Container geoLocation;

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Make sure we have nice window decorations.
				JFrame.setDefaultLookAndFeelDecorated(true);

				// Create and set up the window.
				new Main();

			}
		});

	}

	private JFrame frame;

	public Main() {
		frame = new JFrame("iCalendar Tag Cloud Generator");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		loader = new ICalLoader(iCalFeed, this);
		loader.execute();

		// Make the big window be indented 50 pixels from each edge
		// of the screen.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(inset, inset, screenSize.width - inset * 2,
				screenSize.height - inset * 2);

		JMenuBar menu = new JMenuBar();

		JMenuItem load = new JMenuItem("Load");
		load.setActionCommand("load");
		load.setMnemonic(KeyEvent.VK_N);
		JCheckBoxMenuItem geoCode = new JCheckBoxMenuItem("GeoCode");
		geoCode.setActionCommand("geo");

		JCheckBoxMenuItem nash = new JCheckBoxMenuItem("Add Nashville");
		nash.setActionCommand("nash");
		nash.setSelected(true);
		
		JMenu main = new JMenu("Main");
		main.setMnemonic(KeyEvent.VK_N);
		main.add(load);
		load.addActionListener(defaultActionListener);
		main.add(geoCode);
		geoCode.addActionListener(defaultActionListener);
		main.add(nash);
		nash.addActionListener(defaultActionListener);
		menu.add(main);

		frame.setJMenuBar(menu);

		addComponentsToPane(frame.getContentPane());

		// Display the window.
		frame.setVisible(true);

	}

	public static AtomicBoolean geoCode = new AtomicBoolean(false);
	
	public static AtomicBoolean addNash = new AtomicBoolean(true);

	private ActionListener defaultActionListener = new ActionListener() {

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equalsIgnoreCase("load")) {
				String s = (String) JOptionPane.showInputDialog(frame,
						"Enter an iCalendar URL", "Load new iCalendar Feed",
						JOptionPane.PLAIN_MESSAGE, null, null, "");

				if ((s != null) && (s.length() > 0))
					resetUI(s);

			} else if (e.getActionCommand().equalsIgnoreCase("geo")) {
				if (false == geoCode.compareAndSet(true, false))
					geoCode.compareAndSet(false, true);
			} else if (e.getActionCommand().equalsIgnoreCase("nash")) {
				if (false == addNash.compareAndSet(true, false))
					addNash.compareAndSet(false, true);
			}
		}

	};

	private void resetUI(String newURL) {
		loader.cancel(true);

		loader = new ICalLoader(newURL, this);

		filter.removeAll();
		filter.add(new JLabel("Loading tags from iCal"));
		description.removeAll();
		location.removeAll();
		monthYear.removeAll();
		submitter.removeAll();
		title.removeAll();
		geoLocation.removeAll();

		loader.execute();
	}

	private void addComponentsToPane(Container pane) {

		JTabbedPane tabbedPane = new JTabbedPane();
		pane.add(tabbedPane);

		filter = new JPanel();
		filter.add(new JLabel("Loading tags from iCal"));
		tabbedPane.addTab("Tags", filter);

		monthYear = new JPanel();
		tabbedPane.addTab("Month Year", monthYear);

		submitter = new JPanel();
		tabbedPane.addTab("Submitters", submitter);

		description = new JPanel();
		tabbedPane.addTab("Description", description);

		title = new JPanel();
		tabbedPane.addTab("Title", title);

		location = new JPanel();
		tabbedPane.addTab("Location", location);

		geoLocation = new JPanel();
		tabbedPane.addTab("GeoLocation", geoLocation);
	}

	public void newReply(WorkerReply answer) {
		if (answer == null)
			return; // some exception occurred
		
		updateFilter(answer.eventTags_, filter);
		updateFilter(answer.descriptions_, description);
		updateFilter(answer.locationName_, location);
		updateFilter(answer.monthYear_, monthYear);
		updateFilter(answer.submitter_, submitter);
		updateFilter(answer.titles_, title);
		updateFilter(answer.geoLocation_, geoLocation);

	}

	public void newMessage(LoaderState loaderState) {
		filter.removeAll();
		filter.add(new JLabel(loaderState.getMessage()));
		filter.revalidate();
		filter.repaint();
	}
	
	private void updateFilter(Cloud cloud, Container container) {
		StringBuffer string = new StringBuffer("<html><center>");
		for (Tag t : cloud.tags()) {
			string.append("<a style='font-size: ");
			string.append(t.getWeightInt() * 10);
			string.append("px'>");
			string.append(t.getName());
			string.append("</a> ");
		}
		string.append("</center></html>");

		container.removeAll();

		JLabel label = new JLabel(string.toString());
		label.setPreferredSize(new Dimension(container.getWidth() - 20, label
				.getPreferredSize().height));

		JScrollPane pane = new JScrollPane(label);
		pane.setPreferredSize(container.getSize());

		container.add(pane);

		container.getParent().validate();
		container.getParent().repaint();

		container.validate();
		container.repaint();
	}

	/** Provide a Universal ID for serialization */
	private static final long serialVersionUID = -1261510591458713599L;


}
