/**
 * 
 */
package edu.vanderbilt.vucalendartags;

import java.util.Collection;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import org.mcavallo.opencloud.filters.Filter;

/**
 * @author Hamilton Turner
 * 
 */
public class WorkerReply {

	public Cloud eventTags_ = new Cloud();
	public Cloud descriptions_ = new Cloud();
	public Cloud monthYear_ = new Cloud();
	public Cloud submitter_ = new Cloud();
	public Cloud locationName_ = new Cloud();
	public Cloud titles_ = new Cloud();
	public Cloud geoLocation_ = new Cloud();

	private static final String[] blacklist = { "a", "an", "the", "www",
			"vanderbilt", "to", "com", "edu", "of", "or", "and", "for", "is",
			"in", "this", "will", "with", "http", "mailto", "your", "615-322" };

	public WorkerReply() {
		descriptions_.addInputFilter(defaultFilter);
		locationName_.addInputFilter(defaultFilter);
		titles_.addInputFilter(defaultFilter);
	}
	
	public static final Filter<Tag> defaultFilter = new Filter<Tag>() {
		private static final long serialVersionUID = 1L;

		public void filter(Collection<Tag> coll) {
		}

		public boolean accept(Tag e) {
			String name = e.getName();

			if (name.length() < 4)
				return false;

			for (String s : blacklist)
				if (s.equalsIgnoreCase(name))
					return false;

			return true;
		}
	};

	
}
