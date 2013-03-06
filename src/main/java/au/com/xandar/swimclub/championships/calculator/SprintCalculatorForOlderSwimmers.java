package au.com.xandar.swimclub.championships.calculator;

import java.util.Date;

import au.com.xandar.swimclub.championships.*;

/**
 * Determines ElilgibilityResults for sprint events for older swimmers who will typically swim 50m events at Championships.
 * <p>
 * If they have a 25m PB which is less than the eligibility time for 50m, then they will swim 50m at championships.
 * Otherwise they will swim 25m.
 * </p>
 * <p>
 * Age for older swimmers is 10 and over for Backstroke, Breatstroke and Freestyle and 12 and over for Butterfly.
 * </p>
 * 
 * @author william
 *
 */
public final class SprintCalculatorForOlderSwimmers implements EligibilityCalculator {

	private final Season season;
	
	SprintCalculatorForOlderSwimmers(Season season) {
		this.season = season;
	}
	
	/**
	 * Returns EligibilityResult for the sprint event for the Athlete for the given stroke.
	 */
	public EligibilityResult caculateEligibility(Athlete athlete, Stroke stroke) {
		
        final SprintDistance sprintDistance = stroke.getSprintDistanceForChampionships(athlete);
        final Event eligibleEvent = sprintDistance.getEvent();

		final int raceCount25m = athlete.getRaceCount(new Event(stroke, 25), this.season);
		final int raceCount50m = athlete.getRaceCount(new Event(stroke, 50), this.season);
		
		// Determine required number of swims.
		final int numberOfRequiredSwims = athlete.getNumberOfRequiredSwims(stroke, EventType.Sprint, this.season);
		final Date firstSwimEver = athlete.getFirstSwimEver(stroke, EventType.Sprint); 
		final Date firstSwimForSeason = athlete.getFirstSwimForSeason(stroke, EventType.Sprint, this.season);
		final boolean couldSwimInFirstHalf = athlete.couldSwimInFirstHalf(stroke, EventType.Sprint, this.season);
		
		if ((firstSwimEver == null) && (raceCount25m + raceCount50m) > 0) {
			throw new RuntimeException("Have swims but no firstSwimDate");  // NB I can't see how it is possible for firstSwimEver to be null but we have some swims this year.
		}
			
		if ((firstSwimForSeason == null) && (raceCount25m + raceCount50m) > 0) {
			throw new RuntimeException("Have swims but no firstSwimDateForSeason");  // NB I can't see how it is possible for firstSwimForSeason to be null but we have some swims this year.
		}
			
		return  new EligibilityResult(athlete, eligibleEvent, raceCount25m, raceCount50m, numberOfRequiredSwims, firstSwimEver, firstSwimForSeason, couldSwimInFirstHalf, sprintDistance.getNote());
	}
}
