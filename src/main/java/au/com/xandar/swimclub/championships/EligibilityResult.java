package au.com.xandar.swimclub.championships;

import java.util.Date;

public final class EligibilityResult implements Comparable<EligibilityResult> {

	private final Athlete athlete;
	private final Event event;
	private final Integer primaryTally;
	private final Integer secondaryTally;
	private final Integer requiredSwims;
	
	private final Date firstSwimEver;
	private final Date firstSwimThisSeason; // First time this Season that this event was swum. NB Sprints will be first time either 25, or 50m was swum.

    private final String sprintDistanceNote;

	private int credittedNights = 0;
	private boolean couldSwimInFirstHalf;
	
	public EligibilityResult(Athlete athlete, Event event, int primaryTally, int secondaryTally, int requiredSwims, Date firstSwimEver, Date firstSwimThisSeason, boolean couldSwimInFirstHalf) {
        this(athlete, event, primaryTally, secondaryTally, requiredSwims, firstSwimEver, firstSwimThisSeason, couldSwimInFirstHalf, "");
	}
	

    public EligibilityResult(Athlete athlete, Event event, int primaryTally, int secondaryTally, int requiredSwims, Date firstSwimEver, Date firstSwimThisSeason, boolean couldSwimInFirstHalf, String sprintDistanceNote) {
        this.athlete = athlete;
        this.event = event;
        this.primaryTally = primaryTally;
        this.secondaryTally = secondaryTally;
        this.requiredSwims = requiredSwims;
        this.firstSwimEver = firstSwimEver;
        this.firstSwimThisSeason = firstSwimThisSeason;
        this.couldSwimInFirstHalf = couldSwimInFirstHalf;
        this.sprintDistanceNote = sprintDistanceNote;
    }

	public Athlete getAthlete() {
		return this.athlete;
	}
	
	public Event getEvent() {
		return this.event;
	}

    /**
     * @return true if this Athlete is eligible for this event.
     */
	public boolean getEligible() {
		return this.primaryTally + this.secondaryTally + this.credittedNights >= this.requiredSwims;
	}

	public Integer getPrimaryTally() {
		return this.primaryTally;
	}
	
	public Integer getSecondaryTally() {
		return this.secondaryTally;
	}
	
	public Date getFirstSwimEver() {
		return this.firstSwimEver;
	}
	
	public Date getFirstSwimThisSeason() {
		return this.firstSwimThisSeason;
	}
	
	public Integer getNrCredittedNights() {
		return new Integer(this.credittedNights);
	}
	
	public void incrementCredittedNights() {
		this.credittedNights++;
	}
	
	@SuppressWarnings("boxing")
	public Integer getTotalEligibleSwims() {
		return this.primaryTally + this.secondaryTally + this.credittedNights;
	}
	
	@SuppressWarnings("boxing")
	public Integer getExtraSwimsRequired() {
		final int extraSwimsRequired = this.requiredSwims - this.getTotalEligibleSwims();
		return extraSwimsRequired < 0 ? 0 : extraSwimsRequired;
	}
	
	public Integer getRequiredSwims() {
		return this.requiredSwims;
	}

	/**
	 * @return true if was capable of swimming this event in the first half of the season.
	 * This is assumed to mean that 
	 * 	1) They had joined in the first half of the season
	 * 	2) They either swam this event in first half of season or have swum it in a prior season.
	 */
	public boolean getCouldSwimInFirstHalf() {
		return this.couldSwimInFirstHalf;
	}

    /**
     * @return Note indicating what if any special circumstances surround the decision over sprint distance for this EligibilityResult.  
     */
    public String getSprintDistanceNote() {
        return sprintDistanceNote;
    }

    // Comparable
	
    public int compareTo(EligibilityResult o) {
    	return this.event.compareTo(o.event);
    }
	
    // Object
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof EligibilityResult)) return false;
		final EligibilityResult other = (EligibilityResult) o;
		if (!this.event.equals(other.event)) return false;
		if (!this.athlete.equals(other.athlete)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return this.event.hashCode() + this.athlete.hashCode() * 13;
	}
	
	@Override
	public String toString() {
		final String eligDetails = this.getEligible() + " NrSwims:" + this.getTotalEligibleSwims() + " SwimsReq:" + this.requiredSwims; 
		return "EligibilityResult(Event:" + this.event + " Eligible:" + eligDetails + ")"; 
	}
}
