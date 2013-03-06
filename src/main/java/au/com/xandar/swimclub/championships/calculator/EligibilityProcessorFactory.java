package au.com.xandar.swimclub.championships.calculator;



public interface EligibilityProcessorFactory {

	/**
	 * @return EligiilityProcessor that can be used to determine an Athlete's eligibility for the current season.
	 * @throws ProcessorConstructionException if the EligibilityProcess cannot be constructed.
	 */
	public EligibilityProcessor create() throws ProcessorConstructionException;

}