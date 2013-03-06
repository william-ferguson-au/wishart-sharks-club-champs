package au.com.xandar.swimclub.championships;

import java.util.Date;

/**
 * Represents a club night that was cancelled.
 * 
 * @author william
 */
public final class MissedNight {

	private final Date date;
	private final Stroke openHundred;
	private final Stroke qualifiedHundred;

	public MissedNight(Date date, Stroke open, Stroke qualified) {
		this.date = date;
		this.openHundred = open;
		this.qualifiedHundred = qualified;
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public Stroke getOpenHundred() {
		return this.openHundred;
	}
	
	public Stroke getQualifiedHundred() {
		return this.qualifiedHundred;
	}
	
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("MissedNight{date=");
		buf.append(this.date);
		buf.append(" open=");
		buf.append(this.openHundred);
		buf.append(" qualified=");
		buf.append(this.qualifiedHundred);
		buf.append("}");
		return buf.toString();
	}
}
