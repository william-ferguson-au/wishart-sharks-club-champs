package au.com.xandar.swimclub.championships.eligibility;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.apache.log4j.Logger;

import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.EligibilityResult;
import au.com.xandar.swimclub.championships.Event;
import au.com.xandar.swimclub.championships.calculator.EligibilityProcessor;

/**
 * Responsible for writing an Athlete's events to a file.
 *  
 * @author william
 */
public final class AthleteFormatter {

	private static final Logger LOGGER = Logger.getLogger(AthleteFormatter.class);
	
	private final PrintWriter writer;
	private int i = 0;
	
	public AthleteFormatter(PrintStream stream) {
		this.writer = new PrintWriter(stream);
	}
	
	@SuppressWarnings("boxing")
	public void render(Athlete athlete, EligibilityProcessor processor) {

        if (athlete.getAge() > 18) {
            if (LOGGER.isInfoEnabled()) LOGGER.info("Athlete: " + athlete + " ineligible because they are older than 18");
            return;
        }

        final Date assumedJoinDate = athlete.getDateJoined(processor.getSeason());
        if (assumedJoinDate == null) {
            if (LOGGER.isInfoEnabled()) LOGGER.info("Athlete: " + athlete + " ineligible because they have not swum ANY swims this season");
            return;
        }

		this.writer.printf("%1$3d %2$-22s (%3$2d) %4$s AssumedJoinDate: %5$td-%5$tb-%5$tY",
				this.i++, athlete.getAthleteName(), athlete.getAge(), athlete.getGender(), assumedJoinDate);
        this.writer.println();
		this.writer.flush();

		if (LOGGER.isInfoEnabled()) LOGGER.info("Athlete: " + athlete);

		for (EligibilityResult eligibilityResult : processor.getEligibilityResults(athlete)) {
			
			if (LOGGER.isDebugEnabled()) LOGGER.debug("   " + eligibilityResult);
			if (eligibilityResult.getTotalEligibleSwims() == 0) {
				continue; // Only print events for which an Athlete has at least one swim.
			}
			
			final Event event = eligibilityResult.getEvent();
			final Date firstSwimForSeason = eligibilityResult.getFirstSwimThisSeason();
			final Date firstSwimEver = eligibilityResult.getFirstSwimEver();
			
			this.writer.printf("Nominated [   ]   %1$-6s  %2$4d   ", event.getStroke(), event.getDistance());
			this.writer.printf("Req(%1$1d)", eligibilityResult.getRequiredSwims());
			
			final StringWriter swims = new StringWriter();
			final PrintWriter swimsWriter = new PrintWriter(swims);
			
			if (event.is100mEvent()) {
				swimsWriter.printf("%1$2d=(%2$2d*100m+%3$2dcr)                   ", 
						eligibilityResult.getTotalEligibleSwims(),
						eligibilityResult.getPrimaryTally(),
						eligibilityResult.getNrCredittedNights()
					);
			} else {
				swimsWriter.printf("%1$2d=(%2$2d*25m+%3$2d*50m+%5$2dcr) 25mPB:%4$5.2fs", 
						eligibilityResult.getTotalEligibleSwims(),
						eligibilityResult.getPrimaryTally(),
						eligibilityResult.getSecondaryTally(),
						athlete.getTwentyFiveMetrePersonalBest(event.getStroke()),
						eligibilityResult.getNrCredittedNights()
					);
			}
			swimsWriter.flush();
			
			this.writer.printf("   %1$-17s   Swims:%2$-24s First:%3$td-%3$tb-%3$ty  Season:%4$td-%4$tb-%4$ty  ", 
					(eligibilityResult.getEligible() ? "Eligible" : "Not Yet Eligible"),
					swims.toString(),
					firstSwimEver,
					firstSwimForSeason);
			
			if (!eligibilityResult.getEligible()) {
				// Not eligible so indicate how many extra swims are required.
				final Integer extraSwimsRequired = eligibilityResult.getRequiredSwims() - eligibilityResult.getTotalEligibleSwims();
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
