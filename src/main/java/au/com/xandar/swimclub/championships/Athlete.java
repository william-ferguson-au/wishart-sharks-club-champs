package au.com.xandar.swimclub.championships;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazySortedMap;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Responsible for maintaining a record of how many time an athlete has swum each event.
 * 
 * @author william
 */
public final class Athlete implements Comparable<Athlete> {

	private static final Logger LOGGER = Logger.getLogger(Athlete.class);
	
	/**
	 * Transforms an Event into an EventTally.
	 */
	private static final class EventTransformer implements Transformer {

		public Object transform(Object input) {
			final Event event = (Event) input;
			return new EventTally(event);
		}
	}
	
	private final String lastNameFirstName;
	private final Integer age;
	private String gender;
	
	@SuppressWarnings({ "unchecked", "synthetic-access" })
	private final Map<Event, EventTally> events = LazySortedMap.decorate(new TreeMap<Event, EventTally>(), new EventTransformer());
	
	public Athlete(String fullName) {
        try {
            final int startBracket = fullName.indexOf("(");
            final int endBracket = fullName.indexOf(")");
            final String ageString = fullName.substring(startBracket + 1, endBracket);
            this.age = Integer.valueOf(ageString);

            final String name = fullName.substring(0, startBracket).trim();
            final int firstSpaceInName = name.indexOf(" ");
            final String firstName = name.substring(0, firstSpaceInName);
			final String lastName = name.substring(firstSpaceInName + 1);
            lastNameFirstName = lastName + ", " + firstName;

            this.gender = fullName.substring(endBracket + 1).trim();
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Athlete='" + this.lastNameFirstName + "' gender='" + this.gender + "' fullName='" + fullName + "'");
        } catch (RuntimeException e) {
            LOGGER.error("Failure creating Athlete - Athlete#fullName='" + fullName + "'");
            throw e;
        }
	}
	
	public String getAthleteName() {
		return this.lastNameFirstName;
	}
	
	public Integer getAge() {
		return this.age;
	}
	
	public String getGender() {
		return this.gender;
	}
	
	public EventTally getEventTally(Event event) {
		return this.events.get(event);
	}
	
	/**
	 * @return the number of swims by this Athlete for the given Event and Season.
	 */
	public int getRaceCount(Event event, Season season) {
		// Get the EventTally for a particular Event (stroke/distance) 
		// and ask it for the number of races in the given season.
		final EventTally tally = this.events.get(event);
		return tally.getRaces(season);
	}

    /**
     * The lower of this Athlete's best time over 25m or half their PB over 50m will be returned.
     * <p>
     * Including half the 50m time, covers those kids that have jumped into swimming 50m early
     * and may not have a recent 25m time, but have a decent 50m time. So if there were to swim 25m
     * you would expect they could at least achieve half their 50m time.
     * </p>
     *
     * @param stroke    Stroke for which to determine this Athlete's best time.
     * @return this Athlete's best time for the given Stroke over 25m.
     */
    public Double getTwentyFiveMetrePersonalBest(Stroke stroke) {
        final Double twentyFiveMetrePB = this.getPersonalBest(new Event(stroke, 25));
        final Double fiftyMetrePB = this.getPersonalBest(new Event(stroke, 50));
        if ((fiftyMetrePB != null) && (twentyFiveMetrePB != null)) {
            return Math.min(twentyFiveMetrePB, fiftyMetrePB / 2);
        } else if (fiftyMetrePB != null) {
            return fiftyMetrePB / 2;
        } else if (twentyFiveMetrePB != null) {
            return twentyFiveMetrePB;
        }

        return null; // This means that they have no 25m or 50m swims for the Stroke.
    }

