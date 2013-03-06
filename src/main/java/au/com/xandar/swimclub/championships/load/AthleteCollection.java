package au.com.xandar.swimclub.championships.load;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;

import au.com.xandar.swimclub.championships.Athlete;

/**
 * Responsible for maintaining the collection of AtheleteEventTally records.
 * 
 * @author william
 */
final class AthleteCollection {

	/**
	 * Transforms a name into an {@link Athlete}.
	 */
	private static final class AthleteNameTransformer implements Transformer {

		public Object transform(Object input) {
			final String name = (String) input;
			return new Athlete(name);
		}

	}	
	
	@SuppressWarnings({ "unchecked", "synthetic-access" })
	private final Map<String, Athlete> map = LazyMap.decorate(new HashMap<String, Athlete>(), new AthleteNameTransformer());
	
	public Athlete getAthleteEventTally(String athleteName) {
		return this.map.get(athleteName);
	}
	
	public List<Athlete> getAthleteEventTallys() {
		final List<Athlete> sortedAthletes = new ArrayList<Athlete>();
		sortedAthletes.addAll(this.map.values());
		Collections.sort(sortedAthletes);
		return sortedAthletes;
		
	}
}
