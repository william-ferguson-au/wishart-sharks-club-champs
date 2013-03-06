package au.com.xandar.swimclub.championships;

/**
 * Represents the sprint distance that a swimmer should swim at championships and any notes on why that is so.
 * <p>
 *
 * </p>
 * User: William
 * Date: 05/04/2010
 * Time: 5:49:15 PM
 */
public final class SprintDistance {

    private final Event event;
    private final String note;

    private SprintDistance(Event event, String note) {
        this.event = event;
        this.note = note;
    }

    public Event getEvent() {
        return event;
    }

    public String getNote() {
        return note;
    }

    public static SprintDistance getValidSprintDistance(Event event) {
        return new SprintDistance(event, "");
    }

    /**
     * @param event
     * @return SprintDistance for an older swimmer who has swum 50m but is still a little slow over 25m (ie has not qualified for 50m)..
     */
    public static SprintDistance getSprintDistanceForSlowerSwimmerThatHasNotSwum50m(Event event) {
        return new SprintDistance(event, "Older swimmer. Not yet qualified for 50m. May choose to swim 50m at club champs.");
    }


    /**
     * @param event
     * @return SprintDistance for an older swimmer who has swum 50m but is still a little slow over 25m (ie has not qualified for 50m)..
     */
    public static SprintDistance getSprintDistanceForSlowerSwimmerThatHasSwum50m(Event event) {
        return new SprintDistance(event, "Older swimmer. Swum but not yet qualified for 50m. May choose to swim 50m at club champs.");
    }

    /**
     * @param event
     * @return SprintDistance for an older swimmer who has NEVER swum 50m but has qualified for 50m. They should really be swimming 50m.
     */
    public static SprintDistance getSprintDistanceForSwimmerThatHasQualified(Event event) {
        return new SprintDistance(event, "CONSIDER SWIMMING 50m! Older swimmer. Qualified for but not swum 50m. May choose to swim 50m at club champs.");
    }
}
