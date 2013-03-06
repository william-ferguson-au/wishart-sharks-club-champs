package au.com.xandar.swimclub.championships;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Responsible for keeping a tally of how many times an Event has been swum.
 *  
 * @author william
 */
public final class EventTally {

	private final Event event;
	private SortedSet<Date> raceDates = new TreeSet<Date>();
	private Double personalBest;
		
	public EventTally(Event event) {
		this.event = event;
	}
	
	public Event getEvent() {
		return this.event;
	}
	
	public Integer getTally() {
		return new Integer(this.raceDates.size());
	}
	
	/**
	 * Returns the best time for this Event.
	 */
	public Double getPersonalBest() {
		return this.personalBest;
	}
	
	/**
	 * Returns the number of races swum in this Event for the given Season.
	 */
	public int getRaces(Season season) {
		int counter = 0;
		for (final Date raceDate : this.raceDates) {
			if (raceDate.after(season.getStartDate()) && raceDate.before(season.getFinishDate())) {
				counter++;
			}
		}
		return counter;
	}
	
	/**
	 * Returns the Date of the first race, ever.
	 */
	public Date getFirstRace() {
		if (this.raceDates.isEmpty()) {
			return null;
		}
		return this.raceDates.first();
	}

	/**
	 * Returns the Date of the first race this Season. 
	 */
	public Date getFirstRaceForSeason(Season season) {
		for (final Date raceDate : this.raceDates) {
			if (raceDate.after(season.getStartDate())) {
				// Safety check to make sure that the date falls within the season.
				return raceDate.after(season.getFinishDate()) ? null : raceDate;
			}
		}
		return null;
	}
	
	/**
	 * Increment tally and record the date the Event was first swum.
	 */
	public void incrementTally(Date dateSwum, double raceTime) {
		this.raceDates.add(dateSwum);
		if (this.personalBest == null) {
			this.personalBest = new Double(raceTime);
		} else if (raceTime < this.personalBest.doubleValue()) {
			this.personalBest = new Double(raceTime);
		}
	}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EventTally{nrRaces=");
        sb.append(raceDates.size());
        if (!raceDates.isEmpty()) {
            sb.append(", firstRace=");
            sb.append(raceDates.first());
            sb.append(", lastRace=");
            sb.append(raceDates.last());
            sb.append(", pb=");
            sb.append(personalBest);
        }
        sb.append("}");
        return sb.toString();
    }
}
