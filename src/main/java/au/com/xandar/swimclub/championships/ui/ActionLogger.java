package au.com.xandar.swimclub.championships.ui;

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Decorates an ActionListener and logs when it starts, finishes and it there is an error.
 */
final class ActionLogger implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(ActionLogger.class);
    private final ActionListener listener;

    public ActionLogger(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Starting action for " + this.listener);
            listener.actionPerformed(e);
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Finished action for " + this.listener);
        } catch (RuntimeException ex) {
            LOGGER.error("Failed to perform action: " + this.listener, ex);
        }
    }
}
