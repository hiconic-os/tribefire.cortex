package tribefire.cortex;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingTools {
	private static final Logger logger = Logger.getLogger(LoggingTools.class.getName());

	public static Level toJulLevel(String levelName) {
		if (levelName == null) {
			return null;
		}

		switch (levelName.trim().toUpperCase(Locale.ROOT)) {
			case "TRACE":
				return Level.FINEST;
			case "DEBUG":
				return Level.FINE;
			case "INFO":
				return Level.INFO;
			case "WARN":
				return Level.WARNING;
			case "ERROR":
			case "FATAL":
				return Level.SEVERE;
			default:
				return null;
		}
	}

	public static String toLogLevel(Level julLevel) {
		if (julLevel == null) {
			return null;
		}

		if (julLevel.equals(Level.FINEST) || julLevel.equals(Level.FINER)) {
			return "TRACE";
		}
		if (julLevel.equals(Level.FINE)) {
			return "DEBUG";
		}
		if (julLevel.equals(Level.INFO)) {
			return "INFO";
		}
		if (julLevel.equals(Level.WARNING)) {
			return "WARN";
		}
		if (julLevel.equals(Level.SEVERE)) {
			return "ERROR";
		}

		return null;
	}
	
	static {
		LogConfigLoader.load();
		logger.setLevel(Level.FINER);
		
		try (Reader reader = new InputStreamReader(new FileInputStream(new File("res/log-levels.properties")))) {
			Properties properties = new Properties();
			properties.load(reader);
			
			for (Map.Entry<Object, Object> entry: properties.entrySet()) {
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();
				
				Level julLevel = toJulLevel(value);
				
				Logger.getLogger(key).setLevel(julLevel);
			}
		}
		catch (IOException e) {
			logger.log(Level.SEVERE, "Error while reading log-levels.properties", e);
		}
	}

    public static Map<String, Level> getLogLevels() {
    	
        LogManager logManager = LogManager.getLogManager();
        
        Logger.getLogger(LoggingTools.class.getName()).setLevel(Level.FINEST);

        // Sorted map with proper structured comparator
        SortedMap<String, Level> explicitLevels = new TreeMap<>(new StructuredPackageComparator());

        Enumeration<String> loggerNames = logManager.getLoggerNames();
        while (loggerNames.hasMoreElements()) {
            String name = loggerNames.nextElement();
            Logger logger = logManager.getLogger(name);
            if (logger != null && logger.getLevel() != null) {
                explicitLevels.put(name, logger.getLevel());
            }
        }

        // Print the result
        explicitLevels.forEach((loggerName, level) -> {
            String displayName = loggerName.isEmpty() ? "" : loggerName;
            System.out.println(displayName + " = " + level.getName());
        });
        
        return explicitLevels;
    }

    static class StructuredPackageComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            // Root logger (empty string) always sorts first
            if (a.isEmpty()) return b.isEmpty() ? 0 : -1;
            if (b.isEmpty()) return 1;

            String[] partsA = a.split("\\.");
            String[] partsB = b.split("\\.");

            int len = Math.min(partsA.length, partsB.length);
            for (int i = 0; i < len; i++) {
                int cmp = partsA[i].compareTo(partsB[i]);
                if (cmp != 0) {
                    return cmp;
                }
            }
            // Shorter package comes first (shallower hierarchy)
            return Integer.compare(partsA.length, partsB.length);
        }
    }
}
