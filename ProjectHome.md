Displays tag clouds of various iCalendar properties. Quite primitive right now, but setup in an easily expandable manner.

Future changes could include exporting of the tag clouds, exporting of the tagging data. Perhaps filtering of the input ical feed.

This project uses OpenCloud for Java and ical4j, and is a great way to see potential improvements in each. During the few hours I spent putting this together it was a fight not to go commit on those projects instead ;) For ical4j, perhaps I did not know how to use it properly, but it seems like it is completely terrible at reading icalendar feeds that don't quite match the specifications (perhaps this is intended, "fixing" slightly broken iCal feeds could really be a project in itself!). For OpenCloud, it would be nice if it could return images, and the documentation is slightly lacking (not in quantity, but in quality). I ended up using OC as essentially a utility library, to create a collection for me and then return it, and to break strings into words. It could definitely be made more powerful, and made not limited to using jsp to output clouds

YouTube sample video found at
http://www.youtube.com/watch?v=MBTLCGL--Is