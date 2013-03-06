package au.com.xandar.swimclub.championships.program;

import java.util.Comparator;

/**
 * Sorts the events to the standard Championship order of:
 * <ol>
 * 	<li>100m Breastroke</li>
 * 	<li>25m and 50m Breastroke sprints</li>
 * 	<li>100 Backstroke</li>
 * 	<li>25m and 50m Backstroke sprints</li>
 * 	<li>100 Medley</li>
 * 	<li>25m and 50m Butterfly sprints</li>
 * 	<li>100m Freestyle</li>
 * 	<li>25m and 50m Freestyle sprints</li>
 * </ol> 
 * and then by age group.
 * 
 * @author william
 *
 */
public final class ProgramEventComparator implements Comparator<ProgramEvent> {

	@Override
	public int compare(ProgramEvent o1, ProgramEvent o2) {
		
		final int strokeComparison = o1.getStroke().compareTo(o2.getStroke());
		if (strokeComparison != 0 ) {
			return strokeComparison;
		}

        // The compare on EventType, ie distance event then sprint events.
        final int eventTypeComparison = o1.getEvent().getEventType().compareTo(o2.getEvent().getEventType());
        if (eventTypeComparison != 0) {
            return eventTypeComparison;
        }

		final int distanceComparison = o1.getDistance().compareTo(o2.getDistance());
		if (distanceComparison != 0) {
			return distanceComparison;
		}
		
		final int ageComparison = o1.getAgeGroup().compareTo(o2.getAgeGroup());
        if (ageComparison != 0) {
            return ageComparison;
        }

        return o1.getGender().compareTo(o2.getGender());
	}
}
