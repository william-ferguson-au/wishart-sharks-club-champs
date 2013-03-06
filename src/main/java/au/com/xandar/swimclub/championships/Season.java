package au.com.xandar.swimclub.championships;

import java.util.Date;

/**
 * Represents a swimming season.
 * 
 * @author william
 */
public final class Season {

	private final Date start;
	private final Date midSeason;
	private final Date finish;
	
	public Season(Date start, Date finish) {
		this.start = start;
		this.finish = finish;
		this.midSeason = new Date((start.getTime() + finish.getTime()) / 2);
	}
	
	/**
	 * Returns the Season start Date.
	 */
	public Date getStartDate() {
		return this.start;
	}
	
	/**
	 * Returns the Season finish Date.
	 */
	public Date getFinishDate() {
		return this.finish;
	}
	
	/**
	 * Returns true if the given Date is in the first half of the season.
	 */
	public boolean isInFirstHalf(Date date) {
		return date.after(this.start) && date.before(this.midSeason);
	}
	
	/**
	 * Returns true if the given Date is before the half-way point of the Season.
	 * This includes if it is in prior Seasons.
	 */
	public boolean isBeforeHalfWay(Date date) {
		return date.before(this.midSeason);
	}
	
	/**
	 * Returns true if the given Date is in the second half of the season.
	 */
	public boolean isInSecondHalf(Date date) {
		return date.after(this.midSeason) && date.before(this.finish);
	}
}
