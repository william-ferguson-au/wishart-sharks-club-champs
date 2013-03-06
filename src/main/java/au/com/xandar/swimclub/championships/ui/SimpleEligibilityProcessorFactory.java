package au.com.xandar.swimclub.championships.ui;

import au.com.xandar.swimclub.championships.MissedNight;
import au.com.xandar.swimclub.championships.Season;
import au.com.xandar.swimclub.championships.calculator.EligibilityProcessor;
import au.com.xandar.swimclub.championships.calculator.EligibilityProcessorFactory;
import au.com.xandar.swimclub.championships.calculator.EligibilityProcessorMark2;
import au.com.xandar.swimclub.championships.calculator.ProcessorConstructionException;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Constructs an EligibilityProcessor for the current Season. 
 * 
 * @author william
 */
final class SimpleEligibilityProcessorFactory implements EligibilityProcessorFactory {

    private final JTextField seasonStartDate;
    private final JTextField seasonEndDate;
    private final Collection<MissedNight> missedNights;
    
	public SimpleEligibilityProcessorFactory(JTextField seasonStartDate, JTextField seasonEndDate, Collection<MissedNight> missedNights) {
		this.seasonStartDate = seasonStartDate;
		this.seasonEndDate = seasonEndDate;
		this.missedNights = missedNights;
	}
	
	public EligibilityProcessor create() throws ProcessorConstructionException {
		
		final DateFormat dateFormatter = new SimpleDateFormat();
		final Date startDate;
		final Date endDate;
		
		try {
			startDate = dateFormatter.parse(this.seasonStartDate.getText());
		} catch (ParseException e) {
			throw new ProcessorConstructionException("Could not convert StartDate '" + this.seasonStartDate.getText() + "' into a Date", e);
		}
		
		try {
			endDate = dateFormatter.parse(this.seasonEndDate.getText());
		} catch (ParseException e) {
			throw new ProcessorConstructionException("Could not convert EndDate '" + this.seasonEndDate.getText() + "' into a Date", e);
		}
		
		return new EligibilityProcessorMark2(new Season(startDate, endDate), this.missedNights);
	}
}