	/**
	 * @return this Athlete's best time for the given Event.
	 */
	Double getPersonalBest(Event event) {
		final EventTally tally = this.events.get(event);
		final Double eventPB = tally.getPersonalBest();
        if (eventPB == null & event.getDistance().equals(25)) {
            // No 25m time so return half of the 50m time.
            final EventTally fiftyMetreTally = this.events.get(new Event(event.getStroke(), 50));
            final Double fiftyMetrePB = fiftyMetreTally.getPersonalBest();
            if (fiftyMetrePB != null) {
                return fiftyMetrePB / 2;
            }
        }
        return eventPB;
	}

	/**
	 * @return true if this Athlete was capable of swimming the event in the first half of the season.
     *
     * Capable is deemed to mean that they joined the club in the first half of the season and have either
     * swum the event before or swam the event in the first half of the season.
	 */
	public boolean couldSwimInFirstHalf(Stroke stroke, EventType eventType, Season season) {
		// Determine the date deemed to be when this Athlete joined this Season.
        final Date dateJoined = this.getDateJoined(season);
        if (dateJoined == null) {
            return false; // If they never joined this season then they could certainly never have sum the event on the first half.
        }

		// If that seasonStart date is in the second half of the season then they could not have swum this event in the first half.
		if (season.isInSecondHalf(dateJoined)) {
			return false;
		}
		
		// Get the date this athlete first swam this event ever.
		// If that date is before season half-way then they definitely could have swum it in the first half of the given season.
		final Date firstSwum = this.getFirstSwimEver(stroke, eventType);
		return firstSwum == null ? false : season.isBeforeHalfWay(firstSwum);
	}
	
	/**
	 * @return the very first Date (if any) in which this Athlete has swum the type of event (sprint/distance) in the given stroke. 
	 */
	public Date getFirstSwimEver(Stroke stroke, EventType eventType) {
		return eventType.getFirstDate(stroke, this.events);
	}
	
	/**
	 * @return the first Date this Season (if any) in which this Athlete has swum the type of event (sprint/distance) in the given stroke. 
	 */
	public Date getFirstSwimForSeason(Stroke stroke, EventType eventType, Season season) {
		return eventType.getFirstDateForSeason(stroke, this.events, season);
	}
	
	/**
	 * @return the number of swims in sprints (25m and 50m) required to be eligible for championships in the given stroke.
	 */
	public int getNumberOfRequiredSwims(Stroke stroke, EventType eventType, Season season) {
		return this.couldSwimInFirstHalf(stroke, eventType, season) ? eventType.getMinFullSeasonSwims() : eventType.getMinHalfSeasonSwims();
	}
	
	/**
	 * @return the first time ANY race was swum this season by this Athlete, null if no race has been swum.
     *  This is deemed to be the date at which this Athlete joined the club this season.
	 */
	public Date getDateJoined(Season season) {
		Date firstSwim = null;
		for (final EventTally tally : this.events.values()) {
			final Date firstRaceForEventThisSeason = tally.getFirstRaceForSeason(season);
			if (firstSwim == null) {
				// This is first time we have found a swim so make that the FirstSwim.  
				firstSwim = firstRaceForEventThisSeason;
			} else if (firstRaceForEventThisSeason != null) {
				// Update FirstSwim if its earlier that the first swim for the current EventTally
				firstSwim = firstSwim.before(firstRaceForEventThisSeason) ? firstSwim : firstRaceForEventThisSeason; 
			}
		}
        if (firstSwim == null) {
            LOGGER.warn("Could not determine DateJoined for Athlete : " + this.lastNameFirstName);
			LOGGER.debug("Could not determine DateJoined for Athlete : " + this.lastNameFirstName + " - events: " + this.events);
        }
		return firstSwim;
	}
	
    public int compareTo(Athlete o) {
    	return this.lastNameFirstName.compareTo(o.lastNameFirstName);
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o == null) return false;
        if (!(o instanceof Athlete)) return false;
    	final Athlete other = (Athlete) o;
    	return this.lastNameFirstName.equals(other.lastNameFirstName);
    }
    
    @Override
    public int hashCode() {
    	return this.lastNameFirstName.hashCode();
    }
    
    @Override
    public String toString() {
    	return this.lastNameFirstName;
    }
}
