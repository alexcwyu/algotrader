package com.unisoft.algotrader;

import com.unisoft.algotrader.model.event.data.Bar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by alex on 6/7/15.
 */
public class LoggerTest {   // Define a static logger variable so that it references the
    // Logger instance named "LoggerTest".
    private static final Logger LOG = LogManager.getLogger(LoggerTest.class);

    public static void main(final String... args) {

        // Set up a simple configuration that logs on the console.

        LOG.trace("Entering application.");
        Bar bar = new Bar();
        LOG.info("bar {}", bar);
        LOG.trace("Exiting application.");
    }
}