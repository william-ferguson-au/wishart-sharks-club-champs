package au.com.xandar.swimclub.championships.eligibility;

import java.util.Comparator;

import au.com.xandar.swimclub.championships.EligibilityResult;
import au.com.xandar.swimclub.championships.Event;

/**
 * Orders EligibilityResult into sprints, then distance.
 * 
 * @author william
 */
public final class EligibilityComparator implements Comparator<EligibilityResult> {

	@Override
	public int compare(EligibilityResult o1, EligibilityResult o2) {
		final Event event1 = o1.getEvent();
		final Event event2 = o2.getEvent();
		final int eventTypeComparison = event1.getEventType().compareTo(event2.getEventType());
		if (eventTypeComparison != 0) {
			return eventTypeComparison;
		}
		
		// Otherwise look at Stroke.
		return event1.getStroke().compareTo(event2.getStroke());
	}

}
