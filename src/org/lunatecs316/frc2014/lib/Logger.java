package org.lunatecs316.frc2014.lib;

import java.util.Vector;
import com.sun.squawk.microedition.io.FileConnection;
import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Enumeration;
/**
 * Helper class for logging information to console output
 * @author Domenic Rodriguez
 */
public class Logger {
    /**
     * Class to represent the detail level of the logger
     */
    public static class Level {
        public static final Level INFO = new Level("INFO", 1);
        public static final Level WARNING = new Level("WARNING", 2);
        public static final Level ERROR = new Level("ERROR", 3);
        public static final Level DEBUG = new Level("DEBUG", 4);

        private String m_name;
        private int m_value;

        private Level(String name, int value) {
            m_name = name;
            m_value = value;
        }

        public String toString() {
            return m_name;
        }

        public int getValue() {
            return m_value;
        }
    }

    private static IterativeTimer timer;
    private static Level currentLevel;
    private static boolean toFile;
    private static Vector messages;
    
    static {
        timer = new IterativeTimer();
        messages = new Vector();
        currentLevel = Level.DEBUG;
    }

    /**
     * Set the detail currentLevel of the logger
     * @param l the detail currentLevel
     */
    public static void setLevel(Level l) {
        currentLevel = l;
    }

    /**
     * Print an informative message
     * @param context contextual information on the location of the program
     * @param message the message to print
     */
    public static void info(String context, String message) {
        log(Level.INFO, context, message);
    }

    /**
     * Print a warning message
     * @param context contextual information on the location of the program
     * @param message the message to print
     */
    public static void warning(String context, String message) {
        log(Level.WARNING, context, message);
    }

    /**
     * Print an error message
     * @param context contextual information on the location of the program
     * @param message the message to print
     */
    public static void error(String context, String message) {
        log(Level.ERROR, context, message);
    }

    /**
     * Print a debugging message
     * @param context contextual information on the location of the program
     * @param message the message to print
     */
    public static void debug(String context, String message) {
        log(Level.DEBUG, context, message);
    }

    /**
     * Enable or disable file logging
     * @param enabled
     */
    public static void enableFileLogging(boolean enabled) {
        toFile = enabled;
    }
    
    /**
     * Perform the actual logging operation
     * @param level the severity of the message
     * @param context contextual information on the location of the program
     * @param message the message to print
     */
    private static void log(Level level, String context, String message) {
        if (currentLevel.getValue() >= level.getValue()) {
            String output = timer.getValue() + " [" + context + "] " + level + ": " + message;
            if (level == Level.WARNING || level == Level.ERROR)
                System.err.println(output);
            else
                System.out.println(output);

            if (toFile) {
                messages.addElement(output);
            }
        }
    }

    /**
     * Write the log data to a file
     */
    public static void writeToFile() {
        FileConnection file = null;
        PrintStream writer = null;

        try {
            // Open the file
            file = (FileConnection) Connector.open("file:///logs/match" + timer.getValue() + ".log", Connector.WRITE);
            writer = new PrintStream(file.openDataOutputStream());
            
            // Write each vector element
            Enumeration e = messages.elements();
            while (e.hasMoreElements()) {
                String msg = (String) e.nextElement();
                writer.println(msg);
            }
            messages.removeAllElements();
        } catch (IOException e){
            Logger.error("Logger.writeToFile", e.getMessage());
        } finally {
            try {
                if (writer != null)
                    writer.close();
                if (file != null)
                    file.close();
            } catch (IOException ex) {
                Logger.error("Logger.writeToFile", "Error closing file");
            }
        }
    }
}

