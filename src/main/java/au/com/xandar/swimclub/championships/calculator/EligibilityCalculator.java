package au.com.xandar.swimclub.championships.calculator;


import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.EligibilityResult;
import au.com.xandar.swimclub.championships.Stroke;

public interface EligibilityCalculator {

	/**
	 * Returns EligibilityResult for the Athlete for a particular Stroke.
	 */
	public EligibilityResult caculateEligibility(Athlete athlete, Stroke stroke);

}