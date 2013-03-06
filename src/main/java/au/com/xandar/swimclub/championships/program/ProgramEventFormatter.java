package au.com.xandar.swimclub.championships.program;

import java.io.PrintStream;
import java.io.PrintWriter;

import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.EligibilityResult;

/**
 * Responsible for writing a ProgramEvent to a file.
 *  
 * @author william
 */
final class ProgramEventFormatter {

	private final PrintWriter writer;
	private int i = 1;
	
	public ProgramEventFormatter(PrintStream stream) {
		this.writer = new PrintWriter(stream);
	}
	
	@SuppressWarnings("boxing")
	public void render(ProgramEvent event) {
		
		if (!event.hasEligibleAthletes()) {
			return; // If no eligible Athlete then don't print event.
		}

		int athleteCounter = 1;
		this.writer.printf("Event %1$d   %2$dm  %3$s   Age(%4$s)  %5$s", 
				this.i++, 
				event.getDistance(), 
				event.getStroke(), 
				event.getAgeGroup(),
				event.getGender());
        this.writer.println();
		
		for (EligibilityResult eligibilityResult : event.getEligibilityResults()) {
			
			if (eligibilityResult.getTotalEligibleSwims() == 0) {
				continue; // Only print events for which an Athlete has at least one swim.
			}
			
			final Athlete athlete = eligibilityResult.getAthlete();
			this.writer.printf("  %5$2d   %1$-25s  Age(%2$2d)  Swims(%3$2d)  Req(%4$2d)  ",
					athlete.getAthleteName(),
					athlete.getAge(),
					eligibilityResult.getTotalEligibleSwims(),
					eligibilityResult.getRequiredSwims(),
					athleteCounter++
					);
			
			if (!eligibilityResult.getEligible()) {
				// Not eligible so indicate how many more swims are required.
				final Integer extraSwimsRequired = eligibilityResult.getExtraSwimsRequired();
				this.writer.printf("Requires %1$d more swim%2$-3s", extraSwimsRequired, extraSwimsRequired == 1 ? "" : "s");
            } else {
                this.writer.printf("%1$23s", ""); // Pad eligible events out to match extra swims requirement text above.
            }
            this.writer.printf("%1$s", eligibilityResult.getSprintDistanceNote());

			this.writer.println();
		}
		this.writer.println();
		this.writer.flush();
	}
}
