package au.com.xandar.swimclub.championships;

/**
 * Respresents the valid age groups for an event.
 *
 * User: william
 * Date: 19/04/2009
 * Time: 8:29:09 PM
 */
public final class AgeGroup implements Comparable<AgeGroup> {

    private final Integer lower;
    private final Integer upper;

    public AgeGroup(Integer lower, Integer upper) {
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public int compareTo(AgeGroup other) {
        if (lower != null) {
            if (other.lower == null) return 1; // value after X and under.
            final int lowerCompare = lower.compareTo(other.lower);
            if (lowerCompare != 0) return lowerCompare;
        } else if (other.lower != null) {
            return -1; // X and under before any other value. 
        }

        if (upper != null) {
            if (other.upper == null) return -1; // value before X and over.
            final int upperCompare = upper.compareTo(other.upper);
            if (upperCompare != 0) return upperCompare;
        } else if (other.upper != null) {
            return 1; // X and over after any other value. 
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AgeGroup ageGroup = (AgeGroup) o;

        if (lower != null ? !lower.equals(ageGroup.lower) : ageGroup.lower != null) return false;
        if (upper != null ? !upper.equals(ageGroup.upper) : ageGroup.upper != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lower != null ? lower.hashCode() : 0;
        result = 31 * result + (upper != null ? upper.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (lower == null) {
            return upper.toString() + " & under";
        }
        if (upper == null) {
            return lower.toString() + " & over";
        }
        if (lower.equals(upper)) {
            return lower.toString();
        }
        return lower.toString() + "-" + upper.toString(); 
    }
}
