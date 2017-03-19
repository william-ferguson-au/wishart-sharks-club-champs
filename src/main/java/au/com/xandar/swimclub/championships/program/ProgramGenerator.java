package au.com.xandar.swimclub.championships.program;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import org.apache.log4j.Logger;

import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.EligibilityResult;
import au.com.xandar.swimclub.championships.calculator.EligibilityProcessor;

/**
 * Produces a report listing the potential events and eligible swimmers for each event. 
 * 
 * @author william
 */
public final class ProgramGenerator {

	private static final Logger LOGGER = Logger.getLogger(ProgramGenerator.class);
	
    private final EligibilityProcessor processor;
    
    public ProgramGenerator(EligibilityProcessor processor) {
    	this.processor = processor;
    }
    
    /**
     * Writes a file containing the championships program based on current athlete eligibility.
     * 
     * @param outputFile	File to which the program will be written.
     * @param athletes		Athletes to include in the program.
     * @throws IOException if the program File cannot be written.
     */
	public void generateProgram(File outputFile, File inputFile, Collection<Athlete> athletes) throws IOException {
		
		// Iterate over Athletes and build up a Collection of events with each event holding a collection of AthleteResults.
		final ProgramEventCollection programEventCollection = new ProgramEventCollection();
		for (final Athlete athlete : athletes) {

            if (athlete.getDateJoined(processor.getSeason()) == null) {
                // Ignore Athlete if they never joined this year (ie if they never swam any events).
                continue;
            }

			for (final EligibilityResult result : this.processor.getEligibilityResults(athlete)) {
				
				// Find (or add) ProgramEvent in ProgramEventCollection.
				final ProgramEvent programEvent = programEventCollection.getProgramEvent(new ProgramEvent(result.getEvent(), athlete.getAge(), athlete.getGender()));
				
				// Add Athlete to ProgramEvent.
				programEvent.addAthlete(result);
			}
		}
		
		// Open outputFile ready for writing.
		final PrintStream outStream = new PrintStream(new FileOutputStream(outputFile));
		final Collection<ProgramEvent> programEvents = programEventCollection.getProgramEvents();
		LOGGER.info("Nr ProgramEvents = " + programEvents.size());
		
		// Iterate over ProgramEvents and print each Event.
		outStream.printf("Club Championships eligibility based on %s", inputFile.getName());
        outStream.println();
        outStream.println();
		final ProgramEventFormatter formatter = new ProgramEventFormatter(outStream);
		for (final ProgramEvent programEvent : programEvents) {
			formatter.render(programEvent);
			LOGGER.info(programEvent);
		}
		outStream.close();
		LOGGER.info("ProgramEventsReport complete");
	}
}