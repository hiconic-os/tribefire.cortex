package tribefire.cortex;
import java.util.*;
import java.util.logging.*;

public class LoggingTools {
	private static final Logger logger = Logger.getLogger(LoggingTools.class.getName());
	
	static {
		LogConfigLoader.load();
		logger.setLevel(Level.FINER);
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
