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
    private static boolean tofile;
    private static Vector logVector = new Vector();
    
    
   
    static {
        timer = new IterativeTimer();
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

    public static void setFileloggingEnabled(boolean enabled) {
        tofile = enabled;
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
            if (tofile) {
                logVector.addElement(output);
            }
            }
        }
    private static void vectorDump(Vector vector) {
        try {
            //create and open a file
            FileConnection file = (FileConnection) Connector.open("file:///" + "matchlog", Connector.WRITE);
            PrintStream writer = new PrintStream(file.openDataOutputStream());
            //write each vector element
            String l;
            Enumeration e = logVector.elements();
            while (e.hasMoreElements()) {
                l = (String) e.nextElement();
                writer.println(l);
            }
            //close the file
            writer.close();
            file.close();
        }
        catch (IOException e){
            //log error messages
            Logger.error("logger.vectorDump", "Error dumping shot log!");
            Logger.error("logger.vectorDump", e.getMessage());
        }
    }
}

