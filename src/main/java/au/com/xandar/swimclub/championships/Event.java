package au.com.xandar.swimclub.championships;

/**
 * Represents a stroke at a particular distance.
 * 
 * @author william
 */
public final class Event implements Comparable<Event> {

	private final Stroke stroke;
	private final Integer distance;
	private final boolean is100mEvent;
	private final EventType eventType;
	
	@SuppressWarnings("boxing")
	public Event(Stroke stroke, int distance) {
		this.stroke = stroke;
		this.distance = distance;
		this.is100mEvent = (distance == 100);
		this.eventType = (distance == 100) ? EventType.Distance : EventType.Sprint;
	}
	
	public Stroke getStroke() {
		return this.stroke;
	}
	
	public Integer getDistance() {
		return this.distance;
	}

	public EventType getEventType() {
		return this.eventType;
	}
	
	/**
	 * Returns true if the event is a 100m event.
	 */
	public boolean is100mEvent() {
		return this.is100mEvent;
	}
	
	/**
	 * Returns true if the stroke is either Breast, Back, Free.
	 */
	public boolean isEasyStroke() {
		return "Breast".equals(this.stroke) || "Back".equals(this.stroke) || "Free".equals(this.stroke);
	}

	/**
	 * Returns true if the stroke is Fly.
	 */
	public boolean isHardStroke() {
		return "Fly".equals(this.stroke);
	}
	
	public Event newDistance(int newDistance) {
		return new Event(this.stroke, newDistance);
	}
	
	// Comparable
	
    public int compareTo(Event o) {
    	final int distanceCompare = this.distance.compareTo(o.distance);
    	if (distanceCompare != 0) {
    		return distanceCompare;
    	}
    	
    	return this.stroke.compareTo(o.stroke);
    }
	
    // Object
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Event)) return false;
		final Event other = (Event) o;
		if (!this.stroke.equals(other.stroke)) return false;
		if (!this.distance.equals(other.distance)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return this.stroke.hashCode() + this.distance.hashCode() * 13;
	}
	
	@Override
	public String toString() {
		return this.distance + "m " + this.stroke;
	}
}
