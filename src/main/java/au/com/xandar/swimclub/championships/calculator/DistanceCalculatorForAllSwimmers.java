package au.com.xandar.swimclub.championships.calculator;

import java.util.Date;

import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.EligibilityResult;
import au.com.xandar.swimclub.championships.Event;
import au.com.xandar.swimclub.championships.EventType;
import au.com.xandar.swimclub.championships.Season;
import au.com.xandar.swimclub.championships.Stroke;

/**
 * Determines ElilgibilityResults for distance events for all swimmers.
 * 
 * @author william
 *
 */
public final class DistanceCalculatorForAllSwimmers implements EligibilityCalculator {

	private final Season season;
	
	DistanceCalculatorForAllSwimmers(Season season) {
		this.season = season;
	}
	
	public EligibilityResult caculateEligibility(Athlete athlete, Stroke stroke) {
		
		final int raceCount100m = athlete.getRaceCount(new Event(stroke, 100), this.season);
		
		// Determine required number of swims.
		final int numberOfRequiredSwims = athlete.getNumberOfRequiredSwims(stroke, EventType.Distance, this.season);
		final Date firstSwimEver = athlete.getFirstSwimEver(stroke, EventType.Distance); 
		final Date firstSwimForSeason = athlete.getFirstSwimForSeason(stroke, EventType.Distance, this.season);
		final boolean couldSwimInFirstHalf = athlete.couldSwimInFirstHalf(stroke, EventType.Distance, this.season);  
		
		final EligibilityResult eligibilityResult =  new EligibilityResult(
				athlete, 
				new Event(stroke, 100),
				raceCount100m, 0, 
				numberOfRequiredSwims,
				firstSwimEver,
				firstSwimForSeason,
				couldSwimInFirstHalf);
		
		return eligibilityResult;
	}
}
