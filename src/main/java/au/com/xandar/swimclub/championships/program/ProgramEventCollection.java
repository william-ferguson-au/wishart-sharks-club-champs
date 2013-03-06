package au.com.xandar.swimclub.championships.program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;

/**
 * Responsible for maintaining the collection of Program Event records.
 * 
 * @author william
 */
final class ProgramEventCollection {

	/**
	 * Returns the supplied ProgramEVent.
	 */
	private static final class InputTransformer implements Transformer {

		public Object transform(Object input) {
			return input;
		}
	}	
	
	@SuppressWarnings({ "unchecked", "synthetic-access" })
	private final Map<ProgramEvent, ProgramEvent> map = LazyMap.decorate(new HashMap<ProgramEvent, ProgramEvent>(), new InputTransformer());
	
	public ProgramEvent getProgramEvent(ProgramEvent event) {
		return this.map.get(event);
	}
	
	public List<ProgramEvent> getProgramEvents() {
		final List<ProgramEvent> sortedEvents = new ArrayList<ProgramEvent>();
		sortedEvents.addAll(this.map.values());
		Collections.sort(sortedEvents, new ProgramEventComparator());
		return sortedEvents;
	}
}
