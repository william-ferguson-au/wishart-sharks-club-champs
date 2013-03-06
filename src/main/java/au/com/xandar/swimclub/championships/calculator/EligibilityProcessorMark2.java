package au.com.xandar.swimclub.championships.calculator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.EligibilityResult;
import au.com.xandar.swimclub.championships.Event;
import au.com.xandar.swimclub.championships.MissedNight;
import au.com.xandar.swimclub.championships.Season;
import au.com.xandar.swimclub.championships.Stroke;
import au.com.xandar.swimclub.championships.eligibility.EligibilityComparator;


/**
 * Processes the eligibility for all Events for an Athlete for the current season.
 * 
 * @author william
 */
public final class EligibilityProcessorMark2 implements EligibilityProcessor {

	private static final Logger LOGGER = Logger.getLogger(EligibilityProcessorMark2.class);
	
	private final Season season;
	private final EligibilityCalculator sprintCalcForYoungerSwimmers;
	private final EligibilityCalculator sprintCalcForOlderSwimmers;
	private final EligibilityCalculator distanceCalcForAllSwimmers;
	private final Collection<MissedNight> missedNights;
	
	public EligibilityProcessorMark2(Season season, Collection<MissedNight> missedNights) {
		this.season = season;
		this.sprintCalcForYoungerSwimmers = new SprintCalculatorForYoungerSwimmers(this.season);
		this.sprintCalcForOlderSwimmers = new SprintCalculatorForOlderSwimmers(this.season);
		this.distanceCalcForAllSwimmers = new DistanceCalculatorForAllSwimmers(this.season);
		this.missedNights = missedNights;
	}

	/**
	 * Returns the Season for which Eligibility will be calculated.
	 */
	public Season getSeason() {
		return this.season;
	}
	
	/**
	 * Returns the EligibilityResults for all strokes for the given Athlete, sorted into sprints and then distance.
	 */
	public Collection<EligibilityResult> getEligibilityResults(Athlete athlete) {
		
		final Map<Event, EligibilityResult> results = new HashMap<Event, EligibilityResult>();
		
		// Get sprint results - determine whether younger or older swimmer (for stroke).
		for (final Stroke stroke : Stroke.values()) {
			if (stroke.isDistanceOnlyEvent()) {
				continue;
			}

			if (LOGGER.isDebugEnabled()) LOGGER.debug(" Stroke:" + stroke);
			final EligibilityCalculator calculator = athlete.getAge() < stroke.getStartAgeFor50mEvents() ? this.sprintCalcForYoungerSwimmers : this.sprintCalcForOlderSwimmers;
			final EligibilityResult result = calculator.caculateEligibility(athlete, stroke);
			
			final StringWriter stringWriter = new StringWriter();
			final PrintWriter writer = new PrintWriter(stringWriter);
			writer.printf("Athlete:%1$-30s  %2$-50s", athlete, result);
			if (LOGGER.isDebugEnabled()) LOGGER.debug(stringWriter.toString());
			
			results.put(result.getEvent(), result);
		}
		
		// Get distance results
		for (final Stroke stroke : Stroke.values()) {
			final EligibilityResult result = this.distanceCalcForAllSwimmers.caculateEligibility(athlete, stroke);
			results.put(result.getEvent(), result);
		}
		
		
		// TODO Iterate over MissedNights and credit them to swimmers who deserve them.
		for (final MissedNight missedNight : this.missedNights) {
			final boolean missedNightInSecondHalf = this.season.isInSecondHalf(missedNight.getDate());
			
			// Handle the 2 distance events and then then 3/4 sprint events. Only 3 if the qualified hundred counted.
			
			// Get the EligibilityResult (if any) for the Open Hundred Event and credits it if appropriate
			final Event openDistanceEvent = new Event(missedNight.getOpenHundred(), 100);
			if (results.containsKey(openDistanceEvent)) {
				final EligibilityResult openHundredResult = results.get(openDistanceEvent);
				// If the Athlete has swum this at least once this season 
				if (openHundredResult.getTotalEligibleSwims() > 0) {
					// and would reasonably have swum this event then increment the MissedNights on the EligibilityResult.
					// NB Use a lenient criteria to determine whether they would have been around to swim it.
					if (openHundredResult.getCouldSwimInFirstHalf() || missedNightInSecondHalf) {
						openHundredResult.incrementCredittedNights();
					}
				}
			}
			
			// Get the EligibilityResult (if any) for the Qualified Hundred Event and credits it if appropriate
			Stroke creditedQualifiedHundred = null;
			final Event qualifiedDistanceEvent = new Event(missedNight.getQualifiedHundred(), 100);
			if (results.containsKey(qualifiedDistanceEvent)) {
				final EligibilityResult qualifiedHundredResult = results.get(qualifiedDistanceEvent);
				// If the Athlete has swum this at least once this season and is qualified to swim the hundred metre event.
				if ((qualifiedHundredResult.getTotalEligibleSwims() > 0) && qualifiedDistanceEvent.getStroke().isQualifiedFor100m(athlete)) {
					// would reasonably have swum this event then increment the MissedNights on the EligibilityResult
					// NB use a lenient criteria to determine whether they would have been around to swim it.
					if (qualifiedHundredResult.getCouldSwimInFirstHalf() || missedNightInSecondHalf) {
						// Only count the swim towards the qualified hundred if not already eligible.
						// This allows the Athlete who has already qualified for the hundred to count the swim towards their qualification for the sprints.
						if (!qualifiedHundredResult.getEligible()) {
							qualifiedHundredResult.incrementCredittedNights();
							creditedQualifiedHundred = qualifiedDistanceEvent.getStroke();
						}
					}
				}
			}
			
			// Get the EligibilityResult (if any) for the Sprint Events and credit them if appropriate.
			for (Stroke stroke : Stroke.values()) {
				if (stroke.isDistanceOnlyEvent()) {
					continue; // Only want sprint events.
				}
				final EligibilityResult result = results.containsKey(new Event(stroke, 25)) ? results.get(new Event(stroke, 25)) : results.get(new Event(stroke, 50));
				if (result == null) {
					continue; // No sprint swim for this stroke.
				}
				if (result.getEvent().getStroke().equals(creditedQualifiedHundred)) {
					continue; // if we have already credited for qualified hundred we cannot also credit for sprint.
				}
				
				// If the Athlete has swum this at least once this season then increment the MissedNights on the EligibilityResult
				if (result.getTotalEligibleSwims() > 0) {
					if (result.getCouldSwimInFirstHalf() || missedNightInSecondHalf) {
						result.incrementCredittedNights();
					}
				}
			}
		}
		
		final List<EligibilityResult> sortedResults = new ArrayList<EligibilityResult>();
		sortedResults.addAll(results.values());
		Collections.sort(sortedResults, new EligibilityComparator());
		
		return sortedResults;
	}
}
