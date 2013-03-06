package au.com.xandar.swimclub.championships.calculator;

import java.util.Collection;

import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.EligibilityResult;
import au.com.xandar.swimclub.championships.Season;


/**
 * Processes the eligibility for all Events for an Athlete for the current season.
 * 
 * @author william
 */
public interface EligibilityProcessor {

	/**
	 * Returns the Season for which Eligibility will be calculated.
	 */
	public Season getSeason();
	
	/**
	 * Returns the Collection of EligibilityResult for Athlete for the current season. 
	 */
	public Collection<EligibilityResult> getEligibilityResults(Athlete athlete);
}
