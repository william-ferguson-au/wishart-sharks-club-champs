package au.com.xandar.swimclub.championships.program;

import java.util.ArrayList;
import java.util.List;

import au.com.xandar.swimclub.championships.EligibilityResult;
import au.com.xandar.swimclub.championships.Event;
import au.com.xandar.swimclub.championships.Stroke;
import au.com.xandar.swimclub.championships.AgeGroup;

/**
 * Responsible for collating the Athletes eligible for an event.
 * 
 * @author william
 */
final class ProgramEvent implements Comparable<ProgramEvent> {

	private final Event event;
    private final AgeGroup ageGroup;
	private final String gender;
	private final List<EligibilityResult> eligibilityResults = new ArrayList<EligibilityResult>();
	
	public ProgramEvent(Event event, Integer age, String gender) {
		this.event = event;
        this.ageGroup = event.getEventType().getAgeGroup(age);
		this.gender = gender;
	}
	
	public Event getEvent() {
		return this.event;
	}
	
	public Stroke getStroke() {
		return this.event.getStroke();
	}
	
	public Integer getDistance() {
		return this.event.getDistance();
	}
	
	public final AgeGroup getAgeGroup() {
		return this.ageGroup;
	}
	
	public String getGender() {
		return this.gender;
	}
	
	public void addAthlete(EligibilityResult result) {
		this.eligibilityResults.add(result);
	}
	
	public List<EligibilityResult> getEligibilityResults() {
		return this.eligibilityResults;
	}
	
	/**
	 * @return true if at least one eligible Athlete.
	 */
	public boolean hasEligibleAthletes() {
		for (final EligibilityResult result : this.eligibilityResults) {
			if (result.getEligible()) {
				return true;
			}
		}
		return false;
	}
	
	// Comparable

	/**
	 * Natural sort is stroke, distance, age, then gender.
	 */
    public int compareTo(ProgramEvent o) {
    	final int strokeCompare = this.event.getStroke().compareTo(o.event.getStroke());
    	if (strokeCompare != 0) {
    		return strokeCompare;
    	}
    	
    	final int distanceCompare = this.event.getDistance().compareTo(o.event.getDistance());
    	if (distanceCompare != 0) {
    		return distanceCompare;
    	}
    	
    	final int ageCompare = this.ageGroup.compareTo(o.ageGroup);
    	if (ageCompare != 0) {
    		return ageCompare;
    	}
    	
    	final int genderCompare = this.gender.compareTo(o.gender);
    	if (genderCompare != 0) {
    		return genderCompare;
    	}
    	
    	return 0;
    }
    
    // Object
    
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ProgramEvent)) return false;
		final ProgramEvent other = (ProgramEvent) o;
		if (!this.event.equals(other.event)) return false;
		if (!this.ageGroup.equals(other.ageGroup)) return false;
		if (!this.gender.equals(other.gender)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return this.event.hashCode() + this.ageGroup.hashCode() * 13 + this.gender.hashCode() * 17;
	}
    
	
}
