package au.com.xandar.swimclub.championships.calculator;

import java.util.Date;

import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.EligibilityResult;
import au.com.xandar.swimclub.championships.Event;
import au.com.xandar.swimclub.championships.EventType;
import au.com.xandar.swimclub.championships.Season;
import au.com.xandar.swimclub.championships.Stroke;

/**
 * Determines ElilgibilityResults for sprint events for swimmers who would swim 25m events at Championships.
 * <p>
 * This is under 10 for Backstroke, Breatstroke and Freestyle and under 12 for Butterfly.
 * </p>
 * 
 * @author william
 *
 */
public final class SprintCalculatorForYoungerSwimmers implements EligibilityCalculator {

	private final Season season;
	
	SprintCalculatorForYoungerSwimmers(Season season) {
		this.season = season;
	}
	
	/**
	 * Returns EligibilityResults for the sprint event for the Athlete for the given stroke.
	 */
	public EligibilityResult caculateEligibility(Athlete athlete, Stroke stroke) {
		
		final int raceCount25m = athlete.getRaceCount(new Event(stroke, 25), this.season);
		final int raceCount50m = athlete.getRaceCount(new Event(stroke, 50), this.season);
		
		// Determine required number of swims.
		final int numberOfRequiredSwims = athlete.getNumberOfRequiredSwims(stroke, EventType.Sprint, this.season);
		final Date firstSwimEver = athlete.getFirstSwimEver(stroke, EventType.Sprint); 
		final Date firstSwimForSeason = athlete.getFirstSwimForSeason(stroke, EventType.Sprint, this.season);
		final boolean couldSwimInFirstHalf = athlete.couldSwimInFirstHalf(stroke, EventType.Sprint, this.season);  
		
		final EligibilityResult eligibilityResult =  new EligibilityResult(
				athlete, 
				new Event(stroke, 25),
				raceCount25m, 
				raceCount50m, 
				numberOfRequiredSwims, 
				firstSwimEver,
				firstSwimForSeason,
				couldSwimInFirstHalf);
		
		return eligibilityResult;
	}
}
