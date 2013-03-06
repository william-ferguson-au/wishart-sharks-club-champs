package au.com.xandar.swimclub.championships;


public enum Stroke {
	
	// In order of swims for Championships.
	Back(28, 45, false, 10),
	
	Breast(30, 48, false, 10),
	
	IM(0, 0, true, 0) {
		/**
		 * @return true if theAthlete is qualified to swim the 100m event in this stroke.
		 * 		This requires them to be eligible for all four 50m events.
		 */
		@Override
		public boolean isQualifiedFor100m(Athlete athlete) {
			final boolean qualifiedFor50mBack = Back.isQualifiedFor50m(athlete);
			final boolean qualifiedFor50mBreast = Breast.isQualifiedFor50m(athlete);
			final boolean qualifiedFor50mFly = Fly.isQualifiedFor50m(athlete);
			final boolean qualifiedFor50mFree = Free.isQualifiedFor50m(athlete);
			return qualifiedFor50mBack && qualifiedFor50mBreast && qualifiedFor50mFly && qualifiedFor50mFree;
		}
	},
	
    Fly(26, 0, false, 12),

	Free(23, 38, false, 10);
	
	private final double fiftyMetreQualifyingTime;
	private final double hundredMetreQualifyingTime;
	private final boolean distanceOnlyEvent;
	private int startAgeFor50mEvents;
	
	private Stroke(double fiftyMetreQualifyingTime, double hundredMetreQualifyingTime, boolean distanceOnlyEvent, int startAgeFor50mEvents) {
		this.fiftyMetreQualifyingTime = fiftyMetreQualifyingTime;
		this.hundredMetreQualifyingTime = hundredMetreQualifyingTime;
		this.distanceOnlyEvent = distanceOnlyEvent;
		this.startAgeFor50mEvents = startAgeFor50mEvents;
	}

    /**
     * Using the Athlete's age and PBs for 25m and 50m, determines what sprint distance they should compete in at championships.
     * <p>
     * 9 and under should do 25m events for Breaststroke, Backstroke and Freestyle. 11 and under for Butterfly.
     * </p>
     * <p>
     * If an older kids has swum any 50m event and has either a
     * <ul>
     *  <li>25m PB less than the 50m qualifying time</li>
     *  <li>50m PB less than twice the 50m qualifying time</li>
     * </ul>
     * then they should swim 50m, else 25m.
     * </p>
     *
     * @param athlete   Athlete for whom to determine the correct sprint Event.
     * @return The SprintDistance that the given Athlete should swim at championships.
     */
    public SprintDistance getSprintDistanceForChampionships(Athlete athlete) {
        if (athlete.getAge() < this.startAgeFor50mEvents) {
            return SprintDistance.getValidSprintDistance(new Event(this, 25)); // Young swimmer. They should swim 25m at Club Championships.
        }

        // This is an older kid, who should be swimming 50m by now.
        final Double twentyFiveMetrePB = athlete.getPersonalBest(new Event(this, 25));
        final Double fiftyMetrePB = athlete.getPersonalBest(new Event(this, 50));
        if (fiftyMetrePB == null) {

            if ((twentyFiveMetrePB != null) && (twentyFiveMetrePB < fiftyMetreQualifyingTime)) {
                // Never swum 50m, but qualified for 50. Let them swim 25m, but add note reminding them that they have qualified for 50m.
                return SprintDistance.getSprintDistanceForSwimmerThatHasQualified(new Event(this, 25));
            }

            // Never swum 50m, and has not qualified for 50m. So let them swim 25m.
            return SprintDistance.getSprintDistanceForSlowerSwimmerThatHasNotSwum50m(new Event(this, 25));
        }

        // Has swum at least one 50m event for this stroke. They should swim 50m in the club champs.
        return SprintDistance.getValidSprintDistance(new Event(this, 50));
    }

	/**
     * @param athlete   Athlete for which to determine qualification for 50m. 
	 * @return true if the Athlete is qualified to swim the 50m event in this stroke.
	 */
	public boolean isQualifiedFor50m(Athlete athlete) {
		final Double twentyFiveMetrePB = athlete.getTwentyFiveMetrePersonalBest(this);
		if (twentyFiveMetrePB == null) {
            return false; // no 25m or 50m time.
		}
		return twentyFiveMetrePB < this.fiftyMetreQualifyingTime;
	}
	
	/**
     * @param athlete   Athlete for which to determine qualification for the 100m event. 
	 * @return true if theAthlete is qualified to swim the 100m event in this stroke.
	 */
	public boolean isQualifiedFor100m(Athlete athlete) {
		final Double fiftyMetrePB = athlete.getPersonalBest(new Event(this, 50));
		if (fiftyMetrePB == null) {
			return false; // No fifty metre result, so can't have qualified for 100m.
		}
		return fiftyMetrePB < this.hundredMetreQualifyingTime;
	}
	
	/**
	 * @return true if this Stroke is only swum as a distance event.
	 */
	public boolean isDistanceOnlyEvent() {
		return this.distanceOnlyEvent;
	}
	
	/**
	 * Returns the first age at which 50m events are swum at championships.
	 * <p>
	 * A 50m event must be swum if the Athlete has a PB better than the qualifying time for 50m.
	 * </p>
     *
     * @return The age at which athletes start swimming 50m of this Stroke at championships.
	 */
	public int getStartAgeFor50mEvents() {
		return this.startAgeFor50mEvents;
	}
}
