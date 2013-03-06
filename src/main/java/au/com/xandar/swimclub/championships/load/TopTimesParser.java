package au.com.xandar.swimclub.championships.load;

import au.com.xandar.parsing.CSVLineParser;
import au.com.xandar.swimclub.championships.Athlete;
import au.com.xandar.swimclub.championships.Event;
import au.com.xandar.swimclub.championships.EventTally;
import au.com.xandar.swimclub.championships.Stroke;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Parses the TeamManager TopTimes CSV file and collates a tally for each event for every athlete.
 * <p>
 * The aim is to determine for which events an athlete is eligible
 * and how many more swims are required in order to guarantee eligibility.  
 * </p>
 * 
 * @author william
 */
public final class TopTimesParser {

	private static final Logger LOGGER = Logger.getLogger(TopTimesParser.class);

    // Offsets within line (1 based).
    private static final int ATHLETE_NAME = 12;
    private static final int EVENT_TIME = 15;
    private static final int DISTANCE = 22;
    private static final int STROKE = 23;
    private static final int DATE = 25;

    private static final String NO_SHOW = "NS";
	private static final String DID_NOT_FINISH = "DNF";
	private static final String DISQUALIFIED = "DQ";
	
	public List<Athlete> parseSeasonalSpreadSheet(File inputFile) throws IOException {

        int lineNr = 1;
		final AthleteCollection athletes = new AthleteCollection();
		final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
		try {
			final CSVLineParser lineParser = new CSVLineParser();
			while (true) {
				
				final String line = reader.readLine();
				if (LOGGER.isDebugEnabled()) LOGGER.debug("Read line :" + line);
				if (line == null) {
					break; // EOF so stop
				}
				
				// Use LineParser to tokenize CSV line into ArrayList of elements (or Map of useful elements).
				final List<String> tokens = lineParser.getParsedContents(line);

				final String name = tokens.get(ATHLETE_NAME);
				final String eventTime = tokens.get(EVENT_TIME);
				final String distance = tokens.get(DISTANCE);
				final String stroke = tokens.get(STROKE);
				final String dateString = tokens.get(DATE);

                if (LOGGER.isDebugEnabled()) LOGGER.debug("Name='" + name + "' eventTime=" +eventTime + " distance=" + distance + " stroke=" + stroke + " date=" + dateString);

                if (NO_SHOW.equals(eventTime)) {
					continue; // Ie NO_SHOWS don't count towards ClubChampionships qualifications
				}
				
				// Every other swim (including disqualifications) do count.
				final Athlete athlete = athletes.getAthleteEventTally(name);
				final Event event = new Event(Stroke.valueOf(stroke), Integer.parseInt(distance));
				final EventTally eventTally = athlete.getEventTally(event);
				
				final Date dateSwum;
				try {
					dateSwum = dateFormat.parse(dateString);
				} catch (ParseException e) {
					throw new RuntimeException("Could not parse date '" + dateString + "'", e);
				}
				
				final double raceTime;
				if (DID_NOT_FINISH.equals(eventTime) || DISQUALIFIED.equals(eventTime)) {
					// Count it with a Maximum RaceTime. NB will not be able to parse the time.
					raceTime = Double.MAX_VALUE;
				} else {
					raceTime = this.getTimeInSeconds(lineNr, eventTime);
				}
				
				eventTally.incrementTally(dateSwum, raceTime);
				
				if (LOGGER.isDebugEnabled()) LOGGER.debug("Athlete:" + athlete + " event:" + event + " date: " + dateSwum + " raceTime:" + raceTime + "s tally:" + eventTally.getTally());
                lineNr++;
			}
		} finally {
			reader.close();
		}
		
		return athletes.getAthleteEventTallys();
	}
	
	private double getTimeInSeconds(int lineNr, String raceTime) {
        try {
            final int colonPos = raceTime.indexOf(':');
            if (colonPos == -1) {
                // time is less than one minute
                return Double.parseDouble(raceTime.trim());
            }

            final String minuteString = raceTime.substring(0, colonPos);
            final int minutes = Integer.parseInt(minuteString.trim());

            final String secondString = raceTime.substring(colonPos + 1);
            final double seconds = Double.parseDouble(secondString.trim());

            return minutes * 60 + seconds;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error parsing lineNr#" + lineNr + " - could not convert '" + raceTime + "' into seconds", e);
        }
	}
}
