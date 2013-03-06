package au.com.xandar.swimclub.championships;

import java.util.Date;
import java.util.Map;

public enum EventType {
	
    // NB Distance needs to come first because the Championship Program uses an EventType comparison to put distance before the sprints.
    Distance(3, 2) {
        @Override
        public Date getFirstDate(Stroke stroke, Map<Event, EventTally> events) {
            final EventTally tally100 = events.get(new Event(stroke, 100));
            return tally100 == null ? null : tally100.getFirstRace();
        }

        @Override
        public Date getFirstDateForSeason(Stroke stroke, Map<Event, EventTally> events, Season season) {
            final EventTally tally100 = events.get(new Event(stroke, 100));
            return tally100 == null ? null : tally100.getFirstRaceForSeason(season);
        }

        /**
         * 16 and over complete together, as do 9 and under.
         * Otherwise its year against year.
         *
         * @Override
         */
        public AgeGroup getAgeGroup(Integer age) {
            if (age.intValue() <= 7) {
                return new AgeGroup(null, new Integer(7));
            } else if (age.intValue() >= 16) {
                return new AgeGroup(new Integer(16), null);
            }

            return new AgeGroup(age, age);
        }
    },

	Sprint(8, 4) {
		@Override
		public Date getFirstDate(Stroke stroke, Map<Event, EventTally> events) {
			final EventTally tally25 = events.get(new Event(stroke, 25));
			final EventTally tally50 = events.get(new Event(stroke, 50));
			if (tally25 == null) {
				return tally50 == null ? null : tally50.getFirstRace();
			} else if (tally50 == null) {
				return tally25.getFirstRace();
			}
			
			// have both 25 and 50m races.
			final Date tally25Date = tally25.getFirstRace();
			final Date tally50Date = tally50.getFirstRace();
			return tally25Date == null ? tally50Date :
				(tally50Date == null ? tally25Date :
					(tally25Date.before(tally50Date) ? tally25Date : tally50Date)
				);
		}
		
		@Override
		public Date getFirstDateForSeason(Stroke stroke, Map<Event, EventTally> events, Season season) {
			final EventTally tally25 = events.get(new Event(stroke, 25));
			final EventTally tally50 = events.get(new Event(stroke, 50));
			if (tally25 == null) {
				return tally50 == null ? null : tally50.getFirstRaceForSeason(season);
			} else if (tally50 == null) {
				return tally25.getFirstRace();
			}
			
			// have both 25 and 50m races.
			final Date tally25Date = tally25.getFirstRaceForSeason(season);
			final Date tally50Date = tally50.getFirstRaceForSeason(season);
			return tally25Date == null ? tally50Date :
				(tally50Date == null ? tally25Date :
					(tally25Date.before(tally50Date) ? tally25Date : tally50Date)
				);
		}

        /**
         * 16 and over complete together, as do 4 and under.
         * Otherwise its year against year.
         *
         * @Override
         */
        public AgeGroup getAgeGroup(Integer age) {
            if (age.intValue() <= 4) {
                return new AgeGroup(null, new Integer(4));
            } else if (age.intValue() >= 16) {
                return new AgeGroup(new Integer(16), null);
            }

            return new AgeGroup(age, age);
        }
	};

	private final int minimumFullSeasonSwims;
	private final int minimumHalfSeasonSwims;
	
	private EventType(int fullSeasonSwims, int halfSeasonSwims) {
		this.minimumFullSeasonSwims = fullSeasonSwims;
		this.minimumHalfSeasonSwims = halfSeasonSwims;
	}
	
	public int getMinFullSeasonSwims() {
		return this.minimumFullSeasonSwims;
	}
	
	public int getMinHalfSeasonSwims() {
		return this.minimumHalfSeasonSwims;
	}
	
	/**
	 * @param stroke	
	 * @param events	Map of Event to EventTally for an Athlete.
	 * @return Date that this EventType was first swum by an Athlete for the given Stroke.
	 */
	public abstract Date getFirstDate(Stroke stroke, Map<Event, EventTally> events);
	
	/**
	 * @param stroke	
	 * @param events	Map of Event to EventTally for an Athlete.
	 * @param season	
	 * @return Date that this EventType was first swum by an Athlete for the given Stroke in the given Season.
	 */
	public abstract Date getFirstDateForSeason(Stroke stroke, Map<Event, EventTally> events, Season season);

    /**
     * @param age   Age of the athlete.
     * @return AgeGroup in which an athlete of this age will compete in this EventType.
     */
    public abstract AgeGroup getAgeGroup(Integer age);
}
